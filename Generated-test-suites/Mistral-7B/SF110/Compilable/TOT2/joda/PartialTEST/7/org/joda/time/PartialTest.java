package org.joda.time;
import org.junit.Test;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

public class PartialTest {

    @Test
    public void testPartialConstruction() {
        DateTimeFieldType dateField = DateTimeFieldType.dayOfMonth();
        int value = 10;
        Partial partial = new Partial(dateField, value);
        // Add assertions here to check the partial's properties
    }
}