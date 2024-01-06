package com.aston.sportsfinder;

import static androidx.test.InstrumentationRegistry.getContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.util.Log;
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
public class GameCreateFragmentTest {

    private GameDao gameDao;

    @Before
    public void setUp() { gameDao = DatabaseClient.getInstance(getContext()).getAppDatabase().gameDao(); }

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testCreateGame() throws InterruptedException {
        // Navigate to search fragment
        onView(withId(R.id.searchButton)).perform(click());

        // Use create game button
        onView(withId(R.id.btnCreateGame)).perform(click());

        // Choose "create game" from the popup menu
        onView(withText("Create a Game")).perform(click());

        onView(withId(R.id.etTeam1)).perform(typeText("Team 1"), closeSoftKeyboard());
        onView(withId(R.id.etTeam2)).perform(typeText("Team 2"), closeSoftKeyboard());
        onView(withId(R.id.etLatitude)).perform(typeText("54.49153473484887"), closeSoftKeyboard());
        onView(withId(R.id.etLongitude)).perform(typeText("-6.961619183421135"), closeSoftKeyboard());

        onView(withId(R.id.scrollViewGameCreate)).perform(swipeUp());

        // Save the game
        onView(withId(R.id.saveGame)).perform(click());
        Thread.sleep(250);

        // Verify created game with the game stored in the db
        Game createdGame = gameDao.getGameByDetails("Team 1", "Team 2", 54.49153473484887, -6.961619183421135);
        Thread.sleep(500);
        assertNotNull(createdGame);
        assertEquals("Team 1", createdGame.getTeam1());
        assertEquals("Team 2", createdGame.getTeam2());
        assertEquals(54.49153473484887, createdGame.getLatitude(), 0);
        assertEquals(-6.961619183421135, createdGame.getLongitude(), 0);

        if (createdGame != null) {
            Log.d("SSS", "Game created successfully.");
            Log.d("SSS", "Team 1: " + createdGame.getTeam1());
            Log.d("SSS", "Team 2: " + createdGame.getTeam2());
            Log.d("SSS", "Latitude: " + createdGame.getLatitude());
            Log.d("SSS", "Longitude: " + createdGame.getLongitude());
        } else {
            Log.d("SSS", "Game creation failed.");
        }
    }

    @Test
    public void testGameSearchResults() throws InterruptedException {
        // Navigate to the search fragment
        onView(withId(R.id.navigation_search)).perform(click());

        // Enter a query and perform a search
        String searchQuery = "Football";
        onView(withId(R.id.searchBar)).perform(typeText(searchQuery), pressImeActionButton());

        Thread.sleep(2000);

        // Get games from database matching the search query
        List<Game> expectedGames = gameDao.searchGames(searchQuery);

        // Check if the result is valid
        assertNotNull(expectedGames);
        assertTrue(!expectedGames.isEmpty());
    }
}