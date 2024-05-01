package com.aerologix.app.server.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;

import javax.jdo.Extent;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.aerologix.app.server.jdo.Passenger;
import com.aerologix.app.server.jdo.User;
import com.aerologix.app.server.jdo.User.UserType;
import com.aerologix.app.server.pojo.UserData;
import com.aerologix.app.server.pojo.LoginData;

import jakarta.ws.rs.core.Response;

public class UserServiceTest {
	
	private UserService userService;
    
    private PersistenceManagerFactory pmfMock;
	
    private PersistenceManager pmMock;

    private Transaction txMock;
	
    @BeforeEach
    public void setUp() {
        pmfMock = mock(PersistenceManagerFactory.class);
        pmMock = mock(PersistenceManager.class);
        txMock = mock(Transaction.class);

        when(pmfMock.getPersistenceManager()).thenReturn(pmMock);
        when(pmMock.currentTransaction()).thenReturn(txMock);

        userService = new UserService();
        userService.setPersistenceManagerFactory(pmfMock);
        userService.setPersistenceManager(pmMock); 
        userService.setTransaction(txMock);
        
    }
	@Test
	public void testRegisterUser() {
        UserData userData = new UserData();
        userData.setEmail("test1@aerologix.com");
        userData.setPassword("testpass");
        userData.setUserType("COUNTER_CLERK");
        userData.setName("testName");
        
        User user = null;
        when(pmMock.getObjectById(User.class, userData.getEmail())).thenReturn(user);
        Response response = userService.registerUser(userData);
        
        assertEquals(Response.Status.OK, response.getStatusInfo());
	}
	
	@Test
	public void testRegisterUserAlreadyRegistered() {
        UserData userData = new UserData();
        userData.setEmail("test@aerologix.com");
        userData.setPassword("testpass");
        userData.setUserType("COUNTER_CLERK");
        userData.setName("testName");

        User user = mock(User.class);
        when(pmMock.getObjectById(User.class, userData.getEmail())).thenReturn(user);
        Response response = userService.registerUser(userData);
        
        assertEquals(Response.Status.UNAUTHORIZED, response.getStatusInfo());
	}
	
	@Test
	public void testLogin() {
        LoginData loginData = new LoginData();
        loginData.setEmail("test@aerologix.com");
        loginData.setPassword("testpass");
        
        User user = new User();
        user.setEmail("test@aerologix.com");
        user.setPassword("testpass");
        when(pmMock.getObjectById(User.class, loginData.getEmail())).thenReturn(user);
        Response response = userService.login(loginData);
        
        assertEquals(Response.Status.OK, response.getStatusInfo());
	}
	
	@Test
	public void testLoginWrongPassword() {
        LoginData loginData = new LoginData();
        loginData.setEmail("test@aerologix.com");
        loginData.setPassword("testpass");
        
        User user = new User();
        user.setEmail("test@aerologix.com");
        user.setPassword("testInvalidPass");
        when(pmMock.getObjectById(User.class, loginData.getEmail())).thenReturn(user);
        
        Response response = userService.login(loginData);
        assertEquals(Response.Status.UNAUTHORIZED, response.getStatusInfo());
	}
	
	@Test
	public void testLoginUserNotRegistered() {
        LoginData loginData = new LoginData();
        loginData.setEmail("test@aerologix.com");
        loginData.setPassword("testpass");
        
        User user = null;
        when(pmMock.getObjectById(User.class, loginData.getEmail())).thenReturn(user);
        
        Response response = userService.login(loginData);
        assertEquals(Response.Status.UNAUTHORIZED, response.getStatusInfo());
	}
	
	@Test
	public void testModifyUser() {
        UserData userData = new UserData();
        userData.setEmail("test@aerologix.com");
        userData.setPassword("test2pass");
        userData.setUserType("ADMIN");
        userData.setName("test2Name");
        
        User user = mock(User.class);
        when(pmMock.getObjectById(User.class, userData.getEmail())).thenReturn(user);
        
        Response response = userService.modifyUser(userData);
        assertEquals(Response.Status.OK, response.getStatusInfo());
        verify(user, times(1)).setName("test2Name");
        verify(user, times(1)).setUserType(UserType.ADMIN);
	}
	
	@Test
	public void testModifyUserNotRegistered() {
        UserData userData = new UserData();
        userData.setEmail("test@aerologix.com");
        userData.setPassword("test2pass");
        userData.setUserType("ADMIN");
        userData.setName("test2Name");
        
        User user = null;
        when(pmMock.getObjectById(User.class, userData.getEmail())).thenReturn(user);
        
        Response response = userService.modifyUser(userData);
        assertEquals(Response.Status.UNAUTHORIZED, response.getStatusInfo());
	}
	
	@Test
	public void testDeleteUser() {
        UserData userData = new UserData();
        userData.setEmail("test@aerologix.com");
        userData.setPassword("testpass");
        userData.setUserType("ADMIN");
        userData.setName("testName");
        
        User user = mock(User.class);
        when(pmMock.getObjectById(User.class, userData.getEmail())).thenReturn(user);
        
        Response response = userService.deleteUser(userData.getEmail());
        assertEquals(Response.Status.OK, response.getStatusInfo());
	}
	
	@Test
	public void testDeleteUserNotRegistered() {
        UserData userData = new UserData();
        userData.setEmail("test@aerologix.com");
        userData.setPassword("testpass");
        userData.setUserType("ADMIN");
        userData.setName("testName");
        
        User user = null;
        when(pmMock.getObjectById(User.class, userData.getEmail())).thenReturn(user);
        
        Response response = userService.deleteUser(userData.getEmail());
        assertEquals(Response.Status.UNAUTHORIZED, response.getStatusInfo());
	}
	
	@Test
	public void testGetUser() {
        String email = "test@aerologix.com";
        
        User user = new User();
        user.setEmail("test@aerologix.com");
        user.setPassword("testpass");
        user.setUserType(UserType.ADMIN);
        user.setName("testName");
        when(pmMock.getObjectById(User.class, email)).thenReturn(user);
        
        Response response = userService.getUser(email);
        assertEquals(Response.Status.OK, response.getStatusInfo());
        UserData assertData = new UserData();
        assertData.setEmail("test@aerologix.com");
        assertData.setPassword("testpass");
        assertData.setUserType("ADMIN");
        assertData.setName("testName");
        UserData responseData = (UserData) response.getEntity();
        assertEquals(assertData.getName(), responseData.getName());
	}
	
	@Test
	public void testGetUserNotExisting() {
        String email = "test@aerologix.com";
        
        User user = null;
        when(pmMock.getObjectById(User.class, email)).thenReturn(user);
        
        Response response = userService.getUser(email);
        assertEquals(Response.Status.NOT_FOUND, response.getStatusInfo());
	}
	
	@Test
    public void testGetAllUsers() {
        Extent<User> extentMock = mock(Extent.class);
        Iterator<User> iteratorMock = mock(Iterator.class);
        User user = new User();
        user.setEmail("test@aerologix.com");
        user.setPassword("testpass");
        user.setUserType(UserType.COUNTER_CLERK);
        user.setName("testName");

        when(pmMock.getExtent(User.class, true)).thenReturn(extentMock);
        when(extentMock.iterator()).thenReturn(iteratorMock);
        when(iteratorMock.hasNext()).thenReturn(true, true, false);
        when(iteratorMock.next()).thenReturn(user);

        Response response = userService.getAllUsers();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        ArrayList<UserData> responseList = (ArrayList<UserData>) response.getEntity();
        assertFalse(responseList.isEmpty());
        assertEquals(2, responseList.size());
    }
	
	@Test
    public void testGetAllUsersEmpty() {
        Extent<User> extentMock = mock(Extent.class);
        Iterator<User> iteratorMock = mock(Iterator.class);
        User user = null;

        when(pmMock.getExtent(User.class, true)).thenReturn(extentMock);
        when(extentMock.iterator()).thenReturn(iteratorMock);
        when(iteratorMock.hasNext()).thenReturn(false);
        when(iteratorMock.next()).thenReturn(user);

        Response response = userService.getAllUsers();
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }


}
