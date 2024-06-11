package jipa.test;

import static jipa.Variable.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class VariableTest {

 private static final String VALID_VAR_NAME = "validVarName";
 private static final String ANOTHER_VALID_VAR_NAME = "anotherValidVarName";
 private static final String EXISTING_VAR_NAME = "existingVarName";
 private static final String NON_EXISTING_VAR_NAME = "nonExistingVarName";
 private static final String START_WITH_ASTERISK_VAR_NAME = "*varName";
 private static final int VALID_VAR_VALUE = 42;
 private static final int ANOTHER_VALID_VAR_VALUE = 13;
 private static final int NEGATIVE_VALUE = -42;
 private static final List<Variable> expectedVariableList = new ArrayList<>();

 static {
  expectedVariableList.add(new Variable(VALID_VAR_NAME));
  expectedVariableList.add(new Variable(ANOTHER_VALID_VAR_NAME));
 }

 @Test
 public void testAddVariable() {
  Variable.variableList = new ArrayList<>();
  assertTrue(addVariable(VALID_VAR_NAME));
  assertEquals(1, Variable.variableList.size());
  assertEquals(Variable.variableList.get(0).getName(), VALID_VAR_NAME);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddVariableInvalidName() {
  Variable.variableList = new ArrayList<>();
  assertFalse(addVariable(null));
  assertFalse(addVariable(""));
  assertFalse(addVariable(" "));
  assertFalse(addVariable("\t"));
 }

 @Test
 public void testAddVariableDuplicateName() {
  Variable.variableList = new ArrayList<>();
  addVariable(VALID_VAR_NAME);
  assertFalse(addVariable(VALID_VAR_NAME));
  assertEquals(1, Variable.variableList.size());
 }

 @Test
 public void testRemoveVariable() {
  Variable.variableList = expectedVariableList;
  removeVariable(VALID_VAR_NAME);
  assertEquals(1, Variable.variableList.size());
  assertFalse(Variable.variableList.contains(new Variable(VALID_VAR_NAME)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveVariableNullName() {
  Variable.variableList = new ArrayList<>();
  addVariable(VALID_VAR_NAME);
  assertThrows(IllegalArgumentException.class, () -> removeVariable(null));
 }

 @Test
 public void testRemoveVariableNonExistentName() {
  Variable.variableList = expectedVariableList;
  removeVariable(NON_EXISTING_VAR_NAME);
  assertEquals(2, Variable.variableList.size());
 }

 @Test
 public void testGetVariableValueValidName() {
  Variable.variableList = expectedVariableList;
  Variable varWithValidName = Variable.variableList.get(0);
  assertEquals(0, getVariableValue(VALID_VAR_NAME));
  setVariableValue(VALID_VAR_NAME, VALID_VAR_VALUE);
  assertEquals(VALID_VAR_VALUE, getVariableValue(VALID_VAR_NAME));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetVariableValueNullName() {
  Variable.variableList = new ArrayList<>();
  assertNull(getVariableValue(null));
  addVariable(VALID_VAR_NAME);
  assertThrows(IllegalArgumentException.class, () -> getVariableValue(null));
 }

 @Test
 public void testGetVariableValueNonExistentName() {
  Variable.variableList = expectedVariableList;
  assertEquals(0, getVariableValue(NON_EXISTING_VAR_NAME));
 }

 @Test
 public void testSetVariableValueValidName() {
  Variable.variableList = expectedVariableList;
  Variable varWithValidName = Variable.variableList.get(0);
  setVariableValue(VALID_VAR_NAME, VALID_VAR_VALUE);
  assertEquals(VALID_VAR_VALUE, getVariableValue(VALID_VAR_NAME));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueNullName() {
  Variable.variableList = new ArrayList<>();
  addVariable(VALID_VAR_NAME);
  assertThrows(IllegalArgumentException.class, () -> setVariableValue(null, VALID_VAR_VALUE));
 }

 @Test
 public void testSetVariableValueNonExistentName() {
  Variable.variableList = expectedVariableList;
  setVariableValue(NON_EXISTING_VAR_NAME, VALID_VAR_VALUE);
  assertEquals(2, Variable.variableList.size());
  assertFalse(Variable.variableList.get(1).getName().equals(NON_EXISTING_VAR_NAME));
 }

 @Test
 public void testFindVariableValidName() {
  Variable.variableList = expectedVariableList;
  assertEquals(0, findVariable(VALID_VAR_NAME));
  assertEquals(expectedVariableList.get(0), findVariable(VALID_VAR_NAME));
 }

 @Test
 public void testFindVariableAnotherValidName() {
  Variable.variableList = expectedVariableList;
  assertEquals(1, findVariable(ANOTHER_VALID_VAR_NAME));
  assertEquals(expectedVariableList.get(1), findVariable(ANOTHER_VALID_VAR_NAME));
 }

 @Test
 public void testFindVariableNullName() {
  Variable.variableList = new ArrayList<>();
  addVariable(VALID_VAR_NAME);
  assertNull(findVariable(null));
 }

 @Test
 public void testFindVariableNonExistentName() {
  Variable.variableList = expectedVariableList;
  assertFalse(findVariable(NON_EXISTING_VAR_NAME) instanceof Variable);
  assertFalse(findVariable(NON_EXISTING_VAR_NAME) instanceof Integer);
 }

 @Test
 public void testFindVariableStartsWithAsterisk() {
  Variable.variableList = expectedVariableList;
  assertEquals(0, findVariable(START_WITH_ASTERISK_VAR_NAME));
  assertEquals(expectedVariableList.get(0).getValue(), findVariable(START_WITH_ASTERISK_VAR_NAME));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindVariableNullList() {
  assertNull(findVariable(null));
 }

 @Test
 public void testSetStaticVariableListNull() {
  Variable.variableList = null;
  addVariable(VALID_VAR_NAME);
  assertEquals(1, Variable.variableList.size());
 }

 @Test
 public void testSetStaticVariableListEmpty() {
  Variable.variableList = new ArrayList<>();
  addVariable(VALID_VAR_NAME);
  assertEquals(1, Variable.variableList.size());
 }

 @Test
 public void testStaticInitialization() {
  assertEquals(0, Variable.variableList.size());
 }
}