package com.sandy.layoutsinjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sandy.layoutsinjetpackcompose.ui.theme.LayoutsInJetpackComposeTheme

class ModifierActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutsInJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    PhotographerCard()
                }
            }
        }
    }
}

/**
 * Modifier : Composable 데코레이션
 */
@Composable
private fun PhotographerCard(modifier: Modifier = Modifier) {
    //padding의 위치에 따라 클릭영역이 반영된다.
    //modifier.clickable(onClick = {} ).padding(16.dp) : 16.dp만큼 패딩준 전체영역이 클릭영역
    //modifier.padding(16.dp).clickable(onClick = {} ) : 16.dp만큼 패딩 주기전 영역이 클릭영역
    //Modifier를 이용하여 View Size와 위치정보 가져오기 : Modifier.onGloballyPositioned { }
    Row(modifier
        .padding(8.dp)
        .clip(RoundedCornerShape(4.dp))
        .background(MaterialTheme.colors.surface)
        .clickable(onClick = {})
        .padding(16.dp)
    ) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.surface.copy(alpha = 0.5f)
        ) {
            //Image 설정
        }
        Column(
            // align(Alignment.CenterVertically) : Layout gravity = center
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = "Sandy",
                fontWeight = FontWeight.Bold
            )
            //LocalContentAlpha : opacity(불투명도) 설정
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                Text(
                    text = "3 minutes ago",
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Preview
@Composable
private fun PhotographerCardPreview() {
    LayoutsInJetpackComposeTheme {
        PhotographerCard()
    }
}