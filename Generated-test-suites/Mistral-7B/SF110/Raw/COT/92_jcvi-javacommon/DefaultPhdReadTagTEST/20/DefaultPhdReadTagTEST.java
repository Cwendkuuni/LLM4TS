import org.junit.Assert;
import org.junit.Test;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstructor() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(type, defaultPhdReadTag.getType());
        Assert.assertEquals(source, defaultPhdReadTag.getSource());
        Assert.assertEquals(ungappedRange, defaultPhdReadTag.getUngappedRange());
        Assert.assertEquals(date, defaultPhdReadTag.getDate());
        Assert.assertEquals(comment, defaultPhdReadTag.getComment());
        Assert.assertEquals(freeFormData, defaultPhdReadTag.getFreeFormData());
    }

    @Test
    public void testGetters() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(type, defaultPhdReadTag.getType());
        Assert.assertEquals(source, defaultPhdReadTag.getSource());
        Assert.assertEquals(ungappedRange, defaultPhdReadTag.getUngappedRange());
        Assert.assertEquals(date, defaultPhdReadTag.getDate());
        Assert.assertEquals(comment, defaultPhdReadTag.getComment());
        Assert.assertEquals(freeFormData, defaultPhdReadTag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        String type1 = "testType1";
        String type2 = "testType2";
        String source = "testSource";
        Range ungappedRange1 = new Range(1, 10);
        Range ungappedRange2 = new Range(1, 11);
        Date date1 = new Date("2022-01-01");
        Date date2 = new Date("2022-01-02");
        String comment1 = "testComment1";
        String comment2 = "testComment2";
        String freeFormData1 = "testFreeFormData1";
        String freeFormData2 = "testFreeFormData2";

        DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag(type1, source, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type2, source, ungappedRange2, date2, comment2, freeFormData2);

        int hashCode1 = defaultPhdReadTag1.hashCode();
        int hashCode2 = defaultPhdReadTag2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);

        DefaultPhdReadTag defaultPhdReadTag3 = new DefaultPhdReadTag(type1, source, ungappedRange1, date1, comment1, freeFormData1);

        int hashCode3 = defaultPhdReadTag3.hashCode();

        Assert.assertEquals(hashCode1, hashCode3);
    }

    @Test
    public void testEquals() {
        String type1 = "testType1";
        String type2 = "testType2";
        String source = "testSource";
        Range ungappedRange1 = new Range(1, 10);
        Range ungappedRange2 = new Range(1, 11);
        Date date1 = new Date("2022-01-01");
        Date date2 = new Date("2022-01-02");
        String comment1 = "testComment1";
        String comment2 = "testComment2";
        String freeFormData1 = "testFreeFormData1";
        String freeFormData2 = "testFreeFormData2";

        DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag(type1, source, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type2, source, ungappedRange2, date2, comment2, freeFormData2);
        DefaultPhdReadTag defaultPhdReadTag3 = new DefaultPhdReadTag(type1, source, ungappedRange1, date1, comment1, freeFormData1);

        Assert.assertFalse(defaultPhdReadTag1.equals(defaultPhdReadTag2));
        Assert.assertTrue(defaultPhdReadTag1.equals(defaultPhdReadTag3));
    }

    @Test(expected = NullPointerException.class)
    public void testNullType() {
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullSource() {
        String type = "testType";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullUngappedRange() {
        String type = "testType";
        String source = "testSource";
        Date date = new Date("2022-01-01");
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullDate() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullComment() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String freeFormData = "testFreeFormData";

        new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullFreeFormData() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "testComment";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);
    }
}