
package jipa;

import org.junit.Assert;
import org.junit.Test;

public class VariableTest {
    @Test
    public void testGetValue() {
        Variable variable = new Variable("testVariable");
        variable.setValue(10);
        int expectedValue = 10;
        int actualValue = variable.getValue();
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testAddVariableAndGetVariableValue() {
        Variable variable1 = new Variable("variable1");
        Variable variable2 = new Variable("variable2");
        Variable.addVariable("variable1");
        Variable.addVariable("variable2");
        int expectedValue1 = 0;
        int expectedValue2 = 0;
        int actualValue1 = Variable.getVariableValue("variable1");
        int actualValue2 = Variable.getVariableValue("variable2");
        Assert.assertEquals(expectedValue1, actualValue1);
        Assert.assertEquals(expectedValue2, actualValue2);
    }

    @Test
    public void testAddVariableThrowsExceptionWhenListIsEmpty() {
        Variable variable = new Variable("testVariable");
        List<Variable> emptyVariableList = new ArrayList<>();
        Variable.variableList = emptyVariableList;
        Assert.assertThrows(IndexOutOfBoundsException.class, () -> Variable.addVariable("testVariable"));
    }

    @Test
    public void testAddVariableThrowsExceptionWhenNullStringPassed() {
        Variable variable = new Variable("testVariable");
        Assert.assertThrows(IllegalArgumentException.class, () -> Variable.addVariable(null));
    }

    @Test
    public void testRemoveVariableThrowsExceptionWhenListIsEmpty() {
        Variable variable = new Variable("testVariable");
        List<Variable> emptyVariableList = new ArrayList<>();
        Variable.variableList = emptyVariableList;
        Assert.assertThrows(IndexOutOfBoundsException.class, () -> Variable.removeVariable("testVariable"));
    }

    @Test
    public void testGetVariableValueWithWildcardName() {
        Variable variable1 = new Variable("variable1");
        Variable variable2 = new Variable("variable2");
        Variable.addVariable("variable1");
        Variable.addVariable("variable2");
        Variable.setVariableValue("variable1", 10);
        Variable.setVariableValue("variable2", 20);
        int expectedValue = 10;
        int actualValue = Variable.getVariableValue("*");
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testSetVariableValueSetsCorrectValue() {
        Variable variable = new Variable("testVariable");
        Variable.addVariable("testVariable");
        int expectedValue = 10;
        Variable.setVariableValue("testVariable", 10);
        int actualValue = Variable.getVariableValue("testVariable");
        Assert.assertEquals(expectedValue, actualValue);
    }
}