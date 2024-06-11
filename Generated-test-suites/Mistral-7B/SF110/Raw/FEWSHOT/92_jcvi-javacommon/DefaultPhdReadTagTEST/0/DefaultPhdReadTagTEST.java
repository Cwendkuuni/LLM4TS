package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagCreation() {
        String type = "protein";
        String source = "NC_012345.1";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2017-01-01");
        String comment = "this is a comment";
        String freeFormData = "ABC123";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, defaultPhdReadTag.getType());
        assertEquals(source, defaultPhdReadTag.getSource());
        assertEquals(ungappedRange, defaultPhdReadTag.getUngappedRange());
        assertEquals(date, defaultPhdReadTag.getDate());
        assertEquals(comment, defaultPhdReadTag.getComment());
        assertEquals(freeFormData, defaultPhdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagGettersWithNullValues() {
        String type = null;
        String source = null;
        Range ungappedRange = null;
        Date date = null;
        String comment = null;
        String freeFormData = null;

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertNull(defaultPhdReadTag.getType());
        assertNull(defaultPhdReadTag.getSource());
        assertNull(defaultPhdReadTag.getUngappedRange());
        assertNull(defaultPhdReadTag.getDate());
        assertNull(defaultPhdReadTag.getComment());
        assertNull(defaultPhdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagHashCode() {
        String type = "protein";
        String source = "NC_012345.1";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2017-01-01");
        String comment = "this is a comment";
        String freeFormData = "ABC123";

        DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        String type1 = "DNA";
        String source1 = "NC_012345.2";
        Range ungappedRange1 = new Range(1, 200);
        Date date1 = new Date("2017-01-02");
        String comment1 = "this is another comment";
        String freeFormData1 = "XYZ789";

        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        int hashCode1 = defaultPhdReadTag1.hashCode();
        int hashCode2 = defaultPhdReadTag2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type = "protein";
        String source = "NC_012345.1";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2017-01-01");
        String comment = "this is a comment";
        String freeFormData = "ABC123";

        DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertTrue(defaultPhdReadTag1.equals(defaultPhdReadTag2));

        String type1 = "DNA";
        String source1 = "NC_012345.2";
        Range ungappedRange1 = new Range(1, 200);
        Date date1 = new Date("2017-01-02");
        String comment1 = "this is another comment";
        String freeFormData1 = "XYZ789";

        DefaultPhdReadTag defaultPhdReadTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag3));
    }
}