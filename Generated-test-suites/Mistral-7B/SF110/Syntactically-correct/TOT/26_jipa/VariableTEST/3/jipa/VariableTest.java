import jipa.Variable;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VariableTest {
    @Test
    public void testAddVariable() {
        Variable.addVariable("testVar1");
        assertTrue(Variable.variableList.size() == 1);
        assertEquals("testVar1", Variable.variableList.get(0).getName());
    }
}