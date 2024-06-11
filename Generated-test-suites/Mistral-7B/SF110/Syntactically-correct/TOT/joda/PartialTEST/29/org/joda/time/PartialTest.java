import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeFieldVerifier;
import org.junit.Test;
import static org.junit.Assert.*;

public class PartialTest {

 @Test
 public void testEmptyPartial() {
 Partial emptyPartial = new Partial();
 assertEquals(0, emptyPartial.size());
 assertTrue(emptyPartial.isMatch(new BaseDateTime()));
 }

 @Test
 public void testSingleFieldPartial() {
 final DateTimeFieldType YEAR_FIELD = DateTimeFieldType.year();
 final int yearValue = 2022;
 Partial singleFieldPartial = new Partial(YEAR_FIELD, yearValue);
 assertEquals(1, singleFieldPartial.size());
 assertEquals(Year.forValue(yearValue), singleFieldPartial.getFieldType(0));
 assertTrue(singleFieldPartial.isMatch(new LocalDateTime(2022, DateTimeConstants.JANUARY_1, DateTimeConstants.DAY_OF_MONTH_1, 0, 0, 0)));
 }

 @Test
 public void testMultipleFieldsPartial() {
 final DateTimeFieldType YEAR_FIELD = DateTimeFieldType.year();
 final DateTimeFieldType MONTH_FIELD = DateTimeFieldType.monthOfYear();
 final DateTimeFieldType DAY_OF_MONTH_FIELD = DateTimeFieldType.dayOfMonth();
 final int yearValue = 2022;
 final int monthValue = DateTimeConstants.MAY;
 final int dayOfMonthValue = DateTimeConstants.FIFTH_OF_MAY;
 Partial multipleFieldsPartial = new Partial(YEAR_FIELD, yearValue, MONTH_FIELD, monthValue, DAY_OF_MONTH_FIELD, dayOfMonthValue);
 assertEquals(3, multipleFieldsPartial.size());
 assertEquals(Year.forValue(yearValue), multipleFieldsPartial.getFieldType(0));
 assertEquals(MonthOfYear.forValue(monthValue), multipleFieldsPartial.getFieldType(1));
 assertEquals(DayOfMonth.forValue(dayOfMonthValue), multipleFieldsPartial.getFieldType(2));
 assertTrue(multipleFieldsPartial.isMatch(new LocalDateTime(yearValue, monthValue, dayOfMonthValue, 0, 0, 0)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullFieldTypeThrowsIllegalArgumentException() {
 Partial nullFieldTypePartial = new Partial(null, 1);
 fail("Should throw IllegalArgumentException for null field type");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDifferentLengthsThrowsIllegalArgumentException() {
 final DateTimeFieldType YEAR_FIELD = DateTimeFieldType.year();
 final DateTimeFieldType MONTH_FIELD = DateTimeFieldType.monthOfYear();
 final DateTimeFieldType DAY_OF_MONTH_FIELD = DateTimeFieldType.dayOfMonth();
 final DateTimeFieldType HOUR_OF_DAY_FIELD = DateTimeFieldType.hourOfDay();
 Partial differentLengthsPartial = new Partial(new DateTimeFieldType[] { YEAR_FIELD, MONTH_FIELD, DAY_OF_MONTH_FIELD, HOUR_OF_DAY_FIELD }, new int[] { 1, 2, 3, 4 });
 fail("Should throw IllegalArgumentException for different lengths of field types and values");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateFieldTypesThrowsIllegalArgumentException() {
 final DateTimeFieldType YEAR_FIELD = DateTimeFieldType.year();
 final DateTimeFieldType MONTH_FIELD = DateTimeFieldType.monthOfYear();
 Partial duplicateFieldsPartial = new Partial(new DateTimeFieldType[] { YEAR_FIELD, MONTH_FIELD, YEAR_FIELD }, new int[] { 2022, 5, 2022 });
 fail("Should throw IllegalArgumentException for duplicate field types");
 }

 @Test
 public void testWithChronologyRetainFields() {
 final DateTimeFieldType YEAR_FIELD = DateTimeFieldType.year();
 final DateTimeFieldType MONTH_FIELD = DateTimeFieldType.monthOfYear();
 final DateTimeFieldType DAY_OF_MONTH_FIELD = DateTimeFieldType.dayOfMonth();
 final int yearValue = 2022;
 final int monthValue = DateTimeConstants.MAY;
 final int dayOfMonthValue = DateTimeConstants.FIFTH_OF_MAY;
 Partial originalPartial = new Partial(YEAR_FIELD, yearValue, MONTH_FIELD, monthValue, DAY_OF_MONTH_FIELD, dayOfMonthValue);
 Partial newPartial = originalPartial.withChronologyRetainFields(DateTimeUtils.UTC);
 assertEquals(originalPartial.getFieldTypes(), newPartial.getFieldTypes());
 assertEquals(originalPartial.getValues(), newPartial.getValues());
 }

 @Test
 public void testWith() {
 final DateTimeFieldType YEAR_FIELD = DateTimeFieldType.year();
 final DateTimeFieldType MONTH_FIELD = DateTimeFieldType.monthOfYear();
 final DateTimeFieldType DAY_OF_MONTH_FIELD = DateTimeFieldType.dayOfMonth();
 final DateTimeFieldType DAY_OF_WEEK_FIELD = DateTimeFieldType.dayOfWeek();
 final int yearValue = 2022;
 final int monthValue = DateTimeConstants.MAY;
 final int dayOfMonthValue = DateTimeConstants.FIFTH_OF_MAY;
 final int dayOfWeekValue = DateTimeConstants.SATURDAY;
 Partial originalPartial = new Partial(YEAR_FIELD, yearValue, MONTH_FIELD, monthValue, DAY_OF_MONTH_FIELD, dayOfMonthValue);
 Partial newPartial = originalPartial.with(DAY_OF_WEEK_FIELD, dayOfWeekValue);
 assertEquals(originalPartial.size() + 1, newPartial.size());
 assertEquals(DAY_OF_WEEK_FIELD, newPartial.getFieldType(originalPartial.size()));
 assertEquals(dayOfWeekValue, newPartial.getValue(originalPartial.size()));
 }

 @Test
 public void testWithout() {
 final DateTimeFieldType YEAR_FIELD = DateTimeFieldType.year();
 final DateTimeFieldType MONTH_FIELD = DateTimeFieldType.monthOfYear();
 final DateTimeFieldType DAY_OF_MONTH_FIELD = DateTimeFieldType.dayOfMonth();
 final int yearValue = 2022;
 final int monthValue = DateTimeConstants.MAY;
 final int dayOfMonthValue = DateTimeConstants.FIFTH_OF_MAY;
 final int indexToRemove = 1;
 Partial originalPartial = new Partial(YEAR_FIELD, yearValue, MONTH_FIELD, monthValue, DAY_OF_MONTH_FIELD, dayOfMonthValue);
 Partial newPartial = originalPartial.without(MONTH_FIELD);
 assertEquals(originalPartial.size() - 1, newPartial.size());
 assertFalse(newPartial.getFieldType(indexToRemove) instanceof DateTimeFieldType);
 }

 @Test
 public void testWithField() {
 final DateTimeFieldType YEAR_FIELD = DateTimeFieldType.year();
 final DateTimeFieldType MONTH_FIELD = DateTimeFieldType.monthOfYear();
 final DateTimeFieldType DAY_OF_MONTH_FIELD = DateTimeFieldType.dayOfMonth();
 final DateTimeFieldType DAY_OF_WEEK_FIELD = DateTimeFieldType.dayOfWeek();
 final int yearValue = 2022;
 final int monthValue = DateTimeConstants.MAY;
 final int dayOfMonthValue = DateTimeConstants.FIFTH_OF_MAY;
 final int dayOfWeekValue = DateTimeConstants.SATURDAY;
 Partial originalPartial = new Partial(YEAR_FIELD, yearValue, MONTH_FIELD, monthValue, DAY_OF_MONTH_FIELD, dayOfMonthValue, DAY_OF_WEEK_FIELD, dayOfWeekValue);
 Partial newPartial = originalPartial.withField(DAY_OF_WEEK_FIELD, DateTimeConstants.SUNDAY);
 assertEquals(originalPartial.size(), newPartial.size());
 assertEquals(dayOfWeekValue, newPartial.getValue(originalPartial.size() - 1));
 assertEquals(DAY_OF_WEEK_FIELD, newPartial.getFieldType(originalPartial.size() - 1));
 }

 @Test
 public void testWithFieldAdded() {
 final DateTimeFieldType HOUR_OF_DAY_FIELD = DateTimeFieldType.hourOfDay();
 final DateTimeFieldType MINUTE_FIELD = DateTimeFieldType.minuteOfHour();
 final DateTimeFieldType SECOND_FIELD = DateTimeFieldType.secondOfMinute();
 final int hoursValue = 10;
 final int minutesValue = 30;
 final int secondsValue = 45;
 final int millisValue = 123;
 Partial originalPartial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(), DateTimeFieldType.dayOfWeek() }, new int[] { 2022, DateTimeConstants.MAY, DateTimeConstants.FIFTH_OF_MAY, DateTimeConstants.SATURDAY });
 Partial newPartial = originalPartial.withFieldAdded(HOUR_OF_DAY_FIELD, hoursValue);
 assertEquals(originalPartial.size() + 1, newPartial.size());
 assertEquals(HOUR_OF_DAY_FIELD, newPartial.getFieldType(originalPartial.size()));
 assertEquals(hoursValue, newPartial.getValue(originalPartial.size()));
 newPartial = newPartial.withFieldAdded(MINUTE_FIELD, minutesValue);
 assertEquals(originalPartial.size() + 2, newPartial.size());
 assertEquals(MINUTE_FIELD, newPartial.getFieldType(originalPartial.size()));
 assertEquals(minutesValue, newPartial.getValue(originalPartial.size()));
 newPartial = newPartial.withFieldAdded(SECOND_FIELD, secondsValue);
 assertEquals(originalPartial.size() + 3, newPartial.size());
 assertEquals(SECOND_FIELD, newPartial.getFieldType(originalPartial.size()));
 assertEquals(secondsValue, newPartial.getValue(originalPartial.size()));
 }

 @Test
 public void testStringFormat() {
 final DateTimeFieldType YEAR_FIELD = DateTimeFieldType.year();
 final DateTimeFieldType MONTH_FIELD = DateTimeFieldType.monthOfYear();
 final DateTimeFieldType DAY_OF_MONTH_FIELD = DateTimeFieldType.dayOfMonth();
 final int yearValue = 2022;
 final int monthValue = DateTimeConstants.MAY;
 final int dayOfMonthValue = DateTimeConstants.FIFTH_OF_MAY;
 Partial partial = new Partial(YEAR_FIELD, yearValue, MONTH_FIELD, monthValue, DAY_OF_MONTH_FIELD, dayOfMonthValue);
 assertEquals("[" + YEAR_FIELD.getName() + "=2022, " + MONTH_FIELD.getName() + "=5, " + DAY_OF_MONTH_FIELD.getName() + "=5]", partial.toString());
 assertEquals("[year=2022, month=5, dayOfMonth=5]", partial.toString("yyyy, MM, dd"));
 assertEquals("[year=2022, month=5, dayOfMonth=5]", partial.toString("yyyy, M, dd"));
 assertEquals("[year=2022, month=5, dayOfMonth=5]", partial.toString("yyyy, MMM, d"));
 assertEquals("[year=2022, month=5, dayOfMonth=5]", partial.toString("yyyy, MMMM, ddd"));
 assertEquals("[year=2022, month=5, dayOfMonth=5]", partial.toString("yyyy, 'May', dddd"));
 assertEquals("[year=2022, month=5, dayOfMonth=5]", partial.toString("yyyy, 'May', ddd"));
 assertEquals("[year=2022, month=5, dayOfMonth=5]", partial.toString("yyyy, 'May', dd"));
 assertEquals("[year=2022, month=5, dayOfMonth=5]", partial.toString("yyyy, 'May', d"));
 }

 @Test
 public void testIsMatch() {
 final DateTimeFieldType YEAR_FIELD = DateTimeFieldType.year();
 final DateTimeFieldType MONTH_FIELD = DateTimeFieldType.monthOfYear();
 final DateTimeFieldType DAY_OF_MONTH_FIELD = DateTimeFieldType.dayOfMonth();
 final DateTimeFieldType DAY_OF_WEEK_FIELD = DateTimeFieldType.dayOfWeek();
 final int yearValue = 2022;
 final int monthValue = DateTimeConstants.MAY;
 final int dayOfMonthValue = DateTimeConstants.FIFTH_OF_MAY;
 final int dayOfWeekValue = DateTimeConstants.SATURDAY;
 Partial partial = new Partial(YEAR_FIELD, yearValue, MONTH_FIELD, monthValue, DAY_OF_MONTH_FIELD, dayOfMonthValue, DAY_OF_WEEK_FIELD, dayOfWeekValue);
 assertTrue(partial.isMatch(new LocalDateTime(yearValue, monthValue, dayOfMonthValue, 0, 0, 0, 0)));
 assertTrue(partial.isMatch(new LocalDateTime(yearValue, monthValue, dayOfMonthValue, 10, 30, 45, 123)));
 assertTrue(partial.isMatch(new LocalDateTime(yearValue, monthValue, dayOfMonthValue, 10, 30, 45, 123, DateTimeUtils.UTC)));
 assertTrue(partial.isMatch(new LocalDateTime(yearValue, monthValue, dayOfMonthValue, 10, 30, 45, 123, DateTimeUtils.UTC).withFieldsAdded(DAY_OF_WEEK_FIELD, dayOfWeekValue)));
 assertTrue(partial.isMatch(new LocalDateTime(yearValue, monthValue, dayOfMonthValue, 10, 30, 45, 123, DateTimeUtils.UTC).withFieldsAdded(DAY_OF_WEEK_FIELD, DateTimeConstants.SUNDAY)));
 }

 @Test
 public void testGetFormatter() {
 final DateTimeFieldType YEAR_FIELD = DateTimeFieldType.year();
 final DateTimeFieldType MONTH_FIELD = DateTimeFieldType.monthOfYear();
 final DateTimeFieldType DAY_OF_MONTH_FIELD = DateTimeFieldType.dayOfMonth();
 final int yearValue = 2022;
 final int monthValue = DateTimeConstants.MAY;
 final int dayOfMonthValue = DateTimeConstants.FIFTH_OF_MAY;
 Partial partial = new Partial(YEAR_FIELD, yearValue, MONTH_FIELD, monthValue, DAY_OF_MONTH_FIELD, dayOfMonthValue);
 DateTimeFormatter formatter = partial.getFormatter();
 assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", formatter.pattern());
 LocalDateTime localDateTime = new LocalDateTime(yearValue, monthValue, dayOfMonthValue, 10, 30, 45, 123, DateTimeUtils.UTC);
 String formatted = formatter.print(localDateTime);
 assertEquals("2022-05-05T10:30:45.123Z", formatted);
 }
}