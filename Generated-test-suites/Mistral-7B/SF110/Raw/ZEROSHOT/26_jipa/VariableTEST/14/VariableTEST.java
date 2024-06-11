package jipa.test;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class VariableTest {

 private static List<Variable> variableList;

 @BeforeClass
 public static void setup() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("var1");
 Variable.addVariable("var2");
 Variable.addVariable("var3");
 }

 @Test
 public void testAddVariable() {
 String newVarName = "newVar";
 assertTrue(Variable.addVariable(newVarName));
 assertEquals(4, Variable.variableList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddVariableWithExistingName() {
 String existingVarName = "var1";
 assertFalse(Variable.addVariable(existingVarName));
 }

 @Test
 public void testRemoveVariable() {
 String varNameToRemove = "var2";
 Variable.removeVariable(varNameToRemove);
 assertEquals(3, Variable.variableList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveNonExistentVariable() {
 String nonExistentVarName = "nonExistentVar";
 Variable.removeVariable(nonExistentVarName);
 }

 @Test
 public void testGetVariableValue() {
 String varName = "var1";
 int expectedValue = 0;
 assertEquals(expectedValue, Variable.getVariableValue(varName));

 String refVarName = "*var3";
 int expectedRefValue = 3;
 assertEquals(expectedRefValue, Variable.getVariableValue(refVarName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetVariableValueWithNonExistentVariable() {
 String nonExistentVarName = "nonExistentVar";
 assertEquals(0, Variable.getVariableValue(nonExistentVarName));
 }

 @Test
 public void testSetVariableValue() {
 String varName = "var3";
 int newValue = 10;
 Variable.setVariableValue(varName, newValue);
 assertEquals(newValue, Variable.getVariableValue(varName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueWithNonExistentVariable() {
 String nonExistentVarName = "nonExistentVar";
 Variable.setVariableValue(nonExistentVarName, 5);
 }
}