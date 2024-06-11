package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Time;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagCreation() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Time date = new Time("2022-01-01T00:00:00.000Z");
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag readTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, readTag.getType());
        assertEquals(source, readTag.getSource());
        assertEquals(ungappedRange, readTag.getUngappedRange());
        assertEquals(date, readTag.getDate());
        assertEquals(comment, readTag.getComment());
        assertEquals(freeFormData, readTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagHashCode() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Time date = new Time("2022-01-01T00:00:00.000Z");
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag readTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag readTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        int hashCode1 = readTag1.hashCode();
        int hashCode2 = readTag2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type1 = "type1";
        String type2 = "type2";
        String source1 = "source1";
        String source2 = "source2";
        Range ungappedRange1 = new Range(1, 10);
        Range ungappedRange2 = new Range(1, 10);
        Time date1 = new Time("2022-01-01T00:00:00.000Z");
        Time date2 = new Time("2022-01-01T00:00:00.000Z");
        String comment1 = "comment1";
        String comment2 = "comment2";
        String freeFormData1 = "freeFormData1";
        String freeFormData2 = "freeFormData2";

        DefaultPhdReadTag readTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag readTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);
        DefaultPhdReadTag readTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        assertTrue(readTag1.equals(readTag3));
        assertFalse(readTag1.equals(readTag2));
    }

    @Test
    public void testDefaultPhdReadTagNullValues() {
        DefaultPhdReadTag readTag = new DefaultPhdReadTag(null, null, null, null, null, null);

        assertNull(readTag.getType());
        assertNull(readTag.getSource());
        assertNull(readTag.getUngappedRange());
        assertNull(readTag.getDate());
        assertNull(readTag.getComment());
        assertNull(readTag.getFreeFormData());
    }
}