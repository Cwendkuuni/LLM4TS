package org.jcvi.jillion.assembly.consed.phd;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        String expectedType = "DNA";
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag(expectedType, "source", new Range(1, 10), new Date(2022, 1, 1), "comment", "freeFormData");
        String actualType = phdTag.getType();
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetSource() {
        String expectedSource = "contig123";
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag("type", expectedSource, new Range(1, 10), new Date(2022, 1, 1), "comment", "freeFormData");
        String actualSource = phdTag.getSource();
        assertEquals(expectedSource, actualSource);
    }

    @Test
    public void testGetUngappedRange() {
        int start = 1;
        int end = 10;
        Range expectedRange = new Range(start, end);
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag("type", "source", expectedRange, new Date(2022, 1, 1), "comment", "freeFormData");
        Range actualRange = phdTag.getUngappedRange();
        assertEquals(expectedRange, actualRange);
    }

    @Test
    public void testGetDate() {
        Date expectedDate = new Date(2022, 1, 1);
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), expectedDate, "comment", "freeFormData");
        Date actualDate = phdTag.getDate();
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testGetComment() {
        String expectedComment = "This is a comment";
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(2022, 1, 1), expectedComment, "freeFormData");
        String actualComment = phdTag.getComment();
        assertEquals(expectedComment, actualComment);
    }

    @Test
    public void testGetFreeFormData() {
        String expectedFreeFormData = "free data";
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(2022, 1, 1), "comment", expectedFreeFormData);
        String actualFreeFormData = phdTag.getFreeFormData();
        assertEquals(expectedFreeFormData, actualFreeFormData);
    }

    @Test
    public void testNullValues() {
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag(null, null, null, null, null, null);
        assertNull(phdTag.getType());
        assertNull(phdTag.getSource());
        assertNull(phdTag.getUngappedRange());
        assertNull(phdTag.getDate());
        assertNull(phdTag.getComment());
        assertNull(phdTag.getFreeFormData());
    }

    @Test
    public void testEmptyStrings() {
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag("", "", new Range(1, 10), new Date(2022, 1, 1), "", "");
        assertEquals("", phdTag.getType());
        assertEquals("", phdTag.getSource());
        assertEquals(new Range(1, 10), phdTag.getUngappedRange());
        assertEquals(new Date(2022, 1, 1), phdTag.getDate());
        assertEquals("", phdTag.getComment());
        assertEquals("", phdTag.getFreeFormData());
    }

    @Test
    public void testMinAndMaxValues() {
        int minStart = Integer.MIN_VALUE;
        int maxEnd = Integer.MAX_VALUE;
        Range expectedRange = new Range(minStart, maxEnd);
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag("type", "source", expectedRange, new Date(2022, 1, 1), "comment", "freeFormData");
        Range actualRange = phdTag.getUngappedRange();
        assertEquals(expectedRange, actualRange);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDateFormat() {
        String invalidDate = "Invalid Date";
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new SimpleDateFormat("dd/MM/yyyy").parse(invalidDate), "comment", "freeFormData");
    }

    @Test
    public void testHashCodeAndEquals() {
        DefaultPhdReadTag phdTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2022, 1, 1), "comment1", "freeFormData1");
        DefaultPhdReadTag phdTag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2022, 1, 1), "comment1", "freeFormData1");
        DefaultPhdReadTag phdTag3 = new DefaultPhdReadTag("type2", "source2", new Range(1, 10), new Date(2022, 1, 1), "comment2", "freeFormData2");

        assertEquals(phdTag1.hashCode(), phdTag2.hashCode());
        assertTrue(phdTag1.equals(phdTag2));
        assertFalse(phdTag1.equals(phdTag3));
    }
}