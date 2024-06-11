package org.joda.time.chrono.test;

import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.field.FieldUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2); // February

        // Test get method
        assertEquals(1, field.get(BaseDateTime.UNIX_EPOCH));
        assertEquals(2, field.get(BaseDateTime.UTC.getMillis()));
        assertEquals(3, field.get(BaseDateTime.parse("2010-03-31T23:59:59.999Z").getMillis()));

        // Test add method with positive number
        assertEquals(2 + 1, field.add(BaseDateTime.UNIX_EPOCH, 1).get(field));
        assertEquals(2 + 1, field.add(BaseDateTime.UTC.getMillis(), 1).get(field));
        assertEquals(3 + 1, field.add(BaseDateTime.parse("2010-02-28T23:59:59.999Z").getMillis(), 1).get(field));

        // Test add method with negative number
        assertEquals(2 - 1, field.add(BaseDateTime.UNIX_EPOCH, -1).get(field));
        assertEquals(2 - 1, field.add(BaseDateTime.UTC.getMillis(), -1).get(field));
        assertEquals(3 - 1, field.add(BaseDateTime.parse("2010-03-01T00:00:00.001Z").getMillis(), -1).get(field));

        // Test add method with large number
        try {
            field.add(BaseDateTime.parse("0001-01-01T00:00:00.000Z").getMillis(), 10000);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test set method
        long set = BaseDateTime.parse("2010-03-01T00:00:00.001Z").getMillis();
        field.set(set, 5);
        assertEquals(5, field.get(set));

        // Test isLeap method
        assertEquals(false, field.isLeap(BaseDateTime.parse("2010-03-01T00:00:00.001Z").getMillis()));
        assertEquals(true, field.isLeap(BaseDateTime.parse("2012-02-29T00:00:00.001Z").getMillis()));

        // Test getLeapAmount method
        assertEquals(0, field.getLeapAmount(BaseDateTime.parse("2010-03-01T00:00:00.001Z").getMillis()));
        assertEquals(1, field.getLeapAmount(BaseDateTime.parse("2012-02-29T00:00:00.001Z").getMillis()));

        // Test getMaximumValue method
        assertEquals(12, field.getMaximumValue());

        // Test getMinimumValue method
        assertEquals(1, field.getMinimumValue());
    }
}