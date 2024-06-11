package jipa;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class VariableTest {

 private Variable variable;
 private static List<Variable> variableList = new ArrayList<>();

 @Test
 public void testAddVariable() {
 Variable.variableList = new ArrayList<>();
 String varName = "testVar";
 boolean added = Variable.addVariable(varName);
 assertTrue(added);
 assertEquals(1, Variable.variableList.size());
 }

 @Test
 public void testAddVariableWithExistingName() {
 Variable.variableList = new ArrayList<>();
 String varName = "testVar";
 boolean added = Variable.addVariable(varName);
 added = Variable.addVariable(varName);
 assertFalse(added);
 assertEquals(1, Variable.variableList.size());
 }

 @Test
 public void testRemoveVariable() {
 Variable.variableList.add(new Variable("testVar1"));
 Variable.variableList.add(new Variable("testVar2"));
 Variable.removeVariable("testVar1");
 assertEquals(1, Variable.variableList.size());
 assertNull(Variable.findVariable("testVar1"));
 }

 @Test
 public void testGetVariableValue() {
 Variable.variableList.add(new Variable("testVar1"));
 Variable.variableList.get(0).setValue(10);
 assertEquals(10, Variable.getVariableValue("testVar1"));
 }

 @Test
 public void testGetVariableValueWithInvalidName() {
 assertEquals(0, Variable.getVariableValue("invalidVar"));
 }

 @Test
 public void testGetVariableValueWithReferenceName() {
 Variable.variableList.add(new Variable("testVar1"));
 Variable.variableList.get(0).setValue(10);
 assertEquals(10, Variable.getVariableValue("&testVar1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueWithInvalidName() {
 Variable.setVariableValue("invalidVar", 10);
 }

 @Test
 public void testSetVariableValue() {
 Variable.variableList.add(new Variable("testVar1"));
 Variable.setVariableValue("testVar1", 10);
 assertEquals(10, Variable.getVariableValue("testVar1"));
 }

 @Test
 public void testFindVariable() {
 Variable.variableList.add(new Variable("testVar1"));
 Variable.variableList.add(new Variable("testVar2"));
 int index = Variable.findVariable("testVar1");
 assertEquals(0, index);
 index = Variable.findVariable("testVar2");
 assertEquals(1, index);
 }

 @Test
 public void testFindVariableWithInvalidName() {
 int index = Variable.findVariable("invalidVar");
 assertEquals(-1, index);
 }

 @Test
 public void testFindVariableWithReferenceName() {
 Variable.variableList.add(new Variable("testVar1"));
 Variable.variableList.add(new Variable("testVar2"));
 int index = Variable.findVariable("&testVar1");
 assertEquals(0, index);
 }
}