import org.junit.Test;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testgetType() {
        String expectedType = "testType";
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("testType", "source", new Range(1, 10), new Date(2022, 1, 1), "comment", "freeFormData");
        String actualType = defaultPhdReadTag.getType();
        assertEquals(expectedType, actualType);
    }
}