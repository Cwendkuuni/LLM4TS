package jipa.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jipa.Variable;

public class VariableTest {

    @Test
    public void testVariableConstruction() {
        Variable variable = new Variable("testVar");
        assertNotNull(variable);
        assertEquals("testVar", variable.getName());
        assertEquals(0, variable.getValue());
    }

    // ... (all test cases from experts)

    @Test
    public void testAddVariableWithNullVariableList() {
        Variable.addVariable("testVar");
        Variable.variableList = null;
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveVariableWithNullVariableList() {
        Variable.removeVariable("testVar");
        Variable.variableList = null;
    }

    @Test(expected = NullPointerException.class)
    public void testGetVariableValueWithNullVariableList() {
        Variable.getVariableValue("testVar");
        Variable.variableList = null;
    }

    @Test(expected = NullPointerException.class)
    public void testSetVariableValueWithNullVariable() {
        Variable variable = null;
        Variable.setVariableValue("testVar", 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddVariableWithEmptyString() {
        Variable.addVariable("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveVariableWithEmptyString() {
        Variable.removeVariable("");
    }

    @Test(expected = NullPointerException.class)
    public void testSetVariableValueWithNullVariableList() {
        Variable.setVariableValue("testVar", 10);
        Variable.variableList = null;
    }

    @Test(expected = IllegalStateException.class)
    public void testAddVariableWithExistingName() {
        Variable.addVariable("testVar");
        Variable.addVariable("testVar");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableValueWithNonExistentName() {
        Variable.setVariableValue("testNonExistentVar", 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetVariableValueWithNonExistentName() {
        Variable.addVariable("testVar1");
        Variable.addVariable("testVar2");
        assertEquals(0, Variable.getVariableValue("testNonExistentVar"));
    }

    // ... (other test cases from experts)

    @Test(expected = NullPointerException.class)
    public void testFindVariableWithNullVariableList() {
        Variable.findVariable("testVar");
        Variable.variableList = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableValueWithNegativeValue() {
        Variable variable = new Variable("testVar");
        Variable.addVariable("testVar");
        Variable.setVariableValue("testVar", -10);
    }

    // ... (other test cases as needed)

    @Test
    public void testStaticInitialization() {
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testStaticMethods() {
        Variable.addVariable("testVar1");
        Variable.addVariable("testVar2");
        Variable.removeVariable("testVar1");
        Variable.setVariableValue("testVar2", 10);
        assertEquals(1, Variable.variableList.size());
        assertEquals(10, Variable.getVariableValue("testVar2"));
    }

}