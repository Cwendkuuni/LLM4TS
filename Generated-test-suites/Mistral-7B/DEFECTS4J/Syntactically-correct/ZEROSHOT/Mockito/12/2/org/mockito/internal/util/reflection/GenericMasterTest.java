package org.junit. Test; import org.junit.Assert; import org.junit.Before; import org.junit.Test; import org.mockito.internal.util.reflection.GenericMaster;

public class GenericMasterTest {

 private GenericMaster genericMaster;

 @Before
 public void setup() {
 genericMaster = new GenericMaster();
 }

 @Test
 public void testGetGenericType() {
 // Test case 1: Test with a generic field
 Field field1 = getClass().getDeclaredField("someField"); // Assuming there is a generic field named "someField" in this class
 field1.setAccessible(true);
 Class expectedType1 = String.class;
 Class actualType1 = genericMaster.getGenericType(field1);
 Assert.assertEquals(expectedType1, actualType1);

 // Test case 2: Test with a non-generic field
 Field field2 = getClass().getDeclaredField("nonGenericField"); // Assuming there is a non-generic field named "nonGenericField" in this class
 field2.setAccessible(true);
 Class expectedType2 = Object.class;
 Class actualType2 = genericMaster.getGenericType(field2);
 Assert.assertEquals(expectedType2, actualType2);
}
}