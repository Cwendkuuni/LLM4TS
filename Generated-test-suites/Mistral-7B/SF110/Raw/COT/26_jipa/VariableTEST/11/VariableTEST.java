package jipa.test;

import org.junit.Assert;
import org.junit.Test;
import jipa.Variable;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class VariableTest {

 private List<Variable> variableList;

 @BeforeClass
 public static void setupClass() {
 Variable.variableList = new ArrayList<>();
 }

 @Test
 public void testVariableConstructor() {
 String varName = "testVar";
 Variable variable = new Variable(varName);
 assertEquals(varName, variable.getName());
 assertEquals(0, variable.getValue());
 }

 @Test
 public void testAddVariable() {
 String varName1 = "var1";
 String varName2 = "var2";
 Variable.addVariable(varName1);
 Variable.addVariable(varName2);
 assertEquals(2, Variable.variableList.size());
 assertFalse(Variable.addVariable(varName1));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddVariableWithExistingName() {
 String varName = "testVar";
 Variable.addVariable(varName);
 Variable.addVariable(varName);
 }

 @Test
 public void testRemoveVariable() {
 String varName = "testVar";
 Variable.addVariable(varName);
 Variable.removeVariable(varName);
 assertEquals(0, Variable.variableList.size());
 }

 @Test
 public void testGetVariableValue() {
 String varName = "testVar";
 Variable.addVariable(varName);
 Variable.setVariableValue(varName, 10);
 assertEquals(10, Variable.getVariableValue(varName));
 }

 @Test
 public void testGetVariableValueWithReferenceAndExistingName() {
 String varName = "*testVar";
 Variable.addVariable("testVar");
 Variable.setVariableValue("testVar", 10);
 assertEquals(10, Variable.getVariableValue(varName));
 }

 @Test
 public void testGetVariableValueWithNonExistingName() {
 assertEquals(0, Variable.getVariableValue("nonExistingVar"));
 }

 @Test
 public void testSetVariableValue() {
 String varName = "testVar";
 Variable.addVariable(varName);
 Variable.setVariableValue(varName, 10);
 assertEquals(10, Variable.getVariableValue(varName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueWithNonExistingName() {
 String varName = "nonExistingVar";
 Variable.setVariableValue(varName, 10);
 }

 @Test(expected = NullPointerException.class)
 public void testFindVariableWithNullVariableList() {
 Variable.variableList = null;
 String varName = "testVar";
 Variable.findVariable(varName);
 }

 @Test
 public void testFindVariableWithExistingName() {
 String varName = "testVar";
 Variable.addVariable(varName);
 int index = Variable.findVariable(varName);
 assertSame(Variable.variableList.get(index), Variable.variableList.get(0));
 }

 @Test
 public void testFindVariableWithNonExistingName() {
 String varName = "nonExistingVar";
 assertNull(Variable.findVariable(varName));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testFindVariableWithInvalidIndex() {
 Variable.addVariable("testVar");
 Variable.removeVariable("testVar");
 int index = Variable.findVariable("testVar");
 Variable.variableList.remove(index);
 Variable.findVariable(index);
 }
}