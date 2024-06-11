package org.junit.jillion.assembly.consed.phd;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("DNA", new Range(10, 20), new Date("2022-01-01"), "Comment", "FreeFormData");
        assertEquals("DNA", defaultPhdReadTag.getType());
    }

    @Test(expected = NullPointerException.class)
    public void testGetTypeWithNullType() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(null, new Range(10, 20), new Date("2022-01-01"), "Comment", "FreeFormData");
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("DNA", new Range(10, 20), new Date("2022-01-01"), "Comment", "FreeFormData");
        assertEquals("DNA", defaultPhdReadTag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("DNA", new Range(10, 20), new Date("2022-01-01"), "Comment", "FreeFormData");
        assertEquals(new Range(10, 20), defaultPhdReadTag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("DNA", new Range(10, 20), new Date("2022-01-01"), "Comment", "FreeFormData");
        assertEquals(new Date("2022-01-01"), defaultPhdReadTag.getDate());
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("DNA", new Range(10, 20), new Date("2022-01-01"), "Comment", "FreeFormData");
        assertEquals("Comment", defaultPhdReadTag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("DNA", new Range(10, 20), new Date("2022-01-01"), "Comment", "FreeFormData");
        assertEquals("FreeFormData", defaultPhdReadTag.getFreeFormData());
    }

    @Test(expected = NullPointerException.class)
    public void testGetTypeWithNullType() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(null, new Range(10, 20), new Date("2022-01-01"), "Comment", "FreeFormData");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDateWithInvalidFormat() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("DNA", new Range(10, 20), null, "Comment", "FreeFormData");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUngappedRangeWithInvalidStart() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("DNA", null, new Date("2022-01-01"), "Comment", "FreeFormData");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUngappedRangeWithInvalidEnd() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("DNA", new Range(10, 5), new Date("2022-01-01"), "Comment", "FreeFormData");
    }
}