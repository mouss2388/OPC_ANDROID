package com.example.projet_7;

import static org.junit.Assert.assertEquals;

import com.example.projet_7.manager.UserManager;
import com.example.projet_7.model.User;
import com.google.android.gms.tasks.Task;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void getUsersData() {
        UserManager userManager = Mockito.mock(UserManager.class);
        Task task = Mockito.mock(Task.class);

        User user = new User();

        Mockito.when(userManager.getUserData()).thenReturn(task);

        Mockito.when(task.getResult()).thenReturn(user);

        assertEquals(user, userManager.getUserData().getResult());
    }
}