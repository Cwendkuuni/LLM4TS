package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.util.JiónUtil;
import org.jcvi.jillion.core.util.RangeUtil;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstructorGivenValidArguments() {
        String type = "dna";
        String source = "r1.fastq";
        Range ungappedRange = new Range(1, 100);
        Date date = mock(Date.class);
        String comment = "comment1";
        String freeFormData = "data1";
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
        String type = "dna";
        String source = "r1.fastq";
        Range ungappedRange = new Range(1, 100);
        Date date = mock(Date.class);
        String comment = "comment1";
        String freeFormData = "data1";
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(phdReadTag1.hashCode(), phdReadTag2.hashCode());

        String type2 = "rna";
        Date date2 = mock(Date.class);
        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type2, source, ungappedRange, date2, comment, freeFormData);
        assertNotSame(phdReadTag1.hashCode(), phdReadTag3.hashCode());

        String comment2 = "comment2";
        DefaultPhdReadTag phdReadTag4 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment2, freeFormData);
        assertNotSame(phdReadTag1.hashCode(), phdReadTag4.hashCode());

        Range ungappedRange2 = new Range(101, 200);
        DefaultPhdReadTag phdReadTag5 = new DefaultPhdReadTag(type, source, ungappedRange2, date, comment, freeFormData);
        assertNotSame(phdReadTag1.hashCode(), phdReadTag5.hashCode());
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type = "dna";
        String source = "r1.fastq";
        Range ungappedRange = new Range(1, 100);
        Date date = mock(Date.class);
        String comment = "comment1";
        String freeFormData = "data1";
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(phdReadTag1, phdReadTag2);

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
        assertNotSame(phdReadTag1, phdReadTag3);

        DefaultPhdReadTag phdReadTag4 = new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
        assertNotSame(phdReadTag1, phdReadTag4);

        DefaultPhdReadTag phdReadTag5 = new DefaultPhdReadTag(type2, source, ungappedRange, date, comment, freeFormData);
        assertNotSame(phdReadTag1, phdReadTag5);

        DefaultPhdReadTag phdReadTag6 = new DefaultPhdReadTag(type, source, ungappedRange2, date, comment, freeFormData);
        assertNotSame(phdReadTag1, phdReadTag6);

        DefaultPhdReadTag phdReadTag7 = new DefaultPhdReadTag(type, source, ungappedRange, null, comment2, freeFormData);
        assertNotSame(phdReadTag1, phdReadTag7);

        DefaultPhdReadTag phdReadTag8 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);
        assertNotSame(phdReadTag1, phdReadTag8);

        DefaultPhdReadTag phdReadTag9 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData2);
        assertNotSame(phdReadTag1, phdReadTag9);
    }

    @Test
    public void testDefaultPhdReadTagEqualsGivenNullObject() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("dna", "r1.fastq", null, null, null, null);
        assertNotSame(phdReadTag, null);
    }

    @Test
    public void testDefaultPhdReadTagEqualsGivenObjectOfAnotherClass() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("dna", "r1.fastq", null, null, null, null);
        Object anotherObject = new Object();
        assertNotSame(phdReadTag, anotherObject);
    }

    @Test
    public void testGetType() {
        String type = "dna";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, null, null, null, null, null);
        assertEquals(type, phdReadTag.getType());
    }

    @Test
    public void testGetSource() {
        String source = "r1.fastq";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, source, null, null, null, null);
        assertEquals(source, phdReadTag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        int start = 1;
        int end = 100;
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, new Range(start, end), null, null, null);
        Range ungappedRange = RangeUtil.createClosedRange(start, end);
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        Date date = mock(Date.class);
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, date, null, null);
        assertEquals(date, phdReadTag.getDate());
    }

    @Test
    public void testGetComment() {
        String comment = "comment1";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, null, comment, null);
        assertEquals(comment, phdReadTag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        String freeFormData = "data1";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, null, null, freeFormData);
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }
}