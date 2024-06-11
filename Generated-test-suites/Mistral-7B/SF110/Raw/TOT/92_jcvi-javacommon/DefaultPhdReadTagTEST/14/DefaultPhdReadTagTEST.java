import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.core.Date;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {
    @Test
    public void testDefaultPhdReadTagConstructor() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range("1:10", "2:20");
        Date date = new Date("2022-01-01");
        String comment = "testComment";
        String freeFormData = "testData";

        DefaultPhdReadTag phdTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(type, phdTag.getType());
        assertEquals(source, phdTag.getSource());
        assertEquals(ungappedRange, phdTag.getUngappedRange());
        assertEquals(date, phdTag.getDate());
        assertEquals(comment, phdTag.getComment());
        assertEquals(freeFormData, phdTag.getFreeFormData());
    }
}