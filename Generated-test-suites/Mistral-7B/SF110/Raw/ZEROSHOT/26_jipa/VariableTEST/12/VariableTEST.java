package jipa;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class VariableTest
{
    private List<Variable> variableList;

    @Before
    public void setUp()
    {
        Variable.variableList = new ArrayList<Variable>();
    }

    @Test
    public void testConstructor()
    {
        Variable variable = new Variable("testVar");
        assertNotNull(variable);
        assertEquals("testVar", variable.getName());
        assertEquals(0, variable.getValue());
    }

    @Test
    public void testAddVariable()
    {
        Variable.addVariable("testVar1");
        Variable.addVariable("testVar2");
        assertEquals(2, Variable.variableList.size());
        assertTrue(Variable.addVariable("testVar1"));
        assertFalse(Variable.addVariable("testVar1"));
    }

    @Test
    public void testRemoveVariable()
    {
        Variable.addVariable("testVar1");
        Variable.addVariable("testVar2");
        Variable.removeVariable("testVar1");
        assertEquals(1, Variable.variableList.size());
        assertFalse(Variable.removeVariable("testVar2"));
        Variable.removeVariable("testVar2");
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue()
    {
        Variable.addVariable("testVar1");
        Variable.addVariable("testVar2");
        Variable.setVariableValue("testVar1", 5);
        Variable.setVariableValue("testVar2", 10);
        assertEquals(5, Variable.getVariableValue("testVar1"));
        assertEquals(10, Variable.getVariableValue("testVar2"));
        assertEquals(0, Variable.getVariableValue("nonexistentVar"));
    }

    @Test
    public void testSetVariableValue()
    {
        Variable.addVariable("testVar1");
        Variable.addVariable("testVar2");
        Variable.setVariableValue("testVar1", 5);
        Variable.setVariableValue("testVar2", 10);
        assertEquals(5, Variable.getVariableValue("testVar1"));
        assertEquals(10, Variable.getVariableValue("testVar2"));
        Variable.setVariableValue("testVar1", 7);
        assertEquals(7, Variable.getVariableValue("testVar1"));
    }

    @Test
    public void testFindVariable()
    {
        Variable.addVariable("testVar1");
        Variable.addVariable("testVar2");
        Variable.addVariable("testVar3");
        Variable.setVariableValue("testVar1", 5);
        Variable.setVariableValue("testVar2", 10);
        Variable.setVariableValue("testVar3", 15);

        // Test with exact name
        Variable variable1 = Variable.findVariable("testVar1");
        assertNotNull(variable1);
        assertEquals("testVar1", variable1.getName());
        assertEquals(5, variable1.getValue());

        // Test with reference
        Variable variable2 = Variable.findVariable("&testVar1");
        assertNotNull(variable2);
        assertEquals(5, variable2.getValue());

        // Test with non-existent name
        Variable variable3 = Variable.findVariable("nonexistentVar");
        assertNull(variable3);

        // Test with name starting with '&'
        Variable variable4 = Variable.findVariable("&testVar1");
        assertEquals(5, variable4.getValue());
    }

    @After
    public void tearDown()
    {
        Variable.variableList.clear();
    }
}