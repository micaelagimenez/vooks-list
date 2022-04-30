package com.project.vooks.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BooksDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBook(book: BooksData)

    @Query("SELECT * FROM books_table ORDER BY id ASC")
    fun readALlData(): LiveData<List<BooksData>>

    @Delete
    suspend fun deleteBook(book: BooksData)

    @Update
    suspend fun update(item: BooksData)
}