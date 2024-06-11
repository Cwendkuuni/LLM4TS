import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.junit.Assert.*;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTag_validInput() {
        Range range = new Range(1, 5);
        Date date = new Date("2022-12-31");
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("Type1", "Source1", range, date, "Comment1", "FreeFormData1");
        assertEquals("Type1", phdReadTag.getType());
        assertEquals("Source1", phdReadTag.getSource());
        assertEquals(range, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals("Comment1", phdReadTag.getComment());
        assertEquals("FreeFormData1", phdReadTag.getFreeFormData());
    }

    @Test
    public void testDefaultPhdReadTag_invalidInput() {
        // Test for null constructor arguments
        try {
            new DefaultPhdReadTag(null, null, null, null, null, null);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test for invalid date format
        try {
            DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 5), new Date("InvalidFormat"), "Comment1", "FreeFormData1");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test for empty strings
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("", "", new Range(1, 5), new Date("2022-12-31"), "", "");
        assertEquals("", phdReadTag1.getType());
        assertEquals("", phdReadTag1.getSource());
        assertEquals(new Range(1, 5), phdReadTag1.getUngappedRange());
        assertEquals(new Date("2022-12-31"), phdReadTag1.getDate());
        assertEquals("", phdReadTag1.getComment());
        assertEquals("", phdReadTag1.getFreeFormData());

        // Test for invalid Range input
        try {
            new DefaultPhdReadTag("Type1", "Source1", new Range(-1, -5), new Date("2022-12-31"), "Comment1", "FreeFormData1");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDefaultPhdReadTag_hashCodeEquals() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 5), new Date("2022-12-31"), "Comment1", "FreeFormData1");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 5), new Date("2022-12-31"), "Comment1", "FreeFormData1");
        assertEquals(phdReadTag1.hashCode(), phdReadTag2.hashCode());
        DefaultPhdReadTag phdReadTag3 = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 5), new Date("2022-12-31"), "Comment2", "FreeFormData1");
        assertNotEquals(phdReadTag1.hashCode(), phdReadTag3.hashCode());
    }
}
//