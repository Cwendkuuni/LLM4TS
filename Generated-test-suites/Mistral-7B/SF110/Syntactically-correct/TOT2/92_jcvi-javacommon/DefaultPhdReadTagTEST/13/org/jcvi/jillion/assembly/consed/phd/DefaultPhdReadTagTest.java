import static org.junit.Assert.*;
import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.assembly.consed.phd.PhdReadTag;
import org.junit.Test;

public class DefaultPhdReadTagTest {
    @Test
    public void testGetType() {
        String type = "testType";
        PhdReadTag phdReadTag = new DefaultPhdReadTag(type, null, null, null, null, null);
        String result = phdReadTag.getType();
        assertEquals(type, result);
    }

    @Test
    public void testGetSource() {
        String source = "testSource";
        PhdReadTag phdReadTag = new DefaultPhdReadTag(null, source, null, null, null, null);
        String result = phdReadTag.getSource();
        assertEquals(source, result);
    }

    @Test
    public void testGetUngappedRange() {
        int start = 1;
        int end = 10;
        PhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, new Range(start, end), null, null, null);
        Range result = phdReadTag.getUngappedRange();
        assertEquals(new Range(start, end), result);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullValues() {
        new DefaultPhdReadTag(null, null, null, null, null, null);
    }

    @Test
    public void testHashCodeAndEquals() {
        String type1 = "type1";
        String type2 = "type2";
        String source1 = "source1";
        String source2 = "source2";
        Date date1 = new Date("2022-12-31");
        Date date2 = new Date("2023-01-01");
        Range range1 = new Range(1, 10);
        Range range2 = new Range(11, 20);

        PhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, range1, date1, null, null);
        PhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, range2, date2, null, null);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        assertNotEquals(hashCode1, hashCode2);

        PhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, range1, date1, null, null);
        assertTrue(phdReadTag1.equals(phdReadTag3));

        PhdReadTag phdReadTag4 = new DefaultPhdReadTag(type2, source1, range1, date1, null, null);
        assertFalse(phdReadTag1.equals(phdReadTag4));

        PhdReadTag phdReadTag5 = new DefaultPhdReadTag(type1, source1, new Range(1, 11), date1, null, null);
        assertTrue(phdReadTag1.equals(phdReadTag5));
    }

    @Test
    public void testDateInitialization() {
        Date date = new Date("2022-12-31");
        PhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, date, null, null);
        assertEquals(date, phdReadTag.getDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateInvalidFormat() {
        new DefaultPhdReadTag(null, null, null, null, null, null, "Invalid date format");
    }

    @Test
    public void testRangeInitialization() {
        int start = 1;
        int end = 10;
        PhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, new Range(start, end), null, null, null);
        Range result = phdReadTag.getUngappedRange();
        assertEquals(new Range(start, end), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRangeInvalidStart() {
        new DefaultPhdReadTag(null, null, new Range(-1, 10), null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRangeInvalidEnd() {
        new DefaultPhdReadTag(null, null, new Range(1, -1), null, null, null);
    }

    @Test
    public void testGetComment() {
        String comment = "testComment";
        PhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, null, comment, null);
        String result = phdReadTag.getComment();
        assertEquals(comment, result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetFreeFormDataNull() {
        PhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, null, null, null);
        phdReadTag.getFreeFormData();
    }

    @Test
    public void testGetFreeFormData() {
        String freeFormData = "testFreeFormData";
        PhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, null, null, freeFormData);
        String result = phdReadTag.getFreeFormData();
        assertEquals(freeFormData, result);
    }
}