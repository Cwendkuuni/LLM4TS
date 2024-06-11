import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;

public class GenericMasterTest {

 @Test
 public void testGetGenericTypeWithSimpleField() {
 Field field = getField(SomeClass.class, "simpleField");
 Class expected = String.class;
 Class actual = new GenericMaster().getGenericType(field);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetGenericTypeWithNullField() {
 Class expected = Object.class;
 Class actual = new GenericMaster().getGenericType(null);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetGenericTypeWithNonGenericField() {
 Field field = getField(SomeClass.class, "nonGenericField");
 Class expected = Object.class;
 Class actual = new GenericMaster().getGenericType(field);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetGenericTypeWithArrayField() {
 Field field = getField(SomeArrayClass.class, "arrayField");
 int expectedDimensions = 1;
 Class expected = int[].class;
 Class actual = new GenericMaster().getGenericType(field);
 Assert.assertEquals(expectedDimensions, Array.getLength(expected.getComponentType()));
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testGetGenericTypeWithNullGenericType() {
 Field field = getField(SomeClassWithNullGenericType.class, "field");
 Class expected = Object.class;
 try {
 Class actual = new GenericMaster().getGenericType(field);
 } catch (NullPointerException e) {
 Assert.assertNotNull(e.getMessage());
 }
 Assert.assertEquals(expected, Object.class);
 }

 @Test(expected = ArrayStoreException.class)
 public void testGetGenericTypeWithMismatchedArrayType() {
 Field field = getField(MismatchedArrayClass.class, "field");
 Class expected = int[].class;
 Class actual = new GenericMaster().getGenericType(field);
 Assert.assertEquals(expected.getClass(), Object[].class);
 try {
 actual.setComponentType(String.class);
 } catch (ArrayStoreException e) {
 // expected
 }
 }

 private static Field getField(Class clazz, String fieldName) {
 try {
 return clazz.getDeclaredField(fieldName);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 private static class SomeClass {
 private String simpleField;
 }

 private static class SomeArrayClass {
 private int[] arrayField;
 }

 private static class SomeClassWithNullGenericType {
 private Field nullField;
 }

 private static class MismatchedArrayClass {
 private int[] field;
 }
}