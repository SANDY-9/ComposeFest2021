package com.sandy.layoutsinjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.sandy.layoutsinjetpackcompose.ui.theme.LayoutsInJetpackComposeTheme
import kotlinx.coroutines.launch

/**
 * @author SANDY
 * @email nnal0256@naver.com
 * @created 2021-12-08
 * @desc
 */
class ListsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutsInJetpackComposeTheme() {
                SimpleList()
            }
        }
    }
}

// verticalScroll을 이용하여 리스트 구현하기
@Composable
private fun SimpleList() {
    // rememberScrollState() : 스크롤의 포지션 state를 저장, 리스트 스크롤에 이용
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        repeat(100) {
            Text("SimpleList Item #$it")
        }
    }
}

// LazyColumn 이용하여 리스트 구현하기
@Composable
private fun LazyList() {
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState) {
        items(100) {
            Text("LazyList Item #$it")
        }
    }
}

@Composable
private fun ImageListItem(index: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = rememberImagePainter(
                data = "https://developer.android.com/images/brand/Android_Robot.png"
            ),
            contentDescription = "Android Logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = "Item #$index",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(end = 8.dp)
        )
    }
}

@Composable
private fun ImageList() {
    val scrollState = rememberLazyListState()
    val listSize = 100
    // rememberCoroutineScope() : 스크롤 애니메이션 등을 위한 코루틴 생성
    val coroutineScope = rememberCoroutineScope()
    Column {
        Row {
            Button(onClick = {
                coroutineScope.launch {
                    // 0 is the first item index
                    scrollState.animateScrollToItem(0)
                }
            },
                modifier = Modifier.weight(1f).fillMaxWidth()
                ) {
                Text("Scroll to the top")
            }
            Button(onClick = {
                coroutineScope.launch {
                    // listSize - 1 is the last index of the list
                    scrollState.animateScrollToItem(listSize - 1)
                }
            },
                modifier = Modifier.weight(1f).fillMaxWidth()
                ) {
                Text("Scroll to the end")
            }
        }
        LazyColumn(state = scrollState) {
            items(100) {
                ImageListItem(it)
            }
        }
    }
}

@Preview
@Composable
private fun ImageListItemPreview() {
    LayoutsInJetpackComposeTheme {
        ImageListItem(index = 0)
    }
}

@Preview
@Composable
private fun ListPreview() {
    LayoutsInJetpackComposeTheme() {
        Scaffold {
            //SimpleList()
            //LazyList()
            ImageList()
        }
    }
}

