import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeTest {
  @Test
  public void testRecordType() {
    // Given
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", JSTypeFactory.STRING));
    RecordType recordType = new RecordType(registry, properties);

    // When
    String name = "John Doe";
    JSType value = registry.getNativeType(JSTypeNative.STRING).getInstance(name);

    // Then
    assertEquals(recordType.getPropertyType("name").getInstance(name),
        recordType.get("name"));

    // Given
    RecordProperty newProperty = new RecordProperty("age", JSTypeFactory.NUMBER);
    properties.put("age", newProperty);
    RecordType updatedRecordType = new RecordType(registry, properties);

    // When
    int age = 30;
    JSType newValue = registry.getNativeType(JSTypeNative.NUMBER).getInstance(age);

    // Then
    assertEquals(updatedRecordType.getPropertyType("age").getInstance(age),
        updatedRecordType.get("age"));
  }

  @Test
  public void testIsEquivalentTo() {
    // Given
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", JSTypeFactory.STRING));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", JSTypeFactory.STRING));
    properties2.put("age", new RecordProperty("age", JSTypeFactory.NUMBER));
    RecordType recordType2 = new RecordType(registry, properties2);

    // When
    boolean result = recordType1.isEquivalentTo(recordType2);

    // Then
    assertFalse(result);
  }

  @Test
  public void testIsSubtype() {
    // Given
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", JSTypeFactory.STRING));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", JSTypeFactory.STRING));
    properties2.put("age", new RecordProperty("age", JSTypeFactory.NUMBER));
    RecordType recordType2 = new RecordType(registry, properties2);

    // When
    boolean result = recordType1.isSubtype(recordType2);

    // Then
    assertFalse(result);
  }
}