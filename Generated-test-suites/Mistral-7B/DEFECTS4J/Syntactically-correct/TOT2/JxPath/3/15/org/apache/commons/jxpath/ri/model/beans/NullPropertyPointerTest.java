import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("*", nullPropertyPointer.getName().getLocalPart());
    }
}