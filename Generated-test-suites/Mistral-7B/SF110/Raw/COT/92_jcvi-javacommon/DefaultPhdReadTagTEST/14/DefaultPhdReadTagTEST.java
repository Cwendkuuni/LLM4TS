import org.junit.Test;
import org.junit.Assert.*;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.assembly.consed.phd.PhdReadTag;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstructor() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "testComment";
        String freeFormData = "testData";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testGetters() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "testComment";
        String freeFormData = "testData";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        String type1 = "type1";
        String source1 = "source1";
        String comment1 = "comment1";
        Date date1 = new Date(2022, 1, 1);
        String freeFormData1 = "freeFormData1";

        String type2 = "type2";
        String source2 = "source2";
        String comment2 = "comment2";
        Date date2 = new Date(2022, 1, 1);
        String freeFormData2 = "freeFormData2";

        Range range1 = new Range(1, 10);
        Range range2 = new Range(1, 10);

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, range2, date2, comment2, freeFormData2);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        // Since all fields are different, the hash codes should be different
        assertNotEquals(hashCode1, hashCode2);

        // Test equal objects
        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);
        int hashCode3 = phdReadTag3.hashCode();

        assertEquals(hashCode1, hashCode3);
    }

    @Test
    public void testEquals() {
        String type1 = "type1";
        String source1 = "source1";
        String comment1 = "comment1";
        Date date1 = new Date(2022, 1, 1);
        String freeFormData1 = "freeFormData1";

        String type2 = "type2";
        String source2 = "source1";
        String comment2 = "comment1";
        Date date2 = new Date(2022, 1, 1);
        String freeFormData2 = "freeFormData1";

        Range range1 = new Range(1, 10);
        Range range2 = new Range(1, 10);

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, range2, date2, comment2, freeFormData2);
        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);

        // Test different objects
        assertFalse(phdReadTag1.equals(phdReadTag2));

        // Test equal objects
        assertTrue(phdReadTag1.equals(phdReadTag3));
    }

    @Test(expected = NullPointerException.class)
    public void testNullSource() {
        String type = "testType";
        String source = null;
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "testComment";
        String freeFormData = "testData";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullType() {
        String type = null;
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "testComment";
        String freeFormData = "testData";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullComment() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = null;
        String freeFormData = "testData";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullFreeFormData() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "testComment";
        String freeFormData = null;

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptySource() {
        String type = "testType";
        String source = "";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "testComment";
        String freeFormData = "testData";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyType() {
        String type = "";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "testComment";
        String freeFormData = "testData";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyComment() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "";
        String freeFormData = "testData";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyFreeFormData() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "testComment";
        String freeFormData = "";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test
    public void testGettersWithNullValues() {
        String type = null;
        String source = null;
        Range ungappedRange = null;
        Date date = null;
        String comment = null;
        String freeFormData = null;

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertNull(phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }
}