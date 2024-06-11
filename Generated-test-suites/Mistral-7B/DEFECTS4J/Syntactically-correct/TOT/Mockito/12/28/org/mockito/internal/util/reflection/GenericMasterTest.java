import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldUtil;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.FieldUtil.getDeclaredField;

public class GenericMasterTest {

 private GenericMaster genericMaster;
 private Object testObject;

 @Before
 public void init() {
 testObject = new TestClass<Integer, String>();
 genericMaster = new GenericMaster();
 }

 @Test
 public void testGetGenericTypeWithSingleType() {
 Field<Integer> integerField = getDeclaredField(TestClass.class, "integerField");
 Class expected = Integer.TYPE;
 Class actual = genericMaster.getGenericType(integerField);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetGenericTypeWithListType() {
 Field<List<String>> listField = getDeclaredField(TestClass.class, "listField");
 Class expected = List.class;
 Class<?> rawType = listField.getType().getComponentType();
 Class actual = genericMaster.getGenericType(listField);
 assertEquals(expected, actual);
 assertEquals(String.class, rawType);
 }

 @Test
 public void testGetGenericTypeWithNullField() {
 assertNull(genericMaster.getGenericType(null));
 }

 @Test
 public void testGetGenericTypeWithNonGenericField() {
 Field<Object> objectField = getDeclaredField(TestClass.class, "objectField");
 Class expected = Object.class;
 Class actual = genericMaster.getGenericType(objectField);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetGenericTypeWithArrayType() {
 Field<int[]> arrayField = getDeclaredField(TestClass.class, "arrayField");
 Class expected = int[].class;
 Class actual = genericMaster.getGenericType(arrayField);
 assertEquals(expected, actual.getComponentType());
 }

 @Test
 public void testGetGenericTypeWithNestedGenerics() {
 Field<Map<List<Integer>, List<String>>> mapField = getDeclaredField(TestClass.class, "mapField");
 Class expected = Map.class;
 Class<?> keyClass = List.class;
 Class<?> valueClass = List.class;
 Class<?> keyType = Integer.TYPE;
 Class<?> valueType = String.class;
 Class actual = (Class) genericMaster.getGenericType(mapField);
 assertEquals(expected, actual);
 assertEquals(List.class, actual.getComponentType());
 assertEquals(List.class, actual.getGenericComponentType(0));
 assertEquals(Integer.TYPE, actual.getGenericComponentType(1).getComponentType());
 assertEquals(String.class, actual.getGenericComponentType(1).getGenericComponentType(0));
 }

 @Test
 public void testGetGenericTypeWithParameterizedType() {
 Field<MyList<MyMap<Integer, String, String>>> mapListField = getDeclaredField(TestClass.class, "mapListField");
 Class expected = MyList.class;
 Class<?> mapType = MyMap.class;
 Class<?> mapKeyType = Integer.TYPE;
 Class<?> mapValueType1 = String.class;
 Class<?> mapValueType2 = String.class;
 Class<?> listElementType = MyMap.class;
 Class actual = (Class) genericMaster.getGenericType(mapListField);
 assertEquals(expected, actual);
 assertEquals(MyMap.class, actual.getComponentType());
 assertEquals(MyMap.class, actual.getGenericComponentType(0));
 assertEquals(Integer.TYPE, actual.getGenericComponentType(1).getComponentType());
 assertEquals(String.class, actual.getGenericComponentType(1).getGenericComponentType(0));
 assertEquals(String.class, actual.getGenericComponentType(1).getGenericComponentType(1));
 }

 class TestClass<T, U> {
 private T integerField;
 private List<U> listField;
 private Object objectField;
 private int[] arrayField;
 private Map<List<Integer>, List<String>> mapField;
 private MyList<MyMap<Integer, String, String>> mapListField;

 // getters and setters
 }

 class MyList<T> {
 // List implementation
 }

 class MyMap<K, V, VV> {
 // Map implementation
 }
}