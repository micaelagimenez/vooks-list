package com.project.vooks.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books_table")
data class BooksData (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var title: String,
    var author: String
)