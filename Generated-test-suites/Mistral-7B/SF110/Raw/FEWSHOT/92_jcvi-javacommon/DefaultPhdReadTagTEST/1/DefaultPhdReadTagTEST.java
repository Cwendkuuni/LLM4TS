package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import java.util.Date;

import org.jcvi.jillion.core.Range;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstruction() {
        String type = "type1";
        String source = "source1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "comment1";
        String freeFormData = "freeFormData1";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testGetHashCodeWithSameObject() {
        String type = "type1";
        String source = "source1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "comment1";
        String freeFormData = "freeFormData1";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = phdReadTag1;

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testGetHashCodeWithDifferentObject() {
        String type1 = "type1";
        String source1 = "source1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date();
        String comment1 = "comment1";
        String freeFormData1 = "freeFormData1";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "type2";
        String source2 = "source2";
        Range ungappedRange2 = new Range(1, 10);
        Date date2 = new Date();
        String comment2 = "comment2";
        String freeFormData2 = "freeFormData2";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEqualsWithSameObject() {
        String type = "type1";
        String source = "source1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "comment1";
        String freeFormData = "freeFormData1";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = phdReadTag1;

        boolean equals1 = phdReadTag1.equals(phdReadTag2);

        assertTrue(equals1);
    }

    @Test
    public void testEqualsWithDifferentObjectType() {
        String type = "type1";
        String source = "source1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "comment1";
        String freeFormData = "freeFormData1";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Object obj = new Object();

        boolean equals = phdReadTag1.equals(obj);

        assertFalse(equals);
    }

    @Test
    public void testEqualsWithDifferentObjectInstance() {
        String type1 = "type1";
        String source1 = "source1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date();
        String comment1 = "comment1";
        String freeFormData1 = "freeFormData1";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "type2";
        String source2 = "source2";
        Range ungappedRange2 = new Range(1, 10);
        Date date2 = new Date();
        String comment2 = "comment2";
        String freeFormData2 = "freeFormData2";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        boolean equals = phdReadTag1.equals(phdReadTag2);

        assertFalse(equals);
    }

    @Test
    public void testEqualsWithSameValues() {
        String type = "type1";
        String source = "source1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "comment1";
        String freeFormData = "freeFormData1";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        String type2 = "type1";
        String source2 = "source1";
        Range ungappedRange2 = new Range(1, 10);
        Date date2 = new Date();
        String comment2 = "comment1";
        String freeFormData2 = "freeFormData1";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        boolean equals = phdReadTag1.equals(phdReadTag2);

        assertTrue(equals);
    }

    @Test
    public void testEqualsWithDifferentValues() {
        String type1 = "type1";
        String source1 = "source1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date();
        String comment1 = "comment1";
        String freeFormData1 = "freeFormData1";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "type2";
        String source2 = "source2";
        Range ungappedRange2 = new Range(11, 20);
        Date date2 = new Date();
        String comment2 = "comment2";
        String freeFormData2 = "freeFormData2";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        boolean equals = phdReadTag1.equals(phdReadTag2);

        assertFalse(equals);
    }
}