import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonTreeReader;

public class JsonTreeReaderTest {

  @Test
  public void testJsonTreeReaderEmptyJson() {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
    assertFalse(reader.hasNext());
  }

  @Test
  public void testJsonTreeReaderSingleObject() {
    String json = "{\"name\": \"John\", \"age\": 30}";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override
      public JsonArray getAsJsonArray() {
        return null;
      }

      @Override
      public JsonObject getAsJsonObject() {
        return new JsonObject() {
          {
            addProperty("name", "John");
            addProperty("age", 30);
          }
        };
      }

      @Override
      public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override
      public JsonNull getAsJsonNull() {
        return null;
      }
    }.getAsJsonObject());

    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());
    assertEquals("age", reader.nextName());
    assertEquals(30, reader.nextInt());
    assertFalse(reader.hasNext());
  }

  @Test
  public void testJsonTreeReaderSingleArray() {
    String json = "[1, 2, 3]";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override
      public JsonArray getAsJsonArray() {
        return new JsonArray() {
          {
            add(1);
            add(2);
            add(3);
          }
        };
      }

      @Override
      public JsonObject getAsJsonObject() {
        return null;
      }

      @Override
      public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override
      public JsonNull getAsJsonNull() {
        return null;
      }
    }.getAsJsonArray());

    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    assertEquals(2, reader.nextInt());
    assertEquals(3, reader.nextInt());
    assertEquals(JsonToken.END_ARRAY, reader.peek().getAsInt());
    reader.endArray();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testJsonTreeReaderNestedObjectsAndArrays() {
    String json = "{\"name\": \"John\", \"age\": 30, \"cities\": [{\"name\": \"New York\", \"population\": 8500000}, {\"name\": \"Los Angeles\", \"population\": 3800000}]}";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override
      public JsonArray getAsJsonArray() {
        return null;
      }

      @Override
      public JsonObject getAsJsonObject() {
        return new JsonObject() {
          {
            addProperty("name", "John");
            addProperty("age", 30);
            addProperty("cities", new JsonArray() {
              {
                add(new JsonObject() {
                  {
                    addProperty("name", "New York");
                    addProperty("population", 8500000);
                  }
                });
                add(new JsonObject() {
                  {
                    addProperty("name", "Los Angeles");
                    addProperty("population", 3800000);
                  }
                });
              }
            });
          }
        };
      }

      @Override
      public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override
      public JsonNull getAsJsonNull() {
        return null;
      }
    }.getAsJsonObject());

    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());
    assertEquals("age", reader.nextName());
    assertEquals(30, reader.nextInt());
    reader.beginArray();
    assertEquals("name", reader.nextName());
    assertEquals("New York", reader.nextString());
    assertEquals("population", reader.nextName());
    assertEquals(8500000, reader.nextInt());
    reader.endArray();
    reader.beginArray();
    assertEquals("name", reader.nextName());
    assertEquals("Los Angeles", reader.nextString());
    assertEquals("population", reader.nextName());
    assertEquals(3800000, reader.nextInt());
    reader.endArray();
    assertFalse(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderClosedReader() {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
    reader.close();
    reader.nextName();
  }
}