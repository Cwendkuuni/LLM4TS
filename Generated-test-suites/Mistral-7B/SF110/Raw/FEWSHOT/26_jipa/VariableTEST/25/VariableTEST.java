package jipa;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class VariableTest {

    @Test
    public void testAddVariable() {
        Variable.variableList = new ArrayList<>();
        assertTrue(Variable.addVariable("var1"));
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testAddVariableDuplicate() {
        Variable.variableList = new ArrayList<>();
        Variable v1 = new Variable("var1");
        Variable v2 = new Variable("var1");
        assertTrue(Variable.addVariable("var1"));
        assertTrue(Variable.addVariable("var1"));
        assertEquals(1, Variable.variableList.size());
        assertEquals(v1, Variable.variableList.get(0));
    }

    @Test
    public void testRemoveVariable() {
        Variable.variableList = new ArrayList<>();
        Variable v1 = new Variable("var1");
        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistentVariable() {
        Variable.variableList = new ArrayList<>();
        Variable.removeVariable("nonExistentVar");
    }

    @Test
    public void testGetVariableValue() {
        Variable.variableList = new ArrayList<>();
        Variable v1 = new Variable("var1");
        Variable v2 = new Variable("var2");
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 10);
        Variable.setVariableValue("var2", 20);
        assertEquals(10, Variable.getVariableValue("var1"));
        assertEquals(20, Variable.getVariableValue("var2"));
        assertEquals(0, Variable.getVariableValue("nonExistentVar"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.variableList = new ArrayList<>();
        Variable v1 = new Variable("var1");
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNonExistentVariableValue() {
        Variable.variableList = new ArrayList<>();
        Variable.setVariableValue("nonExistentVar", 10);
    }

    @Test
    public void testFindVariableIndex() {
        Variable.variableList = new ArrayList<>();
        Variable v1 = new Variable("var1");
        Variable v2 = new Variable("var2");
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        int index1 = Variable.findVariable("var1");
        int index2 = Variable.findVariable("var2");
        assertEquals(0, index1);
        assertEquals(1, index2);
        int index3 = Variable.findVariable("nonExistentVar");
        assertEquals(-1, index3);
    }

    @Test
    public void testFindVariableValueByReference() {
        Variable.variableList = new ArrayList<>();
        Variable v1 = new Variable("var1");
        Variable v2 = new Variable("&var1");
        Variable.addVariable("var1");
        Variable.addVariable("&var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("&var1"));
    }

    @Test
    public void testVariableListInitialization() {
        Variable.variableList = new ArrayList<>();
        Variable v1 = new Variable("var1");
        Variable v2 = new Variable("var2");
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        assertEquals(2, Variable.variableList.size());
    }

    @Test
    public void testVariableValueGettersAndSetters() {
        Variable v = new Variable("testVar");
        assertEquals(0, v.getValue());
        v.setValue(10);
        assertEquals(10, v.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVariableNullNameConstructor() {
        new Variable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVariableEmptyName() {
        new Variable(" ");
    }
}