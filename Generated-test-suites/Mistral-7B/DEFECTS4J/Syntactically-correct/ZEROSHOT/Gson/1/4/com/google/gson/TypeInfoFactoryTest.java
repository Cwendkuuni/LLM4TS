import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Preconditions;
import com.google.gson.TypeUtils;
import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeVariableImpl;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Before;
import org.junit.Test;

public class TypeInfoFactoryTest {

  private static final Class<?> TEST_CLASS = TestTypeInfoFactory.class;
  private static final Field TEST_FIELD = TEST_CLASS.getDeclaredField("testField");

  private TypeInfoFactory typeInfoFactory;
  private Field testField;

  @Before
  public void setUp() throws Exception {
    typeInfoFactory = TypeInfoFactory.getInstance();
    testField = TEST_FIELD;
    TEST_FIELD.setAccessible(true);
  }

  @Test
  public void testGetTypeInfoForArray() {
    Type type = new int[5].getClass().getComponentType();
    TypeInfoArray typeInfoArray = typeInfoFactory.getTypeInfoForArray(type);
    assertNotNull(typeInfoArray);
    assertEquals(type, typeInfoArray.getType());
  }

  @Test
  public void testGetTypeInfoForField() throws Exception {
    Type type = TypeToken.get(TEST_CLASS).getType();
    Type parentType = TEST_CLASS.getGenericSuperclass();
    Class<?> rawParentClass = TEST_CLASS.getSuperclass();

    Type actualType = typeInfoFactory.getActualType(testField.getGenericType(), parentType, rawParentClass);
    TypeInfo typeInfo = typeInfoFactory.getTypeInfoForField(testField, parentType);

    assertEquals(actualType, typeInfo.getType());
  }

  @Test
  public void testGetActualTypeForClass() {
    Class<?> testClass = TYPE_INFO_FACTORY_TEST_TYPE.class;
    Type actualType = typeInfoFactory.getActualType(testClass, null, null);
    assertEquals(testClass, actualType);
  }

  @Test
  public void testGetActualTypeForParameterizedType() {
    Class<?> testClass = List.class;
    Type parentType = new TypeToken<List<String>>() {}.getType();
    Class<?> rawParentClass = ArrayList.class;

    Type[] actualTypeArguments = typeInfoFactory.extractRealTypes(testClass.getTypeParameters(), parentType, rawParentClass);
    Type rawType = testClass.getGenericSuperclass();
    Type actualType =
        typeInfoFactory.getActualType(new ParameterizedTypeImpl(rawType, actualTypeArguments, null), parentType, rawParentClass);

    assertEquals(List.class, actualType);
  }

  @Test
  public void testGetActualTypeForGenericArrayType() {
    Class<?> testClass = int[].class;
    Type parentType = null;
    Class<?> rawParentClass = Integer.class;

    Type componentType = int.class;
    Type actualType = typeInfoFactory.getActualType(new GenericArrayTypeImpl(componentType), parentType, rawParentClass);

    assertEquals(int[].class, actualType);
  }

  @Test
  public void testGetActualTypeForTypeVariable() {
    Class<?> testClass = TestTypeInfoVariable.class;
    Type parentType = new TypeToken<TestTypeInfoVariable<Integer>>() {}.getType();
    Class<?> rawParentClass = TestTypeInfoVariable.class;

    TypeVariable<?> fieldTypeVariable = TypeVariableImpl.of("T", Integer.TYPE);
    TypeVariable<?>[] classTypeVariables = testClass.getTypeParameters();
    int indexOfActualTypeArgument = typeInfoFactory.getIndex(classTypeVariables, fieldTypeVariable);
    Type actualTypeArgument = new TypeToken<Integer>() {}.getType();

    Type actualType = typeInfoFactory.getActualType(parentType, parentType, rawParentClass);
    assertEquals(actualTypeArgument, actualType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualTypeForUnknownType() {
    Type unknownType = new Object() {}.getClass().getGenericSuperclass();
    typeInfoFactory.getActualType(unknownType, null, null);
  }

  @Test
  public void testExtractRealTypes() {
    Class<?> testClass = List.class;
    Type parentType = new TypeToken<List<String>>() {}.getType();
    Class<?> rawParentClass = ArrayList.class;

    Type[] actualTypeArguments = typeInfoFactory.extractRealTypes(testClass.getTypeParameters(), parentType, rawParentClass);
    assertEquals(2, actualTypeArguments.length);
    assertEquals(String.class, actualTypeArguments[0]);
    assertEquals(Collection.class, actualTypeArguments[1]);
  }

  @Test
  public void testGetIndex() {
    TypeVariable<?>[] types = new TypeVariable<?>[] { new TypeVariable<?>("A"), new TypeVariable<>("B") };
    TypeVariable<?> type = new TypeVariable<>("A");

    int index = typeInfoFactory.getIndex(types, type);
    assertEquals(0, index);
  }

  private static class TestTypeInfoFactory {
    private static final TypeInfoFactory INSTANCE = new TypeInfoFactory();

    public static TypeInfoFactory getInstance() {
      return INSTANCE;
    }

    private int testField;
  }

  private static class TestTypeInfoVariable<T> {
    private T value;
  }

  private static class TestTypeInfoVariableType<T> {
    private TypeVariable<T> typeVariable;
  }

  private static class TypeInfoArray implements TypeInfo {
    private Type type;

    public TypeInfoArray(Type type) {
      this.type = type;
    }

    @Override
    public Type getType() {
      return type;
    }
  }

  private static class ParameterizedTypeImpl implements ParameterizedType {
    private final Type rawType;
    private final Type[] actualTypeArguments;
    private final Type ownerType;

    public ParameterizedTypeImpl(Type rawType, Type[] actualTypeArguments, Type ownerType) {
      this.rawType = rawType;
      this.actualTypeArguments = actualTypeArguments;
      this.ownerType = ownerType;
    }

    @Override
    public Type getRawType() {
      return rawType;
    }

    @Override
    public Type[] getActualTypeArguments() {
      return actualTypeArguments;
    }

    @Override
    public Type getOwnerType() {
      return ownerType;
    }

    @Override
    public String getCanonicalName() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Type getComponentType() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Type getDeclaringType() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Type getErasure() {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Type getTypeArguments() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Type getGenericComponentType() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Type[] getTypeParameters() {
      throw new UnsupportedOperationException();
    }
  }

  private static class GenericArrayTypeImpl implements GenericArrayType {
    private final Type componentType;

    public GenericArrayTypeImpl(Type componentType) {
      this.componentType = componentType;
    }

    @Override
    public Type getComponentType() {
      return componentType;
    }

    @Override
    public String getCanonicalName() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Type getGenericComponentType() {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
      throw new UnsupportedOperationException();
    }
  }

  private static class TypeVariableImpl implements TypeVariable<Object> {
    private final String name;
    private final Class<?> bound;

    public TypeVariableImpl(String name, Class<?> bound) {
      this.name = name;
      this.bound = bound;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public Type getBounds() {
      return bound;
    }

    @Override
    public String getGenericDeclaration() {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getCanonicalName() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Type[] getBoundsTypes() {
      return new Type[] { bound };
    }

    @Override
    public Type getType() {
      throw new UnsupportedOperationException();
    }
  }
}

class TestTypeInfoVariable<T> {
  private T value;
}

class TestTypeInfoVariableType<T> {
  private TypeVariable<T> typeVariable;
}

class TestTypeInfoVariableTypeTest {
  @Test
  public void testGetActualType() {
    TypeVariable<Integer> integerTypeVariable = new TypeVariable<>("T", Integer.TYPE);
    Type testType = new TypeToken<TestTypeInfoVariableType<Integer>>() {}.getType();
    Class<?> rawParentClass = TestTypeInfoVariableType.class;

    Type actualType = typeInfoFactory.getActualType(integerTypeVariable, testType, rawParentClass);
    assertEquals(Integer.TYPE, actualType);
  }
}