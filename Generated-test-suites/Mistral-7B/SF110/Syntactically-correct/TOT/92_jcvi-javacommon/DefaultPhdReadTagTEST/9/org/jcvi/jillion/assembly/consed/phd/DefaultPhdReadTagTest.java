
package org.jcvi.jillion.assembly.consed.phd;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DefaultPhdReadTagTest {
    // Test cases for getType() method
    @Test
    public void testGetTypeMethod() {
        String type = "testType";
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag(type, "testSource", new Range(1, 10), new Date(), "testComment", "testFreeFormData");
        assertEquals(type, phdTag.getType());
    }

    // Test cases for getUngappedRange() method
    @Test
    public void testGetUngappedRangeMethod() {
        Range expectedRange = new Range(1, 10);
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag("testType", "testSource", expectedRange, new Date(), "testComment", "testFreeFormData");
        Range actualRange = phdTag.getUngappedRange();
        assertEquals(expectedRange, actualRange);
    }

    // Test cases for equals() method
    @Test
    public void testEqualsMethod() {
        String type1 = "testType1";
        String type2 = "testType2";
        String source = "testSource";
        Range ungappedRange1 = new Range(1, 10);
        Range ungappedRange2 = new Range(1, 10);
        Date date = new Date();
        String comment1 = "testComment1";
        String comment2 = "testComment2";
        String freeFormData1 = "testFreeFormData1";
        String freeFormData2 = "testFreeFormData2";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source, ungappedRange1, date, comment1, freeFormData1);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type2, source, ungappedRange2, date, comment2, freeFormData2);

        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(type1, source, ungappedRange1, date, comment1, freeFormData1);

        assertTrue(tag1.equals(tag3));
        assertFalse(tag1.equals(tag2));
    }
}
