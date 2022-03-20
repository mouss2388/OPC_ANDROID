package com.cleanup.todoc;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.cleanup.todoc.controller.activity.MainActivity;
import com.cleanup.todoc.database.AppDatabase;
import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.database.model.Project;
import com.cleanup.todoc.database.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @author Gaëtan HERFRAY
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    private AppDatabase database;
    private TaskDao taskDao;
    private ProjectDao projectDao;

    private final Project PROJECT_DEMO = new Project("Tartampion", 0xFFEADAD1);
    private final Task TASK_DEMO = new Task(1, "tâche 1", new Date().getTime());


    @Before
    public void createDb() {
        this.database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                AppDatabase.class)
                .allowMainThreadQueries()
                .build();

        taskDao = database.taskDao();
        projectDao = database.projectDao();
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void addAndRemoveTask() {

        long projectId = this.projectDao.insert(PROJECT_DEMO);
        PROJECT_DEMO.setId(projectId);

//        Task task = new Task(projectId, "tâche 1", new Date().getTime());

        try {
            List<Task> listBeforeInsertion = LiveDataTestUtil.getValue(this.taskDao.getAllTasks());

            long taskId = this.taskDao.insert(TASK_DEMO);
            TASK_DEMO.setId(taskId);

            List<Task> listAfterInsertion = LiveDataTestUtil.getValue(this.taskDao.getAllTasks());
            Task taskInserted = LiveDataTestUtil.getValue(this.taskDao.getTaskById(taskId));

            assertEquals(listBeforeInsertion.size() + 1, listAfterInsertion.size());

            this.taskDao.delete(taskInserted.getId());

            assertEquals(0, LiveDataTestUtil.getValue(this.taskDao.getAllTasks()).size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //    @Test
//    public void addAndRemoveTask() {
//        MainActivity activity = rule.getActivity();
//        TextView lblNoTask = activity.findViewById(R.id.lbl_no_task);
//        RecyclerView listTasks = activity.findViewById(R.id.list_tasks);
//
//        onView(withId(R.id.fab_add_task)).perform(click());
//        onView(withId(R.id.txt_task_name)).perform(replaceText("Tâche example"));
//        onView(withId(android.R.id.button1)).perform(click());
//
//        // Check that lblTask is not displayed anymore
//        assertThat(lblNoTask.getVisibility(), equalTo(View.GONE));
//        // Check that recyclerView is displayed
//        assertThat(listTasks.getVisibility(), equalTo(View.VISIBLE));
//        // Check that it contains one element only
//        assertThat(listTasks.getAdapter().getItemCount(), equalTo(1));
//
//        onView(withId(R.id.img_delete)).perform(click());
//
//        // Check that lblTask is displayed
//        assertThat(lblNoTask.getVisibility(), equalTo(View.VISIBLE));
//        // Check that recyclerView is not displayed anymore
//        assertThat(listTasks.getVisibility(), equalTo(View.GONE));
//    }
//
    @Test
    public void sortTasks() {

        long projectId = this.projectDao.insert(PROJECT_DEMO);
        PROJECT_DEMO.setId(projectId);

        Task taskAAA = new Task(projectId, "aaa Tâche example", new Date().getTime());
        long id = this.taskDao.insert(taskAAA);
        taskAAA.setId(id);

        Task taskZZZ = new Task(projectId, "zzz Tâche example", new Date().getTime());
        id = this.taskDao.insert(taskZZZ);
        taskZZZ.setId(id);

        Task taskHHH = new Task(projectId, "hhh Tâche example", new Date().getTime());
        id = this.taskDao.insert(taskHHH);
        taskHHH.setId(id);

        try {
            // Sort alphabetical

            List<Task> listAlphabetical = LiveDataTestUtil.getValue(this.taskDao.getAllTaskAZComparator());
            assertEquals("aaa Tâche example", listAlphabetical.get(0).getName());
            assertEquals("hhh Tâche example", listAlphabetical.get(1).getName());
            assertEquals("zzz Tâche example", listAlphabetical.get(2).getName());

            // Sort alphabetical inverted

            List<Task> listAlphabeticalInverted = LiveDataTestUtil.getValue(this.taskDao.getAllTaskZAComparator());
            assertEquals("zzz Tâche example", listAlphabeticalInverted.get(0).getName());
            assertEquals("hhh Tâche example", listAlphabeticalInverted.get(1).getName());
            assertEquals("aaa Tâche example", listAlphabeticalInverted.get(2).getName());

            // Sort old first

            List<Task> listOldFirst = LiveDataTestUtil.getValue(this.taskDao.getAllTaskOldComparator());
            assertEquals("aaa Tâche example", listOldFirst.get(0).getName());
            assertEquals("zzz Tâche example", listOldFirst.get(1).getName());
            assertEquals("hhh Tâche example", listOldFirst.get(2).getName());

            // Sort recent first

            List<Task> listRecentFirst = LiveDataTestUtil.getValue(this.taskDao.getAllTaskRecentComparator());
            assertEquals("hhh Tâche example", listRecentFirst.get(0).getName());
            assertEquals("zzz Tâche example", listRecentFirst.get(1).getName());
            assertEquals("aaa Tâche example", listRecentFirst.get(2).getName());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


//    @Test
//    public void sortTasks() {
//        MainActivity activity = rule.getActivity();
//
//        onView(withId(R.id.fab_add_task)).perform(click());
//        onView(withId(R.id.txt_task_name)).perform(replaceText("aaa Tâche example"));
//        onView(withId(android.R.id.button1)).perform(click());
//        onView(withId(R.id.fab_add_task)).perform(click());
//        onView(withId(R.id.txt_task_name)).perform(replaceText("zzz Tâche example"));
//        onView(withId(android.R.id.button1)).perform(click());
//        onView(withId(R.id.fab_add_task)).perform(click());
//        onView(withId(R.id.txt_task_name)).perform(replaceText("hhh Tâche example"));
//        onView(withId(android.R.id.button1)).perform(click());
//
//        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
//                .check(matches(withText("aaa Tâche example")));
//        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
//                .check(matches(withText("zzz Tâche example")));
//        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
//                .check(matches(withText("hhh Tâche example")));
//
//        // Sort alphabetical
//        onView(withId(R.id.action_filter)).perform(click());
//        onView(withText(R.string.sort_alphabetical)).perform(click());
//        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
//                .check(matches(withText("aaa Tâche example")));
//        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
//                .check(matches(withText("hhh Tâche example")));
//        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
//                .check(matches(withText("zzz Tâche example")));
//
//        // Sort alphabetical inverted
//        onView(withId(R.id.action_filter)).perform(click());
//        onView(withText(R.string.sort_alphabetical_invert)).perform(click());
//        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
//                .check(matches(withText("zzz Tâche example")));
//        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
//                .check(matches(withText("hhh Tâche example")));
//        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
//                .check(matches(withText("aaa Tâche example")));
//
//        // Sort old first
//        onView(withId(R.id.action_filter)).perform(click());
//        onView(withText(R.string.sort_oldest_first)).perform(click());
//        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
//                .check(matches(withText("aaa Tâche example")));
//        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
//                .check(matches(withText("zzz Tâche example")));
//        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
//                .check(matches(withText("hhh Tâche example")));
//
//        // Sort recent first
//        onView(withId(R.id.action_filter)).perform(click());
//        onView(withText(R.string.sort_recent_first)).perform(click());
//        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
//                .check(matches(withText("hhh Tâche example")));
//        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
//                .check(matches(withText("zzz Tâche example")));
//        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
//                .check(matches(withText("aaa Tâche example")));
//    }
}
