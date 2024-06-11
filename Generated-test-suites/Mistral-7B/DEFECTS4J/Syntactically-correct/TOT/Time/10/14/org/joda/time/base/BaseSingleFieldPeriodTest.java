import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.base.BaseSingleFieldPeriodTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testDaysPeriod() {
        BaseSingleFieldPeriod daysPeriod = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        // Test equals method
        BaseSingleFieldPeriod daysPeriodEqual = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertTrue(daysPeriod.equals(daysPeriodEqual));
        assertEquals(daysPeriod.hashCode(), daysPeriodEqual.hashCode());

        // Test not equals method
        BaseSingleFieldPeriod daysPeriodDifferentType = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertFalse(daysPeriod.equals(daysPeriodDifferentType));
        assertNotSame(daysPeriod.hashCode(), daysPeriodDifferentType.hashCode());

        // Test zero period
        BaseSingleFieldPeriod daysPeriodZero = new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertSame(Period.ZERO, daysPeriodZero.toPeriod());

        // Test adding periods
        BaseSingleFieldPeriod daysPeriodAdd = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod daysPeriodAddTwo = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod daysPeriodSum = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(daysPeriodAdd.add(daysPeriodAddTwo), daysPeriodSum);

        // Test subtracting periods
        BaseSingleFieldPeriod daysPeriodSubtract = new BaseSingleFieldPeriod(-1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(daysPeriodAdd, daysPeriodSum.subtract(daysPeriodSubtract));

        // Test values
        assertEquals(1, daysPeriod.getValue());
        assertEquals(DurationFieldType.DAYS, daysPeriod.getFieldType());
        assertEquals(PeriodType.standard(), daysPeriod.getPeriodType());

        // Test get method
        assertEquals(1, daysPeriod.get(DurationFieldType.DAYS));

        // Test isSupported method
        assertTrue(daysPeriod.isSupported(DurationFieldType.DAYS));
        assertFalse(daysPeriod.isSupported(DurationFieldType.HOURS));

        // Test toPeriod method
        Period daysPeriodToPeriod = daysPeriod.toPeriod();
        assertEquals(1, daysPeriodToPeriod.getDays());

        // Test toMutablePeriod method
        MutablePeriod daysPeriodToMutablePeriod = daysPeriod.toMutablePeriod();
        assertEquals(1, daysPeriodToMutablePeriod.getDays());
    }

    @Test
    public void testHoursPeriod() {
        BaseSingleFieldPeriod hoursPeriod = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        // Test equals method
        BaseSingleFieldPeriod hoursPeriodEqual = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertTrue(hoursPeriod.equals(hoursPeriodEqual));
        assertEquals(hoursPeriod.hashCode(), hoursPeriodEqual.hashCode());

        // Test not equals method
        BaseSingleFieldPeriod hoursPeriodDifferentType = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertFalse(hoursPeriod.equals(hoursPeriodDifferentType));
        assertNotSame(hoursPeriod.hashCode(), hoursPeriodDifferentType.hashCode());

        // Test zero period
        BaseSingleFieldPeriod hoursPeriodZero = new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertSame(Period.ZERO, hoursPeriodZero.toPeriod());

        // Test adding periods
        BaseSingleFieldPeriod hoursPeriodAdd = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod hoursPeriodAddTwo = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod hoursPeriodSum = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(hoursPeriodAdd.add(hoursPeriodAddTwo), hoursPeriodSum);

        // Test subtracting periods
        BaseSingleFieldPeriod hoursPeriodSubtract = new BaseSingleFieldPeriod(-1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(hoursPeriodAdd, hoursPeriodSum.subtract(hoursPeriodSubtract));

        // Test values
        assertEquals(1, hoursPeriod.getValue());
        assertEquals(DurationFieldType.HOURS, hoursPeriod.getFieldType());
        assertEquals(PeriodType.standard(), hoursPeriod.getPeriodType());

        // Test get method
        assertEquals(1, hoursPeriod.get(DurationFieldType.HOURS));

        // Test isSupported method
        assertTrue(hoursPeriod.isSupported(DurationFieldType.HOURS));
        assertFalse(hoursPeriod.isSupported(DurationFieldType.DAYS));

        // Test toPeriod method
        Period hoursPeriodToPeriod = hoursPeriod.toPeriod();
        assertEquals(1, hoursPeriodToPeriod.getHours());

        // Test toMutablePeriod method
        MutablePeriod hoursPeriodToMutablePeriod = hoursPeriod.toMutablePeriod();
        assertEquals(1, hoursPeriodToMutablePeriod.getHours());
    }

    @Test
    public void testInvalidPeriod() {
        // Test null constructor
        try {
            new BaseSingleFieldPeriod(null) {
                @Override
                public DurationFieldType getFieldType() {
                    return DurationFieldType.DAYS;
                }

                @Override
                public PeriodType getPeriodType() {
                    return PeriodType.standard();
                }
            };
            fail("Should not be able to create a period with a null value");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test negative constructor
        try {
            new BaseSingleFieldPeriod(-1) {
                @Override
                public DurationFieldType getFieldType() {
                    return DurationFieldType.DAYS;
                }

                @Override
                public PeriodType getPeriodType() {
                    return PeriodType.standard();
                }
            };
            fail("Should not be able to create a period with a negative value");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test adding a larger period than supported
        BaseSingleFieldPeriod daysPeriod = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod hoursPeriod = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        try {
            assertEquals(hoursPeriod.add(daysPeriod), hoursPeriod);
            fail("Should not be able to add a larger period than supported");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testChronologyConversion() {
        BaseSingleFieldPeriod daysPeriodUTC = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod daysPeriodISO = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.iso();
            }
        };

        Chronology utc = DateTimeUtils.getInstantChronology(0L);
        Chronology iso = ISOChronology.getInstanceUTC();

        long daysUTC = daysPeriodUTC.toDuration(utc).getMillis();
        long daysISO = daysPeriodISO.toDuration(iso).getMillis();

        assertEquals(daysUTC, daysISO + DateTimeUtils.MILLIS_PER_DAY);
    }
}