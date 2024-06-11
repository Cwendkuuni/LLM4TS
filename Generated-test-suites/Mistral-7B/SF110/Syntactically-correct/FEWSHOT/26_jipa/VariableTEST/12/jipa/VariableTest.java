package jipa;

import static org.junit.Assert.*;
import java.util.*;

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
        assertFalse(Variable.addVariable("var1"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValueWithStar() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 10);
        Variable.setVariableValue("var2", 20);

        assertEquals(10, Variable.getVariableValue("*var1"));
        assertEquals(20, Variable.getVariableValue("*var2"));
    }

    @Test
    public void testFindVariableIndex() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        int index1 = Variable.findVariable("var1");
        assertEquals(0, index1);

        int index2 = Variable.findVariable("var2");
        assertEquals(1, index2);

        int index3 = Variable.findVariable("nonExistentVar");
        assertEquals(-1, index3);
    }

    @Test
    public void testGetReferenceVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("&var1");
        Variable.setVariableValue("var1", 10);

        assertEquals(10, Variable.getVariableValue("var1"));
        assertEquals(10, Variable.getVariableValue("&var1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindVariableWithNullName() {
        Variable.findVariable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableValueWithNullName() {
        Variable.setVariableValue(null, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetVariableValueWithNegativeIndex() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 10);
        Variable.setVariableValue("var2", 20);

        Variable.getVariableValue(-1);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetNonExistentVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 10);
        Variable.setVariableValue("var2", 20);

        Variable.getVariableValue("nonExistentVar");
    }

    @Test
    public void testInitialization() {
        Variable.variableList = new ArrayList<Variable>();
        assertTrue(Variable.variableList.isEmpty());
    }
}