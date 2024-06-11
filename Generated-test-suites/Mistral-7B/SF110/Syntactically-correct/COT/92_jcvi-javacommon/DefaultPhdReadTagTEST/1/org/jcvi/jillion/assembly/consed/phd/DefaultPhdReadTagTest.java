import org.junit.Assert;
import org.junit.Test;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.assembly.consed.phd.PhdReadTag;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstructor() {
        String type = "protein";
        String source = "contig1";
        Range ungappedRange = new Range(1, 100);
        Date date = new Date("2022-01-01");
        String comment = "comment1";
        String freeFormData = "data1";
        PhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(type, phdReadTag.getType());
        Assert.assertEquals(source, phdReadTag.getSource());
        Assert.assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        Assert.assertEquals(date, phdReadTag.getDate());
        Assert.assertEquals(comment, phdReadTag.getComment());
        Assert.assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testGetHashCode() {
        String type1 = "protein";
        String source1 = "contig1";
        Range ungappedRange1 = new Range(1, 100);
        Date date1 = new Date("2022-01-01");
        String comment1 = "comment1";
        String freeFormData1 = "data1";
         phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "dna";
        String source2 = "contig2";
        Range ungappedRange2 = new Range(2, 200);
        Date date2 = new Date("2022-01-02");
        String comment2 = "comment2";
        String freeFormData2 = "data2";
         phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        Assert.assertNotEquals(phdReadTag1.hashCode(), phdReadTag2.hashCode());
    }

    @Test
    public void testEquals() {
        String type1 = "protein";
        String source1 = "contig1";
        Range ungappedRange1 = new Range(1, 100);
        Date date1 = new Date("2022-01-01");
        String comment1 = "comment1";
        String freeFormData1 = "data1";
         PhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);

        String type2 = "protein";
        String source2 = "contig1";
        Range ungappedRange2 = new Range(1, 100);
        Date date2 = new Date("2022-01-01");
        String comment2 = "comment1";
        String freeFormData2 = "data1";
         PhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        Assert.assertTrue(phdReadTag1.equals(phdReadTag2));

        String type3 = "protein";
        String source3 = "contig2";
        Range ungappedRange3 = new Range(1, 100);
        Date date3 = new Date("2022-01-01");
        String comment3 = "comment1";
        String freeFormData3 = "data2";
         PhdReadTag phdReadTag3 = new DefaultPhdReadTag(type3, source3, ungappedRange3, date3, comment3, freeFormData3);

        Assert.assertFalse(phdReadTag1.equals(phdReadTag3));

        String nullType = null;
        String nullSource = null;
        Range nullUngappedRange = null;
        Date nullDate = null;
        String nullComment = null;
        String nullFreeFormData = null;
         PhdReadTag nullPhdReadTag = new DefaultPhdReadTag(nullType, nullSource, nullUngappedRange, nullDate, nullComment, nullFreeFormData);

        Assert.assertFalse(phdReadTag1.equals(nullPhdReadTag));
    }

}