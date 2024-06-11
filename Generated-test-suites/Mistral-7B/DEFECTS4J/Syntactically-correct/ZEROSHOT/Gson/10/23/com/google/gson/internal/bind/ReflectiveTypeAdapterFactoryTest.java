import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.FieldNamingPolicy;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.stream.JsonReader;
import com.google.gson.internal.stream.JsonWriter;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReflectiveTypeAdapterFactoryTest {

  private static final String SERIALIZED_NAME_FIELD = "serializedName";
  private static final String ALTERNATE_FIELD_NAME = "alternateName";

  @Test
  public void testCreateTypeAdapter() {
    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setExcluder(new Excluder())
        .create();

    Class<MyClass> myClassType = MyClass.class;
    TypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ObjectConstructor<>(myClassType),
        new FieldNamingPolicy.Standard(),
        new Excluder()
    );

    TypeToken<MyClass> typeToken = TypeToken.get(myClassType);

    TypeAdapter<MyClass> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);

    Assert.assertNotNull(typeAdapter);
  }

  @Test
  public void testExcludeField() {
    Class<MyClass> myClassType = MyClass.class;
    Field fieldToExclude = MyClass.class.getDeclaredField("fieldToExclude");

    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ObjectConstructor<>(myClassType),
        new FieldNamingPolicy.Standard(),
        new Excluder()
    );

    Assert.assertFalse(reflectiveTypeAdapterFactory.excludeField(fieldToExclude, true));
    Assert.assertFalse(reflectiveTypeAdapterFactory.excludeField(fieldToExclude, false));

    Excluder excluder = mock(Excluder.class);
    when(excluder.excludeClass(FieldType.class, true)).thenReturn(false);
    when(excluder.excludeField(fieldToExclude, true)).thenReturn(true);

    reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ObjectConstructor<>(myClassType),
        new FieldNamingPolicy.Standard(),
        excluder
    );

    Assert.assertFalse(reflectiveTypeAdapterFactory.excludeField(fieldToExclude, true));
    Assert.assertTrue(reflectiveTypeAdapterFactory.excludeField(fieldToExclude, false));
  }

  @Test
  public void testGetFieldNames() {
    Class<MyClass> myClassType = MyClass.class;
    Field fieldWithSerializedName = MyClass.class.getDeclaredField("fieldWithSerializedName");
    Field fieldWithAlternateName = MyClass.class.getDeclaredField("fieldWithAlternateName");

    List<String> fieldNames = ReflectiveTypeAdapterFactory.getFieldNames(fieldWithSerializedName);
    List<String> fieldNames1 = ReflectiveTypeAdapterFactory.getFieldNames(fieldWithAlternateName);

    Assert.assertEquals(1, fieldNames.size());
    Assert.assertEquals("fieldWithSerializedName", fieldNames.get(0));

    List<String> fieldNames2 = ReflectiveTypeAdapterFactory.getFieldNames(fieldWithAlternateName);

    Assert.assertEquals(2, fieldNames2.size());
    Assert.assertEquals("fieldWithAlternateName", fieldNames2.get(1));
    Assert.assertEquals("alternateName", fieldNames2.get(0));
  }

  @Test
  public void testCreateBoundField() {
    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setExcluder(new Excluder())
        .create();

    Class<MyClass> myClassType = MyClass.class;
    Field fieldWithSerializedName = MyClass.class.getDeclaredField("fieldWithSerializedName");
    Field fieldWithAlternateName = MyClass.class.getDeclaredField("fieldWithAlternateName");
    TypeToken<?> fieldTypeToken = TypeToken.of(int.class);

    TypeAdapter<Integer> intTypeAdapter = gson.getAdapter(fieldTypeToken);

    BoundField boundFieldWithSerializedName = ReflectiveTypeAdapterFactory.createBoundField(
        gson, fieldWithSerializedName,
        SERIALIZED_NAME_FIELD, fieldTypeToken, true, true
    );

    Assert.assertNotNull(boundFieldWithSerializedName);

    BoundField boundFieldWithAlternateName = ReflectiveTypeAdapterFactory.createBoundField(
        gson, fieldWithAlternateName,
        ALTERNATE_FIELD_NAME, fieldTypeToken, true, true
    );

    Assert.assertNotNull(boundFieldWithAlternateName);
  }

  @Test
  public void testGetBoundFields() {
    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setExcluder(new Excluder())
        .create();

    Class<MyClass> myClassType = MyClass.class;
    Field fieldWithSerializedName = MyClass.class.getDeclaredField("fieldWithSerializedName");
    Field fieldWithAlternateName = MyClass.class.getDeclaredField("fieldWithAlternateName");
    TypeToken<MyClass> typeToken = TypeToken.get(myClassType);

    Map<String, BoundField> boundFields = ReflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, myClassType);

    Assert.assertNotNull(boundFields);
    Assert.assertEquals(2, boundFields.size());

    BoundField boundFieldWithSerializedName = boundFields.get(SERIALIZED_NAME_FIELD);
    BoundField boundFieldWithAlternateName = boundFields.get(ALTERNATE_FIELD_NAME);

    Assert.assertEquals("fieldWithSerializedName", boundFieldWithSerializedName.name);
    Assert.assertEquals(true, boundFieldWithSerializedName.serialized);
    Assert.assertEquals(true, boundFieldWithSerializedName.deserialized);

    Assert.assertEquals("fieldWithAlternateName", boundFieldWithAlternateName.name);
    Assert.assertEquals(true, boundFieldWithAlternateName.serialized);
    Assert.assertEquals(true, boundFieldWithAlternateName.deserialized);
  }

  @Test
  public void testWriteAndReadFields() throws IOException {
    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setExcluder(new Excluder())
        .create();

    Class<MyClass> myClassType = MyClass.class;
    Field fieldWithSerializedName = MyClass.class.getDeclaredField("fieldWithSerializedName");
    Field fieldWithAlternateName = MyClass.class.getDeclaredField("fieldWithAlternateName");
    TypeToken<MyClass> typeToken = TypeToken.get(myClassType);

    MyClass myClass = new MyClass();
    int value = 42;

    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ObjectConstructor<>(myClassType),
        new FieldNamingPolicy.Standard(),
        new Excluder()
    );

    Map<String, BoundField> boundFields = ReflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, myClassType);

    JsonWriter jsonWriter = new JsonWriter(System.out);
    MyClass myClassWritten = reflectiveTypeAdapterFactory.create(gson, typeToken).write(jsonWriter, myClass);

    JsonReader jsonReader = new JsonReader(new StringReader(jsonWriter.toString()));

    MyClass myClassRead = reflectiveTypeAdapterFactory.create(gson, typeToken).read(jsonReader);

    Assert.assertEquals(myClass, myClassWritten);
    Assert.assertEquals(myClass, myClassRead);

    Field valueField = MyClass.class.getDeclaredField("fieldWithSerializedName");
    Field serializedValueField = boundFields.get(SERIALIZED_NAME_FIELD);

    Assert.assertEquals(value, ((Number) valueField.get(myClass)).intValue());
    Assert.assertEquals(value, ((Number) serializedValueField.read(jsonReader, myClassRead)).intValue());

    valueField = MyClass.class.getDeclaredField("fieldWithAlternateName");
    Field alternateValueField = boundFields.get(ALTERNATE_FIELD_NAME);

    Assert.assertNull(valueField.get(myClass));
    Assert.assertNull(alternateValueField.read(jsonReader, myClassRead));

    jsonWriter.value(value);
    jsonReader.beginObject();
    jsonReader.name(ALTERNATE_FIELD_NAME);
    jsonReader.value(value);
    jsonReader.endObject();

    myClassRead = reflectiveTypeAdapterFactory.create(gson, typeToken).read(jsonReader);

    Assert.assertEquals(value, ((Number) alternateValueField.read(jsonReader, myClassRead)).intValue());
  }

  static class MyClass {
    int fieldWithSerializedName;

    @SerializedName("alternateName")
    int fieldWithAlternateName;

    public MyClass() {}
  }
}