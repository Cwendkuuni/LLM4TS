package org.joda.time.base;

import org.junit.Test;
import org.joda.time.BaseDuration;
import org.joda.time.BaseLocal;
import org.joda.time.BasePeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.PeriodType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class BasePeriodTest {

    @Test
    public void testEmptyConstruction() {
        BasePeriod period = new BasePeriod();
        assertNotNull(period);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(0, period.getValue(DurationFieldType.years()));
        assertEquals(0, period.getValue(DurationFieldType.months()));
        assertEquals(0, period.getValue(DurationFieldType.weeks()));
        assertEquals(0, period.getValue(DurationFieldType.days()));
        assertEquals(0, period.getValue(DurationFieldType.hours()));
        assertEquals(0, period.getValue(DurationFieldType.minutes()));
        assertEquals(0, period.getValue(DurationFieldType.seconds()));
        assertEquals(0, period.getValue(DurationFieldType.millis()));
    }

    @Test
    public void testConstructionWithValues() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.getValue(DurationFieldType.years()));
        assertEquals(2, period.getValue(DurationFieldType.months()));
        assertEquals(3, period.getValue(DurationFieldType.weeks()));
        assertEquals(4, period.getValue(DurationFieldType.days()));
        assertEquals(5, period.getValue(DurationFieldType.hours()));
        assertEquals(6, period.getValue(DurationFieldType.minutes()));
        assertEquals(7, period.getValue(DurationFieldType.seconds()));
        assertEquals(8, period.getValue(DurationFieldType.millis()));
    }

    @Test
    public void testConstructionFromInstantToInstant() {
        Instant start = Instant.now();
        Instant end = Instant.now().plus(Duration.standardDays(1));
        BasePeriod period = new BasePeriod(start, end, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.getValue(DurationFieldType.years()));
        assertEquals(0, period.getValue(DurationFieldType.months()));
        assertEquals(0, period.getValue(DurationFieldType.weeks()));
        assertEquals(1, period.getValue(DurationFieldType.days()));
        assertEquals(0, period.getValue(DurationFieldType.hours()));
        assertEquals(0, period.getValue(DurationFieldType.minutes()));
        assertEquals(0, period.getValue(DurationFieldType.seconds()));
        assertEquals(0, period.getValue(DurationFieldType.millis()));
    }

    @Test
    public void testConstructionFromReadableInstantToReadableInstant() {
        Instant start = Instant.now();
        Instant end = Instant.now().plus(Duration.standardDays(1));
        BasePeriod period = new BasePeriod(new ReadableInstant() {
            @Override
            public long getMillis() {
                return start.getMillis();
            }
        }, new ReadableInstant() {
            @Override
            public long getMillis() {
                return end.getMillis();
            }
        }, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.getValue(DurationFieldType.years()));
        assertEquals(0, period.getValue(DurationFieldType.months()));
        assertEquals(0, period.getValue(DurationFieldType.weeks()));
        assertEquals(1, period.getValue(DurationFieldType.days()));
        assertEquals(0, period.getValue(DurationFieldType.hours()));
        assertEquals(0, period.getValue(DurationFieldType.minutes()));
        assertEquals(0, period.getValue(DurationFieldType.seconds()));
        assertEquals(0, period.getValue(DurationFieldType.millis()));
    }

    @Test
    public void testConstructionFromReadablePartialToReadablePartial() {
        LocalDateTime dateTime1 = new LocalDateTime();
        LocalDateTime dateTime2 = dateTime1.plusDays(1);
        BasePeriod period = new BasePeriod(new ReadablePartial() {
            @Override
            public ReadableInstant getStartInstant() {
                return new ReadableInstant() {
                    @Override
                    public long getMillis() {
                        return dateTime1.toInstant().getMillis();
                    }
                };
            }

            @Override
            public ReadablePartial getEndPartial() {
                return new ReadablePartial() {
                    @Override
                    public ReadableInstant getStartInstant() {
                        return new ReadableInstant() {
                            @Override
                            public long getMillis() {
                                return dateTime2.toInstant().getMillis();
                            }
                        };
                    }

                    @Override
                    public int getFieldValue(DurationFieldType durationFieldType) {
                        return 0;
                    }

                    @Override
                    public int size() {
                        return 0;
                    }

                    @Override
                    public DurationFieldType getFieldType(int i) {
                        return null;
                    }
                };
            }
        }, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.getValue(DurationFieldType.years()));
        assertEquals(0, period.getValue(DurationFieldType.months()));
        assertEquals(0, period.getValue(DurationFieldType.weeks()));
        assertEquals(1, period.getValue(DurationFieldType.days()));
        assertEquals(0, period.getValue(DurationFieldType.hours()));
        assertEquals(0, period.getValue(DurationFieldType.minutes()));
        assertEquals(0, period.getValue(DurationFieldType.seconds()));
        assertEquals(0, period.getValue(DurationFieldType.millis()));
    }

    @Test
    public void testConstructionFromReadableInstantAndDuration() {
        Instant start = Instant.now();
        Duration duration = Duration.standardDays(1);
        BasePeriod period = new BasePeriod(start, duration, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.getValue(DurationFieldType.years()));
        assertEquals(0, period.getValue(DurationFieldType.months()));
        assertEquals(0, period.getValue(DurationFieldType.weeks()));
        assertEquals(1, period.getValue(DurationFieldType.days()));
        assertEquals(0, period.getValue(DurationFieldType.hours()));
        assertEquals(0, period.getValue(DurationFieldType.minutes()));
        assertEquals(0, period.getValue(DurationFieldType.seconds()));
        assertEquals(0, period.getValue(DurationFieldType.millis()));
    }

    @Test
    public void testConstructionFromReadableDurationAndReadableInstant() {
        Instant instant = Instant.now();
        Duration duration = Duration.standardDays(1);
        BasePeriod period = new BasePeriod(duration, instant, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.getValue(DurationFieldType.years()));
        assertEquals(0, period.getValue(DurationFieldType.months()));
        assertEquals(0, period.getValue(DurationFieldType.weeks()));
        assertEquals(1, period.getValue(DurationFieldType.days()));
        assertEquals(0, period.getValue(DurationFieldType.hours()));
        assertEquals(0, period.getValue(DurationFieldType.minutes()));
        assertEquals(0, period.getValue(DurationFieldType.seconds()));
        assertEquals(0, period.getValue(DurationFieldType.millis()));
    }

    @Test
    public void testConstructionWithNullArguments() {
        try {
            new BasePeriod(null, null, PeriodType.standard());
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        try {
            new BasePeriod(new ReadablePartial() {
                @Override
                public ReadableInstant getStartInstant() {
                    return null;
                }

                @Override
                public ReadablePartial getEndPartial() {
                    return null;
                }
            }, PeriodType.standard());
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetPeriodType() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testGetValue() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        assertEquals(1, period.getValue(DurationFieldType.years()));
        assertEquals(2, period.getValue(DurationFieldType.months()));
        assertEquals(3, period.getValue(DurationFieldType.weeks()));
        assertEquals(4, period.getValue(DurationFieldType.days()));
        assertEquals(5, period.getValue(DurationFieldType.hours()));
        assertEquals(6, period.getValue(DurationFieldType.minutes()));
        assertEquals(7, period.getValue(DurationFieldType.seconds()));
        assertEquals(8, period.getValue(DurationFieldType.millis()));
    }

    @Test
    public void testToDurationFrom() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Instant instant = new Instant(1516044400000L);
        Duration duration = period.toDurationFrom(instant);
        assertEquals(Duration.standardDays(1).plus(Duration.standardHours(2)).plus(Duration.standardMinutes(3)).plus(Duration.standardSeconds(4)).plus(Duration.standardMillis(5)), duration);
    }

    @Test
    public void testToDurationTo() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        Instant instant = new Instant(1516044400000L);
        Duration duration = period.toDurationTo(instant);
        assertEquals(Duration.standardDays(1).minus(Duration.standardHours(2)).minus(Duration.standardMinutes(3)).minus(Duration.standardSeconds(4)).minus(Duration.standardMillis(5)), duration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckAndUpdateWithNullDurationFieldType() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.checkAndUpdate(null, new int[8], 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckAndUpdateWithNegativeValue() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.checkAndUpdate(DurationFieldType.years(), new int[8], -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPeriodWithNullReadablePeriod() {
        BasePeriod period = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period.setPeriod(null);
    }

    @Test
    public void testSetPeriodWithValidReadablePeriod() {
        BasePeriod period1 = new BasePeriod(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        BasePeriod period2 = new BasePeriod(2, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard());
        period1.setPeriod(period2);
        assertEquals(2, period1.getValue(DurationFieldType.years()));
        assertEquals(2, period1.getValue(DurationFieldType.months()));
        assertEquals(3, period1.getValue(DurationFieldType.weeks()));
        assertEquals(4, period1.getValue(DurationFieldType.days()));
        assertEquals(5, period1.getValue(DurationFieldType.hours()));
        assertEquals(6, period1.getValue(DurationFieldType.minutes()));
        assertEquals(7, period1.getValue(DurationFieldType.seconds()));
        assertEquals(8, period1.getValue(DurationFieldType.millis()));
    }
}