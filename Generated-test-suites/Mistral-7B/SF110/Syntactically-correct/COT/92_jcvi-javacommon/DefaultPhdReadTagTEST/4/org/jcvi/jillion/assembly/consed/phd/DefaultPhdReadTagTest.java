import org.junit.Assert;
import org.junit.Test;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
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

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(type, phdReadTag.getType());
        Assert.assertEquals(source, phdReadTag.getSource());
        Assert.assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        Assert.assertEquals(date, phdReadTag.getDate());
        Assert.assertEquals(comment, phdReadTag.getComment());
        Assert.assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testGettersHashCodeEquals() {
        String type1 = "type1";
        String type2 = "type2";
        String source = "source";
        Range ungappedRange1 = new Range(1, 10);
        Range ungappedRange2 = new Range(11, 20);
        Date date1 = new Date("2022-01-01");
        Date date2 = new Date("2022-01-02");
        String comment1 = "comment1";
        String comment2 = "comment2";
        String freeFormData1 = "freeFormData1";
        String freeFormData2 = "freeFormData2";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source, ungappedRange2, date2, comment2, freeFormData2);

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source, ungappedRange1, date1, comment1, freeFormData1);

        Assert.assertEquals(phdReadTag1.hashCode(), phdReadTag3.hashCode());
        Assert.assertNotEquals(phdReadTag1.hashCode(), phdReadTag2.hashCode());

        Assert.assertTrue(phdReadTag1.equals(phdReadTag3));
        Assert.assertFalse(phdReadTag1.equals(phdReadTag2));
    }

    @Test(expected = NullPointerException.class)
    public void testNullType() {
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment";
        String freeFormData = "freeFormData";

        new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullSource() {
        String type = "type";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment";
        String freeFormData = "freeFormData";

        new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullUngappedRange() {
        String type = "type";
        String source = "source";
        Date date = new Date("2022-01-01");
        String comment = "comment";
        String freeFormData = "freeFormData";

        new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullDate() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        String comment = "comment";
        String freeFormData = "freeFormData";

        new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullComment() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String freeFormData = "freeFormData";

        new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullFreeFormData() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);
    }

    @Test
    public void testEmptyStringType() {
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("", source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals("", phdReadTag.getType());
    }

    @Test
    public void testEmptyStringSource() {
        String type = "type";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, "", ungappedRange, date, comment, freeFormData);

        Assert.assertEquals("", phdReadTag.getSource());
    }

    @Test
    public void testEmptyStringComment() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String freeFormData = "freeFormData";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, "", freeFormData);

        Assert.assertEquals("", phdReadTag.getComment());
    }

    @Test
    public void testEmptyStringFreeFormData() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, "");

        Assert.assertEquals("", phdReadTag.getFreeFormData());
    }
}