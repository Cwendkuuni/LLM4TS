package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.joda.time.BaseLongField;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.test.BaseTestCase;

/**
 * Test cases for {@link BaseSingleFieldPeriod}.
 */
public class BaseSingleFieldPeriodTest extends BaseTestCase {

    /** Test single field period representing 1 hour. */
    private static final BaseSingleFieldPeriod ONE_HOUR = new Hours(1);

    /** Test single field period representing 1 day. */
    private static final BaseSingleFieldPeriod ONE_DAY = new Days(1);

    /** Test single field period representing 1 week. */
    private static final BaseSingleFieldPeriod ONE_WEEK = new Weeks(1);

    /** Test single field period representing 1 month. */
    private static final BaseSingleFieldPeriod ONE_MONTH = new Months(1);

    /** Test single field period representing 1 year. */
    private static final BaseSingleFieldPeriod ONE_YEAR = new Years(1);

    /**
     * Test that between method calculates correct number of hours between two instants.
     */
    @Test
    public void testBetweenInstants() {
        long startMillis = DateTimeConstants.MILLIS_IN_DAY - 1000L; // 24 hours ago
        long endMillis = DateTimeConstants.MILLIS_IN_DAY; // Midnight today
        int hours = BaseSingleFieldPeriod.between(DateTimeUtils.instant(startMillis), DateTimeUtils.instant(endMillis), DurationFieldType.HOURS);
        assertEquals(24, hours);
    }

    /**
     * Test that between method calculates correct number of hours between two partials.
     */
    @Test
    public void testBetweenPartials() {
        LocalDate start = LocalDate.parse("2015-10-01");
        LocalDate end = LocalDate.parse("2015-10-02");
        int days = BaseSingleFieldPeriod.between(start, end, Period.DAYS);
        assertEquals(1, days);

        start = LocalDate.parse("2015-10-01");
        end = LocalDate.parse("2015-10-02").plus(Period.DAYS.withFieldsAdded(new Months(1), new Days(22)));
        days = BaseSingleFieldPeriod.between(start, end, Period.DAYS);
        assertEquals(23, days);
    }

    /**
     * Test that standardPeriodIn method converts correct number of hours from a Period.
     */
    @Test
    public void testStandardPeriodIn() {
        Period period = new Period(DateTimeConstants.MILLIS_IN_DAY, PeriodType.standard());
        int hours = BaseSingleFieldPeriod.standardPeriodIn(period, TimeUnit.MILLISECONDS.toHours(1));
        assertEquals(24, hours);

        period = new Period(DateTimeConstants.MILLIS_IN_DAY, PeriodType.standard()).withHours(-10);
        hours = BaseSingleFieldPeriod.standardPeriodIn(period, TimeUnit.MILLISECONDS.toHours(1));
        assertEquals(14, hours);
    }

    /**
     * Test that equals method returns true for equal periods.
     */
    @Test
    public void testEquals() {
        assertEquals(ONE_DAY, ONE_DAY);
        assertEquals(ONE_HOUR, ONE_HOUR);
        assertEquals(ONE_MONTH, ONE_MONTH);
        assertEquals(ONE_YEAR, ONE_YEAR);

        assertEquals(new Hours(1), new Hours(1));
        assertEquals(new Days(1), new Days(1));
        assertEquals(new Weeks(1), new Weeks(1));
        assertEquals(new Months(1), new Months(1));
        assertEquals(new Years(1), new Years(1));
    }

    /**
     * Test that equals method returns false for different periods.
     */
    @Test
    public void testNotEquals() {
        assertNotEquals(ONE_DAY, ONE_HOUR);
        assertNotEquals(ONE_DAY, ONE_MONTH);
        assertNotEquals(ONE_DAY, ONE_YEAR);
        assertNotEquals(ONE_HOUR, ONE_MONTH);
        assertNotEquals(ONE_HOUR, ONE_YEAR);
        assertNotEquals(ONE_MONTH, ONE_YEAR);

        assertNotEquals(new Hours(1), new Days(1));
        assertNotEquals(new Hours(1), new Weeks(1));
        assertNotEquals(new Hours(1), new Months(1));
        assertNotEquals(new Hours(1), new Years(1));

        assertNotEquals(new Days(1), new Weeks(1));
        assertNotEquals(new Days(1), new Months(1));
        assertNotEquals(new Days(1), new Years(1));

        assertNotEquals(new Weeks(1), new Months(1));
        assertNotEquals(new Weeks(1), new Years(1));

        assertNotEquals(new Months(1), new Years(1));
    }

    /**
     * Test that hashCode method returns same value for equal periods.
     */
    @Test
    public void testHashCode() {
        int hashCodeOneDay = ONE_DAY.hashCode();
        int hashCodeOneHour = ONE_HOUR.hashCode();
        int hashCodeOneMonth = ONE_MONTH.hashCode();
        int hashCodeOneYear = ONE_YEAR.hashCode();

        int hashCodeHours = new Hours(1).hashCode();
        int hashCodeDays = new Days(1).hashCode();
        int hashCodeWeeks = new Weeks(1).hashCode();
        int hashCodeMonths = new Months(1).hashCode();
        int hashCodeYears = new Years(1).hashCode();

        assertEquals(hashCodeOneDay, hashCodeDays);
        assertEquals(hashCodeOneHour, hashCodeHours);
        assertEquals(hashCodeOneMonth, hashCodeMonths);
        assertEquals(hashCodeOneYear, hashCodeYears);

        assertEquals(hashCodeHours, hashCodeHours);
        assertEquals(hashCodeDays, hashCodeDays);
        assertEquals(hashCodeWeeks, hashCodeWeeks);
        assertEquals(hashCodeMonths, hashCodeMonths);
        assertEquals(hashCodeYears, hashCodeYears);
    }

    /**
     * Test that compareTo method returns correct ordering for periods.
     */
    @Test
    public void testCompareTo() {
        int comparisonResult = ONE_DAY.compareTo(ONE_HOUR);
        assertEquals(1, comparisonResult);

        comparisonResult = ONE_HOUR.compareTo(ONE_DAY);
        assertEquals(-1, comparisonResult);

        comparisonResult = ONE_DAY.compareTo(ONE_DAY);
        assertEquals(0, comparisonResult);

        comparisonResult = ONE_MONTH.compareTo(ONE_YEAR);
        assertEquals(-1, comparisonResult);

        comparisonResult = ONE_YEAR.compareTo(ONE_MONTH);
        assertEquals(1, comparisonResult);

        comparisonResult = new Hours(1).compareTo(new Hours(2));
        assertEquals(-1, comparisonResult);

        comparisonResult = new Hours(1).compareTo(new Hours(1));
        assertEquals(0, comparisonResult);

        comparisonResult = new Days(1).compareTo(new Days(2));
        assertEquals(-1, comparisonResult);

        comparisonResult = new Days(1).compareTo(new Days(1));
        assertEquals(0, comparisonResult);

        comparisonResult = new Weeks(1).compareTo(new Weeks(2));
        assertEquals(-1, comparisonResult);

        comparisonResult = new Weeks(1).compareTo(new Weeks(1));
        assertEquals(0, comparisonResult);

        comparisonResult = new Months(1).compareTo(new Months(2));
        assertEquals(-1, comparisonResult);

        comparisonResult = new Months(1).compareTo(new Months(1));
        assertEquals(0, comparisonResult);

        comparisonResult = new Years(1).compareTo(new Years(2));
        assertEquals(-1, comparisonResult);

        comparisonResult = new Years(1).compareTo(new Years(1));
        assertEquals(0, comparisonResult);
    }

    /**
     * Test that method toPeriod returns correct Period instance.
     */
    @Test
    public void testToPeriod() {
        Period periodOneHour = ONE_HOUR.toPeriod();
        assertEquals(1, periodOneHour.getHours());
        assertEquals(0, periodOneHour.getMinutes());
        assertEquals(0, periodOneHour.getSeconds());
        assertEquals(0, periodOneHour.getMillis());

        Period periodOneDay = ONE_DAY.toPeriod();
        assertEquals(0, periodOneDay.getHours());
        assertEquals(0, periodOneDay.getMinutes());
        assertEquals(0, periodOneDay.getSeconds());
        assertEquals(DateTimeConstants.MILLIS_IN_DAY, periodOneDay.getMillis());

        Period periodOneWeek = ONE_WEEK.toPeriod();
        assertEquals(0, periodOneWeek.getHours());
        assertEquals(0, periodOneWeek.getMinutes());
        assertEquals(0, periodOneWeek.getSeconds());
        assertEquals(DateTimeConstants.MILLIS_IN_WEEK, periodOneWeek.getMillis());

        Period periodOneMonth = ONE_MONTH.toPeriod();
        assertEquals(0, periodOneMonth.getHours());
        assertEquals(0, periodOneMonth.getMinutes());
        assertEquals(0, periodOneMonth.getSeconds());
        assertEquals(DateTimeConstants.MILLIS_IN_MONTH, periodOneMonth.getMillis());

        Period periodOneYear = ONE_YEAR.toPeriod();
        assertEquals(0, periodOneYear.getHours());
        assertEquals(0, periodOneYear.getMinutes());
        assertEquals(0, periodOneYear.getSeconds());
        assertEquals(DateTimeConstants.MILLIS_IN_YEAR, periodOneYear.getMillis());
    }

    /**
     * Test that method toMutablePeriod returns correct MutablePeriod instance.
     */
    @Test
    public void testToMutablePeriod() {
        MutablePeriod mutablePeriodOneHour = ONE_HOUR.toMutablePeriod();
        mutablePeriodOneHour.add(ONE_HOUR);
        assertEquals(1, mutablePeriodOneHour.getHours());
        assertEquals(0, mutablePeriodOneHour.getMinutes());
        assertEquals(0, mutablePeriodOneHour.getSeconds());
        assertEquals(0, mutablePeriodOneHour.getMillis());

        mutablePeriodOneHour.setHours(2);
        assertEquals(2, mutablePeriodOneHour.getHours());
        assertEquals(0, mutablePeriodOneHour.getMinutes());
        assertEquals(0, mutablePeriodOneHour.getSeconds());
        assertEquals(0, mutablePeriodOneHour.getMillis());

        MutablePeriod mutablePeriodOneDay = ONE_DAY.toMutablePeriod();
        mutablePeriodOneDay.add(ONE_DAY);
        assertEquals(1, mutablePeriodOneDay.getHours());
        assertEquals(0, mutablePeriodOneDay.getMinutes());
        assertEquals(0, mutablePeriodOneDay.getSeconds());
        assertEquals(DateTimeConstants.MILLIS_IN_DAY, mutablePeriodOneDay.getMillis());

        mutablePeriodOneDay.setHours(24);
        assertEquals(24, mutablePeriodOneDay.getHours());
        assertEquals(0, mutablePeriodOneDay.getMinutes());
        assertEquals(0, mutablePeriodOneDay.getSeconds());
        assertEquals(0, mutablePeriodOneDay.getMillis());

        MutablePeriod mutablePeriodOneWeek = ONE_WEEK.toMutablePeriod();
        mutablePeriodOneWeek.add(ONE_WEEK);
        assertEquals(1, mutablePeriodOneWeek.getHours());
        assertEquals(0, mutablePeriodOneWeek.getMinutes());
        assertEquals(0, mutablePeriodOneWeek.getSeconds());
        assertEquals(DateTimeConstants.MILLIS_IN_WEEK, mutablePeriodOneWeek.getMillis());

        mutablePeriodOneWeek.setHours(168);
        assertEquals(168, mutablePeriodOneWeek.getHours());
        assertEquals(0, mutablePeriodOneWeek.getMinutes());
        assertEquals(0, mutablePeriodOneWeek.getSeconds());
        assertEquals(0, mutablePeriodOneWeek.getMillis());

        MutablePeriod mutablePeriodOneMonth = ONE_MONTH.toMutablePeriod();
        mutablePeriodOneMonth.add(ONE_MONTH);
        assertEquals(1, mutablePeriodOneMonth.getHours());
        assertEquals(0, mutablePeriodOneMonth.getMinutes());
        assertEquals(0, mutablePeriodOneMonth.getSeconds());
        assertEquals(DateTimeConstants.MILLIS_IN_MONTH, mutablePeriodOneMonth.getMillis());

        mutablePeriodOneMonth.setHours(30 * 24);
        assertEquals(30 * 24, mutablePeriodOneMonth.getHours());
        assertEquals(0, mutablePeriodOneMonth.getMinutes());
        assertEquals(0, mutablePeriodOneMonth.getSeconds());
        assertEquals(0, mutablePeriodOneMonth.getMillis());

        MutablePeriod mutablePeriodOneYear = ONE_YEAR.toMutablePeriod();
        mutablePeriodOneYear.add(ONE_YEAR);
        assertEquals(1, mutablePeriodOneYear.getHours());
        assertEquals(0, mutablePeriodOneYear.getMinutes());
        assertEquals(0, mutablePeriodOneYear.getSeconds());
        assertEquals(DateTimeConstants.MILLIS_IN_YEAR, mutablePeriodOneYear.getMillis());

        mutablePeriodOneYear.setHours(365 * 24);
        assertEquals(365 * 24, mutablePeriodOneYear.getHours());
        assertEquals(0, mutablePeriodOneYear.getMinutes());
        assertEquals(0, mutablePeriodOneYear.getSeconds());
        assertEquals(0, mutablePeriodOneYear.getMillis());
    }
}