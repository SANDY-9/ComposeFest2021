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

package com.codelabs.state.examples

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codelabs.state.databinding.ActivityHelloCodelabBinding

/**
 * An example showing unstructured state stored in an Activity.
 */
class HelloCodelabActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelloCodelabBinding
    var name = ""

    /**
     * 기존 아키텍처(layout.xml)의 문제점
     * 1) 데이터가 변경되었을 때 UI를 업데이트 하기 위해서는 뷰를 조회 하고 속성을 다시 설정(set)해야함.
     * 2) 상태가 바뀔때마다 새로운 데이터를 UI와 동기화해야한다.
     * 3) 뷰마다 상태가 다르고 각각 업데이트 해야하기 때문에 과정이 매우 복잡하고
          자바/코틀린 코드와 xml의 결합도가 높다.
     * 4) 뷰의 수가 증가할 수록 소프트웨어 유지관리의 복잡성이 증가
     * 5) 뷰를 수동으로 조작하기 때문에 오류가 발생할 가능성이 높음
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelloCodelabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * 일반적인 이벤트 흐름
         * UI Update Loop : Event → Update State → Display State → Event
         * 이벤트가 발생하면 이벤트 핸들러는 UI에서 사용하는 State를 변경하고 새 state를 사용하도록 UI가 업데이트 된다.
         */
        // doAfterTextChange is an event that modifies state
        binding.textInput.doAfterTextChanged { text ->
            name = text.toString()
            updateHello()
        }
    }

    /**
     * This function updates the screen to show the current state of [name]
     */
    private fun updateHello() {
        binding.helloText.text = "Hello, $name"
    }
}

/**
 * A ViewModel extracts _state_ from the UI and defines _events_ that can update it.
 */
class HelloViewModel : ViewModel() {

    // LiveData holds state which is observed by the UI
    // (state flows down from ViewModel)
    private val _name = MutableLiveData("")
    val name: LiveData<String> = _name

    // onNameChanged is an event we're defining that the UI can invoke
    // (events flow up from UI)
    fun onNameChanged(newName: String) {
        _name.value = newName
    }
}

/**
 * An example showing unidirectional data flow in the View system using a ViewModel.
 */
class HelloCodeLabActivityWithViewModel : AppCompatActivity() {
    private val helloViewModel by viewModels<HelloViewModel>()

    /**
     * 뷰모델을 이용한 단방향 데이터 흐름 : 단방향 데이터 흐름은 event가 위로 흐르고 state가 아래로 흐르는 설계방식이다.
     * 1) event : Activity → ViewModel
     * 2) state(livedata) : ViewModel → Activity
     * state : 시간이 지남에 따라 변경될 수 있는 모든 어떤 값
     * 장점 - 테스트 용이, state의 캡슐화, observable패턴으로 인한 UI 일관성
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHelloCodelabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // doAfterTextChange is an event that triggers an event on the ViewModel
        binding.textInput.doAfterTextChanged {
            // onNameChanged is an event on the ViewModel
            helloViewModel.onNameChanged(it.toString())
        }
        // [helloViewModel.name] is state that we observe to update the UI
        helloViewModel.name.observe(this) { name ->
            binding.helloText.text = "Hello, $name"
        }
    }
}
