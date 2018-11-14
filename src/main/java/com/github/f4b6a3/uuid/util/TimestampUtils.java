/**
 * Copyright 2018 Fabio Lima <br/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); <br/>
 * you may not use this file except in compliance with the License. <br/>
 * You may obtain a copy of the License at <br/>
 *
 * http://www.apache.org/licenses/LICENSE-2.0 <br/>
 *
 * Unless required by applicable law or agreed to in writing, software <br/>
 * distributed under the License is distributed on an "AS IS" BASIS, <br/>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br/>
 * See the License for the specific language governing permissions and <br/>
 * limitations under the License. <br/>
 *
 */

package com.github.f4b6a3.uuid.util;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Class that provides methods related to timestamps.
 * 
 * @author fabiolimace
 *
 */
public class TimestampUtils implements Serializable {

	private static final long serialVersionUID = -2664354707894888058L;
	
	/*
	 * -------------------------
	 * Private static constants
	 * -------------------------
	 */
	private static final long GREGORIAN_EPOCH_SECONDS = getGregorianEpochSeconds();
	
	private static final long ONE_HUNDRED = 100L;
	private static final long MILLI_MULTIPLIER = 1_000L;
	private static final long MICRO_MULTIPLIER = 1_000_000L;
	private static final long NANOS_MULTIPLIER = 1_000_000_000L;
	
	private static final long TIMESTAMP_MULTIPLIER = NANOS_MULTIPLIER / ONE_HUNDRED;
	
	/* 
	 * -------------------------
	 * Public static methods
	 * -------------------------
	 */
	
	/**
	 * Get the current timestamp.
	 * 
	 * The UUID timestamp is a number of 100-nanos since gregorian epoch.
	 *
	 * Although it has 100-nanos precision, the timestamp returned has
	 * milliseconds accuracy.
	 * 
	 * "Precision" refers to the number of significant digits, and "accuracy" is
	 * whether the number is correct.
	 * 
	 * ### RFC-4122 - 4.2.1.2. System Clock Resolution
	 * 
	 * The timestamp is generated from the system time, whose resolution may be
	 * less than the resolution of the UUID timestamp.
	 * 
	 * If UUIDs do not need to be frequently generated, the timestamp can simply
	 * be the system time multiplied by the number of 100-nanosecond intervals
	 * per system time interval.
	 * 
	 * @return
	 */
	public static long getCurrentTimestamp() {
		long milliseconds = System.currentTimeMillis();
		long seconds = (milliseconds / MILLI_MULTIPLIER) - GREGORIAN_EPOCH_SECONDS;
		long nanoseconds = (milliseconds % MILLI_MULTIPLIER) * MICRO_MULTIPLIER;
		return (seconds * TIMESTAMP_MULTIPLIER) + (nanoseconds / ONE_HUNDRED);
	}
	
	/**
	 * Get the timestamp associated with a given instant.
	 *
	 * @param instant
	 * @return
	 */
	public static long toTimestamp(Instant instant) {
		long seconds = instant.getEpochSecond() - GREGORIAN_EPOCH_SECONDS;
		return (seconds * TIMESTAMP_MULTIPLIER) + (instant.getNano() / ONE_HUNDRED);
	}
	
	/**
	 * Get the instant associated with the given timestamp.
	 *
	 * @param timestamp
	 * @return
	 */
	public static Instant toInstant(long timestamp) {
		long seconds = (timestamp / TIMESTAMP_MULTIPLIER) + GREGORIAN_EPOCH_SECONDS;
		long nanoseconds = (timestamp % TIMESTAMP_MULTIPLIER) * ONE_HUNDRED;
		return Instant.ofEpochSecond(seconds, nanoseconds);
	}
		
	/**
	 * Get the beggining of the Gregorian Calendar in seconds: 1582-10-15 00:00:00Z.
	 * 
	 * The expression "Gregorian Epoch" means the date and time the Gregorian
	 * Calendar started. This expression is similar to "Unix Epoch", started in
	 * 1970-01-01 00:00:00Z.
	 *
	 * @return
	 */
	private static long getGregorianEpochSeconds() {
		LocalDate localDate = LocalDate.parse("1582-10-15");
		return localDate.atStartOfDay(ZoneId.of("UTC")).toInstant().getEpochSecond();
	}
}
