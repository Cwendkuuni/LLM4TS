package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.Date;
import org.jcvi.jillion.core.Range;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTag() {
        String type = "protein:1:cds";
        String source = "LOC123456";
        Range ungappedRange = new Range(10, 20);
        Date date = new Date("2022-01-01");
        String comment = "this is a comment";
        String freeFormData = "ABCDEFG";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testGetHashCode() {
        String type = "protein:1:cds";
        String source = "LOC123456";
        Range ungappedRange = new Range(10, 20);
        Date date = new Date("2022-01-01");
        String comment = "this is a comment";
        String freeFormData = "ABCDEFG";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        String type1 = "protein:1:cds";
        String type2 = "protein:2:cds";
        String source1 = "LOC123456";
        String source2 = "LOC234567";
        Range ungappedRange1 = new Range(10, 20);
        Range ungappedRange2 = new Range(11, 21);
        Date date1 = new Date("2022-01-01");
        Date date2 = new Date("2022-01-02");
        String comment1 = "this is a comment";
        String comment2 = "this is a different comment";
        String freeFormData1 = "ABCDEFG";
        String freeFormData2 = "HIJKLMN";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        assertFalse(phdReadTag1.equals(phdReadTag2));

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        assertTrue(phdReadTag1.equals(phdReadTag3));
    }

    @Test
    public void testNullObject() {
        String type = "protein:1:cds";
        String source = "LOC123456";
        Range ungappedRange = new Range(10, 20);
        Date date = new Date("2022-01-01");
        String comment = "this is a comment";
        String freeFormData = "ABCDEFG";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertFalse(phdReadTag.equals(null));
        assertNull(phdReadTag.getSource());
        assertNull(phdReadTag.getComment());
        assertNull(phdReadTag.getFreeFormData());
    }
}