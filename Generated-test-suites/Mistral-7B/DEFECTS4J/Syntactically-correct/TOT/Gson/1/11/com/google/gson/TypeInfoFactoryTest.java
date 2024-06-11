package com.google.gson.tests;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;

import com.google.gson.TypeInfoFactory;

@RunWith(MockitoJUnitRunner.class)
public class TypeInfoFactoryTest {

  private static class DummyClass<A> {
    private A field;

    public A getField() {
      return field;
    }

    public static class DummyNestedClass<B> {
      private B nestedField;

      public B getNestedField() {
        return nestedField;
      }
    }
  }

  @Test
  public void testTypeInfoForArray() {
    Type arrayType = new TypeToken<int[]>() {}.getType();
    TypeInfo expectedTypeInfo = new TypeInfoArray(arrayType);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(arrayType);
    assertThat(actualTypeInfo).isEqualTo(expectedTypeInfo);
  }

  @Test
  public void testTypeInfoForField_simpleType() throws NoSuchFieldException {
    Class<DummyClass<Integer>> dummyClass = DummyClass.class;
    Field field = dummyClass.getDeclaredField("field");
    Type fieldType = field.getGenericType();

    TypeInfo expectedTypeInfo = new TypeInfo(Integer.TYPE);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, fieldType, dummyClass);
    assertThat(actualTypeInfo).isEqualTo(expectedTypeInfo);
  }

  @Test
  public void testTypeInfoForField_parameterizedType() throws NoSuchFieldException {
    Class<DummyClass<DummyClass.DummyNestedClass<String>>> nestedClass =
        DummyClass.DummyNestedClass.class;
    Class<DummyClass<DummyClass.DummyNestedClass>> outerClass = DummyClass.class;

    Field field = nestedClass.getDeclaredField("nestedField");
    Type fieldType = field.getGenericType();

    TypeInfo expectedTypeInfo = new TypeInfo(new TypeToken<DummyClass.DummyNestedClass<String>>() {}.getType());
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, fieldType, outerClass);
    assertThat(actualTypeInfo).isEqualTo(expectedTypeInfo);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForField_invalidType() throws NoSuchFieldException {
    Class<DummyClass<Integer>> dummyClass = DummyClass.class;
    Field field = dummyClass.getDeclaredField("field");
    Type fieldType = new Object() {};

    TypeInfoFactory.getTypeInfoForField(field, fieldType, dummyClass);
  }

  @Test
  public void testGetActualType_rawType() {
    Type rawType = Integer.TYPE;
    Type parentType = new TypeToken<DummyClass<Integer>>() {}.getType();
    Class<DummyClass<Integer>> rawParentClass = DummyClass.class;

    Type actualType = TypeInfoFactory.getActualType(rawType, parentType, rawParentClass);
    assertThat(actualType).isEqualTo(rawType);
  }

  @Test
  public void testGetActualType_parameterizedType() {
    Type rawType = new TypeToken<List<Integer>>() {}.getType();
    Type parentType = new TypeToken<DummyClass<List<Integer>>>() {}.getType();
    Class<DummyClass<List<Integer>>> rawParentClass = DummyClass.class;

    Type actualType = TypeInfoFactory.getActualType(rawType, parentType, rawParentClass);
    Type expectedType = new ParameterizedTypeImpl(
        new TypeToken<List<Integer>>() {}.getType(),
        new Type[] { Integer.TYPE },
        null);
    assertThat(actualType).isEqualTo(expectedType);
  }

  @Test
  public void testGetActualType_genericArrayType() {
    Type rawType = new TypeToken<int[]>() {}.getType();
    Type parentType = new TypeToken<DummyClass<int[]>>() {}.getType();
    Class<DummyClass<int[]>> rawParentClass = DummyClass.class;

    Type actualType = TypeInfoFactory.getActualType(rawType, parentType, rawParentClass);
    Type expectedType = new GenericArrayTypeImpl(Integer.TYPE);
    assertThat(actualType).isEqualTo(expectedType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualType_typeVariable_missing() {
    Type parentType = new TypeToken<DummyClass<A>>() {}.getType();
    Class<DummyClass<Object>> rawParentClass = DummyClass.class;

    TypeVariable<A> typeVariable = TypeToken.getParameterized(DummyClass.class, Object.class).getType()
        .getTypeParameters()[0];
    Type actualType = TypeInfoFactory.getActualType(typeVariable, parentType, rawParentClass);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualType_typeVariable_mismatched() {
    Type parentType = new TypeToken<DummyClass<List<String>>>() {}.getType();
    Class<DummyClass<List<Object>>> rawParentClass = DummyClass.class;

    TypeVariable<List<String>> typeVariable = TypeToken.getParameterized(List.class, String.class).getType()
        .getTypeParameters()[0];
    Type actualType = TypeInfoFactory.getActualType(typeVariable, parentType, rawParentClass);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualType_wildcardType() {
    Type parentType = new TypeToken<DummyClass<?>>() {}.getType();
    Class<DummyClass<Object>> rawParentClass = DummyClass.class;

    Type actualType = TypeInfoFactory.getActualType(parentType, parentType, rawParentClass);
  }

  @Test
  public void testExtractRealTypes() {
    Type[] types = new Type[] { new TypeToken<Integer>() {}.getType(),
        new TypeToken<String>() {}.getType() };
    Type parentType = new TypeToken<DummyClass<Integer, String>>() {}.getType();
    Class<DummyClass<Integer, String>> rawParentClass = DummyClass.class;

    Type[] expectedTypes = new Type[] { Integer.TYPE, String.class };
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(types, parentType, rawParentClass);
    Arrays.sort(expectedTypes);
    Arrays.sort(actualTypes);
    for (int i = 0; i < expectedTypes.length; ++i) {
      assertThat(actualTypes[i]).isEqualTo(expectedTypes[i]);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExtractRealTypes_invalidType() {
    Type[] types = new Type[] { new Object() {}, new Object() {} };
    Type parentType = new TypeToken<DummyClass<Object, Object>>() {}.getType();
    Class<DummyClass<Object, Object>> rawParentClass = DummyClass.class;

    TypeInfoFactory.extractRealTypes(types, parentType, rawParentClass);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testExtractRealTypes_missingTypeVariable() {
    Type[] types = new Type[] { new TypeVariable<A>() {}.getGenericDeclaration(),
        new TypeToken<String>() {}.getType() };
    Type parentType = new TypeToken<DummyClass<A, String>>() {}.getType();
    Class<DummyClass<Object, String>> rawParentClass = DummyClass.class;

    TypeInfoFactory.extractRealTypes(types, parentType, rawParentClass);
  }
}