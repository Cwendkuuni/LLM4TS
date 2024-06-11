package jipa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrueWithMessage;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class VariableTest {

    @Test
    public void testVariableListInitialization() {
        assertTrue(Variable.variableList instanceof ArrayList);
        assertTrue(Variable.variableList.isEmpty());
    }

    @Test
    public void testVariableCreation() {
        Variable variable1 = new Variable("var1");
        Variable variable2 = new Variable("var2");

        List<Variable> expectedVariableList = new ArrayList<>();
        expectedVariableList.add(variable1);
        expectedVariableList.add(variable2);

        Variable.addVariable("var1");
        Variable.addVariable("var2");

        List<Variable> variableList = Variable.variableList;

        assertEquals(expectedVariableList, variableList);
    }

    @Test
    public void testAddVariableWithExistingVariable() {
        Variable variable1 = new Variable("var1");
        Variable variable2 = new Variable("var1");

        List<Variable> expectedVariableList = new ArrayList<>();
        expectedVariableList.add(variable1);

        Variable.addVariable("var1");
        Variable.addVariable("var1");

        List<Variable> variableList = Variable.variableList;

        assertEquals(expectedVariableList, variableList);
        assertTrueWithMessage("Expected only one instance of 'var1'", variableList.size() == 1);
    }

    @Test
    public void testAddVariableWithNullVariableName() {
        assertFalse(Variable.addVariable(null));
    }

    @Test
    public void testGetVariableValueWithUnknownVariable() {
        Variable variable1 = new Variable("var1");
        Variable variable2 = new Variable("var2");

        Variable.addVariable("var1");
        Variable.addVariable("var2");

        int variableValue = Variable.getVariableValue("var3");

        assertEquals(0, variableValue);
    }

    @Test
    public void testGetVariableValueWithKnownVariable() {
        Variable variable1 = new Variable("var1");
        Variable variable2 = new Variable("var2");

        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 5);

        int variableValue = Variable.getVariableValue("var1");

        assertEquals(5, variableValue);
    }

    @Test
    public void testSetVariableValueWithUnknownVariable() {
        Variable.setVariableValue("var1", 5);

        assertNull(Variable.findVariable("var1"));
    }

    @Test
    public void testSetVariableValueWithExistingVariable() {
        Variable variable1 = new Variable("var1");

        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);

        Variable foundVariable = Variable.findVariable("var1");

        assertTrue(foundVariable != null);
        assertEquals(5, foundVariable.getValue());
    }

    @Test
    public void testSetVariableValueWithNullVariableName() {
        assertFalse(Variable.setVariableValue(null, 5));
    }

    @Test
    public void testRemoveVariableWithUnknownVariable() {
        Variable variable1 = new Variable("var1");
        Variable variable2 = new Variable("var2");

        Variable.addVariable("var1");
        Variable.addVariable("var2");

        Variable.removeVariable("var3");

        List<Variable> variableList = Variable.variableList;

        assertEquals(2, variableList.size());
    }

    @Test
    public void testRemoveVariableWithKnownVariable() {
        Variable variable1 = new Variable("var1");
        Variable variable2 = new Variable("var2");

        Variable.addVariable("var1");
        Variable.addVariable("var2");

        Variable.removeVariable("var1");

        List<Variable> variableList = Variable.variableList;

        assertEquals(1, variableList.size());
        assertTrueWithMessage("Expected 'var2' to remain in the list", variableList.get(0).getName().equals("var2"));
    }

    @Test
    public void testFindVariableWithUnknownVariable() {
        Variable variable1 = new Variable("var1");
        Variable variable2 = new Variable("var2");

        Variable.addVariable("var1");
        Variable.addVariable("var2");

        int variableIndex = Variable.findVariable("var3");

        assertEquals(-1, variableIndex);
    }

    @Test
    public void testFindVariableWithKnownVariable() {
        Variable variable1 = new Variable("var1");
        Variable variable2 = new Variable("var2");

        Variable.addVariable("var1");
        Variable.addVariable("var2");

        int variableIndex = Variable.findVariable("var1");

        assertEquals(0, variableIndex);
    }

    @Test
    public void testFindVariableWithReferenceToKnownVariable() {
        Variable variable1 = new Variable("var1");
        Variable variable2 = new Variable("&var1");

        Variable.addVariable("var1");
        Variable.addVariable("&var1");

        int variableIndex = Variable.findVariable("var1");

        assertEquals(0, variableIndex);
    }

    @Test
    public void testFindVariableWithNullVariableName() {
        Variable variable1 = new Variable("var1");
        Variable variable2 = new Variable("var2");

        Variable.addVariable("var1");
        Variable.addVariable("var2");

        assertNull(Variable.findVariable(null));
    }
}