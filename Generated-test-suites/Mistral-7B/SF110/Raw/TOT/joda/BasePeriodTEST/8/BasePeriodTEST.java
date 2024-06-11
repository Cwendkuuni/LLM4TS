package org.joda.time.base;

import org.joda.time.*;
import org.joda.time.duration.*;
import org.joda.time.field.*;
import org.junit.*;

import static org.junit.Assert.*;

public class BasePeriodTest {

 @BeforeClass
 public static void setUpClass() {
 // Set up any static resources or initialize any class-level variables here
 }

 @AfterClass
 public static void tearDownClass() {
 // Clean up any static resources or variables here
 }

 @Before
 public void setUp() {
 // Set up any test-specific resources or variables here
 }

 @After
 public void tearDown() {
 // Clean up any test-specific resources or variables here
 }

 @Test
 public void testEmptyPeriod() {
 BasePeriod period = new BasePeriod(0L);
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertArrayEquals(new int[] { 0, 0, 0, 0, 0, 0, 0, 0 }, period.iValues);
 }

 @Test
 public void testPeriodFromInstant() {
 ReadableInstant startInstant = new DateTime(2010, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
 BasePeriod period1 = new BasePeriod(startInstant, new DateTime(2010, 1, 1, 1, 0, 0, 0, DateTimeZone.UTC), PeriodType.standard());
 assertEquals(PeriodType.standard(), period1.getPeriodType());
 assertArrayEquals(new int[] { 0, 0, 1, 0, 0, 0, 0, 0 }, period1.iValues);

 ReadableInstant endInstant = new DateTime(2010, 1, 1, 2, 0, 0, 0, DateTimeZone.UTC);
 BasePeriod period2 = new BasePeriod(startInstant, endInstant, PeriodType.standard());
 assertEquals(PeriodType.standard(), period2.getPeriodType());
 assertArrayEquals(new int[] { 0, 0, 1, 0, 0, 0, 0, 1 * 60 * 60 * 1000 }, period2.iValues);
}

 @Test
 public void testPeriodFromReadableDuration() {
 ReadableDuration duration = Duration.standardDays(1);
 BasePeriod period = new BasePeriod(duration, new DateTime(2010, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC), PeriodType.standard());
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertArrayEquals(new int[] { 0, 0, 1, 0, 0, 0, 0, 0 }, period.iValues);
}

 @Test
 public void testPeriodFromReadableInstantAndReadableDuration() {
 ReadableInstant startInstant = new DateTime(2010, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
 ReadableDuration duration = Duration.standardDays(1);
 BasePeriod period = new BasePeriod(startInstant, duration, PeriodType.standard());
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertArrayEquals(new int[] { 0, 0, 1, 0, 0, 0, 0, 1 * 60 * 60 * 1000 }, period.iValues);
}

 @Test
 public void testPeriodFromReadablePartial() {
 BaseLocal startLocal = new LocalDateTime(2010, 1, 1, 0, 0, 0).toLocalDateTime();
 BaseLocal endLocal = new LocalDateTime(2010, 1, 1, 1, 0, 0).toLocalDateTime();
 BasePeriod period = new BasePeriod(startLocal, endLocal, PeriodType.standard());
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertArrayEquals(new int[] { 0, 0, 1, 0, 0, 0, 0, 1 * 60 * 60 * 1000 }, period.iValues);
}

 @Test
 public void testAddingPeriods() {
 BasePeriod period1 = new BasePeriod(0, 1, 2, 3, 4, 5, 6, 7, PeriodType.standard());
 BasePeriod period2 = new BasePeriod(0, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
 BasePeriod sum = new BasePeriod(period1, period2, PeriodType.standard());
 assertEquals(PeriodType.standard(), sum.getPeriodType());
 assertArrayEquals(new int[] { 1, 2, 5, 4, 9, 11, 7, 9 }, sum.iValues);
}

 @Test(expected = IllegalArgumentException.class)
 public void testNullReadablePeriod() {
 BasePeriod period = new BasePeriod(null, new DateTime(2010, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC), PeriodType.standard());
 // Test fails with IllegalArgumentException
}

 @Test(expected = IllegalArgumentException.class)
 public void testReadablePeriodWithNullChronology() {
 BasePeriod period = new BasePeriod(new DateTime(2010, 1, 1, 0, 0, 0, 0, null), null, PeriodType.standard());
 // Test fails with IllegalArgumentException
}

 @Test(expected = IllegalArgumentException.class)
 public void testReadablePeriodWithNonContiguousFields() {
 BasePartial startPartial = new GeneralDatetimeField(1, new DateTimeFieldType[] { DurationFieldType.years(), DurationFieldType.days() }, new int[] { 2010, 1 }, null);
 BasePartial endPartial = new GeneralDatetimeField(1, new DateTimeFieldType[] { DurationFieldType.weeks(), DurationFieldType.days() }, new int[] { 2010, 2 }, null);
 BasePeriod period = new BasePeriod(startPartial, endPartial, PeriodType.standard());
 // Test fails with IllegalArgumentException
}

 @Test(expected = IllegalArgumentException.class)
 public void testReadablePeriodWithDifferentFieldTypes() {
 BasePartial startPartial = new GeneralDatetimeField(1, new DateTimeFieldType[] { DurationFieldType.years(), DurationFieldType.days() }, new int[] { 2010, 1 }, null);
 BasePartial endPartial = new GeneralDatetimeField(1, new DateTimeFieldType[] { DurationFieldType.weeks(), DurationFieldType.hours() }, new int[] { 2010, 2 }, null);
 BasePeriod period = new BasePeriod(startPartial, endPartial, PeriodType.standard());
 // Test fails with IllegalArgumentException
}

 @Test
 public void testToDurationFrom() {
 BasePeriod period = new BasePeriod(0, 1, 2, 3, 4, 5, 6, 7, PeriodType.standard());
 Duration duration = period.toDurationFrom(new DateTime(2010, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC));
 assertEquals(Duration.standardDays(1), duration);
}

 @Test
 public void testToDurationTo() {
 BasePeriod period = new BasePeriod(0, 1, 2, 3, 4, 5, 6, 7, PeriodType.standard());
 Duration duration = period.toDurationTo(new DateTime(2010, 1, 1, 1, 0, 0, 0, DateTimeZone.UTC));
 assertEquals(Duration.standardDays(1), duration);
}

 @AfterClass
 public static void tearDownClass() {
 // Clean up any static resources or variables here
 }
}