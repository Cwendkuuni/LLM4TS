package jipa;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Test;

public class VariableTest {

 private static Variable variable1;
 private static Variable variable2;
 private static Variable variable3;
 private static List<Variable> variableList = new ArrayList<>();

 @BeforeClass
 public static void setupClass() {
 Variable.variableList = new ArrayList<>();
 variable1 = new Variable("var1");
 variable2 = new Variable("var2");
 variable3 = new Variable("var3");
 Variable.variableList.add(variable1);
 Variable.variableList.add(variable2);
 }

 @AfterClass
 public static void teardownClass() {
 Variable.variableList.clear();
 }

 @Before
 public void setup() {
 Variable.variableList = new ArrayList<>(Variable.variableList);
 }

 @After
 public void teardown() {
 Variable.variableList = Variable.variableList; // prevent modification of the shared list
 }

 @Test
 public void testAddVariable() {
 assertEquals(2, Variable.variableList.size());
 assertTrue(Variable.addVariable("var4"));
 assertEquals(3, Variable.variableList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddVariableWithExistingName() {
 assertFalse(Variable.addVariable("var1"));
 }

 @Test
 public void testRemoveVariable() {
 assertEquals(3, Variable.variableList.size());
 Variable.removeVariable("var2");
 assertEquals(2, Variable.variableList.size());
 Variable.removeVariable("var4"); // should not affect the list as var4 was not added
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveVariableWithNonExistingName() {
 Variable.removeVariable("var5");
 }

 @Test
 public void testGetVariableValue() {
 Variable.setVariableValue("var1", 10);
 Variable.setVariableValue("var2", 20);
 assertEquals(10, Variable.getVariableValue("var1"));
 assertEquals(20, Variable.getVariableValue("var2"));
 assertEquals(0, Variable.getVariableValue("var3"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetVariableValueWithNonExistingName() {
 assertEquals(0, Variable.getVariableValue("var4"));
 }

 @Test
 public void testSetVariableValue() {
 Variable.setVariableValue("var1", 100);
 assertEquals(100, Variable.getVariableValue("var1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueWithNonExistingName() {
 Variable.setVariableValue("var4", 40);
 }

 @Test
 public void testFindVariable() {
 assertEquals(0, Variable.findVariable("var1"));
 assertEquals(1, Variable.findVariable("var2"));
 assertEquals(-1, Variable.findVariable("var3"));
 assertEquals(10, Variable.findVariable("var1_ref")); // tests passing a reference
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindVariableWithNonExistingName() {
 assertEquals(-1, Variable.findVariable("var4"));
 }
}