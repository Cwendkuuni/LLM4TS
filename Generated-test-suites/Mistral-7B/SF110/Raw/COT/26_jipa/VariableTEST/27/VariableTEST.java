package jipa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class VariableTest {

 private Variable variable;
 private static final String VAR_NAME = "testVar";
 private static final int VALUE = 10;

 @Test
 public void testVariableConstructor() {
     variable = new Variable(VAR_NAME);
     assertEquals(VAR_NAME, variable.getName());
     assertEquals(0, variable.getValue());
 }

 @Test
 public void testAddVariable() {
     Variable.variableList = new ArrayList<>();
     assertTrue(Variable.addVariable(VAR_NAME));
     assertEquals(1, Variable.variableList.size());
     assertSame(Variable.variableList.get(0), variable);
 }

 @Test (expected = IllegalStateException.class)
 public void testAddDuplicateVariable() {
     Variable.variableList = new ArrayList<>();
     Variable.addVariable(VAR_NAME);
     assertFalse(Variable.addVariable(VAR_NAME));
 }

 @Test
 public void testFindVariableName() {
     Variable.variableList = new ArrayList<>();
     Variable.addVariable(VAR_NAME);
     int index = Variable.findVariable(VAR_NAME);
     assertNotNull(index);
     assertEquals(0, index);
 }

 @Test
 public void testFindVariableValue() {
     Variable.variableList = new ArrayList<>();
     Variable.addVariable(VAR_NAME);
     variable = new Variable(VAR_NAME);
     Variable.setVariableValue(VAR_NAME, VALUE);
     assertEquals(VALUE, Variable.getVariableValue(VAR_NAME));
 }

 @Test
 public void testFindNonExistentVariableName() {
     Variable.variableList = new ArrayList<>();
     assertNull(Variable.findVariable("nonExistentVarName"));
 }

 @Test
 public void testFindVariableByValue() {
     Variable.variableList = new ArrayList<>();
     Variable.addVariable(VAR_NAME);
     variable = new Variable(VAR_NAME);
     Variable.setVariableValue(VAR_NAME, VALUE);
     int index = Variable.findVariable(Integer.toString(VALUE));
     assertNotNull(index);
     assertEquals(0, index);
 }

 @Test
 public void testRemoveVariable() {
     Variable.variableList = new ArrayList<>();
     Variable.addVariable(VAR_NAME);
     assertEquals(1, Variable.variableList.size());
     Variable.removeVariable(VAR_NAME);
     assertEquals(0, Variable.variableList.size());
 }

 @Test
 public void testRemoveNonExistentVariable() {
     Variable.variableList = new ArrayList<>();
     Variable.removeVariable(VAR_NAME);
     assertEquals(0, Variable.variableList.size());
 }

 @Test (expected = IndexOutOfBoundsException.class)
 public void testGetVariableValueNegativeIndex() {
     Variable.variableList = new ArrayList<>();
     Variable.addVariable(VAR_NAME);
     Variable.addVariable("anotherVarName");
     Variable.setVariableValue(VAR_NAME, VALUE);
     Variable.setVariableValue("anotherVarName", VALUE);
     int index = -1;
     Variable.getVariableValue(Integer.toString(index));
 }

 @Test (expected = NullPointerException.class)
 public void testGetVariableValueNullName() {
     Variable.variableList = new ArrayList<>();
     Variable.addVariable(VAR_NAME);
     String nullName = null;
     Variable.getVariableValue(nullName);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testGetVariableValueEmptyString() {
     Variable.variableList = new ArrayList<>();
     Variable.addVariable(VAR_NAME);
     String emptyString = "";
     Variable.getVariableValue(emptyString);
 }

 @Test (expected = NumberFormatException.class)
 public void testGetVariableValueInvalidInteger() {
     Variable.variableList = new ArrayList<>();
     Variable.addVariable(VAR_NAME);
     Variable.addVariable("anotherVarName");
     Variable.setVariableValue(VAR_NAME, VALUE);
     Variable.setVariableValue("anotherVarName", VALUE);
     String invalidString = "invalidValue";
     Variable.getVariableValue(invalidString);
 }

 @Test
 public void testSetVariableValue() {
     Variable.variableList = new ArrayList<>();
     Variable.addVariable(VAR_NAME);
     int initialValue = Variable.getVariableValue(VAR_NAME);
     Variable.setVariableValue(VAR_NAME, VALUE);
     assertEquals(VALUE, Variable.getVariableValue(VAR_NAME));
 }

 @Test (expected = IndexOutOfBoundsException.class)
 public void testSetVariableValueNegativeIndex() {
     Variable.variableList = new ArrayList<>();
     Variable.addVariable(VAR_NAME);
     Variable.addVariable("anotherVarName");
     Variable.setVariableValue(VAR_NAME, VALUE);
     Variable.setVariableValue("anotherVarName", VALUE);
     int index = -1;
     Variable.setVariableValue(Integer.toString(index), VALUE);
 }

 @Test (expected = NullPointerException.class)
 public void testSetVariableValueNullName() {
     Variable.variableList = new ArrayList<>();
     Variable.addVariable(VAR_NAME);
     String nullName = null;
     Variable.setVariableValue(nullName, VALUE);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testSetVariableValueEmptyString() {
     Variable.variableList = new ArrayList<>();
     Variable.addVariable(VAR_NAME);
     String emptyString = "";
     Variable.setVariableValue(emptyString, VALUE);
 }

 @Test (expected = NumberFormatException.class)
 public void testSetVariableValueInvalidInteger() {
     Variable.variableList = new ArrayList<>();
     Variable.addVariable(VAR_NAME);
     Variable.addVariable("anotherVarName");
     Variable.setVariableValue(VAR_NAME, VALUE);
     Variable.setVariableValue("anotherVarName", VALUE);
     String invalidString = "invalidValue";
     Variable.setVariableValue(invalidString, VALUE);
 }

 @Test
 public void testGetVariableValueWithAsterisk() {
     Variable.variableList = new ArrayList<>();
     Variable.addVariable("*varName1");
     Variable.addVariable("varName2");
     Variable.addVariable("*varName3");
     String foundName = Variable.getVariableValue("*varName1");
     String expectedName = "varName1";
     assertEquals(expectedName, foundName);
 }

 @Test
 public void testGetVariableValueWithNonExistentAsterisk() {
     Variable.variableList = new ArrayList<>();
     Variable.addVariable("*varName1");
     Variable.addVariable("varName2");
     Variable.addVariable("*varName3");
     String foundName = Variable.getVariableValue("*nonExistentVarName");
     assertNull(foundName);
 }
}