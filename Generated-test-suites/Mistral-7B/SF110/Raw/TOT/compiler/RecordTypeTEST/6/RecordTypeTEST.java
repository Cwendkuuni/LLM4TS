import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.TypeCheckError;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreationWithDeclaredProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals("Object", recordType.getClass().getName());
    assertTrue(recordType.isDeclared());

    assertEquals(String.class, recordType.getPropertyType("name"));
    assertEquals(Number.class, recordType.getPropertyType("age"));

    RecordType recordType2 = new RecordType(registry, properties, true);
    assertEquals(recordType, recordType2);
  }

  @Test
  public void testRecordTypeCreationWithSynthesizedProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, properties, false);

    assertEquals("Object", recordType.getClass().getName());
    assertFalse(recordType.isDeclared());

    assertEquals(String.class, recordType.getPropertyType("name"));
    assertEquals(Number.class, recordType.getPropertyType("age"));

    RecordType recordType2 = new RecordType(registry, properties, false);
    assertEquals(recordType, recordType2);
  }

  @Test
  public void testRecordTypeGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, properties, true);

    ObjectType implicitPrototype = recordType.getImplicitPrototype();

    assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype);
  }

  @Test
  public void testRecordTypeDefinePropertyFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, properties, true);

    assertFalse(recordType.defineProperty("name", String.class, false, null));
  }

  @Test(expected = TypeCheckError.class)
  public void testRecordTypeDefinePropertyNonMatchingTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, properties, true);

    assertFalse(recordType.defineProperty("name", Number.class, false, null));
  }

  @Test
  public void testRecordTypeGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Number.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Number.class));
    propertiesB.put("city", new RecordProperty("city", String.class));

    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    RecordType greatestSubtype = recordTypeA.getGreatestSubtype(recordTypeB);

    assertEquals(recordTypeB, greatestSubtype);
  }

  @Test
  public void testRecordTypeIsSubtypeOfEmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType emptyRecord = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    RecordType recordType = new RecordType(registry, null, true);

    assertTrue(recordType.isSubtype(emptyRecord));
  }

  @Test
  public void testRecordTypeIsSubtypeOfSelf() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, properties, true);

    assertTrue(recordType.isSubtype(recordType));
  }

  @Test
  public void testRecordTypeIsSubtypeOfDifferentDeclaredPropertyTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Number.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", Integer.class));
    propertiesB.put("age", new RecordProperty("age", String.class));

    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeIsSubtypeOfDifferentSynthesizedProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Number.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA, false);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", Integer.class));
    propertiesB.put("age", new RecordProperty("age", Double.class));

    RecordType recordTypeB = new RecordType(registry, propertiesB, false);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeIsSubtypeOfDifferentSynthesizedPropertiesWithInvariantTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Number.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA, false);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", Number.class));
    propertiesB.put("age", new RecordProperty("age", String.class));

    RecordType recordTypeB = new RecordType(registry, propertiesB, false);

    // Both record types have invariant property types, so they are equivalent
    assertTrue(recordTypeA.isSubtype(recordTypeB));
    assertTrue(recordTypeB.isSubtype(recordTypeA));
  }

  @Test
  public void testRecordTypeIsSubtypeOfDifferentDeclaredAndSynthesizedProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Number.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", Integer.class));
    propertiesB.put("age", new RecordProperty("age", Number.class));
    propertiesB.put("city", new RecordProperty("city", String.class));

    RecordType recordTypeB = new RecordType(registry, propertiesB, false);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeCheckEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Number.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Number.class));

    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, EquivalenceMethod.IDENTITY));
  }

  @Test
  public void testRecordTypeCheckEquivalenceHelperWithDifferentNumberOfProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Number.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Number.class));
    propertiesB.put("city", new RecordProperty("city", String.class));

    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, EquivalenceMethod.IDENTITY));
  }

  @Test
  public void testRecordTypeCheckEquivalenceHelperWithDifferentPropertyTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Number.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", Number.class));
    propertiesB.put("age", new RecordProperty("age", String.class));

    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, EquivalenceMethod.IDENTITY));
  }

  @Test
  public void testRecordTypeCheckEquivalenceHelperWithDifferentPropertyOrders() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("age", new RecordProperty("age", Number.class));
    propertiesA.put("name", new RecordProperty("name", String.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Number.class));

    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, EquivalenceMethod.IDENTITY));
  }

  @Test
  public void testRecordTypeCheckEquivalenceHelperWithNullProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordTypeA = new RecordType(registry, null, true);
    RecordType recordTypeB = new RecordType(registry, null, true);

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, EquivalenceMethod.IDENTITY));
  }

  enum EquivalenceMethod {
    IDENTITY,
    STRICT,
    PROTOTYPE_CHAIN,
    TO_STRING,
    VALUE_OF
  }
}