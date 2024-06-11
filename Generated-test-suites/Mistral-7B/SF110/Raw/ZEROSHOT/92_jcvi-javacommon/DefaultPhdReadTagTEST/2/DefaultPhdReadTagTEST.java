package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.*;
import org.jcvi.jillion.test.TestUtils;
import org.jcvi.jillion.util.Ranges;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class DefaultPhdReadTagTest {

    DefaultPhdReadTag tag1;
    DefaultPhdReadTag tag2;

    @Before
    public void setUp() {
        String type1 = "type1";
        String source1 = "source1";
        Range ungappedRange1 = Ranges.range(1, 10);
        Date date1 = Date.ofInstant(Instant.now(), "UTC");
        String comment1 = "comment1";
        String freeFormData1 = "freeFormData1";

        tag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "type2";
        String source2 = "source2";
        Range ungappedRange2 = Ranges.range(5, 15);
        Date date2 = Date.ofInstant(Instant.now(), "UTC");
        String comment2 = "comment2";
        String freeFormData2 = "freeFormData2";

        tag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);
    }

    @Test
    public void testGetType() {
        assertEquals(tag1.getType(), "type1");
    }

    @Test
    public void testGetSource() {
        assertEquals(tag1.getSource(), "source1");
    }

    @Test
    public void testGetUngappedRange() {
        assertEquals(tag1.getUngappedRange(), Ranges.range(1, 10));
    }

    @Test
    public void testGetDate() {
        assertEquals(tag1.getDate(), Date.ofInstant(Instant.now(), "UTC"));
    }

    @Test
    public void testGetComment() {
        assertEquals(tag1.getComment(), "comment1");
    }

    @Test
    public void testGetFreeFormData() {
        assertEquals(tag1.getFreeFormData(), "freeFormData1");
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        assertEquals(tag1.hashCode(), tag3.hashCode());

        tag3 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);
        assertNotEquals(tag1.hashCode(), tag3.hashCode());

        String comment3 = "comment3";
        tag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment3, freeFormData1);
        assertNotEquals(tag1.hashCode(), tag3.hashCode());

        Date date3 = Date.ofInstant(Instant.now().minusMillis(100), "UTC");
        tag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date3, comment1, freeFormData1);
        assertNotEquals(tag1.hashCode(), tag3.hashCode());

        Range range3 = Ranges.range(2, 11);
        tag3 = new DefaultPhdReadTag(type1, source1, range3, date1, comment1, freeFormData1);
        assertNotEquals(tag1.hashCode(), tag3.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        assertTrue(tag1.equals(tag3));
        assertTrue(tag3.equals(tag1));

        tag3 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);
        assertFalse(tag1.equals(tag3));
        assertFalse(tag3.equals(tag1));

        tag3 = new DefaultPhdReadTag(type1, null, ungappedRange1, date1, comment1, freeFormData1);
        assertFalse(tag1.equals(tag3));
        assertFalse(tag3.equals(tag1));

        tag3 = new DefaultPhdReadTag(type1, source1, null, date1, comment1, freeFormData1);
        assertFalse(tag1.equals(tag3));
        assertFalse(tag3.equals(tag1));

        tag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, null, comment1, freeFormData1);
        assertFalse(tag1.equals(tag3));
        assertFalse(tag3.equals(tag1));

        tag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, null, freeFormData1);
        assertFalse(tag1.equals(tag3));
        assertFalse(tag3.equals(tag1));

        tag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, null);
        assertFalse(tag1.equals(tag3));
        assertFalse(tag3.equals(tag1));
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(tag1.equals(null));
        assertFalse(null.equals(tag1));
    }

    @Test
    public void testToString() {
        String expected = "[DefaultPhdReadTag type=type1 source=source1 range=[1, 10] date=" + tag1.getDate() + " comment=comment1 freeFormData=freeFormData1]";
        assertEquals(expected, tag1.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullType() {
        new DefaultPhdReadTag(null, source1, ungappedRange1, date1, comment1, freeFormData1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSource() {
        new DefaultPhdReadTag(type1, null, ungappedRange1, date1, comment1, freeFormData1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullUngappedRange() {
        new DefaultPhdReadTag(type1, source1, null, date1, comment1, freeFormData1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullDate() {
        new DefaultPhdReadTag(type1, source1, ungappedRange1, null, comment1, freeFormData1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullComment() {
        new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, null, freeFormData1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullFreeFormData() {
        new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, null);
    }

    @Test
    public void testCreateList() {
        List<DefaultPhdReadTag> tags = Arrays.asList(tag1, tag2);
        List<DefaultPhdReadTag> expected = Arrays.asList(tag1, tag2);
        assertEquals(expected, tags);
    }
}