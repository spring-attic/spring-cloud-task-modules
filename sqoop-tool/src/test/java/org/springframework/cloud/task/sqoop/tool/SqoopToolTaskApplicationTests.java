/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.task.sqoop.tool;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Verifies that the Sqoop Tool Task Application runs without throwing exception.
 *
 * NOTE: This test is using the local file system and running mapreduce in local mode.
 *
 * NOTE: We also start an HSQLDB database server with a data path of 'target/db'
 *
 * @author Thomas Risberg
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes={SqoopToolTaskApplication.class, SqoopToolTestConfiguration.class})
@IntegrationTest
public abstract class SqoopToolTaskApplicationTests {

	private static String testDir;

	static {
		try {
			testDir = Files.createTempDirectory("sqoop-test-").toFile().getAbsolutePath();
			FileUtils.deleteDirectory(new File(testDir));
		} catch (IOException e) {
			testDir = System.getProperty("${java.io.tmpdir}") + "/sqoop-test-" + UUID.randomUUID();
		}
		System.setProperty("sqoop.test.dir", testDir);
	}

	@IntegrationTest({"spring.hadoop.fsUri=file:///",
			"spring.hadoop.config.mapreduce.framework.name=local",
			"connect=jdbc:hsqldb:hsql://localhost:${db.server.port}/test",
			"username=sa",
			"command=import",
			"tool-args=--table FOO --target-dir ${sqoop.test.dir} -m 1 --outdir target/java"})
	public static class SqoopToolImportTests extends SqoopToolTaskApplicationTests {

		@Test
		public void testImportTool() throws Exception {
			File testOutput = new File(testDir);
			assertTrue(testOutput.exists());
			File[] files = testOutput.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					if (name.startsWith("part")) {
						return true;
					}
					return false;
				}
			});
			assertTrue("At least one data file should have been created", files.length > 0);
		}
	}

	@IntegrationTest({"spring.hadoop.fsUri=file:///",
			"spring.hadoop.config.mapreduce.framework.name=local",
			"connect=jdbc:hsqldb:hsql://localhost:${db.server.port}/test",
			"username=sa",
			"command=export",
			"tool-args=--table BAR --export-dir data --outdir target/java"})
	public static class SqoopToolExportTests extends SqoopToolTaskApplicationTests {

		@Test
		public void testExportTool() throws Exception {
			int hsqldbPort = Integer.valueOf(System.getProperty("db.server.port"));
			SingleConnectionDataSource dataSource =
					new SingleConnectionDataSource("jdbc:hsqldb:hsql://localhost:" + hsqldbPort + "/test", "sa", "", false);
			JdbcTemplate db = new JdbcTemplate(dataSource);
			Number count = db.queryForObject("SELECT count(*) FROM BAR", Number.class);
			dataSource.destroy();
			assertTrue("All rows should have been imported", count.intValue() == 2);
		}
	}

	@AfterClass
	public static void cleanup() throws IOException {
		FileUtils.deleteDirectory(new File(testDir));
	}
}
