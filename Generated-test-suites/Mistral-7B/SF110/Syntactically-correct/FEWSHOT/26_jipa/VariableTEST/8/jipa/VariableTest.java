package jipa;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class VariableTest {

    @Test
    public void testAddVariableAndGetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        assertEquals(0, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("var2"));
    }

    @Test
    public void testAddVariableWithExistingName() {
        Variable.addVariable("var1");
        Variable.addVariable("var1");
        assertEquals(0, Variable.getVariableValue("var1"));
        assertFalse(Variable.variableList.size() > 1);
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertEquals(1, Variable.getVariableValue("var2"));
        assertEquals(0, Variable.getVariableValue("var1"));
        assertFalse(Variable.variableList.contains(new Variable("var1")));
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testAddVariableWithNullName() {
        Variable.addVariable(null);
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        assertEquals(0, Variable.findVariable("var1"));
        assertEquals(1, Variable.findVariable("var2"));
        assertFalse(Variable.findVariable("nonExistentVar") != -1);
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("var1"));
    }

    @Test
    public void testSetVariableValueWithNonExistentVariable() {
        Variable.setVariableValue("nonExistentVar", 5);
        assertEquals(0, Variable.getVariableValue("nonExistentVar"));
    }

    @Test
    public void testGetVariableValueWithStarPrefix() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("*var1"));
    }

    @Test
    public void testGetVariableValueWithReference() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("&var1"));
    }

    @Test
    public void testVariableListInitialization() {
        assertEquals(0, Variable.getVariableValue("var1"));
        assertFalse(Variable.variableList.isEmpty());
    }

    @Test(expected = java.lang.IndexOutOfBoundsException.class)
    public void testGetVariableValueWithNegativeIndex() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 5);
        Variable.setVariableValue("var2", 10);
        Variable.removeVariable("var1");
        Variable.getVariableValue(-1);
    }

    @Test
    public void testVariableListIteration() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 5);
        Variable.setVariableValue("var2", 10);
        List<Variable> variables = new ArrayList<>(Variable.variableList);
        assertEquals("var1", variables.get(0).getName());
        assertEquals("var2", variables.get(1).getName());
    }
}