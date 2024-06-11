package org.jcvi.jillion.assembly.consed.phd;

import org.junit.Test;
import org.jcvi.jillion.core.*;
import org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstructor() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals("testType", tag.getType());
        assertEquals("testSource", tag.getSource());
        assertEquals(new Range(1, 10), tag.getUngappedRange());
        assertEquals(new Date("2022-01-01"), tag.getDate());
        assertEquals("testComment", tag.getComment());
        assertEquals("testFreeFormData", tag.getFreeFormData());
    }

    @Test
    public void testGetType() {
        String type = "testType";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, "source", null, null, null, null);

        assertEquals("testType", tag.getType());
    }

    @Test
    public void testEquals() {
        String type1 = "testType1";
        String type2 = "testType2";
        String source1 = "testSource1";
        String source2 = "testSource2";
        Range range1 = new Range(1, 10);
        Range range2 = new Range(1, 10);
        Date date1 = new Date("2022-01-01");
        Date date2 = new Date("2022-01-02");
        String comment1 = "testComment1";
        String comment2 = "testComment2";
        String freeFormData1 = "testFreeFormData1";
        String freeFormData2 = "testFreeFormData2";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type2, source2, range2, date2, comment2, freeFormData2);

        assertFalse(tag1.equals(tag2));

        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);

        assertTrue(tag1.equals(tag3));
    }
}