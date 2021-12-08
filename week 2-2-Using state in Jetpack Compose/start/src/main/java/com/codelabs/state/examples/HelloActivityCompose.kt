package com.codelabs.state.examples

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * @author SANDY
 * @email nnal0256@naver.com
 * @created 2021-12-08
 * @desc Compose and ViewModels
 */
/*** note
  State – 시간에 따라 변경 될 수 있는 어떤 값
  Event – 프로그램 일부에 무슨 일이 생긴 것을 알린다.
  단방향 데이터 흐름(Unidirectional data flow) – 이벤트는 위로 state는 아래로 흐르는 설계
 */
class HelloActivityCompose : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloScreen()
        }
    }
}

@Composable
private fun HelloScreen(helloViewModel: HelloViewModel = viewModel()) {
    // helloViewModel follows the Lifecycle as the the Activity or Fragment that calls this
    // composable function.

    // name is the _current_ value of [helloViewModel.name]
    val name: String by helloViewModel.name.observeAsState("")

    HelloInput(name = name, onNameChange = { helloViewModel.onNameChanged(it) })
}

@Composable
private fun HelloScreenWithInternalState() {
    val (name, setName) = remember { mutableStateOf("") }
    HelloInput(name = name, onNameChange = setName)
}

/**
 * @param name (state) current text to display
 * @param onNameChange (event) request that text change
 */
@Composable
private fun HelloInput(
    name: String,
    onNameChange: (String) -> Unit
) {
    Column {
        Text(name)
        TextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Name") }
        )
    }
}