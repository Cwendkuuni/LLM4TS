package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.*;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod instance;

 @Before
 public void setUp() {
 instance = new MySingleFieldPeriod(1);
 }

 @Test
 public void testGetFieldType() {
 DurationFieldType fieldType = instance.getFieldType();
 assertNotNull(fieldType);
 assertEquals(MySingleFieldPeriod.class, fieldType.getFieldTypeClass());
 }

 @Test
 public void testGetPeriodType() {
 PeriodType periodType = instance.getPeriodType();
 assertNotNull(periodType);
 assertEquals(MySingleFieldPeriod.class, periodType.getPeriodTypeClass());
 }

 @Test
 public void testSize() {
 assertEquals(1, instance.size());
 }

 @Test
 public void testGetValue() {
 assertEquals(1, instance.getValue());
 }

 @Test
 public void testGetValue_NegativeIndex() {
 try {
 instance.getValue(-1);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testGetValue_InvalidIndex() {
 try {
 instance.getValue(2);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testGet_ValidFieldType() {
 DurationFieldType fieldType = instance.getFieldType();
 int value = instance.get(fieldType);
 assertEquals(1, value);
 }

 @Test
 public void testGet_InvalidFieldType() {
 try {
 instance.get(new AlwaysNanFieldType());
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testIsSupported_ValidFieldType() {
 DurationFieldType fieldType = instance.getFieldType();
 assertTrue(instance.isSupported(fieldType));
 }

 @Test
 public void testIsSupported_InvalidFieldType() {
 DurationFieldType invalidFieldType = new AlwaysNanFieldType();
 assertFalse(instance.isSupported(invalidFieldType));
 }

 @Test
 public void testToPeriod() {
 Period period = instance.toPeriod();
 assertEquals(instance, period.withFields(instance));
 }

 @Test
 public void testToMutablePeriod() {
 MutablePeriod mutablePeriod = instance.toMutablePeriod();
 mutablePeriod.add(instance);
 assertEquals(instance, mutablePeriod.toPeriod());
 }

 @Test
 public void testEquals() {
 MySingleFieldPeriod instance2 = new MySingleFieldPeriod(1);
 assertTrue(instance.equals(instance2));
 }

 @Test
 public void testEquals_NullObject() {
 assertFalse(instance.equals(null));
 }

 @Test
 public void testEquals_DifferentType() {
 MySingleFieldPeriod instanceMySingleFieldPeriod = new MySingleFieldPeriod(1);
 assertFalse(instance.equals(new DateTime()));
 }

 @Test
 public void testEquals_DifferentValue() {
 MySingleFieldPeriod instance2 = new MySingleFieldPeriod(2);
 assertFalse(instance.equals(instance2));
 }

 @Test
 public void testCompareTo_SameInstance() {
 assertEquals(0, instance.compareTo(instance));
 }

 @Test
 public void testCompareTo_DifferentInstanceSameValue() {
 MySingleFieldPeriod instance2 = new MySingleFieldPeriod(1);
 assertEquals(0, instance.compareTo(instance2));
 }

 @Test
 public void testCompareTo_DifferentInstanceDifferentValue() {
 MySingleFieldPeriod instance2 = new MySingleFieldPeriod(2);
 assertEquals(-1, instance.compareTo(instance2));
 }

 @Test
 public void testCompareTo_DifferentClass() {
 MySingleFieldPeriod instance = new MySingleFieldPeriod(1);
 Object anotherInstance = new AnotherSingleFieldPeriod(1);
 try {
 instance.compareTo(anotherInstance);
 fail();
 } catch (ClassCastException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_NullReadableInstant() {
 BaseSingleFieldPeriod.between(null, new Instant(), DurationFieldType.days());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_NullReadableInstant2() {
 BaseSingleFieldPeriod.between(new Instant(), null, DurationFieldType.days());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_NullDurationFieldType() {
 BaseSingleFieldPeriod.between(new Instant(), new Instant(), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_InvalidReadableInstant() {
 BaseSingleFieldPeriod.between(new Instant("2022-01-01T00:00:00.000Z"), null, DurationFieldType.days());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_InvalidReadableInstant2() {
 BaseSingleFieldPeriod.between(null, new Instant("2022-01-01T00:00:00.000Z"), DurationFieldType.days());
 }

 @Test
 public void testStandardPeriodIn() {
 long n = 1000L;
 assertEquals(1, BaseSingleFieldPeriod.standardPeriodIn(instance, n));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodIn_NullReadablePeriod() {
 BaseSingleFieldPeriod.standardPeriodIn(null, 1000L);
 }
}

 class MySingleFieldPeriod extends BaseSingleFieldPeriod {
 public static final DurationFieldType FIELD_TYPE = new MyFieldType();

 @Override
 public DurationFieldType getFieldType() {
 return FIELD_TYPE;
 }

 @Override
 public PeriodType getPeriodType() {
 return new MyPeriodType();
 }

 static class MyFieldType extends AbstractDurationFieldType {
 @Override
 public String getName() {
 return "MyFieldType";
 }
 }

 static class MyPeriodType extends AbstractPeriodType {
 @Override
 public String getID() {
 return "MyPeriodType";
 }
 }
}