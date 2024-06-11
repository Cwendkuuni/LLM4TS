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
    public void testAddVariableAndGetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.addVariable("var3");

        assertEquals(0, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("var2"));
        assertEquals(0, Variable.getVariableValue("var3"));

        Variable.setVariableValue("var1", 10);
        Variable.setVariableValue("var2", 20);

        assertEquals(10, Variable.getVariableValue("var1"));
        assertEquals(20, Variable.getVariableValue("var2"));
        assertEquals(0, Variable.getVariableValue("var3"));
    }

    @Test
    public void testAddVariableWithExistingName() {
        Variable.addVariable("var1");
        Variable.addVariable("var1");

        assertTrue(Variable.variableList.size() == 1);
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");

        assertEquals(1, Variable.variableList.size());
        assertSame("var2", Variable.variableList.get(0).getName());
    }

    @Test
    public void testRemoveNonExistingVariable() {
        Variable.addVariable("var1");
        Variable.removeVariable("var2");

        assertEquals(1, Variable.variableList.size());
        assertSame("var1", Variable.variableList.get(0).getName());

        assertNull(Variable.findVariable("var2"));
    }

    @Test
    public void testGetVariableWithStarPrefix() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 10);

        assertEquals(10, Variable.getVariableValue("*var1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddVariableWithNullName() {
        Variable.addVariable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableValueWithNullName() {
        Variable.setVariableValue(null, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableValueWithNegativeValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", -1);
    }

    @Test
    public void testFindVariableWithIsReferenceSet() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.addVariable("&var1");

        assertEquals(2, Variable.variableList.size());
        assertEquals("var1", Variable.findVariable("&var1").getName());
        assertEquals(10, Variable.findVariable("&var1").getValue());
    }

    @Test
    public void testFindVariableWithIsReferenceNotSet() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");

        assertEquals("var1", Variable.findVariable("var1").getName());
        assertEquals(0, Variable.findVariable("var1").getValue());
    }

    @Test
    public void testFindVariableWithNullName() {
        Variable.addVariable("var1");

        assertNull(Variable.findVariable(null));
    }

    private Variable findVariable(String varName) {
        final List<Variable> variableList = Variable.variableList;
        final ListIterator<Variable> i = variableList.listIterator();
        final boolean isReference = varName.startsWith("&");

        if (isReference) {
            varName = varName.substring(1);
        }

        while (i.hasNext()) {
            final Variable curVariable = i.next();
            if (curVariable.getName().equals(varName)) {
                if (!isReference) {
                    return curVariable;
                }
                return curVariable.getValue();
            }
        }

        return null;
    }
}