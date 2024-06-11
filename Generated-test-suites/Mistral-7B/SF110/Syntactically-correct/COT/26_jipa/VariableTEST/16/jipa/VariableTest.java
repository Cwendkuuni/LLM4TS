package jipa.test;

import org.junit.Assert;
import org.junit.Test;
import jipa.Variable;
import java.util.ArrayList;
import java.util.List;

public class VariableTest {

 private List<Variable> variableList;

 @BeforeClass
 public static void setupClass() {
 Variable.variableList = new ArrayList<>();
 }

 @Test
 public void testAddVariable() {
 String varName = "testVar";
 Variable.addVariable(varName);
 Assert.assertEquals(1, Variable.variableList.size());
 Variable testVariable = Variable.variableList.get(0);
 Assert.assertEquals(varName, testVariable.getName());
 Assert.assertEquals(0, testVariable.getValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddDuplicateVariable() {
 String varName = "testVar";
 Variable.addVariable(varName);
 Assert.assertThrows(IllegalStateException.class, () -> Variable.addVariable(varName));
 }

 @Test
 public void testRemoveVariable() {
 String varName = "testVar";
 Variable.addVariable(varName);
 Variable.removeVariable(varName);
 Assert.assertEquals(0, Variable.variableList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindVariableNullName() {
 String varName = null;
 int result = Variable.findVariable(varName);
 Assert.assertEquals(-1, result);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testFindVariableDoesNotExist() {
 String varName = "nonexistentVar";
 int result = Variable.findVariable(varName);
 Assert.assertEquals(-1, result);
 }

 @Test
 public void testFindVariableExist() {
 String varName = "testVar";
 Variable.addVariable(varName);
 int result = Variable.findVariable(varName);
 Assert.assertEquals(0, result);
 }

 @Test
 public void testGetVariableValue() {
 String varName = "testVar";
 Variable.addVariable(varName);
 Variable.setVariableValue(varName, 10);
 int result = Variable.getVariableValue(varName);
 Assert.assertEquals(10, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueNullName() {
 String varName = null;
 int value = 10;
 Variable.setVariableValue(varName, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueNegativeValue() {
 String varName = "testVar";
 int value = -10;
 Variable.setVariableValue(varName, value);
 }

 @Test
 public void testSetVariableValueExist() {
 String varName = "testVar";
 Variable.addVariable(varName);
 Variable.setVariableValue(varName, 10);
 int result = Variable.getVariableValue(varName);
 Assert.assertEquals(10, result);
 }

 @Test
 public void testGetVariableValueUnknownVariable() {
 String varName = "nonexistentVar";
 int result = Variable.getVariableValue(varName);
 Assert.assertEquals(0, result);
 }
}