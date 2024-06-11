package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.Date;
import org.jcvi.jillion.core.Range;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstruction() {
        String type = "DNA";
        String source = "Contig_1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(Instant.now());
        String comment = "My Comment";
        String freeFormData = "Some free form data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(type, phdReadTag.getType());
        Assert.assertEquals(source, phdReadTag.getSource());
        Assert.assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        Assert.assertEquals(date, phdReadTag.getDate());
        Assert.assertEquals(comment, phdReadTag.getComment());
        Assert.assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type1 = "DNA";
        String source1 = "Contig_1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date(Instant.now());
        String comment1 = "My Comment";
        String freeFormData1 = "Some free form data";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "Protein";
        String source2 = "Contig_1";
        Range ungappedRange2 = new Range(1, 10);
        Date date2 = new Date(Instant.now());
        String comment2 = "My Comment";
        String freeFormData2 = "Some free form data";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        Assert.assertFalse(phdReadTag1.equals(phdReadTag2));
        Assert.assertTrue(phdReadTag1.equals(phdReadTag3));
    }

    @Test
    public void testDefaultPhdReadTagHashCode() {
        String type = "DNA";
        String source = "Contig_1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(Instant.now());
        String comment = "My Comment";
        String freeFormData = "Some free form data";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testDefaultPhdReadTagGetters() {
        String type = "DNA";
        String source = "Contig_1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(Instant.now());
        String comment = "My Comment";
        String freeFormData = "Some free form data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(type, phdReadTag.getType());
        Assert.assertEquals(source, phdReadTag.getSource());
        Assert.assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        Assert.assertEquals(date, phdReadTag.getDate());
        Assert.assertEquals(comment, phdReadTag.getComment());
        Assert.assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagWithNullValues() {
        String type = null;
        String source = null;
        Range ungappedRange = null;
        Date date = null;
        String comment = null;
        String freeFormData = null;

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertNull(phdReadTag.getType());
        Assert.assertNull(phdReadTag.getSource());
        Assert.assertNull(phdReadTag.getUngappedRange());
        Assert.assertNull(phdReadTag.getDate());
        Assert.assertNull(phdReadTag.getComment());
        Assert.assertNull(phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagWithEmptyValues() {
        String type = "";
        String source = "";
        Range ungappedRange = new Range();
        Date date = new Date();
        String comment = "";
        String freeFormData = "";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals("", phdReadTag.getType());
        Assert.assertEquals("", phdReadTag.getSource());
        Assert.assertNotNull(phdReadTag.getUngappedRange());
        Assert.assertNotNull(phdReadTag.getDate());
        Assert.assertEquals("", phdReadTag.getComment());
        Assert.assertEquals("", phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagWithEmptyUngappedRange() {
        String type = "DNA";
        String source = "Contig_1";
        Range ungappedRange = new Range();
        Date date = new Date(Instant.now());
        String comment = "My Comment";
        String freeFormData = "Some free form data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(type, phdReadTag.getType());
        Assert.assertEquals(source, phdReadTag.getSource());
        Assert.assertNotNull(phdReadTag.getUngappedRange());
        Assert.assertEquals(0, phdReadTag.getUngappedRange().getLength());
        Assert.assertEquals(date, phdReadTag.getDate());
        Assert.assertEquals(comment, phdReadTag.getComment());
        Assert.assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }
}