import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import static jipa.Variable.*;

public class VariableTest {

 private static Variable testVariable;

 @Test
 public void testConstructor() {
 testVariable = new Variable("testVariable");
 Assert.assertEquals("testVariable", testVariable.getName());
 Assert.assertEquals(0, testVariable.getValue());
 }

 @Test
 public void testGetValue() {
 testVariable.setValue(10);
 Assert.assertEquals(10, testVariable.getValue());
 }

 @Test
 public void testSetValue() {
 testVariable.setValue(20);
 Assert.assertEquals(20, testVariable.getValue());
 }

 @Test
 public void testGetName() {
 Assert.assertEquals("testVariable", testVariable.getName());
 }

 @Test
 public void testAddVariable_NewVariable() {
 Variable.addVariable("newVariable");
 Variable newVariable = Variable.variableList.get(Variable.variableList.size() - 1);
 Assert.assertEquals("newVariable", newVariable.getName());
 Assert.assertEquals(0, newVariable.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddVariable_ExistingVariableThrowsException() {
 Variable.addVariable("testVariable");
 }

 @Test
 public void testRemoveVariable_ExistingVariable() {
 Variable.addVariable("removeVariable");
 Variable.removeVariable("removeVariable");
 Assert.assertEquals(0, Variable.variableList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveVariable_NonExistingVariableThrowsException() {
 Variable.removeVariable("nonExistingVariable");
 }

 @Test
 public void testGetVariableValue_ExistingVariable() {
 Variable.addVariable("getVariableValue");
 testVariable = Variable.variableList.get(0);
 testVariable.setValue(5);
 Assert.assertEquals(5, Variable.getVariableValue("getVariableValue"));
 }

 @Test
 public void testGetVariableValue_NonExistingVariable() {
 Assert.assertEquals(0, Variable.getVariableValue("nonExistingVariable"));
 }

 @Test
 public void testSetVariableValue_ExistingVariable() {
 Variable.addVariable("setVariableValue");
 testVariable = Variable.variableList.get(0);
 testVariable.setValue(15);
 Variable.setVariableValue("setVariableValue", 20);
 Assert.assertEquals(20, testVariable.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValue_NonExistingVariableThrowsException() {
 Variable.setVariableValue("nonExistingVariable", 10);
 }

 @Test
 public void testFindVariable_ExistingVariable() {
 Variable.addVariable("findVariable");
 testVariable = Variable.variableList.get(0);
 Assert.assertEquals(0, Variable.findVariable("findVariable"));
 }

 @Test
 public void testFindVariable_NonExistingVariable() {
 Assert.assertEquals(-1, Variable.findVariable("nonExistingVariable"));
 }

 @Test
 public void testFindVariable_ReferenceVariable() {
 Variable.addVariable("refVariable");
 testVariable = Variable.variableList.get(0);
 testVariable.setValue(10);
 Assert.assertEquals(10, Variable.findVariable("refVariable"));
 }

 @Test
 public void testFindVariable_ReferenceVariableWithSameName() {
 Variable.addVariable("refVariable");
 Variable refVariable = new Variable("refVariable");
 Variable.variableList.add(refVariable);
 Assert.assertEquals(refVariable.getValue(), Variable.findVariable("refVariable"));
 }
}