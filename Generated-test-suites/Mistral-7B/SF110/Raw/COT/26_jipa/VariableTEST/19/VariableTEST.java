package jipa.test;

import static jipa.Variable.addVariable;
import static jipa.Variable.findVariable;
import static jipa.Variable.getVariableValue;
import static jipa.Variable.removeVariable;
import static jipa.Variable.setVariableValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class VariableTest {

 private static Variable var1;
 private static Variable var2;
 private static Variable var3;

 @BeforeClass
 public static void setup() {
 addVariable("var1");
 addVariable("var2");
 addVariable("var3");
 var1 = Variable.variableList.get(0);
 var2 = Variable.variableList.get(1);
 var3 = Variable.variableList.get(2);
 }

 @Test
 public void testAddVariable() {
 String varName = "var4";
 assertTrue(addVariable(varName));
 assertEquals(4, Variable.variableList.size());
 }

 @Test
 public void testAddDuplicateVariable() {
 String varName = "var1";
 assertFalse(addVariable(varName));
 assertEquals(3, Variable.variableList.size());
 }

 @Test
 public void testGetName() {
 assertEquals("var1", var1.getName());
 assertEquals("var2", var2.getName());
 assertEquals("var3", var3.getName());
 }

 @Test
 public void testSetValue() {
 var1.setValue(10);
 assertEquals(10, getVariableValue("var1"));
 }

 @Test
 public void testGetValue() {
 assertEquals(0, var1.getValue());
 }

 @Test
 public void testGetVariableValueWithValidName() {
 assertEquals(0, getVariableValue("var1"));
 assertEquals(0, getVariableValue("var2"));
 assertEquals(0, getVariableValue("var3"));
 }

 @Test
 public void testGetVariableValueWithInvalidName() {
 assertNull(getVariableValue("invalidName"));
 }

 @Test
 public void testSetVariableValue() {
 setVariableValue("var1", 10);
 assertEquals(10, getVariableValue("var1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueWithInvalidName() {
 setVariableValue("invalidName", 10);
 }

 @Test
 public void testRemoveVariable() {
 String varName = "var1";
 removeVariable(varName);
 assertEquals(2, Variable.variableList.size());
 assertEquals(0, getVariableValue(varName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveVariableWithInvalidName() {
 removeVariable("invalidName");
 assertEquals(3, Variable.variableList.size());
 }

 @Test
 public void testFindVariable() {
 assertEquals(0, findVariable("var1"));
 assertEquals(1, findVariable("var2"));
 assertEquals(2, findVariable("var3"));
 assertNull(findVariable("invalidName"));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testFindVariableWithInvalidIndex() {
 findVariable(-1);
 }

 @Test
 public void testStaticInitialization() {
 List<Variable> expectedList = new ArrayList<>();
 assertEquals(expectedList, Variable.variableList);
 }

}