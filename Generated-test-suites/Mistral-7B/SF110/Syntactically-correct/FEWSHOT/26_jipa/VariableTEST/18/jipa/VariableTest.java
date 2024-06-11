package jipa;

import static org.junit.Assert.*;
import java.util.*;

public class VariableTest {

 private List<Variable> variableList;

 @BeforeClass
 public static void setupClass() {
 Variable.variableList = new ArrayList<>();
 }

 @Test
 public void testAddVariable() {
 String varName = "testVar";
 boolean added = Variable.addVariable(varName);
 assertTrue(added);
 assertEquals(1, Variable.variableList.size());
 }

 @Test
 public void testAddVariableExists() {
 String varName = "testVar";
 boolean added = Variable.addVariable(varName);
 assertFalse(added);
 assertEquals(1, Variable.variableList.size());
 }

 @Test
 public void testGetVariableValue() {
 String varName = "testVar";
 Variable variable = new Variable(varName);
 Variable.addVariable(varName);
 Variable.setVariableValue(varName, 42);
 int value = Variable.getVariableValue(varName);
 assertEquals(42, value);
 }

 @Test
 public void testGetVariableValueNotFound() {
 String varName = "notExist";
 int value = Variable.getVariableValue(varName);
 assertEquals(0, value);
 }

 @Test
 public void testSetVariableValue() {
 String varName = "testVar";
 Variable variable = new Variable(varName);
 Variable.addVariable(varName);
 Variable.setVariableValue(varName, 42);
 int value = Variable.getVariableValue(varName);
 assertEquals(42, value);
 }

 @Test
 public void testSetVariableValueNullList() {
 Variable.variableList = null;
 String varName = "testVar";
 Variable variable = new Variable(varName);
 Variable.addVariable(varName);
 Variable.setVariableValue(varName, 42);
 assertNull(Variable.variableList);
 }

 @Test
 public void testFindVariable() {
 String varName = "testVar";
 Variable variable = new Variable(varName);
 Variable.addVariable(varName);
 int index = Variable.findVariable(varName);
 assertEquals(0, index);
 }

 @Test
 public void testFindVariableNull() {
 String varName = null;
 int index = Variable.findVariable(varName);
 assertEquals(-1, index);
 }

 @Test
 public void testFindVariableReference() {
 String varName = "&testVar";
 Variable variable = new Variable("testVar");
 Variable.addVariable("testVar");
 Variable.setVariableValue("testVar", 42);
 int value = Variable.getVariableValue(varName);
 assertEquals(42, value);
 }

 @Test
 public void testFindVariableReferenceNull() {
 String varName = "&nullVar";
 int value = Variable.getVariableValue(varName);
 assertEquals(0, value);
 }

 @Test
 public void testRemoveVariable() {
 String varName = "testVar";
 Variable variable = new Variable(varName);
 Variable.addVariable(varName);
 int sizeBeforeRemove = Variable.variableList.size();
 Variable.removeVariable(varName);
 int sizeAfterRemove = Variable.variableList.size();
 assertEquals(sizeBeforeRemove - 1, sizeAfterRemove);
 }

 @Test
 public void testRemoveVariableNullName() {
 String varName = null;
 Variable.removeVariable(varName);
 assertEquals(0, Variable.variableList.size());
 }

 @Test
 public void testRemoveVariableNotFound() {
 String varName = "notExist";
 Variable.removeVariable(varName);
 assertEquals(0, Variable.variableList.size());
 }
}