import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordProperty prop1 = new RecordProperty("prop1", JSType.STRING_TYPE);
    RecordProperty prop2 = new RecordProperty("prop2", JSType.NUMBER_TYPE);

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", prop1);
    properties.put("prop2", prop2);

    RecordType recordType = new RecordType(registry, properties);
    JSType recordTypeInstance = recordType.getInstance(registry);

    // Basic functionality test
    assertEquals(recordType, recordType.getSuperclass());
    assertEquals(2, recordType.getPropertyNames().length);
    assertEquals("prop1", recordType.getPropertyNames()[0]);
    assertEquals("prop2", recordType.getPropertyNames()[1]);

    // Edge case 1: Empty record type
    Map<String, RecordProperty> emptyProperties = Maps.newHashMap();
    RecordType emptyRecordType = new RecordType(registry, emptyProperties);
    assertEquals(0, emptyRecordType.getPropertyNames().length);

    // Edge case 2: Record type with the same property names but different types
    RecordProperty prop1Different = new RecordProperty("prop1", JSType.BOOLEAN_TYPE);
    Map<String, RecordProperty> differentProperties = Maps.newHashMap();
    differentProperties.put("prop1", prop1Different);
    differentProperties.put("prop2", prop2);
    RecordType differentRecordType = new RecordType(registry, differentProperties);
    assertFalse(recordType.isSubtype(differentRecordType));

    // Edge case 3: Record type with an unknown property
    RecordProperty unknownProp = new RecordProperty("unknownProp", JSType.UNKNOWN_TYPE);
    Map<String, RecordProperty> unknownProperties = Maps.newHashMap();
    unknownProperties.put("prop1", prop1);
    unknownProperties.put("unknownProp", unknownProp);
    RecordType unknownRecordType = new RecordType(registry, unknownProperties);
    RecordType expectedRecordType = new RecordTypeBuilder(registry)
        .addProperty("prop1", JSType.STRING_TYPE, null)
        .addProperty("unknownProp", JSType.UNKNOWN_TYPE, null)
        .build();
    assertFalse(recordType.isEquivalentTo(unknownRecordType));

    // Exception handling: Property type is null
    try {
      RecordProperty nullProp = new RecordProperty("nullProp", null);
      Map<String, RecordProperty> nullProperties = Maps.newHashMap();
      nullProperties.put("prop1", prop1);
      nullProperties.put("nullProp", nullProp);
      new RecordType(registry, nullProperties);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }
}