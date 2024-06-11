package org.joda.time.base;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod instance;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 instance = new MySingleFieldPeriod(5);
 }

 @Test
 public void testGetFieldType() {
 DurationFieldType expected = new GenericFieldType("TEST_FIELD", PeriodType.create(new int[] { 1 }, new String[] { "TEST_FIELD" }));
 assertEquals(expected, instance.getFieldType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetFieldTypeUnsupported() {
 instance.getFieldType(1);
 }

 @Test
 public void testGetPeriodType() {
 PeriodType expected = new PeriodTypeImpl(new int[] { 1 }, new String[] { "TEST_FIELD" });
 assertEquals(expected, instance.getPeriodType());
 }

 @Test
 public void testSize() {
 assertEquals(1, instance.size());
 }

 @Test
 public void testGetValue() {
 assertEquals(5, instance.getValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueInvalidIndex() {
 instance.getValue(1);
 }

 @Test
 public void testGetValueAndSetValue() {
 int originalValue = instance.getValue();
 instance.setValue(10);
 assertEquals(10, instance.getValue());
 assertEquals(originalValue, instance.getValue());
 instance.setValue(originalValue);
 }

 @Test
 public void testStandardPeriodIn() {
 long divisor = 2;
 Period testPeriod = Period.parse("PT5S");
 int expected = BaseSingleFieldPeriod.standardPeriodIn(testPeriod, divisor);
 assertEquals(2, expected);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInWithNullPeriod() {
 Period testPeriod = null;
 int expected = BaseSingleFieldPeriod.standardPeriodIn(testPeriod, divisor);
 fail();
 }

 @Test
 public void testGet() {
 DurationFieldType testField = new GenericFieldType("TEST_FIELD", PeriodType.create(new int[] { 1 }, new String[] { "TEST_FIELD" }));
 int expected = 5;
 assertEquals(expected, instance.get(testField));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithInvalidFieldType() {
 DurationFieldType invalidFieldType = new GenericFieldType("INVALID_FIELD", PeriodType.create(new int[] { 1 }, new String[] { "TEST_FIELD" }));
 int expected = instance.get(invalidFieldType);
 fail();
 }

 @Test
 public void testIsSupported() {
 DurationFieldType testField = new GenericFieldType("TEST_FIELD", PeriodType.create(new int[] { 1 }, new String[] { "TEST_FIELD" }));
 assertTrue(instance.isSupported(testField));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsSupportedWithInvalidFieldType() {
 DurationFieldType invalidFieldType = new GenericFieldType("INVALID_FIELD", PeriodType.create(new int[] { 1 }, new String[] { "TEST_FIELD" }));
 boolean supported = instance.isSupported(invalidFieldType);
 fail();
 }

 @Test
 public void testToPeriod() {
 Period expected = Period.withFields(new BasicFieldSet(true).add(instance.getFieldType(), 5));
 Period actual = instance.toPeriod();
 assertEquals(expected, actual);
 }

 @Test
 public void testToMutablePeriod() {
 MutablePeriod mutablePeriod = new MutablePeriod();
 instance.toMutablePeriod().addTo(mutablePeriod);
 Period expected = Period.withFields(new BasicFieldSet(true).add(instance.getFieldType(), 5));
 Period actual = mutablePeriod.toImmutable();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 MySingleFieldPeriod anotherInstance = new MySingleFieldPeriod(5);
 assertTrue(instance.equals(anotherInstance));
 }

 @Test
 public void testEqualsDifferentClass() {
 TestAnotherClass anotherInstance = new TestAnotherClass();
 assertFalse(instance.equals(anotherInstance));
 }

 @Test
 public void testEqualsNull() {
 assertFalse(instance.equals(null));
 }

 @Test
 public void testCompareTo() {
 MySingleFieldPeriod anotherInstance = new MySingleFieldPeriod(3);
 assertEquals(1, instance.compareTo(anotherInstance));
 }

 @Test
 public void testCompareToSameInstance() {
 assertEquals(0, instance.compareTo(instance));
 }

 @Test
 public void testCompareToNull() {
 try {
 instance.compareTo(null);
 fail();
 } catch (ClassCastException e) {
 // expected
 }
 }
}

 class MySingleFieldPeriod extends BaseSingleFieldPeriod {
 MySingleFieldPeriod(int iPeriod) {
 super(iPeriod);
 }
 }