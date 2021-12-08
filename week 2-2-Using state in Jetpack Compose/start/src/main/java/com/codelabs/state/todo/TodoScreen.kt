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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codelabs.state.util.generateRandomTodoItem
import kotlin.random.Random

/**
 * Stateless component that is responsible for the entire todo screen.
 *
 * @param items (state) list of [TodoItem] to display
 * @param onAddItem (event) request an item be added
 * @param onRemoveItem (event) request an item be removed
 * stateless composable : state를 가지고 있지 않은 composable.
                          composable의 state를 직접적으로 바꿀 수 없음 (easy testing, fewer bugs, reuse)
 * stateless -> editable 바꾸는 방법 : State hoisting
 */
/**
 * State hoisting - 매개변수 구성
 * 1. T : the current value to display 보여주기 위한 현재값
 * 2. (T) -> Unit : ValueChange
 */
@Composable
fun TodoScreen(
    items: List<TodoItem>, //보여주기 위한 현재값(T)
    currentlyEditing: TodoItem?, //ValueChange((T) -> Unit)
    onAddItem: (TodoItem) -> Unit, //ValueChange((T) -> Unit)
    onRemoveItem: (TodoItem) -> Unit, //ValueChange((T) -> Unit)
    onStartEdit: (TodoItem) -> Unit, //ValueChange((T) -> Unit)
    onEditItemChange: (TodoItem) -> Unit, //ValueChange((T) -> Unit)
    onEditDone: () -> Unit //ValueChange((T) -> Unit)
) {
    Column {
        val enableTopSection = currentlyEditing == null
        TodoItemInputBackground(elevate = true, modifier = Modifier.fillMaxWidth()) {
            if(enableTopSection) {
                TodoItemEntryInput(onAddItem)
            } else {
                Text(
                    "Editing item",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(16.dp)
                        .fillMaxWidth()
                )

            }
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(items) { todo ->
                if (currentlyEditing?.id == todo.id) {
                    TodoItemInlineEditor(
                        item = currentlyEditing,
                        onEditItemChange = onEditItemChange,
                        onEditDone = onEditDone,
                        onRemoveItem = { onRemoveItem(todo) }
                    )
                } else {
                    TodoRow(
                        todo = todo,
                        onItemClicked = { onStartEdit(it) },
                        modifier = Modifier.fillParentMaxWidth()
                    )
                }
            }
        }

        // For quick testing, a random item generator button
        Button(
            onClick = { onAddItem(generateRandomTodoItem()) },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            Text("Add random item")
        }
    }
}

/**
 * Compose에서 프로그래밍 할 때 알아야 할 사항
 * 1) Compose 함수는 순서와 관계없이 실행 가능
 * 2) Compose는 동시에 실행 가능. 각 함수는 독립적임
 * 3) Recomposition은 변경된 구성 요소만 실행
 * 4) 같은 데이터라면 동일한 화면을 그릴 수 있도록 해야함
 * 5) Compose 함수는 UI에서 매우 자주 실행될 수 있음. 따라서 비용이 많이 드는 작업을
      Compose에서 실행하게 되면 UI의 버벅임이 발생 할 수 있으므로 UI의 외부 다른 스레드로 이동해서 작업을 해야함
 */
/**
 * stateful composable : 시간에 따라 변경될 수 있는 state 일부를 가지고 있는 컴포저블.
                         remember를 사용해서 내부 state 생성
 * Stateless composable that displays a full-width [TodoItem].
 *
 * @param todo item to show
 * @param onItemClicked (event) notify caller that the row was clicked
 * @param modifier modifier for this element
 */
@Composable
fun TodoRow(
    todo: TodoItem,
    onItemClicked: (TodoItem) -> Unit,
    modifier: Modifier = Modifier,
    iconAlpha : Float =  remember(todo.id) { randomTint() }
) {
    Row(
        modifier = modifier
            .clickable { onItemClicked(todo) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(todo.task)
        // 랜덤으로 설정된 부분을 일관된 값을 가지게 하기 위해 randomTint()로 생성된 randomTint state remember하기
        // ** Composable은 recomposition을 지원하기 위해 항상 결과가 동일해야 한다.
        // remember : 컴포즈 트리에 저장 * todo.id : remeber key값
        // val iconAlpha = remember(todo.id) { randomTint() }
        Icon(
            imageVector = todo.icon.imageVector,
            // LocalContentColor.current : 알맞는 색상을 제공한다. Surface와 같은 컴포저블에 의해 변경된다.
            tint = LocalContentColor.current.copy(alpha = iconAlpha),
            contentDescription = stringResource(id = todo.icon.contentDescription)
        )
    }
}

/**
 * recomposition : 컴포저블을 다시 호출하여 컴포즈가 화면을 업데이트 하는것
 * 데이터가 변경될 때 트리를 업데이트하기 위해 동일한 컴포저블을 다시 실행하는 프로세스다.
 */
private fun randomTint(): Float {
    //리컴포지션할때마다 서로 다른 랜덤값의 틴트가 생성된다.
    return Random.nextFloat().coerceIn(0.3f, 0.9f)
}

@Preview
@Composable
fun PreviewTodoScreen() {
    val items = listOf(
        TodoItem("Learn compose", TodoIcon.Event),
        TodoItem("Take the codelab"),
        TodoItem("Apply state", TodoIcon.Done),
        TodoItem("Build dynamic UIs", TodoIcon.Square)
    )
    TodoScreen(items, null, {}, {}, {}, {}, {})
}

@Preview
@Composable
fun PreviewTodoRow() {
    val todo = remember { generateRandomTodoItem() }
    TodoRow(todo = todo, onItemClicked = {}, modifier = Modifier.fillMaxWidth())
}

/** note
 * stateful vs stateless
 * 1. stateful
    - remember를 사용해서 내부 state 생성
    - 호출하는 쪽에서 state를 관리하지 않음
    - 재사용 가능성이 적음
    - 테스트 하기 어려움
 * 2. stateless
    - state를 갖기 않는 composable
    - state를 호출해야 하는 곳에서 제어
    - 테스트 하기 쉬움
 * State Hoisting : stateful -> stateless / stateless -> stateful
*/

// TextField = EditText
// State hoisting : stateless -> stateful
@Composable
fun TodoInputTextField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier
) {
    val (text, setText) = remember { mutableStateOf("") }
    TodoInputText(text, setText, modifier)
}

// State hoisting : stateful -> stateless function 추출
@Composable
fun TodoItemEntryInput(onItemComplete: (TodoItem) -> Unit) {
    val (text, setText) = remember { mutableStateOf("") }

    val (icon, setIcon) = remember { mutableStateOf(TodoIcon.Default)}
    val iconsVisible = text.isNotBlank()
    val submit = {
        onItemComplete(TodoItem(text, icon))
        setIcon(TodoIcon.Default)
        setText("")
    }
    TodoItemInput(
        text = text,
        onTextChange = setText,
        icon = icon,
        onIconChange = setIcon,
        submit = submit,
        iconsVisible = iconsVisible
    ) {
        TodoEditButton(onClick = submit, text = "Add", enabled = text.isNotBlank())
    }
}

@Composable
fun TodoItemInput(
    text: String,
    onTextChange: (String) -> Unit,
    icon: TodoIcon,
    onIconChange: (TodoIcon) -> Unit,
    submit: () -> Unit,
    iconsVisible: Boolean,
    buttonSlot: @Composable () -> Unit // Define a slotAPI
) {
    Column {
        Row(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            TodoInputText(
                text = text,
                onTextChange = onTextChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                onImeAction = submit
            )
            /*
            TodoEditButton(
                onClick = submit,
                text = "Add",
                modifier = Modifier.align(Alignment.CenterVertically),
                enabled = text.isNotBlank() // enable if text is not blank
            )
             */
            Spacer(modifier = Modifier.width(8.dp))
            // TodoEditButton -> slotAPI Replace
            Box(Modifier.align(Alignment.CenterVertically)) { buttonSlot() }
        }
        if (iconsVisible) {
            AnimatedIconRow(icon, onIconChange, Modifier.padding(top = 8.dp))
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TodoItemInlineEditor(
    item: TodoItem,
    onEditItemChange: (TodoItem) -> Unit,
    onEditDone: () -> Unit,
    onRemoveItem: () -> Unit
) = TodoItemInput(
    text = item.task,
    onTextChange = { onEditItemChange(item.copy(task = it)) },
    icon = item.icon,
    onIconChange = { onEditItemChange(item.copy(icon = it)) },
    submit = onEditDone,
    iconsVisible = true,
    buttonSlot = {
        Row {
            val shrinkButtons = Modifier.widthIn(20.dp)
            TextButton(onClick = onEditDone, modifier = shrinkButtons) {
                Text(
                    text = "\uD83D\uDCBE", // floppy disk
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(30.dp)
                )
            }
            TextButton(onClick = onRemoveItem, modifier = shrinkButtons) {
                Text(
                    text = "❌",
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(30.dp)
                )
            }
        }
    }
)