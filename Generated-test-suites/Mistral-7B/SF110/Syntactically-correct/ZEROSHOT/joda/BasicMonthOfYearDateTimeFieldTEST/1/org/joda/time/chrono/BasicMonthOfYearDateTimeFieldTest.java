import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

 @Test
 public void testIsLenient() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 assertFalse(field.isLenient());
 }

 @Test
 public void testGet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 DateTime date = new DateTime(2022, 2, 28);
 assertEquals(2, field.get(date.getMillis()));
 }

 @Test
 public void testAdd() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 DateTime date = new DateTime(2022, 2, 28);
 long newDate = field.add(date.getMillis(), 3);
 DateTime newDateTime = new DateTime(newDate);
 assertEquals(new DateTime(2022, 3, 31), newDateTime);
 }

 @Test
 public void testAddLong() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 DateTime date = new DateTime(2022, 2, 28);
 long newDate = field.add(date.getMillis(), 6);
 DateTime newDateTime = new DateTime(newDate);
 assertEquals(new DateTime(2022, 4, 30), newDateTime);
 }

 @Test
 public void testAddWrapField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 DateTime date = new DateTime(2022, 2, 28);
 long newDate = field.addWrapField(date.getMillis(), 13);
 DateTime newDateTime = new DateTime(newDate);
 assertEquals(new DateTime(2022, 3, 11), newDateTime);
 }

 @Test
 public void testGetDifferenceAsLong() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 DateTime date1 = new DateTime(2022, 2, 28);
 DateTime date2 = new DateTime(2022, 4, 30);
 long difference = field.getDifferenceAsLong(date1.getMillis(), date2.getMillis());
 assertEquals(2, difference);
 }

 @Test
 public void testSet() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 DateTime date = new DateTime(2022, 2, 28);
 long newDate = field.set(date.getMillis(), 4);
 DateTime newDateTime = new DateTime(newDate);
 assertEquals(new DateTime(2022, 4, 30), newDateTime);
 }

 @Test
 public void testGetRangeDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 DurationField rangeDurationField = field.getRangeDurationField();
 assertEquals(new Years(1), rangeDurationField);
 }

 @Test
 public void testIsLeap() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 DateTime date = new DateTime(2022, 2, 28);
 assertTrue(field.isLeap(date.getMillis()));
 }

 @Test
 public void testGetLeapAmount() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 DateTime date = new DateTime(2022, 2, 28);
 assertEquals(1, field.getLeapAmount(date.getMillis()));
 }

 @Test
 public void testGetLeapDurationField() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 DurationField leapDurationField = field.getLeapDurationField();
 assertEquals(new Days(1), leapDurationField);
 }

 @Test
 public void testGetMinimumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 assertEquals(1, field.getMinimumValue());
 }

 @Test
 public void testGetMaximumValue() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 assertEquals(12, field.getMaximumValue());
 }

 @Test
 public void testRoundFloor() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 DateTime date = new DateTime(2022, 2, 28);
 long roundedDate = field.roundFloor(date.getMillis());
 assertEquals(new DateTime(2022, 2, 28).getMillis(), roundedDate);
 }

 @Test
 public void testRemainder() {
 BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISOChronology.getInstance(), 2);
 DateTime date = new DateTime(2022, 2, 29);
 long remainder = field.remainder(date.getMillis());
 assertEquals(1, remainder);
 }
}