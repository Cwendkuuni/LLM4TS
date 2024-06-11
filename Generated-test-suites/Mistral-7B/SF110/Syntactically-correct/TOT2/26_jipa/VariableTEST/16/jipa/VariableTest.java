import static org.junit.Assert.*;
import jipa.Variable;
import java.util.ArrayList;

public class VariableTest {
    @Test
    public void testVariableConstructor() {
        Variable variable = new Variable("testVariable");
        assertEquals("testVariable", variable.getName());
        assertEquals(0, variable.getValue());
    }

    @Test
    public void testGetValue() {
        Variable variable = new Variable("testVariable");
        variable.setValue(10);
        assertEquals(10, variable.getValue());
    }

    @Test
    public void testSetValue() {
        Variable variable = new Variable("testVariable");
        variable.setValue(10);
        variable.setValue(20);
        assertEquals(20, variable.getValue());
    }

    // Test other methods with similar test cases
}