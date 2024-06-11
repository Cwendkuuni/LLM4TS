package org.jcvi.jillion.assembly.consed.phd.test;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.*;
import org.junit.Test;

import java.util.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagInstance() {
        String type = "protein";
        String source = "NC_012345.1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "This is a comment";
        String freeFormData = "some data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals("protein", phdReadTag.getType());
        assertEquals("NC_012345.1", phdReadTag.getSource());
        assertEquals(new Range(1, 10), phdReadTag.getUngappedRange());
        assertEquals("2022-01-01", phdReadTag.getDate().toString());
        assertEquals("This is a comment", phdReadTag.getComment());
        assertEquals("some data", phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagHashCode() {
        String type = "protein";
        String source = "NC_012345.1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "This is a comment";
        String freeFormData = "some data";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type = "protein";
        String source = "NC_012345.1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "This is a comment";
        String freeFormData = "some data";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertTrue(phdReadTag1.equals(phdReadTag2));

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag("protein", source, ungappedRange, date, comment, null);

        assertFalse(phdReadTag1.equals(phdReadTag3));
    }

    @Test
    public void testDefaultPhdReadTagNullValues() {
        String type = null;
        String source = null;
        Range ungappedRange = null;
        Date date = null;
        String comment = null;
        String freeFormData = null;

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(null, phdReadTag.getType());
        assertEquals(null, phdReadTag.getSource());
        assertEquals(null, phdReadTag.getUngappedRange());
        assertEquals(null, phdReadTag.getDate());
        assertEquals(null, phdReadTag.getComment());
        assertEquals(null, phdReadTag.getFreeFormData());
    }
}