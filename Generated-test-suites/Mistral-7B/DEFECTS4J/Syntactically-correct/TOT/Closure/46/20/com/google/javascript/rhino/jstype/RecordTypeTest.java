import com.google.javascript.jscomp.type.Type;
import com.google.javascript.jscomp.type.TypeFactory;
import com.google.javascript.jscomp.type.TypeUtil;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = JSTypeFactory.getDefaultRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", TypeFactory.stringType()));
    properties.put("b", new RecordProperty("b", TypeFactory.numberType()));

    RecordType recordType = new RecordType(registry, properties);

    // Test that the record type has the correct properties.
    assertEquals(2, recordType.getDeclaredPropertyNames().length);
    assertEquals("a", recordType.getDeclaredPropertyNames()[0]);
    assertEquals("b", recordType.getDeclaredPropertyNames()[1]);

    // Test that the properties have the correct types.
    assertEquals(TypeFactory.stringType(), recordType.getPropertyType("a"));
    assertEquals(TypeFactory.numberType(), recordType.getPropertyType("b"));

    // Test that the record type is a subtype of itself.
    assertEquals(recordType, recordType.getLeastSupertype(recordType));

    // Test that the record type is a supertype of a sub-record type.
    Map<String, RecordProperty> subProperties = Maps.newHashMap();
    subProperties.putAll(properties);
    subProperties.put("c", new RecordProperty("c", TypeFactory.booleanType()));
    RecordType subRecordType = new RecordType(registry, subProperties);
    assertTrue(recordType.isSubtype(subRecordType));

    // Test that the record type is not a subtype of a non-record type.
    assertFalse(recordType.isSubtype(TypeFactory.numberType()));

    // Test that the record type is not a subtype of a record type with different properties.
    Map<String, RecordProperty> otherProperties = Maps.newHashMap();
    otherProperties.put("d", new RecordProperty("d", TypeFactory.booleanType()));
    RecordType otherRecordType = new RecordType(registry, otherProperties);
    assertFalse(recordType.isSubtype(otherRecordType));

    // Test that the record type can be assigned to a record with the same properties.
    Node recordNode = Node.objectLiteral()
        .setProperty("a", Node.stringLiteral("foo"))
        .setProperty("b", Node.doubleLiteral(3.14));
    Object value = recordType.coerceToValue(registry, null, recordNode,
        new ObjectType(registry, null, null));
    assertNotNull(value);
    RecordType recordValue = (RecordType) value;
    assertEquals(recordType, recordValue);

    // Test that the record type cannot be assigned to a record with different properties.
    Node otherRecordNode = Node.objectLiteral()
        .setProperty("d", Node.booleanLiteral(true));
    try {
      recordType.coerceToValue(registry, null, otherRecordNode,
          new ObjectType(registry, null, null));
      fail("Should have thrown an exception.");
    } catch (Exception e) {
      // Expected.
    }
  }

  @Test
  public void testRecordTypeFrozen() {
    JSTypeRegistry registry = JSTypeFactory.getDefaultRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", TypeFactory.stringType()));

    RecordType recordType = new RecordType(registry, properties);
    recordType.freeze();

    try {
      // Test that the record type cannot be modified after it is frozen.
      recordType.defineProperty("b", TypeFactory.numberType(), false, null);
      fail("Should have thrown an exception.");
    } catch (UnsupportedOperationException e) {
      // Expected.
    }
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = JSTypeFactory.getDefaultRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", TypeFactory.stringType()));
    propertiesA.put("b", new RecordProperty("b", TypeFactory.numberType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", TypeFactory.stringType()));
    propertiesB.put("b", new RecordProperty("b", TypeFactory.numberType()));
    propertiesB.put("c", new RecordProperty("c", TypeFactory.booleanType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    // Test that record types with different properties are not equivalent.
    assertFalse(recordTypeA.isEquivalentTo(recordTypeB));

    // Test that record types with the same properties are equivalent.
    RecordType recordTypeC = new RecordType(registry, propertiesA);
    assertEquals(recordTypeA, recordTypeC);
    assertEquals(recordTypeA, recordTypeC.toMaybeRecordType());
  }

  @Test
  public void testRecordTypeGetLeastSupertype() {
    JSTypeRegistry registry = JSTypeFactory.getDefaultRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", TypeFactory.stringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", TypeFactory.stringType()));
    propertiesB.put("b", new RecordProperty("b", TypeFactory.numberType()));

    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("a", new RecordProperty("a", TypeFactory.stringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);
    RecordType recordTypeC = new RecordType(registry, propertiesC);

    // Test that the least supertype of a record type and itself is itself.
    assertEquals(recordTypeA, recordTypeA.getLeastSupertype(recordTypeA));

    // Test that the least supertype of a record type and a sub-record type is the sub-record type.
    assertEquals(recordTypeB, recordTypeA.getLeastSupertype(recordTypeB));

    // Test that the least supertype of a record type and a non-record type is the native object type.
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordTypeA.getLeastSupertype(TypeFactory.numberType()));

    // Test that the least supertype of two record types is the intersection of their properties.
    assertEquals(recordTypeC, recordTypeA.getLeastSupertype(recordTypeB));
  }

  @Test
  public void testRecordTypeGetGreatestSubtype() {
    JSTypeRegistry registry = JSTypeFactory.getDefaultRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", TypeFactory.stringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", TypeFactory.stringType()));
    propertiesB.put("b", new RecordProperty("b", TypeFactory.numberType()));

    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("a", new RecordProperty("a", TypeFactory.stringType()));
    propertiesC.put("c", new RecordProperty("c", TypeFactory.booleanType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);
    RecordType recordTypeC = new RecordType(registry, propertiesC);

    // Test that the greatest subtype of a record type and itself is itself.
    assertEquals(recordTypeA, recordTypeA.getGreatestSubtype(recordTypeA));

    // Test that the greatest subtype of a record type and a super-record type is the record type.
    assertEquals(recordTypeA, recordTypeB.getGreatestSubtype(recordTypeA));

    // Test that the greatest subtype of a record type and a non-record type is the empty object type.
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE),
        recordTypeA.getGreatestSubtype(TypeFactory.numberType()));

    // Test that the greatest subtype of a record type and a sub-record type is the sub-record type.
    assertEquals(recordTypeC, recordTypeA.getGreatestSubtype(recordTypeC));

    // Test that the greatest subtype of two record types is the union of their properties.
    RecordType greatestSubtype = recordTypeA.getGreatestSubtype(recordTypeB);
    assertEquals(propertiesC, greatestSubtype.getProperties().entrySet().stream()
        .collect(Maps.toMap(Map.Entry::getKey, Map.Entry::getValue)));
  }

  @Test
  public void testRecordTypeIsSubtype() {
    JSTypeRegistry registry = JSTypeFactory.getDefaultRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", TypeFactory.stringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", TypeFactory.stringType()));
    propertiesB.put("b", new RecordProperty("b", TypeFactory.numberType()));

    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("a", new RecordProperty("a", TypeFactory.stringType()));
    propertiesC.put("c", new RecordProperty("c", TypeFactory.booleanType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);
    RecordType recordTypeC = new RecordType(registry, propertiesC);

    // Test that a record type is a subtype of itself.
    assertTrue(recordTypeA.isSubtype(recordTypeA));

    // Test that a record type is a subtype of a super-record type.
    assertTrue(recordTypeB.isSubtype(recordTypeA));

    // Test that a record type is not a subtype of a non-record type.
    assertFalse(recordTypeA.isSubtype(TypeFactory.numberType()));

    // Test that a record type is not a subtype of a record type with different properties.
    assertFalse(recordTypeC.isSubtype(recordTypeA));
  }

  @Test
  public void testRecordTypeHasProperty() {
    JSTypeRegistry registry = JSTypeFactory.getDefaultRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", TypeFactory.stringType()));

    RecordType recordType = new RecordType(registry, properties);

    // Test that the record type has the expected property.
    assertTrue(recordType.hasProperty("a"));

    // Test that the record type does not have an unexpected property.
    assertFalse(recordType.hasProperty("b"));
  }

  @Test
  public void testRecordTypePropertyType() {
    JSTypeRegistry registry = JSTypeFactory.getDefaultRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", TypeFactory.stringType()));

    RecordType recordType = new RecordType(registry, properties);

    // Test that the property type of a record type is the expected type.
    assertEquals(TypeFactory.stringType(), recordType.getPropertyType("a"));

    // Test that the property type of a record type is null for an undefined property.
    assertNull(recordType.getPropertyType("b"));
  }
}