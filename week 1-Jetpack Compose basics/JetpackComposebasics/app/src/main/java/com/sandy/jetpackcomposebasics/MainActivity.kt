package com.sandy.jetpackcomposebasics

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sandy.jetpackcomposebasics.ui.theme.JetpackComposebasicsTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.coerceAtLeast

class MainActivity : ComponentActivity() {

    /**
     * setContent { } 안에 layout.xml 대신 레이아웃을 구성할 Composable function(@Composable)들이 들어간다.
     * Compose는 layout.xml을 사용하지 않고 ui를 차곡차곡 구성한다.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposebasicsTheme {
                MyApp(listOf("sandy1","sandy2", "sandy3", "sandy4", "sandy5"))
            }
        }
    }
}

/**
 * @Composable : @Composable을 사용하면 Composable function을 만들 수 있다.
 */
@Composable
fun Greeting(name: String) {
    /**
     * State in Compose
     * 1) State<T>가 변경될 떄 Remember 컴포저블로 객체 저장.
     * 2) State<T>의 변경 : mutableStateOf<T>로 접근
     * 뷰 갱신 : 초기 Composition-> 데이터 변경 -> ReComposition(데이터가 변경될 때 Composition을 업데이트 하기위해 Composable을 다시 실행) -> 뷰갱신
     * 단순히 value값만 변경하는 것은 state<T>의 상태가 변경된 것이 아님. 뷰가 갱신되지 않음. state를 통해서 뷰를 갱신해야함.
     */
    // Remember 컴포저블 객체 : open/close상태 remember
    val expended = remember {
        mutableStateOf(false)
    }

    //val extrapadding = if(expended.value) 48.dp else 0.dp
    //애니메이션 추가하기기 : animateDpAsState
   val extraPadding by animateDpAsState(
        if (expended.value) 48.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    //Surface() : 배경
    Surface(
        color = MaterialTheme.colors.onPrimary,
        //Modifier : 레이아웃을 어떻게 표시할지, 어떻게 동작할지에 대한 속성 지정
        modifier = Modifier.padding(
            vertical = 4.dp,
            horizontal = 8.dp
        )
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier
                .weight(1f)
                //coerceAtLeast으로 초기값 부여
                .padding(bottom = extraPadding.coerceAtLeast(0.dp))) {
                Text(
                    text = "Hello,",
                    color = Color.White,
                    //fillMaxWidth() : match parent 설정
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "$name!",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.h5
                )
            }
//            Button(
//                onClick = { expended.value = !expended.value },
//                modifier = Modifier
//                    .weight(1f)
//                    .align(Alignment.CenterVertically)) {
//                Text(text = if(!expended.value) "show me" else "close")
//            }
            IconButton(onClick = { expended.value = !expended.value }) {
                Icon(
                    imageVector = if (!expended.value) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (!expended.value) {
                        stringResource(R.string.close)
                    } else {
                        stringResource(R.string.show_more)
                    }

                )
            }
        }
    }
}

/**
 * Reusing composables : @Composable어노테이션을 추가하여 함수를 분리하거나 독립적으로 편집할 수 있다.
 *                        -> xml에서 <inclue>태그를 이용하여 레이아웃을 분리한 것과 유사
 */
@Composable
private fun MyApp() {
    Surface(color = MaterialTheme.colors.background) {
        /**
         * Creating columns and rows : 기존 xml의 LinearLayout방식이나 ConstraintLayout으로 레이아웃을 구성하였는데
         *                              Compose의 경우 1)Column 2)Row 3)Box으로 레이아웃을 구성할 수 있다.
         * 1) Column : LinearLayout vertical
         * 2) Row : LinearLayout Horizontal
         * 3) Box : FrameLayout
         */
        Column {
            Greeting(name ="sandy")
            Greeting(name = "sunmi")
        }
    }
}

/**
 * State hoisting
 * Compose에서 View visible/invisible 판단 : Compose 함수의 호출여부에 따라
 * 1) 함수 호출시 뷰가 구성됨 -> visible
 * 2) 함수를 호출하지 않을 시 뷰가 구성되지 않음 -> invisible
 */
@Composable
private fun MyApp(names: List<String> = listOf("sandy", "sunmi")) {

    //var shouldShowOnboarding by remember { mutableStateOf(true) }
    //rememberSaveable : 화면전환, 테마(다크/라이트)변경, 권한변경 등으로 인해 액티비티가 재구성될때 상태 저장(Persisting state)
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    // shouldShowOnboarding의 state가 true일 때 -> OnboardingScreen() 호출
    if (shouldShowOnboarding) {
        // 롱클릭시 shouldShowOnboarding의 state = false로 변경
        OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })

    // shouldShowOnboarding의 state가 false일 때 -> Greeting() 호출
    } else {
        Surface(color = MaterialTheme.colors.background) {
            /*
            Column {
                for (name in names) {
                    Greeting(name = name)
                }
            }
             */
            Greetings()
        }
    }
}

/**
 * performant lazy list : Compose는 뷰를 인스턴스화하지 않기 때문에 뷰를 재활용하지 않는다.
 *                          화면에 보이는 것으로만 지연(lazy)으로 구성하여 절감한다.
 * 1) LazyColunmn
 * 2) LazyRow
 */
@Composable
private fun Greetings(names: List<String> = List(30) { "$it" } ) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            Greeting(name = name)
        }
    }
}

@Composable
fun OnboardingScreen() {
    var shouldShowOnboarding by remember { mutableStateOf(true) }
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to the Basics Codelab!")
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = { shouldShowOnboarding = false }
            ) {
                Text("Continue")
            }
        }
    }
}

@Composable
fun OnboardingScreen(onContinueClicked: () -> Unit) {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to the Basics Codelab!")
            Button(
                modifier = Modifier
                    .padding(vertical = 24.dp),
                onClick = onContinueClicked
            ) {
                Text("Continue")
            }
        }
    }
}

/**
 * @Preview : 프리뷰 어노테이션. @Preview를 통해 다양한 프리뷰를 볼 수 있다.
@Preview(showBackground = true, name = "DefaultPreview")
@Composable
fun DefaultPreview() {
    JetpackComposebasicsTheme {
        Greeting("Sandy")
    }
}
 */

@Preview(
    showBackground = true,
    name = "MyAppPreview",
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES)
@Composable
fun MyAppPreview() {
    JetpackComposebasicsTheme {
        //MyApp()
        MyApp(listOf("sandy1","sandy2", "sandy3", "sandy4", "sandy5"))
    }
}

//@Preview(showBackground = true, name = "OnboardingScreenPreview")
//@Composable
//fun OnboardingScreenPreview() {
//    JetpackComposebasicsTheme {
//        OnboardingScreen()
//        // OnboardingScreen(onContinueClicked = {}) 아무것도 롱클릭하지 않았을때
//    }
//}