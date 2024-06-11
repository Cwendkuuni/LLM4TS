import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class VariableTest {

 private static final String VALID_VAR_NAME_1 = "validVar1";
 private static final String VALID_VAR_NAME_2 = "validVar2";
 private static final String INVALID_CHAR_VAR_NAME = "var#";
 private static final String DUPLICATE_VAR_NAME = "duplicateVar";
 private static final int VALID_VALUE = 42;
 private static final int NEGATIVE_VALUE = -1;
 private static final int ZERO_VALUE = 0;

 @Test
 public void testAddVariable() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.addVariable(VALID_VAR_NAME_2);
  List<Variable> variables = Variable.variableList;
  Assert.assertEquals(2, variables.size());
 }

 @Test(expected = java.util.ConcurrentModificationException.class)
 public void testAddVariableConcurrentModification() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.addVariable(VALID_VAR_NAME_2);
  Variable.addVariable(VALID_VAR_NAME_1);
 }

 @Test
 public void testFindVariable() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.addVariable(VALID_VAR_NAME_2);
  Variable foundVariable = Variable.variableList.get(Variable.variableList.size() - 1);
  int index = Variable.findVariable(foundVariable.getName());
  Variable expectedVariable = Variable.variableList.get(0);
  Assert.assertEquals(expectedVariable, Variable.variableList.get(index));
 }

 @Test
 public void testFindVariableInvalidName() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.addVariable(VALID_VAR_NAME_2);
  int index = Variable.findVariable(INVALID_CHAR_VAR_NAME);
  Assert.assertEquals(-1, index);
 }

 @Test
 public void testFindVariableDuplicateName() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.addVariable(VALID_VAR_NAME_1);
  int index = Variable.findVariable(VALID_VAR_NAME_1);
  Variable expectedVariable = Variable.variableList.get(0);
  Assert.assertEquals(expectedVariable, Variable.variableList.get(index));
 }

 @Test
 public void testRemoveVariable() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.removeVariable(VALID_VAR_NAME_1);
  List<Variable> variables = Variable.variableList;
  Assert.assertEquals(1, variables.size());
 }

 @Test
 public void testRemoveNonexistentVariable() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.removeVariable("invalidVarName");
  List<Variable> variables = Variable.variableList;
  Assert.assertEquals(1, variables.size());
 }

 @Test
 public void testSetVariableValue() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.setVariableValue(VALID_VAR_NAME_1, VALID_VALUE);
  Variable expectedVariable = Variable.variableList.get(0);
  Assert.assertEquals(VALID_VALUE, expectedVariable.getValue());
 }

 @Test
 public void testSetVariableValueNegativeValue() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.setVariableValue(VALID_VAR_NAME_1, NEGATIVE_VALUE);
  Variable expectedVariable = Variable.variableList.get(0);
  Assert.assertEquals(ZERO_VALUE, expectedVariable.getValue());
 }

 @Test
 public void testGetVariableValue() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.setVariableValue(VALID_VAR_NAME_1, VALID_VALUE);
  int value = Variable.getVariableValue(VALID_VAR_NAME_1);
  Assert.assertEquals(VALID_VALUE, value);
 }

 @Test
 public void testGetVariableValueInvalidName() {
  int value = Variable.getVariableValue("invalidVarName");
  Assert.assertEquals(ZERO_VALUE, value);
 }

 @Test
 public void testGetVariableValueStarNotation() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.addVariable(VALID_VAR_NAME_2);
  Variable.setVariableValue(VALID_VAR_NAME_1, VALID_VALUE);
  int value = Variable.getVariableValue("*" + VALID_VAR_NAME_1.substring(1));
  Assert.assertEquals(VALID_VALUE, value);
 }

 @Test
 public void testGetVariableValueStarInvalidName() {
  Variable.addVariable(VALID_VAR_NAME_1);
  int value = Variable.getVariableValue("*invalidVarName");
  Assert.assertEquals(ZERO_VALUE, value);
 }
}