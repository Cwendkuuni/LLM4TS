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
 public void testAddVariableDuplicateName() {
 String duplicateVarName = "var1";
 assertFalse(Variable.addVariable(duplicateVarName));
 assertEquals(4, Variable.variableList.size());
 Variable.addVariable(duplicateVarName); // this should throw an exception
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
 assertEquals(3, Variable.variableList.size());
 }

 @Test
 public void testGetVariableValue() {
 String getVarName = "var1";
 int getVarValue = Variable.getVariableValue(getVarName);
 assertEquals(0, getVarValue);
 }

 @Test
 public void testGetVariableValueExistingReference() {
 String refVarName = "*var2";
 int refVarValue = Variable.getVariableValue(refVarName);
 int var2Value = Variable.getVariableList().get(1).getValue();
 assertEquals(var2Value, refVarValue);
 }

 @Test
 public void testSetVariableValue() {
 String setVarName = "var1";
 int setVarValue = 10;
 Variable.setVariableValue(setVarName, setVarValue);
 int getSetVarValue = Variable.getVariableValue(setVarName);
 assertEquals(setVarValue, getSetVarValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueNull() {
 String setVarName = null;
 int setVarValue = 10;
 Variable.setVariableValue(setVarName, setVarValue); // this should throw an exception
 }

 @Test
 public void testSetVariableValueNonExistent() {
 String setVarName = "nonExistentVar";
 int setVarValue = 10;
 Variable.setVariableValue(setVarName, setVarValue); // this should not change the list
 assertEquals(3, Variable.variableList.size());
 }
}