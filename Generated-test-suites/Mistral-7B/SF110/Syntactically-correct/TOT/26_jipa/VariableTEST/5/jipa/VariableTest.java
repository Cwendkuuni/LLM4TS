//JUnit 4 test file for the Variable class
package jipa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class VariableTest {

private static List<Variable> variables = Variable.variableList;

@Test
public void testAddVariable() {
// Test adding valid variables
Variable variable1 = new Variable("var1");
Variable variable2 = new Variable("var2");
Variable variable3 = new Variable("var3");
Variable.addVariable("var1");
Variable.addVariable("var2");
Variable.addVariable("var3");
assertEquals(3, variables.size());
assertTrue(variables.contains(variable1));
assertTrue(variables.contains(variable2));
assertTrue(variables.contains(variable3));

// Test adding duplicate variable
Variable variable1Duplicate = new Variable("var1");
Variable.addVariable("var1");
assertFalse(Variable.addVariable("var1"));
assertEquals(3, variables.size());
assertTrue(variables.contains(variable1Duplicate));

// Test adding null variable name
assertFalse(Variable.addVariable(null));
assertFalse(variables.isEmpty());

// Test adding empty string variable name
assertFalse(Variable.addVariable(""));
assertFalse(variables.isEmpty());
}

@Test
public void testGetVariableValue() {
// Test getting value of existing variable
Variable variable1 = new Variable("var1");
Variable.addVariable("var1");
Variable.setVariableValue("var1", 42);
assertEquals(42, Variable.getVariableValue("var1"));
assertEquals(42, variable1.getValue());

// Test getting value of non-existent variable
assertNull(Variable.getVariableValue("nonExistentVariable"));

// Test getting value of variable with a leading '*'
Variable variableStar1 = new Variable("*var1");
Variable.addVariable("*var1");
Variable.setVariableValue("*var1", 56);
assertEquals(56, Variable.getVariableValue("*var1"));
assertEquals(56, variableStar1.getValue());

// Test getting value of variable with a leading '&'
Variable variableAnd1 = new Variable("&var1");
Variable.addVariable("&var1");
Variable.setVariableValue("&var1", 78);
assertNotNull(Variable.getVariableValue("&var1"));
assertEquals(78, ((Variable) Variable.getVariableValue("&var1")).getValue());

// Test getting value of variable with the same name but different casing
Variable variable1CasedDifferently = new Variable("VAR1");
Variable.addVariable("VAR1");
Variable.setVariableValue("VAR1", 89);
assertEquals(89, Variable.getVariableValue("VAR1"));
assertEquals(89, variable1CasedDifferently.getValue());
}

@Test
public void testRemoveVariable() {
// Test removing valid variables
Variable variable1 = new Variable("var1");
Variable variable2 = new Variable("var2");
Variable variable3 = new Variable("var3");
Variable.addVariable("var1");
Variable.addVariable("var2");
Variable.addVariable("var3");
assertEquals(3, variables.size());
assertTrue(variables.contains(variable1));
assertTrue(variables.contains(variable2));
assertTrue(variables.contains(variable3));

// Test removing existing variable
Variable.removeVariable("var1");
assertEquals(2, variables.size());
assertFalse(variables.contains(variable1));
assertEquals("var2", Variable.getVariableValue("var2").getName());

// Test removing non-existent variable
Variable.removeVariable("nonExistentVariable");
assertEquals(2, variables.size());

// Test removing duplicate variables
Variable variable1Duplicate = new Variable("var1");
Variable.addVariable("var1");
Variable.addVariable("var1");
Variable.removeVariable("var1");
assertEquals(1, variables.size());
assertFalse(variables.contains(variable1Duplicate));
assertEquals("var2", Variable.getVariableValue("var2").getName());

// Test removing all variables
Variable.removeVariable("var2");
assertEquals(0, variables.size());
}

// Test edge cases
@Test
public void testEdgeCases() {
// Test adding a variable with an invalid name (empty string)
Variable invalidVariable = new Variable("");
assertFalse(Variable.addVariable(""));
assertFalse(variables.isEmpty());

// Test getting value of variable with an empty name
assertNull(Variable.getVariableValue(""));

// Test getting value of variable with a null name
assertNull(Variable.getVariableValue(null));

// Test removing a variable with a null name
Variable.removeVariable(null);
assertTrue(variables.isEmpty());
}

// Test error scenarios
@Test(expected = IllegalArgumentException.class)
public void testErrorScenarios() {
// Test adding a variable with a null reference
Variable variableNull = new Variable(null);
assertFalse(Variable.addVariable(null));
assertFalse(variables.isEmpty());

// Test setting value of a non-existent variable
Variable.setVariableValue("nonExistentVariable", 123);
assertEquals(0, variables.size());
}
}