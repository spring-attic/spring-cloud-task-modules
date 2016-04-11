/*
 * Copyright 2015-2016 the original author or authors.
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

package org.springframework.cloud.task.timestamp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.OutputCapture;

/**
 * Verifies that the Task Application outputs the correct task log entries.
 *
 * @author Glenn Renfro
 */
public class TaskApplicationTests {

	@Rule
	public OutputCapture outputCapture = new OutputCapture();

	@Test
	public void testTimeStampApp() throws Exception {
		final String TEST_DATE_DOTS = ".......";
		final String CREATE_TASK_MESSAGE = "Creating: TaskExecution{executionId=";
		// with multiple tests can't really track full executionId
		final String UPDATE_TASK_MESSAGE = "Updating: TaskExecution with executionId=";
		String[] args = { "--format=yyyy" + TEST_DATE_DOTS, "--spring.cloud.task.closecontext.enable=false" };

		assertEquals(0, SpringApplication.exit(SpringApplication
				.run(TaskApplication.class, args)));

		String output = this.outputCapture.toString();
		assertTrue("Unable to find the timestamp: " + output,
				output.contains(TEST_DATE_DOTS));
		assertTrue("Test results do not show create task message: " + output,
				output.contains(CREATE_TASK_MESSAGE));
		assertTrue("Test results do not show success message: " + output,
				output.contains(UPDATE_TASK_MESSAGE));
	}

	@Test
	public void testTimeStampAppRepeat() throws Exception {
		final String TEST_SLEEP = "Sleeping";
		final String CREATE_TASK_MESSAGE = "Creating: TaskExecution{executionId=";
		// with multiple tests can't really track full executionId
		final String UPDATE_TASK_MESSAGE = "Updating: TaskExecution with executionId=";
		String[] args = { "--repeat=3", "--spring.cloud.task.closecontext.enable=false" };

		assertEquals(0, SpringApplication.exit(SpringApplication
				.run(TaskApplication.class, args)));

		String output = this.outputCapture.toString();
		assertTrue("Unable to find the sleep message: " + output,
				output.contains(TEST_SLEEP));
		assertTrue("Test results do not show create task message: " + output,
				output.contains(CREATE_TASK_MESSAGE));
		assertTrue("Test results do not show success message: " + output,
				output.contains(UPDATE_TASK_MESSAGE));
	}
}
