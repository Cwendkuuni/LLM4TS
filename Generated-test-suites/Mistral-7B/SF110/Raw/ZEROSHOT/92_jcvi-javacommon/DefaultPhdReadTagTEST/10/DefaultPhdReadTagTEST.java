package org.jcvi.jillion.assembly.consed.phd.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jcvi.jillion.core.*;
import org.jcvi.jillion.test.RangeTest;
import org.jcvi.jillion.test.TestUtil;
import org.jcvi.jillion.test.date.TestDate;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstruction() {
        String type = "protein";
        String source = "NC_001234.1";
        Range ungappedRange = Range.of(1, 100);
        Date date = TestDate.parseDate("2022-01-01");
        String comment = "this is a comment";
        String freeFormData = "some free form data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertNotNull(phdReadTag);
        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        Range expectedUngappedRange = RangeTest.rangeWithSameStartAndEnd(ungappedRange);
        assertEquals(expectedUngappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagHashCode() {
        String type = "protein";
        String source = "NC_001234.1";
        Range ungappedRange = Range.of(1, 100);
        Date date = TestDate.parseDate("2022-01-01");
        String comment = "this is a comment";
        String freeFormData = "some free form data";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type = "protein";
        String source = "NC_001234.1";
        Range ungappedRange1 = Range.of(1, 100);
        Range ungappedRange2 = Range.of(1, 100);
        Date date = TestDate.parseDate("2022-01-01");
        String comment = "this is a comment";
        String freeFormData = "some free form data";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange1, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange2, date, comment, freeFormData);

        assertTrue(phdReadTag1.equals(phdReadTag2));

        String differentComment = "this is a different comment";
        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type, source, ungappedRange1, date, differentComment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag3));

        String differentSource = "NC_001235.1";
        DefaultPhdReadTag phdReadTag4 = new DefaultPhdReadTag(type, differentSource, ungappedRange1, date, comment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag4));

        String differentType = "DNA";
        DefaultPhdReadTag phdReadTag5 = new DefaultPhdReadTag(differentType, source, ungappedRange1, date, comment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag5));

        Range differentUngappedRange = Range.of(2, 101);
        DefaultPhdReadTag phdReadTag6 = new DefaultPhdReadTag(type, source, differentUngappedRange, date, comment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag6));

        Date differentDate = TestDate.parseDate("2022-02-01");
        DefaultPhdReadTag phdReadTag7 = new DefaultPhdReadTag(type, source, ungappedRange1, differentDate, comment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag7));
    }

    @Test
    public void testDefaultPhdReadTagNullValues() {
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
    public void testDefaultPhdReadTagEmptyValues() {
        String type = "";
        String source = "";
        Range ungappedRange = Range.EMPTY;
        Date date = TestDate.EMPTY;
        String comment = "";
        String freeFormData = "";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals("", phdReadTag.getType());
        assertEquals("", phdReadTag.getSource());
        assertNull(phdReadTag.getUngappedRange());
        assertNull(phdReadTag.getDate());
        assertEquals("", phdReadTag.getComment());
        assertEquals("", phdReadTag.getFreeFormData());
    }
}