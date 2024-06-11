package jipa.test;

import org.junit.BeforeClass;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class VariableTest {

 @BeforeClass
 public static void setup() {
 Variable.variableList = new ArrayList<>();
 }

 @Test
 public void testVariableConstruction() {
 String varName = "testVar";
 Variable variable = new Variable(varName);
 assertEquals(varName, variable.getName());
 assertEquals(0, variable.getValue());
 }

 @Test
 public void testAddVariable() {
 String varName = "testAddVar";
 assertTrue(Variable.addVariable(varName));
 assertEquals(1, Variable.variableList.size());
 Variable addedVariable = Variable.variableList.get(0);
 assertEquals(varName, addedVariable.getName());
 assertEquals(0, addedVariable.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateVariable() {
 String duplicateVarName = "testAddVar";
 Variable.addVariable(duplicateVarName);
 assertFalse(Variable.addVariable(duplicateVarName));
 assertEquals(1, Variable.variableList.size());
 }

 @Test
 public void testRemoveVariable() {
 String varNameToRemove = "testRemoveVar";
 Variable.addVariable(varNameToRemove);
 assertEquals(1, Variable.variableList.size());
 Variable variableToRemove = Variable.variableList.get(0);
 Variable.removeVariable(varNameToRemove);
 assertEquals(0, Variable.variableList.size());
 assertNull(variableToRemove);
 }

 @Test
 public void testGetVariableValue() {
 String varName = "testGetVarValue";
 Variable.addVariable(varName);
 Variable testingVariable = Variable.variableList.get(0);
 testingVariable.setValue(10);
 assertEquals(10, Variable.getVariableValue(varName));
 }

 @Test
 public void testSetVariableValue() {
 String varName = "testSetVarValue";
 Variable.addVariable(varName);
 Variable testingVariable = Variable.variableList.get(0);
 Variable.setVariableValue(varName, 20);
 assertEquals(20, testingVariable.getValue());
 }

 @Test
 public void testGetValueOfReferenceVariable() {
 String refVarName = "*testRefVar";
 Variable.addVariable("testRefVar");
 Variable testingVariable = Variable.variableList.get(0);
 testingVariable.setValue(10);
 assertEquals(10, Variable.getVariableValue(refVarName));
 }

 @Test
 public void testFindVariable() {
 String varName = "testFindVar";
 Variable.addVariable(varName);
 Variable testingVariable = Variable.variableList.get(0);
 assertEquals(varName, Variable.findVariable(varName).getName());
 assertEquals(testingVariable.getValue(), Variable.findVariable(varName).getValue());
 }

 @Test
 public void testFindNonExistentVariable() {
 String nonExistentVarName = "testNonExistentVar";
 assertNull(Variable.findVariable(nonExistentVarName));
 }
}