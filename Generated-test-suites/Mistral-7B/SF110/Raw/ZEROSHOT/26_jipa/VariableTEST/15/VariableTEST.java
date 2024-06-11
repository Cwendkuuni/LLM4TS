import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class VariableTest {

 private static Variable variable1;
 private static Variable variable2;
 private static List<Variable> variableList;

 @Before
 public void setUp() {
 variableList = new ArrayList<Variable>();
 variable1 = new Variable("var1");
 variable2 = new Variable("var2");
 Variable.variableList = variableList;
 }

 @Test
 public void testAddVariable() {
 assertTrue(Variable.addVariable("var3"));
 assertEquals(3, Variable.variableList.size());
 assertFalse(Variable.addVariable("var1"));
 }

 @Test
 public void testRemoveVariable() {
 Variable.addVariable("var4");
 assertEquals(4, Variable.variableList.size());
 Variable.removeVariable("var4");
 assertEquals(3, Variable.variableList.size());
 }

 @Test
 public void testGetVariableValue() {
 Variable.addVariable("var5");
 Variable.setVariableValue("var5", 5);
 assertEquals(5, Variable.getVariableValue("var5"));
 }

 @Test(expected = AssertionError.class)
 public void testGetVariableValueInvalidName() {
 Variable.getVariableValue("invalidName");
 }

 @Test
 public void testGetVariableValueReference() {
 Variable.addVariable("&var6");
 Variable.setVariableValue("var6", 6);
 assertEquals(6, Variable.getVariableValue("var6"));
 }

 @Test(expected = AssertionError.class)
 public void testGetVariableValueReferenceInvalidName() {
 Variable.getVariableValue("&invalidName");
 }

 @Test
 public void testSetVariableValue() {
 Variable.addVariable("var7");
 Variable.setVariableValue("var7", 7);
 assertEquals(7, Variable.getVariableValue("var7"));
 }

 @Test(expected = AssertionError.class)
 public void testSetVariableValueInvalidName() {
 Variable.setVariableValue("invalidName", 1);
 }

 @Test
 public void testFindVariable() {
 Variable.addVariable("var8");
 Variable.addVariable("var9");
 assertEquals(2, Variable.findVariable("var9"));
 }

 @Test
 public void testFindVariableNotExist() {
 assertEquals(-1, Variable.findVariable("invalidName"));
 }
}