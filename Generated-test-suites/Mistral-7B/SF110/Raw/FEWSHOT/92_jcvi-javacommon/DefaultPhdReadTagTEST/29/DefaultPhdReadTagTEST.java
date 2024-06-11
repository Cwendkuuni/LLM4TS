package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;

import org.jcvi.jillion.core.*;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagCreation() {
        String type = "protein";
        String source = "contig_001";
        Range ungappedRange = new Range(10, 20);
        Date date = new Date();
        String comment = "comment1";
        String freeFormData = "data1";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagGetters() {
        String type = "protein";
        String source = "contig_001";
        Range ungappedRange = new Range(10, 20);
        Date date = new Date();
        String comment = "comment1";
        String freeFormData = "data1";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertNull(phdReadTag.getType());
        assertNull(phdReadTag.getSource());
        assertNull(phdReadTag.getUngappedRange());
        assertNull(phdReadTag.getDate());
        assertNull(phdReadTag.getComment());
        assertNull(phdReadTag.getFreeFormData());

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagHashCode() {
        String type1 = "protein1";
        String type2 = "protein2";
        String source1 = "contig_001";
        String source2 = "contig_002";
        Range range1 = new Range(10, 20);
        Range range2 = new Range(20, 30);
        Date date1 = new Date();
        Date date2 = new Date();
        String comment1 = "comment1";
        String comment2 = "comment2";
        String freeFormData1 = "data1";
        String freeFormData2 = "data2";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, range2, date2, comment2, freeFormData2);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        assertNotEquals(hashCode1, hashCode2);

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);

        assertEquals(hashCode1, phdReadTag3.hashCode());
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type1 = "protein1";
        String type2 = "protein2";
        String source1 = "contig_001";
        String source2 = "contig_002";
        Range range1 = new Range(10, 20);
        Range range2 = new Range(20, 30);
        Date date1 = new Date();
        Date date2 = new Date();
        String comment1 = "comment1";
        String comment2 = "comment2";
        String freeFormData1 = "data1";
        String freeFormData2 = "data2";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, range2, date2, comment2, freeFormData2);
        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag4 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData2);

        assertNotEquals(phdReadTag1, phdReadTag2);
        assertNotEquals(phdReadTag1, phdReadTag3);
        assertNotEquals(phdReadTag1, phdReadTag4);

        assertEquals(phdReadTag1, phdReadTag3);
    }

    @Test
    public void testDefaultPhdReadTagNullInputs() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, null, null, null);

        assertNull(phdReadTag.getType());
        assertNull(phdReadTag.getSource());
        assertNull(phdReadTag.getUngappedRange());
        assertNull(phdReadTag.getDate());
        assertNull(phdReadTag.getComment());
        assertNull(phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagComparingObjects() {
        String type1 = "protein1";
        String type2 = "protein2";
        String source1 = "contig_001";
        String source2 = "contig_002";
        Range range1 = new Range(10, 20);
        Range range2 = new Range(20, 30);
        Date date1 = new Date();
        Date date2 = new Date();
        String comment1 = "comment1";
        String comment2 = "comment2";
        String freeFormData1 = "data1";
        String freeFormData2 = "data2";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, range2, date2, comment2, freeFormData2);

        int comparisonResult = phdReadTag1.compareTo(phdReadTag2);

        assertTrue(comparisonResult < 0);

        phdReadTag1 = new DefaultPhdReadTag(type2, source2, range2, date2, comment2, freeFormData2);
        phdReadTag2 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);

        comparisonResult = phdReadTag1.compareTo(phdReadTag2);

        assertTrue(comparisonResult > 0);

        phdReadTag1 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);
        phdReadTag2 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);

        int comparisonResultEqual = phdReadTag1.compareTo(phdReadTag2);

        assertEquals(0, comparisonResultEqual);
    }

    @Test
    public void testDefaultPhdReadTagComparatorNullInputs() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(null, null, null, null, null, null);
        DefaultPhdReadTag phdReadTag2 = null;

        int comparisonResult = phdReadTag1.compareTo(phdReadTag2);

        assertEquals(0, comparisonResult);
    }
}