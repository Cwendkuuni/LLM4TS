import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestHelper;

import java.lang.reflect.Field;
import java.util.Arrays;

public class GenericMasterTest {

 @Test
 public void testGetGenericType_FieldWithSingleGenericType() {
 Field field = new FieldTestHelper().getField(GenericMaster.class, "genericTypeField");
 Class expected = Integer.class;
 Class actual = new GenericMaster().getGenericType(field);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetGenericType_FieldWithNoGenericType() {
 Field field = new FieldTestHelper().getField(GenericMaster.class, "nonGenericField");
 Class expected = Object.class;
 Class actual = new GenericMaster().getGenericType(field);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testGetGenericType_NullField() {
 Field field = null;
 //This test case is for NullPointerException when field is null
 GenericMaster genericMaster = new GenericMaster();
 genericMaster.getGenericType(field);
 }

 @Test
 public void testGetGenericType_NestedGenericType() {
 Field field = new FieldTestHelper().getField(GenericMaster.class, "nestedGenericTypeField");
 Class expected = String[].class;
 Class actual = new GenericMaster().getGenericType(field);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetGenericType_FieldWithMultipleGenericTypes() {
 Field field = new FieldTestHelper().getField(GenericMaster.class, "multipleGenericTypesField");
 Class[] expected = {Integer.class, String.class};
 Class[] actual = new GenericMaster().getGenericType(field).getActualTypeArguments();
 Assert.assertArrayEquals(expected, actual);
 }

 @Test(expected = ClassCastException.class)
 public void testGetGenericType_FieldWithNonParameterizedType() {
 Field field = new FieldTestHelper().getField(GenericMaster.class, "nonParameterizedTypeField");
 Class actual = new GenericMaster().getGenericType(field);
 Assert.assertNotNull(actual);
 Assert.assertEquals(Object.class, actual.getClass());
 }
}