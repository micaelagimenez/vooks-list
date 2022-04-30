package com.project.vooks.data

import androidx.lifecycle.LiveData

class BooksRepository(private val booksDao: BooksDao) {

    val readAllData: LiveData<List<BooksData>> = booksDao.readALlData()

    suspend fun addBook(book: BooksData) {
        booksDao.addBook(book)
    }

    suspend fun deleteBook(book: BooksData) {
        booksDao.deleteBook(book)
    }

    suspend fun updateBook(book: BooksData) {
        booksDao.update(book)
    }
}