import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.Cloneable;
import org.apache.commons.lang3.SerializationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Given: an instance of a cloneable class
        Cloneable original = new MyCloneableClass();

        // When: calling the clone method with the original instance
        MyCloneableClass cloned = SerializationUtils.clone(original);

        // Then: the cloned instance should be equal to the original one
        assertEquals(original, cloned);

        // Given: a null instance
        Cloneable nullObject = null;

        // When: calling the clone method with a null instance
        try {
            MyCloneableClass clonedNull = (MyCloneableClass) SerializationUtils.clone(nullObject);
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Then: a SerializationException should be thrown
            assertNotNull(e.getMessage());
        }
    }

    // Add test cases for serialize and deserialize methods following the same pattern
}

class MyCloneableClass implements Cloneable {
    // Add some fields and methods here
}

