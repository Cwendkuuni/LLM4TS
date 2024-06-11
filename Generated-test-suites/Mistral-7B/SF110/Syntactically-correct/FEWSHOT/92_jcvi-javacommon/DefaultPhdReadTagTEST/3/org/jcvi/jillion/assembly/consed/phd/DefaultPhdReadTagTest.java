package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagWithValidArguments() {
        String type = "DNA";
        String source = "Contig123";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "This is a comment";
        String freeFormData = "Free form data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagWithNullArguments() {
        String type = null;
        String source = null;
        Range ungappedRange = null;
        Date date = null;
        String comment = null;
        String freeFormData = null;

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertNull(phdReadTag.getType());
        assertNull(phdReadTag.getSource());
        assertNull(phdReadTag.getUngappedRange());
        assertNull(phdReadTag.getDate());
        assertNull(phdReadTag.getComment());
        assertNull(phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagWithEmptyArguments() {
        String type = "";
        String source = "";
        Range ungappedRange = new Range(0, 0);
        Date date = new Date("");
        String comment = "";
        String freeFormData = "";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals("", phdReadTag.getType());
        assertEquals("", phdReadTag.getSource());
        assertEquals(new Range(0, 0), phdReadTag.getUngappedRange());
        assertEquals(new Date(""), phdReadTag.getDate());
        assertEquals("", phdReadTag.getComment());
        assertEquals("", phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagHashCode() {
        String type = "DNA";
        String source = "Contig123";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "This is a comment";
        String freeFormData = "Free form data";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testDefaultPhdReadTagEqualsObject() {
        String type = "DNA";
        String source = "Contig123";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "This is a comment";
        String freeFormData = "Free form data";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertTrue(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testDefaultPhdReadTagNotEqualsObjectDifferentType() {
        String type = "DNA";
        String source = "Contig123";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "This is a comment";
        String freeFormData = "Free form data";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        Object obj = new Object();

        assertFalse(phdReadTag1.equals(obj));
    }

    @Test
    public void testDefaultPhdReadTagNotEqualsObjectDifferentSource() {
        String type = "DNA";
        String source = "Contig123";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "This is a comment";
        String freeFormData = "Free form data";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, "Contig456", ungappedRange, date, comment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testDefaultPhdReadTagNotEqualsObjectDifferentUngappedRange() {
        String type = "DNA";
        String source = "Contig123";
        Range ungappedRange1 = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "This is a comment";
        String freeFormData = "Free form data";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange1, date, comment, freeFormData);
        Range ungappedRange2 = new Range(5, 15);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange2, date, comment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testDefaultPhdReadTagNotEqualsObjectDifferentDate() {
        String type = "DNA";
        String source = "Contig123";
        Range ungappedRange = new Range(1, 10);
        Date date1 = new Date("2022-01-01");
        Date date2 = new Date("2022-01-02");
        String comment = "This is a comment";
        String freeFormData = "Free form data";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date1, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date2, comment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testDefaultPhdReadTagNotEqualsObjectDifferentComment() {
        String type = "DNA";
        String source = "Contig123";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment1 = "This is a comment";
        String comment2 = "Another comment";
        String freeFormData = "Free form data";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment1, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment2, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testDefaultPhdReadTagNotEqualsObjectDifferentFreeFormData() {
        String type = "DNA";
        String source = "Contig123";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "This is a comment";
        String freeFormData1 = "Free form data";
        String freeFormData2 = "Another free form data";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData2);

        assertFalse(phdReadTag1.equals(phdReadTag2));
    }
}