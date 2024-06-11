package jipa.junit;

import jipa.Variable;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class VariableTest {
    @Test
    public void testConstructor() {
        Variable variable = new Variable("testVar");
        assertNotNull(variable.getName());
        assertEquals(0, variable.getValue());
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testAddVariableAndGetVariableValue() {
        Variable.addVariable("testVar1");
        Variable.addVariable("testVar2");

        assertTrue(Variable.variableList.size() == 2);
        assertEquals(0, Variable.getVariableValue("testVar1"));
        assertEquals(0, Variable.getVariableValue("testVar2"));
    }

    @Test
    public void testFindVariableAddAndRemoveVariable() {
        Variable.addVariable("testVar1");
        Variable.addVariable("testVar2");
        Variable.removeVariable("testVar1");

        assertEquals(-1, Variable.findVariable("testVar1"));
        assertEquals(0, Variable.getVariableValue("testVar2"));
    }

    @Test
    public void testSetAndGetValue() {
        Variable variable = new Variable("testVar");
        variable.setValue(5);
        assertEquals(5, variable.getValue());
    }

    @Test
    public void testSetVariableValueAndGetVariableValue() {
        Variable.addVariable("testVar1");
        Variable.setVariableValue("testVar1", 10);
        assertEquals(10, Variable.getVariableValue("testVar1"));
    }

    @Test
    public void testFindVariableAndAddVariableWithExistingVariable() {
        Variable.addVariable("testVar1");
        assertEquals(-1, Variable.findVariable("testVar1"));
        Variable.addVariable("testVar1");
        assertEquals(1, Variable.variableList.size());
        assertEquals("testVar1", Variable.variableList.get(0).getName());
    }

    @Test
    public void testSetVariableValueWithNonExistentVariable() {
        assertEquals(0, Variable.getVariableValue("testVar1"));
        Variable.setVariableValue("testVar1", 10);
        assertEquals(0, Variable.getVariableValue("testVar1"));
    }

    @Test
    public void testAddVariableWithNullVariableName() {
        assertFalse(Variable.addVariable(null));
    }

    @Test
    public void testFindVariableAndAddVariableWithNullVariableName() {
        assertFalse(Variable.findVariable(null) != -1);
    }

    @Test
    public void testGetVariableValueWithNullVariableName() {
        assertEquals(0, Variable.getVariableValue(null));
    }

    @Test
    public void testSetVariableValueWithInvalidVariableName() {
        Variable.addVariable("testVar1");
        Variable.setVariableValue("testVar1&", 10);
        assertEquals(0, Variable.getVariableValue("testVar1"));
    }
}