package jipa.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jipa.Variable;

public class VariableTest {

 private List<Variable> variableList;

 @BeforeClass
 public static void setup() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("var1");
 Variable.addVariable("var2");
 }

 @Test
 public void testAddVariable() {
 String varName = "var3";
 assertTrue(Variable.addVariable(varName));
 assertEquals(3, Variable.variableList.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddVariableWithExistingName() {
 String varName = "var1";
 assertFalse(Variable.addVariable(varName));
 assertEquals(3, Variable.variableList.size());
 }

 @Test
 public void testRemoveVariable() {
 String varName = "var2";
 Variable.removeVariable(varName);
 assertEquals(2, Variable.variableList.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveNonExistingVariable() {
 String varName = "nonExistingVar";
 Variable.removeVariable(varName);
 assertEquals(2, Variable.variableList.size());
 }

 @Test
 public void testGetVariableValue() {
 String varName = "var1";
 int expectedValue = 0;
 assertEquals(expectedValue, Variable.getVariableValue(varName));
 }

 @Test
 public void testGetVariableValueWithReference() {
 String varName = "*var1";
 int expectedValue = 0;
 assertEquals(expectedValue, Variable.getVariableValue(varName));
 }

 @Test
 public void testGetVariableValueWithNonExistingName() {
 String varName = "nonExistingVar";
 int expectedValue = 0;
 assertEquals(expectedValue, Variable.getVariableValue(varName));
 }

 @Test
 public void testSetVariableValue() {
 String varName = "var1";
 int varValue = 10;
 Variable.setVariableValue(varName, varValue);
 int expectedValue = 10;
 assertEquals(expectedValue, Variable.getVariableValue(varName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueWithNonExistingName() {
 String varName = "nonExistingVar";
 int varValue = 10;
 Variable.setVariableValue(varName, varValue);
 int expectedValue = 0;
 assertEquals(expectedValue, Variable.getVariableValue(varName));
 }

 @Test
 public void testFindVariable() {
 String varName = "var1";
 int expectedIndex = 0;
 int index = Variable.findVariable(varName);
 assertEquals(expectedIndex, index);
 }

 @Test
 public void testFindVariableWithReference() {
 String varName = "*var1";
 int expectedValue = 0;
 int index = Variable.findVariable(varName);
 assertEquals(-1, index);
 }

 @Test
 public void testFindVariableWithNonExistingName() {
 String varName = "nonExistingVar";
 int expectedIndex = -1;
 int index = Variable.findVariable(varName);
 assertEquals(expectedIndex, index);
 }

 @Test
 public void testFindVariableWithReferenceAndValue() {
 Variable var1 = Variable.variableList.get(0);
 int expectedValue = var1.getValue();
 String varName = "*" + expectedValue;
 int index = Variable.findVariable(varName);
 assertEquals(0, index);
 }
}