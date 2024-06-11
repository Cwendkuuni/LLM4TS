import org.junit.Test;
import org.junit.Assert.*;
import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.assembly.consed.phd.PhdReadTag;
import java.util.Date;
import java.util.Arrays;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstructor() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        PhdReadTag expectedPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(expectedPhdReadTag, phdReadTag);
    }

    @Test
    public void testGetType() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "freeFormData1");
        String expectedType = "type1";
        String actualType = phdReadTag.getType();

        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "freeFormData1");
        String expectedSource = "source1";
        String actualSource = phdReadTag.getSource();

        assertEquals(expectedSource, actualSource);
    }

    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "freeFormData1");
        Range expectedUngappedRange = new Range(1, 10);
        Range actualUngappedRange = phdReadTag.getUngappedRange();

        assertEquals(expectedUngappedRange, actualUngappedRange);
    }

    @Test
    public void testGetDate() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "freeFormData1");
        Date expectedDate = new Date();
        Date actualDate = phdReadTag.getDate();

        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "freeFormData1");
        String expectedComment = "comment1";
        String actualComment = phdReadTag.getComment();

        assertEquals(expectedComment, actualComment);
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "freeFormData1");
        String expectedFreeFormData = "freeFormData1";
        String actualFreeFormData = phdReadTag.getFreeFormData();

        assertEquals(expectedFreeFormData, actualFreeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testHashCodeWithNullComment() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, "source1", new Range(1, 10), new Date(), null, "freeFormData1");
        int hashCode = phdReadTag.hashCode();
    }

    @Test(expected = NullPointerException.class)
    public void testHashCodeWithNullDate() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), null, "comment1", "freeFormData1");
        int hashCode = phdReadTag.hashCode();
    }

    @Test(expected = NullPointerException.class)
    public void testHashCodeWithNullFreeFormData() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", null);
        int hashCode = phdReadTag.hashCode();
    }

    @Test(expected = NullPointerException.class)
    public void testHashCodeWithNullSource() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type1", null, new Range(1, 10), new Date(), "comment1", "freeFormData1");
        int hashCode = phdReadTag.hashCode();
    }

    @Test(expected = NullPointerException.class)
    public void testHashCodeWithNullType() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, "source1", new Range(1, 10), new Date(), "comment1", "freeFormData1");
        int hashCode = phdReadTag.hashCode();
    }

    @Test(expected = NullPointerException.class)
    public void testHashCodeWithNullUngappedRange() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type1", "source1", null, new Date(), "comment1", "freeFormData1");
        int hashCode = phdReadTag.hashCode();
    }

    @Test
    public void testEqualsWithSameObject() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "freeFormData1");
        DefaultPhdReadTag phdReadTag2 = phdReadTag1;

        assertTrue(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testEqualsWithDifferentObjectDifferentType() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "freeFormData1");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("type2", "source2", new Range(1, 10), new Date(), "comment1", "freeFormData1");

        assertFalse(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testEqualsWithDifferentObjectSameTypeButDifferentComment() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "freeFormData1");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment2", "freeFormData1");

        assertFalse(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testEqualsWithDifferentObjectSameTypeButDifferentDate() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "freeFormData1");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(1, 1, 2023), "comment1", "freeFormData1");

        assertFalse(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testEqualsWithDifferentObjectSameTypeButDifferentFreeFormData() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "freeFormData1");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "differentFreeFormData");

        assertFalse(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testEqualsWithDifferentObjectSameTypeButDifferentSource() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "freeFormData1");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("type1", "source2", new Range(1, 10), new Date(), "comment1", "freeFormData1");

        assertFalse(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testEqualsWithDifferentObjectSameTypeButDifferentUngappedRangeStart() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "freeFormData1");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("type1", "source1", new Range(2, 10), new Date(), "comment1", "freeFormData1");

        assertFalse(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testEqualsWithDifferentObjectSameTypeButDifferentUngappedRangeEnd() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "freeFormData1");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 11), new Date(), "comment1", "freeFormData1");

        assertFalse(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testEqualsWithSameTypeButDifferentObjectsWithSameValues() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "freeFormData1");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "freeFormData1");

        assertTrue(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testEqualsWithSameTypeButDifferentObjectsWithSameValuesButDifferentHashCodes() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "freeFormData1");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "freeFormData1");

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();
        assertNotEquals(hashCode1, hashCode2);
        assertTrue(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testEqualsWithSameTypeButDifferentObjectsWithSameValuesButDifferentUngappedRanges() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "freeFormData1");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 11), new Date(), "comment1", "freeFormData1");

        assertFalse(phdReadTag1.equals(phdReadTag2));
    }
}