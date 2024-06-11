package jipa.test;

import jipa.Variable;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class VariableTest {

 private static List<Variable> variableList;

 @BeforeClass
 public static void setUp() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("var1");
 Variable.addVariable("var2");
 Variable.addVariable("var3");
 }

 @Test
 public void testAddVariable() {
 String varName = "var4";
 Assert.assertTrue(Variable.addVariable(varName));
 Assert.assertEquals(4, Variable.variableList.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddDuplicateVariable() {
 String varName = "var1";
 Assert.assertFalse(Variable.addVariable(varName));
 }

 @Test
 public void testFindVariable() {
 String varName = "var2";
 int expectedIndex = 1;
 int actualIndex = Variable.findVariable(varName);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testFindNonExistentVariable() {
 String varName = "nonExistentVar";
 int expectedIndex = -1;
 int actualIndex = Variable.findVariable(varName);
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testGetVariableValue() {
 String varName = "var1";
 int expectedValue = 0;
 int actualValue = Variable.getVariableValue(varName);
 Assert.assertEquals(expectedValue, actualValue);

 Variable.setVariableValue(varName, 10);
 expectedValue = 10;
 actualValue = Variable.getVariableValue(varName);
 Assert.assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testGetVariableValueWithReference() {
 String varName = "*var1";
 int expectedValue = 0;
 int actualValue = Variable.getVariableValue(varName);
 Assert.assertEquals(expectedValue, actualValue);

 Variable.setVariableValue(varName, 20);
 expectedValue = 20;
 actualValue = Variable.getVariableValue(varName);
 Assert.assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testSetVariableValue() {
 String varName = "var2";
 int newValue = 20;
 Variable.setVariableValue(varName, newValue);
 int expectedValue = newValue;
 int actualValue = Variable.getVariableValue(varName);
 Assert.assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testRemoveVariable() {
 String varName = "var3";
 Variable.removeVariable(varName);
 int expectedSize = 2;
 int actualSize = Variable.variableList.size();
 Assert.assertEquals(expectedSize, actualSize);
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveNonExistentVariable() {
 String varName = "nonExistentVar";
 Variable.removeVariable(varName);
 }
}