package jipa.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class VariableTest {

 private static ArrayList<Variable> variableList;

 @Before
 public void setUp() {
 Variable.variableList = new ArrayList<Variable>();
 }

 @Test
 public void testAddVariable() {
 String varName = "testVar";
 assertTrue(Variable.addVariable(varName));
 assertEquals(1, Variable.variableList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddVariableInvalidName() {
 String varName = "";
 assertFalse(Variable.addVariable(varName));
 assertEquals(0, Variable.variableList.size());
 }

 @Test
 public void testFindVariable() {
 String varName = "testVar";
 Variable.addVariable(varName);
 int index = Variable.findVariable(varName);
 assertEquals(0, index);
 }

 @Test
 public void testFindVariableNotFound() {
 int index = Variable.findVariable("nonexistentVar");
 assertEquals(-1, index);
 }

 @Test
 public void testSetVariableValue() {
 String varName = "testVar";
 Variable.addVariable(varName);
 int value = 5;
 Variable.setVariableValue(varName, value);
 assertEquals(value, Variable.getVariableValue(varName));
 }

 @Test
 public void testGetVariableValue() {
 String varName = "testVar";
 Variable.addVariable(varName);
 Variable.setVariableValue(varName, 5);
 assertEquals(5, Variable.getVariableValue(varName));
 }

 @Test
 public void testGetVariableValueReference() {
 String varName = "*testVar";
 Variable.addVariable("testVar");
 Variable.setVariableValue("testVar", 5);
 assertEquals(5, Variable.getVariableValue(varName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindVariableInvalidReference() {
 String varName = "&nonexistentVar";
 int index = Variable.findVariable(varName);
 assertEquals(-1, index);
 }

 @Test
 public void testRemoveVariable() {
 String varName = "testVar";
 Variable.addVariable(varName);
 int initialSize = Variable.variableList.size();
 Variable.removeVariable(varName);
 int finalSize = Variable.variableList.size();
 assertEquals(initialSize - 1, finalSize);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveVariableInvalidName() {
 String varName = "nonexistentVar";
 Variable.removeVariable(varName);
 assertEquals(Variable.variableList.size(), 0);
 }

 @After
 public void tearDown() {
 Variable.variableList.clear();
 }

}