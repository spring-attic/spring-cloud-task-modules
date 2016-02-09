/*
 * Copyright 2015 the original author or authors.
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

package org.springframework.cloud.task.sparkapp.client;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Verifies that the Spark App Task Application runs without throwing exception.
 *
 * NOTE: the 'dummy.jar' doesn't exist but the test still runs since the example class
 * is on the classpath already.
 *
 * @author Thomas Risberg
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=SparkAppClientTaskApplication.class)
@IntegrationTest({"app-name:pi",
		"app-jar:dummy.jar",
		"app-class:org.apache.spark.examples.JavaSparkPi",
		"app-args:10"})
public class SparkAppClientTaskApplicationTests {

	@Test
	public void testExampleSparkApp() throws Exception {
	}
}
