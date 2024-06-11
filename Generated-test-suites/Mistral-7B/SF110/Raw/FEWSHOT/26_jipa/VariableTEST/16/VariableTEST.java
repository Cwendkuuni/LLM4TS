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
    public void testAddVariableAndRemoveVariable() {
        Variable.variableList = new ArrayList<Variable>();

        String varName1 = "var1";
        String varName2 = "var2";
        String varName3 = "var1";

        assertTrue(Variable.addVariable(varName1));
        assertEquals(1, Variable.variableList.size());
        assertSame(varName1, Variable.variableList.get(0).getName());
        assertNull(Variable.getVariableValue(varName1));

        assertTrue(Variable.addVariable(varName2));
        assertEquals(2, Variable.variableList.size());
        assertSame(varName2, Variable.variableList.get(1).getName());
        assertNull(Variable.getVariableValue(varName2));

        assertFalse(Variable.addVariable(varName1));
        assertEquals(2, Variable.variableList.size());

        Variable.removeVariable(varName2);
        assertEquals(1, Variable.variableList.size());
        assertSame(varName1, Variable.variableList.get(0).getName());
    }

    @Test
    public void testGetVariableValue() {
        Variable.variableList = new ArrayList<Variable>();

        String varName1 = "var1";
        Variable var1 = new Variable(varName1);
        Variable.variableList.add(var1);
        var1.setValue(10);

        String varName2 = "var2";
        Variable.addVariable(varName2);

        int varValue1 = Variable.getVariableValue(varName1);
        assertEquals(10, varValue1);

        int varValue2 = Variable.getVariableValue(varName2);
        assertEquals(0, varValue2);

        String varName3 = "*var1";
        int varValue3 = Variable.getVariableValue(varName3);
        assertEquals(10, varValue3);
    }

    @Test
    public void testSetVariableValue() {
        Variable.variableList = new ArrayList<Variable>();

        String varName1 = "var1";
        Variable var1 = new Variable(varName1);
        Variable.variableList.add(var1);

        String varName2 = "var2";
        Variable.addVariable(varName2);

        Variable.setVariableValue(varName1, 10);
        assertEquals(10, Variable.getVariableValue(varName1));

        Variable.setVariableValue(varName2, 20);
        assertEquals(20, Variable.getVariableValue(varName2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddVariableWithExistingName() {
        Variable.variableList = new ArrayList<Variable>();

        String varName1 = "var1";
        Variable.addVariable(varName1);

        String varName2 = "var1";
        Variable.addVariable(varName2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindVariableWithNullName() {
        Variable.variableList = new ArrayList<Variable>();
        String varName = null;
        Variable.findVariable(varName);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testFindVariableWithNonExistingName() {
        Variable.variableList = new ArrayList<Variable>();

        String varName = "nonExistentVar";
        Variable.findVariable(varName);
    }

    @Test
    public void testFindVariableWithReferenceName() {
        Variable.variableList = new ArrayList<Variable>();

        String varNameRef = "&var1";
        Variable var1 = new Variable("var1");
        Variable.variableList.add(var1);

        int varValue = Variable.getVariableValue("var1");
        assertEquals(0, varValue);

        int refValue = Variable.getVariableValue(varNameRef);
        assertEquals(0, refValue);

        Variable.setVariableValue("var1", 10);

        varValue = Variable.getVariableValue("var1");
        assertEquals(10, varValue);

        refValue = Variable.getVariableValue(varNameRef);
        assertEquals(10, refValue);
    }

    @Test
    public void testInitialization() {
        Variable.variableList = new ArrayList<Variable>();
        assertEquals(0, Variable.variableList.size());
        assertNull(Variable.findVariable("var1"));
    }
}