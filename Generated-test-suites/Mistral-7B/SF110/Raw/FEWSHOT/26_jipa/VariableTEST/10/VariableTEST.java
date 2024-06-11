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
    public void testVariableListInit() {
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testAddVariable() {
        Variable.addVariable("var1");
        assertEquals(1, Variable.variableList.size());
        Variable v1 = Variable.variableList.get(0);
        assertEquals("var1", v1.getName());
        assertEquals(0, v1.getValue());

        Variable.addVariable("var2");
        assertEquals(2, Variable.variableList.size());
        Variable v2 = Variable.variableList.get(1);
        assertEquals("var2", v2.getName());
        assertEquals(0, v2.getValue());
    }

    @Test
    public void testAddVariableDuplicate() {
        Variable.addVariable("var1");
        Variable.addVariable("var1");
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 42);

        assertEquals(0, Variable.getVariableValue("var2"));
        assertEquals(0, Variable.getVariableValue("var3"));
        assertEquals(42, Variable.getVariableValue("var1"));
        assertEquals(42, Variable.getVariableValue("*var1"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 42);

        assertEquals(0, Variable.getVariableValue("var2"));
        assertEquals(0, Variable.getVariableValue("var3"));
        assertEquals(42, Variable.getVariableValue("var1"));
        assertEquals(42, Variable.getVariableValue("*var1"));

        Variable.setVariableValue("var1", 13);
        assertEquals(13, Variable.getVariableValue("var1"));
        assertEquals(13, Variable.getVariableValue("*var1"));
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        assertEquals(-1, Variable.findVariable("var3"));
        assertEquals(0, Variable.findVariable("var1"));
        assertEquals(1, Variable.findVariable("var2"));

        Variable v1 = Variable.findVariable("var1");
        assertEquals("var1", v1.getName());
        assertEquals(0, v1.getValue());
    }

    @Test
    public void testFindVariableWithReference() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.addVariable("&var1");

        Variable v1Ref = Variable.findVariable("&var1");
        assertSame(Variable.variableList.get(0), v1Ref);
        assertEquals("var1", v1Ref.getName());
        assertEquals(0, v1Ref.getValue());

        Variable v1 = Variable.findVariable("var1");
        assertEquals("var1", v1.getName());
        assertEquals(0, v1.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddVariableNull() {
        Variable.addVariable(null);
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        Variable.removeVariable("var2");
        assertEquals(1, Variable.variableList.size());
        assertNull(Variable.findVariable("var2"));
    }

    @Test
    public void testRemoveVariableNotExist() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        Variable.removeVariable("var3");
        assertEquals(2, Variable.variableList.size());
        assertNull(Variable.findVariable("var3"));
    }

    @Test
    public void testRemoveVariableWithReference() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.addVariable("&var1");

        Variable.removeVariable("var1");
        assertEquals(1, Variable.variableList.size());
        assertSame(Variable.variableList.get(0), Variable.findVariable("&var1"));
        assertEquals("var2", Variable.findVariable("var1").getName());
        assertEquals(0, Variable.findVariable("var1").getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveVariableNull() {
        Variable.addVariable("var1");
        Variable.removeVariable(null);
    }

    @Test
    public void testGetVariableValueWithStartsWithAsterisk() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 42);

        assertEquals(42, Variable.getVariableValue("*var1"));
        assertEquals(0, Variable.getVariableValue("var2"));
        assertEquals(0, Variable.getVariableValue("var3"));
    }
}