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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

/**
 * @author Glenn Renfro
 * @author Janne Valkealahti
 */
@ConfigurationProperties
public class TimestampTaskProperties {

	/**
	 * The timestamp format, "yyyy-MM-dd HH:mm:ss.SSS" by default.
	 */
	private String format = "yyyy-MM-dd HH:mm:ss.SSS";

	/**
	 * How many times application repeats logging while waiting 1 sec between
	 * each repeat, "1" by default.
	 */
	private int repeat = 1;

	public String getFormat() {
		Assert.hasText(format, "format must not be empty nor null");
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public int getRepeat() {
		return repeat;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}
}
