package jipa;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class VariableTest {

    @Test
    public void testAddVariableAndGetValue() {
        Variable.addVariable("var1");
        Variable variable1 = Variable.variableList.get(0);
        assertEquals("var1", variable1.getName());
        assertEquals(0, variable1.getValue());

        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
    }

    @Test
    public void testAddVariableWithSameNameFails() {
        Variable.addVariable("var1");
        boolean addVariableResult = Variable.addVariable("var1");
        assertFalse(addVariableResult);

        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testFindVariableWithExistingNameReturnsIndex() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        int indexOfVar1 = Variable.findVariable("var1");
        int indexOfVar2 = Variable.findVariable("var2");

        assertEquals(0, indexOfVar1);
        assertEquals(1, indexOfVar2);
    }

    @Test
    public void testFindVariableWithNonExistentNameReturnsMinusOne() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        int indexOfVar3 = Variable.findVariable("var3");

        assertEquals(-1, indexOfVar3);
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");

        assertEquals(1, Variable.variableList.size());
        assertFalse(Variable.variableList.contains(Variable.variableList.get(0).getName(), "var1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindVariableWithNullNameThrowsException() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        Variable.findVariable(null);
    }

    @Test
    public void testGetVariableValueWithExistingVariableName() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);

        assertEquals(10, Variable.getVariableValue("var1"));
    }

    @Test
    public void testGetVariableValueWithNonExistentVariableName() {
        Variable.addVariable("var1");

        assertEquals(0, Variable.getVariableValue("var2"));
    }

    @Test
    public void testSetVariableValueWithExistingVariableName() {
        Variable.addVariable("var1");

        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableValueWithNonExistentVariableNameThrowsException() {
        Variable.addVariable("var1");

        Variable.setVariableValue("var2", 10);
    }

    @Test
    public void testGetVariableValueWithStarPrefixReturnsValueOfVariableWithStarAsPrefix() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 10);

        assertEquals(10, Variable.getVariableValue("*var1"));
    }

    @Test
    public void testGetVariableValueWithStarPrefixAndNonExistentVariableNameReturnsZero() {
        Variable.addVariable("var1");

        assertEquals(0, Variable.getVariableValue("*var2"));
    }

    @Test
    public void testInitializeVariableList() {
        Variable variableList = Variable.variableList;
        assertEquals(0, variableList.size());
    }

    @Test
    public void testAddVariableAndGetValueMultipleTimes() {
        for (int i = 0; i < 10; i++) {
            String varName = "var" + i;
            Variable.addVariable(varName);
            Variable variable = Variable.variableList.get(i);
            assertEquals(varName, variable.getName());
            assertEquals(0, variable.getValue());
        }
    }
}