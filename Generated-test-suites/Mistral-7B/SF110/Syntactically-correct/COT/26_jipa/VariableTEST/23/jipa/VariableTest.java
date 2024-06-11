package jipa.test;

import jipa.Variable;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class VariableTest {

 private List<Variable> variableList;

 @BeforeClass
 public static void setUpClass() {
 Variable.variableList = new ArrayList<>();
 }

 @Test
 public void testAddVariable() {
 Variable variable1 = new Variable("testVar1");
 assertTrue(Variable.addVariable("testVar1"));
 assertEquals(1, Variable.variableList.size());
 assertSame(variable1, Variable.variableList.get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddVariableWithExistingName() {
 Variable variable1 = new Variable("testVar1");
 Variable.addVariable("testVar1");
 assertFalse(Variable.addVariable("testVar1"));
 assertEquals(1, Variable.variableList.size());
 }

 @Test
 public void testRemoveVariable() {
 Variable variable1 = new Variable("testVar1");
 Variable.addVariable("testVar1");
 Variable.removeVariable("testVar1");
 assertNull(Variable.findVariable("testVar1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueWithNullVariableList() {
 Variable.variableList = null;
 Variable.setVariableValue("testVar1", 10);
 assertNull(Variable.findVariable("testVar1"));
 }

 @Test
 public void testGetVariableValue() {
 Variable variable1 = new Variable("testVar1");
 Variable.addVariable("testVar1");
 Variable.setVariableValue("testVar1", 10);
 assertEquals(10, Variable.getVariableValue("testVar1"));
 }

 @Test
 public void testGetVariableValueWithNonExistentVariable() {
 assertEquals(0, Variable.getVariableValue("nonExistentVariable"));
 }

 @Test
 public void testSetVariableValue() {
 Variable variable1 = new Variable("testVar1");
 Variable.addVariable("testVar1");
 Variable.setVariableValue("testVar1", 10);
 assertEquals(10, Variable.getVariableValue("testVar1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueWithNullVariableName() {
 Variable.setVariableValue(null, 10);
 assertNull(Variable.findVariable("testVar1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueWithNonExistentVariable() {
 Variable.setVariableValue("nonExistentVariable", 10);
 assertNull(Variable.findVariable("nonExistentVariable"));
 }

 @Test
 public void testFindVariable() {
 Variable variable1 = new Variable("testVar1");
 Variable.addVariable("testVar1");
 assertEquals(variable1, Variable.findVariable("testVar1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindVariableWithNullVariableName() {
 Variable.findVariable(null);
 assertNull(Variable.findVariable("testVar1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindVariableWithNonExistentVariable() {
 Variable.findVariable("nonExistentVariable");
 assertNull(Variable.findVariable("testVar1"));
 }
}