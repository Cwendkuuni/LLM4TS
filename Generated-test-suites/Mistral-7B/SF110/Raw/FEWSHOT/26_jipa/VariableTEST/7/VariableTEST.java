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
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");

        assertEquals(1, Variable.variableList.size());
        assertFalse(Variable.variableList.contains(new Variable("var1")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullVariableName() {
        Variable.addVariable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableValueWithNullVariableName() {
        Variable.setVariableValue(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableValueWithNegativeVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindVariableWithNullVariableName() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.findVariable(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testFindVariableWithNonExistentVariableName() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.findVariable("var3");
    }

    @Test
    public void testFindVariableWithReferenceVariableName() {
        Variable.addVariable("&var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);

        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(1, Variable.getVariableValue("&var1"));
    }

    @Test
    public void testVariableListInitialization() {
        assertEquals(0, Variable.variableList.size());
    }
}