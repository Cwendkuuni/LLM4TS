
package jipa;

import org.junit.Test;
import static org.junit.Assert.*;
import static jipa.Variable.addVariable;
import static jipa.Variable.findVariable;
import static jipa.Variable.getVariableValue;
import static jipa.Variable.setVariableValue;

public class VariableTest {

    @Test
    public void testAddVariable() {
        // Test adding a new variable
        String varName = "testVar";
        boolean result = addVariable(varName);
        assertFalse(result); // The method should return false if the variable already exists
        Variable testVariable = findVariable(varName);
        assertNotNull(testVariable);
        assertEquals("testVar", testVariable.getName());
        assertEquals(0, testVariable.getValue());

        // Test adding the same variable again
        result = addVariable(varName);
        assertTrue(result); // The method should return true if the variable doesn't exist yet
        testVariable = findVariable(varName);
        assertEquals("testVar", testVariable.getName());
        assertEquals(0, testVariable.getValue());
    }

    @Test
    public void testGetVariableValue() {
        // Test getting the value of an existing variable
        String varName = "testVar";
        addVariable(varName);
        Variable testVariable = findVariable(varName);
        testVariable.setValue(10);
        int expectedValue = 10;
        int actualValue = getVariableValue(varName);
        assertEquals(expectedValue, actualValue);

        // Test getting the value of a non-existent variable
        int actualValue2 = getVariableValue("nonExistentVar");
        assertEquals(0, actualValue2);
    }

    @Test
    public void testSetVariableValue() {
        // Test setting the value of an existing variable
        String varName = "testVar";
        addVariable(varName);
        Variable testVariable = findVariable(varName);
        int initialValue = testVariable.getValue();
        int newValue = 20;
        setVariableValue(varName, newValue);
        int expectedValue = newValue;
        int actualValue = getVariableValue(varName);
        assertEquals(expectedValue, actualValue);
        assertEquals(newValue, testVariable.getValue());

        // Test setting the value of a non-existent variable
        setVariableValue("nonExistentVar", 30);
        int actualValue2 = getVariableValue("nonExistentVar");
        assertEquals(0, actualValue2);
    }

    // Test other methods, e.g., addVariable and removeVariable, in a similar way

    private Variable findVariable(String varName) {
        List<Variable> variables = Variable.variableList;
        for (Variable variable : variables) {
            if (variable.getName().equals(varName)) {
                return variable;
            }
        }
        return null;
    }
}