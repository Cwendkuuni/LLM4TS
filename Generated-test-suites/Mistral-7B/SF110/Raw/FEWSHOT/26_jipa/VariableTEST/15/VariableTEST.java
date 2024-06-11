package jipa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class VariableTest {

    @Test
    public void testVariableListInitialization() {
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testVariableCreation() {
        Variable var1 = new Variable("var1");
        Variable var2 = new Variable("var2");
        Variable var3 = new Variable("var3");

        assertEquals("var1", var1.getName());
        assertEquals(0, var1.getValue());

        assertEquals("var2", var2.getName());
        assertEquals(0, var2.getValue());

        assertEquals("var3", var3.getName());
        assertEquals(0, var3.getValue());
    }

    @Test
    public void testAddVariable() {
        Variable var1 = new Variable("var1");
        Variable var2 = new Variable("var2");

        assertTrue(Variable.addVariable("var1"));
        assertEquals(1, Variable.variableList.size());
        assertSame(var1, Variable.variableList.get(0));

        assertTrue(Variable.addVariable("var2"));
        assertEquals(2, Variable.variableList.size());
        assertSame(var2, Variable.variableList.get(1));
    }

    @Test
    public void testAddDuplicateVariable() {
        Variable var1 = new Variable("var1");
        Variable var2 = new Variable("var1");

        assertFalse(Variable.addVariable("var1"));
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariable() {
        Variable var1 = new Variable("var1");
        Variable var2 = new Variable("var2");
        Variable var3 = new Variable("var1");

        assertTrue(Variable.addVariable("var1"));
        assertTrue(Variable.addVariable("var2"));
        assertEquals(2, Variable.variableList.size());

        Variable.removeVariable("var1");
        assertEquals(1, Variable.variableList.size());
        assertNull(Variable.findVariable("var1"));

        Variable.removeVariable("var1"); // should have no effect
        assertEquals(1, Variable.variableList.size());

        Variable.removeVariable("var2");
        assertEquals(0, Variable.variableList.size());

        Variable.removeVariable("var3"); // already removed
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testFindVariable() {
        Variable var1 = new Variable("var1");
        Variable var2 = new Variable("var2");
        Variable var3 = new Variable("var1");

        assertTrue(Variable.addVariable("var1"));
        assertTrue(Variable.addVariable("var2"));
        assertEquals(2, Variable.variableList.size());

        assertSame(var1, Variable.findVariable("var1"));
        assertSame(var2, Variable.findVariable("var2"));
        assertNull(Variable.findVariable("nonExistent"));
    }

    @Test
    public void testGetVariableValue() {
        Variable var1 = new Variable("var1");
        Variable var2 = new Variable("var2");
        Variable var3 = new Variable("var1");

        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 42);

        assertEquals(0, Variable.getVariableValue("nonExistent"));
        assertEquals(0, Variable.getVariableValue("var2"));
        assertEquals(42, Variable.getVariableValue("var1"));
    }

    @Test
    public void testSetVariableValue() {
        Variable var1 = new Variable("var1");
        Variable var2 = new Variable("var2");

        Variable.addVariable("var1");
        Variable.addVariable("var2");

        Variable.setVariableValue("var1", 42);
        assertEquals(42, Variable.getVariableValue("var1"));

        Variable.setVariableValue("var2", 7);
        assertEquals(7, Variable.getVariableValue("var2"));
    }

    @Test
    public void testGetVariableValueWithStarPrefix() {
        Variable var1 = new Variable("var1");
        Variable var2 = new Variable("var2");

        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 42);

        assertEquals(42, Variable.getVariableValue("*var1"));
        assertEquals(0, Variable.getVariableValue("*nonExistent"));
    }

    @Test
    public void testRemoveLastVariable() {
        Variable var1 = new Variable("var1");
        Variable var2 = new Variable("var2");
        Variable var3 = new Variable("var3");

        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.addVariable("var3");

        Variable.removeVariable("var3");
        assertEquals(2, Variable.variableList.size());
        assertSame(var1, Variable.findVariable("var1"));
        assertSame(var2, Variable.findVariable("var2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullVariableName() {
        Variable.addVariable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindNullVariableName() {
        Variable.findVariable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeVariableValue() {
        Variable var1 = new Variable("var1");

        Variable.addVariable("var1");
        Variable.setVariableValue("var1", -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveNonExistentVariable() {
        Variable.removeVariable("nonExistent");
    }

    private Variable findVariable(String varName) {
        return Variable.findVariable(varName);
    }
}