package jipa.test;

import org.junit.Assert;
import org.junit.Test;
import jipa.Variable;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class VariableTest {

 private List<Variable> variableList;

 @BeforeClass
 public static void setUpClass() {
 Variable.variableList = new ArrayList<Variable>();
 }

 @Test
 public void testAddVariable() {
 String varName = "testVar";
 Variable.addVariable(varName);
 assertFalse(Variable.variableList.isEmpty());
 Variable testVariable = Variable.variableList.get(0);
 assertEquals(varName, testVariable.getName());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddVariableDuplicateName() {
 String varName = "testVar";
 Variable.addVariable(varName);
 Variable.addVariable(varName);
 }

 @Test
 public void testFindVariable() {
 String varName = "testVar";
 Variable.addVariable(varName);
 int index = Variable.variableList.indexOf(Variable.findVariable(varName));
 assertEquals(index, Variable.variableList.size() - 1);
 }

 @Test
 public void testFindVariableNull() {
 assertNull(Variable.findVariable(null));
 }

 @Test
 public void testFindVariableReference() {
 Variable refVariable = new Variable("refVariable");
 Variable.addVariable("refVariable");
 int value = refVariable.getValue();
 int refValue = Variable.getVariableValue("&refVariable");
 assertEquals(value, refValue);
 }

 @Test
 public void testFindVariableNotFound() {
 assertNull(Variable.findVariable("notExists"));
 }

 @Test
 public void testRemoveVariable() {
 String varName = "testVar";
 Variable.addVariable(varName);
 Variable.removeVariable(varName);
 assertFalse(Variable.variableList.contains(Variable.findVariable(varName)));
 }

 @Test
 public void testSetVariableValue() {
 String varName = "testVar";
 Variable.addVariable(varName);
 int initialValue = 0;
 Variable.setVariableValue(varName, initialValue + 1);
 int newValue = Variable.getVariableValue(varName);
 assertEquals(initialValue + 1, newValue);
 }

 @Test
 public void testSetVariableValueNotFound() {
 int newValue = Variable.setVariableValue("notExists", 1);
 assertEquals(0, newValue);
 }

 @Test
 public void testGetVariableValue() {
 String varName = "testVar";
 Variable.addVariable(varName);
 Variable.setVariableValue(varName, 123);
 int value = Variable.getVariableValue(varName);
 assertEquals(123, value);
 }

 @Test
 public void testGetVariableValueNull() {
 assertNull(Variable.getVariableValue(null));
 }

 @Test
 public void testGetVariableValueReference() {
 Variable refVariable = new Variable("refVariable");
 Variable.addVariable("refVariable");
 refVariable.setValue(123);
 int value = Variable.getVariableValue("&refVariable");
 assertEquals(123, value);
 }

 @Test
 public void testGetVariableValueNotFound() {
 assertNull(Variable.getVariableValue("notExists"));
 }

 @AfterClass
 public static void tearDownClass() {
 Variable.variableList.clear();
 }

}