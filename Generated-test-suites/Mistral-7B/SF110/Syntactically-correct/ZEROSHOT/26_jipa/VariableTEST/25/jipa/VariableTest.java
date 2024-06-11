package jipa;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class VariableTest {

 private static final String VAR_NAME_1 = "var1";
 private static final String VAR_NAME_2 = "var2";
 private static final String VAR_NAME_3 = "*var3";
 private static final int VAR_VALUE_1 = 10;
 private static final int VAR_VALUE_2 = 20;

 @Test
 public void testAddVariable() {
  Variable.addVariable(VAR_NAME_1);
  Variable.addVariable(VAR_NAME_2);
  List<Variable> variableList = Variable.variableList;
  assertEquals(2, variableList.size());
  assertTrue(variableList.get(0).getName().equals(VAR_NAME_1));
  assertTrue(variableList.get(1).getName().equals(VAR_NAME_2));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddVariableDuplicate() {
  Variable.addVariable(VAR_NAME_1);
  Variable.addVariable(VAR_NAME_1);
 }

 @Test
 public void testRemoveVariable() {
  Variable.addVariable(VAR_NAME_1);
  Variable.addVariable(VAR_NAME_2);
  Variable.removeVariable(VAR_NAME_1);
  List<Variable> variableList = Variable.variableList;
  assertEquals(1, variableList.size());
  assertTrue(variableList.get(0).getName().equals(VAR_NAME_2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveNonExistentVariable() {
  Variable.removeVariable("nonExistentVar");
 }

 @Test
 public void testGetVariableValue() {
  Variable.addVariable(VAR_NAME_1);
  Variable.addVariable(VAR_NAME_2);
  Variable.setVariableValue(VAR_NAME_1, VAR_VALUE_1);
  Variable.setVariableValue(VAR_NAME_2, VAR_VALUE_2);
  List<Variable> variableList = Variable.variableList;
  assertEquals(VAR_VALUE_1, Variable.getVariableValue(VAR_NAME_1));
  assertEquals(VAR_VALUE_2, Variable.getVariableValue(VAR_NAME_2));
  assertEquals(0, Variable.getVariableValue("nonExistentVar"));
 }

 @Test
 public void testSetVariableValue() {
  Variable.addVariable(VAR_NAME_1);
  Variable.addVariable(VAR_NAME_2);
  List<Variable> variableList = Variable.variableList;
  Variable var1 = variableList.get(0);
  Variable var2 = variableList.get(1);
  Variable.setVariableValue(VAR_NAME_1, VAR_VALUE_1);
  Variable.setVariableValue(VAR_NAME_2, VAR_VALUE_2);
  assertEquals(VAR_VALUE_1, var1.getValue());
  assertEquals(VAR_VALUE_2, var2.getValue());
 }

 @Test
 public void testFindVariable() {
  Variable.addVariable(VAR_NAME_1);
  Variable.addVariable(VAR_NAME_2);
  List<Variable> variableList = Variable.variableList;
  int index = Variable.findVariable(VAR_NAME_1);
  assertEquals(0, index);
  index = Variable.findVariable(VAR_NAME_2);
  assertEquals(1, index);
  index = Variable.findVariable("nonExistentVar");
  assertEquals(-1, index);
 }

 @Test
 public void testFindVariableByReference() {
  Variable var1 = new Variable(VAR_NAME_1);
  Variable var2 = new Variable(VAR_NAME_2);
  Variable.variableList.add(var1);
  Variable.variableList.add(var2);
  List<Variable> variableList = Variable.variableList;
  int index = Variable.findVariable("&" + VAR_NAME_1);
  assertEquals(0, index);
  int value = (int) Variable.getVariableValue("&" + VAR_NAME_1);
  assertEquals(VAR_VALUE_1, value);
  index = Variable.findVariable("&" + VAR_NAME_2);
  assertEquals(-1, index);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindVariableInvalidReference() {
  Variable.addVariable(VAR_NAME_1);
  Variable.addVariable(VAR_NAME_2);
  List<Variable> variableList = Variable.variableList;
  int index = Variable.findVariable("&nonExistentVar");
  assertEquals(-1, index);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueInvalidReference() {
  Variable.addVariable(VAR_NAME_1);
  Variable.addVariable(VAR_NAME_2);
  Variable.setVariableValue("&nonExistentVar", 1);
 }

 @Test
 public void testStaticInitialization() {
  List<Variable> variableList = Variable.variableList;
  assertEquals(0, variableList.size());
 }
}