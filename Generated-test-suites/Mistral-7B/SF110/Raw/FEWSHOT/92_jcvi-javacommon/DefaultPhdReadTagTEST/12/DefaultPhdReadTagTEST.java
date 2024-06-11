package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;

import java.util.Arrays;

public class DefaultPhdReadTagTest {
    @Test
    public void testDefaultPhdReadTagCreation() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(1, 1, 2022);
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        String type1 = "type1";
        String type2 = "type2";
        String source1 = "source1";
        String source2 = "source2";
        Range ungappedRange1 = new Range(1, 10);
        Range ungappedRange2 = new Range(2, 20);
        Date date1 = new Date(1, 1, 2022);
        Date date2 = new Date(2, 1, 2022);
        String comment1 = "comment1";
        String comment2 = "comment2";
        String freeFormData1 = "freeFormData1";
        String freeFormData2 = "freeFormData2";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        assertNotEquals(hashCode1, hashCode2);

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        assertEquals(hashCode1, phdReadTag3.hashCode());
    }

    @Test
    public void testEquals() {
        String type1 = "type1";
        String type2 = "type2";
        String source1 = "source1";
        String source2 = "source2";
        Range ungappedRange1 = new Range(1, 10);
        Range ungappedRange2 = new Range(2, 20);
        Date date1 = new Date(1, 1, 2022);
        Date date2 = new Date(2, 1, 2022);
        String comment1 = "comment1";
        String comment2 = "comment2";
        String freeFormData1 = "freeFormData1";
        String freeFormData2 = "freeFormData2";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        assertFalse(phdReadTag1.equals(phdReadTag2));

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        assertTrue(phdReadTag1.equals(phdReadTag3));
    }

    @Test
    public void testEqualsWithNullObject() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", null, null, null, null);

        assertFalse(phdReadTag.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", null, null, null, null);

        assertFalse(phdReadTag.equals("test"));
    }

    @Test
    public void testDefaultPhdReadTagWithNullValues() {
        String type = null;
        String source = null;
        Range ungappedRange = null;
        Date date = null;
        String comment = null;
        String freeFormData = null;

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(null, phdReadTag.getType());
        assertEquals(null, phdReadTag.getSource());
        assertNull(phdReadTag.getUngappedRange());
        assertNull(phdReadTag.getDate());
        assertEquals(null, phdReadTag.getComment());
        assertEquals(null, phdReadTag.getFreeFormData());
    }

    @Test
    public void testGetters() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(1, 1, 2022);
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagWithEmptyValues() {
        String type = "";
        String source = "";
        Range ungappedRange = new Range(0, 0);
        Date date = new Date(0, 0, 0);
        String comment = "";
        String freeFormData = "";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals("", phdReadTag.getType());
        assertEquals("", phdReadTag.getSource());
        assertEquals(new Range(0, 0), phdReadTag.getUngappedRange());
        assertEquals(new Date(0, 0, 0), phdReadTag.getDate());
        assertEquals("", phdReadTag.getComment());
        assertEquals("", phdReadTag.getFreeFormData());
    }

    @Test
    public void testToString() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(1, 1, 2022);
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        String expectedToString = "DefaultPhdReadTag{" +
                "type='" + type + '\'' +
                ", source='" + source + '\'' +
                ", ungappedRange=" + ungappedRange +
                ", date=" + date +
                ", comment='" + comment + '\'' +
                ", freeFormData='" + freeFormData + '\'' +
                '}';

        assertEquals(expectedToString, phdReadTag.toString());
    }
}