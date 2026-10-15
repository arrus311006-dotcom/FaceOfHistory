package com.example.historyquizru.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.historyquizru.R
import com.example.historyquizru.data.AppDatabase
import com.example.historyquizru.data.PersonalityEntity
import com.example.historyquizru.data.QuizResultEntity
import com.example.historyquizru.databinding.ActivityQuizBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding

    private var questions: List<PersonalityEntity> = emptyList()
    private var currentOptions: List<PersonalityEntity> = emptyList()
    private var currentIndex = 0
    private var correctCount = 0
    private var wrongCount = 0
    private var livesLeft = 3
    private var isAnswered = false
    private var resultSaved = false
    private var timerJob: Job? = null
    private var epochId = 0
    private var userId = 0L
    private var userName = ""

    private val optionButtons
        get() = listOf(
            binding.btnOption1,
            binding.btnOption2,
            binding.btnOption3,
            binding.btnOption4
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        epochId = intent.getIntExtra(LoginActivity.EXTRA_EPOCH_ID, 0)
        userId = intent.getLongExtra(LoginActivity.EXTRA_USER_ID, 0L)
        userName = intent.getStringExtra(LoginActivity.EXTRA_USER_NAME).orEmpty()

        optionButtons.forEachIndexed { index, button ->
            button.setOnClickListener { onOptionSelected(index) }
        }
        binding.btnNext.setOnClickListener { onNextPressed() }

        loadQuestions()
    }

    private fun loadQuestions() {
        lifecycleScope.launch {
            val dao = AppDatabase.getInstance(applicationContext).personalityDao()
            questions = dao.getRandomByEpoch(epochId)

            if (questions.size < QUESTION_COUNT) {
                binding.tvHint.text = getString(R.string.quiz_no_questions)
                optionButtons.forEach { it.visibility = View.GONE }
                return@launch
            }

            currentIndex = 0
            correctCount = 0
            wrongCount = 0
            livesLeft = STARTING_LIVES
            resultSaved = false
            displayQuestion()
        }
    }

    private suspend fun displayQuestion() {
        if (currentIndex !in questions.indices) return

        isAnswered = false
        timerJob?.cancel()

        val question = questions[currentIndex]
        val wrongOptions = AppDatabase.getInstance(applicationContext)
            .personalityDao()
            .getRandomWrong(epochId, question.id)

        if (wrongOptions.size < 3) {
            binding.tvHint.text = getString(R.string.quiz_no_questions)
            optionButtons.forEach { it.visibility = View.GONE }
            return
        }

        currentOptions = (wrongOptions + question).shuffled()

        binding.tvQuestionNumber.text = getString(
            R.string.quiz_question_format,
            currentIndex + 1,
            QUESTION_COUNT
        )
        binding.tvLives.text = getString(R.string.quiz_lives).take(livesLeft)
        binding.tvTimer.text = TIMER_SECONDS.toString()
        binding.progressBar.max = TIMER_SECONDS
        binding.progressBar.progress = TIMER_SECONDS
        binding.tvHint.text = question.shortHint
        binding.tvFullDescription.text = question.fullDescription
        binding.tvFullDescription.visibility = View.GONE
        binding.btnNext.visibility = View.GONE

        binding.ivPersonality.load("file:///android_asset/${question.imageName}") {
            crossfade(true)
            placeholder(R.drawable.ic_placeholder)
            error(R.drawable.ic_error)
        }

        optionButtons.forEachIndexed { index, button ->
            button.visibility = View.VISIBLE
            button.text = currentOptions[index].name
            button.isEnabled = true
            button.backgroundTintList = null
        }

        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = lifecycleScope.launch {
            for (seconds in TIMER_SECONDS downTo 0) {
                if (isAnswered) return@launch
                binding.tvTimer.text = seconds.toString()
                binding.progressBar.progress = seconds

                if (seconds == 0) {
                    handleAnswer(selectedIndex = null)
                    return@launch
                }
                delay(1000)
            }
        }
    }

    private fun onOptionSelected(index: Int) {
        if (!isAnswered) {
            handleAnswer(index)
        }
    }

    private fun handleAnswer(selectedIndex: Int?) {
        if (isAnswered || currentOptions.isEmpty()) return

        isAnswered = true
        timerJob?.cancel()
        optionButtons.forEach { it.isEnabled = false }

        val question = questions[currentIndex]
        val correctIndex = currentOptions.indexOfFirst { it.id == question.id }

        if (selectedIndex == correctIndex) {
            correctCount++
        } else {
            wrongCount++
            livesLeft--
        }

        binding.tvLives.text = getString(R.string.quiz_lives).take(livesLeft.coerceAtLeast(0))

        val green = ContextCompat.getColor(this, R.color.color_correct)
        val red = ContextCompat.getColor(this, R.color.color_incorrect)

        optionButtons.forEachIndexed { index, button ->
            when {
                index == correctIndex -> button.backgroundTintList = ColorStateList.valueOf(green)
                index == selectedIndex -> button.backgroundTintList = ColorStateList.valueOf(red)
            }
        }

        binding.tvFullDescription.visibility = View.VISIBLE
        binding.btnNext.visibility = View.VISIBLE
    }

    private fun onNextPressed() {
        if (livesLeft <= 0 || currentIndex + 1 >= QUESTION_COUNT) {
            finishQuiz()
        } else {
            currentIndex++
            lifecycleScope.launch {
                displayQuestion()
            }
        }
    }

    private fun finishQuiz() {
        if (resultSaved) return
        resultSaved = true
        timerJob?.cancel()

        lifecycleScope.launch {
            AppDatabase.getInstance(applicationContext).quizResultDao().insert(
                QuizResultEntity(
                    userId = userId,
                    epochId = epochId,
                    correctCount = correctCount,
                    wrongCount = wrongCount,
                    date = System.currentTimeMillis()
                )
            )
            showResultDialog()
        }
    }

    private fun showResultDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.quiz_finished_title)
            .setMessage(
                getString(
                    R.string.quiz_result_format,
                    correctCount,
                    questions.size
                )
            )
            .setCancelable(false)
            .setPositiveButton(R.string.quiz_restart) { _, _ ->
                restartQuiz()
            }
            .setNegativeButton(R.string.to_main_menu) { _, _ ->
                val intent = Intent(this, MainMenuActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra(LoginActivity.EXTRA_USER_ID, userId)
                    putExtra(LoginActivity.EXTRA_USER_NAME, userName)
                }
                startActivity(intent)
                finish()
            }
            .show()
    }

    private fun restartQuiz() {
        timerJob?.cancel()
        questions = emptyList()
        currentOptions = emptyList()
        currentIndex = 0
        correctCount = 0
        wrongCount = 0
        livesLeft = STARTING_LIVES
        isAnswered = false
        resultSaved = false
        optionButtons.forEach { it.backgroundTintList = null }
        loadQuestions()
    }

    override fun onDestroy() {
        timerJob?.cancel()
        super.onDestroy()
    }

    companion object {
        private const val QUESTION_COUNT = 10
        private const val STARTING_LIVES = 3
        private const val TIMER_SECONDS = 20
    }
}