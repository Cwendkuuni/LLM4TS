package jipa.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class VariableTest {
    private static List<Variable> variableList;
    private Variable testVariable;

    @BeforeClass
    public static void setUpClass() {
        Variable.variableList = new ArrayList<>();
    }

    @Before
    public void setUp() {
        String varName = "testVar";
        Variable.addVariable(varName);
        testVariable = Variable.variableList.get(0);
        testVariable.setValue(42);
    }

    @Test
    public void testAddVariable() {
        String varName = "testVar";
        assertTrue(Variable.addVariable(varName));
        assertEquals(1, Variable.variableList.size());
        Variable testVariable = Variable.variableList.get(0);
        assertEquals(varName, testVariable.getName());
        assertEquals(0, testVariable.getValue());
    }

    @Test
    public void testGetVariableValue() {
        String varName = "testVar";
        assertEquals(testVariable.getValue(), Variable.getVariableValue(varName));
    }

    @Test
    public void testGetVariableValue_InvalidName() {
        String varName = "emptyVar";
        assertEquals(0, Variable.getVariableValue(varName));
    }

    @Test
    public void testGetVariableValue_StarNotation() {
        Variable.addVariable("testVar1");
        Variable.addVariable("testVar2");
        Variable.setVariableValue("testVar1", 10);
        assertEquals(10, Variable.getVariableValue("*testVar1"));
    }

    @Test
    public void testSetVariableValue() {
        int newValue = 123;
        Variable.setVariableValue(varName, newValue);
        assertEquals(newValue, testVariable.getValue());
    }

    @Test
    public void testSetVariableValue_InvalidName() {
        String varName = "emptyVar";
        assertFalse(Variable.addVariable(varName));
        assertThrows(IllegalArgumentException.class, () -> {
            Variable.setVariableValue(varName, 123);
        });
    }

    @Test
    public void testSetVariableValue_AlreadySet() {
        int initialValue = 42;
        Variable.setVariableValue(varName, initialValue);
        int newValue = 123;
        Variable.setVariableValue(varName, newValue);
        assertEquals(newValue, testVariable.getValue());
    }
}