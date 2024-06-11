import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RecordTypeTest {

  private static JSTypeRegistry registry;

  static {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeCreation() {
    // Test creating a simple record type.
    Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);
    assertEquals("{name: String, age: Number}", recordType.toString());
  }

  @Test
  public void testRecordTypeSubtyping() {
    // Test that a record type is a subtype of itself.
    Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertEquals(recordType1, recordType2);
    assertEquals(recordType1, recordType1.getGreatestSubtype(recordType2));
    assertEquals(recordType2, recordType1.getGreatestSubtype(recordType2));

    // Test that a record type is a subtype of a record type with the same properties.
    RecordType recordType3 = new RecordType(registry, properties);
    RecordType recordType4 = new RecordType(registry, properties);
    assertEquals(recordType3, recordType4.getGreatestSubtype(recordType3));
    assertEquals(recordType4, recordType3.getGreatestSubtype(recordType4));

    // Test that a record type is a subtype of a record type with additional properties.
    properties.put("gender", new RecordProperty("gender", registry.getStringType()));
    RecordType recordType5 = new RecordType(registry, properties);
    assertEquals(recordType3, recordType5.getGreatestSubtype(recordType3));
    assertEquals(recordType3, recordType5.getGreatestSubtype(recordType4));
    assertEquals(recordType4, recordType5.getGreatestSubtype(recordType3));

    // Test that a record type is not a subtype of a record type with different property types.
    properties.put("name", new RecordProperty("name", registry.getNumberType()));
    RecordType recordType6 = new RecordType(registry, properties);
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE),
        recordType3.getGreatestSubtype(recordType6));
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE),
        recordType6.getGreatestSubtype(recordType3));
  }

  @Test
  public void testRecordTypeFrozenProperty() {
    // Test that a property can't be added to a frozen record type.
    Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);
    recordType.freeze();

    try {
      recordType.defineProperty("gender", registry.getStringType(), false, null);
      fail("Should have thrown IllegalStateException.");
    } catch (IllegalStateException e) {
      // Expected.
    }
  }

  @Test
  public void testRecordTypeToString() {
    Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);
    assertEquals("{name: String, age: Number}", recordType.toString());
  }
}