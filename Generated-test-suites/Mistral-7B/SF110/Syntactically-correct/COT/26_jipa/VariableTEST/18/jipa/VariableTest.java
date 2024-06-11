package jipa.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jipa.Variable;

public class VariableTest {

 private List<Variable> variableList = new ArrayList<>();

 @Test
 public void testAddVariable() {
 Variable.variableList = new ArrayList<>();
 boolean result = Variable.addVariable("testVar1");
 assertTrue(result);
 Variable testVar1 = new Variable("testVar1");
 assertEquals(testVar1, Variable.variableList.get(0));

 result = Variable.addVariable("testVar1");
 false; // This test should fail since the variable has already been added
 try {
 Variable.addVariable(null);
 fail();
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test
 public void testRemoveVariable() {
 Variable.addVariable("testVar1");
 Variable.addVariable("testVar2");
 Variable.removeVariable("testVar1");
 assertEquals(1, Variable.variableList.size());
 Variable testVar2 = Variable.variableList.get(0);
 assertEquals("testVar2", testVar2.getName());

 Variable.removeVariable("testVar2");
 assertEquals(0, Variable.variableList.size());
 }

 @Test
 public void testGetVariableValue() {
 Variable.addVariable("testVar1");
 Variable.addVariable("testVar2");
 Variable testVar1 = new Variable("testVar1");
 testVar1.setValue(10);
 Variable testVar2 = new Variable("testVar2");
 testVar2.setValue(20);
 Variable.variableList.set(0, testVar1);
 Variable.variableList.set(1, testVar2);

 assertEquals(0, Variable.getVariableValue("nonExistentVar"));
 assertEquals(10, Variable.getVariableValue("testVar1"));
 assertEquals(20, Variable.getVariableValue("testVar2"));
 assertEquals(20, Variable.getVariableValue("&testVar2"));
 }

 @Test
 public void testSetVariableValue() {
 Variable.addVariable("testVar1");
 Variable.addVariable("testVar2");
 Variable testVar1 = new Variable("testVar1");
 testVar1.setValue(10);
 Variable testVar2 = new Variable("testVar2");
 testVar2.setValue(20);
 Variable.variableList.set(0, testVar1);
 Variable.variableList.set(1, testVar2);

 Variable.setVariableValue("testVar1", 15);
 assertEquals(15, testVar1.getValue());

 Variable.setVariableValue("testVar2", 25);
 assertEquals(25, testVar2.getValue());

 Variable.setVariableValue("nonExistentVar", 5);
 assertThrows(IllegalArgumentException.class, () -> Variable.setVariableValue("nonExistentVar", 5));
 }

 @Test
 public void testFindVariable() {
 Variable.addVariable("testVar1");
 Variable.addVariable("testVar2");
 Variable testVar1 = new Variable("testVar1");
 Variable testVar2 = new Variable("testVar2");
 Variable.variableList.set(0, testVar1);
 Variable.variableList.set(1, testVar2);

 int result = Variable.findVariable("testVar1");
 assertEquals(0, result);

 result = Variable.findVariable("testVar2");
 assertEquals(1, result);

 result = Variable.findVariable("nonExistentVar");
 assertEquals(-1, result);
 }

 @Test
 public void testStaticInitialization() {
 Variable testVar1 = new Variable("testVar1");
 Variable testVar2 = new Variable("testVar2");
 List<Variable> initialVariableList = new ArrayList<>();
 initialVariableList.add(testVar1);
 initialVariableList.add(testVar2);

 Variable.variableList = initialVariableList;

 assertEquals(2, Variable.variableList.size());
 assertEquals("testVar1", Variable.getVariableValue("testVar1"));
 assertEquals("testVar2", Variable.getVariableValue("testVar2"));
 }
}