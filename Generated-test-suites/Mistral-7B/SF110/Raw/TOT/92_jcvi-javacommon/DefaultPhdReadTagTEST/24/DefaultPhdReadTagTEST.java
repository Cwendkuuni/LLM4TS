package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.*;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstruction() {
        String type = "protein";
        String source = "sample.fasta";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date(2022, 1, 1);
        String comment = "this is a comment";
        String freeFormData = "free form data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
        assertNotNull(phdReadTag.hashCode());
    }

    @Test
    public void testGetType() {
        String type = "DNA";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, null, null, null, null, null);

        assertEquals(type, phdReadTag.getType());
    }

    @Test
    public void testEquals() {
        String type1 = "protein";
        String type2 = "DNA";
        String source1 = "sample.fasta";
        String source2 = "sample.fastq";
        Range range1 = new Range(1, 100);
        Range range2 = new Range(1, 200);
        Date date1 = new Date(2022, 1, 1);
        Date date2 = new Date(2022, 1, 2);
        String comment1 = "comment1";
        String comment2 = "comment2";
        String freeFormData1 = "data1";
        String freeFormData2 = "data2";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type2, source2, range2, date2, comment2, freeFormData2);
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);

        // equal tags
        assertTrue(tag1.equals(tag3));

        // different types
        assertFalse(tag1.equals(tag2));

        // different sources
        assertFalse(tag1.equals(tag2));

        // different ungappedRanges
        assertFalse(tag1.equals(tag2));

        // different dates
        assertFalse(tag1.equals(tag2));

        // different comments
        assertFalse(tag1.equals(tag2));

        // different freeFormData
        assertFalse(tag1.equals(tag2));
    }
}