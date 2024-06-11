package jipa.test;

import jipa.Variable;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class VariableTest {

 @Test
 public void testAddVariable_newVariable_shouldAddToList() {
 Variable.variableList = new ArrayList<>();
 String varName = "testVar";
 Variable variable = new Variable(varName);
 assertTrue(Variable.addVariable(varName));
 assertEquals(variable, Variable.variableList.get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddVariable_existingVariable_shouldThrowException() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("testVar");
 try {
 Variable.addVariable("testVar");
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 }

 @Test
 public void testFindVariable_existingVariable_shouldReturnIndex() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("testVar1");
 Variable.addVariable("testVar2");
 int index = Variable.findVariable("testVar1");
 assertEquals(index, 0);
 }

 @Test
 public void testFindVariable_nonExistentVariable_shouldReturnMinusOne() {
 Variable.variableList = new ArrayList<>();
 int index = Variable.findVariable("testVar");
 assertEquals(index, -1);
 }

 @Test(expected = NumberFormatException.class)
 public void testGetVariableValue_invalidVarName_shouldThrowException() {
 String invalidVarName = "";
 int value = Variable.getVariableValue(invalidVarName);
 // expected exception
 }

 @Test
 public void testGetVariableValue_existingVariable_shouldReturnValue() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("testVar1");
 Variable.setVariableValue("testVar1", 42);
 int value = Variable.getVariableValue("testVar1");
 assertEquals(42, value);
 }

 @Test
 public void testGetVariableValue_nonExistentVariable_shouldReturnZero() {
 Variable.variableList = new ArrayList<>();
 int value = Variable.getVariableValue("testVar");
 assertEquals(0, value);
 }

 @Test
 public void testSetVariableValue_existingVariable_shouldSetValue() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("testVar1");
 Variable.setVariableValue("testVar1", 42);
 int value = Variable.getVariableValue("testVar1");
 assertEquals(42, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValue_nonExistentVariable_shouldThrowException() {
 Variable.variableList = new ArrayList<>();
 try {
 Variable.setVariableValue("testVar", 42);
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testSetVariableValue_invalidVarName_shouldThrowException() {
 String invalidVarName = "";
 try {
 Variable.setVariableValue(invalidVarName, 42);
 } catch (NumberFormatException e) {
 // expected exception
 }
 }

 @Test
 public void testRemoveVariable_existingVariable_shouldRemoveFromList() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("testVar1");
 Variable.addVariable("testVar2");
 int index = Variable.findVariable("testVar1");
 Variable.removeVariable("testVar1");
 assertEquals(1, Variable.variableList.size());
 assertFalse(Variable.variableList.contains("testVar1"));
 }

 @Test
 public void testRemoveVariable_nonExistentVariable_shouldDoNothing() {
 Variable.variableList = new ArrayList<>();
 Variable.removeVariable("testVar");
 assertEquals(0, Variable.variableList.size());
 }

 @Test
 public void testFindVariable_referenceVariable_shouldReturnValue() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("testVar1");
 Variable.addVariable("testVar2");
 int refValue = 42;
 String refVarName = "*testVar1";
 assertEquals(refValue, Variable.getVariableValue(refVarName));
 }

 @Test(expected = NumberFormatException.class)
 public void testFindVariable_invalidRefVariable_shouldThrowException() {
 String invalidRefVarName = "";
 int value = Variable.getVariableValue(invalidRefVarName);
 // expected exception
 }

 @Test
 public void testFindVariable_referenceVariable_existingVariable_shouldReturnValue() {
 Variable.variableList = new ArrayList<>();Variable.addVariable("testVar1");
 Variable.setVariableValue("testVar1", 42);
 String refVarName = "*testVar1";
 int value = Variable.findVariable(refVarName);
 assertEquals(42, value);
 }

 @Test
 public void testFindVariable_referenceVariable_nonExistentVariable_shouldReturnMinusOne() {
 Variable.variableList = new ArrayList<>();
 int value = Variable.findVariable("*testVar1");
 assertEquals(value, -1);
 }

}