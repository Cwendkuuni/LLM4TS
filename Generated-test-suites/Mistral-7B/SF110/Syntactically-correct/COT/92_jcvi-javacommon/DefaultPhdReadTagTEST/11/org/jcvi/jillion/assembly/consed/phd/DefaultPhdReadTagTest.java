import org.junit.Assert;
import org.junit.Test;
import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.assembly.consed.phd.PhdReadTag;
import java.util.Date;
import java.util.Range;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstructor() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date(System.currentTimeMillis());
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        PhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(type, phdReadTag.getType());
        Assert.assertEquals(source, phdReadTag.getSource());
        Assert.assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        Assert.assertEquals(date, phdReadTag.getDate());
        Assert.assertEquals(comment, phdReadTag.getComment());
        Assert.assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagHashCode() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date(System.currentTimeMillis());
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        PhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        PhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        int hashCode1 = phdReadTag1.hashCode();
        int hashCode2 = phdReadTag2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date(System.currentTimeMillis());
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        PhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        PhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        boolean equals = phdReadTag1.equals(phdReadTag2);

        Assert.assertTrue(equals);
    }

    @Test(expected = NullPointerException.class)
    public void testDefaultPhdReadTagNullType() {
        String source = "testSource";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date(System.currentTimeMillis());
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        new DefaultPhdReadTag(null, source, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testDefaultPhdReadTagNullSource() {
        String type = "testType";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date(System.currentTimeMillis());
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        new DefaultPhdReadTag(type, null, ungappedRange, date, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testDefaultPhdReadTagNullUngappedRange() {
        String type = "testType";
        String source = "testSource";
        Date date = new Date(System.currentTimeMillis());
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        new DefaultPhdReadTag(type, source, null, date, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testDefaultPhdReadTagNullDate() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(0, 10);
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        new DefaultPhdReadTag(type, source, ungappedRange, null, comment, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testDefaultPhdReadTagNullComment() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date(System.currentTimeMillis());
        String freeFormData = "testFreeFormData";

        new DefaultPhdReadTag(type, source, ungappedRange, date, null, freeFormData);
    }

    @Test(expected = NullPointerException.class)
    public void testDefaultPhdReadTagNullFreeFormData() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date(System.currentTimeMillis());
        String comment = "testComment";

        new DefaultPhdReadTag(type, source, ungappedRange, date, comment, null);
    }

    @Test
    public void testDefaultPhdReadTagGetters() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date(System.currentTimeMillis());
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        PhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(type, phdReadTag.getType());
        Assert.assertEquals(source, phdReadTag.getSource());
        Assert.assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        Assert.assertEquals(date, phdReadTag.getDate());
        Assert.assertEquals(comment, phdReadTag.getComment());
        Assert.assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagGettersWithNullValues() {
        String type = null;
        String source = null;
        Range ungappedRange = null;
        Date date = null;
        String comment = null;
        String freeFormData = null;

        PhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertNull(phdReadTag.getType());
        Assert.assertNull(phdReadTag.getSource());
        Assert.assertNull(phdReadTag.getUngappedRange());
        Assert.assertNull(phdReadTag.getDate());
        Assert.assertNull(phdReadTag.getComment());
        Assert.assertNull(phdReadTag.getFreeFormData());
    }
}