package jipa;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class VariableTest {

    @Test
    public void testGetName() {
        Variable variable = new Variable("testVar");
        assertEquals("testVar", variable.getName());
    }

    @Test
    public void testAddVariableWithExistingName() {
        Variable variable1 = new Variable("testVar");
        Variable variable2 = new Variable("testVar");
        Variable.addVariable("testVar"); // Should not add a new variable
        assertEquals(1, Variable.variableList.size());
    }

    @Test(expected = NullPointerException.class)
    public void testAddVariableWithNullName() {
        Variable.addVariable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddVariableWithEmptyName() {
        Variable.addVariable("");
    }

    @Test
    public void testRemoveVariable() {
        Variable variable = new Variable("testVar");
        Variable.addVariable("testVar");
        Variable.removeVariable("testVar");
        assertEquals(0, Variable.variableList.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetVariableValueWithNullName() {
        Variable.addVariable("testVar");
        Variable.getVariableValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetVariableValueWithEmptyName() {
        Variable.addVariable("testVar");
        Variable.getVariableValue("");
    }

    @Test
    public void testGetVariableValue() {
        Variable variable = new Variable("testVar");
        Variable.addVariable("testVar");
        Variable.setVariableValue("testVar", 5);
        assertEquals(5, Variable.getVariableValue("testVar"));
    }

    @Test
    public void testSetVariableValue() {
        Variable variable = new Variable("testVar");
        Variable.addVariable("testVar");
        Variable.setVariableValue("testVar", 5);
        assertEquals(5, Variable.getVariableValue("testVar"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindVariableWithNullName() {
        Variable.addVariable("testVar");
        Variable.findVariable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindVariableWithEmptyName() {
        Variable.addVariable("testVar");
        Variable.findVariable("");
    }

    @Test
    public void testFindVariable() {
        Variable variable = new Variable("testVar");
        Variable.addVariable("testVar");
        assertEquals(variable, Variable.findVariable("testVar"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testFindVariableWithNonexistentName() {
        Variable.addVariable("testVar");
        Variable.findVariable("nonexistentVar");
    }

    @Test
    public void testStaticInitialization() {
        assertEquals(0, Variable.variableList.size());
    }
}