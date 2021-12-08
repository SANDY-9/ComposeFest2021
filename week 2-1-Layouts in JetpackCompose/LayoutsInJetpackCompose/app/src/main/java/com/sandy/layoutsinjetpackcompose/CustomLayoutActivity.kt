package com.sandy.layoutsinjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sandy.layoutsinjetpackcompose.ui.theme.LayoutsInJetpackComposeTheme

class CustomLayoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutsInJetpackComposeTheme {
                BodyContent()
            }
        }
    }
}

private fun Modifier.firstBaselineToTop(firstBaselineToTop: Dp) = this.then(
    // 1) measurable : 측정값, 포지션값
    // 2) constraints : 너비, 높이의 최소 및 최대치
    layout { measurable, constraints ->

        // Compose에서 child의 measure는 단 한번만 측정할 수 있다. 두번 측정시 오류 발생
        // Coposable 측정 : measurable.measure(constraints)
        val placeable = measurable.measure(constraints)

        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = placeable[FirstBaseline]

        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY

        // Coposable 사이즈 지정
        layout(placeable.width, height) {
            // Coposable 배치 : placeable.placeRelative(x, y)
            placeable.placeRelative(0, placeableY)
        }
    }
)

/*
    Slot Api
    1. 사용자 정의 레이아어를 가져오기 위해 Compose가 도입한 패턴
    2. 형태
    @Composable
    fun CustomLayout(
        content: @Composable() () -> Unit // custom layout 속성들
    ){ }

    CustomLayout
    @Composable
    fun CustomLayout(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(
        modifier = modifier,
        content = content //custom layout 속성들
    ) { measurables, constraints ->
        // 주어진 constraints로 하위 요소들을 측정하고, 배치한다.
    }
}
 */
@Composable
private fun MyOwnColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    // 주어진 constraints로 하위 요소들을 측정하고, 배치한다.
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints) // Coposable 측정
        }

        // 수직으로 배치하기 위해 하위 요소들의 y 좌표값을 추적
        var yPosition = 0

        // Composable 사이즈, 포지션 설정
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEach { placeable ->
                // Coposable 배치 : placeable.placeRelative(x, y)
                placeable.placeRelative(x = 0, y = yPosition)
                // y좌표값 저장
                yPosition += placeable.height
            }
        }
    }
}

@Composable
private fun BodyContent(modifier: Modifier = Modifier) {
    MyOwnColumn(modifier.padding(8.dp)) {
        Text("MyOwnColumn")
        Text("places items")
        Text("vertically.")
        Text("We've done it by hand!")
    }
}

@Preview
@Composable
private fun TextWithPaddingToBaselinePreview() {
    LayoutsInJetpackComposeTheme {
        Text("Hi there!", Modifier.firstBaselineToTop(32.dp))
    }
}

@Preview
@Composable
private fun TextWithNormalPaddingPreview() {
    LayoutsInJetpackComposeTheme {
        Text("Hi there!", Modifier.padding(top = 32.dp))
    }
}

@Preview
@Composable
private fun BodyContentPreview() {
    LayoutsInJetpackComposeTheme {
        BodyContent()
    }
}