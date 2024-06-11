package org.jcvi.jillion.assembly.consed.phd.test;

import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.util.DateUtil;
import org.jcvi.jillion.test.JillionTest;
import org.junit.Test;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.assembly.consed.phd.PhdReadTag;

import java.time.LocalDate;
import java.util.Collections;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest extends JillionTest {

    @Test
    public void testDefaultPhdReadTagCreation() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = DateUtil.parseLocalDate("2022-03-15");
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        PhdReadTag expectedPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(expectedPhdReadTag, phdReadTag);
    }

    @Test
    public void testDefaultPhdReadTagGetters() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = DateUtil.parseLocalDate("2022-03-15");
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

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
        String type = "testType1";
        String source = "testSource1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = DateUtil.parseLocalDate("2022-03-15");
        String comment1 = "testComment1";
        String freeFormData1 = "testFreeFormData1";

        String type2 = "testType2";
        String source2 = "testSource2";
        Range ungappedRange2 = new Range(2, 20);
        Date date2 = DateUtil.parseLocalDate("2022-03-16");
        String comment2 = "testComment2";
        String freeFormData2 = "testFreeFormData2";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        assertNotEquals(hashCode1, hashCode2);

        // Test if changing any field updates hashcode
        String newComment1 = "newComment1";
        DefaultPhdReadTag updatedPhdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, newComment1, freeFormData1);

        int updatedHashCode1 = updatedPhdReadTag1.hashCode();
        assertNotEquals(hashCode1, updatedHashCode1);

        String newSource2 = "newSource2";
        DefaultPhdReadTag updatedPhdReadTag2 = new DefaultPhdReadTag(type2, newSource2, ungappedRange2, date2, comment2, freeFormData2);

        int updatedHashCode2 = updatedPhdReadTag2.hashCode();
        assertNotEquals(hashCode2, updatedHashCode2);
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type1 = "testType1";
        String source1 = "testSource1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = DateUtil.parseLocalDate("2022-03-15");
        String comment1 = "testComment1";
        String freeFormData1 = "testFreeFormData1";

        String type2 = "testType2";
        String source2 = "testSource2";
        Range ungappedRange2 = new Range(2, 20);
        Date date2 = DateUtil.parseLocalDate("2022-03-16");
        String comment2 = "testComment2";
        String freeFormData2 = "testFreeFormData2";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        assertFalse(phdReadTag1.equals(phdReadTag2));

        // Test equals with same object
        assertTrue(phdReadTag1.equals(phdReadTag1));

        // Test equals with null
        assertFalse(phdReadTag1.equals(null));

        // Test equals with different class
        assertFalse(phdReadTag1.equals(new Object()));

        // Test equals with same fields
        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        assertTrue(phdReadTag1.equals(phdReadTag3));

        // Test equals with null fields
        DefaultPhdReadTag phdReadTag4 = new DefaultPhdReadTag(null, source1, ungappedRange1, date1, comment1, freeFormData1);

        assertTrue(phdReadTag1.equals(phdReadTag4));

        DefaultPhdReadTag phdReadTag5 = new DefaultPhdReadTag(type1, null, ungappedRange1, date1, comment1, freeFormData1);

        assertTrue(phdReadTag1.equals(phdReadTag5));

        DefaultPhdReadTag phdReadTag6 = new DefaultPhdReadTag(type1, source1, null, date1, comment1, freeFormData1);

        assertTrue(phdReadTag1.equals(phdReadTag6));

        DefaultPhdReadTag phdReadTag7 = new DefaultPhdReadTag(type1, source1, ungappedRange1, null, comment1, freeFormData1);

        assertTrue(phdReadTag1.equals(phdReadTag7));

        DefaultPhdReadTag phdReadTag8 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, null, freeFormData1);

        assertTrue(phdReadTag1.equals(phdReadTag8));

        DefaultPhdReadTag phdReadTag9 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, null);

        assertTrue(phdReadTag1.equals(phdReadTag9));

        // Test equals with empty fields
        DefaultPhdReadTag phdReadTag10 = new DefaultPhdReadTag(type1, source1, Range.EMPTY, DateUtil.EMPTY_DATE, comment1, Collections.emptyList());

        assertTrue(phdReadTag1.equals(phdReadTag10));
    }
}