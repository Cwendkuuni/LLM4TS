import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new TypeToken<int[]>() {}.getType();
    TypeInfo expectedTypeInfo = new TypeInfoArray(type);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test
  public void testTypeInfoForField_FieldWithClassType() throws NoSuchFieldException {
    Class<?> clazz = int[].class;
    Field field = clazz.getDeclaredField("value");
    Type type = field.getType();
    Type parentType = int[].class;
    Class<?> rawParentClass = int[].class;
    TypeInfo expectedTypeInfo = new TypeInfo(type);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, TypeUtils.boxedType(type), parentType);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test
  public void testTypeInfoForField_FieldWithParameterizedType() throws NoSuchFieldException {
    class MyClass<T> {
      T field;
    }

    Class<?> clazz = MyClass.class;
    Field field = clazz.getDeclaredField("field");
    Type type = field.getGenericType();
    Type parentType = MyClass.class;
    Class<?> rawParentClass = MyClass.class;
    Type expectedType = new TypeToken<Integer>() {}.getType();
    TypeInfo expectedTypeInfo = new TypeInfo(expectedType);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, type, parentType);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test
  public void testTypeInfoForField_FieldWithGenericArrayType() throws NoSuchFieldException {
    class MyClass<T> {
      T[] field;
    }

    Class<?> clazz = MyClass.class;
    Field field = clazz.getDeclaredField("field");
    Type type = field.getGenericType();
    Type parentType = MyClass.class;
    Class<?> rawParentClass = MyClass.class;
    Type expectedComponentType = int.class;
    TypeInfo expectedTypeInfo = new TypeInfoArray(new TypeToken<int[]>() {}.getType());
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, type, parentType);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test
  public void testTypeInfoForField_FieldWithTypeVariable() throws NoSuchFieldException {
    class MyClass<A, B> {
      A a;
      B b;
    }

    Class<?> clazz = MyClass.class;
    Field aField = clazz.getDeclaredField("a");
    Field bField = clazz.getDeclaredField("b");
    TypeVariable<?> aTypeVariable = (TypeVariable<?>) aField.getGenericType();
    TypeVariable<?> bTypeVariable = (TypeVariable<?>) bField.getGenericType();
    Type parentType = MyClass.class;
    Class<?> rawParentClass = MyClass.class;
    Type expectedAType = new TypeToken<Integer>() {}.getType();
    Type expectedBType = new TypeToken<String>() {}.getType();
    Type[] expectedTypeArguments = {expectedAType, expectedBType};
    TypeInfo expectedTypeInfoA = new TypeInfo(expectedAType);
    TypeInfo expectedTypeInfoB = new TypeInfo(expectedBType);
    TypeInfo actualTypeInfoA = TypeInfoFactory.getTypeInfoForField(aField, parentType, rawParentClass);
    TypeInfo actualTypeInfoB = TypeInfoFactory.getTypeInfoForField(bField, parentType, rawParentClass);
    Type[] actualTypeArguments = ((ParameterizedTypeImpl) actualTypeInfoA.getType()).getActualTypeArguments();
    assertEquals(expectedTypeInfoA, actualTypeInfoA);
    assertEquals(expectedTypeInfoB, actualTypeInfoB);
    assertEquals(expectedTypeArguments, actualTypeArguments);
  }

  @Test
  public void testTypeInfoForField_FieldWithWildcardType() throws NoSuchFieldException {
    class MyClass<T> {
      List<T> list;
    }

    Class<?> clazz = MyClass.class;
    Field listField = clazz.getDeclaredField("list");
    Type type = listField.getGenericType();
    Type parentType = MyClass.class;
    Class<?> rawParentClass = MyClass.class;
    Type upperBound = Object.class;
    TypeInfo expectedTypeInfo = new TypeInfo(new WildcardTypeImpl(TypeUtils.getRawType(List.class), upperBound));
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(listField, parentType, rawParentClass);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test
  public void testGetActualType_ClassType() {
    Type expectedType = int[].class;
    Type parentType = int[].class;
    Class<?> rawParentClass = int[].class;
    Type actualType = TypeInfoFactory.getActualType(expectedType, parentType, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualType_ParameterizedType() {
    Type expectedType = new TypeToken<List<String>>() {}.getType();
    Type parentType = List.class;
    Class<?> rawParentClass = List.class;
    Type[] expectedTypeArguments = {String.class};
    Type[] actualTypeArguments = TypeInfoFactory.extractRealTypes((Type[]) expectedType.getActualTypeArguments(), parentType, rawParentClass);
    assertEquals(expectedTypeArguments, actualTypeArguments);
    assertEquals(String.class, actualTypeArguments[0]);
  }

  @Test
  public void testGetActualType_GenericArrayType() {
    Type expectedType = new TypeToken<int[]>() {}.getType();
    Type parentType = int[].class;
    Class<?> rawParentClass = int[].class;
    int expectedComponentType = 0;
    Type componentType = new TypeToken<Integer>() {}.getType();
    Type actualType = TypeInfoFactory.getActualType(componentType, parentType, rawParentClass);
    assertEquals(componentType, actualType);
    actualType = TypeInfoFactory.getActualType(expectedType, parentType, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualType_TypeVariable() {
    Type expectedType = new TypeToken<MyClass<Integer, String>>() {}.getType();
    Type parentType = MyClass.class;
    Class<?> rawParentClass = MyClass.class;
    TypeVariable<?> aTypeVariable = new TypeVariable<>("A") {};
    TypeVariable<?> bTypeVariable = new TypeVariable<>("B") {};
    Type[] expectedTypeArguments = {new TypeToken<Integer>() {}.getType(), new TypeToken<String>() {}.getType()};
    Type[] actualTypeArguments = TypeInfoFactory.extractRealTypes((Type[]) expectedType.getActualTypeArguments(), parentType, rawParentClass);
    assertEquals(expectedTypeArguments, actualTypeArguments);
    assertEquals(Integer.class, actualTypeArguments[0]);
    assertEquals(String.class, actualTypeArguments[1]);
  }

  @Test
  public void testGetActualType_WildcardType() {
    Type expectedType = new TypeToken<MyClass<?>>() {}.getType();
    Type parentType = MyClass.class;
    Class<?> rawParentClass = MyClass.class;
    Type upperBound = Object.class;
    Type actualType = TypeInfoFactory.getActualType(expectedType, parentType, rawParentClass);
    assertEquals(new WildcardTypeImpl(TypeUtils.getRawType(MyClass.class), upperBound), actualType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualType_InvalidType() {
    Type invalidType = new Type[] {}.getClass();
    Type parentType = int[].class;
    Class<?> rawParentClass = int[].class;
    TypeInfoFactory.getActualType(invalidType, parentType, rawParentClass);
  }
}