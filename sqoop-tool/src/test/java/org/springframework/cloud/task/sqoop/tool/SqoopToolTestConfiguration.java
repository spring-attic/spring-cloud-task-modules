package org.springframework.cloud.task.sqoop.tool;

import java.io.IOException;
import java.sql.SQLException;

import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.ServerAcl;
import org.hsqldb.server.ServerConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.util.SocketUtils;

@Configuration
public class SqoopToolTestConfiguration {

	@Bean(destroyMethod = "stop")
	public Server testDbServer() throws SQLException, IOException, ServerAcl.AclFormatException {
		int hsqldbPort = SocketUtils.findAvailableTcpPort(10000);
		System.setProperty("db.server.port", Integer.toString(hsqldbPort));
		HsqlProperties configProps = new HsqlProperties();
		configProps.setProperty("server.port", hsqldbPort);
		configProps.setProperty("server.database.0", "file:target/db/test");
		configProps.setProperty("server.dbname.0", "test");
		ServerConfiguration.translateDefaultDatabaseProperty(configProps);
		Server server = new org.hsqldb.Server();
		server.setLogWriter(null);
		server.setRestartOnShutdown(false);
		server.setNoSystemExit(true);
		server.setProperties(configProps);
		server.start();
		SingleConnectionDataSource dataSource =
				new SingleConnectionDataSource("jdbc:hsqldb:hsql://localhost:" + hsqldbPort + "/test", "sa", "", false);
		JdbcTemplate db = new JdbcTemplate(dataSource);
		db.execute("DROP TABLE IF EXISTS FOO");
		db.execute("DROP TABLE IF EXISTS BAR");
		db.execute("CREATE TABLE FOO (ID smallint NOT NULL, ARTIST varchar(255), " +
				"TITLE varchar(255), PRIMARY KEY (ID))");
		db.execute("INSERT INTO FOO VALUES(1, 'The Beatles', 'Let it be')");
		db.execute("INSERT INTO FOO VALUES(2, 'The Doors', 'Light my fire')");
		db.execute("CREATE TABLE BAR (ID smallint NOT NULL, ARTIST varchar(255), " +
				"TITLE varchar(255), PRIMARY KEY (ID))");
		dataSource.destroy();
		return server;
	}
}
