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
        String source = "contig_1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "this is a comment";
        String freeFormData = "free form data";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(type, phdReadTag.getType());
        Assert.assertEquals(source, phdReadTag.getSource());
        Assert.assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        Assert.assertEquals(date, phdReadTag.getDate());
        Assert.assertEquals(comment, phdReadTag.getComment());
        Assert.assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagGettersHashCode() {
        String type = "protein";
        String source = "contig_1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "this is a comment";
        String freeFormData = "free form data";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(phdReadTag1.hashCode(), phdReadTag2.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullComment() {
        String type = "protein";
        String source = "contig_1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = null;
        String freeFormData = "free form data";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullDate() {
        String type = "protein";
        String source = "contig_1";
        Range ungappedRange = new Range(1, 10);
        Date date = null;
        String comment = "this is a comment";
        String freeFormData = "free form data";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullFreeFormData() {
        String type = "protein";
        String source = "contig_1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "this is a comment";
        String freeFormData = null;

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullSource() {
        String type = "protein";
        String source = null;
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "this is a comment";
        String freeFormData = "free form data";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullType() {
        String type = null;
        String source = "contig_1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "this is a comment";
        String freeFormData = "free form data";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPhdReadTagNullUngappedRange() {
        String type = "protein";
        String source = "contig_1";
        Range ungappedRange = null;
        Date date = new Date(2022, 1, 1);
        String comment = "this is a comment";
        String freeFormData = "free form data";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type = "protein";
        String source = "contig_1";
        Range ungappedRange1 = new Range(1, 10);
        Range ungappedRange2 = new Range(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "this is a comment";
        String freeFormData = "free form data";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange1, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange2, date, comment, freeFormData);

        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag("protein", "contig_2", ungappedRange1, date, comment, freeFormData);

        Assert.assertTrue(phdReadTag1.equals(phdReadTag2));
        Assert.assertFalse(phdReadTag1.equals(phdReadTag3));
    }
}