import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() {
 // Test case 1: Test with a simple generic field
 Class clazz = MyClass.class;
 Field field = ReflectionUtils.findField(clazz, "myList");
 Assert.assertEquals(List.class, new GenericMaster().getGenericType(field));

 // Test case 2: Test with a null field
 Assert.assertEquals(Object.class, new GenericMaster().getGenericType(null));

 // Test case 3: Test with a non-generic field
 Field nonGenericField = ReflectionUtils.findField(clazz, "myStringField");
 Assert.assertEquals(String.class, new GenericMaster().getGenericType(nonGenericField));

 // Test case 4: Test with a complex generic field (nested generics)
 Class nestedClass = NestedClass.class;
 Field nestedField = ReflectionUtils.findField(nestedClass, "myNestedList");
 Assert.assertEquals(List.class, new GenericMaster().getGenericType(nestedField).getComponentType());
 }

 private static class MyClass {
 private List<String> myList;
 private String myStringField;
 }

 private static class NestedClass {
 private List<?> myNestedList;
 }
}