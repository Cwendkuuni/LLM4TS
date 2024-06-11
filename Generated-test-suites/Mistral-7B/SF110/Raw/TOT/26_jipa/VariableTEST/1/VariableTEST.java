package jipa;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {
    List<Variable> variableList;
    Variable testVariable;

    @Before
    public void setup() {
        Variable.variableList = new ArrayList<>();
    }

    @Test
    public void testAddVariable() {
        String varName = "testVar";
        assertTrue(Variable.addVariable(varName));
        assertEquals(1, Variable.variableList.size());
        Variable testVariable = Variable.variableList.get(0);
        assertEquals(testVariable.getName(), varName);
        assertEquals(testVariable.getValue(), 0);

        String sameVarName = "testVar";
        assertFalse(Variable.addVariable(sameVarName));
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariable() {
        String varName = "testVar";
        int initialSize = Variable.variableList.size();
        Variable.removeVariable(varName);
        int finalSize = Variable.variableList.size();
        assertEquals(initialSize - 1, finalSize);
        assertFalse(Variable.findVariable(varName) != -1);
    }

    @Test
    public void testGetVariableValue() {
        String varName = "testVar";
        int initialValue = 0;
        testVariable = new Variable(varName);
        Variable.variableList.add(testVariable);
        int expectedValue = initialValue;
        int testValue = Variable.getVariableValue(varName);
        assertEquals(expectedValue, testValue);

        String nonExistentVarName = "nonExistentVar";
        int nonExistentValue = Variable.getVariableValue(nonExistentVarName);
        assertEquals(0, nonExistentValue);

        String starVarName = "*testVar";
        int starExpectedValue = initialValue;
        int starTestValue = Variable.getVariableValue(starVarName);
        assertEquals(starExpectedValue, starTestValue);
    }

    @Test
    public void testSetVariableValue() {
        String varName = "testVar";
        int initialValue = 0;
        Variable testVariable = new Variable(varName);
        Variable.variableList.add(testVariable);

        int newValue = 42;
        Variable.setVariableValue(varName, newValue);
        int testValue = Variable.getVariableValue(varName);
        assertEquals(newValue, testValue);

        String nonExistentVarName = "nonExistentVar";
        Variable.setVariableValue(nonExistentVarName, 42);
        int nonExistentTestValue = Variable.getVariableValue(nonExistentVarName);
        assertEquals(0, nonExistentTestValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindVariableWithNullVarName() {
        String nullVarName = null;
        Variable.findVariable(nullVarName);
    }

    @Test
    public void testFindVariableWithEmptyVarName() {
        String emptyVarName = "";
        int testValue = Variable.findVariable(emptyVarName);
        assertEquals(-1, testValue);
    }
}