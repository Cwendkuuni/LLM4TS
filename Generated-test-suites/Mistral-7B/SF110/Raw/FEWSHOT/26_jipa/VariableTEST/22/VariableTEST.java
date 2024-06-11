package jipa;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class VariableTest {

 @Test
 public void testAddVariableAndGetValue() {
 Variable.variableList = new ArrayList<Variable>();
 Variable.addVariable("var1");
 Variable var1 = Variable.variableList.get(0);
 assertEquals("var1", var1.getName());
 assertEquals(0, var1.getValue());
 }

 @Test
 public void testAddVariableWithExistingNameFails() {
 Variable.variableList = new ArrayList<Variable>();
 Variable.addVariable("var1");
 assertFalse(Variable.addVariable("var1"));
 }

 @Test
 public void testRemoveVariable() {
 Variable.variableList = new ArrayList<Variable>();
 Variable.addVariable("var1");
 Variable.addVariable("var2");
 Variable.removeVariable("var1");
 assertEquals(1, Variable.variableList.size());
 assertNull(Variable.findVariable("var1"));
 }

 @Test
 public void testGetVariableValueWithExistingName() {
 Variable.variableList = new ArrayList<Variable>();
 Variable.addVariable("var1");
 Variable.setVariableValue("var1", 10);
 assertEquals(10, Variable.getVariableValue("var1"));
 }

 @Test
 public void testGetVariableValueWithNonExistingName() {
 Variable.variableList = new ArrayList<Variable>();
 assertEqual(0, Variable.getVariableValue("nonExistingVar"));
 }

 @Test
 public void testGetVariableValueWithReference() {
 Variable.variableList = new ArrayList<Variable>();
 Variable.addVariable("var1");
 Variable.addVariable("var2");
 Variable.setVariableValue("var1", 10);
 assertEquals(10, Variable.getVariableValue("&var1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindVariableWithNull() {
 Variable.findVariable(null);
 }

 @Test
 public void testFindVariableWithExistingName() {
 Variable.variableList = new ArrayList<Variable>();
 Variable.addVariable("var1");
 Variable.addVariable("var2");
 final int index = Variable.variableList.indexOf(Variable.findVariable("var1"));
 assertEquals(0, index);
 }

 @Test
 public void testFindVariableWithNonExistingName() {
 Variable.variableList = new ArrayList<Variable>();
 Variable.addVariable("var1");
 Variable.addVariable("var2");
 assertNull(Variable.findVariable("nonExistingVar"));
 }
}