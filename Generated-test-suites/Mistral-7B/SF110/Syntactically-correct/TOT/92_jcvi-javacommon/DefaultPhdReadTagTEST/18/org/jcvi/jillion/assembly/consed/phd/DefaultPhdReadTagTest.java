
package org.junit.junction.assembly.consed.phd;

import org.jcvi.jillion.core.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstructor() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag phdTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdTag.getType());
        assertEquals(source, phdTag.getSource());
        assertEquals(ungappedRange, phdTag.getUngappedRange());
        assertEquals(date, phdTag.getDate());
        assertEquals(comment, phdTag.getComment());
        assertEquals(freeFormData, phdTag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        String type1 = "type1";
        String type2 = "type2";
        String source1 = "source1";
        String source2 = "source2";
        Range ungappedRange1 = new Range(1, 10);
        Range ungappedRange2 = new Range(11, 20);
        Date date1 = new Date("2022-01-01");
        Date date2 = new Date("2022-01-02");
        String comment1 = "comment1";
        String comment2 = "comment2";
        String freeFormData1 = "freeFormData1";
        String freeFormData2 = "freeFormData2";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();

        // Different values should have different hash codes
        assertNotEquals(hashCode1, hashCode2);

        // Same values should have the same hash code
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        int hashCode3 = tag3.hashCode();
        assertEquals(hashCode1, hashCode3);
    }

    @Test
    public void testEquals() {
        String type1 = "type1";
        String type2 = "type2";
        String source1 = "source1";
        String source2 = "source1";
        Range ungappedRange1 = new Range(1, 10);
        Range ungappedRange2 = new Range(11, 20);
        Date date1 = new Date("2022-01-01");
        Date date2 = new Date("2022-01-02");
        String comment1 = "comment1";
        String comment2 = "comment1";
        String freeFormData1 = "freeFormData1";
        String freeFormData2 = "freeFormData2";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        // Different objects should not be equal
        assertFalse(tag1.equals(tag2));

        // Same objects should be equal
        assertTrue(tag1.equals(tag3));
    }
}
