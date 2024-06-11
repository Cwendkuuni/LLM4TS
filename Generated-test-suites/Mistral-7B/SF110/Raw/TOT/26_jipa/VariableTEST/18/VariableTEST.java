package jipa.test;

import jipa.Variable;
import org.junit.*;
import static org.junit.Assert.*;

public class VariableTest {

    private List<Variable> variableList;

    @Before
    public void setUp() {
        Variable.variableList = new ArrayList<>();
    }

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var1"));
        assertEquals(1, Variable.variableList.size());
        assertNull(Variable.findVariable("nonExistingVar"));
        assertFalse(Variable.addVariable("var1"));
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 42);

        assertEquals(42, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("nonExistingVar"));
        assertEquals(0, Variable.getVariableValue("*"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 42);

        assertEquals(42, Variable.getVariableValue("var1"));
        Variable.setVariableValue("var1", 7);
        assertEquals(7, Variable.getVariableValue("var1"));

        Variable.removeVariable("var1");
        assertNull(Variable.findVariable("var1"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        Variable.removeVariable("var1");
        assertEquals(1, Variable.variableList.size());
        assertNull(Variable.findVariable("var1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableValueWithNullName() {
        Variable.addVariable("var1");
        Variable.setVariableValue(null, 42);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableValueWithNegativeValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetVariableValueWithNonExistingVar() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        Variable.getVariableValue("nonExistingVar");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetVariableValueWithNonExistingVar() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        Variable.setVariableValue("nonExistingVar", 42);
    }

    @Test(expected = NullPointerException.class)
    public void testFindVariableWithNullName() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        Variable.findVariable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindVariableWithEmptyName() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        Variable.findVariable("");
    }

    @Test
    public void testFindVariableWithAmpersand() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        assertEquals(Variable.findVariable("var2").getValue(), Variable.findVariable("&var2").getValue());
    }
}