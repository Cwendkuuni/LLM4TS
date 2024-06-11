package jipa;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class VariableTest {
    @Test
    public void testAddVariable() {
        // Test adding a valid variable
        String varName = "testVar";
        boolean added = Variable.addVariable(varName);
        assertTrue(added);
        // Test adding a variable that already exists
        added = Variable.addVariable(varName);
        assertFalse(added);
        // Test adding a variable with an empty list
        Variable.variableList = new ArrayList<>();
        added = Variable.addVariable(varName);
        assertTrue(added);
        // Test adding multiple variables
        String varName1 = "testVar1";
        String varName2 = "testVar2";
        Variable.addVariable(varName1);
        Variable.addVariable(varName2);
        assertEquals(2, Variable.variableList.size());
    }
    
    @Test
    public void testGetVariableValue() {
        // Test getting the value of a valid variable
        String varName = "testVar";
        Variable.addVariable(varName);
        Variable.setVariableValue(varName, 42);
        int value = Variable.getVariableValue(varName);
        assertEquals(42, value);
        
        // Test getting the value of a non-existent variable
        String nonExistentVarName = "nonExistentVar";
        value = Variable.getVariableValue(nonExistentVarName);
        assertEquals(0, value);
        
        // Test getting the value of a variable with a leading asterisk
        String varNameWithAsterisk = "*testVar";
        Variable.addVariable(varNameWithAsterisk);
        Variable.setVariableValue(varNameWithAsterisk, 7);
        value = Variable.getVariableValue(varNameWithAsterisk);
        assertEquals(7, value);
        
        // Test getting the value of a variable with a leading ampersand
        String varNameWithAmpersand = "&testVar";
        Variable.addVariable(varNameWithAmpersand);
        Variable.setVariableValue(varNameWithAmpersand, 13);
        value = Variable.getVariableValue(varNameWithAmpersand);
        assertEquals(13, value);
    }
    
    @Test
    public void testRemoveVariable() {
        // Test removing a valid variable
        String varName = "testVar";
        Variable.addVariable(varName);
        Variable.removeVariable(varName);
        assertTrue(Variable.variableList.isEmpty());
        
        // Test removing a non-existent variable
        String nonExistentVarName = "nonExistentVar";
        Variable.removeVariable(nonExistentVarName);
        assertEquals(0, Variable.variableList.size());
        
        // Test removing a variable with an empty list
        Variable.variableList = new ArrayList<>();
        Variable.removeVariable(varName);
        assertEquals(0, Variable.variableList.size());
        
        // Test removing multiple variables
        String varName1 = "testVar1";
        String varName2 = "testVar2";
        Variable.addVariable(varName1);
        Variable.addVariable(varName2);
        Variable.removeVariable(varName1);
        assertEquals(1, Variable.variableList.size());
        assertEquals(varName2, Variable.variableList.get(0).getName());
    }
}