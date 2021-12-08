/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codelabs.state.todo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codelabs.state.ui.StateCodelabTheme

class TodoActivity : AppCompatActivity() {

    val todoViewModel: TodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // StateCodelabTheme : 컴포즈 프로젝트로 시작할 때 안드로이드 스튜디오에서 기본적으로 생성해주는 테마
            StateCodelabTheme {
                // Surface : 껍데기. 앱에 배경을 추가함
                Surface {
                    TodoActivityScreen(todoViewModel = todoViewModel)
                }
            }
        }
    }
}

@Composable
private fun TodoActivityScreen(todoViewModel: TodoViewModel) {
    // viewModel - LiveData 이용
    // Compose는 뷰모델의 라이브데이터를 observeAsState함으로서 옵저버패턴으로 delegation할 수 있다.
    // -> 굳이 옵저버 달아서 옵저버 패턴 구현 안해도됨
    /*
    val items : List<TodoItem> by todoViewModel.todoItems.observeAsState(initial = listOf())
    TodoScreen(
        items = items,
        onAddItem = todoViewModel::addItem,
        onRemoveItem = todoViewModel::removeItem
//        onAddItem = { todoViewModel.addItem(it) },
//        onRemoveItem = { todoViewModel.removeItem(it) }
    )
     */
    // viewModel - mutableStateListOf 이용
    TodoScreen(
        items = todoViewModel.todoItems,
        currentlyEditing = todoViewModel.currentEditItem,
        onAddItem = todoViewModel::addItem,
        onRemoveItem = todoViewModel::removeItem,
        onStartEdit = todoViewModel::onEditItemSelected,
        onEditItemChange = todoViewModel::onEditItemChange,
        onEditDone = todoViewModel::onEditDone
    )
}

