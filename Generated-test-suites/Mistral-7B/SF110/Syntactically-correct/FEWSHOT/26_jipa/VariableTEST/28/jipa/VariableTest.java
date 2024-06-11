package jipa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
        Variable variable = new Variable("var1");
        assertEquals("var1", variable.getName());
        assertEquals(0, variable.getValue());
    }

    @Test
    public void testAddVariableSuccess() {
        Variable.addVariable("var1");
        assertEquals(1, Variable.variableList.size());
        Variable variable = Variable.variableList.get(0);
        assertEquals("var1", variable.getName());
        assertEquals(0, variable.getValue());
    }

    @Test
    public void testAddVariableFail() {
        Variable.addVariable("var1");
        try {
            Variable.addVariable("var1");
            fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariableSuccess() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertEquals(1, Variable.variableList.size());
        assertNull(Variable.findVariable("var1"));
        Variable variable = Variable.variableList.get(0);
        assertEquals("var2", variable.getName());
    }

    @Test
    public void testRemoveVariableFail() {
        Variable.addVariable("var1");
        Variable.removeVariable("var2");
        assertEquals(1, Variable.variableList.size());
        assertNull(Variable.findVariable("var2"));
    }

    @Test
    public void testGetVariableValueSuccess() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 42);
        assertEquals(42, Variable.getVariableValue("var1"));
    }

    @Test
    public void testGetVariableValueFail() {
        Variable.addVariable("var1");
        assertEquals(0, Variable.getVariableValue("var2"));
    }

    @Test
    public void testSetVariableValueSuccess() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 42);
        assertEquals(42, Variable.getVariableValue("var1"));
    }

    @Test
    public void testSetVariableValueFail() {
        Variable.addVariable("var1");
        try {
            Variable.setVariableValue("var2", 42);
            fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        assertEquals(0, Variable.getVariableValue("var2"));
    }

    @Test
    public void testFindVariableSuccess() {
        Variable.addVariable("var1");
        Variable variable = new Variable("var1");
        assertEquals(Variable.variableList.get(0), variable);
        int index = Variable.findVariable("var1");
        assertEquals(0, index);
    }

    @Test
    public void testFindVariableFail() {
        Variable.addVariable("var1");
        int index = Variable.findVariable("var2");
        assertEquals(-1, index);
    }

    @Test
    public void testFindVariableByReferenceSuccess() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 42);
        assertEquals(42, Variable.findVariable("&var1").getValue());
    }

    @Test
    public void testFindVariableByReferenceFail() {
        Variable.addVariable("var1");
        assertEquals(0, Variable.findVariable("&var2").getValue());
    }
}