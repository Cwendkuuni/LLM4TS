package jipa;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class VariableTest {

    @Test
    public void testAddVariable() {
        Variable.addVariable("var1");
        assertTrue(Variable.variableList.size() > 0);
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        assertFalse(Variable.variableList.contains("var1"));

        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertTrue(Variable.variableList.size() == 1);
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetVariableValueNonExistentVariable() {
        Variable.addVariable("var1");
        assertEquals(0, Variable.getVariableValue("var2"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetVariableValueNonExistentVariable() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var2", 10);
    }

    @Test
    public void testSetVariableValueNullVariableName() {
        Variable.addVariable("var1");
        Variable.setVariableValue(null, 10);
        assertEquals(0, Variable.getVariableValue("var1"));
    }

    @Test
    public void testSetVariableValueEmptyVariableName() {
        Variable.addVariable("var1");
        Variable.setVariableValue("", 10);
        assertEquals(0, Variable.getVariableValue("var1"));
    }

    @Test
    public void testSetVariableValueSpecialCharacters() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var%1$", 10);
        assertEquals(0, Variable.getVariableValue("var%1$"));
    }

    @Test
    public void testSetVariableValueMinValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, Variable.getVariableValue("var1"));
    }

    @Test
    public void testSetVariableValueMaxValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, Variable.getVariableValue("var1"));
    }
}