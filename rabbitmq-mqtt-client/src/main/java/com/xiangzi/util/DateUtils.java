package com.xiangzi.util;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public abstract class DateUtils {

	public static final String DATETIME_FORMAT = "yyyyMMddHHmmss";
	public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATETIME_FORMAT);
	public static final String DATE_FORMAT = "yyyyMMdd";
	public static final String TIME_FORMAT = "HH:mm:ss";
	public static final String SHOW_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String SHOW_DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * 得到以yyyy-MM-dd格式表示的当前日期字符串
	 */
	public static String getCurrentDate() {
		return new SimpleDateFormat(SHOW_DATE_FORMAT).format(new Date());
	}

	/**
	 * 得到以HH:mm:ss表示的当前时间字符串
	 */
	public static String getCurrentTime() {
		return new SimpleDateFormat(TIME_FORMAT).format(new Date());
	}

	/**
	 * 获取当前时间串，格式为：yyyyMMddHHmmss
	 *
	 * @return
	 */
	public static final String getCurrDatetime() {
		return format(new Date(), DATETIME_FORMAT);
	}

	/**
	 * 获取当前日期串，格式为yyyyMMdd
	 *
	 * @return
	 */
	public static final String getCurrDate() {
		return format(new Date(), DATE_FORMAT);
	}

	/**
	 * 得到以yyyy-MM-dd HH:mm:ss表示的当前时间字符串
	 */
	public static String getCurrentDateTime() {
		return format(new Date(), SHOW_DATETIME_FORMAT);
	}

	public static String getCurrentDateTime(String format) {
		return format(new Date(), format);
	}

	/**
	 * @param date
	 *            时间
	 * @param format
	 *            格式化串
	 * @return
	 */
	public static String format(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 对{@code date}字符串，采用<code>0</code>右补齐到{@code length}的长度
	 *
	 * @param date
	 * @param length
	 * @return
	 */
	public static String pad0(String date, int length) {
		return padChar(date, length, '0');
	}

	/**
	 * 对{@code date}字符串，采用<code>9</code>右补齐到{@code length}的长度
	 *
	 * @param date
	 * @param length
	 * @return
	 */
	public static String pad9(String date, int length) {
		return padChar(date, length, '9');
	}

	private static String padChar(String date, int length, char theChar) {
		if (StringUtils.isEmpty(date)) {
			date = "";
		}
		return StringUtils.rightPad(date, length, theChar);
	}

	/**
	 * {@code time1}是否小于{@code time2},即类似于
	 * 
	 * <pre>
	 * time1 &lt; time2
	 * </pre>
	 * 
	 * 。 如果{@code time2}为<code>null</code>， 则视为最小。
	 *
	 * @param time1
	 *            时间字符串，格式为 yyyyMMddHHmmss，不足14位后补0
	 * @param time2
	 *            时间字符串，格式为 yyyyMMddHHmmss，不足14位后补0
	 * @return
	 */
	public static boolean lessThan(String time1, String time2) {
		if (StringUtils.isEmpty(time1)) {
			if (StringUtils.isEmpty(time2)) {
				return false;
			} else {
				return true;
			}
		} else {
			return time1.compareTo(time2) < 0;
		}
	}

	/**
	 * {@code time1}是否大于{@code time2},即类似于
	 * 
	 * <pre>
	 * time1 &gt; time2
	 * </pre>
	 * 
	 * 。如果{@code time2}为<code>null</code>， 则视为最大。
	 *
	 * @param time1
	 *            时间字符串，格式为 yyyyMMddHHmmss，不足14位后补9
	 * @param time2
	 *            时间字符串，格式为 yyyyMMddHHmmss，不足14位后补9
	 * @return
	 */
	public static boolean greaterThan(String time1, String time2) {
		if (StringUtils.isEmpty(time1)) {
			if (StringUtils.isEmpty(time2)) {
				return false;
			} else {
				return true;
			}
		} else {
			return time1.compareTo(time2) > 0;
		}
	}

	/**
	 * 将<code>datetime</code>字符串时间转换为毫秒数
	 * 
	 * @param datetime
	 *            长度必须大于等于8而小于等于14，格式为 yyyyMMddHHmmss，不足14位后补0
	 * @return
	 */
	public static long toMilliseconds(String datetime) {
		return parseDate(datetime).getTime();
	}

	/**
	 * 将格式为{@link #DATETIME_FORMAT}的时间格式解析为Date对象,{@code datetime}的长度必须大于8小于14.
	 * 
	 * @param datetime
	 * @return
	 */
	public static Date parseDate(String datetime) {
		Assert.notNull(datetime);
		Assert.isTrue(datetime.length() >= 4 && datetime.length() <= 14, "长度必须大于等于8而小于等于14");
		DateFormat dateFormat = SIMPLE_DATE_FORMAT;
		try {
			if (datetime.length() < 14) {
				dateFormat = new SimpleDateFormat(DATETIME_FORMAT.substring(0, datetime.length()));
			}
			return dateFormat.parse(datetime);
		} catch (ParseException e) {
			throw new IllegalArgumentException("入参datetime：" + datetime + "解析异常，请检查格式必须为：" + DATETIME_FORMAT);
		}
	}

	/**
	 * 将字符串时间解析为对象
	 * 
	 * @param datetime
	 * @param format
	 * @return
	 */
	public static Date parseDate(String datetime, String format) {
		Assert.notNull(datetime);
		Assert.notNull(format);
		Assert.isTrue(datetime.length() == format.length(), "值和格式串的长度不一致");
		DateFormat dateFormat = new SimpleDateFormat(format);
		try {
			return dateFormat.parse(datetime);
		} catch (ParseException e) {
			throw new IllegalArgumentException(MessageFormat.format("入参datetime：{1}解析异常，请检查格式必须为：{2}", datetime, format));
		}
	}

	public static int getDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static int getDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static int getDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static int getDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	// 获取某一个月的天数
	public static int getMaxDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.getActualMaximum(Calendar.DATE);
	}

	// 获取某月的第一天
	public static String getFirstDayOfMonth(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseDate(date, SHOW_DATE_FORMAT));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return new SimpleDateFormat(SHOW_DATE_FORMAT).format(cal.getTime());
	}

	public static int getDayOfYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_YEAR);
	}

	public static int getDayOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_YEAR);
	}

	public static int getDayOfWeek(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseDate(date, SHOW_DATE_FORMAT));
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static int getDayOfMonth(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseDate(date, SHOW_DATE_FORMAT));
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static int getDayOfYear(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseDate(date, SHOW_DATE_FORMAT));
		return cal.get(Calendar.DAY_OF_YEAR);
	}

	public static int compare(String date1, String date2) {
		return compare(date1, date2, SHOW_DATE_FORMAT);
	}

	public static int compareTime(String time1, String time2) {
		return compareTime(time1, time2, TIME_FORMAT);
	}

	public static int compare(String date1, String date2, String format) {
		Date d1 = parseDate(date1, format);
		Date d2 = parseDate(date2, format);
		return d1.compareTo(d2);
	}

	public static int compareTime(String time1, String time2, String format) {
		String[] arr1 = time1.split(":");
		String[] arr2 = time2.split(":");
		if (arr1.length < 2) {
			throw new RuntimeException("错误的时间值:" + time1);
		}
		if (arr2.length < 2) {
			throw new RuntimeException("错误的时间值:" + time2);
		}
		int h1 = Integer.parseInt(arr1[0]);
		int m1 = Integer.parseInt(arr1[1]);
		int h2 = Integer.parseInt(arr2[0]);
		int m2 = Integer.parseInt(arr2[1]);
		int s1 = 0, s2 = 0;
		if (arr1.length == 3) {
			s1 = Integer.parseInt(arr1[2]);
		}
		if (arr2.length == 3) {
			s2 = Integer.parseInt(arr2[2]);
		}
		if (h1 < 0 || h1 > 23 || m1 < 0 || m1 > 59 || s1 < 0 || s1 > 59) {
			throw new RuntimeException("错误的时间值:" + time1);
		}
		if (h2 < 0 || h2 > 23 || m2 < 0 || m2 > 59 || s2 < 0 || s2 > 59) {
			throw new RuntimeException("错误的时间值:" + time2);
		}
		if (h1 != h2) {
			return h1 > h2 ? 1 : -1;
		} else {
			if (m1 == m2) {
				if (s1 == s2) {
					return 0;
				} else {
					return s1 > s2 ? 1 : -1;
				}
			} else {
				return m1 > m2 ? 1 : -1;
			}
		}
	}

	public static boolean isTime(String time) {
		String[] arr = time.split(":");
		if (arr.length < 2) {
			return false;
		}
		try {
			int h = Integer.parseInt(arr[0]);
			int m = Integer.parseInt(arr[1]);
			int s = 0;
			if (arr.length == 3) {
				s = Integer.parseInt(arr[2]);
			}
			if (h < 0 || h > 23 || m < 0 || m > 59 || s < 0 || s > 59) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isDate(String date) {
		String[] arr = date.split("-");
		if (arr.length < 3) {
			return false;
		}
		try {
			int y = Integer.parseInt(arr[0]);
			int m = Integer.parseInt(arr[1]);
			int d = Integer.parseInt(arr[2]);
			if (y < 0 || m > 12 || m < 0 || d < 0 || d > 31) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isWeekend(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int t = cal.get(Calendar.DAY_OF_WEEK);
		if (t == Calendar.SATURDAY || t == Calendar.SUNDAY) {
			return true;
		}
		return false;
	}

	public static boolean isWeekend(String str) {
		return isWeekend(parseDate(str));
	}

	/**
	 * 日期date上加count分钟，count为负表示减
	 */
	public static Date addMinute(Date date, int count) {
		return new Date(date.getTime() + 60000L * count);
	}

	/**
	 * 日期date上加count小时，count为负表示减
	 */
	public static Date addHour(Date date, int count) {
		return new Date(date.getTime() + 3600000L * count);
	}

	/**
	 * 日期date上加count天，count为负表示减
	 */
	public static Date addDay(Date date, int count) {
		return new Date(date.getTime() + 86400000L * count);
	}

	/**
	 * 日期date上加count星期，count为负表示减
	 */
	public static Date addWeek(Date date, int count) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.WEEK_OF_YEAR, count);
		return c.getTime();
	}

	/**
	 * 日期date上加count月，count为负表示减
	 */
	public static Date addMonth(Date date, int count) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, count);
		return c.getTime();
	}

	/**
	 * 日期date上加count年，count为负表示减
	 */
	public static Date addYear(Date date, int count) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, count);
		return c.getTime();
	}

	public static String toDisplayDateTime(String date) {
		if (StringUtils.isEmpty(date))
			return null;
		try {
			if (isDate(date)) {
				return toDisplayDateTime(parseDate(date, SHOW_DATE_FORMAT));
			}
			SimpleDateFormat t = new SimpleDateFormat(SHOW_DATETIME_FORMAT);
			Date d = t.parse(date);
			return toDisplayDateTime(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "不是标准格式时间!";
	}

	public static String toDisplayDateTime(Date date) {
		long minite = (System.currentTimeMillis() - date.getTime()) / 60000L;
		if (minite < 60L) {
			return toString(date, "MM-dd") + " " + minite + "分钟前";
		}
		if (minite < 1440L) {
			return toString(date, "MM-dd") + " " + (minite / 60L) + "小时前";
		}
		return toString(date, "MM-dd") + " " + (minite / 1440L) + "天前";
	}

	/**
	 * 按指定的format输出日期字符串
	 */
	public static String toString(Date date, String format) {
		SimpleDateFormat t = new SimpleDateFormat(format);
		return t.format(date);
	}

	// 获得本周第一天
	public static String getFirstDayOfNowWeek() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat(SHOW_DATE_FORMAT);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 获取本周一的日期
		return df.format(cal.getTime());
	}

	// 获取本月第一天
	public static String getFirstDayOfNowMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		SimpleDateFormat df = new SimpleDateFormat(SHOW_DATE_FORMAT);
		return df.format(cal.getTime());
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 字符串的日期格式的计算
	 */
	public static int daysBetween(String smdate, String bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

}