import org.junit.Test;
import static org.junit.Assert.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;

public class DefaultPhdReadTagTest {
    @Test
    public void testGetType() {
        DefaultPhdReadTag phdTag = new DefaultPhdReadTag("tagType", "source", new Range(1, 10), new Date("2022-01-01"), "comment", "freeFormData");
        String expectedType = "tagType";
        String actualType = phdTag.getType();
        assertEquals(expectedType, actualType);
    }
}