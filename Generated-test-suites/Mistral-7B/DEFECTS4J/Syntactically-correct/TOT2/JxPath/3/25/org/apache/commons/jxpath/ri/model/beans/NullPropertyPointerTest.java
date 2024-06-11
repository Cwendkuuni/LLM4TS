import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        QName expected = new QName("*");
        QName actual = nullPropertyPointer.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(1);
        int expected = 1;
        int actual = nullPropertyPointer.getPropertyIndex();
        assertEquals(expected, actual);
    }

    // Add tests for other methods
}