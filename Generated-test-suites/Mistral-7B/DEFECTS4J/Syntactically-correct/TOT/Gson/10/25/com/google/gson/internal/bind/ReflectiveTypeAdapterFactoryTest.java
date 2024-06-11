import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Bindings;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.FieldNamingPolicy;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.ReflectiveTypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ReflectiveTypeAdapterFactoryTest {

  private static final String JSON_DESERIALIZATION_TEST =
      "{\"name\":\"John Doe\", \"age\":42, \"address\":{\"city\":\"New York\", \"state\":\"NY\"}}";
  private static final String JSON_SERIALIZATION_TEST =
      "{\"name\":\"John Doe\", \"age\":42, \"address\":{\"city\":\"New York\", \"state\":\"NY\"}}";

  private ReflectiveTypeAdapterFactory factory;
  private ObjectConstructor<Person> constructor;
  private Person person;
  private Person personWithoutAge;
  private Person personWithNullAddress;
  private Person personWithNullName;
  private Person personWithEmptyName;
  private Person personWithNoFields;
  private JsonObject jsonObject;
  private JsonReader jsonReader;
  private JsonWriter jsonWriter;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    Gson gson = new GsonBuilder()
        .setExcluder(Excluder.create())
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .create();
    factory = new ReflectiveTypeAdapterFactory(
        new ObjectConstructor<Person>() {
          @Override
          public Person construct() {
            return new Person();
          }
        }, new FieldNamingPolicy() {
          @Override
          public String translateName(Field field) {
            return field.getName();
          }
        }, Excluder.create());
    constructor = Mockito.spy(new ObjectConstructor<Person>() {
      @Override
      public Person construct() {
        person = new Person();
        return person;
      }
    });
    jsonReader = Mockito.spy(new JsonReader(new ByteArrayInputStream(JSON_DESERIALIZATION_TEST.getBytes())));
    jsonWriter = Mockito.spy(new JsonWriter(new OutputStreamWriter(new ByteArrayOutputStream())));
  }

  @Test
  public void testDeserialize() throws IOException {
    TypeAdapter<Person> typeAdapter = factory.create(gson, TypeToken.get(Person.class));
    Person deserializedPerson = typeAdapter.read(jsonReader);
    assertEquals("John Doe", deserializedPerson.name);
    assertEquals(42, deserializedPerson.age);
  }

  @Test
  public void testDeserializeWithNullJson() throws IOException {
    TypeAdapter<Person> typeAdapter = factory.create(gson, TypeToken.get(Person.class));
    Person deserializedPerson = typeAdapter.read(jsonReader);
    assertNull(deserializedPerson);
  }

  @Test
  public void testDeserializeWithEmptyJson() throws IOException {
    TypeAdapter<Person> typeAdapter = factory.create(gson, TypeToken.get(Person.class));
    JsonObject jsonObject = new JsonObject();
    Person deserializedPerson = typeAdapter.read(jsonReader);
    assertNull(deserializedPerson);
  }

  @Test
  public void testDeserializeWithEmptyObject() throws IOException {
    TypeAdapter<Person> typeAdapter = factory.create(gson, TypeToken.get(Person.class));
    Person deserializedPerson = typeAdapter.read(jsonReader);
    assertNotNull(deserializedPerson);
    assertEquals(0, deserializedPerson.name.length());
  }

  @Test
  public void testDeserializeWithMissingField() throws IOException {
    TypeAdapter<Person> typeAdapter = factory.create(gson, TypeToken.get(Person.class));
    Person deserializedPerson = typeAdapter.read(new JsonReader(new ByteArrayInputStream(JSON_DESERIALIZATION_TEST.getBytes())) {
      @Override
      public JsonElement peek() throws IOException {
        throw new IllegalStateException("peek() not supported");
      }
    });
    assertNotNull(deserializedPerson);
    assertFalse(deserializedPerson.hasAge());
  }

  @Test
  public void testDeserializeWithNullField() throws IOException {
    TypeAdapter<Person> typeAdapter = factory.create(gson, TypeToken.get(Person.class));
    Person deserializedPerson = typeAdapter.read(jsonReader);
    assertNull(deserializedPerson.address);
  }

  @Test
  public void testDeserializeWithNullName() {
    personWithoutName = new Person();
    personWithoutName.name = null;

    TypeAdapter<Person> typeAdapter = factory.create(gson, TypeToken.get(Person.class));
    Person deserializedPerson = typeAdapter.read(jsonReader);
    assertEquals(personWithoutName, deserializedPerson);
  }

  @Test
  public void testDeserializeWithEmptyName() {
    personWithEmptyName = new Person();
    personWithEmptyName.name = "";

    TypeAdapter<Person> typeAdapter = factory.create(gson, TypeToken.get(Person.class));
    Person deserializedPerson = typeAdapter.read(jsonReader);
    assertEquals(personWithEmptyName, deserializedPerson);
  }

  @Test
  public void testDeserializeWithNoFields() {
    personWithNoFields = new Person();

    TypeAdapter<Person> typeAdapter = factory.create(gson, TypeToken.get(Person.class));
    Person deserializedPerson = typeAdapter.read(jsonReader);
    assertEquals(personWithNoFields, deserializedPerson);
  }

  @Test
  public void testSerialize() throws IOException {
    TypeAdapter<Person> typeAdapter = factory.create(gson, TypeToken.get(Person.class));

    Person personToSerialize = new Person();
    personToSerialize.name = "John Doe";
    personToSerialize.age = 42;
    personToSerialize.address = new Address();
    personToSerialize.address.city = "New York";
    personToSerialize.address.state = "NY";

    jsonWriter.beginObject();
    typeAdapter.write(jsonWriter, personToSerialize);
    jsonWriter.endObject();

    String jsonString = new String(new ByteArrayOutputStream(jsonWriter.getBuffer()).toByteArray());
    assertEquals(JSON_SERIALIZATION_TEST, jsonString);
  }

  @Test
  public void testSerializeWithNullJsonWriter() {
    TypeAdapter<Person> typeAdapter = factory.create(gson, TypeToken.get(Person.class));
    assertThrows(IllegalArgumentException.class, () -> typeAdapter.write(null, person));
  }

  @Test
  public void testSerializeWithNullPerson() {
    TypeAdapter<Person> typeAdapter = factory.create(gson, TypeToken.get(Person.class));
    assertThrows(NullPointerException.class, () -> typeAdapter.write(jsonWriter, null));
  }

  @Test
  public void testSerializeWithNullName() {
    personWithoutName = new Person();
    personWithoutName.name = null;

    TypeAdapter<Person> typeAdapter = factory.create(gson, TypeToken.get(Person.class));
    jsonWriter.beginObject();
    typeAdapter.write(jsonWriter, personWithoutName);
    jsonWriter.endObject();

    String jsonString = new String(new ByteArrayOutputStream(jsonWriter.getBuffer()).toByteArray());
    assertEquals("{}", jsonString);
  }

  @Test
  public void testSerializeWithEmptyName() {
    personWithEmptyName = new Person();
    personWithEmptyName.name = "";

    TypeAdapter<Person> typeAdapter = factory.create(gson, TypeToken.get(Person.class));
    jsonWriter.beginObject();
    typeAdapter.write(jsonWriter, personWithEmptyName);
    jsonWriter.endObject();

    String jsonString = new String(new ByteArrayOutputStream(jsonWriter.getBuffer()).toByteArray());
    assertEquals("{\"name\":\"\"}", jsonString);
  }

  @Test
  public void testSerializeWithNoFields() {
    personWithNoFields = new Person();

    TypeAdapter<Person> typeAdapter = factory.create(gson, TypeToken.get(Person.class));
    jsonWriter.beginObject();
    typeAdapter.write(jsonWriter, personWithNoFields);
    jsonWriter.endObject();

    String jsonString = new String(new ByteArrayOutputStream(jsonWriter.getBuffer()).toByteArray());
    assertEquals("{}", jsonString);
  }

  @Test
  public void testSetFieldWithDeserializedValue() throws IOException, IllegalAccessException {
    Map<String, Field> fields = new HashMap<>();
    fields.put("name", getField(Person.class, "name"));
    fields.put("age", getField(Person.class, "age"));
    fields.put("address", getField(Person.class, "address"));

    Person deserializedPerson = factory.create(gson, TypeToken.get(Person.class)).read(jsonReader);

    for (Field field : fields.values()) {
      field.setAccessible(true);
      field.set(person, deserializedPerson.getClass().getDeclaredField(field.getName()).get(deserializedPerson));
    }

    assertEquals("John Doe", person.name);
    assertEquals(42, person.age);
    assertNotNull(person.address);
  }

  @Test
  public void testSetFieldWithNullDeserializedValue() throws IOException, IllegalAccessException {
    Map<String, Field> fields = new HashMap<>();
    fields.put("name", getField(Person.class, "name"));
    fields.put("age", getField(Person.class, "age"));
    fields.put("address", getField(Person.class, "address"));

    Person deserializedPerson = factory.create(gson, TypeToken.get(Person.class)).read(new JsonReader(new ByteArrayInputStream(new byte[0])) {
      @Override
      public JsonElement peek() throws IOException {
        throw new IllegalStateException("peek() not supported");
      }
    });

    for (Field field : fields.values()) {
      field.setAccessible(true);
      field.set(person, deserializedPerson == null ? null : deserializedPerson.getClass().getDeclaredField(field.getName()).get(deserializedPerson));
    }

    assertNull(person.name);
    assertFalse(person.hasAge());
    assertNull(person.address);
  }

  private Field getField(Class<?> clazz, String fieldName) {
    Field field = null;
    for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
      try {
        field = c.getDeclaredField(fieldName);
        field.setAccessible(true);
        break;
      } catch (NoSuchFieldException e) {
        // do nothing
      }
    }
    if (field == null) {
      throw new IllegalStateException("Could not find field " + fieldName + " in " + clazz.getName());
    }
    return field;
  }

  private static class Address {
    String city;
    String state;
  }

  private static class Person {
    String name;
    int age;
    Address address;

    boolean hasAge() {
      return age > 0;
    }
  }
}