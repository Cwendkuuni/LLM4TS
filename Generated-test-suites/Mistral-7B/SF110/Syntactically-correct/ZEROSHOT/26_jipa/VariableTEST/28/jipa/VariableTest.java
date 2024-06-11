package jipa.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class VariableTest {

 private static List<Variable> variableList;

 @Before
 public void setUp() {
 Variable.variableList = new ArrayList<>();
 }

 @Test
 public void testAddVariable() {
 String varName = "testVar1";
 assertTrue(Variable.addVariable(varName));
 assertEquals(1, Variable.variableList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddVariableWithInvalidName() {
 String varName = "";
 assertFalse(Variable.addVariable(varName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddVariableWithExistingName() {
 String varName = "testVar1";
 Variable.addVariable(varName);
 assertFalse(Variable.addVariable(varName));
 }

 @Test
 public void testRemoveVariable() {
 String varName = "testVar1";
 Variable.addVariable(varName);
 Variable.removeVariable(varName);
 assertFalse(Variable.findVariable(varName) != -1);
 }

 @Test
 public void testGetVariableValue() {
 String varName = "testVar1";
 Variable.addVariable(varName);
 Variable.setVariableValue(varName, 5);
 assertEquals(5, Variable.getVariableValue(varName));
 }

 @Test
 public void testGetVariableValueWithInvalidName() {
 String varName = "";
 assertEquals(0, Variable.getVariableValue(varName));
 }

 @Test
 public void testGetVariableValueWithNonExistentName() {
 String varName = "nonExistentVar";
 assertEquals(0, Variable.getVariableValue(varName));
 }

 @Test
 public void testSetVariableValue() {
 String varName = "testVar1";
 Variable.addVariable(varName);
 Variable.setVariableValue(varName, 5);
 assertEquals(5, Variable.getVariableValue(varName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueWithInvalidName() {
 String varName = "";
 Variable.setVariableValue(varName, 5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueWithNonExistentName() {
 String varName = "nonExistentVar";
 Variable.setVariableValue(varName, 5);
 }

 @Test
 public void testFindVariable() {
 String varName = "testVar1";
 Variable.addVariable(varName);
 Variable.setVariableValue(varName, 5);
 assertEquals(Variable.findVariable(varName), 0);
 }

 @Test
 public void testFindVariableWithNonExistentName() {
 String varName = "nonExistentVar";
 assertEquals(-1, Variable.findVariable(varName));
 }

 @Test
 public void testFindVariableWithReferenceName() {
 String varName = "*testVar1";
 assertEquals(5, Variable.getVariableValue(varName));
 }

 @Test
 public void testFindVariableWithReferenceNameAndNonExistentName() {
 String varName = "&nonExistentVar";
 assertNull(Variable.findVariable(varName));
 }

 @After
 public void tearDown() {
 Variable.variableList.clear();
 }

}