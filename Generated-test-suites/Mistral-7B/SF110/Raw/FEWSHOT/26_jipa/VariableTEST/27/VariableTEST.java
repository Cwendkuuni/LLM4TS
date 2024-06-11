package jipa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class VariableTest {

    @Test
    public void testVariableListInitialization() {
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testAddVariableSuccess() {
        Variable.addVariable("var1");
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testAddVariableFailure() {
        Variable.addVariable("var1");
        assertFalse(Variable.addVariable("var1"));
    }

    @Test
    public void testRemoveVariableSuccess() {
        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariableFailure() {
        Variable.addVariable("var1");
        Variable.removeVariable("var2");
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValueStringSuccess() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("var1"));
    }

    @Test
    public void testGetVariableValueStringFailure() {
        Variable.addVariable("var1");
        assertEquals(0, Variable.getVariableValue("var2"));
    }

    @Test
    public void testGetVariableValueStarSuccess() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("*var1"));
    }

    @Test
    public void testGetVariableValueStarFailure() {
        Variable.addVariable("var1");
        assertEquals(0, Variable.getVariableValue("*var2"));
    }

    @Test
    public void testSetVariableValueSuccess() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("var1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableValueNullName() {
        Variable.addVariable("var1");
        Variable.setVariableValue(null, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableValueNegativeValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", -1);
    }

    @Test
    public void testFindVariableSuccess() {
        Variable.addVariable("var1");
        Variable variable = new Variable("var1");
        int index = Variable.findVariable("var1");
        assertTrue(index > -1);
        assertSame(variable, Variable.variableList.get(index));
    }

    @Test
    public void testFindVariableFailure() {
        Variable.addVariable("var1");
        assertNull(Variable.findVariable("var2"));
    }

    @Test
    public void testFindVariableReferenceSuccess() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.findVariable("&var1").getValue());
    }

    @Test
    public void testFindVariableReferenceFailure() {
        Variable.addVariable("var1");
        assertNull(Variable.findVariable("&var2"));
    }
}