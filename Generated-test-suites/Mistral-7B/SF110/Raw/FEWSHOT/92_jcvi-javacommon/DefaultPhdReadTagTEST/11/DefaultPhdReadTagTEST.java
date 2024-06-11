package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagCreation() {
        String type = "DNA";
        String source = "contig1";
        Range ungappedRange = new Range(1, 50);
        Date date = new Date("2022-01-01");
        String comment = "This is a comment";
        String freeFormData = "ABC123";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, tag.getType());
        assertEquals(source, tag.getSource());
        assertEquals(ungappedRange, tag.getUngappedRange());
        assertEquals(date, tag.getDate());
        assertEquals(comment, tag.getComment());
        assertEquals(freeFormData, tag.getFreeFormData());
    }

    @Test
    public void testGetHashCode() {
        String type1 = "DNA";
        String source1 = "contig1";
        Range ungappedRange1 = new Range(1, 50);
        Date date1 = new Date("2022-01-01");
        String comment1 = "This is a comment";
        String freeFormData1 = "ABC123";

        String type2 = "RNA";
        String source2 = "contig2";
        Range ungappedRange2 = new Range(51, 100);
        Date date2 = new Date("2022-01-02");
        String comment2 = "Another comment";
        String freeFormData2 = "XYZ456";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        String type1 = "DNA";
        String source1 = "contig1";
        Range ungappedRange1 = new Range(1, 50);
        Date date1 = new Date("2022-01-01");
        String comment1 = "This is a comment";
        String freeFormData1 = "ABC123";

        String type2 = "DNA";
        String source2 = "contig1";
        Range ungappedRange2 = new Range(1, 50);
        Date date2 = new Date("2022-01-01");
        String comment2 = "This is a comment";
        String freeFormData2 = "ABC123";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        assertTrue(tag1.equals(tag3));
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testNullObjects() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(null, null, null, null, null, null);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("DNA", null, new Range(1, 50), null, null, "ABC123");

        assertNull(tag1.getType());
        assertNull(tag1.getSource());
        assertNull(tag1.getUngappedRange());
        assertNull(tag1.getDate());
        assertNull(tag1.getComment());
        assertNull(tag1.getFreeFormData());

        assertNotNull(tag2.getType());
        assertNull(tag2.getSource());
        assertNotNull(tag2.getUngappedRange());
        assertNull(tag2.getDate());
        assertNull(tag2.getComment());
        assertNotNull(tag2.getFreeFormData());

        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEmptyObjects() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("", "", null, null, "", "");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("", "", null, null, "", "");

        assertEquals("", tag1.getType());
        assertEquals("", tag1.getSource());
        assertNull(tag1.getUngappedRange());
        assertNull(tag1.getDate());
        assertEquals("", tag1.getComment());
        assertEquals("", tag1.getFreeFormData());

        assertEquals("", tag2.getType());
        assertEquals("", tag2.getSource());
        assertNull(tag2.getUngappedRange());
        assertNull(tag2.getDate());
        assertEquals("", tag2.getComment());
        assertEquals("", tag2.getFreeFormData());

        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        String type1 = "DNA";
        String source1 = "contig1";
        Range ungappedRange1 = new Range(1, 50);
        Date date1 = new Date("2022-01-01");
        String comment1 = "This is a comment";
        String freeFormData1 = "ABC123";

        String type2 = "RNA";
        String source2 = "contig2";
        Range ungappedRange2 = new Range(51, 100);
        Date date2 = new Date("2022-01-02");
        String comment2 = "Another comment";
        String freeFormData2 = "XYZ456";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testClone() {
        String type = "DNA";
        String source = "contig1";
        Range ungappedRange = new Range(1, 50);
        Date date = new Date("2022-01-01");
        String comment = "This is a comment";
        String freeFormData = "ABC123";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        DefaultPhdReadTag clonedTag = tag.clone();

        assertEquals(tag.getType(), clonedTag.getType());
        assertEquals(tag.getSource(), clonedTag.getSource());
        assertEquals(tag.getUngappedRange(), clonedTag.getUngappedRange());
        assertEquals(tag.getDate(), clonedTag.getDate());
        assertEquals(tag.getComment(), clonedTag.getComment());
        assertEquals(tag.getFreeFormData(), clonedTag.getFreeFormData());

        // Test modification of cloned tag does not affect original tag
        clonedTag.setComment("Modified comment");

        assertNotEquals(tag.getComment(), clonedTag.getComment());
    }

    @Test
    public void testCloneWithNullObjects() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag(null, null, null, null, null, null);

        DefaultPhdReadTag clonedTag = tag.clone();

        assertNull(clonedTag.getType());
        assertNull(clonedTag.getSource());
        assertNull(clonedTag.getUngappedRange());
        assertNull(clonedTag.getDate());
        assertNull(clonedTag.getComment());
        assertNull(clonedTag.getFreeFormData());
    }

    @Test
    public void testCloneWithEmptyObjects() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("", "", null, null, "", "");

        DefaultPhdReadTag clonedTag = tag.clone();

        assertEquals("", clonedTag.getType());
        assertEquals("", clonedTag.getSource());
        assertNull(clonedTag.getUngappedRange());
        assertNull(clonedTag.getDate());
        assertEquals("", clonedTag.getComment());
        assertEquals("", clonedTag.getFreeFormData());
    }

    @Test
    public void testCloneWithDifferentObjects() {
        String type1 = "DNA";
        String source1 = "contig1";
        Range ungappedRange1 = new Range(1, 50);
        Date date1 = new Date("2022-01-01");
        String comment1 = "This is a comment";
        String freeFormData1 = "ABC123";

        String type2 = "RNA";
        String source2 = "contig2";
        Range ungappedRange2 = new Range(51, 100);
        Date date2 = new Date("2022-01-02");
        String comment2 = "Another comment";
        String freeFormData2 = "XYZ456";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        DefaultPhdReadTag clonedTag1 = tag1.clone();
        DefaultPhdReadTag clonedTag2 = tag2.clone();

        assertNotEquals(clonedTag1, clonedTag2);
        assertNotEquals(tag1, clonedTag1);
        assertNotEquals(tag2, clonedTag2);
        assertNotEquals(tag1, clonedTag2);
    }
}