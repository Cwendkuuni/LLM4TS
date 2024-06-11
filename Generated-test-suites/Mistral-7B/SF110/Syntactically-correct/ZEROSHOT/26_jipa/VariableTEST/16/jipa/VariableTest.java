import org.junit.BeforeClass;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class VariableTest {

 private static List<Variable> variableList;

 @BeforeClass
 public static void setup() {
 variableList = new ArrayList<>();
 Variable.addVariable("var1");
 Variable.addVariable("var2");
 Variable.addVariable("var3");
 }

 @Test
 public void testAddVariable() {
 String varName = "var4";
 assertTrue(Variable.addVariable(varName));
 assertEquals(4, Variable.variableList.size());
 Variable var4 = Variable.variableList.get(3);
 assertEquals("var4", var4.getName());
 assertEquals(0, var4.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateVariable() {
 String varName = "var1";
 assertFalse(Variable.addVariable(varName));
 }

 @Test
 public void testRemoveVariable() {
 String varName = "var2";
 Variable.removeVariable(varName);
 assertEquals(3, Variable.variableList.size());
 assertNull(Variable.findVariable(varName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveNonExistentVariable() {
 String varName = "nonExistentVar";
 Variable.removeVariable(varName);
 }

 @Test
 public void testGetVariableValue() {
 String varName = "var1";
 int expectedValue = 0;
 assertEquals(expectedValue, Variable.getVariableValue(varName));

 varName = "*var2";
 int var2Value = 2;
 Variable.setVariableValue(varName.substring(1), var2Value);
 expectedValue = var2Value;
 assertEquals(expectedValue, Variable.getVariableValue(varName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetInvalidVariableValue() {
 String varName = "nonExistentVar";
 int expectedValue = 0;
 assertNull(Variable.getVariableValue(varName));
 }

 @Test
 public void testSetVariableValue() {
 String varName = "var1";
 int newValue = 42;
 Variable.setVariableValue(varName, newValue);
 int expectedValue = newValue;
 assertEquals(expectedValue, Variable.getVariableValue(varName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetInvalidVariableValue() {
 String varName = "var1";
 Variable.setVariableValue(varName, -1);
 }

 @Test
 public void testFindVariable() {
 String varName = "var1";
 int expectedIndex = 0;
 int index = Variable.findVariable(varName);
 assertTrue(index == expectedIndex);
 }

 @Test
 public void testFindReferenceVariable() {
 String varName = "&var2";
 int expectedValue = 2;
 int value = Variable.findVariable(varName);
 assertTrue(value == expectedValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindNonExistentVariable() {
 String varName = "nonExistentVar";
 int index = Variable.findVariable(varName);
 assertNull(index);
 }
}