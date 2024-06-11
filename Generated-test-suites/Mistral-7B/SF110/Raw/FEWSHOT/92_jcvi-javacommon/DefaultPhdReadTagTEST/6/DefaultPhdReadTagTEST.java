package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;

import org.jcvi.jillion.core.Range;

import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstruction() {
        String type = "DNA";
        String source = "contig1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(1633452032000L); // 2021-11-01 12:00:32
        String comment = "comment1";
        String freeFormData = "data1";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, tag.getType());
        assertEquals(source, tag.getSource());
        assertEquals(ungappedRange, tag.getUngappedRange());
        assertEquals(date, tag.getDate());
        assertEquals(comment, tag.getComment());
        assertEquals(freeFormData, tag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagHashCode() {
        String type1 = "DNA1";
        String source1 = "contig1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date(1633452032000L); // 2021-11-01 12:00:32
        String comment1 = "comment1";
        String freeFormData1 = "data1";

        String type2 = "DNA2";
        String source2 = "contig2";
        Range ungappedRange2 = new Range(2, 20);
        Date date2 = new Date(1633452132000L); // 2021-11-01 12:01:32
        String comment2 = "comment2";
        String freeFormData2 = "data2";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type1 = "DNA1";
        String source1 = "contig1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date(1633452032000L); // 2021-11-01 12:00:32
        String comment1 = "comment1";
        String freeFormData1 = "data1";

        String type2 = "DNA2";
        String source2 = "contig1";
        Range ungappedRange2 = new Range(1, 10);
        Date date2 = new Date(1633452032000L); // 2021-11-01 12:00:32
        String comment2 = "comment1";
        String freeFormData2 = "data1";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        assertTrue(tag1.equals(tag3));
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testDefaultPhdReadTagEqualsWithNullObjects() {
        String type1 = "DNA1";
        String source1 = "contig1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date(1633452032000L); // 2021-11-01 12:00:32
        String comment1 = "comment1";
        String freeFormData1 = "data1";

        String type2 = null;
        String source2 = "contig2";
        Range ungappedRange2 = new Range(2, 20);
        Date date2 = new Date(1633452132000L); // 2021-11-01 12:01:32
        String comment2 = "comment2";
        String freeFormData2 = "data2";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testDefaultPhdReadTagEqualsWithSameObjects() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("DNA", "contig1", null, null, null, null);
        DefaultPhdReadTag tag2 = tag1;

        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testDefaultPhdReadTagEqualsWithDifferentObjectsButSameValues() {
        String type = "DNA";
        String source = "contig1";
        Range ungappedRange = null;
        Date date = null;
        String comment = null;
        String freeFormData = null;

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertTrue(tag1.equals(tag2));
    }
}