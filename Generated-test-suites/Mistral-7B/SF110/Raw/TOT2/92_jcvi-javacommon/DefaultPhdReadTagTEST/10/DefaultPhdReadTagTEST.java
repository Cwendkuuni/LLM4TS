package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.*;
import org.junit.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testgetType() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("DNA", new Range(1, 10), new Date(), "", "", "");
        assertEquals("DNA", phdReadTag.getType());
    }

    @Test
    public void testgetSource() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("DNA", new Range(1, 10), new Date(), "", "", "");
        assertEquals("", phdReadTag.getSource());
    }

    @Test(expected = NullPointerException.class)
    public void testgetSourceNull() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("DNA", new Range(1, 10), new Date(), "", null, "");
    }

    @Test
    public void testgetUngappedRange() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("DNA", new Range(1, 10), new Date(), "", "", "");
        Range expectedRange = new Range(1, 10);
        assertEquals(expectedRange, phdReadTag.getUngappedRange());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testgetUngappedRangeInvalidRange() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("DNA", new Range(0, 1), new Date(), "", "", "");
    }

    @Test
    public void testgetDate() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("DNA", new Range(1, 10), new Date(), "", "", "");
        assertEquals(new Date(), phdReadTag.getDate());
    }

    @Test(expected = NullPointerException.class)
    public void testgetDateNull() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("DNA", new Range(1, 10), null, "", "", "");
    }

    @Test
    public void testgetComment() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("DNA", new Range(1, 10), new Date(), "", "", "");
        assertEquals("", phdReadTag.getComment());
    }

    @Test
    public void testgetCommentEmptyString() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("DNA", new Range(1, 10), new Date(), "", "   ", "");
        assertEquals("", phdReadTag.getComment());
    }

    @Test(expected = NullPointerException.class)
    public void testgetCommentNull() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("DNA", new Range(1, 10), new Date(), "", null, "");
    }

    @Test
    public void testgetFreeFormData() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("DNA", new Range(1, 10), new Date(), "", "", "");
        assertEquals("", phdReadTag.getFreeFormData());
    }

    @Test
    public void testgetFreeFormDataEmptyString() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("DNA", new Range(1, 10), new Date(), "", "   ", "");
        assertEquals("", phdReadTag.getFreeFormData());
    }

    @Test(expected = NullPointerException.class)
    public void testgetFreeFormDataNull() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("DNA", new Range(1, 10), new Date(), "", null, "");
    }
}