package jipa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class VariableTest {

    private List<Variable> variableList;

    @Before
    public void setUp() {
        variableList = new ArrayList<Variable>();
    }

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var1"));
        assertEquals(1, variableList.size());
        assertSame("var1", variableList.get(0).getName());
    }

    @Test
    public void testAddVariableSameName() {
        Variable.addVariable("var1");
        assertFalse(Variable.addVariable("var1"));
        assertEquals(1, variableList.size());
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        assertEquals(0, Variable.findVariable("var1"));
        assertEquals(1, Variable.findVariable("var2"));
        assertNull(Variable.findVariable("var3"));
    }

    @Test
    public void testFindVariableWithReference() {
        Variable.addVariable("var1");
        Variable var2 = new Variable("var2");
        Variable.variableList.add(var2);

        assertEquals(0, Variable.findVariable("var1"));
        assertEquals(1, Variable.findVariable("var2"));
        assertSame(var2, Variable.findVariable("&var2"));
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 10);

        assertEquals(0, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("var2"));
        assertEquals(10, Variable.getVariableValue("*var1"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("var2"));
        assertEquals(10, Variable.getVariableValue("*var1"));

        Variable.setVariableValue("var2", 20);
        assertEquals(10, Variable.getVariableValue("var1"));
        assertEquals(20, Variable.getVariableValue("var2"));
        assertEquals(10, Variable.getVariableValue("*var1"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");

        assertEquals(1, variableList.size());
        assertNull(Variable.findVariable("var1"));
        assertEquals("var2", variableList.get(0).getName());
    }

    @Test(expected = NullPointerException.class)
    public void testFindVariableNull() {
        Variable.findVariable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindVariableEmptyList() {
        Variable.findVariable("var1");
    }
}