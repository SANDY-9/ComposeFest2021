package com.sandy.layoutsinjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import androidx.constraintlayout.widget.ConstraintLayout
import com.sandy.layoutsinjetpackcompose.ui.theme.LayoutsInJetpackComposeTheme

class ConstraintLayoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutsInJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                }
            }
        }
    }
}

@Composable
private fun ConstraintLayoutContent() {
    /**
     * ConstraintLayout
     * 1. createRefs() : 배치를 위한 constraint 참조를 생성
     * 2. Modifier.constrainAs() : 배치
     * 3. linkTo(constaintPosition, margin) : ConstraintLayout constraint 연결
     */
    ConstraintLayout {
        /*
        // 배치를 위한 참조를 생성 : button 1개, text 1개
        val (button, text) = createRefs()

        Button(
            onClick = { /* Do something */ },
            modifier = Modifier.constrainAs(button) {
                top.linkTo(parent.top, margin = 16.dp)
            }
        ) {
            Text("Button")
        }

        Text("Text", Modifier.constrainAs(text) {
            top.linkTo(button.bottom, margin = 16.dp)
            //가운데 정렬
            centerHorizontallyTo(parent)
        })
         */

        // constraints 생성
        val (button1, button2, text) = createRefs()

        Button(
            onClick = { /* Do something */ },
            modifier = Modifier.constrainAs(button1) {
                top.linkTo(parent.top, margin = 16.dp)
            }
        ) {
            Text("Button 1")
        }

        Text("Text", Modifier.constrainAs(text) {
            top.linkTo(button1.bottom, margin = 16.dp)
            centerAround(button1.end)
        })

        val barrier = createEndBarrier(button1, text)
        Button(
            onClick = { /* Do something */ },
            modifier = Modifier.constrainAs(button2) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(barrier)
            }
        ) {
            Text("Button 2")
        }
    }
}

@Composable
private fun LargeConstraintLayout() {
    ConstraintLayout {
        val text = createRef()

        val guideline = createGuidelineFromStart(fraction = 0.5f)
        Text(
            "This is a very very very very very very very long text",
            Modifier.constrainAs(text) {
                linkTo(start = guideline, end = parent.end)
                /** Dimension
                1. wrapContent – 원래 크기(화면밖으로 벗어날 수 있음)
                2. preferredWrapContent – 기본적으로 wrapContent와 같지만, 주어진 제약조건내에서 컨텐츠를 감싼다. (화면 밖을 벗어나지 않음)
                3. fillToConstraints – match_parent
                4. value - 고정된 dp 값을 갖는다. (화면 밖을 벗어날 수 있음)
                5. preferredValue – 주어진 제약조건을 잘 지키며, 고정된 dp값을 갖는다. (화면 밖을 벗어나지 않음)
                 */
                //width 사이즈 제한
                width = Dimension.preferredWrapContent
            }
        )
    }
}

@Composable
private fun DecoupledConstraintLayout() {
    BoxWithConstraints {
        val constraints = if (maxWidth < maxHeight) {
            decoupledConstraints(margin = 16.dp) // Portrait constraints
        } else {
            decoupledConstraints(margin = 32.dp) // Landscape constraints
        }

        ConstraintLayout(constraints) {
            Button(
                onClick = { /* Do something */ },
                modifier = Modifier.layoutId("button")
            ) {
                Text("Button")
            }

            Text("Text", Modifier.layoutId("text"))
        }
    }
}

private fun decoupledConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val button = createRefFor("button")
        val text = createRefFor("text")

        constrain(button) {
            top.linkTo(parent.top, margin= margin)
        }
        constrain(text) {
            top.linkTo(button.bottom, margin)
        }
    }
}


@Preview
@Composable
private fun ConstraintLayoutPreview() {
    LayoutsInJetpackComposeTheme {
        ConstraintLayoutContent()
    }
}

@Preview(showBackground = true)
@Composable
private fun LargeConstraintLayoutPreview() {
    LayoutsInJetpackComposeTheme {
        LargeConstraintLayout()
    }
}

@Preview(showBackground = true)
@Composable
private fun DecoupledConstraintLayoutPreview() {
    LayoutsInJetpackComposeTheme {
        DecoupledConstraintLayout()
    }
}