import static org.junit.Assert.*;
import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.assembly.consed.phd.PhdReadTag;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        PhdReadTag phdReadTag = new DefaultPhdReadTag("testType", "testSource", new Range(1, 10), new Date("2022-01-01"), "testComment", "testData");
        String expectedType = "testType";
        String actualType = phdReadTag.getType();
        assertEquals(expectedType, actualType);
    }

    // Test cases for other methods ...
}