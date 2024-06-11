package jipa;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class VariableTest {

    @Test
    public void testAddVariableAndGetVariableValue() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("var1");
        Variable var1 = Variable.variableList.get(0);
        assertEquals("var1", var1.getName());
        assertEquals(0, var1.getValue());
        assertEquals(0, Variable.getVariableValue("var1"));

        Variable.addVariable("var2");
        Variable var2 = Variable.variableList.get(1);
        assertEquals("var2", var2.getName());
        assertEquals(0, var2.getValue());
        assertEquals(0, Variable.getVariableValue("var2"));

        Variable.setVariableValue("var1", 42);
        assertEquals(42, Variable.getVariableValue("var1"));
    }

    @Test
    public void testAddVariableWithSameName() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("var1");
        Variable.addVariable("var1");
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testFindVariable() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable var1 = Variable.variableList.get(0);
        Variable var2 = Variable.variableList.get(1);

        assertEquals(0, Variable.findVariable("var1"));
        assertEquals(1, Variable.findVariable("var2"));
        assertEquals(-1, Variable.findVariable("nonExistentVar"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        assertEquals(2, Variable.variableList.size());

        Variable.removeVariable("var1");
        assertEquals(1, Variable.variableList.size());
        assertNull(Variable.findVariable("var1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddVariableWithNullName() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableValueWithNullName() {
        Variable.variableList = new ArrayList<>();
        Variable variable = new Variable("var1");
        Variable.variableList.add(variable);
        Variable.setVariableValue(null, 42);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetVariableValueWithNonExistentVariable() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        assertEquals(0, Variable.getVariableValue("nonExistentVar"));
    }

    @Test
    public void testGetVariableValueWithReference() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("&var1");
        Variable.addVariable("var1");
        Variable var1 = Variable.variableList.get(1);
        assertEquals(0, var1.getValue());
        Variable.setVariableValue("var1", 42);
        assertEquals(42, Variable.getVariableValue("&var1"));
    }
}