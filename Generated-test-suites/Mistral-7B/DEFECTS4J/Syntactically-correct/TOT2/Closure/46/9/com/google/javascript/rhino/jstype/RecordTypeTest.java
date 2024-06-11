import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  private static JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
    properties.put("intProp", new RecordProperty("intProp", registry.getIntType()));
    properties.put("stringProp", new RecordProperty("stringProp", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);

    assertEquals(registry.getRecordType("{intProp: int, stringProp: string}"), recordType);
  }

  @Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> propertiesA = Maps.newLinkedHashMap();
    propertiesA.put("intProp", new RecordProperty("intProp", registry.getIntType()));
    propertiesA.put("stringProp", new RecordProperty("stringProp", registry.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newLinkedHashMap();
    propertiesB.put("intProp", new RecordProperty("intProp", registry.getIntType()));
    propertiesB.put("stringProp", new RecordProperty("stringProp", registry.getStringType()));

    Map<String, RecordProperty> propertiesC = Maps.newLinkedHashMap();
    propertiesC.put("intProp", new RecordProperty("intProp", registry.getIntType()));
    propertiesC.put("stringProp", new RecordProperty("stringProp", registry.getStringType()));
    propertiesC.put("booleanProp", new RecordProperty("booleanProp", registry.getBooleanType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);
    RecordType recordTypeC = new RecordType(registry, propertiesC);

    assertTrue(recordTypeA.isEquivalentTo(recordTypeB));
    assertFalse(recordTypeA.isEquivalentTo(recordTypeC));
  }

  @Test
  public void testRecordTypeIsSubtypeOfRecordType() {
    Map<String, RecordProperty> propertiesA = Maps.newLinkedHashMap();
    propertiesA.put("intProp", new RecordProperty("intProp", registry.getIntType()));
    propertiesA.put("stringProp", new RecordProperty("stringProp", registry.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newLinkedHashMap();
    propertiesB.put("intProp", new RecordProperty("intProp", registry.getIntType()));
    propertiesB.put("stringProp", new RecordProperty("stringProp", registry.getStringType()));
    propertiesB.put("booleanProp", new RecordProperty("booleanProp", registry.getBooleanType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeIsSubtypeOfObjectType() {
    Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
    properties.put("intProp", new RecordProperty("intProp", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties);

    assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordType));
  }

  @Test
  public void testRecordTypeIsSubtypeOfUnknownType() {
    Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
    properties.put("intProp", new RecordProperty("intProp", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties);

    JSType unknownType = registry.getNativeType(JSTypeNative.NO_TYPE);

    assertFalse(unknownType.isSubtype(recordType));
  }

  @Test
  public void testRecordTypeHasProperty() {
    Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
    properties.put("intProp", new RecordProperty("intProp", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties);

    assertTrue(recordType.hasProperty("intProp"));
  }

  @Test
  public void testRecordTypeDoesNotHaveProperty() {
    Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
    properties.put("intProp", new RecordProperty("intProp", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties);

    assertFalse(recordType.hasProperty("stringProp"));
  }

  @Test
  public void testRecordTypeGetPropertyType() {
    Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
    properties.put("intProp", new RecordProperty("intProp", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties);

    assertEquals(registry.getIntType(), recordType.getPropertyType("intProp"));
  }

  @Test
  public void testRecordTypeGetPropertyNode() {
    Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
    properties.put("intProp", new RecordProperty("intProp", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties);

    assertNotNull(recordType.getPropertyNode("intProp"));
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypePropertyNodeIsNull() {
    Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
    properties.put("intProp", new RecordProperty("intProp", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties);

    properties.put("intProp", null);

    // This test should throw an IllegalStateException since the RecordProperty
    // associated with "intProp" should not be null.
    recordType.resolveInternal(null, null);
  }

  @Test
  public void testRecordTypeLeastSupertype() {
    Map<String, RecordProperty> propertiesA = Maps.newLinkedHashMap();
    propertiesA.put("intProp", new RecordProperty("intProp", registry.getIntType()));

    Map<String, RecordProperty> propertiesB = Maps.newLinkedHashMap();
    propertiesB.put("intProp", new RecordProperty("intProp", registry.getIntType()));
    propertiesB.put("stringProp", new RecordProperty("stringProp", registry.getStringType()));

    Map<String, RecordProperty> propertiesC = Maps.newLinkedHashMap();
    propertiesC.put("intProp", new RecordProperty("intProp", registry.getIntType()));
    propertiesC.put("stringProp", new RecordProperty("stringProp", registry.getStringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);
    RecordType recordTypeC = new RecordType(registry, propertiesC);

    JSType leastSupertypeAB = recordTypeA.getLeastSupertype(recordTypeB);
    JSType leastSupertypeAC = recordTypeA.getLeastSupertype(recordTypeC);

    assertEquals(recordTypeB, leastSupertypeAB);
    assertEquals(recordTypeC, leastSupertypeAC);
  }

  @Test
  public void testRecordTypeGreatestSubtype() {
    Map<String, RecordProperty> propertiesA = Maps.newLinkedHashMap();
    propertiesA.put("intProp", new RecordProperty("intProp", registry.getIntType()));
    propertiesA.put("stringProp", new RecordProperty("stringProp", registry.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newLinkedHashMap();
    propertiesB.put("intProp", new RecordProperty("intProp", registry.getIntType()));
    propertiesB.put("stringProp", new RecordProperty("stringProp", registry.getStringType()));
    propertiesB.put("booleanProp", new RecordProperty("booleanProp", registry.getBooleanType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    JSType greatestSubtype = recordTypeA.getGreatestSubtype(recordTypeB);

    assertEquals(recordTypeB, greatestSubtype);
  }

  @Test
  public void testRecordTypeFreeze() {
    Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
    properties.put("intProp", new RecordProperty("intProp", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties);

    assertFalse(recordType.isFrozen());

    recordType.freeze();

    assertTrue(recordType.isFrozen());
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeFreezeTwice() {
    Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
    properties.put("intProp", new RecordProperty("intProp", registry.getIntType()));

    RecordType recordType = new RecordType(registry, properties);

    recordType.freeze();
    recordType.freeze();

    // This test should throw an IllegalStateException since a record type
    // can only be frozen once.
    recordType.freeze();
  }
}