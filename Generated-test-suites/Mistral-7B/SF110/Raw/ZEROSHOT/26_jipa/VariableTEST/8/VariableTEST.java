package jipa.test;

import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class VariableTest {

 private List<Variable> variableList;

 @Test
 public void testAddVariable() {
 Variable.variableList = new ArrayList<>();
 String varName = "var1";
 boolean added = Variable.addVariable(varName);
 Variable var1 = new Variable(varName);
 Assert.assertTrue(added);
 Assert.assertEquals(1, Variable.variableList.size());
 Assert.assertEquals(varName, Variable.variableList.get(0).getName());
 }

 @Test
 public void testAddDuplicateVariable() {
 Variable.variableList = new ArrayList<>();
 String varName = "var1";
 boolean added = Variable.addVariable(varName);
 Assert.assertTrue(added);
 added = Variable.addVariable(varName);
 Assert.assertFalse(added);
 Assert.assertEquals(1, Variable.variableList.size());
 }

 @Test
 public void testRemoveVariable() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("var1");
 Variable.addVariable("var2");
 String varName = "var1";
 Variable.removeVariable(varName);
 Assert.assertEquals(1, Variable.variableList.size());
 Assert.assertNotEquals(varName, Variable.variableList.get(0).getName());
 }

 @Test
 public void testInvalidRemoveVariable() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("var1");
 String varName = "var2";
 Variable.removeVariable(varName);
 Assert.assertEquals(1, Variable.variableList.size());
 }

 @Test
 public void testGetVariableValue() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("var1");
 Variable.addVariable("var2");
 Variable.setVariableValue("var1", 5);
 int expectedValue1 = 5;
 int expectedValue2 = 0;
 Assert.assertEquals(expectedValue1, Variable.getVariableValue("var1"));
 Assert.assertEquals(expectedValue2, Variable.getVariableValue("var2"));
 }

 @Test
 public void testGetValueOfReferenceVariable() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("&refVar");
 Variable refVar = new Variable("refVar");
 Variable.variableList.add(refVar);
 Variable.setVariableValue("refVar", 5);
 int expectedValue = 5;
 Assert.assertEquals(expectedValue, Variable.getVariableValue("&refVar"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeFindVariable() {
 String varName = "-var";
 Variable.findVariable(varName);
 }

 @Test
 public void testFindVariableWithEmptyList() {
 Variable.variableList = new ArrayList<>();
 String varName = "var1";
 int expectedIndex = -1;
 Assert.assertEquals(expectedIndex, Variable.findVariable(varName));
 }

 @Test
 public void testFindVariable() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("var1");
 Variable.addVariable("var2");
 String varName = "var1";
 int expectedIndex = 0;
 Assert.assertEquals(expectedIndex, Variable.findVariable(varName));
 }

 @Test
 public void testFindVariableWithReference() {
 Variable.variableList = new ArrayList<>();
 Variable refVar = new Variable("&refVar");
 Variable.variableList.add(refVar);
 String varName = "refVar";
 int expectedValue = 0; // assuming refVar has not been initialized
 Assert.assertEquals(expectedValue, Variable.getVariableValue(varName));
 int expectedIndex = Variable.variableList.indexOf(refVar);
 Assert.assertEquals(expectedIndex, Variable.findVariable(varName));
 }

 @Test
 public void testStaticInitialization() {
 Variable.variableList = new ArrayList<>();
 Variable var1 = new Variable("var1");
 Variable var2 = new Variable("var2");
 Variable.variableList.add(var1);
 Variable.variableList.add(var2);
 List<Variable> expectedVariableList = new ArrayList<>(List.of(var1, var2));
 Assert.assertEquals(expectedVariableList, Variable.variableList);
 }

}