package com.example.mareu;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringContains.containsString;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.mareu.controllers.activity.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MeetingsListTest {

    private View decorView;
    private final int MEETINGS_COUNT = 3;

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        rule.getScenario().onActivity(activity -> decorView = activity.getWindow().getDecorView());
    }

    @Test
    public void addOneMeetingWithSuccess() {

        onView(withId(R.id.recyclerView)).check(matches(isDisplayed())).check(matches(hasChildCount(MEETINGS_COUNT)));
        onView(withId(R.id.btn_addReu)).check(matches(isDisplayed()))
                .perform(click());

        onView((withId(R.id.addMeeting))).check(matches(isDisplayed()));

        ///INPUTTEXT///

        onView(
                allOf(
                        isDescendantOfA(withId(R.id.textFieldSubject)),
                        isAssignableFrom(EditText.class)))
                .perform(typeText("Reunion D"));

        onView(
                allOf(
                        isDescendantOfA(withId(R.id.textFieldListEmails)),
                        isAssignableFrom(EditText.class)))
                .perform(typeText("test@gmail.com"));

        ///SPINNER ROOM///
        onView(withId(R.id.spinner_room)).check(matches(isDisplayed()))
                .perform(click());
        onView(withText("Toad")).perform(click());


        ///TIME PICKER///
        onView(withId(R.id.btn_time_picker)).check(matches(isDisplayed()))
                .perform(click());

        ViewInteraction materialButton = onView(
                Matchers.allOf(withId(R.id.material_timepicker_ok_button), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton.perform(click());

        onView((withId(R.id.btnAdd))).check(matches(isDisplayed())).perform(click());

        onView((withId(R.id.recyclerView))).check(matches(isDisplayed())).check(matches(hasChildCount(MEETINGS_COUNT + 1)));
    }

    @Test
    public void failedAddOneMeetingWithHourYetBooked() {

        onView(withId(R.id.recyclerView)).check(matches(isDisplayed())).check(matches(hasChildCount(MEETINGS_COUNT)));

        onView(withId(R.id.btn_addReu)).check(matches(isDisplayed()))
                .perform(click());

        onView((withId(R.id.addMeeting))).check(matches(isDisplayed()));

        ///INPUTTEXT///

        onView(
                allOf(
                        isDescendantOfA(withId(R.id.textFieldSubject)),
                        isAssignableFrom(EditText.class)))
                .perform(typeText("Reunion E"));

        onView(
                allOf(
                        isDescendantOfA(withId(R.id.textFieldListEmails)),
                        isAssignableFrom(EditText.class)))
                .perform(typeText("test@gmail.com"));

        ///SPINNER ROOM///
        onView(withId(R.id.spinner_room)).check(matches(isDisplayed()))
                .perform(click());
        onView(withText("Mario")).check(matches(isDisplayed())).perform(click());

        ///TIME PICKER///
        onView(withId(R.id.btn_time_picker)).check(matches(isDisplayed()))
                .perform(click());

        checkInputHourFilled("14", "25");

        onView(withText("Salle Mario non disponible pour cet horaire, une réunion dure environ 45 min \n veuillez changer de salle ou d'horaire")).inRoot(withDecorView(not(decorView))).check(matches(isDisplayed()));
    }

    @Test
    public void failedAddOneMeetingWithMidnightHourYetBooked() {

        onView(withId(R.id.recyclerView)).check(matches(isDisplayed())).check(matches(hasChildCount(MEETINGS_COUNT)));

        onView(withId(R.id.btn_addReu)).check(matches(isDisplayed()))
                .perform(click());

        onView((withId(R.id.addMeeting))).check(matches(isDisplayed()));

        ///INPUTTEXT///

        onView(
                allOf(
                        isDescendantOfA(withId(R.id.textFieldSubject)),
                        isAssignableFrom(EditText.class)))
                .perform(typeText("Reunion E"));

        onView(
                allOf(
                        isDescendantOfA(withId(R.id.textFieldListEmails)),
                        isAssignableFrom(EditText.class)))
                .perform(typeText("test@gmail.com"));

        ///SPINNER ROOM///
        onView(withId(R.id.spinner_room)).check(matches(isDisplayed()))
                .perform(click());
        onView(withText("Luigi")).check(matches(isDisplayed())).perform(click());

        ///TIME PICKER///
        onView(withId(R.id.btn_time_picker)).check(matches(isDisplayed()))
                .perform(click());

        checkInputHourFilled("23", "55");

        onView(withText("Salle Luigi non disponible pour cet horaire, une réunion dure environ 45 min \n veuillez changer de salle ou d'horaire")).inRoot(withDecorView(not(decorView))).check(matches(isDisplayed()));
    }


    @Test
    public void deleteOneMeeting() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed())).check(matches(hasChildCount(MEETINGS_COUNT)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(
                        0,
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
                                View btnDelete = view.findViewById(R.id.trash);
                                if (btnDelete != null) {
                                    btnDelete.performClick();
                                }
                            }
                        }));
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed())).check(matches(hasChildCount(MEETINGS_COUNT - 1)));
    }

    @Test
    public void filterMeetingByRoomWithSuccess() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed())).check(matches(hasChildCount(MEETINGS_COUNT)));

        ViewInteraction actionMenuItemView = onView(
                Matchers.allOf(withId(R.id.menu_activity_main_filter), withContentDescription("filtre"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.includeToolbar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction materialTextView = onView(
                Matchers.allOf(withId(R.id.title), withText("Filtre par Lieu"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.content),
                                        1),
                                0),
                        isDisplayed()));
        materialTextView.perform(click());

        onView(withText("Mario")).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.recyclerView)).check(matches(isDisplayed())).check(matches(hasChildCount(1)));

        onView(withId(R.id.recyclerView)).check(matches(hasItem(hasDescendant(withText( containsString("Mario"))))));
    }

    @Test
    public void filterMeetingByHourWithSuccess() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed())).check(matches(hasChildCount(MEETINGS_COUNT)));

        ViewInteraction actionMenuItemView = onView(
                Matchers.allOf(withId(R.id.menu_activity_main_filter), withContentDescription("filtre"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.includeToolbar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction materialTextView = onView(
                Matchers.allOf(withId(R.id.title), withText("Filtre par Heure"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.content),
                                        1),
                                0),
                        isDisplayed()));
        materialTextView.perform(click());

        checkInputHourFilled("16","00");

        onView(withId(R.id.recyclerView)).check(matches(isDisplayed())).check(matches(hasChildCount(1)));

        onView(withId(R.id.recyclerView)).check(matches(hasItem(hasDescendant(withText( containsString("16:00"))))));

    }

    @Test
    public void filterMeetingByRoomWithFailed() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed())).check(matches(hasChildCount(MEETINGS_COUNT)));

        ViewInteraction actionMenuItemView = onView(
                Matchers.allOf(withId(R.id.menu_activity_main_filter), withContentDescription("filtre"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.includeToolbar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction materialTextView = onView(
                Matchers.allOf(withId(R.id.title), withText("Filtre par Lieu"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.content),
                                        1),
                                0),
                        isDisplayed()));
        materialTextView.perform(click());

        onView(withText("Toad")).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.recyclerView)).check(matches(hasChildCount(0)));

        onView(withText(R.string.meeting_not_founded)).inRoot(withDecorView(not(decorView))).check(matches(isDisplayed()));
    }

    @Test
    public void filterMeetingByHourWithFailed() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed())).check(matches(hasChildCount(MEETINGS_COUNT)));

        ViewInteraction actionMenuItemView = onView(
                Matchers.allOf(withId(R.id.menu_activity_main_filter), withContentDescription("filtre"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.includeToolbar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction materialTextView = onView(
                Matchers.allOf(withId(R.id.title), withText("Filtre par Heure"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.content),
                                        1),
                                0),
                        isDisplayed()));
        materialTextView.perform(click());

        checkInputHourFilled("19","00");

        onView(withId(R.id.recyclerView)).check(matches(hasChildCount(0)));

        onView(withText(R.string.meeting_not_founded)).inRoot(withDecorView(not(decorView))).check(matches(isDisplayed()));

    }

    private void checkInputHourFilled(String hour, String minute) {
        ViewInteraction switchButton = onView(
                Matchers.allOf(withId(R.id.material_timepicker_mode_button), withContentDescription("Switch to text input mode for the time input."),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        switchButton.perform(click());

        ViewInteraction clickOnHourEditText = onView(
                Matchers.allOf(withText("00"),
                        childAtPosition(
                                Matchers.allOf(withId(R.id.material_hour_text_input),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        clickOnHourEditText.perform(click());

        ViewInteraction hourEditText = onView(
                Matchers.allOf(withId(R.id.material_timepicker_edit_text),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        hourEditText.perform(replaceText(hour), closeSoftKeyboard());


        ViewInteraction clickOnMinuteEditText = onView(
                Matchers.allOf(withText("00"),
                        childAtPosition(
                                Matchers.allOf(withId(R.id.material_minute_text_input),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                2)),
                                0),
                        isDisplayed()));
        clickOnMinuteEditText.perform(click());

        ViewInteraction minuteEditText = onView(
                Matchers.allOf(withId(R.id.material_timepicker_edit_text),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        minuteEditText.perform(replaceText(minute), closeSoftKeyboard());


        ViewInteraction validateButton = onView(
                Matchers.allOf(withId(R.id.material_timepicker_ok_button), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        validateButton.perform(click());
    }


    public static Matcher<View> hasItem(Matcher<View> matcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {

            @Override public void describeTo(Description description) {
                description.appendText("has item: ");
                matcher.describeTo(description);
            }

            @Override protected boolean matchesSafely(RecyclerView view) {
                RecyclerView.Adapter adapter = view.getAdapter();
                for (int position = 0; position < adapter.getItemCount(); position++) {
                    int type = adapter.getItemViewType(position);
                    RecyclerView.ViewHolder holder = adapter.createViewHolder(view, type);
                    adapter.onBindViewHolder(holder, position);
                    if (matcher.matches(holder.itemView)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}