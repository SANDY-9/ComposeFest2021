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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/** note
 * State Hoisting rule
 * 1. state는 state를 사용하는 모든 Composable의 최소 공통 상위 항목으로
 * 2. state는 최소한 수정 할 수 있는 최고 수준으로
 * 3. 동일한 이벤트에 대한 응답으로 두 state가 변경되면 함께
 */

// LiveData<List> -> mutableStateListOf
class TodoViewModel : ViewModel() {

    // mutableStateListOf는 LiveData<List> 작업의 오버헤드를 제거한다.
    // private var _todoItems = MutableLiveData(listOf<TodoItem>())
    // val todoItems: LiveData<List<TodoItem>> = _todoItems

    // state: todoItems
    var todoItems = mutableStateListOf<TodoItem>()
        private set
    // mutableStateListOf은 관찰가능한 MutableList 인스턴스를 생성하도록 한다. MutableList로 작업하는 것과 같은 방식으로 작업할 수 있다.

    // Define editor state
    // private state
    private var currentEditPosition by mutableStateOf(-1)
    // state 변환
    val currentEditItem: TodoItem?
        get() = todoItems.getOrNull(currentEditPosition)

    // event: addItem
    fun addItem(item: TodoItem) {
        //_todoItems.value = _todoItems.value!! + listOf(item)
        todoItems.add(item)
    }

    // event: removeItem
    fun removeItem(item: TodoItem) {
//        _todoItems.value = _todoItems.value!!.toMutableList().also {
//            it.remove(item)
//        }
        todoItems.remove(item)
        onEditDone()
    }

    // Define editor event
    // event: onEditItemSelected
    fun onEditItemSelected(item: TodoItem) {
        currentEditPosition = todoItems.indexOf(item)
    }

    // event: onEditDone
    fun onEditDone() {
        currentEditPosition = -1
    }

    // event: onEditItemChange
    fun onEditItemChange(item: TodoItem) {
        val currentItem = requireNotNull(currentEditItem)
        require(currentItem.id == item.id) {
            "You can only change an item with the same id as currentEditItem"
        }
        todoItems[currentEditPosition] = item
    }
}

/*
          LiveData) private var _todoItems = MutableLiveData(listOf<TodoItem>())
                    val todoItems: LiveData<List<TodoItem>> = _todoItems
                    ↓
mutableStateListOf) var todoItems = mutableStateListOf<TodoItem>()
                    private set

  * mutableStateListOf 및 MutableState로 수행된 작업은 Compose를 위해 만들어진 것이다.
    ViewModel이 View 시스템에서도 사용된 경우 LiveData를 계속 사용하는 것이 좋다.
 */
