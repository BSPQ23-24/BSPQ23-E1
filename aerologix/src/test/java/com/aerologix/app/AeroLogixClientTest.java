package com.aerologix.app;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.server.pojo.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AeroLogixClientTest {
    AeroLogixClient client = new AeroLogixClient("127.0.0.1", "8080");
    @BeforeEach
    void setUp() {
        client.deleteUser("Test@Aerologix.com");
        client.deleteUser("Test2@Aerologix.com");
        client.deleteUser("Test3@Aerologix.com");
    }
    //////USERS///////
    @Test
    void registerUser() {
        int result;
        result = client.registerUser("Test@Aerologix.com", "Test", "ADMIN", "Test");
        assertEquals(result, 0);
        result = client.registerUser("Test@Aerologix.com", "Test", "ADMIN", "Test");
        assertEquals(result, -1);
        result = client.registerUser("Test2@Aerologix.com", "Test2", "COUNTER_CLERK", "Test2");
        assertEquals(result, 0);
    }

    @Test
    void login() {
        boolean result;
        client.registerUser("Test@Aerologix.com", "Test", "ADMIN", "Test");
        result = client.login("Test@Aerologix.com", "Test");
        assertTrue(result);
        result = client.login("Test@Aerologix.com", "Test2");
        assertFalse(result);
        client.registerUser("Test2@Aerologix.com", "Test2", "ADMIN", "Test");
        result = client.login("Test2@Aerologix.com", "Test2");
        assertTrue(result);
        result = client.login("Test3@Aerologix.com", "Test");
        assertFalse(result);
    }

    @Test
    void getUser() {
        UserData originalUser = new UserData(), resultUser;
        originalUser.setEmail("Test@Aerologix.com");
        originalUser.setPassword("Test");
        originalUser.setName("Test");
        originalUser.setUserType("ADMIN");

        client.registerUser(originalUser.getEmail(), originalUser.getPassword(), originalUser.getUserType(), originalUser.getName());
        resultUser = client.getUser("Test@Aerologix.com");

        assertNotNull(resultUser);
        assertEquals(originalUser.getEmail(), resultUser.getEmail());
        assertEquals(originalUser.getPassword(), resultUser.getPassword());
        assertEquals(originalUser.getName(), resultUser.getName());
        assertEquals(originalUser.getUserType(), resultUser.getUserType());
        assertNotEquals("Test2@Aerologix.com", resultUser.getEmail());
        assertNull(client.getUser("Test2@Aerologix.com"));
    }

    @Test
    void getAllUsers() {
        ArrayList<UserData> resultArray;
        client.registerUser("Test@Aerologix.com", "Test", "COUNTER_CLERK", "Test");
        client.registerUser("Test2@Aerologix.com", "Test2", "COUNTER_CLERK", "Test2");
        resultArray = client.getAllUsers();
        assertFalse(resultArray.isEmpty());
        assertEquals(2, resultArray.size());
        assertEquals("Test2@Aerologix.com", resultArray.get(0).getEmail());
        assertEquals("Test2", resultArray.get(0).getPassword());
        assertEquals("Test@Aerologix.com", resultArray.get(1).getEmail());
        assertNotEquals("Test2@Aerologix.com", resultArray.get(1).getEmail());
        client.registerUser("Test3@Aerologix.com", "Test3", "ADMIN", "Test3");
        resultArray = client.getAllUsers();
        assertEquals(2, resultArray.size()); //As admins are not received
        assertEquals("Test@Aerologix.com", resultArray.get(1).getEmail());
    }

    @Test
    void modifyUser() {
        int result;
        client.registerUser("Test@Aerologix.com", "Test", "ADMIN", "Test");
        result = client.modifyUser("Test@Aerologix.com", "Test2", "COUNTER_CLERK", "Test2");
        assertEquals(result, 0);
        result = client.modifyUser("Test2@Aerologix.com", "Test2", "COUNTER_CLERK", "Test2");
        assertEquals(result, -1);
        UserData resultUser;
        resultUser = client.getUser("Test@Aerologix.com");
        assertNotNull(resultUser);
        assertEquals(resultUser.getEmail(), "Test@Aerologix.com");
        assertNotEquals(resultUser.getName(), "Test");
        assertEquals(resultUser.getName(), "Test2");
        assertNotEquals(resultUser.getUserType(), "ADMIN");
        assertEquals(resultUser.getUserType(), "COUNTER_CLERK");

    }

    @Test
    void deleteUser() {
        int result;
        result = client.deleteUser("Test@Aerologix.com");
        assertEquals(-1, result);
        client.registerUser("Test@Aerologix.com", "Test", "COUNTER_CLERK", "Test");
        client.registerUser("Test2@Aerologix.com", "Test2", "COUNTER_CLERK", "Test");
        result = client.deleteUser("Test@Aerologix.com");
        assertEquals(0, result);
        ArrayList <UserData> resultArray;
        resultArray = client.getAllUsers();
        assertFalse(resultArray.isEmpty());
        assertEquals(1, resultArray.size());
        assertEquals("Test2@Aerologix.com", resultArray.get(0).getEmail());
    }
}