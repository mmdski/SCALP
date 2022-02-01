package gov.usgs.scalp;

import hec.heclib.util.HecTime;

/**
 * SCALP date/time class
 */
public class DateTime implements Comparable<DateTime> {

	private HecTime hecTime;

	private final long MILLISECONDS_IN_AN_HOUR = 3600000;

	/**
	 * Constructs a DateTime instance from an time in milliseconds from the epoch of
	 * 1970-01-01T00:00:00Z
	 *
	 * @param milliEpoch
	 *            time in milliseconds
	 */
	public DateTime(long milliEpoch) {
		hecTime = new HecTime();
		hecTime.setTimeInMillis(milliEpoch);
	}

	/**
	 * Copy constructor
	 *
	 * @param other
	 *            other DateTime
	 */
	public DateTime(DateTime other) {
		this(other.hecTime);
	}

	/**
	 * Constructs a DateTime from year, month, day, hour, and minute values
	 *
	 * @param year
	 *            year
	 * @param month
	 *            month
	 * @param day
	 *            day
	 * @param hour
	 *            hour
	 * @param minute
	 *            minute
	 */
	public DateTime(int year, int month, int day, int hour, int minute) {
		String date = String.format("%02d/%02d/%04d", month, day, year);
		String time = String.format("%02d:%02d", hour, minute);
		hecTime = new HecTime(date, time);
	}

	/**
	 * Constructs a DateTime from an HecTime instance
	 *
	 * @param hecTime
	 *            HecTime instance
	 */
	public DateTime(HecTime hecTime) {

		if (hecTime == null)
			throw new NullPointerException();

		this.hecTime = new HecTime(hecTime);
	}

	/**
	 * Adds hours to this instance and returns a new instance
	 *
	 * @param hours
	 *            hours to add to this instance
	 * @return new instance
	 */
	public DateTime addHours(int hours) {
		HecTime otherTime = new HecTime(hecTime);
		otherTime.addHours(hours);
		return new DateTime(otherTime);
	}

	/**
	 * Adds minutes to this instance and returns a new instance
	 *
	 * @param minutes
	 *            minutes to add to this instance
	 * @return new instance
	 */
	public DateTime addMinutes(int minutes) {
		HecTime otherTime = new HecTime(hecTime);
		otherTime.addMinutes(minutes);
		return new DateTime(otherTime);
	}

	/**
	 * Adds seconds to this instance and returns a new instance
	 *
	 * @param seconds
	 *            seconds to add to this instance
	 * @return new instance
	 */
	public DateTime addSeconds(int seconds) {
		HecTime otherTime = new HecTime(hecTime);
		otherTime.addSeconds(seconds);
		return new DateTime(otherTime);
	}

	/**
	 * Compares this instance to another DateTime instance
	 *
	 * @param other
	 *            another DateTime instance
	 * @return int
	 */
	public int compareTo(DateTime other) {

		if (other == null)
			throw new NullPointerException();

		if (equals(other))
			return 0;

		return hecTime.compareTo(other.hecTime);
	}

	@Override
	public boolean equals(Object other) {

		if (this == other)
			return true;

		if (other == null)
			return false;

		if (getClass() != other.getClass())
			return false;

		DateTime dateTime = (DateTime) other;

		return hecTime.equalTo(dateTime.hecTime);
	}

	/**
	 * Returns the difference in hours between this and another DateTime
	 *
	 * @param other
	 *            other DateTime instance
	 * @return difference in hours
	 */
	public int diffHours(DateTime other) {

		if (other == null)
			throw new NullPointerException();

		long thisMillis = hecTime.getTimeInMillis();
		long otherMillis = other.getTimeInMillis();

		int nHours = Math.toIntExact((thisMillis - otherMillis) / MILLISECONDS_IN_AN_HOUR) + 1;

		return nHours;
	}

	/**
	 * Returns the day of the month of this instance
	 *
	 * @return day of month
	 */
	public int getDayOfMonth() {
		return hecTime.day();
	}

	/**
	 * Returns the day of the week of this instance
	 *
	 * @return day of week
	 */
	public int getDayOfWeek() {
		return hecTime.dayOfWeek();
	}

	/**
	 * Returns a HecTime instance that's representative of this instance
	 *
	 * @return HecTime instance
	 */
	public HecTime getHecTime() {
		return new HecTime(hecTime);
	}

	/**
	 * Returns the DateTime representation of the top of the hour of this instance
	 *
	 * @return hour
	 */
	public DateTime getHour() {
		int year = hecTime.year();
		int month = hecTime.month();
		int day = hecTime.day();
		int hour = hecTime.hour();
		int minute = 0;
		return new DateTime(year, month, day, hour, minute);
	}

	/**
	 * Returns the hour of the day of this instance
	 *
	 * @return hour of day
	 */
	public int getHourOfDay() {
		return hecTime.hour();
	}

	/**
	 * Returns the month of year represented by this instance
	 *
	 * @return month of year
	 */
	public int getMonth() {
		return hecTime.month();
	}

	/**
	 * Returns the DateTime representation of the beginning of the next hour
	 *
	 * @return beginning of the next hour
	 */
	public DateTime getNextHour() {
		DateTime thisHour = getHour();
		return thisHour.addHours(1);
	}

	/**
	 * Returns the time in milliseconds from the epoch of 1970-01-01T00:00:00Z
	 *
	 * @return time in milliseconds
	 */
	public long getTimeInMillis() {
		return hecTime.getTimeInMillis();
	}

	/**
	 * Returns the year of this instance
	 *
	 * @return year
	 */
	public int getYear() {
		return hecTime.year();
	}

	/**
	 * Returns true if this DateTime is representative of the top of the hour
	 *
	 * @return true if this is an hourly time stamp
	 */
	public boolean isHour() {
		return (hecTime.minute() == 0 && hecTime.second() == 0);
	}

	/**
	 * Returns true if this instance is less than other, false otherwise
	 *
	 * @param other
	 *            other DateTime
	 * @return true if this instance is less than other
	 */
	public boolean lessThan(DateTime other) {
		return hecTime.lessThan(other.hecTime);
	}

	/**
	 * Subtracts hours to this instance and returns a new instance
	 *
	 * @param hours
	 *            hours to subtract from this instance
	 * @return new instance
	 */
	public DateTime subtractHours(int hours) {
		HecTime otherTime = new HecTime(hecTime);
		otherTime.subtractHours(hours);
		return new DateTime(otherTime);
	}

	/**
	 * Returns the string representation of this instance
	 *
	 * @return string representation of this instance
	 */
	public String toString() {
		return hecTime.toString();
	}
}
