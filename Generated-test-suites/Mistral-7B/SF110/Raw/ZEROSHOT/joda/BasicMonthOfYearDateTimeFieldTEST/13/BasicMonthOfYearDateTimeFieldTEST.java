import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeFieldVerifier;
import org.joda.time.MutableDateTime;
import org.joda.time.YearMonth;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final Chronology ISO_CHRONOLOGY = DateTimeConstants.ISO_INSTANT_CHRONOLOGY;

 @Test
 public void testConstruct() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
 assertEquals(ISO_CHRONOLOGY, field.getChronology());
 assertEquals(DateTimeConstants.JANUARY, field.getMax());
 assertEquals(false, field.isLenient());
 }

 @Test
 public void testGet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);

 MutableDateTime testDate = new MutableDateTime();
 testDate.set(2015, 1, 1, 0, 0, 0, 0);
 long testValue = field.get(testDate.getMillis());

 assertEquals(1, testValue);
 }

 @Test
 public void testAddOneMonth() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);

 MutableDateTime testDate = new MutableDateTime();
 testDate.set(2015, 1, 1, 0, 0, 0, 0);
 long testValue = field.add(testDate.getMillis(), 1);

 YearMonth testYearMonth = new YearMonth(2015, 2);
 assertEquals(testYearMonth.getMillis(), testValue);
 }

 @Test
 public void testAddMultipleMonths() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHronology, DateTimeConstants.JANUARY);

 MutableDateTime testDate = new MutableDateTime();
 testDate.set(2015, 1, 1, 0, 0, 0, 0);
 long testValue = field.add(testDate.getMillis(), 12);

 YearMonth testYearMonth = new YearMonth(2016, 2);
 assertEquals(testYearMonth.getMillis(), testValue);
 }

 @Test
 public void testAddNegativeMonths() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);

 MutableDateTime testDate = new MutableDateTime();
 testDate.set(2015, 1, 1, 0, 0, 0, 0);
 long testValue = field.add(testDate.getMillis(), -1);

 YearMonth testYearMonth = new YearMonth(2014, 12);
 assertEquals(testYearMonth.getMillis(), testValue);
 }

 @Test
 public void testAddLong() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);

 MutableDateTime testDate = new MutableDateTime();
 testDate.set(2015, 1, 1, 0, 0, 0, 0);
 long testValue = field.add(testDate.getMillis(), 365);

 YearMonth testYearMonth = new YearMonth(2016, 1);
 assertEquals(testYearMonth.getMillis(), testValue);
 }

 @Test
 public void testAddWrapField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);

 MutableDateTime testDate = new MutableDateTime();
 testDate.set(2015, 12, 31, 0, 0, 0, 0);
 long testValue = field.addWrapField(testDate.getMillis(), 13);

 YearMonth testYearMonth = new YearMonth(2016, 2);
 assertEquals(testYearMonth.getMillis(), testValue);
 }

 @Test
 public void testSet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 MutableDateTime testDate = new MutableDateTime();

 testDate.set(2015, 1, 1, 0, 0, 0, 0);
 long testValue = field.set(testDate.getMillis(), 3);

 assertEquals(2015, testDate.getYear());
 assertEquals(3, testDate.getMonthOfYear());
 assertEquals(testValue, testDate.getMillis());
 }

 @Test
 public void testGetRangeDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 DurationField Type = field.getRangeDurationField();
 assertEquals(DurationFieldType.years(), Type);
 }

 @Test
 public void testIsLeap() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.FEBRUARY);
 MutableDateTime testDate = new MutableDateTime();
 testDate.set(2016, 2, 29, 0, 0, 0, 0);
 assertTrue(field.isLeap(testDate.getMillis()));
 }

 @Test
 public void testGetLeapAmount() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.FEBRUARY);
 MutableDateTime testDate = new MutableDateTime();
 testDate.set(2016, 2, 29, 0, 0, 0, 0);
 assertEquals(1, field.getLeapAmount(testDate.getMillis()));
 }

 @Test
 public void testGetLeapDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.FEBRUARY);
 DurationField Type = field.getLeapDurationField();
 assertEquals(DurationFieldType.days(), Type);
 }

 @Test
 public void testGetMinimumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 assertEquals(1, field.getMinimumValue());
 }

 @Test
 public void testGetMaximumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 assertEquals(12, field.getMaximumValue());
 }

 @Test
 public void testRoundFloor() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 MutableDateTime testDate = new MutableDateTime();
 testDate.set(2015, 1, 1, 0, 0, 0, 0);
 long testValue = field.roundFloor(testDate.getMillis());
 assertEquals(testDate.getYear() * 12 + 1, testValue);
 }

 @Test
 public void testRemainder() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 MutableDateTime testDate = new MutableDateTime();
 testDate.set(2015, 1, 31, 0, 0, 0, 0);
 long testValue = field.remainder(testDate.getMillis());
 assertEquals(2, testValue);
 }

 @Test
 public void testAddPartial() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 ReadablePartial testPartial = new LocalDateTimeFieldVerifier(DateTimeFieldType.dayOfMonth(), ISO_CHRONOLOGY, 1);
 int[] result = field.add(testPartial, 1, new int[3], 0);
 assertEquals(2, result[0]);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testAddPartialLarge() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
 ReadablePartial testPartial = new LocalDateTimeFieldVerifier(DateTimeFieldType.dayOfMonth(), ISO_CHRONOLOGY, 12345);
 int[] result = field.add(testPartial, 1, new int[3], 0);
 fail("Unexpected result");
 }
}