import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class VariableTest {

 private static final String VALID_VAR_NAME_1 = "validVar1";
 private static final String VALID_VAR_NAME_2 = "validVar2";
 private static final String INVALID_VAR_NAME = "&invalidVar";
 private static final String VAR_NAME_WITH_ASTERISK = "*validVar";
 private static final int VALID_VALUE = 42;
 private static final int INVALID_VALUE = -1;

 @Test
 public void testAddVariable() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.addVariable(VALID_VAR_NAME_2);
  Assert.assertEquals(2, Variable.variableList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateVariable() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.addVariable(VALID_VAR_NAME_1);
 }

 @Test
 public void testGetVariableValue_ValidVariableName() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.addVariable(VALID_VAR_NAME_2);
  Variable.setVariableValue(VALID_VAR_NAME_1, VALID_VALUE);
  Variable.setVariableValue(VALID_VAR_NAME_2, VALID_VALUE * 2);
  Assert.assertEquals(VALID_VALUE, Variable.getVariableValue(VALID_VAR_NAME_1));
  Assert.assertEquals(VALID_VALUE * 2, Variable.getVariableValue(VALID_VAR_NAME_2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetVariableValue_InvalidVariableName() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Assert.assertEquals(0, Variable.getVariableValue(INVALID_VAR_NAME));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetVariableValue_InvalidVariableNameWithAsterisk() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.addVariable(VAR_NAME_WITH_ASTERISK);
  Assert.assertEquals(-1, (int) Variable.getVariableValue(VAR_NAME_WITH_ASTERISK));
 }

 @Test
 public void testRemoveVariable() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.addVariable(VALID_VAR_NAME_2);
  Variable.removeVariable(VALID_VAR_NAME_1);
  Assert.assertEquals(1, Variable.variableList.size());
  Assert.assertEquals(VALID_VALUE * 2, Variable.getVariableValue(VALID_VAR_NAME_2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveNonExistentVariable() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.removeVariable(INVALID_VAR_NAME);
 }

 @Test
 public void testSetVariableValue() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.setVariableValue(VALID_VAR_NAME_1, VALID_VALUE);
  Assert.assertEquals(VALID_VALUE, Variable.getVariableValue(VALID_VAR_NAME_1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValue_InvalidVariableName() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.setVariableValue(INVALID_VAR_NAME, INVALID_VALUE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValue_InvalidValue() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.setVariableValue(VALID_VAR_NAME_1, INVALID_VALUE);
 }

 @Test
 public void testFindVariable() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.addVariable(VALID_VAR_NAME_2);
  int index1 = Variable.findVariable(VALID_VAR_NAME_1);
  int index2 = Variable.findVariable(VALID_VAR_NAME_2);
  Variable.removeVariable(VALID_VAR_NAME_1);
  Assert.assertEquals(0, index1);
  Assert.assertEquals(1, index2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindVariable_InvalidVariableNameWithAsterisk() {
  Variable.addVariable(VALID_VAR_NAME_1);
  Variable.addVariable(VAR_NAME_WITH_ASTERISK);
  Assert.assertEquals(-1, Variable.findVariable(VAR_NAME_WITH_ASTERISK));
 }
}