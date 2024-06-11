package org.jcvi.jillion.assembly.consed.phd;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.core.*;
import java.util.Date;

public class DefaultPhdReadTagTest {
    @Test
    public void testGetType() {
        String type = "TestType";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals(type, tag.getType());
    }

    @Test
    public void testGetUngappedRange() {
        int start = 0;
        int end = 10;
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(start, end), new Date(), "comment", "freeFormData");
        Range expectedRange = new Range(start, end);
        assertEquals(expectedRange, tag.getUngappedRange());
    }

    @Test
    public void testEquals() {
        String type1 = "TestType1";
        String type2 = "TestType2";
        String source = "TestSource";
        Date date = new Date();
        String comment = "TestComment";
        String freeFormData = "TestFreeFormData";
        Range range1 = new Range(0, 10);
        Range range2 = new Range(0, 10);

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source, range1, date, comment, freeFormData);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type2, source, range2, date, comment, freeFormData);

        assertTrue(tag1.equals(tag1));
        assertFalse(tag1.equals(tag2));

        tag2.setType(type1);
        assertTrue(tag1.equals(tag2));

        tag2.setSource("TestSource2");
        assertFalse(tag1.equals(tag2));

        tag2.setUngappedRange(new Range(1, 11));
        assertFalse(tag1.equals(tag2));

        tag2.setDate(new Date(System.currentTimeMillis() - 1000));
        assertFalse(tag1.equals(tag2));

        tag2.setComment("TestComment2");
        assertFalse(tag1.equals(tag2));

        tag2.setFreeFormData("TestFreeFormData2");
        assertFalse(tag1.equals(tag2));

        tag2.setUngappedRange(range1);
        assertTrue(tag1.equals(tag2));
    }
}