package com.example.vocabulary.utils

import com.example.vocabulary.R
import com.example.vocabulary.model.Question

object Constants {

    const val TOTAL_QUESTIONS: String = "total_question"
    const val CORRECT_ANSWERS: String = "total_answer"

    fun getQuestions(): ArrayList<Question> {

        val questionList = ArrayList<Question>()
        val que1 = Question(
            1, "What country does this flag belong to?", R.drawable._560px_flag_of_argentina_svg,
            "Argentina1", "Azerbaijan", "Australia", "China", 1
        )

        questionList.add(que1)

        val que2 = Question(
            1, "What country does this flag belong to?", R.drawable._560px_flag_of_argentina_svg,
            "Argentina2", "Azerbaijan", "Australia", "China", 1
        )

        questionList.add(que2)

        val que3 = Question(
            1, "What country does this flag belong to?", R.drawable._560px_flag_of_argentina_svg,
            "Argentina3", "Azerbaijan", "Australia", "China", 1
        )

        questionList.add(que3)

        val que4 = Question(
            1, "What country does this flag belong to?", R.drawable._560px_flag_of_argentina_svg,
            "Argentina4", "Azerbaijan", "Australia", "China", 1
        )

        questionList.add(que4)

        val que5 = Question(
            1, "What country does this flag belong to?", R.drawable._560px_flag_of_argentina_svg,
            "Argentina5", "Azerbaijan", "Australia", "China", 1
        )

        questionList.add(que5)

        val que6 = Question(
            1, "What country does this flag belong to?", R.drawable._560px_flag_of_argentina_svg,
            "Argentina6", "Azerbaijan", "Australia", "China", 1
        )

        questionList.add(que6)


        val que7 = Question(
            1, "What country does this flag belong to?", R.drawable._560px_flag_of_argentina_svg,
            "Argentina7", "Azerbaijan", "Australia", "China", 1
        )

        questionList.add(que7)

        val que8 = Question(
            1, "What country does this flag belong to?", R.drawable._560px_flag_of_argentina_svg,
            "Argentina8", "Azerbaijan", "Australia", "China", 1
        )

        questionList.add(que8)

        val que9 = Question(
            1, "What country does this flag belong to?", R.drawable._560px_flag_of_argentina_svg,
            "Argentina9", "Azerbaijan", "Australia", "China", 1
        )

        questionList.add(que9)

        val que10 = Question(
            1, "What country does this flag belong to?", R.drawable._560px_flag_of_argentina_svg,
            "Argentina10", "Azerbaijan", "Australia", "China", 1
        )

        questionList.add(que10)

        return questionList
    }
}