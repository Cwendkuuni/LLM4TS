package jipa;

import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {
    @Test
    public void testVariableConstructor() {
        Variable variable = new Variable(null);
        assertNull(variable);

        variable = new Variable("");
        assertEquals("", variable.getName());

        variable = new Variable("testVariable");
        assertEquals("testVariable", variable.getName());
    }

    @Test
    public void testAddVariable() {
        Variable variable1 = new Variable("var1");
        Variable variable2 = new Variable("var2");
        Variable variable3 = new Variable("var1");

        Variable.addVariable("var1");
        List<Variable> variableList = Variable.variableList;
        assertEquals(1, variableList.size());
        assertEquals("var1", variableList.get(0).getName());

        Variable.addVariable("var2");
        variableList = Variable.variableList;
        assertEquals(2, variableList.size());
        assertNotEquals("var1", variableList.get(1).getName());

        Variable.addVariable("var1");
        variableList = Variable.variableList;
        assertEquals(2, variableList.size());
    }

    @Test
    public void testRemoveVariable() {
        Variable variable1 = new Variable("var1");
        Variable variable2 = new Variable("var2");
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        Variable.removeVariable("var2");
        List<Variable> variableList = Variable.variableList;
        assertEquals(1, variableList.size());
        assertEquals("var1", variableList.get(0).getName());

        Variable.removeVariable("var1");
        variableList = Variable.variableList;
        assertEquals(0, variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable variable1 = new Variable("var1");
        Variable variable2 = new Variable("var2");
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        Variable.setVariableValue("var1", 5);
        int expectedValue1 = 5;
        int actualValue1 = Variable.getVariableValue("var1");
        assertExpectedEqualsActual(expectedValue1, actualValue1);

        Variable.setVariableValue("var2", 10);
        int expectedValue2 = 10;
        int actualValue2 = Variable.getVariableValue("var2");
        assertExpectedEqualsActual(expectedValue2, actualValue2);

        Variable.setVariableValue("var1", 15);
        int expectedValue3 = 15;
        int actualValue3 = Variable.getVariableValue("var1");
        assertExpectedEqualsActual(expectedValue3, actualValue3);

        Variable.removeVariable("var2");
        int actualValue4 = Variable.getVariableValue("var2");
        assertEquals(0, actualValue4);

        Variable.removeVariable("var1");
        int actualValue5 = Variable.getVariableValue("var1");
        assertEquals(0, actualValue5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableValueWithNegativeValue() {
        Variable variable = new Variable("testVariable");
        Variable.setVariableValue("testVariable", -5);
    }

    @Test
    public void testFindVariableWithNullVarName() {
        Variable variable1 = new Variable("var1");
        Variable variable2 = new Variable("var2");
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        int expectedIndex = -1;
        int actualIndex = Variable.findVariable(null);
        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testFindVariableWithNonExistentVarName() {
        Variable variable1 = new Variable("var1");
        Variable variable2 = new Variable("var2");
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        int expectedIndex = -1;
        int actualIndex = Variable.findVariable("nonExistentVar");
        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testFindVariableWithReference() {
        Variable variable1 = new Variable("var1");
        Variable variable2 = new Variable("var2");
        Variable referenceVariable = new Variable("&var1");
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        int expectedValue = 0;
        int actualValue = Variable.findVariable("&var1");
        assertExpectedEqualsActual(expectedValue, actualValue);
    }

    @Test
    public void testFindVariableWithExistentVarName() {
        Variable variable1 = new Variable("var1");
        Variable variable2 = new Variable("var2");
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        int expectedIndex = 0;
        int actualIndex = Variable.findVariable("var1");
        assertEquals(expectedIndex, actualIndex);
    }

    private void assertExpectedEqualsActual(int expectedValue, int actualValue) {
        assertEquals(expectedValue, actualValue);
    }
}