package com.aerologix.app.server;

import javax.jdo.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("/aerologix")
@Produces(MediaType.APPLICATION_JSON)
public class AeroLogixServer {

	protected static final Logger logger = LogManager.getLogger();

	private static AeroLogixServer instance;

	private PersistenceManagerFactory pmf;

	private AeroLogixServer() {
		this.pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
	}

	public static AeroLogixServer getInstance() {
        if (instance == null) {
            instance = new AeroLogixServer();
        }
        return instance;
    }

	public PersistenceManagerFactory getPersistenceManagerFactory() {
		return this.pmf;
	}
}
