import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagWithValidArguments() {
        String type = "type1";
        String source = "source1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "comment1";
        String freeFormData = "freeFormData1";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals("type1", phdReadTag.getType());
        assertEquals("source1", phdReadTag.getSource());
        assertEquals(new Range(1, 10), phdReadTag.getUngappedRange());
        assertEquals(new Date(2022, 1, 1), phdReadTag.getDate());
        assertEquals("comment1", phdReadTag.getComment());
        assertEquals("freeFormData1", phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagWithNullArguments() {
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
    public void testDefaultPhdReadTagWithEmptyStringArguments() {
        String type = "";
        String source = "";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "";
        String freeFormData = "";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals("", phdReadTag.getType());
        assertEquals("", phdReadTag.getSource());
        assertEquals(new Range(1, 10), phdReadTag.getUngappedRange());
        assertEquals(new Date(2022, 1, 1), phdReadTag.getDate());
        assertEquals("", phdReadTag.getComment());
        assertEquals("", phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTagWithInvalidDate() {
        String type = "type1";
        String source = "source1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(0, 1, 1); // Invalid date
        String comment = "comment1";
        String freeFormData = "freeFormData1";

        try {
            new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}