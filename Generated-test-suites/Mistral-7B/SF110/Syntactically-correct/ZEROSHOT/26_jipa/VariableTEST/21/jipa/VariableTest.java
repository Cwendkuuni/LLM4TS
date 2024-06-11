package jipa;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class VariableTest {

 private static List<Variable> variableList;

 @BeforeClass
 static public void setUpClass() {
 Variable.variableList = new ArrayList<Variable>();
 VariableTest.addVariable("testVar1");
 VariableTest.addVariable("testVar2");
 }

 @AfterClass
 static public void tearDownClass() {
 Variable.variableList.clear();
 }

 @Test
 public void testAddVariable() {
 String varName = "testVar3";
 boolean result = Variable.addVariable(varName);
 assertTrue(result);
 VariableTest.addVariable("testVar4");
 List<Variable> expectedVariables = new ArrayList<>();
 expectedVariables.add(new Variable("testVar1"));
 expectedVariables.add(new Variable("testVar2"));
 expectedVariables.add(new Variable("testVar3"));
 expectedVariables.add(new Variable("testVar4"));
 assertEquals(expectedVariables, Variable.variableList);
 }

 @Test
 public void testAddVariableWithExistingName() {
 String varName = "testVar1";
 boolean result = Variable.addVariable(varName);
 assertFalse(result);
 }

 @Test
 public void testRemoveVariable() {
 String varName = "testVar2";
 Variable.removeVariable(varName);
 List<Variable> expectedVariables = new ArrayList<>();
 expectedVariables.add(new Variable("testVar1"));
 expectedVariables.add(new Variable("testVar3"));
 assertEquals(expectedVariables, Variable.variableList);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveNonExistingVariable() {
 String varName = "nonExistingVar";
 Variable.removeVariable(varName);
 }

 @Test
 public void testGetVariableValueWithExistingName() {
 String varName = "testVar1";
 int expectedValue = 0;
 int result = Variable.getVariableValue(varName);
 assertEquals(expectedValue, result);
 VariableTest.setVariableValue(varName, 42);
 expectedValue = 42;
 result = Variable.getVariableValue(varName);
 assertEquals(expectedValue, result);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetVariableValueWithNonExistingName() {
 String varName = "nonExistingVar";
 int result = Variable.getVariableValue(varName);
 fail("Expected IndexOutOfBoundsException but got: " + result);
 }

 @Test
 public void testGetVariableValueWithReference() {
 String varName = "*testVar1";
 int expectedValue = 0;
 int result = Variable.getVariableValue(varName);
 assertEquals(expectedValue, result);
 VariableTest.setVariableValue("testVar1", 42);
 expectedValue = 42;
 result = Variable.getVariableValue(varName);
 assertEquals(expectedValue, result);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetVariableValueWithNonExistingReference() {
 String varName = "*nonExistingVar";
 int result = Variable.getVariableValue(varName);
 fail("Expected IndexOutOfBoundsException but got: " + result);
 }

 @Test
 public void testSetVariableValue() {
 String varName = "testVar1";
 int newValue = 42;
 Variable.setVariableValue(varName, newValue);
 int expectedValue = newValue;
 int result = Variable.getVariableValue(varName);
 assertEquals(expectedValue, result);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetVariableValueWithNonExistingName() {
 String varName = "nonExistingVar";
 int newValue = 42;
 Variable.setVariableValue(varName, newValue);
 fail("Expected IndexOutOfBoundsException but got: " + newValue);
 }

 @Test
 public void testFindVariable() {
 String varName = "testVar1";
 int expectedIndex = 0;
 int result = Variable.findVariable(varName);
 assertEquals(expectedIndex, result);
 }

 @Test
 public void testFindVariableWithNonExistingName() {
 String varName = "nonExistingVar";
 int result = Variable.findVariable(varName);
 assertEquals(-1, result);
 }

 @Test
 public void testFindVariableWithReference() {
 String varName = "*testVar1";
 int expectedIndex = 0;
 int result = Variable.findVariable(varName);
 if (result != -1) {
 expectedIndex = Variable.variableList.get(result).getValue();
 }
 assertEquals(expectedIndex, result);
 }

 @Test
 public void testFindVariableWithNonExistingReference() {
 String varName = "*nonExistingVar";
 int result = Variable.findVariable(varName);
 assertEquals(-1, result);
 }
}