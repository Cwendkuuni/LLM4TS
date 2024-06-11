package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;

import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.util.SimpleDateFormatUtil;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagInstance() {
        String type = "type1";
        String source = "source1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "comment1";
        String freeFormData = "freeFormData1";

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
        String type1 = "type1";
        String source1 = "source1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date();
        String comment1 = "comment1";
        String freeFormData1 = "freeFormData1";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "type2";
        String source2 = "source2";
        Range ungappedRange2 = new Range(2, 20);
        Date date2 = new Date();
        String comment2 = "comment2";
        String freeFormData2 = "freeFormData2";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        assertNotEquals(hashCode1, hashCode2);

        // Test hashcode with same values
        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        int hashCode3 = phdReadTag3.hashCode();

        assertEquals(hashCode1, hashCode3);
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type1 = "type1";
        String source1 = "source1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date();
        String comment1 = "comment1";
        String freeFormData1 = "freeFormData1";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "type1";
        String source2 = source1;
        Range ungappedRange2 = ungappedRange1;
        Date date2 = date1;
        String comment2 = comment1;
        String freeFormData2 = freeFormData1;

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        assertTrue(phdReadTag1.equals(phdReadTag2));

        // Test equals with different values
        String type3 = "type2";
        String source3 = "source3";
        Range ungappedRange3 = new Range(3, 30);
        Date date3 = new Date();
        String comment3 = "comment3";
        String freeFormData3 = "freeFormData3";

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type3, source3, ungappedRange3, date3, comment3, freeFormData3);

        assertFalse(phdReadTag1.equals(phdReadTag3));
    }

    @Test
    public void testDefaultPhdReadTagGetters() {
        String type = "type1";
        String source = "source1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "comment1";
        String freeFormData = "freeFormData1";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagDateFormatting() {
        Date date = new Date();
        SimpleDateFormatUtil simpleDateFormatUtil = new SimpleDateFormatUtil("yyyy-MM-dd HH:mm:ss");
        String expectedDate = simpleDateFormatUtil.format(date);

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", null, date, null, null);

        assertEquals(expectedDate, phdReadTag.getDate().toString());
    }

    @Test
    public void testDefaultPhdReadTagCompareTo() {
        String type1 = "type1";
        String source1 = "source1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date();
        String comment1 = "comment1";
        String freeFormData1 = "freeFormData1";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "type2";
        String source2 = "source2";
        Range ungappedRange2 = new Range(2, 20);
        Date date2 = new Date();
        String comment2 = "comment2";
        String freeFormData2 = "freeFormData2";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        int compareToResult = phdReadTag1.compareTo(phdReadTag2);

        assertFalse(phdReadTag1.equals(phdReadTag2));
        assertTrue(compareToResult < 0);

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        int compareToResult3 = phdReadTag1.compareTo(phdReadTag3);

        assertTrue(phdReadTag1.equals(phdReadTag3));
        assertEquals(0, compareToResult3);
    }

    @Test
    public void testDefaultPhdReadTagCompareToWithNull() {
        String type = "type1";
        String source = "source1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "comment1";
        String freeFormData = "freeFormData1";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        DefaultPhdReadTag nullPhdReadTag = null;

        int compareToResult = phdReadTag.compareTo(nullPhdReadTag);

        assertNull(nullPhdReadTag);
        assertFalse(phdReadTag.equals(nullPhdReadTag));
        assertTrue(compareToResult < 0);
    }

    @Test
    public void testDefaultPhdReadTagCompareToWithSameInstance() {
        String type = "type1";
        String source = "source1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "comment1";
        String freeFormData = "freeFormData1";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        DefaultPhdReadTag phdReadTag2 = phdReadTag1;

        int compareToResult = phdReadTag1.compareTo(phdReadTag2);

        assertTrue(phdReadTag1.equals(phdReadTag2));
        assertEquals(0, compareToResult);
    }

    @Test
    public void testDefaultPhdReadTagCompareToWithSameValues() {
        String type = "type1";
        String source = "source1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "comment1";
        String freeFormData = "freeFormData1";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        int compareToResult = phdReadTag1.compareTo(phdReadTag2);

        assertTrue(phdReadTag1.equals(phdReadTag2));
        assertEquals(0, compareToResult);
    }

    @Test
    public void testDefaultPhdReadTagCompareToWithDifferentValues() {
        String type1 = "type1";
        String source1 = "source1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date();
        String comment1 = "comment1";
        String freeFormData1 = "freeFormData1";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "type2";
        String source2 = "source2";
        Range ungappedRange2 = new Range(2, 20);
        Date date2 = new Date();
        String comment2 = "comment2";
        String freeFormData2 = "freeFormData2";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        int compareToResult = phdReadTag1.compareTo(phdReadTag2);

        assertFalse(phdReadTag1.equals(phdReadTag2));
        assertTrue(compareToResult < 0);
    }

    @Test
    public void testDefaultPhdReadTagCompareToWithDifferentValuesDifferentTypes() {
        Object phdReadTag1 = new DefaultPhdReadTag("type1", "source1", null, null, null, null);
        Object phdReadTag2 = new Range(1, 10);

        int compareToResult = ((Comparable<?>) phdReadTag1).compareTo(phdReadTag2);

        assertFalse(phdReadTag1.equals(phdReadTag2));
        assertTrue(compareToResult < 0);
    }

    @Test
    public void testDefaultPhdReadTagCompareToWithDifferentValuesDifferentClasses() {
        Object phdReadTag1 = new DefaultPhdReadTag("type1", "source1", null, null, null, null);
        Object phdReadTag2 = new String("String1");

        int compareToResult = ((Comparable<?>) phdReadTag1).compareTo(phdReadTag2);

        assertFalse(phdReadTag1.equals(phdReadTag2));
        assertTrue(compareToResult < 0);
    }

    @Test(expected = ClassCastException.class)
    public void testDefaultPhdReadTagCompareToWithDifferentValuesDifferentClassesThrowException() {
        Object phdReadTag1 = new DefaultPhdReadTag("type1", "source1", null, null, null, null);
        Object phdReadTag2 = new Integer(10);

        ((Comparable<?>) phdReadTag1).compareTo(phdReadTag2);
    }
}