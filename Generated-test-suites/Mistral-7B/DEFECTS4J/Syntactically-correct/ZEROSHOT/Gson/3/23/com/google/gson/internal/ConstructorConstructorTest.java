import org.junit.Test;
import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ConstructorConstructorTest {

  @Test
  public void testGetInstance() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    // Test for Collection
    TypeCollection typeCollection = new TypeCollection();
    TypeCollectionTypeAdapter typeCollectionTypeAdapter = new TypeCollectionTypeAdapter();
    instanceCreators.put(TypeToken.get(Collection.class), typeCollectionTypeAdapter);
    ObjectConstructor<Collection<String>> expectedCollectionConstructor = new ObjectConstructor<Collection<String>>() {
      @Override
      public Collection<String> construct() {
        return new ArrayList<>();
      }
    };
    ObjectConstructor<Collection<String>> actualCollectionConstructor = constructorConstructor.get(new TypeToken<Collection<String>>(){});
    assertEquals(expectedCollectionConstructor, actualCollectionConstructor);

    // Test for EnumSet
    TypeEnumSet typeEnumSet = new TypeEnumSet();
    TypeEnumSetTypeAdapter typeEnumSetTypeAdapter = new TypeEnumSetTypeAdapter();
    instanceCreators.put(EnumSet.class, typeEnumSetTypeAdapter);
    ObjectConstructor<EnumSet<String>> expectedEnumSetConstructor = new ObjectConstructor<EnumSet<String>>() {
      @Override
      public EnumSet<String> construct() {
        return EnumSet.noneOf(String.class);
      }
    };
    ObjectConstructor<EnumSet<String>> actualEnumSetConstructor = constructorConstructor.get(new TypeToken<EnumSet<String>>(){});
    assertEquals(expectedEnumSetConstructor, actualEnumSetConstructor);

    // Test for Set
    ObjectConstructor<Set<String>> expectedSetConstructor = new ObjectConstructor<Set<String>>() {
      @Override
      public Set<String> construct() {
        return new LinkedHashSet<>();
      }
    };
    ObjectConstructor<Set<String>> actualSetConstructor = constructorConstructor.get(new TypeToken<Set<String>>(){});
    assertEquals(expectedSetConstructor, actualSetConstructor);

    // Test for Queue
    ObjectConstructor<Queue<String>> expectedQueueConstructor = new ObjectConstructor<Queue<String>>() {
      @Override
      public Queue<String> construct() {
        return new LinkedList<>();
      }
    };
    ObjectConstructor<Queue<String>> actualQueueConstructor = constructorConstructor.get(new TypeToken<Queue<String>>(){});
    assertEquals(expectedQueueConstructor, actualQueueConstructor);

    // Test for Map
    ObjectConstructor<Map<String, Object>> expectedMapConstructor = new ObjectConstructor<Map<String, Object>>() {
      @Override
      public Map<String, Object> construct() {
        return new LinkedHashMap<>();
      }
    };
    ObjectConstructor<Map<String, Object>> actualMapConstructor = constructorConstructor.get(new TypeToken<Map<String, Object>>(){});
    assertEquals(expectedMapConstructor, actualMapConstructor);

    // Test for SortedMap
    ObjectConstructor<SortedMap<String, Object>> expectedSortedMapConstructor = new ObjectConstructor<SortedMap<String, Object>>() {
      @Override
      public SortedMap<String, Object> construct() {
        return new TreeMap<>();
      }
    };
    ObjectConstructor<SortedMap<String, Object>> actualSortedMapConstructor = constructorConstructor.get(new TypeToken<SortedMap<String, Object>>(){});
    assertEquals(expectedSortedMapConstructor, actualSortedMapConstructor);
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstance_NoDefaultConstructor() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    Class<String> stringClass = String.class;
    ObjectConstructor<String> expectedStringConstructor = new ObjectConstructor<String>() {
      @Override
      public String construct() {
        return new String();
      }
    };

    try {
      ObjectConstructor<String> actualStringConstructor = constructorConstructor.get(new TypeToken<String>(){});
      fail("Expected a RuntimeException when there's no default constructor for String.");
    } catch (RuntimeException e) {
      // Expected
    }

    // Verify the actual constructor is not equal to the expected one
    assertNotEquals(expectedStringConstructor, actualStringConstructor);
  }

  @Test(expected = RuntimeException.class)
  public void testGetInstance_NoInstanceCreator() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    Class<String[]> stringArrayClass = String[].class;
    ObjectConstructor<String[]> expectedStringArrayConstructor = new ObjectConstructor<String[]>() {
      @Override
      public String[] construct() {
        return new String[0];
      }
    };

    try {
      ObjectConstructor<String[]> actualStringArrayConstructor = constructorConstructor.get(new TypeToken<String[]>(){});
      fail("Expected a RuntimeException when there's no InstanceCreator for String[].");
    } catch (RuntimeException e) {
      // Expected
    }

    // Verify the actual constructor is not equal to the expected one
    assertNotEquals(expectedStringArrayConstructor, actualStringArrayConstructor);
  }

  @Test
  public void testToString() {
    Map<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);

    assertEquals("{...}", constructorConstructor.toString());
  }
}

class ObjectConstructor<T> {
  public abstract T construct();
}

class TypeCollection {
  // Empty implementation for testing
}

class TypeCollectionTypeAdapter implements InstanceCreator<Collection<?>> {
  @Override
  public Collection<?> createInstance(Type type) {
    return new ArrayList<>();
  }
}

class TypeEnumSet {
  // Empty implementation for testing
}

class TypeEnumSetTypeAdapter implements InstanceCreator<EnumSet<?>> {
  @Override
  public EnumSet<?> createInstance(Type type) {
    if (type instanceof ParameterizedType) {
      Type elementType = ((ParameterizedType) type).getActualTypeArguments()[0];
      if (elementType instanceof Class) {
        return EnumSet.noneOf((Class)elementType);
      } else {
        throw new IllegalArgumentException("Invalid EnumSet type: " + type.toString());
      }
    } else {
      throw new IllegalArgumentException("Invalid EnumSet type: " + type.toString());
    }
  }
}

class UnsafeAllocator {
  private static final Unsafe UNSAFE;

  static {
    try {
      UNSAFE = sun.misc.Unsafe.getUnsafe();
    } catch (SecurityException e) {
      throw new RuntimeException("Unable to access Unsafe.", e);
    }
  }

  public static UnsafeAllocator create() {
    return new UnsafeAllocator();
  }

  private UnsafeAllocator() {}

  public Object newInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException,
      InvocationTargetException {
    try {
      return UNSAFE.allocateInstance(clazz);
    } catch (InstantiationException e) {
      throw e;
    } catch (IllegalAccessException e) {
      throw e;
    } catch (InvocationTargetException ite) {
      throw ite.getCause();
    }
  }
}