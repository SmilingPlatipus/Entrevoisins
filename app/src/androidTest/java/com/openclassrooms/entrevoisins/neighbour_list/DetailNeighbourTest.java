package com.openclassrooms.entrevoisins.neighbour_list;

import android.content.Intent;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.ui.neighbour_list.DetailNeighbourActivity;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.utils.SelectViewAction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.service.DummyNeighbourGenerator.DUMMY_NEIGHBOURS;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNull.notNullValue;


/**
 * Test class for detail neighbour
 */
@RunWith(AndroidJUnit4.class)
public class DetailNeighbourTest
{
    private ListNeighbourActivity mActivity;

    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityRule =
            new ActivityTestRule(ListNeighbourActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
    }

    @Test
    public void myDetailActivity_widgetsAreDisplayedWithContent()
    {
        onView(allOf(ViewMatchers.isDisplayed(), ViewMatchers.withId(R.id.list_neighbours)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new SelectViewAction()));

        ViewMatchers.withId(R.id.neighbourAvatar).matches(isDisplayed());
        ViewMatchers.withId(R.id.neighbourAvatar).matches(withText(DUMMY_NEIGHBOURS.get(1).getName()));
        ViewMatchers.withId(R.id.cardname).matches(withText(DUMMY_NEIGHBOURS.get(1).getName()));
        ViewMatchers.withId(R.id.email).matches(withText(DUMMY_NEIGHBOURS.get(1).getName()));
        ViewMatchers.withId(R.id.starButton).matches(isDisplayed());
    }

    @Test
    public void myDetailActivity_addFavoriteAction_shouldAddFavoriteToFavoriteList() {

        onView(allOf(ViewMatchers.isDisplayed(), ViewMatchers.withId(R.id.list_neighbours)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new SelectViewAction()));
        //On appuie sur le bouton étoile puis sur le bouton back

        onView(ViewMatchers.withId(R.id.starButton)).perform(click());
        onView(ViewMatchers.withId(R.id.imageButton)).perform(click());

        //On vérifie que le favori a été ajouté à la liste

        onView(ViewMatchers.withId(R.id.container)).perform(ViewActions.swipeLeft());
        onView(allOf(ViewMatchers.isDisplayed(), ViewMatchers.withId(R.id.list_neighbours)))
                .check(withItemCount(1));
    }

    @Test
    public void myDetailActivity_removeFavoriteAction_shouldRemoveFavoriteFromList(){
        onView(ViewMatchers.withId(R.id.container)).perform(ViewActions.swipeLeft());

        onView(allOf(ViewMatchers.isDisplayed(), ViewMatchers.withId(R.id.list_neighbours)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new SelectViewAction()));
        //On appuie sur le bouton étoile puis sur le bouton back

        onView(ViewMatchers.withId(R.id.starButton)).perform(click());
        onView(ViewMatchers.withId(R.id.imageButton)).perform(click());
        onView(allOf(ViewMatchers.isDisplayed(), ViewMatchers.withId(R.id.list_neighbours)))
                .check(withItemCount(0));
    }

}
