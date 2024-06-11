import static org.junit.Assert.*;
import jipa.Variable;

public class VariableTest {
    @Test
    public void testVariableConstructor() {
        Variable variable = new Variable("testVariable");
        assertEquals("testVariable", variable.getName());
        assertEquals(0, variable.getValue());
    }

    @Test
    public void testGetName() {
        Variable variable = new Variable("testVariable");
        assertEquals("testVariable", variable.getName());
    }

    @Test
    public void testGetValue() {
        Variable variable = new Variable("testVariable");
        assertEquals(0, variable.getValue());
    }

    @Test
    public void testSetValue() {
        Variable variable = new Variable("testVariable");
        variable.setValue(5);
        assertEquals(5, variable.getValue());
    }

    @Test
    public void testAddVariable() {
        Variable.addVariable("testVariable1");
        Variable.addVariable("testVariable2");
        assertEquals(2, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("testVariable1");
        Variable.removeVariable("testVariable1");
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable variable1 = new Variable("testVariable1");
        Variable.addVariable("testVariable1");
        Variable.setVariableValue("testVariable1", 5);
        assertEquals(5, Variable.getVariableValue("testVariable1"));
    }

    @Test
    public void testSetVariableValue() {
        Variable variable = new Variable("testVariable");
        Variable.addVariable("testVariable");
        Variable.setVariableValue("testVariable", 5);
        assertEquals(5, Variable.getVariableValue("testVariable"));
    }

    @Test
    public void testFindVariable() {
        Variable variable1 = new Variable("testVariable1");
        Variable.addVariable("testVariable1");
        Variable variable2 = new Variable("testVariable2");
        Variable.addVariable("testVariable2");
        assertEquals(0, Variable.findVariable("nonexistentVariable"));
        assertEquals(1, Variable.findVariable("testVariable1"));
        assertEquals(5, Variable.findVariable("testVariable2").getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVariableConstructorNullVarName() {
        new Variable(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetValueNullVar() {
        Variable variable = new Variable("testVariable");
        variable.setValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddVariableNullVarName() {
        Variable.addVariable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveVariableNullVarName() {
        Variable.removeVariable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetVariableValueNullVarName() {
        Variable.getVariableValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableValueNullVarName() {
        Variable.setVariableValue(null, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindVariableNullVarName() {
        Variable.findVariable(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testFindVariableNonExistentVariable() {
        Variable variable1 = new Variable("testVariable1");
        Variable.addVariable("testVariable1");
        Variable.addVariable("testVariable2");
        assertEquals(-1, Variable.findVariable("nonexistentVariable"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddVariableDuplicateVariable() {
        Variable.addVariable("testVariable1");
        Variable.addVariable("testVariable1");
    }
}