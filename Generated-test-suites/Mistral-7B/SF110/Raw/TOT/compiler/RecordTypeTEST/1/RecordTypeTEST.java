import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  private JSTypeRegistry registry;
  private RecordType recordType;
  private RecordType emptyRecordType;
  private RecordType recordWithStringProperties;
  private RecordType recordWithNestedRecordProperty;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();

    Map<String, RecordProperty> stringProperties = new HashMap<>();
    stringProperties.put("name", new RecordProperty("name", registry.getStringType()));
    stringProperties.put("age", new RecordProperty("age", registry.getNumberType()));

    recordType = new RecordType(registry, stringProperties, true);

    Map<String, RecordProperty> emptyProperties = new HashMap<>();
    emptyRecordType = new RecordType(registry, emptyProperties, true);

    Map<String, RecordProperty> nestedProperties = new HashMap<>();
    nestedProperties.put("name", new RecordProperty("name", registry.getStringType()));
    nestedProperties.put("age", new RecordProperty("age", registry.getNumberType()));
    nestedProperties.put("record",
        new RecordProperty("record", registry.newRecordType(registry, nestedProperties, true)));

    recordWithNestedRecordProperty = new RecordType(registry, nestedProperties, true);
  }

  @Test
  public void testCreateRecordType() {
    assertNotNull(recordType);
    assertEquals("Record", recordType.getName());
  }

  @Test
  public void testCreateEmptyRecordType() {
    assertNotNull(emptyRecordType);
    assertEquals("Record", emptyRecordType.getName());
  }

  @Test
  public void testIsSynthetic() {
    assertFalse(recordType.isSynthetic());
    assertTrue(emptyRecordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    Map<String, RecordProperty> identicalProperties = new HashMap<>(stringProperties);
    RecordType identicalRecord = new RecordType(registry, identicalProperties, true);

    assertTrue(recordType.checkRecordEquivalenceHelper(identicalRecord,
        EquivalenceMethod.STRICT));

    Map<String, RecordProperty> differentProperties = new HashMap<>(stringProperties);
    differentProperties.put("name", new RecordProperty("name", registry.getStringType()));
    differentProperties.put("age2", new RecordProperty("age2", registry.getNumberType()));

    RecordType differentRecord = new RecordType(registry, differentProperties, true);

    assertFalse(recordType.checkRecordEquivalenceHelper(differentRecord,
        EquivalenceMethod.STRICT));
  }

  @Test
  public void testGetImplicitPrototype() {
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordType.getImplicitPrototype());
  }

  @Test
  public void testDefineProperty() {
    String newProperty = "newProperty";
    JSType newPropertyType = registry.getNumberType();
    Node newPropertyNode = Node.newIdentifier("newProperty");

    recordType.defineProperty(newProperty, newPropertyType, false, newPropertyNode);

    assertNotNull(recordType.getPropertyType(newProperty));
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyWhenFrozen() {
    recordType.isFrozen = true;
    String newProperty = "newProperty";
    JSType newPropertyType = registry.getNumberType();
    Node newPropertyNode = Node.newIdentifier("newProperty");

    recordType.defineProperty(newProperty, newPropertyType, false, newPropertyNode);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType anotherRecord = registry.newRecordType(registry, stringProperties, true);
    JSType commonSupertype = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(anotherRecord);

    assertEquals(commonSupertype, greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
        .isSubtype(recordType));
    assertTrue(recordType.isSubtype(recordType));
    assertTrue(recordType.isSubtype(recordWithStringProperties));
    assertFalse(recordType.isSubtype(registry.getStringType()));
    assertFalse(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
        .isSubtype(registry.getStringType()));
  }
}