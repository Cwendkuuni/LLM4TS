package jipa.test;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class VariableTest {

 private static Variable variable1;
 private static Variable variable2;
 private static ArrayList<Variable> variableList;

 @Before
 public void setUp() {
 Variable.variableList = new ArrayList<>();
 variable1 = new Variable("var1");
 variable2 = new Variable("var2");
 Variable.addVariable("var1");
 Variable.addVariable("var2");
 }

 @Test
 public void testAddVariable() {
 assertTrue(Variable.addVariable("var3"));
 assertEquals(2, Variable.variableList.size());
 assertFalse(Variable.addVariable("var1"));
}

 @Test
 public void testRemoveVariable() {
 Variable.removeVariable("var1");
 assertEquals(1, Variable.variableList.size());
 Variable.removeVariable("var2");
 assertEquals(0, Variable.variableList.size());
}

 @Test
 public void testGetVariableValue() {
 Variable.setVariableValue("var1", 10);
 assertEquals(10, Variable.getVariableValue("var1"));
 assertEquals(0, Variable.getVariableValue("var2"));
}

 @Test
 public void testSetVariableValue() {
 Variable.setVariableValue("var1", 15);
 assertEquals(15, Variable.getVariableValue("var1"));
}

 @Test(expected = IllegalArgumentException.class)
 public void testFindVariableWithInvalidName() {
 Variable.findVariable("&invalidName");
}

 @Test
 public void testFindVariable() {
 assertEquals(variable1, Variable.findVariable("var1"));
 assertEquals(variable2, Variable.findVariable("var2"));
 assertNull(Variable.findVariable("invalidName"));
}

 @Test
 public void testFindVariableWithReference() {
 Variable.addVariable("&var1");
 Variable refVariable = Variable.findVariable("var1");
 assertEquals(refVariable.getValue(), variable1.getValue());
}
}