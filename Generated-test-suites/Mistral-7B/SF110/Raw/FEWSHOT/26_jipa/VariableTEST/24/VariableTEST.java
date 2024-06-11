package jipa;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class VariableTest {

    @Test
    public void testAddVariableAndGetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
    }

    @Test
    public void testAddDuplicateVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var1");
        assertEquals(1, Variable.variableList.size());
        assertFalse(Variable.addVariable("var1"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertEquals(1, Variable.variableList.size());
        assertFalse(Variable.getVariableValue("var1") > 0);
        Variable.removeVariable("var2");
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.addVariable("&var1");
        final int index1 = Variable.findVariable("var1");
        final int index2 = Variable.findVariable("var2");
        final int index3 = Variable.findVariable("&var1");
        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(2, index3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindVariableWithInvalidName() {
        Variable.addVariable("var1");
        Variable.findVariable("invalidName");
    }

    @Test
    public void testGetVariableValueWithInvalidName() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        assertEquals(0, Variable.getVariableValue("invalidName"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
        Variable.setVariableValue("var1", 3);
        assertEquals(3, Variable.getVariableValue("var1"));
    }

    @Test
    public void testVariableListInitialization() {
        assertEquals(0, Variable.variableList.size());
    }
}