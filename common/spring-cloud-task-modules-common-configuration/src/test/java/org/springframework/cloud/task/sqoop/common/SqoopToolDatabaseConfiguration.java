package org.springframework.cloud.task.sqoop.common;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.SocketUtils;

@Configuration
public class SqoopToolDatabaseConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(SqoopToolDatabaseConfiguration.class);

	@Bean(destroyMethod = "stop")
	public Server databaseServer() throws SQLException, IOException {
		DriverManager.registerDriver(new org.hsqldb.jdbcDriver());
		int hsqldbPort = SocketUtils.findAvailableTcpPort(10000);
		System.setProperty("db.server.port", Integer.toString(hsqldbPort));
		logger.info("Database is using port: " + Integer.toString(hsqldbPort));
		HsqlProperties configProps = new HsqlProperties();
		configProps.setProperty("server.port", hsqldbPort);
		configProps.setProperty("server.database.0", "file:target/db/test");
		configProps.setProperty("server.dbname.0", "test");
		Server server = new org.hsqldb.Server();
		server.setLogWriter(null);
		server.setErrWriter(null);
		server.setRestartOnShutdown(false);
		server.setNoSystemExit(true);
		server.setProperties(configProps);
		server.start();
		return server;
	}
}
