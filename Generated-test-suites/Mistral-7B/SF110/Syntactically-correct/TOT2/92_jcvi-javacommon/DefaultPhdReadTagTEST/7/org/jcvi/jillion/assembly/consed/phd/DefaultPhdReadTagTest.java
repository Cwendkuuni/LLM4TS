import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testgetType() {
        String type = "PhdReadTagType";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, null, null, null, null, null);
        assertEquals(type, phdReadTag.getType());
    }

    // Test other methods with similar test cases

    @Test(expected = NullPointerException.class)
    public void testNullType() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, "chr1:123-345", null, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullSource() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("PhdReadTagType", null, null, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullUngappedRange() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("PhdReadTagType", "chr1:123-345", null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeUngappedRange() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("PhdReadTagType", "chr1:-10-20", null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDate() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("PhdReadTagType", "chr1:123-345", null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPastDate() {
        Date date = new Date("1900-01-01");
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("PhdReadTagType", "chr1:123-345", null, date, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFutureDate() {
        Date date = new Date("2100-01-01");
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("PhdReadTagType", "chr1:123-345", null, date, null, null);
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("PhdReadTagType1", "chr1:123-345", null, new Date("2022-01-01"), null, null);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("PhdReadTagType2", "chr1:123-345", null, new Date("2022-01-01"), null, null);
        int result1 = phdReadTag1.hashCode();
        int result2 = phdReadTag2.hashCode();
        assertNotEquals(result1, result2);

        phdReadTag1 = new DefaultPhdReadTag("PhdReadTagType1", "chr1:123-345", null, new Date("2022-01-01"), "comment1", null);
        phdReadTag2 = new DefaultPhdReadTag("PhdReadTagType2", "chr1:123-345", null, new Date("2022-01-01"), "comment2", null);
        assertEquals(phdReadTag1.hashCode(), phdReadTag2.hashCode());

        phdReadTag1 = new DefaultPhdReadTag("PhdReadTagType1", "chr1:123-345", null, new Date("2022-01-01"), null, null);
        phdReadTag2 = new DefaultPhdReadTag("PhdReadTagType2", "chr1:123-345", null, new Date("2022-01-01"), "comment1", null);
        assertNotEquals(phdReadTag1.hashCode(), phdReadTag2.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("PhdReadTagType1", "chr1:123-345", null, new Date("2022-01-01"), null, null);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("PhdReadTagType2", "chr1:123-345", null, new Date("2022-01-01"), null, null);
        assertTrue(phdReadTag1.equals(phdReadTag2));

        phdReadTag1 = new DefaultPhdReadTag("PhdReadTagType1", "chr1:123-345", null, new Date("2022-01-01"), null, null);
        phdReadTag2 = new DefaultPhdReadTag("PhdReadTagType2", "chr1:123-445", null, new Date("2022-01-01"), null, null);
        assertFalse(phdReadTag1.equals(phdReadTag2));

        phdReadTag1 = new DefaultPhdReadTag("PhdReadTagType1", "chr1:123-345", null, new Date("2022-01-01"), null, null);
        phdReadTag2 = new DefaultPhdReadTag("PhdReadTagType2", "chr1:123-345", null, new Date("2022-01-01"), "comment1", null);
        assertFalse(phdReadTag1.equals(phdReadTag2));

        phdReadTag1 = new DefaultPhdReadTag("PhdReadTagType1", "chr1:123-345", null, new Date("2022-01-01"), null, null);
        phdReadTag2 = new DefaultPhdReadTag("PhdReadTagType1", "chr1:123-345", null, new Date("2022-01-01"), null, null);
        assertTrue(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testGetFreeFormData() {
        String freeFormData = "freeFormData";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("PhdReadTagType", "chr1:123-345", null, null, null, freeFormData);
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }
}