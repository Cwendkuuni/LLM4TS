package jipa.test;

import jipa.Variable;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class VariableTest {

 private List<Variable> variableList = new ArrayList<>();

 @Test
 public void testAddVariable() {
 Variable variable = new Variable("testVar");
 Assert.assertTrue(Variable.addVariable("testVar"));
 Assert.assertEquals(1, Variable.variableList.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddVariableWithExistingName() {
 Variable.addVariable("testVar");
 Assert.assertFalse(Variable.addVariable("testVar"));
 }

 @Test
 public void testRemoveVariable() {
 Variable variable = new Variable("testVar");
 Variable.addVariable("testVar");
 Variable.removeVariable("testVar");
 Assert.assertEquals(0, Variable.variableList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetVariableValueWithInvalidName() {
 Assert.assertEquals(0, Variable.getVariableValue(null));
 }

 @Test
 public void testGetVariableValueWithValidName() {
 Variable variable = new Variable("testVar");
 Variable.addVariable("testVar");
 Variable.setVariableValue("testVar", 5);
 Assert.assertEquals(5, Variable.getVariableValue("testVar"));
 }

 @Test
 public void testSetVariableValue() {
 Variable variable = new Variable("testVar");
 Variable.addVariable("testVar");
 Variable.setVariableValue("testVar", 5);
 Assert.assertEquals(5, Variable.getVariableValue("testVar"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueWithInvalidName() {
 Variable.setVariableValue(null, 5);
 }

 @Test
 public void testFindVariable() {
 Variable variable1 = new Variable("testVar1");
 Variable variable2 = new Variable("testVar2");
 Variable.addVariable("testVar1");
 Variable.addVariable("testVar2");
 Assert.assertEquals(0, Variable.findVariable("testVar3"));
 Assert.assertEquals(1, Variable.findVariable("testVar1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindVariableWithNullName() {
 Assert.assertEquals(-1, Variable.findVariable(null));
 }

 @Test
 public void testFindVariableWithReferenceName() {
 Variable variable = new Variable("testVar");
 Variable.addVariable("testVar");
 Variable variableRef = new Variable("&testVar");
 Assert.assertEquals(Variable.variableList.get(0).getValue(), Variable.findVariable("&testVar"));
 }
}