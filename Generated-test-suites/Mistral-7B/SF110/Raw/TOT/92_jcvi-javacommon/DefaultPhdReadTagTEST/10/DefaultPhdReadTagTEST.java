import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest
{
    @Test
    public void testDefaultPhdReadTagCreation() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertNotNull(tag);
        assertEquals(type, tag.getType());
        assertEquals(source, tag.getSource());
        assertEquals(ungappedRange, tag.getUngappedRange());
        assertEquals(date, tag.getDate());
        assertEquals(comment, tag.getComment());
        assertEquals(freeFormData, tag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagHashCode() {
        String type1 = "type1";
        String type2 = "type2";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date1 = new Date("2022-01-01");
        Date date2 = new Date("2022-01-02");
        String comment1 = "comment1";
        String comment2 = "comment2";
        String freeFormData1 = "freeFormData1";
        String freeFormData2 = "freeFormData2";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source, ungappedRange, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type2, source, ungappedRange, date2, comment2, freeFormData2);

        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();

        assertNotSame(hashCode1, hashCode2);

        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(type1, source, ungappedRange, date1, comment1, freeFormData1);

        assertEquals(hashCode1, tag3.hashCode());
    }

    @Test
    public void testDefaultPhdReadTagEquals() {
        String type1 = "type1";
        String type2 = "type2";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date1 = new Date("2022-01-01");
        Date date2 = new Date("2022-01-02");
        String comment1 = "comment1";
        String comment2 = "comment2";
        String freeFormData1 = "freeFormData1";
        String freeFormData2 = "freeFormData2";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source, ungappedRange, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type2, source, ungappedRange, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(type1, source, ungappedRange, date2, comment1, freeFormData1);
        DefaultPhdReadTag tag4 = new DefaultPhdReadTag(type1, source, ungappedRange, date1, comment2, freeFormData1);
        DefaultPhdReadTag tag5 = new DefaultPhdReadTag(type1, source, ungappedRange, date1, comment1, freeFormData2);

        assertTrue(tag1.equals(tag2));
        assertFalse(tag1.equals(tag3));
        assertFalse(tag1.equals(tag4));
        assertFalse(tag1.equals(tag5));
    }

    @Test
    public void testDefaultPhdReadTagGetters() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment";
        String freeFormData = "freeFormData";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, tag.getType());
        assertEquals(source, tag.getSource());
        assertEquals(ungappedRange, tag.getUngappedRange());
        assertEquals(date, tag.getDate());
        assertEquals(comment, tag.getComment());
        assertEquals(freeFormData, tag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagWithNullValues() {
        String type = null;
        String source = null;
        Range ungappedRange = null;
        Date date = null;
        String comment = null;
        String freeFormData = null;

        try {
            new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDefaultPhdReadTagWithInvalidDate() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        String invalidDateString = "invalidDate";

        try {
            new DefaultPhdReadTag(type, source, ungappedRange, new Date(invalidDateString), "", "");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}