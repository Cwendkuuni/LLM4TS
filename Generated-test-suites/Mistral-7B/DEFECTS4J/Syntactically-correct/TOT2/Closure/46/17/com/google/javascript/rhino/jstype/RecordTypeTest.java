import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.UnionType;

public class RecordTypeTest {
  @Test
  public void testCreateRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
    RecordProperty prop2 = new RecordProperty("prop2", registry.getNumberType());

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", prop1);
    properties.put("prop2", prop2);

    RecordType recordType = new RecordType(registry, properties);

    // Basic functionality test
    JSType recordTypeInstance = recordType.getInstance(registry, null);
    assertNotNull(recordTypeInstance);
    assertEquals(recordType, recordType.getClassOf(recordTypeInstance));

    // Edge cases and error scenarios

    // Test with null properties
    try {
      new RecordType(registry, null);
      fail();
    } catch (IllegalStateException e) {
      // expected
    }

    // Test with empty properties map
    try {
      new RecordType(registry, Maps.newHashMap());
      fail();
    } catch (IllegalStateException e) {
      // expected
    }

    // Test with properties containing null RecordProperty
    Map<String, RecordProperty> propertiesWithNull = Maps.newHashMap();
    propertiesWithNull.put("prop1", null);
    try {
      new RecordType(registry, propertiesWithNull);
      fail();
    } catch (IllegalStateException e) {
      // expected
    }

    // Test with properties containing unknown type
    RecordProperty unknownTypeProp = new RecordProperty("prop1", new UnionType(registry, UnionTypeBuilder.newSet(JSType.VOID_TYPE, registry.getNumberType())));
    Map<String, RecordProperty> propertiesWithUnknownType = Maps.newHashMap();
    propertiesWithUnknownType.put("prop1", unknownTypeProp);
    RecordType recordTypeWithUnknownType = new RecordType(registry, propertiesWithUnknownType);
    JSType recordTypeWithUnknownTypeInstance = recordTypeWithUnknownType.getInstance(registry, null);
    assertNotNull(recordTypeWithUnknownTypeInstance);
    assertFalse(recordTypeWithUnknownTypeInstance.isEquivalentTo(recordType.getInstance(registry, null)));
  }
}