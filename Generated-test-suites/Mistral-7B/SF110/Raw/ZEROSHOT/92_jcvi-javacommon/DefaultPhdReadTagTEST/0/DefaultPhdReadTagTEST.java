import org.jcvi.jillion.core.Date;
import org.jcvi.jillion.core.Range;
import org.junit.Assert;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagCreation() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag actualDefaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals("Incorrect type", type, actualDefaultPhdReadTag.getType());
        Assert.assertEquals("Incorrect source", source, actualDefaultPhdReadTag.getSource());
        Assert.assertEquals("Incorrect ungappedRange", ungappedRange, actualDefaultPhdReadTag.getUngappedRange());
        Assert.assertEquals("Incorrect date", date, actualDefaultPhdReadTag.getDate());
        Assert.assertEquals("Incorrect comment", comment, actualDefaultPhdReadTag.getComment());
        Assert.assertEquals("Incorrect freeFormData", freeFormData, actualDefaultPhdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagHashCode() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag actualDefaultPhdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag actualDefaultPhdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        int actualHashCode1 = actualDefaultPhdReadTag1.hashCode();
        int actualHashCode2 = actualDefaultPhdReadTag2.hashCode();

        Assert.assertEquals("Hashcodes don't match", actualHashCode1, actualHashCode2);

        comment = "updatedComment";
        DefaultPhdReadTag updatedDefaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        int actualUpdatedHashCode = updatedDefaultPhdReadTag.hashCode();

        Assert.assertNotEquals("Hashcodes still match", actualHashCode1, actualUpdatedHashCode);
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag actualDefaultPhdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag actualDefaultPhdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        boolean actualIsEqual = actualDefaultPhdReadTag1.equals(actualDefaultPhdReadTag2);

        Assert.assertTrue("Not equal when they should be", actualIsEqual);

        DefaultPhdReadTag notEqualDefaultPhdReadTag = new DefaultPhdReadTag("notEqualType", source, ungappedRange, date, comment, freeFormData);

        boolean actualNotEqual = actualDefaultPhdReadTag1.equals(notEqualDefaultPhdReadTag);

        Assert.assertFalse("Equal when they shouldn't be", actualNotEqual);

        comment = "updatedComment";
        DefaultPhdReadTag updatedDefaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        boolean actualUpdatedIsEqual = actualDefaultPhdReadTag1.equals(updatedDefaultPhdReadTag);

        Assert.assertTrue("Not equal when they should be updated", actualUpdatedIsEqual);
    }
}