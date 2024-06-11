package jipa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class VariableTest {

    @Test
    public void testAddVariableAndGetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);

        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
        assertEquals(0, Variable.getVariableValue("nonExistentVar"));
    }

    @Test
    public void testAddVariableWithExistingName() {
        Variable.addVariable("var1");
        Variable.addVariable("var1");

        assertTrue(Variable.variableList.size() == 1);
        assertEquals("var1", Variable.variableList.get(0).getName());
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");

        assertEquals(1, Variable.variableList.size());
        assertEquals("var2", Variable.variableList.get(0).getName());
    }

    @Test
    public void testFindVariableWithExistingName() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        final int index = Variable.findVariable("var1");
        final int index2 = Variable.findVariable("var2");

        assertTrue(index != -1);
        assertTrue(index2 != -1);
        assertTrue(index != index2);
    }

    @Test
    public void testFindVariableWithNonExistingName() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        final int index = Variable.findVariable("nonExistentVar");

        assertEquals(-1, index);
    }

    @Test
    public void testFindVariableWithReferenceName() {
        Variable var1 = new Variable("var1");
        Variable var2 = new Variable("var2");
        Variable.variableList.add(var1);
        Variable.variableList.add(var2);

        final int index = Variable.findVariable("&var1");

        assertTrue(index != -1);
        assertEquals(var1.getValue(), Variable.getVariableValue("&var1"));
    }

    @Test(expected = NullPointerException.class)
    public void testFindVariableWithNullName() {
        Variable.findVariable(null);
    }

    @Test
    public void testVariableListInitialization() {
        assertTrue(Variable.variableList == null || Variable.variableList.isEmpty());
    }

    @Test
    public void testVariableNameInitialization() {
        final Variable var = new Variable("varName");
        assertEquals("varName", var.getName());
    }

    @Test
    public void testVariableValueInitialization() {
        final Variable var = new Variable("varName");
        assertEquals(0, var.getValue());
    }

    @Test
    public void testValueSetting() {
        final Variable var = new Variable("varName");
        var.setValue(1);
        assertEquals(1, var.getValue());
    }

    @Test
    public void testValueGetting() {
        final Variable var = new Variable("varName");
        var.setValue(1);
        assertEquals(1, var.getValue());
    }

    @Test
    public void testValueGettingWithDefault() {
        final Variable var = new Variable("varName");
        assertEquals(0, var.getValue());
    }
}