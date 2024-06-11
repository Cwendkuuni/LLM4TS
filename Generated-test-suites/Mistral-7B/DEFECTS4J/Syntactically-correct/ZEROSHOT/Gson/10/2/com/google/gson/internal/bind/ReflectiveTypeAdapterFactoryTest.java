import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReflectiveTypeAdapterFactoryTest {

  private Gson gson;
  private ConstructorConstructor constructorConstructor;
  private FieldNamingStrategy fieldNamingPolicy;
  private Excluder excluder;
  private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

  @Before
  public void setUp() {
    gson = mock(Gson.class);
    constructorConstructor = mock(ConstructorConstructor.class);
    fieldNamingPolicy = mock(FieldNamingStrategy.class);
    excluder = mock(Excluder.class);
    reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
  }

  @Test
  public void testCreateNull() {
    TypeToken<?> type = null;
    Assert.assertNull(reflectiveTypeAdapterFactory.create(gson, type));
  }

  @Test
  public void testCreateForPrimitive() {
    TypeToken<Integer> type = new TypeToken<Integer>() {};
    Assert.assertNull(reflectiveTypeAdapterFactory.create(gson, type));
  }

  @Test
  public void testCreateForCustomClass() throws Exception {
    class TestClass {
      @SerializedName("test")
      private String testField;

      @JsonAdapter(Adapter.class)
      private Adapter adapterField;
    }

    class Adapter {}

    TypeToken<TestClass> type = new TypeToken<TestClass>() {};
    ObjectConstructor<TestClass> constructor = mock(ObjectConstructor.class);
    when(constructorConstructor.get(type)).thenReturn(constructor);

    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new LinkedHashMap<>();

    Field testField = TestClass.class.getDeclaredField("testField");
    ReflectiveTypeAdapterFactory.BoundField testFieldBoundField = createBoundField("testField", boundFields);
    boundFields.put("testField", testFieldBoundField);

    Field adapterField = TestClass.class.getDeclaredField("adapterField");
    ReflectiveTypeAdapterFactory.BoundField adapterFieldBoundField = createBoundField("adapterField", boundFields);
    boundFields.put("adapterField", adapterFieldBoundField);

    when(fieldNamingPolicy.translateName(testField)).thenReturn("testField");
    when(excluder.excludeClass(testField.getType(), true)).thenReturn(false);
    when(excluder.excludeField(testField, true)).thenReturn(false);
    when(excluder.excludeClass(Adapter.class, false)).thenReturn(true);
    when(excluder.excludeField(testField, false)).thenReturn(false);

    when(gson.getAdapter(TypeToken.get(testField.getType()))).thenReturn(mock(TypeAdapter.class));

    TypeAdapter<TestClass> testAdapter = reflectiveTypeAdapterFactory.create(gson, type);

    Assert.assertNotNull(testAdapter);
  }

  private ReflectiveTypeAdapterFactory.BoundField createBoundField(String fieldName, Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields) {
    Class<?> fieldType = String.class;
    boolean serialize = true;
    boolean deserialize = true;

    Field field = mock(Field.class);
    when(field.getName()).thenReturn(fieldName);
    when(field.getType()).thenReturn(fieldType);

    return new ReflectiveTypeAdapterFactory.BoundField(fieldName, serialize, deserialize) {
      @Override
      public boolean writeField(Object value) throws IOException, IllegalAccessException {
        return false;
      }
    };
  }
}