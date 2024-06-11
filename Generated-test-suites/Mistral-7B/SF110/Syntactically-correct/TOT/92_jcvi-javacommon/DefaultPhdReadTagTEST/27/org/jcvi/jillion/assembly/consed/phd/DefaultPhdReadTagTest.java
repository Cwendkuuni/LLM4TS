import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstruction() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(2022, 1, 1);
        String comment = "testComment";
        String freeFormData = "testData";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, tag.getType());
        assertEquals(source, tag.getSource());
        assertEquals(ungappedRange, tag.getUngappedRange());
        assertEquals(date, tag.getDate());
        assertEquals(comment, tag.getComment());
        assertEquals(freeFormData, tag.getFreeFormData());
    }
}