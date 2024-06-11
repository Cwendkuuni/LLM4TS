import org.junit.Test;
import org.jcvi.jillion.core.*;
import static org.junit.Assert.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.assembly.consed.phd.Range;
import org.jcvi.jillion.core.RangeTestRunner;
import org.jcvi.jillion.core.data.RangeTestData;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag("testType", "testSource", new Range(1, 10), new Date("2022-01-01"), "testComment", "testData");
        String expectedType = "testType";
        assertEquals(expectedType, phdTag.getType());
    }

    // Repeat for other methods
}