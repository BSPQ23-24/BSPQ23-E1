package com.aerologix.app;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.client.controller.UserController;
import com.aerologix.app.server.pojo.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AeroLogixClientTest {
    UserController userController = UserController.getInstance();
    @BeforeEach
    void setUp() {
        userController.deleteUser("Test@Aerologix.com");
        userController.deleteUser("Test2@Aerologix.com");
        userController.deleteUser("Test3@Aerologix.com");
    }
    //////USERS///////
    @Test
    void registerUser() {
        int result;
        result = userController.registerUser("Test@Aerologix.com", "Test", "ADMIN", "Test");
        assertEquals(result, 0);
        result = userController.registerUser("Test@Aerologix.com", "Test", "ADMIN", "Test");
        assertEquals(result, -1);
        result = userController.registerUser("Test2@Aerologix.com", "Test2", "COUNTER_CLERK", "Test2");
        assertEquals(result, 0);
    }

    @Test
    void login() {
        boolean result;
        userController.registerUser("Test@Aerologix.com", "Test", "ADMIN", "Test");
        result = userController.login("Test@Aerologix.com", "Test");
        assertTrue(result);
        result = userController.login("Test@Aerologix.com", "Test2");
        assertFalse(result);
        userController.registerUser("Test2@Aerologix.com", "Test2", "ADMIN", "Test");
        result = userController.login("Test2@Aerologix.com", "Test2");
        assertTrue(result);
        result = userController.login("Test3@Aerologix.com", "Test");
        assertFalse(result);
    }

    @Test
    void getUser() {
        UserData originalUser = new UserData(), resultUser;
        originalUser.setEmail("Test@Aerologix.com");
        originalUser.setPassword("Test");
        originalUser.setName("Test");
        originalUser.setUserType("ADMIN");

        userController.registerUser(originalUser.getEmail(), originalUser.getPassword(), originalUser.getUserType(), originalUser.getName());
        resultUser = userController.getUser("Test@Aerologix.com");

        assertNotNull(resultUser);
        assertEquals(originalUser.getEmail(), resultUser.getEmail());
        assertEquals(originalUser.getPassword(), resultUser.getPassword());
        assertEquals(originalUser.getName(), resultUser.getName());
        assertEquals(originalUser.getUserType(), resultUser.getUserType());
        assertNotEquals("Test2@Aerologix.com", resultUser.getEmail());
        assertNull(userController.getUser("Test2@Aerologix.com"));
    }

    @Test
    void getAllUsers() {
        ArrayList<UserData> resultArray;
        userController.registerUser("Test@Aerologix.com", "Test", "COUNTER_CLERK", "Test");
        userController.registerUser("Test2@Aerologix.com", "Test2", "COUNTER_CLERK", "Test2");
        resultArray = userController.getAllUsers();
        assertFalse(resultArray.isEmpty());
        assertEquals(2, resultArray.size());
        assertEquals("Test2@Aerologix.com", resultArray.get(0).getEmail());
        assertEquals("Test2", resultArray.get(0).getPassword());
        assertEquals("Test@Aerologix.com", resultArray.get(1).getEmail());
        assertNotEquals("Test2@Aerologix.com", resultArray.get(1).getEmail());
        userController.registerUser("Test3@Aerologix.com", "Test3", "ADMIN", "Test3");
        resultArray = userController.getAllUsers();
        assertEquals(2, resultArray.size()); //As admins are not received
        assertEquals("Test@Aerologix.com", resultArray.get(1).getEmail());
    }

    @Test
    void modifyUser() {
        int result;
        userController.registerUser("Test@Aerologix.com", "Test", "ADMIN", "Test");
        result = userController.modifyUser("Test@Aerologix.com", "Test2", "COUNTER_CLERK", "Test2");
        assertEquals(result, 0);
        result = userController.modifyUser("Test2@Aerologix.com", "Test2", "COUNTER_CLERK", "Test2");
        assertEquals(result, -1);
        UserData resultUser;
        resultUser = userController.getUser("Test@Aerologix.com");
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
        result = userController.deleteUser("Test@Aerologix.com");
        assertEquals(-1, result);
        userController.registerUser("Test@Aerologix.com", "Test", "COUNTER_CLERK", "Test");
        userController.registerUser("Test2@Aerologix.com", "Test2", "COUNTER_CLERK", "Test");
        result = userController.deleteUser("Test@Aerologix.com");
        assertEquals(0, result);
        ArrayList <UserData> resultArray;
        resultArray = userController.getAllUsers();
        assertFalse(resultArray.isEmpty());
        assertEquals(1, resultArray.size());
        assertEquals("Test2@Aerologix.com", resultArray.get(0).getEmail());
    }
}