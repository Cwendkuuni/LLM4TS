import org.junit.Test;
import org.junit.Assert.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;

public class DefaultPhdReadTagTest {
    @Test
    public void testGetType() {
        String expectedType = "testType";
        DefaultPhdReadTag tag = new DefaultPhdReadTag("testType", "testSource", new Range(1, 10), new Date(), "testComment", "testFreeFormData");
        String actualType = tag.getType();
        assertEquals(expectedType, actualType);
    }
}