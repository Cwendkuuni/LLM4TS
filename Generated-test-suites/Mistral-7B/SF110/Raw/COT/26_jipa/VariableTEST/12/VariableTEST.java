package jipa.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jipa.Variable;

public class VariableTest {

 private List<Variable> variableList;

 @BeforeClass
 public static void setupClass() {
 Variable.variableList = new ArrayList<>();
 }

 @Test
 public void testAddVariable() {
 String varName = "testVar";
 assertTrue(Variable.addVariable(varName));
 assertEquals(1, Variable.variableList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddVariableWithExistsVarName() {
 String varName = "testVar";
 Variable.addVariable(varName); // add it first to make test fail
 assertFalse(Variable.addVariable(varName));
 }

 @Test
 public void testGetVariableValue() {
 String varName = "testVar";
 Variable.addVariable(varName);
 Variable.setVariableValue(varName, 10);
 assertEquals(10, Variable.getVariableValue(varName));
 }

 @Test
 public void testGetVariableValueWithNotExistVarName() {
 assertEquals(0, Variable.getVariableValue("notExistVar"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueWithNotExistVarName() {
 String varName = "notExistVar";
 Variable.setVariableValue(varName, 10);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueWithNegativeNumber() {
 String varName = "testVar";
 Variable.addVariable(varName);
 Variable.setVariableValue(varName, -5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindVariableWithNullName() {
 assertEquals(-1, Variable.findVariable(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindVariableWithEmptyName() {
 assertEquals(-1, Variable.findVariable(""));
 }

 @Test
 public void testFindVariable() {
 Variable var1 = new Variable("var1");
 Variable var2 = new Variable("var2");
 Variable.variableList.add(var1);
 Variable.variableList.add(var2);
 assertEquals(1, Variable.findVariable("var1"));
 assertEquals(1, Variable.findVariable("&var1")); // test reference
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testFindVariableWithOutOfBoundIndex() {
 Variable.variableList.add(new Variable("var1"));
 Variable.variableList.add(new Variable("var2"));
 Variable.variableList.add(new Variable("var3"));
 assertEquals(-1, Variable.findVariable("notExistVar"));
 assertEquals(3, Variable.findVariable("var3"));
 Variable.variableList.remove(2);
 assertEquals(-1, Variable.findVariable("var3"));
 }

 @AfterClass
 public static void teardownClass() {
 Variable.variableList.clear();
 }

}