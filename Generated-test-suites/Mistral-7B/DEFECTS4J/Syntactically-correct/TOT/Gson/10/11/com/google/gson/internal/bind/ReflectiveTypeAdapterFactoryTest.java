import com.google.gson.*;
import com.google.gson.internal.reflect.TypeAdapterRuntimeTypeWrapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

  @Test
  public void testReflectiveTypeAdapterFactory() throws Exception {
    // Given
    Gson gson = new GsonBuilder().create();
    ReflectiveTypeAdapterFactory typeAdapterFactory = new ReflectiveTypeAdapterFactory(
        new ConstructorConstructor(),
        new FieldNamingPolicy(),
        new Excluder()
    );

    // When
    MyData data = new MyData("John Doe", 30, new Address("New York", "NY"));
    TypeAdapter<MyData> adapter = typeAdapterFactory.create(gson, new TypeToken<MyData>() {});

    // Then - write test
    String json = adapter.toJson(data);
    MyData deserializedData1 = adapter.fromJson(json);
    assertEquals(data, deserializedData1);

    // Then - read test
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    MyData deserializedData2 = gson.fromJson(jsonReader, MyData.class);
    assertEquals(data, deserializedData2);

    // When - failure test
    MyData dataWithNullAddress = new MyData("John Doe", 30, null);
    assertThrows(JsonSyntaxException.class, () -> adapter.write(new JsonWriter(System.out), dataWithNullAddress));
  }

  // MyData class
  static class MyData {
    private String name;
    private int age;
    private Address address;

    public MyData(String name, int age, Address address) {
      this.name = name;
      this.age = age;
      this.address = address;
    }

    public String getName() {
      return name;
    }

    public int getAge() {
      return age;
    }

    public Address getAddress() {
      return address;
    }

    static class Address {
      private String city;
      private String state;

      public Address(String city, String state) {
        this.city = city;
        this.state = state;
      }

      public String getCity() {
        return city;
      }

      public String getState() {
        return state;
      }
    }
  }
}