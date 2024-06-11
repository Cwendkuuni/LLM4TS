
package jipa;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class VariableTest {

    @Test
    public void testGetValue() {
        Variable variable = new Variable("testVariable");
        int expectedValue = 0;
        int actualValue = variable.getValue();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testSetValue() {
        Variable variable = new Variable("testVariable");
        int newValue = 5;
        variable.setValue(newValue);
        int expectedValue = newValue;
        int actualValue = variable.getValue();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetName() {
        Variable variable = new Variable("testVariable");
        String expectedName = "testVariable";
        String actualName = variable.getName();
        assertEquals(expectedName, actualName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddVariableEmptyName() {
        Variable.variableList.clear();
        String emptyName = "";
        Variable.addVariable(emptyName);
    }

    @Test
    public void testRemoveNonexistentVariable() {
        Variable.variableList.clear();
        Variable.addVariable("testVariable");
        Variable.removeVariable("nonExistentVariable");
        int expectedSize = 0;
        int actualSize = Variable.variableList.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testGetValueNonExistentVariable() {
        Variable.variableList.clear();
        Variable.addVariable("testVariable");
        int expectedValue = 0;
        int actualValue = Variable.getVariableValue("nonExistentVariable");
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testSetValueNonExistentVariable() {
        Variable.variableList.clear();
        Variable.addVariable("testVariable");
        int newValue = 5;
        Variable.setVariableValue("nonExistentVariable", newValue);
        int expectedSize = 1;
        int actualSize = Variable.variableList.size();
        assertEquals(expectedSize, actualSize);
        Variable testVariable = Variable.variableList.get(0);
        int expectedValue = 0;
        int actualValue = testVariable.getValue();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testFindVariableByReference() {
        Variable.variableList.clear();
        Variable variable1 = new Variable("testVariable1");
        Variable variable2 = new Variable("testVariable2");
        Variable.addVariable("&testVariable1");
        Variable.addVariable("testVariable2");
        int expectedIndex = 0;
        int actualIndex = Variable.findVariable("testVariable1").intValue();
        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testAddVariableDuplicateName() {
        Variable.variableList.clear();
        String duplicateName = "testVariable";
        Variable.addVariable(duplicateName);
        Variable.addVariable(duplicateName);
        int expectedSize = 1;
        int actualSize = Variable.variableList.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testGetVariableValueMultipleVariables() {
        Variable.variableList.clear();
        Variable variable1 = new Variable("testVariable1");
        Variable variable2 = new Variable("testVariable2");
        Variable.addVariable("testVariable1");
        Variable.addVariable("testVariable2");
        int expectedValue1 = 0;
        int expectedValue2 = 0;
        int actualValue1 = Variable.getVariableValue("testVariable1");
        int actualValue2 = Variable.getVariableValue("testVariable2");
        assertEquals(expectedValue1, actualValue1);
        assertEquals(expectedValue2, actualValue2);
    }

    @Test
    public void testSetVariableValueMultipleVariables() {
        Variable.variableList.clear();
        Variable variable1 = new Variable("testVariable1");
        Variable variable2 = new Variable("testVariable2");
        Variable.addVariable("testVariable1");
        Variable.addVariable("testVariable2");
        int initialValue1 = 0;
        int initialValue2 = 0;
        Variable.setVariableValue("testVariable1", 10);
        Variable.setVariableValue("testVariable2", 20);
        int expectedValue1 = 10;
        int expectedValue2 = 20;
        int actualValue1 = Variable.getVariableValue("testVariable1");
        int actualValue2 = Variable.getVariableValue("testVariable2");
        assertEquals(expectedValue1, actualValue1);
        assertEquals(expectedValue2, actualValue2);
    }
}
