package org.joda.time.base;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

 @Test
 public void testBetweenMethodWithValidArguments() {
 ReadableInstant instant1 = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
 ReadableInstant instant2 = new DateTime(2022, 1, 2, 0, 0, 0, 0, DateTimeZone.UTC);
 DurationFieldType durationFieldType = DurationFieldType.days();
 int result = BaseSingleFieldPeriod.between(instant1, instant2, durationFieldType);
 assertEquals(1, result);
 }

 @Test
 public void testBetweenMethodWithNullArguments() {
 try {
 BaseSingleFieldPeriod.between(null, instant2, durationFieldType);
 fail("IllegalArgumentException should be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BaseSingleFieldPeriod.between(instant1, null, durationFieldType);
 fail("IllegalArgumentException should be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testBetweenMethodWithDifferentChronologies() {
 ReadableInstant instant1 = new DateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
 ReadableInstant instant2 = new DateTime(2022, 1, 2, 0, 0, 0, 0, DateTimeZone.BASIC);
 DurationFieldType durationFieldType = DurationFieldType.days();
 try {
 BaseSingleFieldPeriod.between(instant1, instant2, durationFieldType);
 fail("IllegalArgumentException should be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testBetweenMethodWithDifferentFieldTypes() {
 ReadablePartial readablePartial1 = new GeneralDate(2022, 1, 1);
 ReadablePartial readablePartial2 = new GeneralDate(2022, 1, 2);
 ReadablePeriod readablePeriod = Period.days(1);
 try {
 BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, readablePeriod);
 fail("IllegalArgumentException should be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testBetweenMethodWithNonContiguousReadablePartials() {
 ReadablePartial readablePartial1 = new GeneralDate(2022, 1, 1);
 ReadablePartial readablePartial2 = new GeneralDate(2022, 1, 3);
 ReadablePeriod readablePeriod = Period.days(1);
 try {
 BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, readablePeriod);
 fail("IllegalArgumentException should be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testStandardPeriodInMethodWithValidArguments() {
 ReadablePeriod readablePeriod = Period.days(2);
 long n = 2;
 int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
 assertEquals(1, result);
 }

 @Test
 public void testStandardPeriodInMethodWithNullArgument() {
 try {
 BaseSingleFieldPeriod.standardPeriodIn(null, n);
 fail("IllegalArgumentException should be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testStandardPeriodInMethodWithPreciselessField() {
 ReadablePeriod readablePeriod = new Period(1, DurationFieldType.days().getField(DateTimeUtils.getISOChronology()), 2);
 try {
 BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
 fail("IllegalArgumentException should be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}