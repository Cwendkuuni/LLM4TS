package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.util.DateUtil;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagWithValidArguments() {
        String type = "protein";
        String source = "contig123";
        Range ungappedRange = new Range(1, 100, 1);
        Date date = DateUtil.parse("2022-01-01");
        String comment = "comment1";
        String freeFormData = "data1";

        DefaultPhdReadTag readTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, readTag.getType());
        assertEquals(source, readTag.getSource());
        assertEquals(ungappedRange, readTag.getUngappedRange());
        assertEquals(date, readTag.getDate());
        assertEquals(comment, readTag.getComment());
        assertEquals(freeFormData, readTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagWithNullArguments() {
        String type = null;
        String source = null;
        Range ungappedRange = null;
        Date date = null;
        String comment = null;
        String freeFormData = null;

        assertNull(new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData));
    }

    @Test
    public void testDefaultPhdReadTagWithEmptyStringArguments() {
        String type = "";
        String source = "";
        Range ungappedRange = new Range(1, 100, 1);
        Date date = new Date();
        String comment = "";
        String freeFormData = "";

        DefaultPhdReadTag readTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, readTag.getType());
        assertEquals(source, readTag.getSource());
        assertEquals(ungappedRange, readTag.getUngappedRange());
        assertEquals(date, readTag.getDate());
        assertEquals(comment, readTag.getComment());
        assertEquals(freeFormData, readTag.getFreeFormData());
    }

    @Test
    public void testEquals() {
        String type1 = "protein";
        String type2 = "DNA";
        String source1 = "contig123";
        String source2 = "contig456";
        Range range1 = new Range(1, 100, 1);
        Range range2 = new Range(50, 200, 1);
        Date date1 = DateUtil.parse("2022-01-01");
        Date date2 = DateUtil.parse("2023-01-01");
        String comment1 = "comment1";
        String comment2 = "comment2";
        String freeFormData1 = "data1";
        String freeFormData2 = "data2";

        DefaultPhdReadTag readTag1 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);
        DefaultPhdReadTag readTag2 = new DefaultPhdReadTag(type2, source2, range2, date2, comment2, freeFormData2);

        assertFalse(readTag1.equals(readTag2));

        DefaultPhdReadTag readTag3 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);

        assertTrue(readTag1.equals(readTag3));
    }

    @Test
    public void testHashCode() {
        String type = "protein";
        String source = "contig123";
        Range ungappedRange = new Range(1, 100, 1);
        Date date = DateUtil.parse("2022-01-01");
        String comment = "comment1";
        String freeFormData = "data1";

        int hashCode1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData).hashCode();

        DefaultPhdReadTag readTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(hashCode1, readTag2.hashCode());
    }
}