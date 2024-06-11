package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod testInstance;
 private int expectedValue;

 @Before
 public void setUp() throws Exception {
 testInstance = new MySingleFieldPeriod(5);
 expectedValue = 5;
 }

 @Test
 public void testGetFieldType() {
 DurationFieldType fieldType = testInstance.getFieldType();
 assertSame("Expected field type to match", MyDurationFieldType.INSTANCE, fieldType);
 }

 @Test
 public void testGetPeriodType() {
 PeriodType periodType = testInstance.getPeriodType();
 assertEquals("Expected period type to be MyPeriodType", MyPeriodType.INSTANCE, periodType);
 }

 @Test
 public void testSize() {
 int size = testInstance.size();
 assertEquals("Expected size to be 1", 1, size);
 }

 @Test
 public void testGetFieldTypeAtIndex() {
 DurationFieldType fieldType = testInstance.getFieldType(0);
 assertSame("Expected field type to match", MyDurationFieldType.INSTANCE, fieldType);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFieldTypeAtInvalidIndex() {
 testInstance.getFieldType(1);
 }

 @Test
 public void testGetValueAtIndex() {
 int value = testInstance.getValue(0);
 assertEquals("Expected value to be 5", expectedValue, value);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueAtInvalidIndex() {
 testInstance.getValue(1);
 }

 @Test
 public void testGetValue() {
 int value = testInstance.getValue();
 assertEquals("Expected value to be 5", expectedValue, value);
 }

 @Test
 public void testEquals() {
 MySingleFieldPeriod testInstance2 = new MySingleFieldPeriod(5);
 assertTrue("Expected equal instances to be equal", testInstance.equals(testInstance2));
 MySingleFieldPeriod testInstance3 = new MySingleFieldPeriod(6);
 assertFalse("Expected unequal instances to be unequal", testInstance.equals(testInstance3));
 }

 @Test
 public void testHashCode() {
 int hashCode1 = testInstance.hashCode();
 int hashCode2 = testInstance.hashCode();
 assertSame("Expected equal instances to have the same hashcode", hashCode1, hashCode2);
 MySingleFieldPeriod testInstance4 = new MySingleFieldPeriod(6);
 int hashCode3 = testInstance.hashCode();
 int hashCode4 = testInstance4.hashCode();
 assertNotSame("Expected unequal instances to have different hashcodes", hashCode1, hashCode4);
 }

 @Test
 public void testCompareTo() {
 MySingleFieldPeriod testInstance5 = new MySingleFieldPeriod(3);
 int comparisonResult = testInstance.compareTo(testInstance5);
 assertTrue("Expected testInstance to be greater than testInstance5", comparisonResult > 0);
 MySingleFieldPeriod testInstance6 = new MySingleFieldPeriod(5);
 int comparisonResult2 = testInstance.compareTo(testInstance6);
 int comparisonResult3 = testInstance6.compareTo(testInstance);
 assertEquals("Expected comparison results to be equal", 0, comparisonResult2);
 assertEquals("Expected comparison results to be equal", 0, comparisonResult3);
 }

 @Test
 public void testToPeriod() {
 Period period = testInstance.toPeriod();
 assertEquals("Expected period to have the same value as testInstance", testInstance, period);
 }

 @Test
 public void testToMutablePeriod() {
 MutablePeriod mutablePeriod = testInstance.toMutablePeriod();
 assertEquals("Expected mutable period to have the same value as testInstance", testInstance, mutablePeriod);
 }

 @Test
 public void testToPeriodWithSameChronology() {
 Chronology chronology = ISOChronology.getInstanceUTC();
 ReadableInstant instant = new DateTime(2022, 1, 1, 0, 0, 0, 0, chronology).toInstant();
 Period period = testInstance.toPeriod(instant, chronology);
 assertEquals("Expected period to have the same value as testInstance", testInstance, period);
 }

 @Test
 public void testToPeriodWithDifferentChronology() {
 Chronology chronology2 = GregorianChronology.getInstance();
 ReadableInstant instant2 = new DateTime(2022, 1, 1, 0, 0, 0, 0, chronology2).toInstant();
 Period period = testInstance.toPeriod(instant2, chronology2);
 assertEquals("Expected period to have the same value as testInstance", testInstance, period);
 }
}

 class MyDurationFieldType implements DurationFieldType {
 public static final MyDurationFieldType INSTANCE = new MyDurationFieldType();
 private static final int UNITS = 1;

 @Override
 public int getPreciseUnits() {
 return UNITS;
 }

 @Override
 public String getNameKey() {
 return "MyDurationFieldType";
 }

 @Override
 public String getName() {
 return "MyDurationUnit";
 }

 @Override
 public int getLength(final TemporalUnit unit) {
 return 1;
 }

 @Override
 public int getLength(final long amountToAdd, final TemporalUnit unit) {
 return 1;
 }

 @Override
 public int getUnitMillis() {
 return 1000;
 }

 @Override
 public DurationField getField(FinalChronology chronology) {
 return new MyDurationField();
 }
 }

 class MyDurationField implements DurationField {

 @Override
 public int getDifference(final long value, final long baseValue) {
 return value - baseValue;
 }

 @Override
 public int getValue(final ReadableInstant instant) {
 return 0;
 }

 @Override
 public int getValue(final ReadablePartial temporal) {
 return 0;
 }

 @Override
 public int getValue(final long baseValue, final Chronology chronology) {
 return 0;
 }
}

 class MyPeriodType implements PeriodType {
 public static final MyPeriodType INSTANCE = new MyPeriodType();

 @Override
 public String getID() {
 return "MyPeriodType";
 }

 @Override
 public DurationFieldType[] getFields() {
 return new DurationFieldType[] { MyDurationFieldType.INSTANCE };
 }
}

 class MySingleFieldPeriod extends BaseSingleFieldPeriod {

 private MySingleFieldPeriod(final int iPeriod) {
 super(iPeriod);
 }
}