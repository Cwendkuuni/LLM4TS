package org.joda.time;
import org.junit.Test;
import static org.junit.Assert.*;

public class PartialTest {

    @Test
    public void testDefaultInitialization() {
        Partial partial = new Partial();
        assertEquals(null, partial.getChronology());
        assertEquals(0, partial.size());
    }
}