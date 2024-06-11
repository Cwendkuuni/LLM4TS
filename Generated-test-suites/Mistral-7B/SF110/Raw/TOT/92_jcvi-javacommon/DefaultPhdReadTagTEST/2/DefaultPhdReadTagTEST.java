
package org.jcvi.jillion.assembly.consed.phd;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {
    @Test
    public void testgetType() {
        String type = "testType";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals("testType", tag.getType());
    }

    @Test
    public void testGetUngappedRange() {
        int start = 1;
        int end = 10;
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(start, end), new Date(), "comment", "freeFormData");
        Range expectedRange = new Range(start, end);
        assertEquals(expectedRange, tag.getUngappedRange());
    }

    @Test
    public void testEquals() {
        // ... equals() test case implementation here ...
    }
}
