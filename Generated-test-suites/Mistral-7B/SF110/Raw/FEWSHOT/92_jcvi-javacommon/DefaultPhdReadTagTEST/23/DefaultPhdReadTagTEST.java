package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.*;
import org.jcvi.jillion.core.util.Ranges;
import org.jcvi.jillion.core.util.DateUtils;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagWithValidArgs() {
        String type = "dna_read";
        String source = "R1_001";
        Range ungappedRange = Ranges.create(0, 100);
        Date date = DateUtils.parseDate("2022-01-01");
        String comment = "read from plate 1, lane 1";
        String freeFormData = "read length: 101 bp";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, tag.getType());
        assertEquals(source, tag.getSource());
        assertEquals(ungappedRange, tag.getUngappedRange());
        assertEquals(date, tag.getDate());
        assertEquals(comment, tag.getComment());
        assertEquals(freeFormData, tag.getFreeFormData());

        assertEquals(type.hashCode(), tag.hashCode());
        assertTrue(tag.equals(new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData)));
    }

    @Test
    public void testDefaultPhdReadTagWithNullArgs() {
        String type = null;
        String source = null;
        Range ungappedRange = null;
        Date date = null;
        String comment = null;
        String freeFormData = null;

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertNull(tag.getType());
        assertNull(tag.getSource());
        assertNull(tag.getUngappedRange());
        assertNull(tag.getDate());
        assertNull(tag.getComment());
        assertNull(tag.getFreeFormData());

        assertEquals(0, tag.hashCode());
        assertFalse(tag.equals(null));
        assertFalse(tag.equals(new DefaultPhdReadTag("dna_read", source, ungappedRange, date, comment, freeFormData)));
    }

    @Test
    public void testDefaultPhdReadTagWithEmptyArgs() {
        String type = "";
        String source = "";
        Range ungappedRange = Ranges.create(0, 0);
        Date date = null;
        String comment = "";
        String freeFormData = "";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals("", tag.getType());
        assertEquals("", tag.getSource());
        assertEquals(Ranges.create(0, 0), tag.getUngappedRange());
        assertNull(tag.getDate());
        assertEquals("", tag.getComment());
        assertEquals("", tag.getFreeFormData());

        assertEquals(0, tag.hashCode());
        assertTrue(tag.equals(new DefaultPhdReadTag("", "", Ranges.create(0, 0), null, "", "")));
    }

    @Test
    public void testDefaultPhdReadTagWithInvalidArgs() {
        String type = "dna_read";
        String source = null;
        Range ungappedRange = null;
        Date date = null;
        String comment = null;
        String freeFormData = null;

        assertFalse(new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData) instanceof DefaultPhdReadTag);
    }

    @Test
    public void testDefaultPhdReadTagWithInvalidUngappedRange() {
        String type = "dna_read";
        String source = "R1_001";
        Range ungappedRange = null;
        Date date = null;
        String comment = null;
        String freeFormData = null;

        assertFalse(new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData) instanceof DefaultPhdReadTag);

        ungappedRange = Ranges.create(-1, 100);
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertFalse(tag.getUngappedRange().isDefined());
    }

    @Test
    public void testDefaultPhdReadTagWithInvalidDate() {
        String type = "dna_read";
        String source = "R1_001";
        Range ungappedRange = Ranges.create(0, 100);
        Date date = new Date("Tue Jan 01 1900 01:23:45 GMT"); // Invalid date
        String comment = null;
        String freeFormData = null;

        assertFalse(new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData) instanceof DefaultPhdReadTag);
    }
}