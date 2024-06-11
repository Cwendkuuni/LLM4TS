package jipa;

import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {

    @Test
    public void testAddVariable() {
        Variable.addVariable("var1");
        Variable variable1 = Variable.variableList.get(0);
        assertEquals("var1", variable1.getName());
        int initialSize = Variable.variableList.size();
        Variable.addVariable("var2");
        assertEquals("var2", Variable.variableList.get(initialSize).getName());
        assertEquals(2, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        Variable.setVariableValue("var1", 20);
        assertEquals(20, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("nonExistentVar"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        Variable.setVariableValue("var1", 20);
        assertEquals(20, Variable.getVariableValue("var1"));
        Variable.removeVariable("var1");
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", -5);
        assertEquals(-5, Variable.getVariableValue("var1"));
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable variable1 = Variable.findVariable("var1");
        assertNotNull(variable1);
        assertEquals("var1", variable1.getName());
        Variable.removeVariable("var1");
        Variable variable2 = Variable.findVariable("var1");
        assertNull(variable2);
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertEquals(1, Variable.variableList.size());
        Variable variable1 = Variable.findVariable("var1");
        assertNull(variable1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableValueWithNonExistentVariable() {
        Variable.setVariableValue("nonExistentVar", 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableValueWithNegativeValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        Variable.setVariableValue("var1", -5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddVariableWithExistingVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var1");
    }

    @Test
    public void testVariableListSize() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertEquals(1, Variable.variableList.size());
        Variable.addVariable("var1");
        assertEquals(2, Variable.variableList.size());
        Variable.removeVariable("var2");
        assertEquals(1, Variable.variableList.size());
    }
}