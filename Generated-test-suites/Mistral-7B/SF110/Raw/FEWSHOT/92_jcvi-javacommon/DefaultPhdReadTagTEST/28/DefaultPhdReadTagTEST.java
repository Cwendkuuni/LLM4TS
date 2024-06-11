package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagWithValidArguments() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 12, 31);
        String comment = "comment";
        String freeFormData = "freeFormData";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());

        assertEquals(phdReadTag.hashCode(), phdReadTag.hashCode());
        assertTrue(phdReadTag.equals(phdReadTag));
    }

    @Test
    public void testDefaultPhdReadTagWithNullType() {
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 12, 31);
        String comment = "comment";
        String freeFormData = "freeFormData";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);

        assertNull(phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());

        int hashCode = phdReadTag.hashCode();
        DefaultPhdReadTag anotherPhdReadTag = new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);
        assertTrue(phdReadTag.equals(anotherPhdReadTag));
        assertEquals(hashCode, anotherPhdReadTag.hashCode());
    }

    @Test
    public void testDefaultPhdReadTagWithNullSource() {
        String type = "type";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 12, 31);
        String comment = "comment";
        String freeFormData = "freeFormData";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertNull(phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());

        int hashCode = phdReadTag.hashCode();
        DefaultPhdReadTag anotherPhdReadTag = new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
        assertTrue(phdReadTag.equals(anotherPhdReadTag));
        assertEquals(hashCode, anotherPhdReadTag.hashCode());
    }

    @Test
    public void testDefaultPhdReadTagWithNullUngappedRange() {
        String type = "type";
        String source = "source";
        Date date = new Date(2022, 12, 31);
        String comment = "comment";
        String freeFormData = "freeFormData";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertNull(phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());

        int hashCode = phdReadTag.hashCode();
        DefaultPhdReadTag anotherPhdReadTag = new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
        assertTrue(phdReadTag.equals(anotherPhdReadTag));
        assertEquals(hashCode, anotherPhdReadTag.hashCode());
    }

    @Test
    public void testDefaultPhdReadTagWithNullDate() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        String comment = "comment";
        String freeFormData = "freeFormData";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertNull(phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());

        int hashCode = phdReadTag.hashCode();
        DefaultPhdReadTag anotherPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
        assertTrue(phdReadTag.equals(anotherPhdReadTag));
        assertEquals(hashCode, anotherPhdReadTag.hashCode());
    }

    @Test
    public void testDefaultPhdReadTagWithNullComment() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 12, 31);
        String freeFormData = "freeFormData";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertNull(phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());

        int hashCode = phdReadTag.hashCode();
        DefaultPhdReadTag anotherPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);
        assertTrue(phdReadTag.equals(anotherPhdReadTag));
        assertEquals(hashCode, anotherPhdReadTag.hashCode());
    }

    @Test
    public void testDefaultPhdReadTagWithNullFreeFormData() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 12, 31);
        String comment = "comment";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertNull(phdReadTag.getFreeFormData());

        int hashCode = phdReadTag.hashCode();
        DefaultPhdReadTag anotherPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);
        assertTrue(phdReadTag.equals(anotherPhdReadTag));
        assertEquals(hashCode, anotherPhdReadTag.hashCode());
    }
}