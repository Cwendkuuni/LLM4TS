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
 public void testVariableConstructor() {
 final String varName = "testVar";
 Variable variable = new Variable(varName);
 Assert.assertEquals(varName, variable.getName());
 Assert.assertEquals(0, variable.getValue());
 }

 @Test
 public void testAddVariable() {
 final String varName = "testVar";
 Variable.addVariable(varName);
 Assert.assertEquals(1, Variable.variableList.size());
 Variable addedVariable = Variable.variableList.get(0);
 Assert.assertEquals(varName, addedVariable.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateVariable() {
 final String varName = "testVar";
 Variable.addVariable(varName);
 Assert.assertEquals(1, Variable.variableList.size());
 Assert.assertThrows(IllegalArgumentException.class, () -> Variable.addVariable(varName));
 }

 @Test
 public void testRemoveVariable() {
 final String varName = "testVar";
 Variable.addVariable(varName);
 Variable.removeVariable(varName);
 Assert.assertEquals(0, Variable.variableList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveNonExistentVariable() {
 Variable.removeVariable("nonExistentVar");
 }

 @Test
 public void testGetVariableValue() {
 final String varName = "testVar";
 Variable.addVariable(varName);
 Variable.setVariableValue(varName, 42);
 Assert.assertEquals(42, Variable.getVariableValue(varName));
 }

 @Test
 public void testGetVariableValueWithReference() {
 final String varName = "*testVar";
 Variable.addVariable("testVar");
 Variable.setVariableValue("testVar", 42);
 Assert.assertEquals(42, Variable.getVariableValue(varName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetNonExistentVariableValue() {
 Assert.assertEquals(0, Variable.getVariableValue("nonExistentVar"));
 }

 @Test
 public void testSetVariableValue() {
 final String varName = "testVar";
 Variable.addVariable(varName);
 Variable.setVariableValue(varName, 42);
 Assert.assertEquals(42, Variable.getVariableValue(varName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNonExistentVariableValue() {
 Assert.assertEquals(0, Variable.getVariableValue("nonExistentVar"));
 }

 @Test
 public void testFindVariable() {
 final String varName = "testVar";
 Variable.addVariable(varName);
 int index = Variable.findVariable(varName);
 Variable foundVariable = Variable.variableList.get(index);
 Assert.assertEquals(varName, foundVariable.getName());
 }

 @Test
 public void testFindNonExistentVariable() {
 Assert.assertEquals(-1, Variable.findVariable("nonExistentVar"));
 }

 @Test
 public void testFindVariableByValue() {
 final String varName = "testVar";
 Variable.addVariable(varName);
 Variable.setVariableValue(varName, 42);
 int index = Variable.findVariable("*42");
 Variable foundVariable = Variable.variableList.get(index);
 Assert.assertEquals(varName, foundVariable.getName());
 }

 @Test
 public void testFindVariableByValueWithMultipleVariables() {
 Variable variable1 = new Variable("var1");
 Variable variable2 = new Variable("var2");
 Variable variable3 = new Variable("var3");
 Variable.variableList.add(variable1);
 Variable.variableList.add(variable2);
 Variable.variableList.add(variable3);
 Variable.setVariableValue("var2", 42);
 int index = Variable.findVariable("*42");
 Variable foundVariable = Variable.variableList.get(index);
 Assert.assertEquals(variable2.getName(), foundVariable.getName());
 }

 @AfterClass
 public static void teardownClass() {
 Variable.variableList.clear();
 }

}