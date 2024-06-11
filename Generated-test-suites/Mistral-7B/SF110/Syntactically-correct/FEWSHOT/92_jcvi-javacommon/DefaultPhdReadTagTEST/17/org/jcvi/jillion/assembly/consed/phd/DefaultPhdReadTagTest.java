package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTag() {
        String type = "protein";
        String source = "contig123";
        Range ungappedRange = new Range(10, 20);
        Date date = new Date(2022, 1, 1);
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
        String type = "protein";
        String source = "contig123";
        Range ungappedRange = new Range(10, 20);
        Date date = new Date(2022, 1, 1);
        String comment = "this is a comment";
        String freeFormData = "ABCDEFG";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        assertEquals(hashCode1, hashCode2);

        comment = "another comment";
        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertNotEquals(hashCode1, phdReadTag3.hashCode());
    }

    @Test
    public void testEquals() {
        String type = "protein";
        String source = "contig123";
        Range ungappedRange = new Range(10, 20);
        Date date = new Date(2022, 1, 1);
        String comment = "this is a comment";
        String freeFormData = "ABCDEFG";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertTrue(phdReadTag1.equals(phdReadTag2));

        comment = "another comment";
        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag3));

        source = "contig456";
        DefaultPhdReadTag phdReadTag4 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag4));

        type = "dna";
        DefaultPhdReadTag phdReadTag5 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag5));

        ungappedRange = new Range(5, 15);
        DefaultPhdReadTag phdReadTag6 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag6));

        date = new Date(2023, 2, 1);
        DefaultPhdReadTag phdReadTag7 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag7));
    }
}