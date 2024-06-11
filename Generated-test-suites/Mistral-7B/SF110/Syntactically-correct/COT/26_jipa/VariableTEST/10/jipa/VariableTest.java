package jipa.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jipa.Variable;

public class VariableTest {

 private List<Variable> variableList = new ArrayList<>();

 @Test
 public void testAddVariable_ValidName_ShouldAddVariableToList() {
 Variable.variableList = new ArrayList<>();
 String varName = "testVar";
 boolean expectedResult = Variable.addVariable(varName);
 assertTrue(expectedResult);
 Variable actualVariable = Variable.variableList.get(0);
 assertEquals(varName, actualVariable.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddVariable_DuplicateName_ShouldThrowIllegalArgumentException() {
 Variable.variableList = new ArrayList<>();
 String varName = "testVar";
 Variable.addVariable(varName);
 boolean expectedResult = Variable.addVariable(varName);
 assertFalse(expectedResult);
 }

 @Test
 public void testRemoveVariable_ExistingVariable_ShouldRemoveVariableFromList() {
 Variable.variableList = new ArrayList<>();
 String varName = "testVar";
 Variable.addVariable(varName);
 int expectedResult = Variable.variableList.size();
 Variable.removeVariable(varName);
 int actualResult = Variable.variableList.size();
 assertEquals(expectedResult - 1, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveVariable_NonExistingVariable_ShouldThrowIllegalArgumentException() {
 Variable.variableList = new ArrayList<>();
 String varName = "testVar";
 Variable.addVariable(varName);
 Variable.removeVariable("nonExistingVar");
 }

 @Test
 public void testGetVariableValue_ExistingVariable_ShouldReturnValueOfVariable() {
 Variable.variableList = new ArrayList<>();
 String varName = "testVar";
 Variable.addVariable(varName);
 int initialValue = 10;
 Variable.setVariableValue(varName, initialValue);
 int expectedResult = initialValue;
 int actualResult = Variable.getVariableValue(varName);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetVariableValue_NonExistingVariable_ShouldThrowIllegalArgumentException() {
 Variable.getVariableValue("nonExistingVar");
 }

 @Test
 public void testSetVariableValue_ExistingVariable_ShouldSetValueOfVariable() {
 Variable.variableList = new ArrayList<>();
 String varName = "testVar";
 Variable.addVariable(varName);
 int initialValue = 10;
 Variable.setVariableValue(varName, initialValue);
 int newValue = 20;
 Variable.setVariableValue(varName, newValue);
 int expectedResult = newValue;
 int actualResult = Variable.getVariableValue(varName);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValue_NonExistingVariable_ShouldThrowIllegalArgumentException() {
 String varName = "nonExistingVar";
 Variable.setVariableValue(varName, 10);
 }

 @Test
 public void testFindVariable_ExistingVariable_ShouldReturnIndexOfVariableInList() {
 Variable.variableList = new ArrayList<>();
 String varName = "testVar";
 Variable.addVariable(varName);
 int expectedIndex = 0;
 int actualIndex = Variable.findVariable(varName);
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testFindVariable_NonExistingVariable_ShouldReturnMinusOne() {
 Variable.variableList = new ArrayList<>();
 String varName = "nonExistingVar";
 int actualIndex = Variable.findVariable(varName);
 assertEquals(-1, actualIndex);
 }

 @Test
 public void testFindVariable_ReferenceToVariable_ShouldReturnValueOfVariable() {
 Variable.variableList = new ArrayList<>();
 String refVarName = "&testVar";
 Variable.addVariable("testVar");
 int expectedResult = 0;
 int actualResult = Variable.findVariable(refVarName);
 if (actualResult != -1) {
 expectedResult = Variable.variableList.get(actualResult).getValue();
 }
 int initialValue = 10;
 Variable.setVariableValue("testVar", initialValue);
 int newValue = 20;
 Variable.setVariableValue("testVar", newValue);
 int finalValue = Variable.findVariable(refVarName);
 assertEquals(newValue, finalValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindVariable_InvalidReference_ShouldThrowIllegalArgumentException() {
 Variable.findVariable("&nonExistingVar");
 }
}