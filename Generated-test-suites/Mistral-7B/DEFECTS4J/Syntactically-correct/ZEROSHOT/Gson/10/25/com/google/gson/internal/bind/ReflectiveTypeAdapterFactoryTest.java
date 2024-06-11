import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.FieldNamingPolicy;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReflectiveTypeAdapterFactoryTest {

  private ReflectiveTypeAdapterFactory underTest;
  private Excluder excluder;
  private FieldNamingPolicy fieldNamingPolicy;
  private ObjectConstructor<?> constructorConstructor;

  @Before
  public void setUp() {
    excluder = mock(Excluder.class);
    fieldNamingPolicy = mock(FieldNamingPolicy.class);
    constructorConstructor = mock(ObjectConstructor.class);

    underTest = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);
  }

  @Test
  public void testCreateTypeAdapterForSimpleClass() {
    Class simpleClass = new TestSimpleClass();
    TypeToken<TestSimpleClass> typeToken = TypeToken.get(simpleClass);

    TypeAdapter<TestSimpleClass> adapter = underTest.create(mock(Gson.class), typeToken);

    assertNotNull(adapter);
  }

  @Test
  public void testCreateTypeAdapterForInterface() {
    Class interfaceClass = new TestInterface();
    TypeToken<TestInterface> typeToken = TypeToken.get(interfaceClass);

    TypeAdapter<TestInterface> adapter = underTest.create(mock(Gson.class), typeToken);

    assertNull(adapter);
  }

  @Test
  public void testWriteAndReadObjectWithDefaultFieldName() throws IOException {
    Class simpleClass = new TestSimpleClass();
    TypeToken<TestSimpleClass> typeToken = TypeToken.get(simpleClass);
    TypeAdapter<TestSimpleClass> adapter = underTest.create(mock(Gson.class), typeToken);

    TestSimpleClass value = new TestSimpleClass("test");

    String json = "{\"test\":\"value\"}";

    JsonWriter writer = mock(JsonWriter.class);
    when(writer.value(anyString())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    adapter.write(writer, value);

    JsonReader reader = mock(JsonReader.class);
    when(reader.nextString()).thenReturn("value");

    TestSimpleClass deserializedValue = adapter.read(reader);

    assertEquals(value, deserializedValue);
  }

  @Test
  public void testWriteAndReadObjectWithCustomFieldName() throws IOException {
    Class simpleClass = new TestSimpleClassWithCustomFieldName();
    TypeToken<TestSimpleClassWithCustomFieldName> typeToken = TypeToken.get(simpleClass);
    TypeAdapter<TestSimpleClassWithCustomFieldName> adapter = underTest.create(mock(Gson.class), typeToken);

    TestSimpleClassWithCustomFieldName value = new TestSimpleClassWithCustomFieldName("test");

    String json = "{\"field\":\"value\"}";

    JsonWriter writer = mock(JsonWriter.class);
    when(writer.name("field")).thenReturn(writer);
    when(writer.value(anyString())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    adapter.write(writer, value);

    JsonReader reader = mock(JsonReader.class);
    when(reader.nextString()).thenReturn("value");

    TestSimpleClassWithCustomFieldName deserializedValue = adapter.read(reader);

    assertEquals(value, deserializedValue);
  }

  @Test
  public void testWriteAndReadObjectWithCustomFieldNameAndAlternateAnnotations() throws IOException {
    Class simpleClass = new TestSimpleClassWithCustomFieldNameAndAlternateAnnotations();
    TypeToken<TestSimpleClassWithCustomFieldNameAndAlternateAnnotations> typeToken = TypeToken.get(simpleClass);
    TypeAdapter<TestSimpleClassWithCustomFieldNameAndAlternateAnnotations> adapter = underTest.create(mock(Gson.class), typeToken);

    TestSimpleClassWithCustomFieldNameAndAlternateAnnotations value = new TestSimpleClassWithCustomFieldNameAndAlternateAnnotations("test");

    String json = "{\"alternateField\":\"value\"}";

    JsonWriter writer = mock(JsonWriter.class);
    when(writer.name("alternateField")).thenReturn(writer);
    when(writer.value(anyString())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    adapter.write(writer, value);

    JsonReader reader = mock(JsonReader.class);
    when(reader.nextString()).thenReturn("value");

    TestSimpleClassWithCustomFieldNameAndAlternateAnnotations deserializedValue = adapter.read(reader);

    assertEquals(value, deserializedValue);
  }

  @Test
  public void testWriteAndReadObjectWithJsonAdapter() throws IOException {
    Class simpleClass = new TestSimpleClassWithJsonAdapter();
    TypeToken<TestSimpleClassWithJsonAdapter> typeToken = TypeToken.get(simpleClass);
    TypeAdapter<TestSimpleClassWithJsonAdapter> jsonAdapter = mock(TypeAdapter.class);

    when(jsonAdapter.write(any(), any()))
        .thenAnswer(invocationOnMock -> "{\"customField\":\"value\"}");

    when(jsonAdapter.read(any()))
        .thenAnswer(invocationOnMock -> new TestSimpleClassWithJsonAdapter("value"));

    when(underTest.getTypeAdapter(any(), any(), any(), any()))
        .thenReturn(jsonAdapter);

    TypeAdapter<TestSimpleClassWithJsonAdapter> adapter = underTest.create(mock(Gson.class), typeToken);

    TestSimpleClassWithJsonAdapter value = new TestSimpleClassWithJsonAdapter("test");

    String json = "{\"customField\":\"value\"}";

    JsonWriter writer = mock(JsonWriter.class);
    when(writer.value(anyString())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    adapter.write(writer, value);

    JsonReader reader = mock(JsonReader.class);
    when(reader.nextString()).thenReturn("value");

    TestSimpleClassWithJsonAdapter deserializedValue = adapter.read(reader);

    assertEquals(value, deserializedValue);
  }

  @Test
  public void testExcludeFieldForSerialization() {
    Class simpleClass = new TestSimpleClassWithExcludedFieldForSerialization();
    TypeToken<TestSimpleClassWithExcludedFieldForSerialization> typeToken = TypeToken.get(simpleClass);
    TypeAdapter<TestSimpleClassWithExcludedFieldForSerialization> adapter = underTest.create(mock(Gson.class), typeToken);

    TestSimpleClassWithExcludedFieldForSerialization value = new TestSimpleClassWithExcludedFieldForSerialization("test");

    assertFalse(value.isFieldSerialized());

    JsonWriter writer = mock(JsonWriter.class);

    adapter.write(writer, value);

    verify(writer, never()).value(anyString());
  }

  @Test
  public void testExcludeFieldForDeserialization() {
    Class simpleClass = new TestSimpleClassWithExcludedFieldForDeserialization();
    TypeToken<TestSimpleClassWithExcludedFieldForDeserialization> typeToken = TypeToken.get(simpleClass);
    TypeAdapter<TestSimpleClassWithExcludedFieldForDeserialization> adapter = underTest.create(mock(Gson.class), typeToken);

    TestSimpleClassWithExcludedFieldForDeserialization value = new TestSimpleClassWithExcludedFieldForDeserialization();

    JsonReader reader = mock(JsonReader.class);
    when(reader.nextString()).thenReturn("value");

    adapter.read(reader);

    verify(reader, never()).nextName();
  }

  @Test
  public void testExcludeClassForSerialization() {
    Class simpleClass = new TestSimpleClassWithExcludedClassForSerialization();
    TypeToken<TestSimpleClassWithExcludedClassForSerialization> typeToken = TypeToken.get(simpleClass);
    TypeAdapter<TestSimpleClassWithExcludedClassForSerialization> adapter = underTest.create(mock(Gson.class), typeToken);

    TestSimpleClassWithExcludedClassForSerialization value = new TestSimpleClassWithExcludedClassForSerialization();

    JsonWriter writer = mock(JsonWriter.class);

    adapter.write(writer, value);

    verify(writer, never()).value(anyString());
  }

  @Test
  public void testExcludeClassForDeserialization() {
    Class simpleClass = new TestSimpleClassWithExcludedClassForDeserialization();
    TypeToken<TestSimpleClassWithExcludedClassForDeserialization> typeToken = TypeToken.get(simpleClass);
    TypeAdapter<TestSimpleClassWithExcludedClassForDeserialization> adapter = underTest.create(mock(Gson.class), typeToken);

    TestSimpleClassWithExcludedClassForDeserialization value = new TestSimpleClassWithExcludedClassForDeserialization();

    JsonReader reader = mock(JsonReader.class);

    adapter.read(reader);

    verify(reader, never()).nextString();
  }

  private static class TestSimpleClass {
    String field;

    TestSimpleClass(String field) {
      this.field = field;
    }

    String getField() {
      return field;
    }

    void setField(String field) {
      this.field = field;
    }

    boolean isFieldSerialized() {
      return true;
    }
  }

  private static class TestSimpleClassWithCustomFieldName {
    @SerializedName("field")
    String field;

    TestSimpleClassWithCustomFieldName(String field) {
      this.field = field;
    }

    boolean isFieldSerialized() {
      return true;
    }
  }

  private static class TestSimpleClassWithCustomFieldNameAndAlternateAnnotations {
    @SerializedName("field")
    String field;

    @SerializedName("alternateField")
    String alternateField;

    TestSimpleClassWithCustomFieldNameAndAlternateAnnotations(String field) {
      this.field = field;
    }

    boolean isFieldSerialized() {
      return true;
    }
  }

  private static class TestSimpleClassWithJsonAdapter {
    @JsonAdapter(TypeAdapter.class)
    String customField;

    TestSimpleClassWithJsonAdapter(String customField) {
      this.customField = customField;
    }

    boolean isFieldSerialized() {
      return false;
    }
  }

  private static class TestSimpleClassWithExcludedFieldForSerialization {
    @Exclude
    String field;

    TestSimpleClassWithExcludedFieldForSerialization(String field) {
      this.field = field;
    }

    boolean isFieldSerialized() {
      return false;
    }
  }

  private static class TestSimpleClassWithExcludedFieldForDeserialization {
    @Exclude
    static String field;

    TestSimpleClassWithExcludedFieldForDeserialization() {
    }

    boolean isFieldSerialized() {
      return true;
    }
  }

  private static class TestSimpleClassWithExcludedClassForSerialization {
    static class InnerClass {
      String field;

      InnerClass(String field) {
        this.field = field;
      }

      boolean isFieldSerialized() {
        return true;
      }
    }

    InnerClass innerField;

    TestSimpleClassWithExcludedClassForSerialization() {
    }

    boolean isFieldSerialized() {
      return false;
    }
  }

  private static class TestSimpleClassWithExcludedClassForDeserialization {
    static class InnerClass {
      String field;

      InnerClass(String field) {
        this.field = field;
      }

      boolean isFieldSerialized() {
        return true;
      }
    }

    InnerClass innerField;

    TestSimpleClassWithExcludedClassForDeserialization() {
    }

    boolean isFieldSerialized() {
      return true;
    }
  }
}