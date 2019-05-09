package com.fly.shop

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SignUpActivityTest {

    @Rule
    @JvmField
    val activityTest = ActivityTestRule<SignUpActivity>(SignUpActivity::class.java)

    @Test
    fun signUpSuccess() {
        onView(withId(R.id.email)).perform(typeText("tony91097@gmail.com"))
        onView(withId(R.id.password)).perform(typeText("qqq11111"))
        onView(withId(R.id.signup)).perform(click())
        Thread.sleep(3000)
        onView(withText("Success")).check(ViewAssertions.matches(isDisplayed()))
    }
}