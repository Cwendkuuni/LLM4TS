package jipa.test;

   import org.junit.Assert;
   import org.junit.Test;
   import jipa.Variable;

   import java.util.ArrayList;
   import java.util.List;

   public class VariableTest {
       @Test
       public void testAddVariable() {
           // ...
       }

       @Test
       public void testRemoveVariable() {
           // ...
       }

       @Test
       public void testGetVariableValue() {
           // ...
       }

       @Test
       public void testSetVariableValue() {
           // ...
       }

       @Test
       public void testFindVariable_Index() {
           // ...
       }

       @Test
       public void testFindVariable_Value() {
           // ...
       }

       @Test
       public void testGetterValue() {
           // ...
       }

       @Test
       public void testSetterValue() {
           // ...
       }

       @Test
       public void testGettersAndSetters() {
           Variable variable = new Variable("testVar");
           assertEquals("testVar", variable.getName());
           assertEquals(0, variable.getValue());
           variable.setValue(5);
           assertEquals("testVar", variable.getName());
           assertEquals(5, variable.getValue());
       }

       @Test(expected = IllegalArgumentException.class)
       public void testRemoveMissingVariable() {
           // ...
       }

       @Test(expected = IllegalArgumentException.class)
       public void testSetVariableValueInvalidName() {
           // ...
       }

       @Test(expected = IllegalArgumentException.class)
       public void testGetVariableValueInvalidName() {
           // ...
       }

       @Test(expected = IllegalArgumentException.class)
       public void testFindVariableInvalidName() {
           // ...
       }

       @Test(expected = IllegalArgumentException.class)
       public void testGetVariableValueInvalidNameStartsWithAsterisk() {
           Variable.getVariableValue("*");
       }

       @Test(expected = IllegalArgumentException.class)
       public void testGetVariableValueInvalidNameWithAmpersand() {
           Variable.getVariableValue("&testVar");
       }

       @Test(expected = IndexOutOfBoundsException.class)
       public void testGetVariableValueWithIndexOutOfBounds() {
           Variable.addVariable("testVar1");
           Variable.addVariable("testVar2");
           Variable.removeVariable("testVar1");
           Variable.getVariableValue("testVar1");
       }

       @Test(expected = NullPointerException.class)
       public void testGetVariableValueWithNullVariableList() {
           Variable.variableList = null;
           Variable.getVariableValue("testVar");
       }

       @Test(expected = NullPointerException.class)
       public void testFindVariableWithNullVariableList() {
           Variable.variableList = null;
           Variable.findVariable("testVar");
       }

       @Test(expected = NullPointerException.class)
       public void testRemoveVariableWithNullVariableList() {
           Variable.variableList = null;
           Variable.removeVariable("testVar");
       }

       @Test(expected = NullPointerException.class)
       public void testAddVariableWithNullVariableList() {
           Variable.variableList = null;
           Variable.addVariable("testVar");
       }
   }