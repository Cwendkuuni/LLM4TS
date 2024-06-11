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
 Variable.variableList = new ArrayList<>();
 }

 @Test
 public void testAddVariable() {
 String varName = "testVar";
 assertTrue(Variable.addVariable(varName));
 assertEquals(1, Variable.variableList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddVariableWithExistingName() {
 String varName = "testVar";
 Variable.addVariable(varName);
 assertFalse(Variable.addVariable(varName));
 }

 @Test
 public void testGetVariableValue() {
 String varName = "testVar";
 Variable.addVariable(varName);
 Variable.setVariableValue(varName, 42);
 assertEquals(42, Variable.getVariableValue(varName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetVariableValueWithNonExistingName() {
 assertEquals(0, Variable.getVariableValue("nonExistingVar"));
 }

 @Test
 public void testSetVariableValue() {
 String varName = "testVar";
 Variable.addVariable(varName);
 Variable.setVariableValue(varName, 42);
 assertEquals(42, Variable.getVariableValue(varName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueWithNonExistingName() {
 String varName = "nonExistingVar";
 Variable.setVariableValue(varName, 42);
 assertEquals(0, Variable.getVariableValue(varName));
 }

 @Test
 public void testFindVariable() {
 String varName = "testVar";
 Variable.addVariable(varName);
 final int index = Variable.findVariable(varName);
 assertEquals(0, index);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindVariableWithNonExistingName() {
 final int index = Variable.findVariable("nonExistingVar");
 assertEquals(-1, index);
 }

 @Test
 public void testRemoveVariable() {
 String varName = "testVar";
 Variable.addVariable(varName);
 Variable.removeVariable(varName);
 assertEquals(0, Variable.variableList.size());
 }

 @After
 public void tearDown() {
 Variable.variableList.clear();
 }
}