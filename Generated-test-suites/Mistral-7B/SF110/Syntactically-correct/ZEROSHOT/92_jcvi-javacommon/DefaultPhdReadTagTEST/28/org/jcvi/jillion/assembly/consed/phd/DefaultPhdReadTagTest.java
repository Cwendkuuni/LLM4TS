package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.Date;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.test.TestUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest
{
    private DefaultPhdReadTag testTag;

    @Before
    public void setup()
    {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(0, 10);
        Date date = TestUtils.getDate("2022-01-01");
        String comment = "comment";
        String freeFormData = "freeFormData";

        testTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test
    public void testDefaultPhdReadTagConstruction()
    {
        assertEquals("type", testTag.getType());
        assertEquals("source", testTag.getSource());
        assertEquals(0, testTag.getUngappedRange().getStart());
        assertEquals(10, testTag.getUngappedRange().getEnd());
        assertEquals(TestUtils.getDate("2022-01-01"), testTag.getDate());
        assertEquals("comment", testTag.getComment());
        assertEquals("freeFormData", testTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagGetters()
    {
        assertEquals("type", testTag.getType());
        assertEquals("source", testTag.getSource());
        assertEquals(0, testTag.getUngappedRange().getStart());
        assertEquals(10, testTag.getUngappedRange().getEnd());
        assertEquals(TestUtils.getDate("2022-01-01"), testTag.getDate());
        assertEquals("comment", testTag.getComment());
        assertEquals("freeFormData", testTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagHashCode()
    {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", null, null, null, null);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type2", "source2", null, null, null, null);

        DefaultPhdReadTag tag3 = new DefaultPhdReadTag("type", "source", new Range(5, 10), TestUtils.getDate("2022-01-02"), "comment", "freeFormData");

        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();
        int hashCode3 = tag3.hashCode();

        int expectedHashCode1 = 31 * 31 * 1 + "type1".hashCode() + "source1".hashCode();
        int expectedHashCode2 = 31 * 31 * 1 + "type2".hashCode() + "source2".hashCode();
        int expectedHashCode3 = 31 * 31 * 1 + "type".hashCode() + "source".hashCode() + 5 * 31 + 10 * 31 + TestUtils.getDate("2022-01-02").hashCode() + "comment".hashCode() + "freeFormData".hashCode();

        assertEquals(expectedHashCode1, hashCode1);
        assertEquals(expectedHashCode2, hashCode2);
        assertEquals(expectedHashCode3, hashCode3);
    }

    @Test
    public void testDefaultPhdReadTagEquals()
    {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", null, null, null, null);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", null, null, null, null);
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag("type2", "source2", null, null, null, null);
        DefaultPhdReadTag tag4 = new DefaultPhdReadTag("type", "source", new Range(5, 10), TestUtils.getDate("2022-01-02"), "comment", "freeFormData");

        assertTrue(tag1.equals(tag1));
        assertTrue(tag1.equals(tag2));
        assertFalse(tag1.equals(tag3));
        assertFalse(tag1.equals(tag4));
    }
}