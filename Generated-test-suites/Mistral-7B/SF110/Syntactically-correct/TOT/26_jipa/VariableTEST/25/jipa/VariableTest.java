
package jipa.test;

import jipa.Variable;
import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {

    @Test
    public void testAddVariable() {
        Variable.addVariable("newVarName");
        assertTrue(Variable.variableList.size() > 0);
        Variable.addVariable("newVarName");
        assertFalse(Variable.addVariable("newVarName"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("testVar");
        Variable.removeVariable("testVar");
        assertFalse(Variable.variableList.contains(new Variable("testVar")));

        Variable.removeVariable("nonexistentVar");
        assertTrue(Variable.variableList.size() > 0);
    }

    @Test
    public void testGetValue() {
        Variable.addVariable("testVar");
        Variable testVar = Variable.variableList.get(0);
        testVar.setValue(42);
        assertEquals(42, Variable.getVariableValue("testVar"));

        assertEquals(0, Variable.getVariableValue("nonexistentVar"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("testVar");
        Variable testVar = Variable.variableList.get(0);
        testVar.setValue(42);
        Variable.setVariableValue("testVar", 43);
        assertEquals(43, Variable.getVariableValue("testVar"));

        Variable.setVariableValue("nonexistentVar", 42);
        assertEquals(0, Variable.getVariableValue("nonexistentVar"));
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("testVar1");
        Variable.addVariable("testVar2");
        Variable testVar1 = Variable.variableList.get(0);
        Variable testVar2 = Variable.variableList.get(1);

        // Test with valid names
        assertEquals(0, Variable.findVariable("testVar1"));
        assertEquals(1, Variable.findVariable("testVar2"));
        assertEquals(-1, Variable.findVariable("nonexistentVar"));

        // Test with valid values
        testVar1.setValue(1);
        testVar2.setValue(2);
        assertEquals(0, Variable.findVariable("testVar1"));
        assertEquals(1, Variable.findVariable("testVar2"));
        assertEquals(-1, Variable.findVariable("nonexistentVar"));

        // Test with empty list
        Variable.variableList.clear();
        assertEquals(-1, Variable.findVariable("testVar1"));
    }
}
