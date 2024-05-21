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
/**
 * @brief Service class for managing user-related operations.
 * 
 * <p>
 * This class provides CRUD operations for users.
 */
@Path("/aerologix")
@Produces(MediaType.APPLICATION_JSON)
public class UserService {
	/** Logger for logging messages. */
    protected static final Logger logger = LogManager.getLogger();
    /** PersistenceManagerFactory for creating PersistenceManager instances. */
    private PersistenceManagerFactory pmf;
    /** PersistenceManager for managing JDO operations. */
    private PersistenceManager pm;
    /** Transaction for managing database transactions. */
    private Transaction tx;
    
    /**
     * Default constructor initializing persistence manager and transaction.
     */
    public UserService() {
		this.pmf = AeroLogixServer.getInstance().getPersistenceManagerFactory();
		this.pm = pmf.getPersistenceManager();
		this.tx = pm.currentTransaction();
    }

    /**
     * Creates a new user.
     *
     * @param userData The data of the data to create.
     * @return A Response indicating the result of the creation operation.
     *<p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Attempt to retrieve all the required information to create the user using the provided {@link UserData}.</li>
     *     <li>If all the information can be found:
     *     		<ul>
	 *             <li>We create a {@link User} using all the data.</li>
	 *             <li>We make the user persistent in the database.</li>
	 *             <li>Commit the transaction and return the UserData object(Finalizing all the changes made during the transaction and making them permanent in the database)</li>
	 *         	</ul>
     *     <li>If something from the user data does not exist, return a 401 UNAUTHORIZED.</li>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
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
	/**
	 * Handles user login requests.
	 *
	 * @param loginData The login data containing the user's email and password.
	 * @return A Response indicating the result of the login attempt.
	 *<p>
	 * This method performs the following steps:
	 * <ul>
	 *     <li>Begin a new transaction (We will take the sequence of steps as a single unit of work).</li>
	 *     <li>Attempt to retrieve the user from the database using the provided {@link LoginData}.</li>
	 *     <li>If the user exists:
	 *         <ul>
	 *             <li>Check if the provided password matches the stored password.</li>
	 *             <li>If the password matches, commit the transaction and return a 200 OK response.</li>
	 *             <li>If the password does not match, return a 401 UNAUTHORIZED response with "Invalid credentials".</li>
	 *         </ul>
	 *     </li>
	 *     <li>If the user does not exist, return a 401 UNAUTHORIZED response with "Invalid credentials".</li>
	 *     <li>RollBack the transaction if an exception occurs (Reverting the changes made during a transaction).</li>
	 * </ul>
	 */
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
    /**
     * Modifies an existing user.
     *
     * @param userData The data of the user to modify.
     * @return A Response indicating the result of the modification operation.
     *<p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Attempt to retrieve the user from the database using the provided email.</li>
     *     <li>Attempt to retrieve all the required information to modify the user using the provided {@link UserData}.</li>
     *     <li>If all the information can be found, we modify the data from the {@link User} using all the data.</li>
     *     <li>Commit the transaction and return the UserData object(Finalizing all the changes made during the transaction and making them permanent in the database)</li>
     *     <li>If something from the user data does not exist, return a 401 Unauthorized.</li>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
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
    /**
     * Deletes an existing user by email.
     *
     * @param id The email of the user to delete.
     * @return A Response indicating the result of the deletion operation.
      <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Attempt to retrieve the user from the database using the provided email.</li>
     *     <li>If the user exists, we delete the user from the database.</li>
     *     <li>Commit the transaction and return the UserrData object(Finalizing all the changes made during the transaction and making them permanent in the database).</li>
     *     <li>If the user does not exist, return a 401 Unauthorized.</li>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
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
    /**
     * Retrieves user details for a given user email.
     *
     * @param email The email of the user to retrieve.
     * @return A Response containing the user details or an error message.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Attempt to retrieve the user from the database using the provided ID.</li>
     *     <li>If the user exists, construct a UserData object containing the user details.</li>
     *     <li>Commit the transaction and return the UserData object(Finalizing all the changes made during the transaction and making them permanent in the database).</li>
     *     <li>If the user does not exist, return a 404 Not Found response.</li>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
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
	/**
     * Retrieves all users.
     *
     * @return A Response containing a list of all users or an error message.
     * <p>
     * <ul>
     *     <li>Begin a new transaction(We will take the sequence of steps as a single unit of work).</li>
     *     <li>Retrieve all users from the database.</li>
     *     <li>For each user, construct a UserData object containing the user.</li>
     *     <li>Commit the transaction and return the list of UserData objects.(Finalizing all the changes made during the transaction and making them permanent in the database)</li>
     *     <li>If no users are found, return a 204 No Content response.</li>
     *     <li>RollBack the transaction if an exception occurs(Reverting the changes made during a transaction).</li>
     * </ul>
     */
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
	
	  /**
     * Sets the persistence manager factory.
     * <p>
     * This method is primarily used for testing purposes.
     *
     * @param pmf The {@link PersistenceManagerFactory} to set.
     */
    void setPersistenceManagerFactory(PersistenceManagerFactory pmf) {
        this.pmf = pmf;
    }

    /**
     * Sets the persistence manager.
     * <p>
     * This method is primarily used for testing purposes.
     *
     * @param pm The {@link PersistenceManager} to set.
     */
    void setPersistenceManager(PersistenceManager pm) {
        this.pm = pm;
    }
    /**
     * Sets the transaction.
     * <p>
     * This method is primarily used for testing purposes.
     *
     * @param tx The {@link Transaction} to set.
     */
	void setTransaction(Transaction tx) {
		this.tx = tx;
	}

}
