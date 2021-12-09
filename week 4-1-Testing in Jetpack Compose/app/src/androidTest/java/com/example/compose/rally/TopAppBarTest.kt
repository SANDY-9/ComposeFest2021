package com.example.compose.rally

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.compose.rally.ui.components.RallyTopAppBar
import org.junit.Rule
import org.junit.Test

/**
 * @author SANDY
 * @email nnal0256@naver.com
 * @created 2021-12-09
 * @desc
 */
class TopAppBarTest {

    // ComposeTestRule 선언 : createComposeRule()
    // Compose는 component별 테스트가 가능하다(Testing in isolation)
    @get:Rule
    val composeTestRule = createComposeRule()

    // TopAppBar 테스트
    @Test
    fun rallyTopAppBarTest() {
        val allScreens = RallyScreen.values().toList()
        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = { },
                currentScreen = RallyScreen.Accounts
            )
        }

        Thread.sleep(5000) // 관찰을 위한 시간 추가
    }

    // TopAppBar tab selected 테스트
    @Test
    fun rallyTopAppBarTest_currentTabSelected() {
        val allScreens = RallyScreen.values().toList()
        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = { },
                currentScreen = RallyScreen.Accounts
            )
        }

        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Accounts.name)
            .assertIsSelected()

        Thread.sleep(5000)
    }

    // TopAppBar의 currentLabel이 upper case인지 테스트
    @Test
    fun rallyTopAppBarTest_currentLabelExists() {
        val allScreens = RallyScreen.values().toList()
        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = { },
                currentScreen = RallyScreen.Accounts
            )
        }

        // semantics tree 출력
        // printToLog - 디버깅 레벨에서만 찍히는 Logcat
        composeTestRule.onRoot().printToLog("currentLabelExists")

        /*
        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Accounts.name)
            //.onNodeWithText(RallyScreen.Accounts.name.uppercase()) // Still fails - 이유 “ACCOUNTS”가 아닌 “Accounts”가 text였기 때문
            .assertExists()
         */

        composeTestRule
            .onNode(
                hasText(RallyScreen.Accounts.name.uppercase()) and
                        hasParent(
                            hasContentDescription(RallyScreen.Accounts.name)
                        ),
                useUnmergedTree = true
            )
            .assertExists()
    }

    @Test
    fun rallyTopAppBarTest_selectedChange() {
        // 테스트 설정
        val allScreens = RallyScreen.values().toList()
        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = { },
                currentScreen = RallyScreen.Accounts
            )
        }

        // 탭 클릭 수행
        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Accounts.name)
            .performClick()
            .assertExists()

        // 탭 selected 상태 확인
        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Accounts.name)
            .assertIsSelected()

    }



}