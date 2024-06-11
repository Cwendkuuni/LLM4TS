import org.junit.Assert;
import org.junit.Test;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.assembly.consed.phd.PhdReadTag;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTag_ValidConstruction() {
        String type = "protein";
        String source = "contig_1";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2021-01-01");
        String comment = "comment_1";
        String freeFormData = "freeFormData_1";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(type, phdReadTag.getType());
        Assert.assertEquals(source, phdReadTag.getSource());
        Assert.assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        Assert.assertEquals(date, phdReadTag.getDate());
        Assert.assertEquals(comment, phdReadTag.getComment());
        Assert.assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTag_HashCode_Equals() {
        String type = "protein";
        String source = "contig_1";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2021-01-01");
        String comment = "comment_1";
        String freeFormData = "freeFormData_1";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(phdReadTag1.hashCode(), phdReadTag2.hashCode());
        Assert.assertTrue(phdReadTag1.equals(phdReadTag2));
    }

    @Test
    public void testDefaultPhdReadTag_HashCode_NotEqual() {
        String type1 = "protein";
        String source1 = "contig_1";
        Range ungappedRange1 = new Range(1, 100);
        Date date1 = new Date("2021-01-01");
        String comment1 = "comment_1";
        String freeFormData1 = "freeFormData_1";

        String type2 = "dna";
        String source2 = "contig_2";
        Range ungappedRange2 = new Range(2, 200);
        Date date2 = new Date("2021-01-02");
        String comment2 = "comment_2";
        String freeFormData2 = "freeFormData_2";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        Assert.assertNotEquals(phdReadTag1.hashCode(), phdReadTag2.hashCode());
        Assert.assertFalse(phdReadTag1.equals(phdReadTag2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTag_Null_Source() {
        String type = "protein";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2021-01-01");
        String comment = "comment_1";
        String freeFormData = "freeFormData_1";
        String source = null;

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTag_Null_Type() {
        String source = "contig_1";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2021-01-01");
        String comment = "comment_1";
        String freeFormData = "freeFormData_1";
        String type = null;

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTag_Null_UngappedRange() {
        String type = "protein";
        String source = "contig_1";
        Date date = new Date("2021-01-01");
        String comment = "comment_1";
        String freeFormData = "freeFormData_1";
        Range ungappedRange = null;

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTag_Null_Date() {
        String type = "protein";
        String source = "contig_1";
        Range ungappedRange = new Range(1, 100);
        Date date = null;
        String comment = "comment_1";
        String freeFormData = "freeFormData_1";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTag_Null_Comment() {
        String type = "protein";
        String source = "contig_1";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2021-01-01");
        String comment = null;
        String freeFormData = "freeFormData_1";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTag_Null_FreeFormData() {
        String type = "protein";
        String source = "contig_1";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2021-01-01");
        String comment = "comment_1";
        String freeFormData = null;

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }
}