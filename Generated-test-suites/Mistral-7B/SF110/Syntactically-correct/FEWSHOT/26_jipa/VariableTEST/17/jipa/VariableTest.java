package jipa;

import static org.junit.Assert.*;
import java.util.*;

public class VariableTest {

 private List<Variable> variableList;

 @BeforeClass
 static void setupClass() {
 Variable.variableList = new ArrayList<>();
 Variable.addVariable("var1");
 Variable.addVariable("var2");
 Variable.addVariable("var3");
 }

 @AfterClass
 static void tearDownClass() {
 Variable.variableList.clear();
 }

 @Before
 public void setup() {
 variableList = new ArrayList<>(Variable.variableList);
 }

 @After
 public void tearDown() {
 variableList.clear();
 }

 @Test
 public void testAddVariable() {
 assertEquals(3, Variable.variableList.size());
 assertTrue(Variable.addVariable("var4"));
 assertEquals(4, Variable.variableList.size());
 assertFalse(Variable.addVariable("var1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddVariableNullName() {
 Variable.addVariable(null);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testFindVariable() {
 assertEquals(0, Variable.findVariable("var1"));
 assertEquals(3, Variable.findVariable("var4"));
 assertEquals(-1, Variable.findVariable("nonExistentVar"));
 }

 @Test
 public void testFindVariableWithReference() {
 assertEquals(2, Variable.findVariable("&var2"));
 assertEquals(2, Variable.variableList.get(1).getValue());
 }

 @Test
 public void testGetVariableValue() {
 assertEquals(0, Variable.getVariableValue("var1"));
 assertEquals(0, Variable.getVariableValue("nonExistentVar"));
 assertEquals(1, Variable.getVariableValue("var2"));
 }

 @Test
 public void testRemoveVariable() {
 Variable.removeVariable("var3");
 assertEquals(2, Variable.variableList.size());
 assertFalse(Variable.findVariable("var3") != -1);
 }

 @Test
 public void testSetVariableValue() {
 Variable.setVariableValue("var2", 10);
 assertEquals(10, Variable.getVariableValue("var2"));
 }

 @Test
 public void testSetVariableValueWithNonExistentVariable() {
 Variable.setVariableValue("nonExistentVar", 5);
 assertEquals(0, Variable.getVariableValue("nonExistentVar"));
 }
}