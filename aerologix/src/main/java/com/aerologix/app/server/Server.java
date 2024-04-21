package com.aerologix.app.server;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import com.aerologix.app.server.pojo.*;
import com.aerologix.app.server.jdo.*;

@Path("/aerologix")
@Produces(MediaType.APPLICATION_JSON)
public class Server {
    
    protected static final Logger logger = LogManager.getLogger();

    private PersistenceManager pm;
    private Transaction tx;

    public Server() {
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
        this.pm = pmf.getPersistenceManager();
        this.tx = pm.currentTransaction();
    }

    @POST
    @Path("/user/register")
    public Response registerUser(UserData userData) {
        try{
            tx.begin();
            logger.info("Checking if user '{}' already exists or not", userData.getEmail());
            User user = null;
            
            try { 
                user = pm.getObjectById(User.class, userData.getEmail());
            } catch(JDOObjectNotFoundException e) {
                logger.info("User with email '{}' does not exist in the database.", userData.getEmail());
            }

            if (user == null) {
                logger.info("Creating user...");
                user = new User(userData.getEmail(), userData.getPassword(), User.UserType.valueOf(userData.getUserType()), userData.getName());
                pm.makePersistent(user);
                logger.info("User created: {}", user);
                tx.commit();
                return Response.ok().build();
            } else {
                logger.error("Email '{}' is already in use", userData.getEmail());
                tx.commit();
                return Response.status(Response.Status.UNAUTHORIZED).entity("Email is already in use").build();
            }
            
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
        }
    }

    @POST
    @Path("/user/login")
    public Response login(LoginData loginData) {
        try{
            tx.begin();
            logger.info("Checking if the user '{}' already exists or not...", loginData.getEmail());
            User user = null;
            
            try { 
                user = pm.getObjectById(User.class, loginData.getEmail());
            } catch(JDOObjectNotFoundException e) {
                logger.info("User with email '{}' does not exist in the database.", loginData.getEmail());
            }

            tx.commit();

            // If user exists
            if (user != null) {
                logger.info("Checking credentials...");
                // If client provided password matches: return OK status
                if(user.getPassword().equals(loginData.getPassword())){
                    logger.info("Login succesful for user '{}'", user.getEmail());
                    return Response.ok().build();
                } else {    // If password does not match: return UNAUTHORIZED status
                    return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
                }
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
            }
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
        }
    }

}
