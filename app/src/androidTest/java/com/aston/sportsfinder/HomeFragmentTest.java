package com.aston.sportsfinder;

import static androidx.test.InstrumentationRegistry.getContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.aston.sportsfinder.dao.GameDao;
import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.util.DatabaseClient;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class HomeFragmentTest {

    private GameDao gameDao;

    @Before
    public void setUp() {
        gameDao = DatabaseClient.getInstance(getContext()).getAppDatabase().gameDao();
    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testToggleLocation() {
        onView(withId(R.id.searchButton)).perform(click());

        // Toggle the location button, check if it opens up permission menu
        onView(withId(R.id.toggle_location)).perform(click());
    }

    @Test
    public void testSearchBarInput() {
        onView(withId(R.id.searchBar)).perform(typeText("Football"), closeSoftKeyboard());
        onView(withId(R.id.searchBar)).check(matches(withText("Football")));
    }

    @Test
    public void testNavigationToSearchFragment() {
        onView(withId(R.id.searchButton)).perform(click());
    }

    @Test
    public void navigateToNotificationsFragment() {
        onView(withId(R.id.navigation_notifications)).perform(click());
        onView(withId(R.id.searchBar)).check(matches(isDisplayed()));
    }

    @Test
    public void navigateToHelpFragment() {
        onView(withId(R.id.navigation_help)).perform(click());
        onView(withId(R.id.tvNavigation)).check(matches(isDisplayed()));
    }

    @Test
    public void testSelectingFootballFilter() {
        // Click football item frame in HomeFragment
        onView(withId(R.id.footballFrame)).perform(click());

        // Fetch & assert games with type from GameDao
        List<Game> footballGames = gameDao.getGamesByType("Football");
        assertNotNull("Games of type football are null", footballGames);
        assertFalse("Games of type football doesn't exist", footballGames.isEmpty());
    }

    @Test
    public void testSelectingBaseballFilter() {
        // Click baseball item frame in HomeFragment
        onView(withId(R.id.baseballFrame)).perform(click());

        // Fetch & assert games with type from GameDao
        List<Game> baseballGames = gameDao.getGamesByType("Baseball");
        assertNotNull("Games of type baseball are null", baseballGames);
        assertFalse("Games of type baseball doesn't exist", baseballGames.isEmpty());
    }
}