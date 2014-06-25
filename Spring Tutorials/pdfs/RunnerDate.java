package com.runner.architecture.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Contains following date operations - validation checks - conversion from string to date - conversion from date to
 * string
 *
 * @author klaus.venesmaa
 *
 */
public class RunnerDate {

	/* Supported date formats */
	public static final String DATE_FORMAT_DDMMYYYY = "ddMMyyyy";
	public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
	public static final String DATE_FORMAT_DDMMYYYY_LINE_SEPARATED = "dd-MM-yyyy";
	public static final String DATE_FORMAT_YYYYMMDD_LINE_SEPARATED = "yyyy-MM-dd";
	public static final String DATE_FORMAT_DDMMYYYY_DOT_SEPARATED = "dd.MM.yyyy";
	public static final String GENERAL_DATE_FORMAT = "d.M.yyyy";
	public static final String DATE_FORMAT_YYMMDD = "yyMMdd";
	public static final String DATE_FORMAT_DDMMYY = "ddMMyy";
	public static final String DATE_FORMAT_YYYY = "yyyy";

	/* Suppported time stamp formats */
	public static final String TIME_STAMP_FORMAT_DDMMYYYY_LINE_SEPARATED = "dd-MM-yyyy HH:mm";
	public static final String TIME_STAMP_FORMAT_DDMMYYYY_DOT_SEPARATED = "dd.MM.yyyy HH:mm";
	public static final String TIME_STAMP_FORMAT_DMYYYY_DOT_SEPARATED = "d.M.yyyy HH:mm";
	public static final String TIME_STAMP_FORMAT_DDMMYYYY = "ddMMyyyy HH:mm";

	/* Supported string formats of date */
	private static final String DATE_STRING_FORMAT_SEPARATED_DD_DD_DDDD = "\\d\\d[-.]\\d\\d[-.]\\d\\d\\d\\d";
	private static final String DATE_STRING_FORMAT_SEPARETED_D_DD_DDDD = "\\d[-.]\\d\\d[-.]\\d\\d\\d\\d";
	private static final String DATE_STRING_FORMAT_SEPARATED_DD_D_DDDD = "\\d\\d[-.]\\d[-.]\\d\\d\\d\\d";
	private static final String DATE_STRING_FORMAT_SEPARATED_D_D_DDDD = "\\d[-.]\\d[-.]\\d\\d\\d\\d";
	private static final String DATE_STRING_FORMAT_NOT_SEPARATED = "\\d\\d\\d\\d\\d\\d\\d\\d";

	/* Supported string formats of time */
	private static final String TIME_STRING_FORMAT_SEPARATED_HH_MM = "\\d\\d[:]\\d\\d";
	private static final String DEFAULT_TIME_PART_FOR_TIME_STAMP = "00:00";

	private static final List<String> DATE_FORMAT_LIST = new ArrayList<String>();
	private static final List<String> TIME_STAMP_FORMAT_LIST = new ArrayList<String>();
	private static final List<String> DATE_STRING_FORMAT_LIST = new ArrayList<String>();
	private static final List<String> TIME_STAMP_STRING_FORMAT_LIST = new ArrayList<String>();

	private static final long CONVERT_MILLISECONDS_SECONDS = 1000;
	private static final long CONVERT_SECONDS_MINUTES = 60;
	private static final long CONVERT_MINUTES_HOURS = 60;
	private static final long CONVERT_HOURS_DAYS = 24;

	private static final int EPOCH_YEAR = 1970;
	public static final String DATE_FORMAT_YYYYMM_LINE_SEPARATED = "yyyy-MM-dd";

	/* Used for unit testing.*/
	private static RunnerDate testRunnerDate = null;
	private Date currentDate;

	static {
		DATE_FORMAT_LIST.add(DATE_FORMAT_DDMMYYYY_LINE_SEPARATED);
		DATE_FORMAT_LIST.add(DATE_FORMAT_DDMMYYYY_DOT_SEPARATED);
		DATE_FORMAT_LIST.add(DATE_FORMAT_DDMMYYYY);

		TIME_STAMP_FORMAT_LIST.add(TIME_STAMP_FORMAT_DDMMYYYY_LINE_SEPARATED);
		TIME_STAMP_FORMAT_LIST.add(TIME_STAMP_FORMAT_DDMMYYYY_DOT_SEPARATED);
		TIME_STAMP_FORMAT_LIST.add(TIME_STAMP_FORMAT_DMYYYY_DOT_SEPARATED);
		TIME_STAMP_FORMAT_LIST.add(TIME_STAMP_FORMAT_DDMMYYYY);

		DATE_STRING_FORMAT_LIST.add(DATE_STRING_FORMAT_SEPARATED_DD_DD_DDDD);
		DATE_STRING_FORMAT_LIST.add(DATE_STRING_FORMAT_SEPARETED_D_DD_DDDD);
		DATE_STRING_FORMAT_LIST.add(DATE_STRING_FORMAT_SEPARATED_DD_D_DDDD);
		DATE_STRING_FORMAT_LIST.add(DATE_STRING_FORMAT_SEPARATED_D_D_DDDD);
		DATE_STRING_FORMAT_LIST.add(DATE_STRING_FORMAT_NOT_SEPARATED);

		TIME_STAMP_STRING_FORMAT_LIST.add(DATE_STRING_FORMAT_SEPARATED_DD_DD_DDDD + " "
				+ TIME_STRING_FORMAT_SEPARATED_HH_MM);
		TIME_STAMP_STRING_FORMAT_LIST.add(DATE_STRING_FORMAT_SEPARETED_D_DD_DDDD + " "
				+ TIME_STRING_FORMAT_SEPARATED_HH_MM);
		TIME_STAMP_STRING_FORMAT_LIST.add(DATE_STRING_FORMAT_SEPARATED_DD_D_DDDD + " "
				+ TIME_STRING_FORMAT_SEPARATED_HH_MM);
		TIME_STAMP_STRING_FORMAT_LIST.add(DATE_STRING_FORMAT_SEPARATED_D_D_DDDD + " "
				+ TIME_STRING_FORMAT_SEPARATED_HH_MM);
		TIME_STAMP_STRING_FORMAT_LIST.add(DATE_STRING_FORMAT_NOT_SEPARATED + " "
				+ TIME_STRING_FORMAT_SEPARATED_HH_MM);
	}

	/**
	 * Checks that given dateString's format response to given format.
	 *
	 * If dateString or format is null an IncorrectInputParametersException is thrown.
	 *
	 * @param dateString
	 * @param format
	 * @return true if valid, false if not valid
	 * @throws IncorrectInputParametersException
	 */
	public static boolean isDateValidInSpecificFormat(String dateString, String format)
			throws IncorrectInputParametersException {
		if (dateString == null || format == null) {
			throw new IncorrectInputParametersException(
					"Incorrect input parameters. Parameters dateString and format are mandatory.");
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		simpleDateFormat.setLenient(false);

		try {
			simpleDateFormat.parse(dateString);
		} catch (ParseException pe) {
			return false;
		}

		return true;
	}

	/**
	 * Checks that given dateString's format is one of allowed formats Supported formats: - ddMMyyyy - dd-MM-yyyy -
	 * dd.MM.yyyy
	 *
	 * If dateString is null an IncorrectInputParametersException is thrown.
	 *
	 * @param dateString
	 * @return true if valid, false if not valid
	 * @throws IncorrectInputParametersException
	 */
	public static boolean isDateValid(String dateString) throws IncorrectInputParametersException {

		if (dateString == null) {
			throw new IncorrectInputParametersException(
					"Incorrect input parameters. Parameter dateString is mandatory.");
		}

		// Check that format is i.e dd-mm-yyyy or ddmmyyyy
		if (isFormatSupported(dateString, DATE_STRING_FORMAT_LIST)) {

			for (String format : DATE_FORMAT_LIST) {

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
				simpleDateFormat.setLenient(false);

				try {
					simpleDateFormat.parse(dateString);
					// Corresponding format found
					return true;
				} catch (ParseException pe) {
					continue;
				}
			}
		}
		// Corresponding format was not found so dateString is not valid
		return false;
	}

	/**
	 * Checks that string date's format is supported
	 *
	 * @param date
	 * @return
	 */
	private static boolean isFormatSupported(String date, List<String> formats) {
		for (String format : formats) {
			Pattern mask = Pattern.compile(format);
			Matcher matcher = mask.matcher(date);

			if (matcher.matches()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Converts date to string in given format or null. If format is null it returns date in default format d.m.yyyy
	 * parameter is null.
	 *
	 * @param date
	 * @param format
	 * @return date in string format
	 */
	public static String convertDateToString(Date date, String format) {
		String dateString = null;

		/* Satish : Changes done to remove incorrectParameterException */

		if (date == null ) {
			return null;
		}else{
			if(format == null){
				format = GENERAL_DATE_FORMAT;
			}
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		simpleDateFormat.setLenient(false);
		dateString = simpleDateFormat.format(date);

		return dateString;
	}

	/**
	 * Converts string from given format to date. Throws IncorrectInputParametersException if either dateString or
	 * format parameter is null. Throws ParseException if conversion to Date fails.
	 *
	 * @param date
	 * @param format
	 * @return string in date format
	 * @throws IncorrectInputParametersException
	 *             , ParseException
	 */
	public static Date convertStringToDate(String dateString, String format) throws IncorrectInputParametersException,
			ParseException {
		Date date = null;

		if (dateString == null || format == null) {
			throw new IncorrectInputParametersException(
					"Incorrect input parameters. Parameters dateString and format are mandatory.");
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		simpleDateFormat.setLenient(false);
		date = simpleDateFormat.parse(dateString);

		return date;
	}

	/**
	 * Converts string to date if dateString is in correct format. If dateString is null an
	 * IncorrectInputParametersException is thrown. If no matching patter is found null value is returned.
	 *
	 * @param dateString
	 * @return date
	 * @throws IncorrectInputParametersException
	 */
	public static Date convertStringToDate(String dateString) throws IncorrectInputParametersException {

		if (dateString == null) {
			throw new IncorrectInputParametersException(
					"Incorrect input parameters. Parameter dateString is mandatory.");
		}

		boolean datesFormatOk = isFormatSupported(dateString, DATE_STRING_FORMAT_LIST);

		if (datesFormatOk) {

			for (String format : DATE_FORMAT_LIST) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
				simpleDateFormat.setLenient(false);

				try {
					Date date = simpleDateFormat.parse(dateString);

					// Return a date that only contains year, month and date
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);

					Calendar clearedCalendar = Calendar.getInstance();
					clearedCalendar.clear();
					clearedCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
					clearedCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
					clearedCalendar.set(Calendar.DATE, calendar.get(Calendar.DATE));

					return clearedCalendar.getTime();
				} catch (ParseException pe) {
					continue;
				}
			}
		}

		return null;
	}

	/**
	 * Converts incomplete dates to complete for example the input date is
	 * 0.0.2000 it will return 1.1.2000 and if the year is input invalid then
	 * returns null
	 *
	 * @param dateString
	 * @return
	 * @throws IncorrectInputParametersException
	 */
	public static Date convertIncompleteToCompleteDate(String dateString) throws IncorrectInputParametersException {

		if (dateString == null) {
			throw new IncorrectInputParametersException("Incorrect input parameters. Parameter dateString is mandatory.");
		}
		String[] str = dateString.split("\\.");
		for (int i = 0; i < str.length; i++) {
			try {
				if (Integer.parseInt(str[i]) == 0) {
					if (i < 2) {
						str[i] = "1";
					}
				}
			} catch (NumberFormatException ne) {
				continue;
			}
		}

		StringBuilder strbld = new StringBuilder();
		for (int i = 0; i < str.length; i++) {
			if (i > 0) {
				strbld.append(".");
			}
			strbld.append(str[i]);
		}
		dateString = strbld.toString();

		return (convertStringToDate(dateString));
	}

	/**
	 *
	 * @param timeStampString
	 * @return date
	 * @throws IncorrectInputParametersException
	 */
	public static Date convertTimeStampStringToDate(String inputTimeStampString)
			throws IncorrectInputParametersException {
		String timeStampString = null;

		if (inputTimeStampString == null) {
			throw new IncorrectInputParametersException(
					"Incorrect input parameters. Parameter dateString is mandatory.");
		}

		// If only date part is given then add 00:00 to date string
		if (isFormatSupported(inputTimeStampString, DATE_STRING_FORMAT_LIST)) {
			StringBuffer buffer = new StringBuffer(inputTimeStampString);
			// Append date and time separator
			buffer.append(" ");
			// Append default time
			buffer.append(DEFAULT_TIME_PART_FOR_TIME_STAMP);
			timeStampString = buffer.toString();
		} else {
			timeStampString = inputTimeStampString;
		}

		boolean timeStampFormatOk = isFormatSupported(timeStampString, TIME_STAMP_STRING_FORMAT_LIST);

		if (timeStampFormatOk) {

			for (String format : TIME_STAMP_FORMAT_LIST) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
				simpleDateFormat.setLenient(false);

				try {
					return simpleDateFormat.parse(timeStampString);
				} catch (ParseException pe) {
					continue;
				}
			}
		}

		return null;
	}

	/**
	 * Creates date object according to given parameters. Given month parameter is subtracted by one. This means one
	 * should not pass for example Calendar.JANUARY as a parameter.
	 *
	 * @param year
	 * @param month
	 * @param date
	 * @return date
	 */
	public static Date createDate(int year, int month, int date) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		// month is 0-based so minus one
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, --month);
		calendar.set(Calendar.DATE, date);
		return calendar.getTime();
	}

	/**
	 * Adds one date for given date.
	 *
	 * @param date
	 * @return
	 */
	public static Date increaseDateByOne(Date date) {
		if (date == null) {
			throw new IncorrectInputParametersException("Input parameter date was null.");
		}
		return increaseDateByGivenNumberOfDates(date, 1);
	}

	/**
	 * Adds given number of years to the given date.
	 *
	 * @param date
	 * @param noOfyears
	 * @return
	 */
	public static Date increaseDateByGivenNumberOfYears(Date date, int noOfyears) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);
		cal.add(Calendar.YEAR, noOfyears);
		return cal.getTime();
	}

	/**
	 * Adds given number of months to the given date.
	 *
	 * @param date
	 * @param noOfMonths
	 * @return
	 */
	public static Date increaseDateByGivenNumberOfMonths(Date date, int noOfMonths) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);
		cal.add(Calendar.MONTH, noOfMonths);
		return cal.getTime();
	}

	/**
	 * Adds given number of dates to given date.
	 *
	 * @param date
	 * @param increaseAmount
	 * @return
	 */
	public static Date increaseDateByGivenNumberOfDates(Date date, int increaseAmount) {
		if (date == null) {
			throw new IncorrectInputParametersException("Input parameter date was null.");
		}

		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, increaseAmount);
		return calendar.getTime();
	}

	/**
	 * Compares two dates. Notice that comparison uses only years, months and dates. Returns 0 if dates are both null or
	 * same. If date1 < date2 or date1 is not null and date2 is null returns -1. If date2 < date1 or date2 is not null
	 * and date1 is null returns 1.
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareDates(Date date1, Date date2) {
		if (date1 == null && date2 == null) {
			return 0;
		} else if (date1 != null && date2 == null) {
			return -1;
		} else if (date1 == null && date2 != null) {
			return 1;
		}

		Calendar temp = Calendar.getInstance();

		Calendar calendar1 = Calendar.getInstance();
		calendar1.clear();
		temp.setTime(date1);
		calendar1.set(Calendar.YEAR, temp.get(Calendar.YEAR));
		calendar1.set(Calendar.MONTH, temp.get(Calendar.MONTH));
		calendar1.set(Calendar.DATE, temp.get(Calendar.DATE));

		Calendar calendar2 = Calendar.getInstance();
		calendar2.clear();
		temp.setTime(date2);
		calendar2.set(Calendar.YEAR, temp.get(Calendar.YEAR));
		calendar2.set(Calendar.MONTH, temp.get(Calendar.MONTH));
		calendar2.set(Calendar.DATE, temp.get(Calendar.DATE));

		return calendar1.compareTo(calendar2);
	}

	/**
	 * Compares two timestamps. Returns 0 if dates are both null or same. If date1 < date2 or date1 is not null and
	 * date2 is null returns -1. If date2 < date1 or date2 is not null and date1 is null returns 1.
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareTimestamps(Date date1, Date date2) {
		if (date1 != null && date2 != null) {
			return date1.compareTo(date2);
		} else if (date1 != null) {
			return -1;
		} else if (date2 != null) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * Returns true if both dates are same. Meaning years are same, months are same and dates are same. Otherwise
	 * returns false.
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean areDatesSame(Date date1, Date date2) {
		if (compareDates(date1, date2) == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns current date. Now (and in productions) returns date created with new-operator. If there is a need to
	 * simulate some specific date in testing, this method should be modified so that date is retrieved from for example
	 * database or from configuration file.
	 *
	 * @return current date
	 */
	public static Date getCurrentDateWithOnlyDates() {
		/*If current date has been set in unit testing.*/
		if (testRunnerDate != null) {
			return testRunnerDate.currentDate;
		}
		Calendar calendar = Calendar.getInstance();
		Calendar calendarWithOnlyDate = Calendar.getInstance();
		calendar.clear();
		calendarWithOnlyDate.clear();
		calendar.setTime(new Date());
		calendarWithOnlyDate.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
		calendarWithOnlyDate.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
		calendarWithOnlyDate.set(Calendar.DATE, calendar.get(Calendar.DATE));

		return calendarWithOnlyDate.getTime();
	}

	/**
	 * This method returns last day of the date passed to this.
	 *
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfTheMonth(Date date) {
		if (date == null){
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DATE, 1);
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}

	/**
	 * Returns the first day of next month of the given date parameter.
	 *
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfNextMonthFollowingTheDate(Date date) {
		if (date == null){
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);

		// Go to the next month
		cal.add(Calendar.MONTH, 1);
		// Reset the date to first
		cal.set(Calendar.DATE, 1);

		return cal.getTime();
	}

	/**
	 * Returns the first day of next month of the given date parameter.
	 *
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfNextYearFollowingTheDate(Date date) {
		if (date == null){
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);

		// Reset the month to first
		cal.set(Calendar.MONTH, 0);
		// Reset the date to first
		cal.set(Calendar.DATE, 1);
		// Reset the date to first
		cal.add(Calendar.YEAR, 1);

		return cal.getTime();
	}
	/**
	 * Returns the year from given date parameter.
	 *
	 * @param date
	 * @return date's year
	 */
	public static int getYearFromDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTime(date);

		return calendar.get(Calendar.YEAR);
	}

	/**
	 * Returns the month from given date parameter.
	 *
	 * @param date
	 * @return date's month. Values from 1-12
	 */
	public static int getMonthFromDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTime(date);

		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * Returns the date from given date parameter.
	 *
	 * @param date
	 * @return date's date.
	 */
	public static int getDateFromDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTime(date);

		return calendar.get(Calendar.DATE);
	}

	/**
	 * Formats 2 input dates to following - Date1 SPACE seperator SPACE Date2
	 *
	 * @Throws IncorrectInputParametersException if either date or format parameter is null.
	 *
	 * @param date
	 * @param date
	 * @param seperator
	 * @param format
	 * @return date in string format
	 * @throws IncorrectInputParametersException
	 */
	public static String convertDateToString(Date date1, Date date2, String seperator, String format)
			throws IncorrectInputParametersException {
		String dateString = "";

		if (format == null) {
			throw new IncorrectInputParametersException("Incorrect input parameters. Parameter format is mandatory.");
		}

		if (seperator == null || seperator.trim().length() == 0) {
			seperator = "-";
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		simpleDateFormat.setLenient(false);
		if (date1 != null) {
			dateString = simpleDateFormat.format(date1);
		}
		dateString = dateString + " " + seperator + " ";
		if (date2 != null) {
			dateString = dateString + simpleDateFormat.format(date2);
		}

		return dateString;
	}

	/**
	 * This method returns a list of years from a year to a year passed as
	 * parameters
	 *
	 * @param fromYear
	 * @param toYear
	 * @return
	 */
	public static List<Integer> getListOfYears(Integer fromYear, Integer toYear) {
		if (fromYear == null || toYear == null || fromYear.intValue() > toYear.intValue()) {
			return null;
		}
		List<Integer> listOfYears = new ArrayList<Integer>();
		for (int i = toYear.intValue(); i >= fromYear.intValue(); i--) {
			listOfYears.add(i);
		}
		return listOfYears;
	}

	/**
	 * Calculates the difference in days for the given 2 dates
	 *
	 * @param date1
	 * @param date2
	 * @return int
	 */
	public static int getDiffinDays(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IncorrectInputParametersException("One or more of the input parameters date was null.");
		}

		Calendar calendar1 = Calendar.getInstance();
		calendar1.clear();
		calendar1.setTime(date1);

		Calendar calendar2 = Calendar.getInstance();
		calendar2.clear();
		calendar2.setTime(date2);

		// Get the represented date in milliseconds
        long milis1 = calendar1.getTimeInMillis();
        long milis2 = calendar2.getTimeInMillis();

        // Calculate difference in milliseconds
        long diff = milis2 - milis1;

        long difInDays = diff / (CONVERT_MILLISECONDS_SECONDS * CONVERT_SECONDS_MINUTES * CONVERT_MINUTES_HOURS * CONVERT_HOURS_DAYS);

		return (int)difInDays;
	}

	/**
	 * Calculates the difference in months for the given 2 dates
	 *
	 * @param date1
	 * @param date2
	 * @return int
	 */
	public static int getDiffBetweenTwoDatesInMonths(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IncorrectInputParametersException("One or more of the input parameters date was null.");
		}
		Calendar now = Calendar.getInstance();
		now.clear();
		now.setTime(date1);

		Calendar dob = Calendar.getInstance();
		dob.clear();
		dob.setTime(date2);

		if (dob.after(now)) {
			java.util.Calendar swap = now;
			now = dob;
			dob = swap;
		}
		int year1 = now.get(Calendar.YEAR);
		int year2 = dob.get(Calendar.YEAR);

		int month1 = now.get(Calendar.MONTH);
		int month2 = dob.get(Calendar.MONTH);

		int diffInMonths = (year1-year2)*12+(month1-month2);

		return diffInMonths;
	}

	/**
	 * Returns minimum or earliest date
	 * @param firstDate
	 * @param secondDate
	 * @return
	 */
	public static Date getEarliestDate(Date firstDate, Date secondDate) {

		int val = compareDates(firstDate, secondDate);

		if (val < 1) {
			return firstDate;
		} else {
			return secondDate;
		}

	}

	/**
	 * Returns maximum or later date
	 * @param firstDate
	 * @param secondDate
	 * @return
	 */
	public static Date getLaterDate(Date firstDate, Date secondDate) {

		if (firstDate == null) {
			return secondDate;
		}
		if (secondDate == null) {
			return firstDate;
		}
		
		int val = compareDates(firstDate, secondDate);

		if (val >= 0) {
			return firstDate;
		} else {
			return secondDate;
		}

	}

	/**
	 * Calculates the difference in years for the given 2 dates
	 *
	 * @param date1
	 * @param date2
	 * @return int
	 */
	public static int getDiffBetweenTwoDatesInYears(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IncorrectInputParametersException("One or more of the input parameters date was null.");
		}

		Calendar now = Calendar.getInstance();
		now.clear();
		now.setTime(date1);

		Calendar dob = Calendar.getInstance();
		dob.clear();
		dob.setTime(date2);

		long diff = 0;

		if (dob.getTimeInMillis() > now.getTimeInMillis()) {
			diff = dob.getTimeInMillis() - now.getTimeInMillis();
		} else {
			diff = now.getTimeInMillis() - dob.getTimeInMillis();
		}

		Calendar diffCal = Calendar.getInstance();
		diffCal.clear();
		diffCal.setTimeInMillis(diff);

		int diffInYears = diffCal.get(Calendar.YEAR);

		return diffInYears - EPOCH_YEAR;
	}

	public static Date convertCastorDateToUtilDate(org.exolab.castor.types.Date castorDate) {
		if (castorDate == null) {
			return null;
		}
		return castorDate.toDate();
	}

	public static org.exolab.castor.types.Date convertUtilDateToCastorDate(java.util.Date utilDate) {
		if (utilDate == null) {
			return null;
		}
		return new org.exolab.castor.types.Date(utilDate);
	}

	/**
	 *
	 * This method checks whether testdate falls in between earlier and later dates.
	 *
	 * @param earlierDate - earlier or start date
	 * @param laterDate - later or end date
	 * @param testDate -  date to be tested.
	 * @return int
	 */
	public static boolean isDateBetweenTwoDates(Date earlierDate, Date laterDate, Date testDate){

		if(earlierDate!=null && laterDate!=null && testDate!=null){
			if(testDate.after(earlierDate) && testDate.before(laterDate)){
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks if date is between two dates. Returns true if date is after or equal to first date and is
	 * before or equal to second date.
	 *
	 * @param earlierDate
	 * @param laterDate
	 * @param testDate
	 * @return
	 */
	public static boolean isDateBetweenOrEqualToTwoDates(Date earlierDate, Date laterDate, Date testDate) {
		if ((compareDates(testDate, earlierDate) != -1) && (compareDates(testDate, laterDate) != 1)) {
			return true;
		} else {
			return false;
		}
	}

	// This is temporary method added for formatting dates coming from VRK, once
	// we get confirmation on the exact date format we will change this method
	public static Date getDateFromVRKDateString(String vrkDateString) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		simpleDateFormat.setLenient(false);

		try {
			return simpleDateFormat.parse(vrkDateString);
		} catch (ParseException pe) {
			return null;
		}
	}


	/**
	 * Checks whether given date is first Day of any month
	 *
	 * @param date
	 * @return
	 */
	public static boolean checkIfDateIsFirstDayOfMonth(Date date) {
		if (date == null) {
			throw new IncorrectInputParametersException("Input parameter date was null.");
		}
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		if (day == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks whether given date is first Day of any month
	 *
	 * @param date
	 * @return
	 */
	public static boolean checkIfDateIsLastDayOfYear(Date date) {
		if (date == null) {
			throw new IncorrectInputParametersException("Input parameter date was null.");
		}
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		if (day == 31 && month == 11) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks whether given date is first Day of any month
	 *
	 * @param date
	 * @return
	 */
	public static boolean checkIfDateIsFirstDayOfYear(Date date) {
		if (date == null) {
			throw new IncorrectInputParametersException("Input parameter date was null.");
		}
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_YEAR);
		int month = cal.get(Calendar.MONTH);
		if (day == 1 && month == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Increase time by given number of minutes
	 *
	 * @param date
	 * @param minute
	 * @return
	 */
	public static Date increaseTimeByGivenNumberOfMinutes(Date date, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minute);
		return cal.getTime();
	}

	/**
	 * Increase time by given number of seconds
	 *
	 * @param date
	 * @param minute
	 * @return
	 */
	public static Date increaseTimeByGivenNumberOfSeconds(Date date, int seconds) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}

	public static Date increaseTimeByGivenTime(Date date, int min,int sec) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, min);
		cal.add(Calendar.SECOND, min);
		return cal.getTime();
	}

	/**
	 * Finds the first day of the year
	 *
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfTheYearFromDate(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.YEAR, getYearFromDate(date));
		return cal.getTime();
	}

	public static void setTestRunnerDate(RunnerDate runnerDate) {
		testRunnerDate = runnerDate;
	}

	public void setCurrentDate(Date date) {
		currentDate = date;
	}

	/**
	 * Finds the first day of the month
	 *
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfTheMonthFromDate(Date date) {

		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);
		// Reset date to First.
		cal.set(Calendar.DATE, 1);
		return cal.getTime();
	}

	/**
	 * Finds the Last day of the month
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfTheMonthFromDate(Date date) {

		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(getFirstDayOfNextMonthFollowingTheDate(date));

		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}
	/**
	 * Returns true if both timestamp are same or null
	 * Otherwise returns false.
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean areTimeStampSame(Date date1, Date date2) {
		if (compareTimestamps(date1, date2) == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method returns last day of the year date passed to this.
	 *
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfTheYear(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, 12);
		calendar.set(Calendar.DATE, 1);
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}

}