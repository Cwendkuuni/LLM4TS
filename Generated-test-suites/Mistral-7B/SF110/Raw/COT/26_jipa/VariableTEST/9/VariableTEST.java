import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class VariableTest {

 private List<Variable> variableList;

 @BeforeClass
 public static void setUp() {
 Variable.variableList = new ArrayList<>();
 VariableTest.addVariable("var1");
 VariableTest.addVariable("var2");
 }

 @Test
 public void testAddVariable() {
 String varName = "var3";
 Assert.assertTrue(Variable.addVariable(varName));
 Assert.assertEquals(3, Variable.variableList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddVariableWithExistingName() {
 String varName = "var1";
 Assert.assertFalse(Variable.addVariable(varName));
 }

 @Test
 public void testGetVariableValue() {
 String varName = "var1";
 int expectedValue = 0;
 Assert.assertEquals(expectedValue, Variable.getVariableValue(varName));

 VariableTest.setVariableValue(varName, 10);
 expectedValue = 10;
 Assert.assertEquals(expectedValue, Variable.getVariableValue(varName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetVariableValueWithNonExistentName() {
 String varName = "nonExistentVar";
 Assert.assertEquals(0, Variable.getVariableValue(varName));
 }

 @Test
 public void testSetVariableValue() {
 String varName = "var1";
 int newValue = 20;
 VariableTest.setVariableValue(varName, newValue);
 Assert.assertEquals(newValue, Variable.getVariableValue(varName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueWithNonExistentName() {
 String varName = "nonExistentVar";
 int newValue = 10;
 VariableTest.setVariableValue(varName, newValue);
 }

 @Test
 public void testFindVariable() {
 String varName = "var1";
 int expectedIndex = 0;
 Assert.assertEquals(expectedIndex, Variable.findVariable(varName));
 }

 @Test
 public void testFindVariableWithNonExistentName() {
 String varName = "nonExistentVar";
 int expectedIndex = -1;
 Assert.assertEquals(expectedIndex, Variable.findVariable(varName));
 }

 @Test
 public void testRemoveVariable() {
 String varName = "var2";
 VariableTest.removeVariable(varName);
 Assert.assertEquals(1, Variable.variableList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveVariableWithNonExistentName() {
 String varName = "nonExistentVar";
 VariableTest.removeVariable(varName);
 }

 @Test
 public void testGetVariableValueWithStarPrefix() {
 String varName = "*var1";
 int expectedValue = 0;
 Assert.assertEquals(expectedValue, Variable.getVariableValue(varName));

 VariableTest.setVariableValue(varName.substring(1), 10);
 expectedValue = 10;
 Assert.assertEquals(expectedValue, Variable.getVariableValue(varName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetVariableValueWithStarPrefixAndNonExistentName() {
 String varName = "*nonExistentVar";
 Assert.assertEquals(0, Variable.getVariableValue(varName));
 }

 @Test
 public void testSetVariableValueWithStarPrefix() {
 String varName = "*var1";
 int newValue = 20;
 VariableTest.setVariableValue(varName.substring(1), newValue);
 Assert.assertEquals(newValue, Variable.getVariableValue(varName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueWithStarPrefixAndNonExistentName() {
 String varName = "*nonExistentVar";
 int newValue = 10;
 VariableTest.setVariableValue(varName.substring(1), newValue);
 }
}