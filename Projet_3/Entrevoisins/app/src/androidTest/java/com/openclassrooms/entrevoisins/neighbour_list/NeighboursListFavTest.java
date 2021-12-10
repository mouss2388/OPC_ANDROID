package com.openclassrooms.entrevoisins.neighbour_list;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.core.IsNull.notNullValue;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.activity.ListNeighbourActivity;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class NeighboursListFavTest {

    private static int ITEMS_COUNT = 12;

    private ListNeighbourActivity mActivity;

    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityRule =
            new ActivityTestRule(ListNeighbourActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
    }


    ///MY TESTS/////
    @Test
    public void neighboursList_clickOnRow_showDetailsWellFirstname() {
        onView(ViewMatchers.withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(ViewMatchers.withId(R.id.detail_user))
                .check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.firstname))
                .check(matches(withText("Caroline")));
    }

    @Test
    public void neighbourList_deleteOne_shouldHaveOneUserAtLeast() {
        //LIST COUNT 12 NEIGHBOURS
        onView(ViewMatchers.withId(R.id.list_neighbours)).check(withItemCount(ITEMS_COUNT));
        //CLICK DELETE BUTTON AT POSITION 0 (CAROLINE)
        onView(ViewMatchers.withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(
                        1,
                        new ViewAction() {
                            @Override
                            public Matcher<View> getConstraints() {
                                return null;
                            }

                            @Override
                            public String getDescription() {
                                return "Click on specific button";
                            }

                            @Override
                            public void perform(UiController uiController, View view) {
                                View btnDelete = view.findViewById(R.id.item_list_delete_button);
                                if(btnDelete != null){
                                    btnDelete.performClick();}
                            }
                        })
                );
        onView(ViewMatchers.withId(R.id.list_neighbours)).check(withItemCount(ITEMS_COUNT - 1));
    }

    @Test
    public void neighbourListFav_shouldShowOnlyFavNeighbour() {
        //LIST FAVORITE NEIGHBOUR EMPTY
        onView(ViewMatchers.withId(R.id.list_neighbours_fav))
                .check(matches(hasChildCount(0)));
        //CLICK ON FIRST NEIGHBOUR (CAROLINE)
        onView(ViewMatchers.withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //ADD AS FAVORITE
        onView(ViewMatchers.withId(R.id.add_favorite)).perform(click());
        //GO BACK
        onView(ViewMatchers.withId(R.id.backArrow)).perform(click());
        //CHANGE TAB
        onView(ViewMatchers.withId(R.id.container)).perform(swipeLeft());
        //LIST FAVORITE NEIGHBOUR COUNT ONE PERSON
        onView(ViewMatchers.withId(R.id.list_neighbours_fav))
                .check(matches(hasChildCount(1)));
        //CLICK ON ALONE NEIGHBOUR
        onView(ViewMatchers.withId(R.id.list_neighbours_fav))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //CHECK IF FIRSTNAME IS CAROLINE
        onView(ViewMatchers.withId(R.id.firstname))
                .check(matches(withText("Caroline")));
    }
}
