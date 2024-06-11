import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

import java.util.SortedMap;
import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testCreateRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new Maps.HashMap<>();
    RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
    RecordProperty prop2 = new RecordProperty("prop2", registry.getNumberType());
    recordProperties.put("prop1", prop1);
    recordProperties.put("prop2", prop2);

    RecordType recordType = new RecordType(registry, recordProperties);

    assertEquals("RecordType class", RecordType.class, recordType.getClass());
    assertEquals("RecordType's isolated type registry", registry, recordType.getTypeRegistry());
    assertEquals("RecordType's frozen flag is false", false, recordType.isFrozen());

    SortedMap<String, JSType> expectedProperties = recordType.properties;
    assertEquals("RecordType's properties size", 2, expectedProperties.size());
    assertTrue("Property 'prop1' exists", expectedProperties.containsKey("prop1"));
    assertTrue("Property 'prop2' exists", expectedProperties.containsKey("prop2"));
  }

  @Test
  public void testRecordTypeIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties1 = new Maps.HashMap<>();
    RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
    RecordProperty prop2 = new RecordProperty("prop2", registry.getNumberType());
    recordProperties1.put("prop1", prop1);
    recordProperties1.put("prop2", prop2);

    Map<String, RecordProperty> recordProperties2 = new Maps.HashMap<>();
    RecordProperty prop3 = new RecordProperty("prop1", registry.getStringType());
    RecordProperty prop4 = new RecordProperty("prop2", registry.getNumberType());
    RecordProperty prop5 = new RecordProperty("prop3", registry.getBooleanType());
    recordProperties2.put("prop1", prop1);
    recordProperties2.put("prop2", prop4);
    recordProperties2.put("prop3", prop5);

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertFalse("RecordType1 is not equivalent to RecordType2", recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testRecordTypeIsSubtypeOf() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties1 = new Maps.HashMap<>();
    RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
    RecordProperty prop2 = new RecordProperty("prop2", registry.getNumberType());
    recordProperties1.put("prop1", prop1);
    recordProperties1.put("prop2", prop2);

    Map<String, RecordProperty> recordProperties2 = new Maps.HashMap<>();
    RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
    RecordProperty prop3 = new RecordProperty("prop3", registry.getBooleanType());
    recordProperties2.put("prop1", prop1);
    recordProperties2.put("prop3", prop3);

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    // RecordType1 is a subtype of RecordType2 since it has all the properties
    // of RecordType2 and their types are compatible.
    assertTrue("RecordType1 is a subtype of RecordType2", recordType1.isSubtype(recordType2));

    // RecordType2 is not a subtype of RecordType1 since it has an extra property
    assertFalse("RecordType2 is not a subtype of RecordType1", recordType2.isSubtype(recordType1));

    // ObjectType, as the top type, is a subtype of every record type.
    RecordType recordTypeObject = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).toMaybeRecordType();
    assertTrue("ObjectType is a subtype of RecordType1", recordType1.isSubtype(recordTypeObject));
  }

  @Test
  public void testRecordTypeGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties1 = new Maps.HashMap<>();
    RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
    RecordProperty prop2 = new RecordProperty("prop2", registry.getNumberType());
    recordProperties1.put("prop1", prop1);
    recordProperties1.put("prop2", prop2);

    Map<String, RecordProperty> recordProperties2 = new Maps.HashMap<>();
    RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
    RecordProperty prop3 = new RecordProperty("prop3", registry.getBooleanType());
    RecordProperty prop4 = new RecordProperty("prop4", registry.getIntType());
    recordProperties2.put("prop1", prop1);
    recordProperties2.put("prop3", prop3);
    recordProperties2.put("prop4", prop4);

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    RecordType expectedRecordType = new RecordTypeBuilder(registry)
        .addProperty("prop1", registry.getStringType(), null)
        .addProperty("prop3", registry.getBooleanType(), null)
        .addProperty("prop4", registry.getIntType(), null)
        .build();

    RecordType recordTypeResult = recordType1.getGreatestSubtype(recordType2);
    assertEquals("RecordType1's greatest subtype is equals to the expected RecordType", expectedRecordType, recordTypeResult);
  }

  @Test
  public void testRecordTypeFrozenFlag() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new Maps.HashMap<>();
    RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
    recordProperties.put("prop1", prop1);

    RecordType recordType = new RecordType(registry, recordProperties);
    assertFalse("RecordType's frozen flag is false by default", recordType.isFrozen());

    recordType.freeze();
    assertTrue("RecordType's frozen flag is true after freezing", recordType.isFrozen());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRecordTypeDefinePropertyAfterFreezing() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new Maps.HashMap<>();
    RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
    recordProperties.put("prop1", prop1);

    RecordType recordType = new RecordType(registry, recordProperties);
    recordType.freeze();

    String newPropertyName = "newPropertyName";
    JSType newPropertyType = registry.getNumberType();

    recordType.defineProperty(newPropertyName, newPropertyType, false, null);
  }

  @Test
  public void testRecordTypeGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new Maps.HashMap<>();
    RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
    recordProperties.put("prop1", prop1);

    RecordType recordType = new RecordType(registry, recordProperties);
    RecordType expectedPrototype = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).toMaybeRecordType();

    RecordType actualPrototype = recordType.getImplicitPrototype();
    assertEquals("RecordType's implicit prototype is equals to the expected ObjectType", expectedPrototype, actualPrototype);
  }
}