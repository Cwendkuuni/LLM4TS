package org.jcvi.jillion.assembly.consed.phd.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jcvi.jillion.core.*;
import java.util.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.assembly.consed.phd.PhdReadTag;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstructor() {
        String type = "typespec";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment";
        String freeFormData = "freetext";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals("typespec", phdReadTag.getType());
        assertEquals("source", phdReadTag.getSource());
        assertEquals(new Range(1, 10), phdReadTag.getUngappedRange());
        assertEquals(new Date("2022-01-01"), phdReadTag.getDate());
        assertEquals("comment", phdReadTag.getComment());
        assertEquals("freetext", phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagGetters() {
        String type = "typespec";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment";
        String freeFormData = "freetext";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals("typespec", phdReadTag.getType());
        assertEquals("source", phdReadTag.getSource());
        assertEquals(new Range(1, 10), phdReadTag.getUngappedRange());
        assertEquals(new Date("2022-01-01"), phdReadTag.getDate());
        assertEquals("comment", phdReadTag.getComment());
        assertEquals("freetext", phdReadTag.getFreeFormData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullType() {
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment";
        String freeFormData = "freetext";

        new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullSource() {
        String type = "typespec";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment";
        String freeFormData = "freetext";

        new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullUngappedRange() {
        String type = "typespec";
        String source = "source";
        Date date = new Date("2022-01-01");
        String comment = "comment";
        String freeFormData = "freetext";

        new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullDate() {
        String type = "typespec";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        String comment = "comment";
        String freeFormData = "freetext";

        new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullComment() {
        String type = "typespec";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String freeFormData = "freetext";

        new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);
    }

    @Test
    public void testDefaultPhdReadTagHashCode() {
        String type1 = "typespec1";
        String source1 = "source1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date("2022-01-01");
        String comment1 = "comment1";
        String freeFormData1 = "freetext1";

        String type2 = "typespec2";
        String source2 = "source2";
        Range ungappedRange2 = new Range(1, 10);
        Date date2 = new Date("2022-01-01");
        String comment2 = "comment2";
        String freeFormData2 = "freetext2";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        assertNotSame(hashCode1, hashCode2);
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type1 = "typespec1";
        String source1 = "source1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date("2022-01-01");
        String comment1 = "comment1";
        String freeFormData1 = "freetext1";

        String type2 = "typespec1";
        String source2 = "source1";
        Range ungappedRange2 = new Range(1, 10);
        Date date2 = new Date("2022-01-01");
        String comment2 = "comment1";
        String freeFormData2 = "freetext1";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        assertTrue(phdReadTag1.equals(phdReadTag1));
        assertFalse(phdReadTag1.equals(phdReadTag2));

        phdReadTag2 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        assertTrue(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testDefaultPhdReadTagEqualsWithNull() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date("2022-01-01"), "comment", "freetext");

        assertFalse(phdReadTag.equals(null));
    }

    @Test
    public void testDefaultPhdReadTagEqualsWithDifferentClass() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date("2022-01-01"), "comment", "freetext");

        assertFalse(phdReadTag.equals(new Object()));
    }
}