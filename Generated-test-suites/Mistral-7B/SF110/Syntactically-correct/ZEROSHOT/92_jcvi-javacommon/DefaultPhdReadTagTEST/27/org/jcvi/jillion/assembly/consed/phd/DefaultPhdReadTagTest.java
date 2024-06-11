package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.*;
import org.jcvi.jillion.test.TestUtils;
import org.jcvi.jillion.util.test.TestUtilsEmpty;
import org.jcvi.jillion.util.test.TestUtilsRange;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstruction() {
        String type = "type";
        String source = "source";
        Range ungappedRange = TestUtilsRange.range(1, 100);
        Date date = TestUtils.testDate("2022-01-01");
        String comment = TestUtilsEmpty.string();
        String freeFormData = TestUtilsEmpty.string();

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagGetters() {
        String type = "type";
        String source = "source";
        Range ungappedRange = TestUtilsRange.range(1, 100);
        Date date = TestUtils.testDate("2022-01-01");
        String comment = TestUtilsEmpty.string();
        String freeFormData = TestUtilsEmpty.string();

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
        String type2 = "type2";
        String source1 = "source1";
        String source2 = "source2";
        Range ungappedRange1 = TestUtilsRange.range(1, 100);
        Range ungappedRange2 = TestUtilsRange.range(2, 200);
        Date date1 = TestUtils.testDate("2022-01-01");
        Date date2 = TestUtils.testDate("2022-02-01");
        String comment1 = TestUtilsEmpty.string();
        String comment2 = TestUtilsEmpty.string();
        String freeFormData1 = TestUtilsEmpty.string();
        String freeFormData2 = TestUtilsEmpty.string();

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        // Ensure different instances have different hash codes
        assertNotEquals(hashCode1, hashCode2);

        // Verify hashCode is consistent with equals method
        DefaultPhdReadTag copyOfPhdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        assertEquals(hashCode1, copyOfPhdReadTag1.hashCode());
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type1 = "type1";
        String type2 = "type2";
        String source1 = "source1";
        String source2 = "source2";
        Range ungappedRange1 = TestUtilsRange.range(1, 100);
        Range ungappedRange2 = TestUtilsRange.range(2, 200);
        Date date1 = TestUtils.testDate("2022-01-01");
        Date date2 = TestUtils.testDate("2022-02-01");
        String comment1 = TestUtilsEmpty.string();
        String comment2 = TestUtilsEmpty.string();
        String freeFormData1 = TestUtilsEmpty.string();
        String freeFormData2 = TestUtilsEmpty.string();

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        // Verify that default phd read tag is not equal to null
        assertFalse(phdReadTag1.equals(null));

        // Verify that default phd read tag is not equal to instance of different class
        assertFalse(phdReadTag1.equals(new Object()));

        // Verify that two different instances are not equal
        assertFalse(phdReadTag1.equals(phdReadTag2));

        // Verify that two equal instances are equal
        DefaultPhdReadTag copyOfPhdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        assertTrue(phdReadTag1.equals(copyOfPhdReadTag1));
    }
}