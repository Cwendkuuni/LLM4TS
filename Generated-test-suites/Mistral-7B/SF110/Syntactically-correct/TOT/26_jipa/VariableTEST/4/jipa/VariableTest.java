import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import jipa.Variable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VariableTest {
    // ... other tests ...
    
    @org.junit.Test
    public void testAddVariable() {
        Variable.variableList = new ArrayList<>();
        String varName1 = "var1";
        String varName2 = "var2";
        String varName3 = "var1";

        // Test adding valid variables
        assertTrue(Variable.addVariable(varName1));
        assertEquals(1, Variable.variableList.size());
        Variable firstVariable = Variable.variableList.get(0);
        assertEquals(varName1, firstVariable.getName());
        assertNull(firstVariable.getValue());

        // Test adding duplicate variables
        assertFalse(Variable.addVariable(varName1));
        assertEquals(1, Variable.variableList.size());

        // Test adding a variable with the same name as an existing integer value
        Variable.setVariableValue(varName1, 42);
        assertFalse(Variable.addVariable(varName1));
        assertEquals(1, Variable.variableList.size());

        // Test adding a variable with a different name
        assertTrue(Variable.addVariable(varName2));
        assertEquals(2, Variable.variableList.size());

        // Test adding variables in different orders
        List<String> expectedNames = new ArrayList<>(Arrays.asList(varName1, varName2, varName3));
        Variable.addVariable(varName3);
        List<String> actualNames = new ArrayList<>();
        for (Variable variable : Variable.variableList) {
            actualNames.add(variable.getName());
        }
        assertEquals(expectedNames, actualNames);
    }
}