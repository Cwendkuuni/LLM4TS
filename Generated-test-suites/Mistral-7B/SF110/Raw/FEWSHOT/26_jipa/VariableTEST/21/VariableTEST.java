package jipa;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class VariableTest {

 // Test initialize and get/set value of Variable
 @Test
 public void testVariableInitialization() {
 Variable variable = new Variable("testVar");
 assertEquals("testVar", variable.getName());
 assertEquals(0, variable.getValue());

 variable.setValue(42);
 assertEquals(42, variable.getValue());
 }

 // Test adding variable to list
 @Test
 public void testAddVariable() {
 Variable variable1 = new Variable("var1");
 Variable variable2 = new Variable("var2");
 Variable.variableList = new ArrayList<>();

 Variable.addVariable("var1");
 Variable.addVariable("var2");

 assertTrue(Variable.variableList.size() == 2);
 assertEquals("var1", Variable.variableList.get(0).getName());
 assertEquals("var2", Variable.variableList.get(1).getName());
 }

 // Test adding duplicate variable
 @Test
 public void testAddDuplicateVariable() {
 Variable variable = new Variable("testVar");
 Variable.variableList = new ArrayList<>();

 Variable.addVariable("testVar");
 Variable.addVariable("testVar");

 assertEquals(1, Variable.variableList.size());
 }

 // Test findVariable method with existing variable name
 @Test
 public void testFindVariableExistingName() {
 Variable variable1 = new Variable("var1");
 Variable variable2 = new Variable("var2");
 Variable.variableList = new ArrayList<>();

 Variable.addVariable("var1");
 Variable.addVariable("var2");

 assertEquals(1, Variable.findVariable("var1"));
 }

 // Test findVariable method with non-existing variable name
 @Test
 public void testFindVariableNonExistingName() {
 Variable.variableList = new ArrayList<>();

 assertEquals(-1, Variable.findVariable("testVar"));
 }

 // Test findVariable method with reference variable
 @Test
 public void testFindVariableReference() {
 Variable variable = new Variable("&testVar");
 Variable.variableList = new ArrayList<>();
 Variable testVar = new Variable("testVar");
 Variable.addVariable("testVar");

 assertEquals(testVar.getValue(), Variable.findVariable("&testVar"));
 }

 // Test getVariableValue method with existing variable
 @Test
 public void testGetVariableValueExisting() {
 Variable variable = new Variable("testVar");
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("testVar");
 variable.setValue(42);

 assertEquals(42, Variable.getVariableValue("testVar"));
 }

 // Test getVariableValue method with non-existing variable
 @Test
 public void testGetVariableValueNonExisting() {
 Variable.variableList = new ArrayList<>();

 assertEquals(0, Variable.getVariableValue("testVar"));
 }

 // Test getVariableValue method with reference variable
 @Test
 public void testGetVariableValueReference() {
 Variable variable = new Variable("&testVar");
 Variable testVar = new Variable("testVar");
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("testVar");
 testVar.setValue(42);

 assertEquals(42, Variable.getVariableValue("&testVar"));
 }

 // Test setVariableValue method with existing variable
 @Test
 public void testSetVariableValueExisting() {
 Variable variable = new Variable("testVar");
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("testVar");
 variable.setValue(42);

 Variable.setVariableValue("testVar", 43);
 assertEquals(43, variable.getValue());
 }

 // Test setVariableValue method with non-existing variable
 @Test
 public void testSetVariableValueNonExisting() {
 Variable.variableList = new ArrayList<>();

 Variable.setVariableValue("testVar", 42);
 assertEquals(0, Variable.variableList.size());
 }

 // Test setVariableValue method with reference variable
 @Test
 public void testSetVariableValueReference() {
 Variable variable = new Variable("&testVar");
 Variable testVar = new Variable("testVar");
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("testVar");
 testVar.setValue(42);

 Variable.setVariableValue("&testVar", 43);
 assertEquals(43, testVar.getValue());
 }

 // Test removeVariable method with existing variable
 @Test
 public void testRemoveVariableExisting() {
 Variable variable = new Variable("testVar");
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("testVar");

 Variable.removeVariable("testVar");assertEquals(0, Variable.variableList.size()); }

 // Test removeVariable method with non-existing variable
 @Test public void testRemoveVariableNonExisting() { Variable.variableList = new ArrayList<>();

 Variable.removeVariable("testVar"); assertEquals(0, Variable.variableList.size()); }
}