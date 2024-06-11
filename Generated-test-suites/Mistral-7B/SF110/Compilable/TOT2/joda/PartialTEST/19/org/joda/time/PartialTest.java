package org.joda.time;
import org.junit.Test;
import static org.junit.Assert.*;

public class PartialTest {
    @Test
    public void testEmptyConstructor() {
        Partial emptyPartial = new Partial();
        assertEquals(0, emptyPartial.size());
        assertNull(emptyPartial.getChronology());
    }
}