package com.example.hello_testing;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;

import androidx.test.espresso.Root;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicReference;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule =
            new ActivityScenarioRule<>(MainActivity.class);

    private final AtomicReference<View> decorRef = new AtomicReference<>();

    @Before
    public void setUp() {
        // Capture the Activity decor view for Toast matching
        rule.getScenario().onActivity(activity ->
                decorRef.set(activity.getWindow().getDecorView()));

        // Initialize Espresso-Intents for navigation assertions
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    // 1) Button click updates TextView
    @Test
    public void buttonClick_updatesText() {
        onView(withId(R.id.tvStatus)).check(matches(withText("Ready")));
        onView(withId(R.id.btnHello)).perform(click());
        onView(withId(R.id.tvStatus)).check(matches(withText("Clicked!")));
    }

    // 2) Navigation to DetailActivity with intent extra
    @Test
    public void buttonClick_navigatesWithExtra() {
        onView(withId(R.id.btnNext)).perform(click());

        intended(hasComponent(DetailActivity.class.getName()));
        intended(hasExtra("name", "Test Student"));

        // Optional: verify the target screen displays the extra
        onView(withId(R.id.tvDetail))
                .check(matches(withText("Welcome, Test Student!")));
    }

}
