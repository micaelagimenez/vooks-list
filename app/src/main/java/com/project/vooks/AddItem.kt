package com.project.vooks

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.vooks.data.BooksData
import com.project.vooks.data.BooksViewmodel

var editTextTitle = ""
var editTextAuthor = ""

@Composable
fun AddItem(navController: NavController) {
    Scaffold(
        floatingActionButton = { DoneButton(navController) },
        bottomBar = {
            BottomAppBar { }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EditTextTitle()
            EditTextAuthor()
        }
    }
}

@Composable
fun EditTextAuthor() {
    var field by rememberSaveable { mutableStateOf("") }

    TextField(
        value = field,
        onValueChange = { field = it },
        label = { Text("Author") },
        singleLine = true
    )

    editTextAuthor = field
}

@Composable
fun EditTextTitle() {
    var field by rememberSaveable { mutableStateOf("") }

    TextField(
        value = field,
        onValueChange = { field = it },
        label = { Text("Title") },
        singleLine = true
    )

    editTextTitle = field
}

@Composable
fun DoneButton(navController: NavController,
               viewModel: BooksViewmodel =
                   BooksViewmodel(LocalContext.current.applicationContext as Application))
    {
    FloatingActionButton(
        onClick = {
            insertToDB(viewModel)
            navController.navigate("homeScreen")
                  },
    ) {
        Icon(
            Icons.Filled.Done,
            contentDescription = "Add",
            modifier = Modifier.size(50.dp, 50.dp)
        )
    }
}

fun insertToDB(viewModel: BooksViewmodel){
    val title = editTextTitle
    val author = editTextAuthor

    //create book
    val book =
        BooksData(
            id = 0,
            title,
            author
        )

    //add data to db
    viewModel.addBook(book)
}