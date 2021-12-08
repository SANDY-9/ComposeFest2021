package com.sandy.layoutsinjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sandy.layoutsinjetpackcompose.ui.theme.LayoutsInJetpackComposeTheme

class ScaffoldActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutsInJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    LayoutsCodelab()
                }
            }
        }
    }
}

/**
 * Scaffold : 머테리얼 디자인 UI를 구성하게 해주는 structure
 * 종류 : TopAppBar, BottomAppBar, FloatingActionButton, Drawer 등
 * Scaffold { innerPadding -> 바디 구성 } : 람다는 innerPadding값을 파라미터로 받음
 */
@Composable
private fun LayoutsCodelab() {
    /*
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(text = "Hi there!")
            Text(text = "Thanks for going through the Layouts codelab")
        }
    }
     */
    Scaffold(
        /*
        topBar = {
            Text(
                text = "LayoutsCodelab",
                style = MaterialTheme.typography.h3
            )
        }
         */
        // 상단바(TopAppBar) 만들기
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "LayoutsCodelab")
                },
                // actions : 상단바 동작 만들기(아이콘 버튼 등등)
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(Icons.Filled.Favorite, contentDescription = null)
                    }
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(Icons.Outlined.Home, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        BodyContent(Modifier.padding(innerPadding).padding(8.dp))
    }
}

@Composable
private fun BodyContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "Hi there!",
            style = MaterialTheme.typography.h6
        )
        Text(text = "Thanks for going through the Layouts codelab")
    }
}

@Preview
@Composable
private fun LayoutsCodelabPreview() {
    LayoutsInJetpackComposeTheme {
        LayoutsCodelab()
    }
}