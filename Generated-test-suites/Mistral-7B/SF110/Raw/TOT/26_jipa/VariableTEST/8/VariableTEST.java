package jipa;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class VariableTest {

    @Test
    public void testAddVariable() {
        Variable.variableList = new ArrayList<>();
        String varName = "testVar";
        assertTrue(Variable.addVariable(varName));
        Variable newVariable = Variable.variableList.get(0);
        assertTrue(newVariable.getName().equals(varName));
    }

    @Test
    public void testGetVariableValue() {
        Variable.variableList = new ArrayList<>();
        String varName = "testVar";
        Variable.addVariable(varName);
        Variable testVariable = Variable.variableList.get(0);
        int expectedValue = 0;
        testVariable.setValue(expectedValue);
        int actualValue = Variable.getVariableValue(varName);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testRemoveVariable() {
        Variable.variableList = new ArrayList<>();
        String varName = "testVar";
        Variable.addVariable(varName);
        Variable.removeVariable(varName);
        assertEquals(0, Variable.variableList.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindVariableWithNullName() {
        Variable.variableList = new ArrayList<>();
        String varName = null;
        Variable.findVariable(varName);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testFindVariableWithNonExistentName() {
        Variable.variableList = new ArrayList<>();
        String varName = "nonExistentVar";
        Variable.findVariable(varName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddVariableWithExistingName() {
        Variable.variableList = new ArrayList<>();
        String varName = "testVar";
        Variable.addVariable(varName);
        Variable.addVariable(varName);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetVariableValueWithNonExistentName() {
        Variable.variableList = new ArrayList<>();
        String varName = "nonExistentVar";
        Variable.setVariableValue(varName, 42);
    }
}