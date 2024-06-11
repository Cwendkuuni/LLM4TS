package jipa;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class VariableTest {

    @Test
    public void testAddVariableAndGetValue() {
        Variable.addVariable("var1");
        Variable var1 = Variable.variableList.get(0);
        assertEquals("var1", var1.getName());
        assertEquals(0, var1.getValue());

        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
    }

    @Test
    public void testAddDuplicateVariable() {
        Variable.addVariable("var1");
        boolean result = Variable.addVariable("var1");
        assertFalse(result);
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValueWithReference() {
        Variable.addVariable("var1");
        Variable.addVariable("&var1");
        Variable var1 = Variable.variableList.get(0);
        Variable refVar1 = Variable.variableList.get(1);

        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        assertEquals(10, Variable.getVariableValue("&var1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddVariableWithNullName() {
        Variable.addVariable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableValueWithNullName() {
        Variable.setVariableValue(null, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetVariableValueWithNullName() {
        Variable.getVariableValue(null);
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        int index1 = Variable.findVariable("var1");
        assertEquals(0, index1);

        int index2 = Variable.findVariable("var2");
        assertEquals(1, index2);

        int index3 = Variable.findVariable("var3");
        assertEquals(-1, index3);

        int index4 = Variable.findVariable("&var1");
        assertEquals(1, index4); // The reference is considered the next element in the list
    }

    @Test
    public void testInitVariableList() {
        assertEquals(0, Variable.variableList.size());
    }
}