import org.junit.Assert;
import org.junit.Test;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.assembly.consed.phd.PhdReadTag;

public class DefaultPhdReadTagTest {

    @Test
    public void testConstructDefaultPhdReadTag() {
        String type = "protein";
        String source = "contig_1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2021, 1, 1);
        String comment = "my comment";
        String freeFormData = "some data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(type, phdReadTag.getType());
        Assert.assertEquals(source, phdReadTag.getSource());
        Assert.assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        Assert.assertEquals(date, phdReadTag.getDate());
        Assert.assertEquals(comment, phdReadTag.getComment());
        Assert.assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        String type1 = "protein1";
        String source1 = "contig_1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date(2021, 1, 1);
        String comment1 = "my comment1";
        String freeFormData1 = "some data1";
        
        String type2 = "protein2";
        String source2 = "contig_2";
        Range ungappedRange2 = new Range(2, 20);
        Date date2 = new Date(2021, 2, 2);
        String comment2 = "my comment2";
        String freeFormData2 = "some data2";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        // Different objects should have different hash codes
        Assert.assertNotEquals(hashCode1, hashCode2);

        //Same objects should have same hash codes
        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        int hashCode3 = phdReadTag3.hashCode();
        Assert.assertEquals(hashCode1, hashCode3);
    }

    @Test
    public void testEquals() {
        String type1 = "protein1";
        String source1 = "contig_1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date(2021, 1, 1);
        String comment1 = "my comment1";
        String freeFormData1 = "some data1";

        String type2 = "protein2";
        String source2 = "contig_2";
        Range ungappedRange2 = new Range(2, 20);
        Date date2 = new Date(2021, 2, 2);
        String comment2 = "my comment2";
        String freeFormData2 = "some data2";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);

        // Different objects should not be equal
        Assert.assertFalse(phdReadTag1.equals(phdReadTag2));

        // Same objects should be equal
        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        Assert.assertTrue(phdReadTag1.equals(phdReadTag3));
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructorType() {
        String source = "contig_1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2021, 1, 1);
        String comment = "my comment";
        String freeFormData = "some data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructorSource() {
        String type = "protein";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2021, 1, 1);
        String comment = "my comment";
        String freeFormData = "some data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructorUngappedRange() {
        String type = "protein";
        String source = "contig_1";
        Date date = new Date(2021, 1, 1);
        String comment = "my comment";
        String freeFormData = "some data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructorDate() {
        String type = "protein";
        String source = "contig_1";
        Range ungappedRange = new Range(1, 10);
        String comment = "my comment";
        String freeFormData = "some data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructorComment() {
        String type = "protein";
        String source = "contig_1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2021, 1, 1);
        String freeFormData = "some data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructorFreeFormData() {
        String type = "protein";
        String source = "contig_1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2021, 1, 1);
        String comment = "my comment";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);
    }
}