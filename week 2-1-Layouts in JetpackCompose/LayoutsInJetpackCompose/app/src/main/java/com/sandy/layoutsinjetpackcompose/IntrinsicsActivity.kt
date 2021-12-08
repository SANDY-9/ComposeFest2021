package com.sandy.layoutsinjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sandy.layoutsinjetpackcompose.ui.theme.LayoutsInJetpackComposeTheme

class IntrinsicsActivity : ComponentActivity() {
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

/** Intrinsics : child의 사이즈를 미리 알도록 만든다.
 * (min|max)IntrinsicWidth: 높이(height)가 주어지고, 적절히 콘텐츠를 그릴 수 있는 최소/최대 너비
 * (min|max)IntrinsicHeight: 너비(width)가 주어지고, 적절히 콘텐츠를 그릴 수 있는 최소/최대 너비
 */
@Composable
fun TwoTexts(modifier: Modifier = Modifier, text1: String, text2: String) {
    Row(//modifier = modifier
        modifier = modifier.height(IntrinsicSize.Min)
         ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.Start),
            text = text1
        )

        Divider(color = Color.Black, modifier = Modifier.fillMaxHeight().width(1.dp))
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
                .wrapContentWidth(Alignment.End),

            text = text2
        )
    }
}

@Preview
@Composable
fun TwoTextsPreview() {
    LayoutsInJetpackComposeTheme {
        Surface {
            TwoTexts(text1 = "Hi", text2 = "there")
        }
    }
}