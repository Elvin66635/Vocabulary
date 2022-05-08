package com.english_dev_elv.vocabulary.model

import android.os.Parcel
import android.os.Parcelable

data class Phonetic(
    val letter: String?,
    val letter_description: String?,
    val example_words: ArrayList<WordsExamplePhonetics>
)

data class WordsExamplePhonetics(
    val word: String?,
    val transcription: String?,
    val translation: String?,
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(word)
        parcel.writeString(transcription)
        parcel.writeString(translation)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WordsExamplePhonetics> {
        override fun createFromParcel(parcel: Parcel): WordsExamplePhonetics {
            return WordsExamplePhonetics(parcel)
        }

        override fun newArray(size: Int): Array<WordsExamplePhonetics?> {
            return arrayOfNulls(size)
        }
    }
}
