package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;

import java.util.Arrays;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstruction() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, defaultPhdReadTag.getType());
        assertEquals(source, defaultPhdReadTag.getSource());
        assertEquals(ungappedRange, defaultPhdReadTag.getUngappedRange());
        assertEquals(date, defaultPhdReadTag.getDate());
        assertEquals(comment, defaultPhdReadTag.getComment());
        assertEquals(freeFormData, defaultPhdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("type1", "source1", null, null, null, null);
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type1", "source1", null, null, null, null);

        assertTrue(defaultPhdReadTag1.equals(defaultPhdReadTag2));

        DefaultPhdReadTag defaultPhdReadTag3 = new DefaultPhdReadTag("type2", "source2", null, null, null, null);

        assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag3));
    }

    @Test
    public void testDefaultPhdReadTagHashCode() {
        DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("type1", "source1", null, null, null, null);
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type1", "source1", null, null, null, null);

        assertEquals(defaultPhdReadTag1.hashCode(), defaultPhdReadTag2.hashCode());

        DefaultPhdReadTag defaultPhdReadTag3 = new DefaultPhdReadTag("type2", "source2", null, null, null, null);

        assertNotEquals(defaultPhdReadTag1.hashCode(), defaultPhdReadTag3.hashCode());
    }

    @Test
    public void testDefaultPhdReadTagGetUngappedRange() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date("2022-01-01"), null, null);

        Range expectedUngappedRange = new Range(1, 10);
        Range actualUngappedRange = defaultPhdReadTag.getUngappedRange();

        assertEquals(expectedUngappedRange, actualUngappedRange);
    }

    @Test
    public void testDefaultPhdReadTagGetDate() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", null, new Date("2022-01-01"), null, null);

        Date expectedDate = new Date("2022-01-01");
        Date actualDate = defaultPhdReadTag.getDate();

        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testDefaultPhdReadTagGetComment() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", null, null, "comment", null);

        String expectedComment = "comment";
        String actualComment = defaultPhdReadTag.getComment();

        assertEquals(expectedComment, actualComment);
    }

    @Test
    public void testDefaultPhdReadTagGetFreeFormData() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", null, null, null, "freeFormData");

        String expectedFreeFormData = "freeFormData";
        String actualFreeFormData = defaultPhdReadTag.getFreeFormData();

        assertEquals(expectedFreeFormData, actualFreeFormData);
    }

    @Test
    public void testDefaultPhdReadTagGetType() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", null, null, null, null);

        String expectedType = "type";
        String actualType = defaultPhdReadTag.getType();

        assertEquals(expectedType, actualType);
    }

    @Test
    public void testDefaultPhdReadTagGetSource() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", null, null, null, null);

        String expectedSource = "source";
        String actualSource = defaultPhdReadTag.getSource();

        assertEquals(expectedSource, actualSource);
    }

    @Test
    public void testDefaultPhdReadTagNullValues() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(null, null, null, null, null, null);

        assertNull(defaultPhdReadTag.getType());
        assertNull(defaultPhdReadTag.getSource());
        assertNull(defaultPhdReadTag.getUngappedRange());
        assertNull(defaultPhdReadTag.getDate());
        assertNull(defaultPhdReadTag.getComment());
        assertNull(defaultPhdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagEqualObjects() {
        DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");

        assertTrue(defaultPhdReadTag1.equals(defaultPhdReadTag2));
    }

    @Test
    public void testDefaultPhdReadTagNotEqualObjectsWithDifferentType() {
        DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type2", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");

        assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag2));
    }

    @Test
    public void testDefaultPhdReadTagNotEqualObjectsWithDifferentSource() {
        DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type1", "source2", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");

        assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag2));
    }

    @Test
    public void testDefaultPhdReadTagNotEqualObjectsWithDifferentUngappedRange() {
        DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type1", "source1", new Range(11, 20), new Date("2022-01-01"), "comment1", "freeFormData1");

        assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag2));
    }

    @Test
    public void testDefaultPhdReadTagNotEqualObjectsWithDifferentDate() {
        DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-02-01"), "comment1", "freeFormData1");

        assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag2));
    }

    @Test
    public void testDefaultPhdReadTagNotEqualObjectsWithDifferentComment() {
        DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment2", "freeFormData1");

        assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag2));
    }

    @Test
    public void testDefaultPhdReadTagNotEqualObjectsWithDifferentFreeFormData() {
        DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData2");

        assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag2));
    }

    @Test
    public void testDefaultPhdReadTagEqualsNullObject() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");

        assertFalse(defaultPhdReadTag.equals(null));
    }

    @Test
    public void testDefaultPhdReadTagEqualsObjectOfDifferentClass() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");

        assertFalse(defaultPhdReadTag.equals("not an instance of DefaultPhdReadTag"));
    }

    @Test
    public void testDefaultPhdReadTagHashCodeConsistency() {
        DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date("2022-01-01"), "comment1", "freeFormData1");

        int hashCode1 = defaultPhdReadTag1.hashCode();
        int hashCode2 = defaultPhdReadTag2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }
}