package org.jcvi.jillion.assembly.consed.phd;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jcvi.jillion.core.*;
import java.util.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstruction() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2021-12-31");
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
    public void testDefaultPhdReadTagHashCode() {
        String type1 = "type1";
        String source1 = "source1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date("2021-12-31");
        String comment1 = "comment1";
        String freeFormData1 = "freeFormData1";

        DefaultPhdReadTag phdTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "type2";
        String source2 = "source2";
        Range ungappedRange2 = new Range(1, 11);
        Date date2 = new Date("2021-12-31");
        String comment2 = "comment2";
        String freeFormData2 = "freeFormData2";

        DefaultPhdReadTag phdTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        String type3 = "type1";
        String source3 = "source1";
        Range ungappedRange3 = new Range(1, 10);
        Date date3 = new Date("2022-01-01");
        String comment3 = "comment1";
        String freeFormData3 = "freeFormData1";

        DefaultPhdReadTag phdTag3 = new DefaultPhdReadTag(type3, source3, ungappedRange3, date3, comment3, freeFormData3);

        int hashCode1 = phdTag1.hashCode();
        int hashCode2 = phdTag2.hashCode();
        int hashCode3 = phdTag3.hashCode();

        assertEquals(hashCode1, hashCode3); // same object, should have the same hashcode
        assertNotEquals(hashCode1, hashCode2); // different objects, should have different hashcodes
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type1 = "type1";
        String source1 = "source1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date("2021-12-31");
        String comment1 = "comment1";
        String freeFormData1 = "freeFormData1";

        DefaultPhdReadTag phdTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "type1";
        String source2 = "source1";
        Range ungappedRange2 = new Range(1, 11);
        Date date2 = new Date("2021-12-31");
        String comment2 = "comment1";
        String freeFormData2 = "freeFormData1";

        DefaultPhdReadTag phdTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        String type3 = "type1";
        String source3 = "source1";
        Range ungappedRange3 = new Range(1, 10);
        Date date3 = new Date("2021-12-31");
        String comment3 = "comment1";
        String freeFormData3 = "freeFormData1";

        DefaultPhdReadTag phdTag3 = new DefaultPhdReadTag(type3, source3, ungappedRange3, date3, comment3, freeFormData3);

        DefaultPhdReadTag phdTag4 = new DefaultPhdReadTag("type2", "source2", null, null, null, null);

        assertTrue(phdTag1.equals(phdTag1)); // Reflexive property
        assertTrue(phdTag1.equals(phdTag3)); // Equals reflexive property
        assertTrue(phdTag1.equals(phdTag3)); // Symmetric property
        assertFalse(phdTag1.equals(phdTag4)); //Transitive property
        assertFalse(phdTag1.equals(null)); //Null property
    }
}