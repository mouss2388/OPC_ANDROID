
package com.openclassrooms.entrevoisins.neighbour_list;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNull.notNullValue;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.controller.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


/**
 * Test class for list of neighbours
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class NeighboursListTest {

    // This is fixed
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

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void myNeighboursList_shouldNotBeEmpty() {
        // First scroll to the position that needs to be matched and click on it.
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours), hasDescendant(withText("Vincent"))))
                .check(matches(hasMinimumChildCount(2)));

    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void myNeighboursList_deleteAction_shouldRemoveItem() {
        // Given : We remove the element at position 2
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours), hasDescendant(withText("Vincent")))).check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours), hasDescendant(withText("Vincent"))))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the number of element is 11
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours), hasDescendant(withText("Vincent")))).check(withItemCount(ITEMS_COUNT - 1));
    }

    ////My Instrumented Tests//////

    @Test
    public void neighboursList_clickOnRow_showDetailsWellFirstname() {
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours), hasDescendant(withText("Vincent"))))

                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(ViewMatchers.withId(R.id.detail_user))
                .check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.firstname))
                .check(matches(withText("Caroline")));
    }

    @Test
    public void neighbourList_deleteOne_shouldHaveOneUserAtLeast() {
        //LIST COUNT 11 NEIGHBOURS NOW
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours),hasDescendant(withText("Vincent")))).check(withItemCount(ITEMS_COUNT - 1 ));
        //CLICK DELETE BUTTON AT POSITION 1 (CHLOE CAUSE JACK IS ALREADY DELETED)
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours),hasDescendant(withText("Vincent"))))
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
                                if (btnDelete != null) {
                                    btnDelete.performClick();
                                }
                            }
                        })
                );
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours),hasDescendant(withText("Vincent")))).check(withItemCount(ITEMS_COUNT - 2));
    }

    @Test
    public void neighbourListFav_shouldShowOnlyFavNeighbour() {
        //LIST FAVORITE NEIGHBOUR EMPTY
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours),hasChildCount(0)))
                .check(matches(hasChildCount(0)));
        //CLICK ON FIRST NEIGHBOUR (CAROLINE)
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours),hasDescendant(withText("Vincent"))))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //ADD AS FAVORITE
        onView(ViewMatchers.withId(R.id.add_favorite)).perform(click());
        //GO BACK
        onView(ViewMatchers.withId(R.id.backArrow)).perform(click());
        //CHANGE TAB
        onView(ViewMatchers.withId(R.id.container)).perform(swipeLeft());
        //LIST FAVORITE NEIGHBOUR COUNT ONE PERSON
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours), hasChildCount(1)))
                .check(matches(hasChildCount(1)));
        //CLICK ON ALONE NEIGHBOUR
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours),hasChildCount(1)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //CHECK IF FIRSTNAME IS CAROLINE
        onView(ViewMatchers.withId(R.id.firstname))
                .check(matches(withText("Caroline")));
    }
}