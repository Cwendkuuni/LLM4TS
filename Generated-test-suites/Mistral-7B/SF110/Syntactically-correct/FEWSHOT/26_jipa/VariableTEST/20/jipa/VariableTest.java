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
    }

    @Test
    public void testAddVariableDuplicate() {
        Variable.addVariable("var1");
        assertFalse(Variable.addVariable("var1"));
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 42);
        assertEquals(42, Variable.getVariableValue("var1"));
    }

    @Test
    public void testGetVariableValueNull() {
        assertNull(Variable.getVariableValue(null));
    }

    @Test
    public void testGetVariableValueStar() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        assertEquals(1, Variable.getVariableValue("*var1"));
        assertEquals(2, Variable.getVariableValue("*var2"));
    }

    @Test
    public void testGetVariableValueUnknown() {
        assertNull(Variable.getVariableValue("unknown"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 42);
        assertEquals(42, Variable.getVariableValue("var1"));
    }

    @Test
    public void testSetVariableValueNull() {
        Variable.addVariable("var1");
        assertNull(Variable.getVariableValue("var1"));
        Variable.setVariableValue("var1", null);
        assertNull(Variable.getVariableValue("var1"));
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable variable = new Variable("var2");
        Variable.variableList.add(variable);
        final int index = Variable.findVariable("var1");
        assertEquals(0, index);

        final int index2 = Variable.findVariable("var2");
        assertEquals(1, index2);

        final int index3 = Variable.findVariable("var3");
        assertEquals(-1, index3);
    }

    @Test
    public void testFindVariableReference() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable variable = new Variable("&var1");
        Variable.variableList.add(variable);
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(1, Variable.getVariableValue("&var1"));
    }
}