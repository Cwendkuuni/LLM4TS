import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class VariableTest {

 private List<Variable> variableList;

 @Before
 public void setUp() {
 Variable.variableList = new ArrayList<>();
 }

 @Test
 public void testAddVariableAndGetVariableValue() {
 String varName = "var1";
 Variable variable = new Variable(varName);
 assertTrue(Variable.addVariable(varName));
 assertEquals(0, Variable.getVariableValue(varName));
 }

 @Test
 public void testAddDuplicateVariable() {
 String varName = "var1";
 Variable.addVariable(varName);
 assertFalse(Variable.addVariable(varName));
 }

 @Test
 public void testRemoveVariable() {
 String varName = "var1";
 Variable.addVariable(varName);
 Variable.removeVariable(varName);
 assertNull(Variable.findVariable(varName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullVariableName() {
 Variable.addVariable(null);
 }

 @Test
 public void testGetVariableValueWithReference() {
 String varName = "*var1";
 Variable.addVariable("var1");
 assertEquals(0, Variable.getVariableValue(varName));
 Variable.setVariableValue("var1", 10);
 assertEquals(10, Variable.getVariableValue(varName));
 }

 @Test
 public void testFindVariable() {
 String varName1 = "var1";
 String varName2 = "var2";
 Variable.addVariable(varName1);
 Variable.addVariable(varName2);
 assertEquals(0, Variable.findVariable(varName1));
 assertNull(Variable.findVariable("notExist"));
 }

 @Test
 public void testSetVariableValue() {
 String varName = "var1";
 Variable.addVariable(varName);
 Variable.setVariableValue(varName, 10);
 assertEquals(10, Variable.getVariableValue(varName));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueNullVariableName() {
 String varName = null;
 Variable.addVariable("var1");
 try {
 Variable.setVariableValue(varName, 10);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetVariableValueNegativeValue() {
 String varName = "var1";
 Variable.addVariable(varName);
 try {
 Variable.setVariableValue(varName, -1);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testVariableListSize() {
 Variable.addVariable("var1");
 Variable.addVariable("var2");
 assertEquals(2, Variable.variableList.size());
 }

}