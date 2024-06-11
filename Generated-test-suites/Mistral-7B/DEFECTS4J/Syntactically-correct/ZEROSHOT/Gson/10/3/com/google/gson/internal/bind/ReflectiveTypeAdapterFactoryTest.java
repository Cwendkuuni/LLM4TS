import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.FieldNamingStrategy;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ReflectiveTypeAdapterFactoryTest {

  private Gson gson;
  private ConstructorConstructor constructorConstructor;
  private FieldNamingStrategy fieldNamingPolicy;
  private Excluder excluder;
  private ReflectiveTypeAdapterFactory factory;

  @Before
  public void setUp() {
    initMocks(this);
    gson = mock(Gson.class);
    constructorConstructor = mock(ConstructorConstructor.class);
    fieldNamingPolicy = mock(FieldNamingStrategy.class);
    excluder = mock(Excluder.class);
    when(gson.getAdapter(any(Type.class))).thenReturn(mock(TypeAdapter.class));
    when(gson.newBuilder()).thenReturn(gson);
    factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
  }

  @Test
  public void testCreate_NullTypeToken_ReturnsNull() {
    assertNull(factory.create(gson, null));
  }

  @Test
  public void testCreate_PrimitiveType_ReturnsNull() {
    assertNull(factory.create(gson, new TypeToken<Integer>() {}));
  }

  @Test
  public void testCreate_ObjectType_ReturnsAdapterInstance() {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
    ObjectConstructor<TestClass> constructor = mock(ObjectConstructor.class);
    when(constructorConstructor.get(typeToken)).thenReturn(constructor);

    TypeAdapter<TestClass> adapter = factory.create(gson, typeToken);
    assertNotNull(adapter);
  }

  @Test
  public void testExcludeField_ExcludedField_ReturnsFalse() {
    Field field = mock(Field.class);
    when(excluder.excludeField(field, true)).thenReturn(true);

    assertFalse(factory.excludeField(field, true));
  }

  @Test
  public void testExcludeField_SerializedField_ReturnsFalse() {
    Field field = mock(Field.class);
    when(excluder.excludeClass(field.getType(), true)).thenReturn(true);

    assertFalse(factory.excludeField(field, true));
  }

  @Test
  public void testExcludeField_DeserializedField_ReturnsFalse() {
    Field field = mock(Field.class);
    when(excluder.excludeField(field, false)).thenReturn(true);

    assertFalse(factory.excludeField(field, false));
  }

  @Test
  public void testExcludeField_NonExcludedField_ReturnsTrue() {
    Field field = mock(Field.class);

    assertTrue(factory.excludeField(field, true));
  }

  @Test
  public void testGetBoundFields_Interface_ReturnsEmptyMap() {
    Class<?> raw = String.class;
    TypeToken<?> type = new TypeToken<Object>() {};

    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields =
        factory.getBoundFields(gson, type, raw);

    assertEquals(Collections.emptyMap(), boundFields);
  }

  @Test
  public void testGetBoundFields_ClassWithFields_ReturnsMapWithBoundFields() {
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
    Class<?> raw = TestClass.class;

    Field field1 = mock(Field.class);
    when(field1.getName()).thenReturn("field1");
    when(field1.getType()).thenReturn(String.class);
    when(field1.getGenericType()).thenReturn(String.class);
    when(field1.isAnnotationPresent(JsonAdapter.class)).thenReturn(false);

    Field field2 = mock(Field.class);
    when(field2.getName()).thenReturn("field2");
    when(field2.getType()).thenReturn(Integer.class);
    when(field2.getGenericType()).thenReturn(Integer.class);
    when(field2.isAnnotationPresent(JsonAdapter.class)).thenReturn(true);
    when(field2.getAnnotation(JsonAdapter.class)).thenReturn(new JsonAdapter() {});

    List<String> fieldNames1 = factory.getFieldNames(field1);
    assertEquals(Collections.singletonList("field1"), fieldNames1);

    List<String> fieldNames2 = factory.getFieldNames(field2);
    assertEquals(Collections.singletonList("field2"), fieldNames2);

    Map<String, ReflectiveTypeAdapterFactory.BoundField> expectedBoundFields = new LinkedHashMap<>();
    expectedBoundFields.put("field1", createBoundField("field1", String.class, true, true));
    expectedBoundFields.put("field2", createBoundField("field2", Integer.class, true, true));

    TypeAdapter<?> jsonAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(eq(Integer.class))).thenReturn(jsonAdapter);

    when(constructorConstructor.get(typeToken)).thenReturn(mock(ObjectConstructor.class));

    Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = factory.getBoundFields(gson, typeToken, raw);

    assertEquals(expectedBoundFields, boundFields);
  }

  private ReflectiveTypeAdapterFactory.BoundField createBoundField(String name, Class<?> fieldType,
      boolean serialize, boolean deserialize) {
    return new ReflectiveTypeAdapterFactory.BoundField(name, serialize, deserialize) {};
  }

  @Test
  public void testCreateAdapter_Read_ReadsValueFromReaderAndSetsFieldValue() throws IOException {
    TestClass testClass = new TestClass();
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
    ObjectConstructor<TestClass> constructor = mock(ObjectConstructor.class);
    when(constructor.construct()).thenReturn(testClass);

    Field field = mock(Field.class);
    when(field.getName()).thenReturn("field1");
    when(field.getType()).thenReturn(String.class);
    when(field.getGenericType()).thenReturn(String.class);
    when(field.isAnnotationPresent(JsonAdapter.class)).thenReturn(false);

    List<String> fieldNames = factory.getFieldNames(field);
    assertEquals(Collections.singletonList("field1"), fieldNames);

    String json = "{\"field1\": \"value\"}";

    JsonReader jsonReader = new JsonReader(new StringReader(json));
    jsonReader.setLenient(true);

    ReflectiveTypeAdapterFactory.Adapter<TestClass> adapter =
        (ReflectiveTypeAdapterFactory.Adapter<TestClass>) factory.create(gson, typeToken);

    adapter.read(jsonReader, testClass);

    assertEquals("value", field.get(testClass));
  }

  @Test
  public void testCreateAdapter_Write_WritesValueToWriterFromFieldValue() throws IOException {
    TestClass testClass = new TestClass();
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
    ObjectConstructor<TestClass> constructor = mock(ObjectConstructor.class);
    when(constructor.construct()).thenReturn(testClass);

    Field field = mock(Field.class);
    when(field.getName()).thenReturn("field1");
    when(field.getType()).thenReturn(String.class);
    when(field.getGenericType()).thenReturn(String.class);
    when(field.isAnnotationPresent(JsonAdapter.class)).thenReturn(false);

    List<String> fieldNames = factory.getFieldNames(field);
    assertEquals(Collections.singletonList("field1"), fieldNames);

    String expectedJson = "{\"field1\": \"value\"}";

    JsonWriter jsonWriter = new JsonWriter(new StringWriter());
    ReflectiveTypeAdapterFactory.Adapter<TestClass> adapter =
        (ReflectiveTypeAdapterFactory.Adapter<TestClass>) factory.create(gson, typeToken);

    field.set(testClass, "value");
    adapter.write(jsonWriter, testClass);

    assertEquals(expectedJson, jsonWriter.toString());
  }

  @Test
  public void testReadAndWrite_FieldWithJsonAdapterAnnotation_UsesJsonAdapter() throws IOException {
    TestClass testClass = new TestClass();
    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
    ObjectConstructor<TestClass> constructor = mock(ObjectConstructor.class);
    when(constructor.construct()).thenReturn(testClass);

    Field field = mock(Field.class);
    when(field.getName()).thenReturn("field2");
    when(field.getType()).thenReturn(Integer.class);
    when(field.getGenericType()).thenReturn(Integer.class);
    when(field.isAnnotationPresent(JsonAdapter.class)).thenReturn(true);
    when(field.getAnnotation(JsonAdapter.class)).thenReturn(new JsonAdapter() {});

    List<String> fieldNames = factory.getFieldNames(field);
    assertEquals(Collections.singletonList("field2"), fieldNames);

    int value = 123;

    String json = "{\"field2\": 456}";

    JsonReader jsonReader = new JsonReader(new StringReader(json));
    jsonReader.setLenient(true);

    ReflectiveTypeAdapterFactory.Adapter<TestClass> adapter =
        (ReflectiveTypeAdapterFactory.Adapter<TestClass>) factory.create(gson, typeToken);

    adapter.read(jsonReader, testClass);

    assertEquals(456, field.get(testClass));

    field.set(testClass, value);
    adapter.write(new JsonWriter(new StringWriter()), testClass);

    assertEquals("{\"field2\": " + value + "}", json);
  }
}

class TestClass {
  private String field1;
  private int field2;
}