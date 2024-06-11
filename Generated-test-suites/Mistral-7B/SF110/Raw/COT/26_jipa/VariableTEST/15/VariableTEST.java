package jipa.test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Test;

public class VariableTest {

 private static Variable variable1;
 private static Variable variable2;
 private static Variable variable3;
 private static List<Variable> variableList = new ArrayList<>();

 @Test
 public void testConstructor() {
 Variable variable = new Variable("testVariable");
 assertEquals("testVariable", variable.getName());
 assertEquals(0, variable.getValue());
 }

 @Test
 public void testGetValue() {
 variable1 = new Variable("variable1");
 variable1.setValue(5);
 assertEquals(5, variable1.getValue());
 }

 @Test
 public void testSetValue() {
 variable1 = new Variable("variable1");
 variable1.setValue(5);
 variable1.setValue(10);
 assertEquals(10, variable1.getValue());
 }

 @Test
 public void testGetName() {
 variable1 = new Variable("variable1");
 variable1.setValue(5);
 assertEquals("variable1", variable1.getName());
 }

 @Test
 public void testAddVariable() {
 Variable.addVariable("variable2");
 assertEquals(1, Variable.variableList.size());
 Variable.addVariable("variable3");
 assertEquals(2, Variable.variableList.size());
 Variable.addVariable("variable1"); // should not add since it already exists
 assertEquals(2, Variable.variableList.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddVariableWithNullName() {
 Variable.addVariable(null);
 fail("Expected an IllegalStateException but didn't get one.");
 }

 @Test
 public void testRemoveVariable() {
 Variable.addVariable("variable4");
 Variable.removeVariable("variable4");
 assertEquals(0, Variable.variableList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveVariableWithNullName() {
 Variable.removeVariable(null);
 fail("Expected an IllegalArgumentException but didn't get one.");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetVariableValueWithNonExistentVariable() {
 Variable.getVariableValue("nonExistentVariable");
 fail("Expected an IndexOutOfBoundsException but didn't get one.");
 }

 @Test
 public void testGetVariableValue() {
 Variable.addVariable("variable5");
 Variable.setVariableValue("variable5", 10);
 assertEquals(10, Variable.getVariableValue("variable5"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueWithNullName() {
 Variable.setVariableValue(null, 10);
 fail("Expected an IllegalArgumentException but didn't get one.");
 }

 @Test
 public void testSetVariableValue() {
 Variable.addVariable("variable6");
 Variable.setVariableValue("variable6", 20);
 assertEquals(20, Variable.getVariableValue("variable6"));
 }

 @Test
 public void testFindVariable() {
 Variable.addVariable("variable7");
 Variable.addVariable("variable8");
 Variable variable = new Variable("variable7");
 int index = Variable.findVariable("variable7");
 assertEquals(0, index);
 index = Variable.findVariable("variable8");
 assertEquals(1, index);
 index = Variable.findVariable("variable9");
 assertEquals(-1, index);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindVariableWithNullName() {
 Variable.findVariable(null);
 fail("Expected an IllegalArgumentException but didn't get one.");
 }

 @Test
 public void testFindVariableWithReference() {
 Variable variable5 = Variable.variableList.get(4);
 int index = Variable.findVariable("&variable5");
 assertEquals(4, index);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testFindVariableWithNonExistentReference() {
 Variable.findVariable("&nonExistentVariable");
 fail("Expected an IndexOutOfBoundsException but didn't get one.");
 }
}