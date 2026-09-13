package com.ketchupzzz.analytical.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.ketchupzzz.analytical.R
import com.ketchupzzz.analytical.models.SchoolLevel
import com.ketchupzzz.analytical.models.Students
import com.ketchupzzz.analytical.models.StudentsWithSubmissions
import com.ketchupzzz.analytical.models.quiz.Levels
import com.ketchupzzz.analytical.models.submissions.AnswerSheet
import com.ketchupzzz.analytical.models.submissions.Submissions
import com.ketchupzzz.analytical.presentation.main.games.data.LevelsWithSubmissions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random


fun generateRandomString(size: Int = 10): String {
    val data : String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    return (1..size)
        .map { data[Random.nextInt(data.length)] }
        .joinToString("")
}


fun Int.getEndMinute() : String {
    val minutes = this / 60
    val seconds = this % 60
    val clock = String.format("%02d:%02d", minutes, seconds)
    return clock
}

fun List<AnswerSheet>.getEarnings(): Double {
    var count = 0.0
    this.forEach {
        if (it.correct) {
            count += it.points
        }
    }
    return "%.2f".format(count).toDouble()
}


fun Levels.getRating(answers: List<AnswerSheet>): Double {
    val maxScore = this.questions * this.points
    val score = answers.getEarnings()

    // get how many percentage he got
    return if (maxScore > 0) (score / maxScore) * 100 else 0.0
}

fun Context.toast(message  : String ) {
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}
fun String.shuffleString(): String {
    val characters = this.replace(" ","")
        .toList()
    val shuffledCharacters = characters.shuffled()
    return shuffledCharacters.joinToString("")
}



fun String.getSpacesIndices(): IntArray {
    val indices = mutableListOf<Int>()
    this.forEachIndexed { index, char ->
        if (char == ' ') {
            indices.add(index)
        }
    }
    return indices.toIntArray()
}
fun Double.getScoreMessage(): String {
    return when (this) {
        100.0 -> "Wow, perfect score! You absolutely nailed it! Your hard work has really paid off. Keep doing what you’re doing—this is amazing!"
        in 90.0..99.9 -> "Great job! You’re so close to perfection. Just a tiny bit more, and you’ll be at the top. Keep pushing—you’ve got this!"
        in 80.0..89.9 -> "Nice work! You’re doing really well. With just a little more effort, you can take it to the next level. Keep going strong!"
        in 70.0..79.9 -> "Good effort! You’re making solid progress. Keep practicing, and you’ll see even bigger improvements soon. You’re on the right track!"
        in 60.0..69.9 -> "Not bad at all! You’ve got a good grasp, but there’s definitely room to grow. A little more focus, and you’ll be crushing it in no time."
        in 50.0..59.9 -> "You’re getting there! You’ve shown potential, but it’s time to buckle down and sharpen those skills. Keep at it—you’re capable of more!"
        in 40.0..49.9 -> "Don’t worry, you’re learning! Improvement takes time, so keep at it. You’ll see better results with consistent practice. Keep your head up!"
        in 30.0..39.9 -> "Hey, don’t get discouraged! Everyone starts somewhere, and the fact that you’re trying means you’re already ahead. Stick with it—you’ll get better!"
        in 20.0..29.9 -> "It’s a tough start, but don’t let it get to you. Every attempt helps you grow, so keep going. You’ve got what it takes to improve!"
        in 10.0..19.9 -> "Keep your chin up! This is just the beginning. The more you practice, the better you’ll get. Don’t give up—things will start to click soon!"
        else -> "You’re on the path to getting better! It’s all about progress, not perfection. Every step forward counts, so keep going. You’ll see results with time!"
    }
}



fun List<LevelsWithSubmissions>.getMyCurrentLevel(): Int {

    for (index in indices) {
        val currentLevel = this[index]

        if (currentLevel.submissions.isEmpty()) return index + 1

        if (!currentLevel.isGreaterThanEqual80Percent()) return index + 1
    }
    return size + 1
}

fun LevelsWithSubmissions.isGreaterThanEqual80Percent(): Boolean {
    val maxScore = this.levels.points * this.levels.questions
    val submissions = this.submissions.sortedByDescending { it.performance.earning }
    val myScore = submissions.getOrNull(0)?.performance?.earning ?: 0.00
    return myScore >= (maxScore * 0.8)
}


fun List<LevelsWithSubmissions>.getNextLevel(currentLevelIndex: Int): Levels? {
    return this.drop(currentLevelIndex + 1)
        .firstOrNull()?.levels
}


fun Date.toDisplayDate(): String {
    val dateFormat = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
    return dateFormat.format(this)
}

fun Students.getStudentFullname() : String {
    val mname = this.mname?.getOrNull(0) ?: ""
    return "${this.fname} ${mname} ${this.lname}"
}



fun List<Levels>.toLevelWithSubmissions(submissions: List<Submissions>) : List<LevelsWithSubmissions> {
    val levelsWithSubmissions : MutableList<LevelsWithSubmissions> = mutableListOf()
    for (level in this) {
        levelsWithSubmissions.add(
            LevelsWithSubmissions(
                levels = level,
                submissions = submissions.filter { it.quizInfo?.levels?.id == level.id }
            )
        )
    }
   return levelsWithSubmissions.sortedBy { it.levels.levelNumber }
}

//Category.QUIZ_GAME -> "QUIZ GAME"
//Category.MEMORY_GAME -> "MEMORY GAME"
//Category.PUZZLE_GAME -> "PUZZLE GAME"
//Category.MATH_GAME -> "MATH GAME"
fun String.displayCategory(): String {
    return when (this) {
        "QUIZ_GAME" -> "Quiz Game"
        "MEMORY_GAME" -> "Memory Game"
        "PUZZLE_GAME" -> "Puzzle Game"
        "MATH_GAME" -> "Math Game"
        else -> "Unknown Category"
    }
}

fun Int.getHexBackground(): Int {
    return when ((this - 1) % 10 + 1) {
        1 -> R.drawable.hex_1
        2 -> R.drawable.hex_2
        3 -> R.drawable.hex_3
        4 -> R.drawable.hex_4
        5 -> R.drawable.hex_5
        6 -> R.drawable.hex_6
        7 -> R.drawable.hex_7
        8 -> R.drawable.hex_8
        9 -> R.drawable.hex_9
        10 -> R.drawable.hex_10
        else -> R.drawable.hex_1
    }
}


fun String.createLog(
    message: String
) {
    Log.d(this,message)
}

val mockLeaderboard = listOf(
    StudentsWithSubmissions(
        students = Students(
            id = "1",
            fname = "Mark",
            mname = "D",
            lname = "Zuckerberg",
            schoolLevel = SchoolLevel.GRADE_10,
            profile = "https://example.com/mark.png",
            email = "mark@gmail.com"
        ),
        submissions = emptyList(),
        totalPoints = 250.50
    ),
    StudentsWithSubmissions(
        students = Students(
            id = "2",
            fname = "Elon",
            mname = "M",
            lname = "Musk",
            schoolLevel = SchoolLevel.GRADE_12,
            profile = "https://example.com/elon.png",
            email = "elon@gmail.com"
        ),
        submissions = emptyList(),
        totalPoints = 210.00
    ),
    StudentsWithSubmissions(
        students = Students(
            id = "3",
            fname = "Bill",
            mname = "G",
            lname = "Gates",
            schoolLevel = SchoolLevel.GRADE_11,
            profile = "https://example.com/bill.png",
            email = "bill@gmail.com"
        ),
        submissions = emptyList(),
        totalPoints = 180.75
    ),
    StudentsWithSubmissions(
        students = Students(
            id = "4",
            fname = "Steve",
            mname = "P",
            lname = "Jobs",
            schoolLevel = SchoolLevel.GRADE_9,
            profile = "https://example.com/steve.png",
            email = "steve@gmail.com"
        ),
        submissions = emptyList(),
        totalPoints = 150.00
    ),
    StudentsWithSubmissions(
        students = Students(
            id = "5",
            fname = "Jeff",
            mname = "B",
            lname = "Bezos",
            schoolLevel = SchoolLevel.GRADE_8,
            profile = "https://example.com/jeff.png",
            email = "jeff@gmail.com"
        ),
        submissions = emptyList(),
        totalPoints = 120.25
    )
)
