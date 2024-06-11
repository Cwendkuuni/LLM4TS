package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;

import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.util.DateUtils;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagWithValidArguments() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = DateUtils.parseDate("2022-01-01");
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
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

        assertNull(phdReadTag.getType());
        assertNull(phdReadTag.getSource());
        assertNull(phdReadTag.getUngappedRange());
        assertNull(phdReadTag.getDate());
        assertNull(phdReadTag.getComment());
        assertNull(phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagHashCode() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = DateUtils.parseDate("2022-01-01");
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        String type1 = "type1";
        String source1 = "source1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = DateUtils.parseDate("2022-01-01");
        String comment1 = "comment1";
        String freeFormData1 = "freeFormData1";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = DateUtils.parseDate("2022-01-01");
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertTrue(phdReadTag1.equals(phdReadTag2));

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag3));

        DefaultPhdReadTag phdReadTag4 = new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag4));

        DefaultPhdReadTag phdReadTag5 = new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag5));

        DefaultPhdReadTag phdReadTag6 = new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag6));

        DefaultPhdReadTag phdReadTag7 = new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag7));

        DefaultPhdReadTag phdReadTag8 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);

        assertFalse(phdReadTag1.equals(phdReadTag8));

        DefaultPhdReadTag phdReadTag9 = new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);

        assertFalse(phdReadTag1.equals(phdReadTag9));

        DefaultPhdReadTag phdReadTag10 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        phdReadTag10.setFreeFormData("freeFormData1");

        assertFalse(phdReadTag1.equals(phdReadTag10));

        DefaultPhdReadTag phdReadTag11 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        phdReadTag11.setUngappedRange(new Range(11, 11));

        assertFalse(phdReadTag1.equals(phdReadTag11));

        DefaultPhdReadTag phdReadTag12 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        phdReadTag12.setComment("comment1");

        assertFalse(phdReadTag1.equals(phdReadTag12));

        DefaultPhdReadTag phdReadTag13 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        phdReadTag13.setType("type1");

        assertFalse(phdReadTag1.equals(phdReadTag13));

        DefaultPhdReadTag phdReadTag14 = new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
        phdReadTag14.setSource("source1");

        assertFalse(phdReadTag1.equals(phdReadTag14));

        DefaultPhdReadTag phdReadTag15 = new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
        phdReadTag15.setDate(DateUtils.parseDate("2022-01-02"));

        assertFalse(phdReadTag1.equals(phdReadTag15));

        DefaultPhdReadTag phdReadTag16 = new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);
        phdReadTag16.setType("type1");

        assertFalse(phdReadTag1.equals(phdReadTag16));

        DefaultPhdReadTag phdReadTag17 = new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
        phdReadTag17.setUngappedRange(new Range(1, 11));

        assertFalse(phdReadTag1.equals(phdReadTag17));

        DefaultPhdReadTag phdReadTag18 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);
        phdReadTag18.setFreeFormData(new String[0]);

        assertFalse(phdReadTag1.equals(phdReadTag18));

        DefaultPhdReadTag phdReadTag19 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, Arrays.asList("test", "data").toArray(new String[0]));
        phdReadTag19.setFreeFormData(new String[0]);

        assertFalse(phdReadTag1.equals(phdReadTag19));

        DefaultPhdReadTag phdReadTag20 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData.toCharArray());
        phdReadTag20.setFreeFormData(freeFormData.toCharArray());

        assertTrue(phdReadTag1.equals(phdReadTag20));
    }
}