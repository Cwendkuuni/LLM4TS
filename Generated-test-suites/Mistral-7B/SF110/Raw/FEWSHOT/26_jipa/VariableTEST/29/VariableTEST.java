package jipa;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class VariableTest {

    @Test
    public void testAddVariableAndGetValue() {
        Variable.addVariable("var1");
        Variable var1 = Variable.variableList.get(0);
        assertEquals("var1", var1.getName());
        assertEquals(0, var1.getValue());

        Variable.setVariableValue("var1", 42);
        assertEquals(42, Variable.getVariableValue("var1"));
    }

    @Test
    public void testAddVariableWithSameNameFails() {
        Variable.addVariable("var1");
        assertFalse(Variable.addVariable("var1"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertEquals(1, Variable.variableList.size());
        assertEquals("var2", Variable.variableList.get(0).getName());
    }

    @Test
    public void testGetVariableValueWithReference() {
        Variable.addVariable("var1");
        Variable var1 = Variable.variableList.get(0);
        Variable.setVariableValue("var1", 42);
        int value = Variable.getVariableValue("&var1");
        assertEquals(42, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetVariableValueWithInvalidName() {
        Variable.getVariableValue("invalidName");
    }

    @Test
    public void testFindVariableIndex() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        int index = Variable.findVariable("var1");
        assertEquals(0, index);

        index = Variable.findVariable("var2");
        assertEquals(1, index);

        index = Variable.findVariable("nonExistentVar");
        assertEquals(-1, index);
    }

    @Test
    public void testFindVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 42);
        int value = Variable.findVariable("var1");
        assertEquals(0, value);

        Variable var1 = Variable.variableList.get(value);
        assertEquals(42, var1.getValue());
    }

    @Test
    public void testAddVariableWithInitialList() {
        List<Variable> initialVariables = Arrays.asList(
                new Variable("var1"),
                new Variable("var2"),
                new Variable("var3")
        );
        Variable.variableList = initialVariables;

        Variable var3 = Variable.addVariable("var4");
        assertEquals(4, Variable.variableList.size());
        assertEquals("var4", Variable.variableList.get(3).getName());
    }
}