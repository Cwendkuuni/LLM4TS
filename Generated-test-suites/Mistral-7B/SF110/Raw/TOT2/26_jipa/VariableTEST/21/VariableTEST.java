import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {

    @Test
    public void testVariableConstructionAndSetName() {
        Variable variable = new Variable("testVariable");
        String name = variable.getName();
        assertEquals("testVariable", name);
    }
}