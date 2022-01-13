package com.example.vocabulary.model

import android.os.Parcel
import android.os.Parcelable

data class Topic(
    val id: Int,
    val question: String?,
    val image: String?,
    val title: String?,
    val quizDetails: ArrayList<QuizDetails>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        TODO("quizDetails")
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Topic> {
        override fun createFromParcel(parcel: Parcel): Topic {
            return Topic(parcel)
        }

        override fun newArray(size: Int): Array<Topic?> {
            return arrayOfNulls(size)
        }
    }
}


data class QuizDetails(
    val question: String?,
    val imageDetail: String?,
    val optionOne: String?,
    val optionTwo: String?,
    val optionThree: String?,
    val correctWord: String?,
    val correctAnswer: Int,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(question)
        parcel.writeString(optionOne)
        parcel.writeString(optionTwo)
        parcel.writeString(optionThree)
        parcel.writeString(correctWord)
        parcel.writeInt(correctAnswer)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<QuizDetails> {
        override fun createFromParcel(parcel: Parcel): QuizDetails {
            return QuizDetails(parcel)
        }

        override fun newArray(size: Int): Array<QuizDetails?> {
            return arrayOfNulls(size)
        }
    }
}

