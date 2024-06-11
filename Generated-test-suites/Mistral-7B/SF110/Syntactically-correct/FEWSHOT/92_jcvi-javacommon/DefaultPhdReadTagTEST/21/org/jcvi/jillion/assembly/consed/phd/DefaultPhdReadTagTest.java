package org.jcvi.jillion.assembly.consed.phd;

import org.junit.Assert;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.util.DateUtils;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagWithValidArguments() {
        String type = "TYPE";
        String source = "SOURCE";
        Range ungappedRange = new Range(1, 10);
        Date date = DateUtils.parseDate("2010-01-01", "yyyy-MM-dd");
        String comment = "COMMENT";
        String freeFormData = "FREE_FORM_DATA";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(type, phdReadTag.getType());
        Assert.assertEquals(source, phdReadTag.getSource());
        Assert.assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        Assert.assertEquals(date, phdReadTag.getDate());
        Assert.assertEquals(comment, phdReadTag.getComment());
        Assert.assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagWithNullArguments() {
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
    public void testDefaultPhdReadTagWithEmptyArguments() {
        String type = "";
        String source = "";
        Range ungappedRange = new Range(0, 0);
        Date date = new Date(0L);
        String comment = "";
        String freeFormData = "";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals("", phdReadTag.getType());
        Assert.assertEquals("", phdReadTag.getSource());
        Assert.assertEquals(new Range(0, 0), phdReadTag.getUngappedRange());
        Assert.assertEquals(new Date(0L), phdReadTag.getDate());
        Assert.assertEquals("", phdReadTag.getComment());
        Assert.assertEquals("", phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagWithInvalidArguments() {
        String type = "TYPE";
        String source = null;
        Range ungappedRange = null;
        Date date = new Date(-1L);
        String comment = "";
        String freeFormData = null;

        Assert.assertThrows(NullPointerException.class, () -> {
            new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        });

        source = "SOURCE";
        ungappedRange = null;

        Assert.assertThrows(NullPointerException.class, () -> {
            new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        });

        ungappedRange = new Range(1, 10);
        date = new Date(-1L);

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        });

        date = new Date(0L);
        comment = null;

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);

        Assert.assertEquals(type, phdReadTag.getType());
        Assert.assertEquals(source, phdReadTag.getSource());
        Assert.assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        Assert.assertEquals(date, phdReadTag.getDate());
        Assert.assertNull(phdReadTag.getComment());
        Assert.assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagHashCode() {
        String type1 = "TYPE1";
        String type2 = "TYPE2";
        String source1 = "SOURCE1";
        String source2 = "SOURCE2";
        Range ungappedRange1 = new Range(1, 10);
        Range ungappedRange2 = new Range(2, 20);
        Date date1 = DateUtils.parseDate("2010-01-01", "yyyy-MM-dd");
        Date date2 = DateUtils.parseDate("2020-01-01", "yyyy-MM-dd");
        String comment1 = "COMMENT1";
        String comment2 = "COMMENT2";
        String freeFormData1 = "FREE_FORM_DATA1";
        String freeFormData2 = "FREE_FORM_DATA2";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        Assert.assertEquals(hashCode1, phdReadTag3.hashCode());
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type1 = "TYPE1";
        String type2 = "TYPE2";
        String source1 = "SOURCE1";
        String source2 = "SOURCE2";
        Range ungappedRange1 = new Range(1, 10);
        Range ungappedRange2 = new Range(2, 20);
        Date date1 = DateUtils.parseDate("2010-01-01", "yyyy-MM-dd");
        Date date2 = DateUtils.parseDate("2020-01-01", "yyyy-MM-dd");
        String comment1 = "COMMENT1";
        String comment2 = "COMMENT2";
        String freeFormData1 = "FREE_FORM_DATA1";
        String freeFormData2 = "FREE_FORM_DATA2";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);
        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        Assert.assertFalse(phdReadTag1.equals(phdReadTag2));
        Assert.assertTrue(phdReadTag1.equals(phdReadTag3));
    }

    @Test
    public void testDefaultPhdReadTagEqualsWithNull() {
        String type = "TYPE";
        String source = "SOURCE";
        Range ungappedRange = new Range(1, 10);
        Date date = DateUtils.parseDate("2010-01-01", "yyyy-MM-dd");
        String comment = "COMMENT";
        String freeFormData = "FREE_FORM_DATA";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertFalse(phdReadTag.equals(null));
        Assert.assertFalse(phdReadTag.equals(""));
    }

    @Test
    public void testDefaultPhdReadTagEqualsWithDifferentClass() {
        String type = "TYPE";
        String source = "SOURCE";
        Range ungappedRange = new Range(1, 10);
        Date date = DateUtils.parseDate("2010-01-01", "yyyy-MM-dd");
        String comment = "COMMENT";
        String freeFormData = "FREE_FORM_DATA";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertFalse(phdReadTag.equals(new Object()));
    }
}