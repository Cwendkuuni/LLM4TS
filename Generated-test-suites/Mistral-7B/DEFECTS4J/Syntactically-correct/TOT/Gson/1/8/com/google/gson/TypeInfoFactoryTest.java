import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class TypeInfoFactoryTest {

 private static class DummyClass<A, B, C> {
 Field a;
 Field b;
 Field c;
 }

 @Test
 public void testTypeInfoForArray() {
 Type type = new int[5].getClass().getGenericComponentType();
 TypeInfo expectedTypeInfo = new TypeInfoArray(type);
 TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
 assertEquals(expectedTypeInfo, actualTypeInfo);
 }

 @Test
 public void testTypeInfoForField() throws NoSuchFieldException {
 DummyClass<Integer, String, Integer> dummyClass = new DummyClass<>();
 Field aField = DummyClass.class.getDeclaredField("a");
 Type fieldType = aField.getType();
 Type definingClassType = DummyClass.class;
 Type definingFieldType = TypeUtils.getParameterizedType(definingClassType, 0);
 Type actualType = TypeInfoFactory.getActualType(fieldType, definingFieldType, DummyClass.class);
 TypeInfo expectedTypeInfo = new TypeInfo(Integer.TYPE);
 assertEquals(expectedTypeInfo, TypeInfoFactory.getTypeInfoForField(aField, definingClassType));
 assertEquals(actualType, expectedTypeInfo.getType());
 }

 @Test
 public void testGetActualType() throws NoSuchFieldException {
 DummyClass<TypeVariable<Integer>, String, Integer> dummyClass = new DummyClass<>();
 Field aField = DummyClass.class.getDeclaredField("a");
 Type fieldType = aField.getGenericType();
 Type definingClassType = DummyClass.class;
 Type definingFieldType = TypeUtils.getParameterizedType(definingClassType, 0);
 Type expectedType = new TypeVariable<Integer>("T") {};
 Type actualType = TypeInfoFactory.getActualType(fieldType, definingFieldType, DummyClass.class);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetActualType_WildcardType() {
 Type listType = new TypeToken<List<?>>() {}.getType();
 Type definingClassType = ArrayList.class;
 Class<?> rawParentClass = ArrayList.class;
 Type expectedType = Object.class;
 Type actualType = TypeInfoFactory.getActualType(listType, definingClassType, rawParentClass);
 assertEquals(expectedType, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetActualType_NotTypeOrParameterizedTypeOrGenericArrayTypeOrWildcardType() {
 Type numberType = Double.TYPE;
 Type actualType = TypeInfoFactory.getActualType(numberType, null, null);
 fail("IllegalArgumentException expected");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetActualType_MissingTypeParameter() {
 Class<?> missingTypeParameterClass = new Object() {
 };
 Type missingTypeParameterType = missingTypeParameterClass.getTypeParameters()[0];
 Type definingClassType = missingTypeParameterClass.getClass();
 Type definingFieldType = TypeUtils.getParameterizedType(definingClassType, 0);
 Type expectedExceptionType = UnsupportedOperationException.class;
 try {
 Type actualType = TypeInfoFactory.getActualType(missingTypeParameterType, definingFieldType, definingClassType);
 fail("UnsupportedOperationException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 TypeException e = Mockito.mock(TypeException.class);
 when(e.getMessage())
 .thenReturn("Expecting parameterized type, got " + definingClassType + ".\n Are you missing the use of TypeToken idiom?\n See "
 + "http://sites.google.com/site/gson/gson-user-guide#TOC-Serializing-and-Deserializing-Gener");
 assertThrows(UnsupportedOperationException.class, () -> {
 Type actualType = TypeInfoFactory.getActualType(missingTypeParameterType, definingFieldType, definingClassType);
 });
 assertEquals("Expecting parameterized type, got " + definingClassType + ".\n Are you missing the use of TypeToken idiom?\n See "
 + "http://sites.google.com/site/gson/gson-user-guide#TOC-Serializing-and-Deserializing-Gener", e.getMessage());
 }
}