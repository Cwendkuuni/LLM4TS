package jipa;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class VariableTest {

 @Test
 public void testAddVariableAndGetVariableValue() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("var1");
 Variable var1 = Variable.variableList.get(0);
 assertEquals("var1", var1.getName());
 assertEquals(0, var1.getValue());
 assertEquals(0, Variable.getVariableValue("var1"));
 }

 @Test
 public void testAddVariableWithSameNameFails() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("var1");
 assertFalse(Variable.addVariable("var1"));
 }

 @Test
 public void testRemoveVariable() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("var1");
 Variable var1 = Variable.variableList.get(0);
 Variable.removeVariable("var1");
 assertTrue(Variable.variableList.isEmpty());
 assertNull(Variable.findVariable("var1"));
 }

 @Test
 public void testGetVariableValueWithNominalVariableName() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("var1");
 Variable.setVariableValue("var1", 10);
 assertEquals(10, Variable.getVariableValue("var1"));
 }

 @Test
 public void testGetVariableValueWithReferenceVariableName() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("var1");
 Variable var1 = Variable.variableList.get(0);
 Variable.addVariable("&var1");
 Variable refVar1 = Variable.findVariable("&var1");
 Variable.setVariableValue("&var1", 20);
 assertEquals(20, Variable.getVariableValue("var1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetVariableValueWithInvalidVariableName() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("var1");
 assertNull(Variable.getVariableValue(""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueWithInvalidVariableName() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("var1");
 Variable.setVariableValue("", 10);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetVariableValueWithInvalidIndex() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("var1");
 Variable.addVariable("var2");
 Variable.setVariableValue("var1", 10);
 assertEquals(10, Variable.getVariableValue("var1"));
 assertNull(Variable.getVariableValue(1));
 }

 @Test
 public void testFindVariableWithNominalVariableName() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("var1");
 Variable.addVariable("var2");
 Variable var1 = Variable.findVariable("var1");
 assertEquals("var1", var1.getName());
 }

 @Test
 public void testFindVariableWithReferenceVariableName() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("var1");
 Variable.addVariable("&var1");
 Variable refVar1 = Variable.findVariable("&var1");
 assertEquals("var1", refVar1.getName());
 }

 @Test
 public void testFindVariableWithInvalidVariableName() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("var1");
 assertNull(Variable.findVariable(""));
 }

 @Test
 public void testFindVariableWithMissingVariable() {
 Variable.variableList = new ArrayList<>();
 assertNull(Variable.findVariable("var1"));
 }
}