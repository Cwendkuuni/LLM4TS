package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.*;
import org.jcvi.jillion.core.range.Range;
import org.jcvi.jillion.core.util.DateUtil;
import org.junit.Before;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    private DefaultPhdReadTag tag1;
    private DefaultPhdReadTag tag2;
    private Date date;
    private Range range;

    @Before
    public void setUp() {
        String type = "type1";
        String source = "source1";
        date = DateUtil.parseDate("2022-01-01");
        range = new Range(1, 10);
        String comment = "comment1";
        String freeFormData = "freeFormData1";
        tag1 = new DefaultPhdReadTag(type, source, range, date, comment, freeFormData);
        String type2 = "type2";
        tag2 = new DefaultPhdReadTag(type2, source, range, date, comment, freeFormData);
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
        assertEquals(tag1.getUngappedRange(), range);
    }

    @Test
    public void testGetDate() {
        assertEquals(tag1.getDate(), date);
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
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag("type3", "source2", range, date, null, null);
        int expectedHashCode = 31 * 31 * 1 + ("type1".hashCode()) + ("source1".hashCode()) + range.hashCode() + date.hashCode() + ("comment1".hashCode()) + ("freeFormData1".hashCode());
        int actualHashCode = tag1.hashCode();
        int actualHashCode2 = tag1.hashCode();
        int actualHashCode3 = tag3.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
        assertEquals(expectedHashCode, actualHashCode2);
        assertNotEquals(expectedHashCode, actualHashCode3);
    }

    @Test
    public void testEquals() {
        assertTrue(tag1.equals(tag1));
        assertTrue(tag1.equals(tag2));
        assertFalse(tag1.equals(null));
        assertFalse(tag1.equals("String"));
        assertFalse(tag1.equals(new Object()));
        assertFalse(tag1.equals(new DefaultPhdReadTag("type1", "source2", range, date, comment, freeFormData)));
    }
}