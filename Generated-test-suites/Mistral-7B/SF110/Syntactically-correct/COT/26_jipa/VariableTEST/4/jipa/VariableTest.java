package jipa.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jipa.Variable;

public class VariableTest {

 private static final String VALID_VAR_NAME = "testVar";
 private static final int VALID_VAR_VALUE = 10;
 private static final String INVALID_VAR_NAME = "*invalidVarName";
 private static final String EXISTING_VAR_NAME = "testVar";
 private static final String NON_EXISTING_VAR_NAME = "nonExistingVar";
 private static final String REFERENCED_VAR_NAME = "&referencedVar";
 private static final int REFERENCED_VAR_VALUE = 20;

 @Test
 public void testAddVariable() {
  assertTrue(Variable.addVariable(VALID_VAR_NAME));
  assertEquals(1, Variable.variableList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateVariable() {
  Variable.addVariable(VALID_VAR_NAME);
  assertFalse(Variable.addVariable(VALID_VAR_NAME));
 }

 @Test
 public void testFindVariable() {
  Variable.addVariable(VALID_VAR_NAME);
  assertEquals(0, Variable.findVariable(VALID_VAR_NAME));
  assertEquals(EXISTING_VAR_NAME, Variable.variableList.get(0).getName());
 }

 @Test
 public void testFindVariableWithReference() {
  Variable.addVariable(VALID_VAR_NAME);
  Variable refVar = new Variable(VALID_VAR_NAME + REFERENCE_MARKER);
  refVar.setValue(REFERENCED_VAR_VALUE);
  Variable.variableList.add(refVar);
  assertEquals(REFERENCED_VAR_VALUE, Variable.findVariable(VALID_VAR_NAME + REFERENCE_MARKER));
 }

 @Test
 public void testRemoveVariable() {
  Variable.addVariable(VALID_VAR_NAME);
  Variable.removeVariable(VALID_VAR_NAME);
  assertEquals(0, Variable.variableList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveNonExistentVariable() {
  Variable.removeVariable(NON_EXISTING_VAR_NAME);
 }

 @Test
 public void testGetVariableValue() {
  Variable.addVariable(VALID_VAR_NAME);
  Variable.setVariableValue(VALID_VAR_NAME, VALID_VAR_VALUE);
  assertEquals(VALID_VAR_VALUE, Variable.getVariableValue(VALID_VAR_NAME));
 }

 @Test
 public void testGetVariableValueWithReference() {
  Variable.addVariable(VALID_VAR_NAME);
  Variable refVar = new Variable(VALID_VAR_NAME + REFERENCE_MARKER);
  refVar.setValue(REFERENCED_VAR_VALUE);
  Variable.variableList.add(refVar);
  assertEquals(REFERENCED_VAR_VALUE, Variable.getVariableValue(VALID_VAR_NAME + REFERENCE_MARKER));
 }

 @Test
 public void testGetVariableValueWithStar() {
  Variable refVar1 = new Variable("refVar1");
  Variable refVar2 = new Variable("refVar2");
  Variable.addVariable("refVar1");
  Variable.addVariable("refVar2");
  Variable.setVariableValue("refVar1", 1);
  Variable.setVariableValue("refVar2", 2);
  assertEquals(1, Variable.getVariableValue("*refVar1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetVariableValueWithInvalidNameStartsWithStar() {
  Variable.addVariable(VALID_VAR_NAME);
  assertFalse(Variable.getVariableValue(INVALID_VAR_NAME) instanceof Integer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetVariableValueWithNonExistentVariableName() {
  assertFalse(Variable.getVariableValue(NON_EXISTING_VAR_NAME) instanceof Integer);
 }

 @Test
 public void testSetVariableValue() {
  Variable.addVariable(VALID_VAR_NAME);
  Variable.setVariableValue(VALID_VAR_NAME, VALID_VAR_VALUE);
  assertEquals(VALID_VAR_VALUE, Variable.getVariableValue(VALID_VAR_NAME));
 }

 @Test
 public void testSetVariableValueWithReference() {
  Variable.addVariable(VALID_VAR_NAME);
  Variable refVar = new Variable(VALID_VAR_NAME + REFERENCE_MARKER);
  refVar.setValue(REFERENCED_VAR_VALUE);
  Variable.variableList.add(refVar);
  Variable.setVariableValue(VALID_VAR_NAME + REFERENCE_MARKER, REFERENCED_VAR_VALUE + 1);
  assertEquals(REFERENCED_VAR_VALUE + 1, Variable.findVariable(VALID_VAR_NAME + REFERENCE_MARKER).getValue());
 }

 private static final String REFERENCE_MARKER = "_REF";

}