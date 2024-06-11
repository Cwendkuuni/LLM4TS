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
    public void testConstructor() {
        Variable var = new Variable("varName");
        assertEquals("varName", var.getName());
        assertEquals(0, var.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullName() {
        new Variable(null);
    }

    @Test
    public void testGetValue() {
        Variable var = new Variable("varName");
        assertEquals(0, var.getValue());
    }

    @Test
    public void testSetValue() {
        Variable var = new Variable("varName");
        var.setValue(42);
        assertEquals(42, var.getValue());
    }

    @Test
    public void testGetName() {
        Variable var = new Variable("varName");
        assertEquals("varName", var.getName());
    }

    @Test
    public void testAddVariable() {
        Variable.addVariable("varName1");
        assertEquals(1, Variable.variableList.size());
        assertTrue(Variable.variableList.get(0).getName().equals("varName1"));
        assertEquals(0, Variable.variableList.get(0).getValue());
    }

    @Test
    public void testAddVariableDuplicate() {
        Variable.addVariable("varName1");
        Variable.addVariable("varName1");
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("varName1");
        Variable.removeVariable("varName1");
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("varName1");
        Variable var1 = new Variable("varName1");
        var1.setValue(42);
        Variable.setVariableValue("varName1", 43);
        assertEquals(43, Variable.getVariableValue("varName1"));
        assertEquals(0, Variable.getVariableValue("nonExistentVar"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("varName1");
        Variable var1 = new Variable("varName1");
        Variable.setVariableValue("varName1", 42);
        assertEquals(42, Variable.getVariableValue("varName1"));
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("varName1");
        Variable var1 = new Variable("varName1");
        Variable var2 = new Variable("varName2");
        Variable var3 = new Variable("&varName1");
        int index = Variable.findVariable("varName1");
        assertEquals(0, index);
        assertSame(var1, Variable.variableList.get(0));
        index = Variable.findVariable("&varName1");
        assertEquals(0, index);
        assertEquals(42, Variable.variableList.get(0).getValue());
        index = Variable.findVariable("varName2");
        assertEquals(-1, index);
    }

    @Test
    public void testFindVariableNull() {
        Variable.addVariable("varName1");
        assertNull(Variable.findVariable(null));
    }

    @Test
    public void testFindVariableEmptyList() {
        assertNull(Variable.findVariable("varName1"));
    }
}