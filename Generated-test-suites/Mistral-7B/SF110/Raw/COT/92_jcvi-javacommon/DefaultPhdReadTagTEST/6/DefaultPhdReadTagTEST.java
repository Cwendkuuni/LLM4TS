import org.junit.Assert;
import org.junit.Test;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.assembly.consed.phd.PhdReadTag;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstruction() {
        String type = "protein";
        String source = "source1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment1";
        String freeFormData = "freeFormData1";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(type, phdReadTag.getType());
        Assert.assertEquals(source, phdReadTag.getSource());
        Assert.assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        Assert.assertEquals(date, phdReadTag.getDate());
        Assert.assertEquals(comment, phdReadTag.getComment());
        Assert.assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullType() {
        String source = "source1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment1";
        String freeFormData = "freeFormData1";
        String type = null;

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullSource() {
        String type = "protein";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment1";
        String freeFormData = "freeFormData1";
        String source = null;

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullUngappedRange() {
        String type = "protein";
        String source = "source1";
        Date date = new Date("2022-01-01");
        String comment = "comment1";
        String freeFormData = "freeFormData1";
        Range ungappedRange = null;

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullDate() {
        String type = "protein";
        String source = "source1";
        Range ungappedRange = new Range(1, 10);
        String comment = "comment1";
        String freeFormData = "freeFormData1";
        Date date = null;

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test
    public void testDefaultPhdReadTagHashCode() {
        String type1 = "protein1";
        String source1 = "source1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date("2022-01-01");
        String comment1 = "comment1";
        String freeFormData1 = "freeFormData1";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "protein2";
        String source2 = "source2";
        Range ungappedRange2 = new Range(1, 10);
        Date date2 = new Date("2022-01-01");
        String comment2 = "comment2";
        String freeFormData2 = "freeFormData2";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        int hashCode3 = phdReadTag3.hashCode();

        Assert.assertEquals(hashCode1, hashCode3);
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type1 = "protein1";
        String source1 = "source1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date("2022-01-01");
        String comment1 = "comment1";
        String freeFormData1 = "freeFormData1";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "protein1";
        String source2 = "source1";
        Range ungappedRange2 = new Range(1, 10);
        Date date2 = new Date("2022-01-01");
        String comment2 = "comment1";
        String freeFormData2 = "freeFormData1";

        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        boolean equals = phdReadTag1.equals(phdReadTag2);

        Assert.assertTrue(equals);

        String type3 = "protein2";
        String source3 = "source1";
        Range ungappedRange3 = new Range(1, 11);
        Date date3 = new Date("2022-01-02");
        String comment3 = "comment2";
        String freeFormData3 = "freeFormData2";

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type3, source3, ungappedRange3, date3, comment3, freeFormData3);

        boolean equals2 = phdReadTag1.equals(phdReadTag3);

        Assert.assertFalse(equals2);
    }

    @Test
    public void testDefaultPhdReadTagGetters() {
        String type = "protein";
        String source = "source1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment1";
        String freeFormData = "freeFormData1";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(type, phdReadTag.getType());
        Assert.assertEquals(source, phdReadTag.getSource());
        Assert.assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        Assert.assertEquals(date, phdReadTag.getDate());
        Assert.assertEquals(comment, phdReadTag.getComment());
        Assert.assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }
}