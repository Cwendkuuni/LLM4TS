import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import jipa.Variable;

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
 public void testAddDuplicateVariable() {
 String duplicateVarName = "var1";
 assertFalse(Variable.addVariable(duplicateVarName));
 assertEquals(4, Variable.variableList.size());
 Variable.removeVariable(duplicateVarName);
 }

 @Test
 public void testRemoveVariable() {
 String varNameToRemove = "var2";
 assertEquals(3, Variable.variableList.size());
 Variable.removeVariable(varNameToRemove);
 assertEquals(2, Variable.variableList.size());
 }

 @Test
 public void testGetVariableValue() {
 String varName = "var1";
 int expectedValue = 0;
 assertEquals(expectedValue, Variable.getVariableValue(varName));
 Variable.setVariableValue(varName, 5);
 expectedValue = 5;
 assertEquals(expectedValue, Variable.getVariableValue(varName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetNonExistentVariableValue() {
 String nonExistentVarName = "nonexistentVar";
 assertEquals(0, Variable.getVariableValue(nonExistentVarName));
 }

 @Test
 public void testSetVariableValue() {
 String varName = "var1";
 int newValue = 10;
 Variable.setVariableValue(varName, newValue);
 int expectedValue = 10;
 assertEquals(expectedValue, Variable.getVariableValue(varName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNonExistentVariableValue() {
 String nonExistentVarName = "nonexistentVar";
 Variable.setVariableValue(nonExistentVarName, 5);
 }
}