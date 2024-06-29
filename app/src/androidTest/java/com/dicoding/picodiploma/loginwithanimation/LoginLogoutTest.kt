package com.dicoding.picodiploma.loginwithanimation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicoding.picodiploma.loginwithanimation.view.login.LoginActivity
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginLogoutTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)

    private lateinit var idlingResource: IdlingResource

    @Before
    fun setUp() {
        activityScenarioRule.scenario.onActivity { activity ->
            idlingResource = activity.getIdlingResource()
            IdlingRegistry.getInstance().register(idlingResource)
        }
    }

    @Test
    fun testLoginLogoutFlow() {
        // Login
        onView(withId(R.id.emailEditText)).perform(replaceText("email"))
        onView(withId(R.id.passwordEditText)).perform(replaceText("password"))
        onView(withId(R.id.loginButton)).perform(click())

        // Logout
        onView(withId(R.id.fabLogout)).perform(click())

        // Verify logged out state
        onView(withId(R.id.emailEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.passwordEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }
}
