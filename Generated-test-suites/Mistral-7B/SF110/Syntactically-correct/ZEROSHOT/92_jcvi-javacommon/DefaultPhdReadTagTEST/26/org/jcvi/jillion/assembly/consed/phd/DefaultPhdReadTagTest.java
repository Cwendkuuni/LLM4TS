package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.*;
import org.jcvi.jillion.test.TestUtils;
import org.junit.Test;

import java.util.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstructionWithValidArguments() {
        String type = "myType";
        String source = "mySource";
        Range ungappedRange = new DefaultRange(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "myComment";
        String freeFormData = "myFreeFormData";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagHashCode() {
        String type = "myType";
        String source = "mySource";
        Range ungappedRange = new DefaultRange(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "myComment";
        String freeFormData = "myFreeFormData";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        String type2 = "anotherType";
        String source2 = "anotherSource";
        Range ungappedRange2 = new DefaultRange(5, 15);
        Date date2 = new Date(2023, 2, 2);
        String comment2 = "anotherComment";
        String freeFormData2 = "anotherFreeFormData";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        int hashCodePhdReadTag1 = phdReadTag1.hashCode();
        int hashCodePhdReadTag2 = phdReadTag2.hashCode();

        assertNotEquals(hashCodePhdReadTag1, hashCodePhdReadTag2);

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        int hashCodePhdReadTag1_2 = phdReadTag1.hashCode();

        assertEquals(hashCodePhdReadTag1_2, phdReadTag1.hashCode());
        assertEquals(hashCodePhdReadTag1, phdReadTag3.hashCode());
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type = "myType";
        String source = "mySource";
        Range ungappedRange = new DefaultRange(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "myComment";
        String freeFormData = "myFreeFormData";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        String type2 = "anotherType";
        String source2 = "anotherSource";
        Range ungappedRange2 = new DefaultRange(5, 15);
        Date date2 = new Date(2023, 2, 2);
        String comment2 = "anotherComment";
        String freeFormData2 = "anotherFreeFormData";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        assertFalse(phdReadTag1.equals(phdReadTag2));

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertTrue(phdReadTag1.equals(phdReadTag3));

        DefaultPhdReadTag phdReadTag4 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);

        assertFalse(phdReadTag1.equals(phdReadTag4));
    }

    @Test
    public void testDefaultPhdReadTagGetters() {
        String type = "myType";
        String source = "mySource";
        Range ungappedRange = new DefaultRange(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "myComment";
        String freeFormData = "myFreeFormData";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagDefaultConstructor() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag();

        assertNull(phdReadTag.getType());
        assertNull(phdReadTag.getSource());
        assertNull(phdReadTag.getUngappedRange());
        assertNull(phdReadTag.getDate());
        assertNull(phdReadTag.getComment());
        assertNull(phdReadTag.getFreeFormData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullType() {
        String source = "mySource";
        Range ungappedRange = new DefaultRange(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "myComment";
        String freeFormData = "myFreeFormData";

        new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullSource() {
        String type = "myType";
        Range ungappedRange = new DefaultRange(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "myComment";
        String freeFormData = "myFreeFormData";

        new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullUngappedRange() {
        String type = "myType";
        String source = "mySource";
        Date date = new Date(2022, 1, 1);
        String comment = "myComment";
        String freeFormData = "myFreeFormData";

        new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullDate() {
        String type = "myType";
        String source = "mySource";
        Range ungappedRange = new DefaultRange(1, 10);
        String comment = "myComment";
        String freeFormData = "myFreeFormData";

        new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullComment() {
        String type = "myType";
        String source = "mySource";
        Range ungappedRange = new DefaultRange(1, 10);
        Date date = new Date(2022, 1, 1);
        String freeFormData = "myFreeFormData";

        new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullFreeFormData() {
        String type = "myType";
        String source = "mySource";
        Range ungappedRange = new DefaultRange(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "myComment";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);
    }
}