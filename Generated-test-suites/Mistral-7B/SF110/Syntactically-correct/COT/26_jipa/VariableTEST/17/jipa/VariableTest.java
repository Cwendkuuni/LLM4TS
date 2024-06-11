package jipa.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jipa.Variable;

public class VariableTest {

 private static final String VAR_NAME_1 = "var1";
 private static final String VAR_NAME_2 = "var2";
 private static final String VAR_NAME_3 = "*var3";
 private static final int VAR_VALUE_1 = 10;
 private static final int VAR_VALUE_2 = 20;

 @Test
 public void testAddVariable() {
  assertTrue(Variable.addVariable(VAR_NAME_1));
  assertFalse(Variable.addVariable(VAR_NAME_1));
  List<Variable> initialVariableList = Variable.variableList;
  assertTrue(Variable.addVariable(VAR_NAME_2));
  assertEquals(initialVariableList.size() + 1, Variable.variableList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddVariableNullName() {
  assertFalse(Variable.addVariable(null));
 }

 @Test
 public void testRemoveVariable() {
  Variable.addVariable(VAR_NAME_1);
  Variable.addVariable(VAR_NAME_2);
  Variable.removeVariable(VAR_NAME_1);
  List<Variable> initialVariableList = Variable.variableList;
  Variable.removeVariable(VAR_NAME_2);
  assertEquals(initialVariableList.size() - 2, Variable.variableList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveVariableNullName() {
  Variable.removeVariable(null);
 }

 @Test
 public void testGetVariableValue() {
  Variable.addVariable(VAR_NAME_1);
  Variable.addVariable(VAR_NAME_2);
  Variable.setVariableValue(VAR_NAME_1, VAR_VALUE_1);
  Variable.setVariableValue(VAR_NAME_2, VAR_VALUE_2);
  assertEquals(VAR_VALUE_1, Variable.getVariableValue(VAR_NAME_1));
  assertEquals(VAR_VALUE_2, Variable.getVariableValue(VAR_NAME_2));
  assertEquals(0, Variable.getVariableValue("nonExistentVar"));
 }

 @Test
 public void testSetVariableValue() {
  Variable.addVariable(VAR_NAME_1);
  Variable.setVariableValue(VAR_NAME_1, VAR_VALUE_1);
  assertEquals(VAR_VALUE_1, Variable.getVariableValue(VAR_NAME_1));
  Variable.setVariableValue(VAR_NAME_1, VAR_VALUE_2);
  assertEquals(VAR_VALUE_2, Variable.getVariableValue(VAR_NAME_1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueNullName() {
  Variable.setVariableValue(null, VAR_VALUE_1);
 }

 @Test
 public void testFindVariable() {
  Variable.addVariable(VAR_NAME_1);
  Variable.addVariable(VAR_NAME_2);
  Variable.addVariable(VAR_NAME_3);
  assertEquals(1, Variable.findVariable(VAR_NAME_1));
  assertEquals(Variable.variableList.get(1).getValue(), Variable.findVariable("&" + VAR_NAME_2));
  assertEquals(-1, Variable.findVariable("nonExistentVar"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindVariableNullName() {
  Variable.findVariable(null);
 }

 @Test
 public void testInitialization() {
  assertEquals(0, Variable.variableList.size());
  Variable.addVariable(VAR_NAME_1);
  assertEquals(1, Variable.variableList.size());
 }

}