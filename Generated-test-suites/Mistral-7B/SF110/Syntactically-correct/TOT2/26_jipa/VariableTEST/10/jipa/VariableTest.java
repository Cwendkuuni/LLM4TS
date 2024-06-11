package jipa;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class VariableTest {

@Test
public void testAddVariable() {
Variable.variableList = new ArrayList<>();
String varName = "testVar";
boolean expectedResult = Variable.addVariable(varName);
boolean actualResult = Variable.addVariable(varName);
Assert.assertEquals(expectedResult, actualResult);
}

@Test
public void testRemoveVariable() {
Variable.variableList = new ArrayList<>();
Variable variable = new Variable("testVar");
Variable.addVariable("testVar");
Variable.removeVariable("testVar");
boolean expectedResult = false;
boolean actualResult = Variable.removeVariable("testVar");
Assert.assertEquals(expectedResult, actualResult);
}

@Test
public void testGetVariableValue() {
Variable.variableList = new ArrayList<>();
Variable variable = new Variable("testVar");
Variable.addVariable("testVar");
Variable.setVariableValue("testVar", 5);
int expectedResult = 5;
int actualResult = Variable.getVariableValue("testVar");
Assert.assertEquals(expectedResult, actualResult);
}

@Test
public void testAddVariableEdgeCase() {
Variable.variableList = new ArrayList<>(10);
for (int i = 0; i < 10; i++) {
String varName = "testVar" + i;
Variable.addVariable(varName);
}
String varName = "testVar11";
boolean expectedResult = false;
boolean actualResult = Variable.addVariable(varName);
Assert.assertEquals(expectedResult, actualResult);
}

@Test(expected = NullPointerException.class)
public void testAddVariableNullVarName() {
Variable.variableList = new ArrayList<>();
String varName = null;
Variable.addVariable(varName);
}

@Test
public void testRemoveVariableEdgeCase() {
Variable.variableList = new ArrayList<>();
Variable variable = new Variable("testVar");
Variable.addVariable("testVar");
Variable.removeVariable("testVar");
String varName = "testVar";
boolean expectedResult = false;
boolean actualResult = Variable.removeVariable(varName);
Assert.assertEquals(expectedResult, actualResult);
}

@Test(expected = IndexOutOfBoundsException.class)
public void testRemoveVariableInvalidIndex() {
Variable.variableList = new ArrayList<>();
Variable variable = new Variable("testVar");
Variable.addVariable("testVar");
Variable.removeVariable(0);
}

@Test
public void testGetVariableValueEdgeCase() {
Variable.variableList = new ArrayList<>();
Variable variable = new Variable("testVar");
Variable.addVariable("testVar");
Variable.setVariableValue("testVar", 5);
Variable.removeVariable("testVar");
String varName = "testVar";
int expectedResult = 0;
int actualResult = Variable.getVariableValue(varName);
Assert.assertEquals(expectedResult, actualResult);
}

@Test(expected = NullPointerException.class)
public void testGetVariableValueNullVarName() {
Variable.variableList = new ArrayList<>();
String varName = null;
Variable.getVariableValue(varName);
}

@Test(expected = IndexOutOfBoundsException.class)
public void testGetVariableValueInvalidIndex() {
Variable.variableList = new ArrayList<>();
Variable variable = new Variable("testVar");
Variable.addVariable("testVar");
Variable.setVariableValue("testVar", 5);
String varName = "testVar2";
Variable.getVariableValue(varName);
}
}