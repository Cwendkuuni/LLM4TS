package com.google.gson.testing; // add this package declaration for testing purposes

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    Type type = new int[3].getClass().getComponentType();
    TypeInfoType expectedTypeInfo = new TypeInfoArray(type);
    TypeInfoType actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException, IllegalAccessException {
    Class<?> clazz = new ArrayList<Integer>(5).getClass();
    Field field = clazz.getDeclaredField("list");
    field.setAccessible(true);
    Type type = field.getGenericType();
    Type parentType = List.class;
    Type rawParentClass = ArrayList.class;
    Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
    TypeInfo expectedTypeInfo = new TypeInfo(new TypeToken<List<Integer>>() {}.getType());
    assertEquals(expectedTypeInfo.getType(), actualType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForFieldWithMissingTypeParameter() throws NoSuchFieldException, IllegalAccessException {
    Class<?> clazz = new ArrayList<Integer>(5).getClass();
    Field field = clazz.getDeclaredField("list2"); // this field doesn't exist in ArrayList
    field.setAccessible(true);
    Type type = field.getGenericType();
    Type parentType = List.class;
    Type rawParentClass = ArrayList.class;
    TypeInfoFactory.getActualType(type, parentType, rawParentClass);
  }

  @Test
  public void testGetActualTypeForRawClass() {
    Class<?> rawParentClass = String[].class;
    Type parentType = new TypeToken<List<String>>() {}.getType();
    Type expectedType = TypeUtils.wrapWithArray(String.class);
    Type actualType = TypeInfoFactory.getActualType(parentType, parentType, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualTypeForParameterizedType() {
    Class<?> rawParentClass = ArrayList.class;
    Type parentType = new TypeToken<List<List<String>>>() {}.getType();
    Type[] actualTypeArguments = new Type[] { new TypeToken<List<String>>() {}.getType(),
        new TypeToken<List<String>>() {}.getType() };
    Type expectedType = new ParameterizedTypeImpl(
        new TypeToken<List<List<String>>>() {}.getType(), actualTypeArguments, parentType);
    Type actualType = TypeInfoFactory.getActualType(parentType, parentType, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualTypeForGenericArrayType() {
    Class<?> rawParentClass = ArrayList.class;
    Type parentType = new TypeToken<List<List<String[]>>>() {}.getType();
    Type componentType = String[].class;
    Type expectedType = new GenericArrayTypeImpl(new TypeToken<List<String[]>>() {}.getType());
    Type actualType = TypeInfoFactory.getActualType(parentType, parentType, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualTypeForTypeVariableWithSingleTypeArgument() {
    Class<?> rawParentClass = ArrayList.class;
    Type parentType = List.class;
    TypeVariable<Integer> fieldTypeVariable = Integer.TYPE;
    int indexOfActualTypeArgument = 0;
    Type[] actualTypeArguments = new Type[] { new TypeToken<List<Integer>>() {}.getType() };
    Type expectedType = actualTypeArguments[indexOfActualTypeArgument];
    Type actualType = TypeInfoFactory.getActualType(fieldTypeVariable, parentType, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualTypeForTypeVariableWithMultipleTypeArguments() {
    Class<?> rawParentClass = ArrayList.class;
    Type parentType = new TypeToken<Map<String, List<List<Integer>>>>() {}.getType();
    TypeVariable<Map.Entry<String, List<List<Integer>>>> fieldTypeVariable = Map.Entry.class
        .getComponentType().getGenericComponentType();
    int indexOfActualTypeArgument = 1;
    Type[] actualTypeArguments = new Type[] { new TypeToken<String>().getType(),
        new TypeToken<List<List<Integer>>>() {}.getType() };
    Type expectedType = actualTypeArguments[indexOfActualTypeArgument];
    Type actualType = TypeInfoFactory.getActualType(fieldTypeVariable, parentType, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualTypeForWildcardType() {
    Class<?> rawParentClass = ArrayList.class;
    Type parentType = new TypeToken<List<?>>() {}.getType();
    Type[] upperBounds = new Type[] { String.class, String[].class };
    Type wildcardType = new WildcardTypeImpl(parentType, upperBounds);
    Type expectedType = String.class;
    Type actualType = TypeInfoFactory.getActualType(wildcardType, parentType, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  private interface TestClass<A, B> {
    A getA();
    B getB();
  }

  @Test
  public void testGetActualTypeForParameterizedTypeWithUnknownTypeArguments() {
    Class<?> rawParentClass = TestClass.class;
    Type parentType = new TypeToken<TestClass<?, ?>>() {}.getType();
    try {
      TypeInfoFactory.getActualType(parentType, parentType, rawParentClass);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }

  private static class TestClassImpl<A, B> implements TestClass<A, B> {
    private final A a;
    private final List<B> b;

    public TestClassImpl(A a, List<B> b) {
      this.a = a;
      this.b = b;
    }

    @Override
    public A getA() {
      return a;
    }

    @Override
    public List<B> getB() {
      return b;
    }
  }

  @Test
  public void testExtractRealTypes() {
    Class<?> rawParentClass = TestClassImpl.class;
    Type parentType = new TypeToken<TestClass<?, ?>>() {}.getType();
    Type[] expectedTypeArguments = new Type[] { new TypeToken<String>().getType(),
        new TypeToken<List<Integer>>() {}.getType() };
    Type[] actualTypeArguments = TypeInfoFactory.extractRealTypes(parentType.getActualTypeArguments(),
        parentType, rawParentClass);
    assertEquals(expectedTypeArguments, actualTypeArguments);
  }

  @Test
  public void testGetIndex() {
    TypeVariable<A> typeVariableA = new TypeVariable<A>("A") {};
    TypeVariable<B> typeVariableB = new TypeVariable<B>("B") {};
    TypeVariable<C> typeVariableC = new TypeVariable<C>("C") {};
    TypeVariable<?>[] types = { typeVariableA, typeVariableB, typeVariableC };
    TypeVariable<A> fieldTypeVariable = typeVariableA;
    int expectedIndex = 0;
    int actualIndex = TypeInfoFactory.getIndex(types, fieldTypeVariable);
    assertEquals(expectedIndex, actualIndex);
  }

  private static class TestClassWithThreeTypeVariables<A, B, C> {}

  @Test(expected = IllegalStateException.class)
  public void testGetIndexForMissingTypeVariable() {
    TypeVariable<?>[] types = new TypeVariable<?>[] { new TypeVariable<A>("A") {},
        new TypeVariable<B>("B") {} };
    TypeVariable<C> missingTypeVariable = new TypeVariable<C>("C") {};
    int index = TypeInfoFactory.getIndex(types, missingTypeVariable);
  }

  private static class TestClassWithMissingTypeVariable<A, B> {
    private final A a;
    private final B b;

    public TestClassWithMissingTypeVariable(A a, B b) {
      this.a = a;
      this.b = b;
    }

    @SuppressWarnings("unchecked")
    public static <C> TestClassWithMissingTypeVariable<A, C> newInstance(A a, C c) {
      return new TestClassWithMissingTypeVariable<A, C>(a, (List<C>) null);
    }

    public A getA() {
      return a;
    }

    public List<C> getB() {
      return (List<C>) null;
    }
  }

  @Test
  public void testGetActualTypeForMissingTypeVariable() {
    Class<?> rawParentClass = TestClassWithMissingTypeVariable.class;
    Type parentType = new TypeToken<TestClassWithMissingTypeVariable<String, String>>() {}.getType();
    TypeVariable<C> fieldTypeVariable = new TypeToken<C>() {}.getType().getComponentType()
        .getTypeParameters()[0];
    try {
      Type actualType = TypeInfoFactory.getActualType(fieldTypeVariable, parentType, rawParentClass);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // expected
    }
  }
}

class WildcardTypeImpl implements WildcardType {
  private final Type ownerType;
  private final Type[] upperBounds;

  public WildcardTypeImpl(Type ownerType, Type[] upperBounds) {
    this.ownerType = ownerType;
    this.upperBounds = upperBounds;
  }

  @Override
  public Type getLowerBounds() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Type getOwnerType() {
    return ownerType;
  }

  @Override
  public Type[] getUpperBounds() {
    return upperBounds;
  }
}

class ParameterizedTypeImpl implements ParameterizedType {
  private final Type rawType;
  private final Type[] actualTypeArguments;
  private final Type ownerType;

  public ParameterizedTypeImpl(Type rawType, Type[] actualTypeArguments, Type ownerType) {
    this.rawType = rawType;
    this.actualTypeArguments = actualTypeArguments;
    this.ownerType = ownerType;
  }

  @Override
  public Type getActualTypeArguments() {
    return actualTypeArguments;
  }

  @Override
  public Type getComponentType() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Type getDeclaredComponentType() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Type getRawType() {
    return rawType;
  }

  @Override
  public Type getOwnerType() {
    return ownerType;
  }

  @Override
  public String getTypeName() {
    throw new UnsupportedOperationException();
  }
}

class GenericArrayTypeImpl implements GenericArrayType {
  private final Type componentType;

  public GenericArrayTypeImpl(Type componentType) {
    this.componentType = componentType;
  }

  @Override
  public Type getComponentType() {
    return componentType;
  }

  @Override
  public Type getGenericComponentType() {
    return componentType;
  }

  @Override
  public Type getRawType() {
    return componentType.getComponentType();
  }
}

class TypeInfoArray implements TypeInfo {
  private final Type type;

  public TypeInfoArray(Type type) {
    this.type = type;
  }

  @Override
  public Type getType() {
    return type;
  }
}

class TypeInfo implements TypeInfo {
  private final Type type;

  public TypeInfo(Type type) {
    this.type = type;
  }

  @Override
  public Type getType() {
    return type;
  }
}