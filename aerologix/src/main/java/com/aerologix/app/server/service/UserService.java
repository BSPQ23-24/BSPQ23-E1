package com.aerologix.app.server.service;

import java.util.ArrayList;
import java.util.Iterator;

import javax.jdo.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import com.aerologix.app.server.pojo.*;
import com.aerologix.app.server.AeroLogixServer;
import com.aerologix.app.server.jdo.*;
import com.aerologix.app.server.jdo.User.UserType;

@Path("/aerologix")
@Produces(MediaType.APPLICATION_JSON)
public class UserService {
    
    protected static final Logger logger = LogManager.getLogger();

	private PersistenceManagerFactory pmf;
	private PersistenceManager pm;
	private Transaction tx;

    public UserService() {
		this.pmf = AeroLogixServer.getInstance().getPersistenceManagerFactory();
		this.pm = pmf.getPersistenceManager();
		this.tx = pm.currentTransaction();
    }

    //  CRUD methods

	@POST
	@Path("/user/register")
	public Response registerUser(UserData userData) {
		try {
			tx.begin();
			logger.info("Checking if user '{}' already exists or not", userData.getEmail());
			User user = null;

			try {
				user = pm.getObjectById(User.class, userData.getEmail());
			} catch (JDOObjectNotFoundException e) {
				logger.info("User with email '{}' does not exist in the database.", userData.getEmail());
			}

			if (user == null) {
				logger.info("Creating user...");
				user = new User(userData.getEmail(), userData.getPassword(),
						User.UserType.valueOf(userData.getUserType()), userData.getName());
				pm.makePersistent(user);
				logger.info("User created: {}", user);
				tx.commit();
				return Response.ok().build();
			} else {
				logger.error("Email '{}' is already in use", userData.getEmail());
				tx.commit();
				return Response.status(Response.Status.UNAUTHORIZED).entity("Email is already in use").build();
			}

		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@POST
	@Path("/user/login")
	public Response login(LoginData loginData) {
		try {
			tx.begin();
			logger.info("Checking if the user '{}' already exists or not...", loginData.getEmail());
			User user = null;

			try {
				user = pm.getObjectById(User.class, loginData.getEmail());
			} catch (JDOObjectNotFoundException e) {
				logger.info("User with email '{}' does not exist in the database.", loginData.getEmail());
			}

			tx.commit();

			// If user exists
			if (user != null) {
				logger.info("Checking credentials...");
				// If client provided password matches: return OK status
				if (user.getPassword().equals(loginData.getPassword())) {
					logger.info("Login succesful for user '{}'", user.getEmail());
					return Response.ok().build();
				} else { // If password does not match: return UNAUTHORIZED status
					return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
				}
			} else {
				return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@POST
	@Path("/user/modify")
	public Response modifyUser(UserData userData) {
		try {
			tx.begin();
			// Get current user data
			logger.info("Get the current user data for: {}", userData.getEmail());
			User user = null;
			try {
				user = pm.getObjectById(User.class, userData.getEmail());
			} catch (JDOObjectNotFoundException e) {
				logger.info("User with email '{}' does not exist in the database.", userData.getEmail());
			}

			if (user != null) {
				// Modify all data except primary key (email)
				user.setName(userData.getName());
				user.setPassword(userData.getPassword());
				user.setUserType(UserType.valueOf(userData.getUserType()));
				logger.info("User modified: {}", user);
				tx.commit();
				return Response.ok().build();
			} else {
				logger.error("There is no user registered with email: {}", userData.getEmail());
				tx.commit();
				return Response.status(Response.Status.UNAUTHORIZED).entity("No user found").build();
			}

		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@POST
	@Path("/user/delete")
	public Response deleteUser(String email) {
		try {
			tx.begin();
			logger.info("Checking if user {} exists in the database...", email);
			User user = null;
			try {
				user = pm.getObjectById(User.class, email);
			} catch (JDOObjectNotFoundException e) {
				logger.info("User with email '{}' does not exist in the database.", email);
			}

			if (user != null) {
				// Delete user
				pm.deletePersistent(user);
				logger.info("User deleted: {}", email);
				tx.commit();
				return Response.ok().build();
			} else {
				logger.error("There is no user registered with email: {}", email);
				tx.commit();
				return Response.status(Response.Status.UNAUTHORIZED).entity("No user found").build();
			}

		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@GET
	@Path("/user/get")
	public Response getUser(@QueryParam("email") String email) {
		try {
			tx.begin();
			logger.info("Checking if the user '{}' already exists or not...", email);
			User user = null;

			try {
				user = pm.getObjectById(User.class, email);
			} catch (JDOObjectNotFoundException e) {
				logger.info("User with email '{}' does not exist in the database.", email);
			}

			tx.commit();

			// If user exists
			if (user != null) {
				UserData userData = new UserData();
				userData.setEmail(user.getEmail());
				userData.setPassword(user.getPassword());
				userData.setUserType(user.getUserType().name());
				userData.setName(user.getName());

				logger.info("Sending userData to client...");
				return Response.ok().entity(userData).build();
			} else {
				logger.info("User not found");
				return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@GET
	@Path("/user/getAll")
	public Response getAllUsers() {
		try {
			tx.begin();

			ArrayList<UserData> userList = new ArrayList<UserData>();

			logger.info("Retrieving all users from database...");

			Extent<User> e = pm.getExtent(User.class, true);
			Iterator<User> iter = e.iterator();
			while (iter.hasNext()) {
				User user = (User) iter.next();
				if (!user.getUserType().name().equals("ADMIN")) {
					UserData userData = new UserData();
					userData.setEmail(user.getEmail());
					userData.setPassword(user.getPassword());
					userData.setUserType(user.getUserType().name());
					userData.setName(user.getName());
					userList.add(userData);
				}
			}
			tx.commit();

			if (userList.size() > 0) {
				logger.info("Sending all users to client...");
				return Response.ok().entity(userList).build();
			} else {
				logger.error("No users registered");
				return Response.status(Response.Status.NO_CONTENT).entity("No users registered").build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}
	
	// Other methods

		// In UserService class
	    void setPersistenceManagerFactory(PersistenceManagerFactory pmf) {
	        this.pmf = pmf;
	    }

	    // Similarly, create a setter for the PersistenceManager if necessary
	    void setPersistenceManager(PersistenceManager pm) {
	        this.pm = pm;
	    }

		void setTransaction(Transaction tx) {
			this.tx = tx;
		}


}
