package com.clarivate.UserAPI.infraestructure.sourceOnTrue;

import static org.junit.jupiter.api.Assertions.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.clarivate.UserAPI.domain.model.TimeProgresed;
import com.clarivate.UserAPI.domain.model.User;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.staticUsers.StaticUsersRequester;

public class StaticUsersRequesterTest {

    @Test
    public void testGetAllUsersStatic(){
        // GIVEN
        StaticUsersRequester workUserMocks = new StaticUsersRequester();
        // WHEN
        List<User> staticUsers = workUserMocks.getUsers();

        // THEN
        assertNotNull(staticUsers);
        assertEquals(20, staticUsers.size());
    }

    @Test
    public void testGetUsersStaticRandom(){
        // GIVEN
        Integer numberUsers = 8;
        StaticUsersRequester workUserMocks = new StaticUsersRequester();
        // WHEN
        List<User> staticUsers = workUserMocks.getRandomSubList(numberUsers);

        // THEN
        assertNotNull(staticUsers);
        assertEquals(numberUsers, staticUsers.size());
    }

    @Test
    public void testGetUsersStaticRandomEmpty(){
        // GIVEN
        Integer numberUsers = 0;
        StaticUsersRequester workUserMocks = new StaticUsersRequester();
        // WHEN
        List<User> staticUsers = workUserMocks.getRandomSubList(numberUsers);

        // THEN
        assertNotNull(staticUsers);
        assertEquals(numberUsers, staticUsers.size());
    }
}
