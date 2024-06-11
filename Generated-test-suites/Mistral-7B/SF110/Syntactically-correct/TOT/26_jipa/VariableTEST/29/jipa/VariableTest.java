import static org.junit.Assert.*;
import jipa.Variable;

public class VariableTest {
    @Test
    public void testAddVariable() {
        Variable.variableList = new ArrayList<>();
        String varName = "testVariable";
        boolean result = Variable.addVariable(varName);
        assertTrue(Variable.variableList.size() == 1);
        Variable testVariable = Variable.variableList.get(0);
        assertEquals(varName, testVariable.getName());
        assertEquals(0, testVariable.getValue());
        result = Variable.addVariable(varName);
        assertFalse(Variable.addVariable(varName));
    }
}