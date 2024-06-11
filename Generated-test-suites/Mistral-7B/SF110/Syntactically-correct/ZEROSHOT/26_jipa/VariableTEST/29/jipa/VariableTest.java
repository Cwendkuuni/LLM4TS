package jipa;

import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class VariableTest {

 private static final String VAR_NAME_1 = "var1";
 private static final String VAR_NAME_2 = "var2";
 private static final String VAR_NAME_3 = "*var3";
 private static final int VAR_VALUE_1 = 10;
 private static final int VAR_VALUE_2 = 20;
 private List<Variable> variableList;

 @BeforeClass
 public static void setupClass() {
 Variable.variableList = new ArrayList<>();
 }

 @Test
 public void testAddVariable_NewVariable_IsAddedToList() {
 Variable.addVariable(VAR_NAME_1);
 Assert.assertEquals(1, Variable.variableList.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddVariable_DuplicateVariable_ThrowsException() {
 Variable.addVariable(VAR_NAME_1);
 Variable.addVariable(VAR_NAME_1);
 }

 @Test
 public void testRemoveVariable_ExistingVariable_IsRemovedFromList() {
 Variable.addVariable(VAR_NAME_1);
 Variable.addVariable(VAR_NAME_2);
 Variable.removeVariable(VAR_NAME_1);
 Assert.assertEquals(1, Variable.variableList.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveVariable_NonExistentVariable_ThrowsException() {
 Variable.addVariable(VAR_NAME_1);
 Variable.removeVariable(VAR_NAME_3);
 }

 @Test
 public void testGetVariableValue_ExistingVariable_ReturnsCorrectValue() {
 Variable.addVariable(VAR_NAME_1);
 Variable.addVariable(VAR_NAME_2);
 Variable.setVariableValue(VAR_NAME_1, VAR_VALUE_1);
 Variable.setVariableValue(VAR_NAME_2, VAR_VALUE_2);

 Assert.assertEquals(0, Variable.getVariableValue(VAR_NAME_1));
 Assert.assertEquals(0, Variable.getVariableValue(VAR_NAME_2));
 Assert.assertEquals(VAR_VALUE_1, Variable.getVariableValue(VAR_NAME_1 + "&"));
 Assert.assertEquals(VAR_VALUE_2, Variable.getVariableValue(VAR_NAME_2));
 }

 @Test
 public void testSetVariableValue_ExistingVariable_SetsCorrectValue() {
 Variable.addVariable(VAR_NAME_1);
 Variable.addVariable(VAR_NAME_2);
 Variable.setVariableValue(VAR_NAME_1, VAR_VALUE_1);
 Variable.setVariableValue(VAR_NAME_2, VAR_VALUE_2);

 Variable.setVariableValue(VAR_NAME_1, 15);
 Assert.assertEquals(15, Variable.getVariableValue(VAR_NAME_1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetVariableValue_NonExistentVariable_ThrowsException() {
 Variable.addVariable(VAR_NAME_1);
 Variable.addVariable(VAR_NAME_2);
 Variable.setVariableValue(VAR_NAME_1, VAR_VALUE_1);
 Variable.setVariableValue(VAR_NAME_2, VAR_VALUE_2);

 Assert.assertEquals(0, Variable.getVariableValue(VAR_NAME_3));
 }

 @Test
 public void testFindVariable_ExistingVariable_ReturnsCorrectIndex() {
 Variable.addVariable(VAR_NAME_1);
 Variable.addVariable(VAR_NAME_2);
 Variable.addVariable(VAR_NAME_3);
 Variable.setVariableValue(VAR_NAME_1, VAR_VALUE_1);
 Variable.setVariableValue(VAR_NAME_2, VAR_VALUE_2);

 int index1 = Variable.findVariable(VAR_NAME_1);
 int index2 = Variable.findVariable(VAR_NAME_2);
 int index3 = Variable.findVariable(VAR_NAME_3);

 Assert.assertEquals(0, index1);
 Assert.assertEquals(1, index2);
 Assert.assertEquals(-1, index3);
 }

 @Test
 public void testFindVariable_ExistingVariableByReference_ReturnsCorrectValue() {
 Variable.addVariable(VAR_NAME_1);
 Variable.addVariable(VAR_NAME_2);
 Variable.addVariable(VAR_NAME_3);
 Variable.setVariableValue(VAR_NAME_1, VAR_VALUE_1);
 Variable.setVariableValue(VAR_NAME_2, VAR_VALUE_2);

 int variableValue1 = Variable.findVariable(VAR_NAME_1 + "&");

 Assert.assertEquals(VAR_VALUE_1, variableValue1);
 }

 @Test
 public void testFindVariable_NonExistentVariable_ReturnsMinusOne() {
 Variable.addVariable(VAR_NAME_1);
 Variable.addVariable(VAR_NAME_2);
 Variable.addVariable(VAR_NAME_3);
 Variable.setVariableValue(VAR_NAME_1, VAR_VALUE_1);
 Variable.setVariableValue(VAR_NAME_2, VAR_VALUE_2);

 int index = Variable.findVariable("nonexistentvar");

 Assert.assertEquals(-1, index);
 }
}