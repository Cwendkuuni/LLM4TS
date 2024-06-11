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

    private static List<Variable> variableList;

    @Before
    public void setUp() {
        variableList = new ArrayList<Variable>();
    }

    @Test
    public void testAddVariableAndGetVariableValue() {
        Variable.addVariable("var1");
        Variable var1 = Variable.variableList.get(0);
        assertEquals("var1", var1.getName());
        assertEquals(0, var1.getValue());
        assertEquals(0, Variable.getVariableValue("var1"));

        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
    }

    @Test
    public void testAddVariableWithExistingName() {
        Variable.addVariable("var1");
        assertFalse(Variable.addVariable("var1"));
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable var1 = Variable.variableList.get(0);
        Variable.removeVariable("var1");
        assertNull(Variable.findVariable("var1"));
        assertEquals(0, Variable.variableList.size());

        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable var1 = Variable.addVariable("var2");

        assertEquals("var1", Variable.findVariable("var1").getName());
        assertEquals("var2", Variable.findVariable("var2").getName());
        assertNull(Variable.findVariable("nonExistentVar"));
    }

    @Test
    public void testGetVariableValueWithReference() {
        Variable.addVariable("&var1");
        Variable var1 = Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);

        assertEquals(10, Variable.getVariableValue("&var1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindVariableWithInvalidReference() {
        Variable.addVariable("&nonExistentVar");
    }
}