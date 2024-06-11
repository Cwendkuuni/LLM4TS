import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DefaultPhdReadTagTest {
    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(2021, 1, 1), "comment1", "freeFormData1");
        String expectedType = "type1";
        String actualType = tag.getType();
        assertEquals(expectedType, actualType);
    }
}