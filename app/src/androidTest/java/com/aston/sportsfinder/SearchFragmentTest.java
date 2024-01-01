package com.aston.sportsfinder;

import static androidx.test.InstrumentationRegistry.getContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.view.View;

import androidx.room.Query;
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
public class SearchFragmentTest {

    private GameDao gameDao;

    @Before
    public void setUp() { gameDao = DatabaseClient.getInstance(getContext()).getAppDatabase().gameDao(); }

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testSportTypeFilters() {
        assertGameByType("Football");
        assertGameByType("Baseball");
        assertGameByType("Rugby");
        assertGameByType("Tennis");
        assertGameByType("Hockey");
        assertGameByType("Cricket");
    }

    public void assertGameByType(String gameType) {
        List<Game> expectedGames = gameDao.getGamesByType(gameType);
        // Validate the result for each game type
        assertNotNull("Expected games for " + gameType + " should not be null", expectedGames);
        assertFalse("Expected games for " + gameType + " should not be empty", expectedGames.isEmpty());
    }
}