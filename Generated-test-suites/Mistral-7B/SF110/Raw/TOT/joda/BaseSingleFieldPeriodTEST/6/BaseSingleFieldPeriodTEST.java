package org.joda.time.base;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.*;

import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

 @BeforeClass
 public static void setupClass() {
 // Set up any static variables or initialize any static methods here
 }

 @AfterClass
 public static void tearDownClass() {
 // Clean up any static resources here
 }

 @Before
 public void setup() {
 // Set up any instance variables or initialize any methods here for each test
 }

 @After
 public void tearDown() {
 // Clean up any instance resources here
 }

 @Test
 public void testBetween() {
 ReadableInstant instant1 = new DateTime(2015, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
 ReadableInstant instant2 = new DateTime(2015, 1, 2, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
 BaseSingleFieldPeriod period = new Duration(1, DurationFieldType.DAYS);
 int result = BaseSingleFieldPeriod.between(instant1, instant2, period.getFieldType());
 assertEquals(1, result);

 instant1 = new DateTime(2015, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
 instant2 = new DateTime(2015, 1, 1, 0, 0, 0, 1000, ISOChronology.getInstanceUTC());
 period = new Duration(1, DurationFieldType.MILLISECONDS);
 try {
 result = BaseSingleFieldPeriod.between(instant1, instant2, period.getFieldType());
 fail("Expected IllegalArgumentException for non-contiguous ReadableInstant objects");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testBetween_InvalidArguments() {
 try {
 BaseSingleFieldPeriod.between(null, null, DurationFieldType.DAYS);
 fail("Expected IllegalArgumentException for null ReadableInstant objects");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BaseSingleFieldPeriod.between(new DateTime(2015, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()), null, DurationFieldType.DAYS);
 fail("Expected IllegalArgumentException for null ReadableInstant objects");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testStandardPeriodIn() {
 ReadablePeriod period = new Period(1, DurationFieldType.DAYS, 1, DurationFieldType.MILLISECONDS, 1000);
 long result = BaseSingleFieldPeriod.standardPeriodIn(period, 60 * 60 * 24 * 7 * 4);
 assertEquals(30, result); // 30 days in 4 weeks
 }

 @Test
 public void testStandardPeriodIn_InvalidArguments() {
 try {
 BaseSingleFieldPeriod.standardPeriodIn(null, 1);
 fail("Expected IllegalArgumentException for null ReadablePeriod objects");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BaseSingleFieldPeriod.standardPeriodIn(new Period(1, DurationFieldType.DAYS), -1);
 fail("Expected IllegalArgumentException for negative n value");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}