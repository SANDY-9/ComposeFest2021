package com.example.compose.rally

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.compose.rally.ui.overview.OverviewBody
import org.junit.Rule
import org.junit.Test

/**
 * @author SANDY
 * @email nnal0256@naver.com
 * @created 2021-12-09
 * @desc
 */
class OverviewScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun overviewScreen_alertsDisplayed() {
        composeTestRule.setContent {
            OverviewBody() // fails : possibly due to compose being busy. -> 앱을 테스트와 동기화(Synchronization) 할 수 없음
            // 원인 : repeat 애니메이션 때문에 timeout 발생한 것
            // 해결 : rememberInfiniteTransition사용
        }

        composeTestRule
            .onNodeWithText("Alerts")
            .assertIsDisplayed()
    }

}