/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.task.sparkapp.client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.cloud.task.sparkapp.common.SparkAppCommonTaskProperties;
import org.springframework.context.annotation.Bean;

/**
 * Spring Boot Application that has tasks enabled and will run a Spark App in client mode.
 *
 * @author Thomas Risberg
 */
@SpringBootApplication
@EnableTask
@EnableConfigurationProperties({ SparkAppCommonTaskProperties.class })
public class SparkAppClientTaskApplication {

	@Bean
	public CommandLineRunner commandLineRunner() {
		return new SparkAppClientRunner();
	}

	public static void main(String[] args) {
		SpringApplication.run(SparkAppClientTaskApplication.class, args);
	}

}
