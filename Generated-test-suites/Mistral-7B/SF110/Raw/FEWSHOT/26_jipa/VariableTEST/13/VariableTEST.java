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
    public void testAddVariableAndGetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.addVariable("var1"); // should not add since it already exists

        assertEquals(0, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("var2"));

        Variable.setVariableValue("var1", 10);
        Variable.setVariableValue("var2", 20);

        assertEquals(10, Variable.getVariableValue("var1"));
        assertEquals(20, Variable.getVariableValue("var2"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");

        assertEquals(0, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("var2"));

        Variable.setVariableValue("var2", 20);
        Variable.removeVariable("var2");

        assertNull(Variable.getVariableValue("var1"));
        assertNull(Variable.getVariableValue("var2"));
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.addVariable("var1"); // should not add since it already exists

        final int index1 = Variable.findVariable("var1");
        final int index2 = Variable.findVariable("var2");

        assertTrue(index1 != -1);
        assertTrue(index2 != -1);

        final Variable var1 = Variable.variableList.get(index1);
        final Variable var2 = Variable.variableList.get(index2);

        assertSame(var1, Variable.variableList.get(Variable.findVariable("var1")));
        assertSame(var2, Variable.variableList.get(Variable.findVariable("var2")));

        Variable.removeVariable("var1");

        assertSame(var2, Variable.variableList.get(Variable.findVariable("var2")));
        assertNull(Variable.findVariable("var1"));
    }

    @Test
    public void testGetVariableValueWithReference() {
        Variable.addVariable("&var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 10);

        assertEquals(10, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("var2"));

        final int referenceValue = Variable.getVariableValue("&var1");
        Variable.setVariableValue("var1", 20);

        assertEquals(10, referenceValue); // the value of var1 when the reference was added
        assertEquals(20, Variable.getVariableValue("var1"));
    }

    @Test
    public void testInitialization() {
        assertEquals(0, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("var2"));

        Variable.addVariable("var1");
        Variable.addVariable("var2");

        assertFalse(Variable.variableList.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddVariableWithEmptyName() {
        Variable.addVariable("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddVariableWithNullName() {
        Variable.addVariable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableValueWithNullName() {
        Variable.addVariable("var1");
        Variable.setVariableValue(null, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableValueWithEmptyName() {
        Variable.addVariable("var1");
        Variable.setVariableValue("", 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetVariableValueWithInvalidName() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        Variable.setVariableValue("var1", 10);

        assertNull(Variable.getVariableValue("var3"));
    }
}