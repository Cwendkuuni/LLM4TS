package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.*;
import org.junit.Test;
import org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testHashCodeEquals() {
        // ... test case from Expert 2 ...
    }

    @Test
    public void testGetters() {
        // ... test case from Expert 3 ...
    }

    @Test
    public void testDefaultPhdReadTagConstruction() {
        // ... test case from Expert 1 ...
    }

    @Test
    public void testEmptyValues() {
        String type = null;
        String source = "";
        Range ungappedRange = null;
        Date date = null;
        String comment = null;
        String freeFormData = "";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        if (type != null) {
            assertEquals(type, tag.getType());
        } else {
            assertNull(tag.getType());
        }

        if (!source.isEmpty()) {
            assertEquals(source, tag.getSource());
        } else {
            assertNull(tag.getSource());
        }

        if (ungappedRange != null) {
            assertEquals(ungappedRange, tag.getUngappedRange());
        } else {
            assertNull(tag.getUngappedRange());
        }

        if (date != null) {
            assertEquals(date, tag.getDate());
        } else {
            assertNull(tag.getDate());
        }

        if (comment != null) {
            assertEquals(comment, tag.getComment());
        } else {
            assertNull(tag.getComment());
        }

        if (!freeFormData.isEmpty()) {
            assertEquals(freeFormData, tag.getFreeFormData());
        } else {
            assertNull(tag.getFreeFormData());
        }
    }
}