import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        QName expectedName = new QName("*");
        QName actualName = nullPropertyPointer.getName();
        assertEquals(expectedName, actualName);
    }
}