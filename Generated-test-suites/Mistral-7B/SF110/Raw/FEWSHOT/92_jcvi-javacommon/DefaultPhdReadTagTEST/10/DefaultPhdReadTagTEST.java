package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import java.util.Date;

import org.jcvi.jillion.core.*;
import org.jcvi.jillion.util.RangeTestUtil;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagInstance() {
        String type = "protein";
        String source = "NC_012345.1";
        Range ungappedRange = new Range(10, 20, Strand.PLUS);
        Date date = new Date();
        String comment = "This is a comment";
        String freeFormData = "Free form data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());

        Range expectedRange = RangeTestUtil.createRange(10, 20, Strand.PLUS);
        assertEquals(expectedRange, phdReadTag.getUngappedRange());
    }

    @Test
    public void testHashCodeEquals() {
        String type1 = "protein1";
        String type2 = "protein2";
        String source1 = "NC_012345.1";
        String source2 = "NC_023456.1";
        Range ungappedRange1 = new Range(10, 20, Strand.PLUS);
        Range ungappedRange2 = new Range(20, 30, Strand.MINUS);
        Date date1 = new Date();
        Date date2 = new Date(System.currentTimeMillis() - 1000);
        String comment1 = "This is a comment1";
        String comment2 = "This is a comment2";
        String freeFormData1 = "Free form data1";
        String freeFormData2 = "Free form data2";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);
        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();
        int hashCode3 = phdReadTag3.hashCode();

        assertTrue(hashCode1 == hashCode3);
        assertNotEquals(hashCode1, hashCode2);

        DefaultPhdReadTag phdReadTag4 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, null, freeFormData1);
        assertNotEquals(hashCode1, phdReadTag4.hashCode());

        DefaultPhdReadTag phdReadTag5 = new DefaultPhdReadTag(type1, source1, null, date1, comment1, freeFormData1);
        assertNotEquals(hashCode1, phdReadTag5.hashCode());

        DefaultPhdReadTag phdReadTag6 = new DefaultPhdReadTag(type1, source1, ungappedRange1, null, comment1, freeFormData1);
        assertNotEquals(hashCode1, phdReadTag6.hashCode());

        DefaultPhdReadTag phdReadTag7 = new DefaultPhdReadTag(type1, null, ungappedRange1, date1, comment1, freeFormData1);
        assertNotEquals(hashCode1, phdReadTag7.hashCode());
    }

    @Test
    public void testToString() {
        String type = "protein";
        String source = "NC_012345.1";
        Range ungappedRange = new Range(10, 20, Strand.PLUS);
        Date date = new Date();
        String comment = "This is a comment";
        String freeFormData = "Free form data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        String expectedString = String.format("DefaultPhdReadTag{type='%s', source='%s', ungappedRange=%s, date=%s, comment='%s', freeFormData='%s'}",
                type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(expectedString, phdReadTag.toString());
    }
}