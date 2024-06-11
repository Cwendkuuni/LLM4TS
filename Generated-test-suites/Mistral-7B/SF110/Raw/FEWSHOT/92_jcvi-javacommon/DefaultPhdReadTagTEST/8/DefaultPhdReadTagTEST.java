package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagWithValidArguments() {
        String type = "DNA";
        String source = "contig_123";
        Range ungappedRange = new Range(1, 100, 1);
        Date date = new Date("2023-01-01");
        String comment = "This is a comment";
        String freeFormData = "Some free form data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagWithNullType() {
        String source = "contig_123";
        Range ungappedRange = new Range(1, 100, 1);
        Date date = new Date("2023-01-01");
        String comment = "This is a comment";
        String freeFormData = "Some free form data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);

        assertNull(phdReadTag.getType());
    }

    @Test
    public void testDefaultPhdReadTagWithNullSource() {
        String type = "DNA";
        Range ungappedRange = new Range(1, 100, 1);
        Date date = new Date("2023-01-01");
        String comment = "This is a comment";
        String freeFormData = "Some free form data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);

        assertNull(phdReadTag.getSource());
    }

    @Test
    public void testDefaultPhdReadTagWithNullUngappedRange() {
        String type = "DNA";
        String source = "contig_123";
        Date date = new Date("2023-01-01");
        String comment = "This is a comment";
        String freeFormData = "Some free form data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);

        assertNull(phdReadTag.getUngappedRange());
    }

    @Test
    public void testDefaultPhdReadTagWithNullDate() {
        String type = "DNA";
        String source = "contig_123";
        Range ungappedRange = new Range(1, 100, 1);
        String comment = "This is a comment";
        String freeFormData = "Some free form data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);

        assertNull(phdReadTag.getDate());
    }

    @Test
    public void testDefaultPhdReadTagWithNullComment() {
        String type = "DNA";
        String source = "contig_123";
        Range ungappedRange = new Range(1, 100, 1);
        Date date = new Date("2023-01-01");
        String freeFormData = "Some free form data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);

        assertNull(phdReadTag.getComment());
    }

    @Test
    public void testDefaultPhdReadTagWithNullFreeFormData() {
        String type = "DNA";
        String source = "contig_123";
        Range ungappedRange = new Range(1, 100, 1);
        Date date = new Date("2023-01-01");
        String comment = "This is a comment";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);

        assertNull(phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagHashCode() {
        String type = "DNA";
        String source = "contig_123";
        Range ungappedRange = new Range(1, 100, 1);
        Date date = new Date("2023-01-01");
        String comment = "This is a comment";
        String freeFormData = "Some free form data";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type1 = "DNA";
        String source1 = "contig_123";
        Range ungappedRange1 = new Range(1, 100, 1);
        Date date1 = new Date("2023-01-01");
        String comment1 = "This is a comment";
        String freeFormData1 = "Some free form data";

        String type2 = "DNA";
        String source2 = "contig_123";
        Range ungappedRange2 = new Range(1, 100, 1);
        Date date2 = new Date("2023-01-01");
        String comment2 = "This is a comment";
        String freeFormData2 = "Some free form data";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        assertTrue(phdReadTag1.equals(phdReadTag3));
        assertFalse(phdReadTag1.equals(phdReadTag2));
    }
}