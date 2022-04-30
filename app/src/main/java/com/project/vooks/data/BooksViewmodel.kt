package com.project.vooks.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BooksViewmodel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<BooksData>>
    private val repository: BooksRepository

    init {
        val booksDao = BooksDB.getDatabase(application).booksDao()
        repository = BooksRepository(booksDao)
        readAllData = repository.readAllData
    }

    fun addBook(book: BooksData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addBook(book)
        }
    }

    fun deleteBook(book: BooksData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteBook(book)
        }
    }

    private fun updateBook(item: BooksData) {
        viewModelScope.launch {
            repository.updateBook(item)
        }
    }
}