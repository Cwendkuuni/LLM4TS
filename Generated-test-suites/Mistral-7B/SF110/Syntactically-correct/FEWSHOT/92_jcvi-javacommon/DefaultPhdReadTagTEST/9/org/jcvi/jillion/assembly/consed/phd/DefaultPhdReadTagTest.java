package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.util.DateUtil;
import org.junit.Test;

public class DefaultPhdReadTagTest {
    @Test
    public void testDefaultPhdReadTagCreation() {
        String type = "protein";
        String source = "read1.fasta";
        Range ungappedRange = new Range(10, 20);
        Date date = DateUtil.parseDate("2022-01-01");
        String comment = "this is a comment";
        String freeFormData = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, tag.getType());
        assertEquals(source, tag.getSource());
        assertEquals(ungappedRange, tag.getUngappedRange());
        assertEquals(date, tag.getDate());
        assertEquals(comment, tag.getComment());
        assertEquals(freeFormData, tag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagNullArguments() {
        String type = null;
        String source = null;
        Range ungappedRange = null;
        Date date = null;
        String comment = null;
        String freeFormData = null;

        try {
            new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testDefaultPhdReadTagEmptyStringArguments() {
        String type = "";
        String source = "";
        Range ungappedRange = new Range(0, 0);
        Date date = new Date();
        String comment = "";
        String freeFormData = "";

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
        String type1 = "protein1";
        String source1 = "read1.fasta1";
        Range ungappedRange1 = new Range(10, 20);
        Date date1 = DateUtil.parseDate("2022-01-01");
        String comment1 = "this is a comment1";
        String freeFormData1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "protein2";
        String source2 = "read2.fasta2";
        Range ungappedRange2 = new Range(30, 40);
        Date date2 = DateUtil.parseDate("2022-01-02");
        String comment2 = "this is a comment2";
        String freeFormData2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ2";

        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type1 = "protein1";
        String source1 = "read1.fasta1";
        Range ungappedRange1 = new Range(10, 20);
        Date date1 = DateUtil.parseDate("2022-01-01");
        String comment1 = "this is a comment1";
        String freeFormData1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "protein1";
        String source2 = "read1.fasta1";
        Range ungappedRange2 = new Range(10, 20);
        Date date2 = DateUtil.parseDate("2022-01-01");
        String comment2 = "this is a comment1";
        String freeFormData2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1";

        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, null);

        assertTrue(tag1.equals(tag2));
        assertFalse(tag1.equals(tag3));
    }
}