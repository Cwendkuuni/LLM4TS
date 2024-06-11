package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstruction() {
        String type = "protein";
        String source = "read1.fasta";
        Range ungappedRange = new Range(100, 200);
        Date date = Date.fromLocalDateAndTime(LocalDate.of(2022, 1, 1), LocalTime.of(12, 0));
        String comment = "this is a comment";
        String freeFormData = "ABCDEFG";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, tag.getType());
        assertEquals(source, tag.getSource());
        assertEquals(ungappedRange, tag.getUngappedRange());
        assertEquals(date, tag.getDate());
        assertEquals(comment, tag.getComment());
        assertEquals(freeFormData, tag.getFreeFormData());

        // hashCode test
        int expectedHashCode = Arrays.hashCode(new Object[]{type, source, ungappedRange, date, comment, freeFormData});
        int actualHashCode = tag.hashCode();
        assertEquals(expectedHashCode, actualHashCode);

        // equals test
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertTrue(tag.equals(tag2));

        // equals with different object
        Object obj = new Object();
        assertFalse(tag.equals(obj));

        // equals with null
        assertFalse(tag.equals(null));

        // equals with different type
        String otherType = "dna";
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertFalse(tag.equals(tag3));

        // equals with different source
        String otherSource = "read2.fasta";
        DefaultPhdReadTag tag4 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertFalse(tag.equals(tag4));

        // equals with different ungappedRange
        Range otherUngappedRange = new Range(150, 250);
        DefaultPhdReadTag tag5 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertFalse(tag.equals(tag5));

        // equals with different date
        Date otherDate = Date.fromLocalDateAndTime(LocalDate.of(2022, 1, 2), LocalTime.of(13, 0));
        DefaultPhdReadTag tag6 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertFalse(tag.equals(tag6));

        // equals with different comment
        String otherComment = "this is a different comment";
        DefaultPhdReadTag tag7 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertFalse(tag.equals(tag7));

        // equals with different freeFormData
        String otherFreeFormData = "ABCDEFH";
        DefaultPhdReadTag tag8 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertFalse(tag.equals(tag8));
    }

    @Test
    public void testStringMethods() {
        String type = "protein";
        String source = "read1.fasta";
        Range ungappedRange = new Range(100, 200);
        Date date = Date.fromLocalDateAndTime(LocalDate.of(2022, 1, 1), LocalTime.of(12, 0));
        String comment = "this is a comment";
        String freeFormData = "ABCDEFG";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        // getType()
        assertEquals(type, tag.getType());

        // getSource()
        assertEquals(source, tag.getSource());

        // getUngappedRange()
        assertEquals(ungappedRange.getStart(), tag.getUngappedRange().getStart());
        assertEquals(ungappedRange.getEnd(), tag.getUngappedRange().getEnd());

        // getDate()
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        assertEquals(formatter.format(date), tag.getDate().toString());

        // getComment()
        assertEquals(comment, tag.getComment());

        // getFreeFormData()
        assertEquals(freeFormData, tag.getFreeFormData());
    }
}