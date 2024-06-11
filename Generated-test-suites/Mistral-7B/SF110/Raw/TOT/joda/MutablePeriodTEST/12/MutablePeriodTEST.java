import org.joda.time.*;
import org.joda.time.base.Interval;
import org.joda.time.format.ISOPeriodFormat;
import org.junit.Test;

import static org.junit.Assert.*;

public class MutablePeriodTest {

 @Test
 public void testParse() {
 MutablePeriod period = MutablePeriod.parse("P1Y2M3DT4H5M6S750MS");
 assertEquals("One year, two months, three days, four hours, five minutes, six seconds, seven hundred fifty milliseconds", period.toString());
 }

 @Test
 public void testParseWithCustomFormatter() {
 PeriodFormatter formatter = ISOPeriodFormat.standard().withParsedFields(PeriodFormat.FIELD_YEARS, PeriodFormat.FIELD_MONTHS, PeriodFormat.FIELD_DAYS, PeriodFormat.FIELD_HOURS, PeriodFormat.FIELD_MINUTES, PeriodFormat.FIELD_SECONDS, PeriodFormat.FIELD_MILLISECONDS);
 MutablePeriod period = MutablePeriod.parse("P1Y2M3DT4H5M6.75S", formatter);
 assertEquals("One year, two months, three days, four hours, five minutes, six seconds, seven hundred fifty milliseconds", period.toString());
 }

 @Test
 public void testConstructors() {
 // Default constructor
 MutablePeriod defaultPeriod = new MutablePeriod();
 assertEquals("Pt0S", defaultPeriod.toString());

 // Constructors with PeriodType
 MutablePeriod periodTypeStandard = new MutablePeriod(PeriodType.standard());
 MutablePeriod periodTypeCustom = new MutablePeriod(new PeriodType() {
 @Override
 public String getID() {
 return "custom";
 }

 @Override
 public String getIDString() {
 return "P.custom";
 }

 @Override
 public int getFieldsCount() {
 return 8;
 }

 @Override
 public int getFieldsSize() {
 return 8;
 }

 @Override
 public DurationFieldType getFieldType(int i) {
 switch (i) {
 case 0:
 return DurationFieldType.years();
 case 1:
 return DurationFieldType.months();
 case 2:
 return DurationFieldType.weeks();
 case 3:
 return DurationFieldType.days();
 case 4:
 return DurationFieldType.hours();
 case 5:
 return DurationFieldType.minutes();
 case 6:
 return DurationFieldType.seconds();
 case 7:
 return DurationFieldType.millis();
 default:
 throw new AssertionError("Invalid index: " + i);
 }
 }

 @Override
 public int getPrecision(int field) {
 return 0;
 }

 @Override
 public void add(DurationFieldType field, int value) {
 throw new UnsupportedOperationException();
 }

 @Override
 public void add(ReadablePeriod period) {
 throw new UnsupportedOperationException();
 }
 });
 MutablePeriod periodTypeStandardInstance = new MutablePeriod(periodTypeStandard);
 MutablePeriod periodTypeCustomInstance = new MutablePeriod(periodTypeCustom);
 assertEquals("Pt0S", periodTypeStandardInstance.toString());
 assertEquals("P.custom", periodTypeCustomInstance.toString());

 // Constructors with values
 MutablePeriod periodValues = new MutablePeriod(3, 2, 4, 5, 6, 7, 8, 9);
 assertEquals("P3Y2M4DT5H6M7.8S", periodValues.toString());
 }

 @Test
 public void testConstructorsWithSerializable() {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
 MutablePeriod mutablePeriod = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 objectOutputStream.writeObject(mutablePeriod);
 objectOutputStream.close();
 byte[] bytes = byteArrayOutputStream.toByteArray();
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
 ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
 MutablePeriod deserializedPeriod = (MutablePeriod)objectInputStream.readObject();
 assertEquals("P1Y2M3DT4H5M6.8S", deserializedPeriod.toString());
 }

 @Test
 public void testClone() {
 MutablePeriod originalPeriod = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 MutablePeriod clonedPeriod = originalPeriod.copy();
 assertEquals(originalPeriod, clonedPeriod);
 clonedPeriod.add(1, 1, 1, 1, 1, 1, 1, 1);
 assertNotEquals(originalPeriod, clonedPeriod);
 }

 @Test
 public void testClear() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 period.clear();
 assertEquals("Pt0S", period.toString());
 }

 @Test
 public void testSetValue() {
 MutablePeriod period = new MutablePeriod();
 period.setValue(0, 1);
 assertEquals("P1D", period.toString());
 period.setValue(1, 2);
 assertEquals("P1Y2D", period.toString());
 }

 @Test
 public void testSetField() {
 MutablePeriod period = new MutablePeriod();
 period.setField(DurationFieldType.years(), 1);
 assertEquals("P1Y", period.toString());
 period.setField(DurationFieldType.months(), 2);
 assertEquals("P1Y2M", period.toString());
 }

 @Test
 public void testSetPeriod() {
 MutablePeriod period = new MutablePeriod();
 period.setPeriod(new Period(3, 2, 4, 5, 6, 7, 8, 9));
 assertEquals("P3Y2M4DT5H6M7.8S", period.toString());
 }

 @Test
 public void testSetPeriodWithStartAndEndInstant() {
 ReadableInstant startInstant = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
 ReadableInstant endInstant = new DateTime(2022, 1, 2, 0, 0, 0, 0, DateTimeZone.UTC);
 MutablePeriod period = new MutablePeriod();
 period.setPeriod(startInstant, endInstant);
 assertEquals("P1D", period.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPeriodWithSameInstant() {
 ReadableInstant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
 MutablePeriod period = new MutablePeriod();
 period.setPeriod(instant, instant);
 }

 @Test
 public void testSetPeriodWithStartAndEndMillis() {
 long startMillis = DateTimeUtils.currentMillis();
 long endMillis = startMillis + 1000L;
 MutablePeriod period = new MutablePeriod();
 period.setPeriod(startMillis, endMillis);
 Interval interval = new Interval(startMillis, endMillis);
 assertEquals("P1.0001D", period.toString());
 assertEquals(interval, period.toInterval());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPeriodWithNegativeMillis() {
 long startMillis = DateTimeUtils.currentMillis();
 long endMillis = startMillis - 1000L;
 MutablePeriod period = new MutablePeriod();
 period.setPeriod(startMillis, endMillis);
 }

 @Test(expected = NullPointerException.class)
 public void testSetPeriodWithNullReadableInstant() {
 MutablePeriod period = new MutablePeriod();
 period.setPeriod(null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetPeriodWithNullReadableDuration() {
 MutablePeriod period = new MutablePeriod();
 period.setPeriod(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPeriodWithNullChronology() {
 MutablePeriod period = new MutablePeriod();
 period.setPeriod(DateTimeUtils.currentInstant().toDate(), null);
 }

 @Test
 public void testAdd() {
 MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 MutablePeriod period2 = new MutablePeriod(2, 3, 4, 5, 6, 7, 8, 9);
 MutablePeriod sum = period1.add(period2);
 assertEquals("P3Y5M7DT5H6M7.8S", sum.toString());
 }

 @Test
 public void testAddWithReadableDuration() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 MutablePeriod sum = period.add(new Period(2, 3, 4, 5, 6, 7, 8, 9));
 assertEquals("P3Y5M7DT5H6M7.8S", sum.toString());
 }

 @Test
 public void testAddWithLong() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 MutablePeriod sum = period.add(1000L);
 assertEquals("P1001M", sum.toString());
 }

 @Test
 public void testAddWithLongAndCustomChronology() {
 DateTimeZone customChronology = DateTimeZone.forID("America/New_York");
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 MutablePeriod sum = period.add(1000L, customChronology);
 assertEquals("P1001M", sum.toString());
 assertEquals(customChronology, sum.getChronology());
 }

 @Test
 public void testMergePeriod() {
 MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 MutablePeriod period2 = new MutablePeriod(2, 3, 4, 5, 6, 7, 8, 9);
 MutablePeriod mergedPeriod = new MutablePeriod();
 mergedPeriod.mergePeriod(period1);
 mergedPeriod.mergePeriod(period2);
 assertEquals("P3Y5M7DT5H6M7.8S", mergedPeriod.toString());
 }

 @Test
 public void testGetYears() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 assertEquals(1, period.getYears());
 }

 @Test
 public void testGetMonths() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 assertEquals(2, period.getMonths());
 }

 @Test
 public void testGetWeeks() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 assertEquals(3, period.getWeeks());
 }

 @Test
 public void testGetDays() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 assertEquals(4, period.getDays());
 }

 @Test
 public void testGetHours() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 assertEquals(5, period.getHours());
 }

 @Test
 public void testGetMinutes() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 assertEquals(6, period.getMinutes());
 }

 @Test
 public void testGetSeconds() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 assertEquals(7, period.getSeconds());
 }

 @Test
 public void testGetMillis() {
 MutablePeriod period = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
 assertEquals(8, period.getMillis());
 }

 @Test
 public void testSetFieldWithNegativeValue() {
 MutablePeriod period = new MutablePeriod();
 try {
 period.setField(DurationFieldType.years(), -1);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSetFieldWithValueBiggerThanMaxValue() {
 MutablePeriod period = new MutablePeriod();
 try {
 period.setField(DurationFieldType.years(), Integer.MAX_VALUE + 1);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSetFieldWithValueBiggerThanPeriodTypeMaxValue() {
 MutablePeriod period = new MutablePeriod(new PeriodType() {
 @Override
 public int getFieldsCount() {
 return 1;
 }

 @Override
 public int getFieldsSize() {
 return 1;
 }

 @Override
 public DurationFieldType getFieldType(int i) {
 return DurationFieldType.years();
 }

 @Override
 public int getPrecision(int field) {
 return 0;
 }

 @Override
 public void add(DurationFieldType field, int value) {
 throw new UnsupportedOperationException();
 }

 @Override
 public void add(ReadablePeriod period) {
 throw new UnsupportedOperationException();
 }

 @Override
 public void setValues(int[] values) {
 throw new UnsupportedOperationException();
 }

 @Override
 public int getValues(int[] values) {
 throw new UnsupportedOperationException();
 }

 @Override
 public ReadableDuration toDuration() {
 throw new UnsupportedOperationException();
 }

 @Override
 public int getMillis() {
 throw new UnsupportedOperationException();
 }

 @Override
 public void setPeriod(ReadablePeriod period) {
 throw new UnsupportedOperationException();
 }

 @Override
 public void mergePeriod(ReadablePeriod period) {
 throw new UnsupportedOperationException();
 }

 @Override
 public void clear() {
 throw new UnsupportedOperationException();
 }

 @Override
 public boolean equals(Object obj) {
 throw new UnsupportedOperationException();
 }

 @Override
 public int hashCode() {
 throw new UnsupportedOperationException();
 }

 @Override
 public String toString() {
 throw new UnsupportedOperationException();
 }

 @Override
 public PeriodType clone() {
 throw new UnsupportedOperationException();
 }
 });
 try {
 period.setField(DurationFieldType.years(), Integer.MAX_VALUE + 1000);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}