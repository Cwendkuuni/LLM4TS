import org.junit.Assert;
import org.junit.Test;
import jipa.Variable;

public class VariableTest {

    @Test
    public void testConstructor() {
        Variable variable = new Variable("testVar");
        Assert.assertEquals("testVar", variable.getName());
        Assert.assertEquals(0, variable.getValue());
    }

    @Test
    public void testAddVariableWithSameNameMultipleTimes() {
        Variable.variableList.add(new Variable("testVar1"));
        Variable.addVariable("testVar1");
        Variable.addVariable("testVar1");
        Assert.assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testGetName() {
        Variable variable = new Variable("testVar");
        Assert.assertEquals("testVar", variable.getName());
    }

    @Test
    public void testGetValue() {
        Variable variable = new Variable("testVar");
        Assert.assertEquals(0, variable.getValue());
    }

    @Test
    public void testSetValue() {
        Variable variable = new Variable("testVar");
        variable.setValue(10);
        Assert.assertEquals(10, variable.getValue());
    }

    @Test
    public void testAddVariableWithNullName() {
        Assert.assertFalse(Variable.addVariable(null));
    }

    @Test
    public void testAddVariableWithNegativeNameLength() {
        Assert.assertFalse(Variable.addVariable("abcdefghijklmnopqrstuvwxyz-"));
    }

    // Test other methods with similar test cases

    @Test
    public void testGetVariableValueWithNonExistentVariable() {
        Variable.addVariable("testVar1");
        Variable.addVariable("testVar2");
        Assert.assertEquals(0, Variable.getVariableValue("testVar3"));
    }

    @Test
    public void testSetVariableValueWithNonExistentVariable() {
        Variable.addVariable("testVar1");
        Variable.setVariableValue("testVar2", 10);
        Assert.assertEquals(10, Variable.getVariableValue("testVar1"));
        Assert.assertEquals(0, Variable.getVariableValue("testVar2"));
    }

    @Test
    public void testSetVariableValueWithNegativeValue() {
        Variable.addVariable("testVar1");
        Variable.setVariableValue("testVar1", -1);
        Assert.assertEquals(0, Variable.getVariableValue("testVar1"));
    }

    @Test
    public void testSetVariableValueWithLargeValue() {
        Variable.addVariable("testVar1");
        Variable.setVariableValue("testVar1", Integer.MAX_VALUE);
        Assert.assertEquals(Integer.MAX_VALUE, Variable.getVariableValue("testVar1"));
    }

    // Test other edge cases and exceptions with similar test cases

}