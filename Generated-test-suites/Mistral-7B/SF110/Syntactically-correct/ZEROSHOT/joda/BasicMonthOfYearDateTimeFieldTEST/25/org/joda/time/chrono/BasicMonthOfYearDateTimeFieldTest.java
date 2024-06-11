import org.joda.time.*;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.*;
import org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 private static final BasicChronology ISO_CHRONO = new ISOChronology();

 @org.junit.Test
 public void testBasicMonthOfYearDateTimeField() {
 BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(ISO_CHRONO, 2);

 // test isLenient method
 Assert.assertFalse(testField.isLenient());

 // test get method
 long testDate = ISO_CHRONO.getMillis("2000-01-31");
 int testMonth = testField.get(testDate);
 Assert.assertEquals(1, testMonth);

 // test add method with integer argument
 long expectedDate1 = ISO_CHRONO.getMillis("2000-01-31") + 12 * 30 * 24 * 60 * 60 * 1000;
 long resultDate1 = testField.add(testDate, 12);
 assertEquals(expectedDate1, resultDate1);

 // test add method with long argument
 long expectedDate2 = ISO_CHRONO.getMillis("2000-01-31") + 12 * 365 * 24 * 60 * 60 * 1000;
 long resultDate2 = testField.add(testDate, 12L);
 assertEquals(expectedDate2, resultDate2);

 // test addWrapField method
 long testDateWithLeapYear = ISO_CHRONO.getMillis("2000-02-29");
 int expectedLeapAmount = testField.getLeapAmount(testDateWithLeapYear);
 long resultDateWithLeapYear = testField.addWrapField(testDateWithLeapYear, expectedLeapAmount + 1);
 long expectedDate3 = ISO_CHRONO.getMillis("2001-03-01");
 assertEquals(expectedDate3, resultDateWithLeapYear);

 // test getDifferenceAsLong method
 long testDate3 = ISO_CHRONO.getMillis("2000-01-31");
 long testDate4 = ISO_CHRONO.getMillis("2000-02-28");
 long difference = testField.getDifferenceAsLong(testDate3, testDate4);
 assertEquals(29 * 24 * 60 * 60 * 1000, difference);

 // test set method
 long expectedDate4 = ISO_CHRONO.getMillis("2000-01-31") + 4 * 30 * 24 * 60 * 60 * 1000;
 long resultDate4 = testField.set(testDate, 4);
 assertEquals(expectedDate4, resultDate4);

 // test getRangeDurationField method
 DurationField rangeDurationField = testField.getRangeDurationField();
 Assert.assertNotNull(rangeDurationField);
 assertEquals(DurationFieldType.years(), rangeDurationField.getType());

 // test isLeap method
 long testDateWithLeapMonth = ISO_CHRONO.getMillis("2000-02-29");
 Assert.assertTrue(testField.isLeap(testDateWithLeapMonth));

 // test getLeapAmount method
 int leapAmount = testField.getLeapAmount(testDateWithLeapMonth);
 Assert.assertEquals(1, leapAmount);

 // test getLeapDurationField method
 DurationField leapDurationField = testField.getLeapDurationField();
 Assert.assertNotNull(leapDurationField);
 assertEquals(DurationFieldType.days(), leapDurationField.getType());

 // test getMinimumValue method
 int minimumValue = testField.getMinimumValue();
 Assert.assertEquals(1, minimumValue);

 // test getMaximumValue method
 int maximumValue = testField.getMaximumValue();
 Assert.assertEquals(12, maximumValue);

 // test roundFloor method
 long testDate5 = ISO_CHrono.getMillis("2000-02-01");
 long floorDate = testField.roundFloor(testDate5);
 long expectedFloorDate = ISO_Chrono.getMillis("2000-01-31");
 assertEquals(expectedFloorDate, floorDate);

 // test remainder method
 long testDate6 = ISO_Chrono.getMillis("2000-02-29");
 long remainderDate = testField.remainder(testDate6);
 long expectedRemainderDate = ISO_Chrono.getMillis("2000-03-01");
 assertEquals(expectedRemainderDate, remainderDate);
 }
}