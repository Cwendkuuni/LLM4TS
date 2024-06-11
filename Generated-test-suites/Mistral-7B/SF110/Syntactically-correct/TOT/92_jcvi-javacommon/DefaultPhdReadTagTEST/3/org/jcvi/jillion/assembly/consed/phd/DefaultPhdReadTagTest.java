import org.junit.Test;
import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import java.util.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testDefaultPhdReadTagConstructor() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date(System.currentTimeMillis());
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag phdTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        // Testing getter methods
        assert type.equals(phdTag.getType());
        assert source.equals(phdTag.getSource());
        assert ungappedRange.equals(phdTag.getUngappedRange());
        assert date.equals(phdTag.getDate());
        assert comment.equals(phdTag.getComment());
        assert freeFormData.equals(phdTag.getFreeFormData());
    }
}