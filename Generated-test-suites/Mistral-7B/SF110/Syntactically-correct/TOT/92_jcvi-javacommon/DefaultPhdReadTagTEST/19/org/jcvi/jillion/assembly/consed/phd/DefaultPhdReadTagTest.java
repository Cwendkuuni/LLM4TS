import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstructor() {
        String type = "type1";
        String source = "source1";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date("2022-01-01");
        String comment = "comment1";
        String freeFormData = "freeFormData1";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals("type1", tag.getType());
        assertEquals("source1", tag.getSource());
        assertEquals(new Range(1, 10), tag.getUngappedRange());
        assertEquals(new Date("2022-01-01"), tag.getDate());
        assertEquals("comment1", tag.getComment());
        assertEquals("freeFormData1", tag.getFreeFormData());
    }
}