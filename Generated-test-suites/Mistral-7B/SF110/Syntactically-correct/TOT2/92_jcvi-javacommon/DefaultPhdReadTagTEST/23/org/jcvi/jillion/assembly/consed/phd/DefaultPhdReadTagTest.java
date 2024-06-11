import org.junit.Test;
import static org.junit.Assert.*;
import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.assembly.consed.phd.PhdReadTag;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        String expectedType = "TestType";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("TestType", "TestSource", new Range(1, 10), new Date(), "TestComment", "TestFreeFormData");
        PhdReadTag testObject = new DefaultPhdReadTag("AnotherType", "TestSource", new Range(1, 10), new Date(), "TestComment", "TestFreeFormData");

        String actualType = phdReadTag.getType();
        assertEquals(expectedType, actualType);
        assertNotEquals(testObject.getType(), actualType);
    }

    // Similar test cases for other methods: getSource(), getUngappedRange(), getDate(), getComment(), getFreeFormData()
}