package jipa;

import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {
    @Test
    public void testAddVariable() {
        // Add a new variable
        Variable.addVariable("testVar1");
        assertFalse(Variable.variableList.isEmpty());
        Variable testVar1 = Variable.variableList.get(0);
        assertEquals("testVar1", testVar1.getName());
        assertEquals(0, testVar1.getValue());

        // Try to add an existing variable
        Variable.addVariable("testVar1");
        assertTrue(Variable.variableList.size() == 1);
    }

    @Test
    public void testRemoveVariable() {
        // Add a new variable
        Variable.addVariable("testVar1");
        Variable.addVariable("testVar2");
        Variable testVar1 = Variable.variableList.get(0);
        Variable testVar2 = Variable.variableList.get(1);

        // Remove a non-existent variable
        Variable.removeVariable("nonExistentVar");
        assertEquals(2, Variable.variableList.size());

        // Remove a existing variable
        Variable.removeVariable("testVar1");
        assertEquals(1, Variable.variableList.size());
        assertNotEquals(testVar1, Variable.variableList.get(0));
        assertNull(testVar1);
    }

    @Test
    public void testGetVariableValue() {
        // Test with existing variables
        Variable.addVariable("testVar1");
        Variable.addVariable("testVar2");
        Variable.setVariableValue("testVar1", 10);
        Variable.setVariableValue("testVar2", 20);

        assertEquals(0, Variable.getVariableValue("testVar1"));
        assertEquals(20, Variable.getVariableValue("testVar2"));

        // Test with non-existent variables and wildcard variables
        assertEquals(0, Variable.getVariableValue("nonExistentVar"));
        assertEquals(0, Variable.getVariableValue("*"));
    }

    @Test
    public void testSetVariableValue() {
        // Test with existing variables
        Variable.addVariable("testVar1");
        Variable.addVariable("testVar2");
        Variable testVar1 = Variable.variableList.get(0);
        Variable testVar2 = Variable.variableList.get(1);

        Variable.setVariableValue("testVar1", 10);
        Variable.setVariableValue("testVar2", 20);

        assertEquals(10, Variable.getVariableValue("testVar1"));
        assertEquals(20, Variable.getVariableValue("testVar2"));

        // Test with non-existent variables
        Variable.setVariableValue("nonExistentVar", 30);
        assertEquals(0, Variable.getVariableValue("nonExistentVar"));
    }
}