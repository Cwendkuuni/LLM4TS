package jipa;

import static org.junit.Assert.*;
import java.util.*;

public class VariableTest {

 private List<Variable> variableList;

 @BeforeClass
 static public void setupClass() {
 Variable.variableList = new ArrayList<>();
 }

 @Test
 public void testAddVariable() {
 Variable var1 = new Variable("var1");
 assertTrue(Variable.addVariable("var1"));
 assertEquals("var1", Variable.variableList.get(0).getName());

 Variable var2 = new Variable("var2");
 assertTrue(Variable.addVariable("var2"));
 assertEquals("var2", Variable.variableList.get(1).getName());

 assertFalse(Variable.addVariable("var1"));
 assertEquals(2, Variable.variableList.size());
 }

 @Test
 public void testRemoveVariable() {
 Variable var1 = new Variable("var1");
 Variable var2 = new Variable("var2");
 Variable var3 = new Variable("var3");
 Variable.variableList.add(var1);
 Variable.variableList.add(var2);
 Variable.variableList.add(var3);

 Variable.removeVariable("var2");
 assertEquals(2, Variable.variableList.size());
 assertFalse(Variable.variableList.contains(var2));

 Variable.removeVariable("var1");
 assertEquals(1, Variable.variableList.size());
 assertFalse(Variable.variableList.contains(var1));
 }

 @Test
 public void testGetVariableValue() {
 Variable var1 = new Variable("var1");
 Variable var2 = new Variable("var2");
 Variable var3 = new Variable("var3");
 Variable.variableList.add(var1);
 Variable.variableList.add(var2);
 Variable.variableList.add(var3);

 assertEquals(0, Variable.getVariableValue("var4"));
 assertEquals(0, Variable.getVariableValue("var1"));
 assertEquals(0, Variable.getVariableValue("var2"));
 assertEquals(0, Variable.getVariableValue("var3"));

 var1.setValue(1);
 assertEquals(1, Variable.getVariableValue("var1"));

 var2.setValue(2);
 assertEquals(2, Variable.getVariableValue("var2"));
 }

 @Test
 public void testSetVariableValue() {
 Variable var1 = new Variable("var1");
 Variable var2 = new Variable("var2");
 Variable var3 = new Variable("var3");
 Variable.variableList.add(var1);
 Variable.variableList.add(var2);
 Variable.variableList.add(var3);

 Variable.setVariableValue("var1", 1);
 assertEquals(1, Variable.getVariableValue("var1"));

 Variable.setVariableValue("var2", 2);
 assertEquals(2, Variable.getVariableValue("var2"));
 }

 @Test
 public void testFindVariable() {
 Variable var1 = new Variable("var1");
 Variable var2 = new Variable("var2");
 Variable var3 = new Variable("var3");
 Variable.variableList.add(var1);
 Variable.variableList.add(var2);
 Variable.variableList.add(var3);

 assertEquals(1, Variable.findVariable("var1"));
 assertEquals(2, Variable.findVariable("var2"));
 assertEquals(-1, Variable.findVariable("var4"));
 }

 @Test
 public void testFindVariableWithReference() {
 Variable var1 = new Variable("var1");
 Variable refVar1 = new Variable("&var1");
 Variable var2 = new Variable("var2");
 Variable.variableList.add(var1);
 Variable.variableList.add(refVar1);
 Variable.variableList.add(var2);

 assertEquals(0, Variable.findVariable("var1"));
 assertEquals(0, Variable.findVariable("&var1"));
 assertEquals(1, Variable.findVariable("var2"));
 }
}