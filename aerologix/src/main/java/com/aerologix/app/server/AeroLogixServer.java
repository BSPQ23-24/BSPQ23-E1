package com.aerologix.app.server;

import javax.jdo.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;


/**
 * @brief RESTful web service endpoint that provides functionalities for the AeroLogix application.
 * 
 * AeroLogixServer is a RESTful web service endpoint that provides functionalities 
 * for the AeroLogix application. It uses JDO (Java Data Objects) for persistence 
 * and Apache Log4j for logging.
 * 
 * <p>This class follows the Singleton design pattern to ensure that only one 
 * instance of the server exists. It initializes the PersistenceManagerFactory 
 * from the "datanucleus.properties" configuration file.</p>
 * 
 * <p>The server is accessible at the "/aerologix" path and produces JSON responses.</p>
 * 
 * <p><strong>Usage:</strong></p>
 * <pre>
 * {@code
 * AeroLogixServer server = AeroLogixServer.getInstance();
 * PersistenceManagerFactory pmf = server.getPersistenceManagerFactory();
 * }
 * </pre>
 * 
 * @see javax.jdo.PersistenceManagerFactory
 * @see org.apache.logging.log4j.Logger
 * @see jakarta.ws.rs.Path
 * @see jakarta.ws.rs.Produces
 * @see jakarta.ws.rs.core.MediaType
 */
@Path("/aerologix")
@Produces(MediaType.APPLICATION_JSON)
public class AeroLogixServer {

	/**
     * Logger instance for logging events in the AeroLogixServer.
     */
	protected static final Logger logger = LogManager.getLogger();

	/**
     * Singleton instance of the AeroLogixServer.
     */
	private static AeroLogixServer instance;

	/**
     * PersistenceManagerFactory instance for managing JDO persistence operations.
     */
	private PersistenceManagerFactory pmf;

	/**
     * Private constructor to initialize the PersistenceManagerFactory.
     * Loads the configuration from "datanucleus.properties".
     */
	private AeroLogixServer() {
		this.pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
	}

	/**
     * Returns the singleton instance of AeroLogixServer. If the instance does not 
     * exist, it creates a new one.
     * 
     * @return the singleton instance of AeroLogixServer
     */
	public static AeroLogixServer getInstance() {
        if (instance == null) {
            instance = new AeroLogixServer();
        }
        return instance;
    }

	/**
     * Returns the PersistenceManagerFactory used by this server.
     * 
     * @return the PersistenceManagerFactory instance
     */
	public PersistenceManagerFactory getPersistenceManagerFactory() {
		return this.pmf;
	}
	
}


