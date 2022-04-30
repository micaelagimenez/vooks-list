package com.project.vooks

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.project.vooks.data.BooksData
import com.project.vooks.data.BooksViewmodel
import com.project.vooks.ui.theme.Pink500
import com.project.vooks.ui.theme.Pink700
import com.project.vooks.ui.theme.VooksTheme
import com.project.vooks.ui.theme.White200

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VooksTheme {
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Navigation()
                }
            }
        }
    }
}

val listItems = mutableMapOf<String, String>()

@ExperimentalMaterialApi
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = BooksViewmodel(LocalContext.current.applicationContext as Application)

    val list = viewModel.readAllData.observeAsState().value
    list?.forEach {
        listItems.put(it.title, it.author)
    }

    Scaffold(
        floatingActionButton = { AddButton(navController) },
        bottomBar = {
            BottomAppBar { }
        }
    ) {
        Column(Modifier.fillMaxSize()) {
            if (list != null) {
                DisplayList(items = list)
            }
        }
    }
}

@Composable
fun ListItem(item: BooksData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(60.dp)
            .background(color = Pink700),
        contentAlignment = Center
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_bookmark_24),
                contentDescription = "bookmark icon",
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .align(CenterVertically)
            )
            Column {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    text = item.title,
                    color = Color.White,
                    fontSize = 16.sp
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    text = item.author,
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun DisplayList(items: List<BooksData>) {
    val viewModel = BooksViewmodel(LocalContext.current.applicationContext as Application)

    LazyColumn(modifier = Modifier.fillMaxSize(1F)) {
        items(items) { item ->
            val dismissState = rememberDismissState()
            if (dismissState.isDismissed(DismissDirection.EndToStart)){
                viewModel.deleteBook(item)
            }
            SwipeToDismiss(
                state = dismissState,
                modifier = Modifier.padding(vertical = 2.dp),
                directions = setOf(
                    DismissDirection.StartToEnd,
                    DismissDirection.EndToStart
                ),
                dismissThresholds = { direction ->
                    FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
                },
                background = {
                    val direction =
                        dismissState.dismissDirection ?: return@SwipeToDismiss
                    val color by animateColorAsState(
                        when (dismissState.targetValue) {
                            DismissValue.Default -> Color.LightGray
                            DismissValue.DismissedToEnd -> Color.Red
                            DismissValue.DismissedToStart -> Color.Red
                        }
                    )
                    val alignment = when (direction) {
                        DismissDirection.StartToEnd -> Alignment.CenterStart
                        DismissDirection.EndToStart -> Alignment.CenterEnd
                    }
                    val icon = when (direction) {
                        DismissDirection.StartToEnd -> Icons.Default.Delete
                        DismissDirection.EndToStart -> Icons.Default.Delete
                    }
                    val scale by animateFloatAsState(
                        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                    )
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(horizontal = 30.dp),
                        contentAlignment = alignment
                    ) {
                        Icon(
                            icon,
                            contentDescription = "Localized description",
                            modifier = Modifier.scale(scale)
                        )
                    }
                },
                dismissContent = {
                    Card(
                        elevation = animateDpAsState(
                            if (dismissState.dismissDirection != null) 4.dp else 0.dp
                        ).value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dp(60f))
                            .align(alignment = CenterVertically)
                    ) {
                        ListItem(item = item)
                    }
                }
            )
        }
    }
}

@Composable
fun AddButton(navController: NavController) {
    FloatingActionButton(
        onClick = { navController.navigate("addItem") },
    ) {
        Icon(
            Icons.Filled.AddCircle,
            contentDescription = "Add",
            modifier = Modifier.size(50.dp, 50.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VooksTheme {
        MainActivity()
    }
}