import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {
    @Test
    public void testEqual() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(Objects.equal(obj1, obj2));
        
        Object obj3 = new Object();
        assertFalse(Objects.equal(obj1, obj3));
        
        Object obj4 = null;
        assertFalse(Objects.equal(obj1, obj4));
        
        Object obj5 = new Object();
        assertTrue(Objects.equal(obj5, obj5));
    }
}