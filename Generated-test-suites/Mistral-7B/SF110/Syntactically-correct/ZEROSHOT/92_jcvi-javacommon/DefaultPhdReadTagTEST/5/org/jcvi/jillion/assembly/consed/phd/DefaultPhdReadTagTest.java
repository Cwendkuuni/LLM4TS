package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.*;
import org.jcvi.jillion.test.RangeTest;
import org.jcvi.jillion.test.TestDate;
import org.jcvi.jillion.test.TestString;
import org.jcvi.jillion.test.testutils.JillionTest;
import org.junit.Test;

public class DefaultPhdReadTagTest extends JillionTest {

    @Test
    public void testDefaultPhdReadTagConstruction() {
        String type = TestString.create("type");
        String source = TestString.create("source");
        Range ungappedRange = RangeTest.create(1, 10);
        Date date = new TestDate(2022, 1, 1);
        String comment = TestString.create("comment");
        String freeFormData = TestString.create("freeFormData");

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
        String type = TestString.create("type");
        String source = TestString.create("source");
        Range ungappedRange = RangeTest.create(1, 10);
        Date date = new TestDate(2022, 1, 1);
        String comment = TestString.create("comment");
        String freeFormData = TestString.create("freeFormData");

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        int hashcode1 = phdReadTag1.hashCode();
        int hashcode2 = phdReadTag2.hashCode();

        assertEquals(hashcode1, hashcode2);
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type1 = TestString.create("type1");
        String type2 = TestString.create("type2");
        String source = TestString.create("source");
        Range ungappedRange = RangeTest.create(1, 10);
        Date date = new TestDate(2022, 1, 1);
        String comment1 = TestString.create("comment1");
        String comment2 = TestString.create("comment2");
        String freeFormData1 = TestString.create("freeFormData1");
        String freeFormData2 = TestString.create("freeFormData2");

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source, ungappedRange, date, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source, ungappedRange, date, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source, ungappedRange, date, comment2, freeFormData1);
        DefaultPhdReadTag phdReadTag4 = new DefaultPhdReadTag(type1, source, ungappedRange, date, comment1, freeFormData2);

        assertTrue(phdReadTag1.equals(phdReadTag1));
        assertTrue(phdReadTag1.equals(phdReadTag2));
        assertFalse(phdReadTag1.equals(phdReadTag3));
        assertFalse(phdReadTag1.equals(phdReadTag4));
    }
}