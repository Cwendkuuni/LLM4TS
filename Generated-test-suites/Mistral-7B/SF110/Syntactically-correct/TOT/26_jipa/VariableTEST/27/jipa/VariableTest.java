package jipa.test;

import static org.junit.Assert.*;
import jipa.Variable;
import org.junit.*;

public class VariableTest {
    static {
        Variable.variableList = new ArrayList<>();
    }

    @BeforeClass
    public static void setUp() {
    }

    @Before
    public void setUpTestVariables() {
        Variable.variableList = new ArrayList<>();
    }

    @Test
    public void testAddVariable() {
        String varName = "testVar";
        assertTrue(Variable.addVariable(varName));
        assertTrue(Variable.variableList.size() == 1);
        Variable testVariable = Variable.variableList.get(0);
        assertTrue(testVariable.getName().equals(varName));
        assertTrue(testVariable.getValue() == 0);
    }

    @Test
    public void testGetVariableValue() {
        Variable testVariable = new Variable("testVar");
        Variable.variableList.add(testVariable);
        testVariable.setValue(42);
        int value = Variable.getVariableValue("testVar");
        assertTrue(value == 42);
    }

    @Test
    public void testGetNonExistentVariableValue() {
        int value = Variable.getVariableValue("nonExistentVar");
        assertTrue(value == 0);
    }

    @Test
    public void testRemoveVariable() {
        Variable testVariable = new Variable("testVar");
        Variable.variableList.add(testVariable);
        Variable.removeVariable("testVar");
        assertTrue(Variable.variableList.isEmpty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveNonExistentVariable() {
        Variable.removeVariable("nonExistentVar");
    }

    @Test
    public void testSetVariableValue() {
        Variable testVariable = new Variable("testVar");
        Variable.variableList.add(testVariable);
        Variable.setVariableValue("testVar", 42);
        int value = Variable.getVariableValue("testVar");
        assertTrue(value == 42);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetNonExistentVariableValue() {
        Variable.setVariableValue("nonExistentVar", 42);
    }
}