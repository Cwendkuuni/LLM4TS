package jipa;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class VariableTest {

    @Test
    public void testAddVariable() {
        Variable.variableList = new ArrayList<>();
        assertTrue(Variable.addVariable("var1"));
        assertEquals(1, Variable.variableList.size());
        assertFalse(Variable.addVariable("var1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddVariableNullName() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable(null);
    }

    @Test
    public void testRemoveVariable() {
        Variable.variableList = new ArrayList<>();
        Variable var1 = new Variable("var1");
        Variable var2 = new Variable("var2");
        Variable.variableList.add(var1);
        Variable.variableList.add(var2);
        assertEquals(2, Variable.variableList.size());
        Variable.removeVariable("var1");
        assertEquals(1, Variable.variableList.size());
        assertNull(Variable.findVariable("var1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveVariableNullName() {
        Variable.variableList = new ArrayList<>();
        Variable.removeVariable(null);
    }

    @Test
    public void testGetVariableValue() {
        Variable.variableList = new ArrayList<>();
        Variable var1 = new Variable("var1");
        Variable var2 = new Variable("var2");
        Variable var3 = new Variable("var3");
        Variable.variableList.add(var1);
        Variable.variableList.add(var2);
        Variable.variableList.add(var3);
        Variable.setVariableValue("var2", 10);
        assertEquals(0, Variable.getVariableValue("var1"));
        assertEquals(10, Variable.getVariableValue("var2"));
        assertEquals(0, Variable.getVariableValue("var3"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.variableList = new ArrayList<>();
        Variable var1 = new Variable("var1");
        Variable.variableList.add(var1);
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVariableValueNullName() {
        Variable.variableList = new ArrayList<>();
        Variable var1 = new Variable("var1");
        Variable.variableList.add(var1);
        Variable.setVariableValue(null, 10);
    }

    @Test
    public void testFindVariable() {
        Variable.variableList = new ArrayList<>();
        Variable var1 = new Variable("var1");
        Variable var2 = new Variable("var2");
        Variable.variableList.add(var1);
        Variable.variableList.add(var2);
        assertEquals(0, Variable.findVariable("var2"));
        assertEquals(1, Variable.findVariable("var1"));
        assertNull(Variable.findVariable("var3"));
    }

    @Test
    public void testFindVariableWithReference() {
        Variable.variableList = new ArrayList<>();
        Variable var1 = new Variable("&var1");
        Variable var2 = new Variable("var2");
        Variable var3 = new Variable("var3");
        Variable.variableList.add(var1);
        Variable.variableList.add(var2);
        Variable.variableList.add(var3);
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.findVariableWithReference("var1"));
        assertNull(Variable.findVariableWithReference("var2"));
        assertNull(Variable.findVariableWithReference("var3"));
    }

    @Test
    public void testFindVariableWithReferenceNullName() {
        Variable.variableList = new ArrayList<>();
        Variable.addVariable("&var1");
        Variable var2 = new Variable("var2");
        Variable.variableList.add(var2);
        assertNull(Variable.findVariableWithReference(null));
    }

    private static int findVariableWithReference(String varName) {
        if (varName.startsWith("&")) {
            for (Variable variable : Variable.variableList) {
                if (variable.getValue() == Integer.parseInt(varName.substring(1))) {
                    return Variable.variableList.indexOf(variable);
                }
            }
        } else {
            final ListIterator<Variable> i = Variable.variableList.listIterator();
            while (i.hasNext()) {
                final Variable curVariable = i.next();
                if (curVariable.getName().equals(varName)) {
                    return i.previousIndex();
                }
            }
        }
        return -1;
    }
}