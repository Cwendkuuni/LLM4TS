import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class VariableTest {
    @Test
    public void testAddVariable() {
        Variable.variableList = new ArrayList<>();
        String varName = "testVariable";

        // Test adding the same variable twice
        Variable.addVariable(varName);
        boolean addVariableResult = Variable.addVariable(varName);
        assertTrue(Variable.variableList.size() == 1);
        assertEquals(varName, Variable.variableList.get(0).getName());
        assertFalse(addVariableResult);

        // Test adding a new variable
        String newVarName = "newTestVariable";
        Variable.addVariable(newVarName);
        assertEquals(Variable.variableList.size(), 2);
        assertEquals(newVarName, Variable.variableList.get(1).getName());
    }
}