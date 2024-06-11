
package org.jcvi.jillion.assembly.consed.phd;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jcvi.jillion.core.Range;
import java.util.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag("DNA", "contig1", new Range(1, 100), new Date(), "comment1", "data1");
        assertEquals("DNA", phdTag.getType());
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag("DNA", "contig1", new Range(1, 100), new Date(), "comment1", "data1");
        assertEquals("contig1", phdTag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag("DNA", "contig1", new Range(1, 100), new Date(), "comment1", "data1");
        Range expectedRange = new Range(1, 100);
        assertEquals(expectedRange, phdTag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag("DNA", "contig1", new Range(1, 100), new Date(), "comment1", "data1");
        Date expectedDate = new Date(); // Provide a valid date
        assertEquals(expectedDate, phdTag.getDate());
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag("DNA", "contig1", new Range(1, 100), new Date(), "comment1", "data1");
        assertEquals("comment1", phdTag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag("DNA", "contig1", new Range(1, 100), new Date(), "comment1", "data1");
        assertEquals("data1", phdTag.getFreeFormData());
    }

    @Test (expected = NullPointerException.class)
    public void testNullValues() {
        new DefaultPhdReadTag(null, null, null, null, null, null);
    }

    @Test
    public void testEmptyStrings() {
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag("DNA", "", new Range(1, 100), new Date(), "", "");
        assertEquals("", phdTag.getComment());
        assertEquals("", phdTag.getFreeFormData());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testOutOfRangeUngappedRange() {
        new DefaultPhdReadTag("DNA", "contig1", new Range(-1, 100), new Date(), "comment1", "data1");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidDateFormat() {
        String invalidDate = "2022-02-30";
        new DefaultPhdReadTag("DNA", "contig1", new Range(1, 100), new Date(invalidDate), "comment1", "data1");
    }
}
