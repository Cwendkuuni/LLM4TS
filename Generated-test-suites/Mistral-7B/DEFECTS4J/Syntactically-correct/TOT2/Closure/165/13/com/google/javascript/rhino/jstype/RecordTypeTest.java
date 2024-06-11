import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSTestCase.*;

import com.google.javascript.rhino.jstype.*;
import com.google.javascript.rhino.testing.TypeTester;

public class RecordTypeTest extends JSTestCase {

  public void testRecordTypeCreation() {
    // Test creating a record type with known properties
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType StringType = registry.getNativeType(JSTypeNative.STRING_TYPE);
    JSType NumberType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", StringType));
    properties.put("age", new RecordProperty("age", NumberType));

    RecordType recordType = new RecordType(registry, properties);
    assertEquals("Name: String, Age: Number", recordType.toString());

    // Test creating a record type with unknown properties
    properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", JSType.UNKNOWN));
    properties.put("y", new RecordProperty("y", JSType.UNKNOWN));

    RecordType recordTypeUnknown = new RecordType(registry, properties);
    assertEquals("{ x: UNKNOWN, y: UNKNOWN }", recordTypeUnknown.toString());
  }

  public void testRecordTypeSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertyA = Maps.newHashMap();
    propertyA.put("a", new RecordProperty("a", JSType.INT));
    RecordType recordTypeA = new RecordType(registry, propertyA);

    Map<String, RecordProperty> propertyB = Maps.newHashMap();
    propertyB.put("a", new RecordProperty("a", JSType.STRING));
    propertyB.put("b", new RecordProperty("b", JSType.INT));

    RecordType recordTypeB = new RecordType(registry, propertyB);

    RecordType recordTypeAB = recordTypeA.getGreatestSubtype(recordTypeB);
    assertTrue(recordTypeA.isSubtype(recordTypeAB));
    assertTrue(recordTypeB.isSubtype(recordTypeAB));

    assertFalse(recordTypeAB.isSubtype(registry.getNativeType(
        JSTypeNative.BOOLEAN_TYPE)));
  }

  public void testRecordTypeAssignment() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertyA = Maps.newHashMap();
    propertyA.put("a", new RecordProperty("a", JSType.INT));
    RecordType recordTypeA = new RecordType(registry, propertyA);

    Map<String, RecordProperty> propertyB = Maps.newHashMap();
    propertyB.put("a", new RecordProperty("a", JSType.STRING));
    propertyB.put("b", new RecordProperty("b", JSType.INT));

    RecordType recordTypeB = new RecordType(registry, propertyB);

    // Test assignment from a supertype to a subtype
    RecordType recordTypeAB = recordTypeA.getGreatestSubtype(recordTypeB);
    RecordType recordTypeC = new RecordType(registry, propertyB);
    recordTypeC = recordTypeAB;

    // Test that the properties of the record type are correctly assigned
    assertEquals(recordTypeB.getPropertyType("a"), recordTypeC.getPropertyType("a"));
    assertEquals(recordTypeB.getPropertyType("b"), recordTypeC.getPropertyType("b"));

    // Test assignment from a subtype to a supertype
    RecordType recordTypeAC = new RecordType(registry, propertyA);
    recordTypeAC = recordTypeC;

    // Test that the properties of the record type are correctly assigned
    assertEquals(recordTypeA.getPropertyType("a"), recordTypeAC.getPropertyType("a"));
    assertNull(recordTypeAC.getPropertyType("b"));
  }

  public void testRecordTypeFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertyA = Maps.newHashMap();
    propertyA.put("a", new RecordProperty("a", JSType.INT));
    propertyA.put("b", new RecordProperty("b", JSType.INT));

    RecordType recordTypeA = new RecordType(registry, propertyA);

    // Test adding properties after construction
    try {
      recordTypeA.defineProperty("c", JSType.STRING, false, null);
      fail("Expected an exception");
    } catch (IllegalStateException e) {
      // Expected exception
    }

    // Test freezing the record type
    recordTypeA.isFrozen = true;

    // Test adding properties after freezing
    try {
      recordTypeA.defineProperty("c", JSType.STRING, false, null);
      fail("Expected an exception");
    } catch (IllegalStateException e) {
      // Expected exception
    }
  }

  public void testRecordTypeToString() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test toString for a simple record type
    Map<String, RecordProperty> propertyA = Maps.newHashMap();
    propertyA.put("a", new RecordProperty("a", JSType.INT));
    propertyA.put("b", new RecordProperty("b", JSType.STRING));
    RecordType recordTypeA = new RecordType(registry, propertyA);

    assertEquals("{ a: INT, b: STRING }", recordTypeA.toString());

    // Test toString for a record type with unknown properties
    Map<String, RecordProperty> propertyUnknown = Maps.newHashMap();
    propertyUnknown.put("x", new RecordProperty("x", JSType.UNKNOWN));
    propertyUnknown.put("y", new RecordProperty("y", JSType.UNKNOWN));
    RecordType recordTypeUnknown = new RecordType(registry, propertyUnknown);

    assertEquals("{ x: UNKNOWN, y: UNKNOWN }", recordTypeUnknown.toString());
  }

  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertyA = Maps.newHashMap();
    propertyA.put("a", new RecordProperty("a", JSType.INT));
    propertyA.put("b", new RecordProperty("b", JSType.INT));

    Map<String, RecordProperty> propertyB = Maps.newHashMap();
    propertyB.put("a", new RecordProperty("a", JSType.INT));
    propertyB.put("b", new RecordProperty("b", JSType.INT));
    propertyB.put("c", new RecordProperty("c", JSType.INT));

    RecordType recordTypeA = new RecordType(registry, propertyA);
    RecordType recordTypeB = new RecordType(registry, propertyB);

    // Test equivalence for equal record types
    assertTrue(recordTypeA.isEquivalentTo(recordTypeA));
    assertTrue(recordTypeA.isEquivalentTo(recordTypeB));

    // Test inequivalence for different record types
    RecordType recordTypeC = new RecordType(registry, Maps.newHashMap());
    assertFalse(recordTypeA.isEquivalentTo(recordTypeC));

    // Test inequivalence for record types with different properties
    RecordType recordTypeD = new RecordType(registry, propertyB);
    assertFalse(recordTypeA.isEquivalentTo(recordTypeD));
  }

  public void testRecordTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertyA = Maps.newHashMap();
    propertyA.put("a", new RecordProperty("a", JSType.INT));
    RecordType recordTypeA = new RecordType(registry, propertyA);

    Map<String, RecordProperty> propertyB = Maps.newHashMap();
    propertyB.put("a", new RecordProperty("a", JSType.INT));
    propertyB.put("b", new RecordProperty("b", JSType.INT));

    RecordType recordTypeB = new RecordType(registry, propertyB);

    // Test that recordA is a subtype of recordB
    assertTrue(recordTypeA.isSubtype(recordTypeB));

    // Test that a String is a subtype of a RecordType with a String property
    JSType StringType = registry.getNativeType(JSTypeNative.STRING_TYPE);
    RecordType recordTypeString = new RecordType(registry, Maps.newHashMap());
    recordTypeString.defineProperty("x", StringType, false, null);
    assertTrue(StringType.isSubtype(recordTypeString));

    // Test that a RecordType with a subtype property is a subtype of a RecordType
    // with a supertype property
    JSType subType = registry.getNativeType(JSTypeNative.STRING_TYPE);
    JSType superType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    RecordType subTypeRecord = new RecordType(registry, Maps.newHashMap());
    subTypeRecord.defineProperty("x", subType, false, null);
    RecordType superTypeRecord = new RecordType(registry, Maps.newHashMap());
    superTypeRecord.defineProperty("x", superType, false, null);
    assertTrue(subTypeRecord.isSubtype(superTypeRecord));

    // Test that a RecordType with a supertype property is not a subtype of a RecordType
    // with a subtype property
    assertFalse(superTypeRecord.isSubtype(subTypeRecord));
  }

  public void testRecordTypeEquals() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertyA = Maps.newHashMap();
    propertyA.put("a", new RecordProperty("a", JSType.INT));
    RecordType recordTypeA = new RecordType(registry, propertyA);

    Map<String, RecordProperty> propertyB = Maps.newHashMap();
    propertyB.put("a", new RecordProperty("a", JSType.INT));
    propertyB.put("b", new RecordProperty("b", JSType.INT));

    RecordType recordTypeB = new RecordType(registry, propertyB);

    // Test equals for equal record types
    assertEquals(recordTypeA, recordTypeA);
    assertEquals(recordTypeA, recordTypeB);

    // Test not equals for different record types
    RecordType recordTypeC = new RecordType(registry, Maps.newHashMap());
    assertNotEquals(recordTypeA, recordTypeC);

    // Test not equals for record types with different properties
    RecordType recordTypeD = new RecordType(registry, propertyB);
    assertNotEquals(recordTypeA, recordTypeD);
  }

  public void testRecordTypeHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertyA = Maps.newHashMap();
    propertyA.put("a", new RecordProperty("a", JSType.INT));
    RecordType recordTypeA = new RecordType(registry, propertyA);

    Map<String, RecordProperty> propertyB = Maps.newHashMap();
    propertyB.put("a", new RecordProperty("a", JSType.INT));
    propertyB.put("b", new RecordProperty("b", JSType.INT));

    RecordType recordTypeB = new RecordType(registry, propertyB);

    // Test hashcode for equal record types
    int hashcodeA = recordTypeA.hashCode();
    int hashcodeB = recordTypeB.hashCode();
    assertEquals(hashcodeA, hashcodeB);

    // Test hashcode for different record types
    RecordType recordTypeC = new RecordType(registry, Maps.newHashMap());
    int hashcodeC = recordTypeC.hashCode();
    assertNotEquals(hashcodeA, hashcodeC);

    // Test hashcode for record types with different properties
    RecordType recordTypeD = new RecordType(registry, propertyB);
    int hashcodeD = recordTypeD.hashCode();
    assertNotEquals(hashcodeA, hashcodeD);
  }

  public void testRecordTypeGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> property = Maps.newHashMap();
    property.put("a", new RecordProperty("a", JSType.INT));
    RecordType recordType = new RecordType(registry, property);

    ObjectType implicitPrototype = recordType.getImplicitPrototype();
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
  }

  public void testRecordTypeGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertyA = Maps.newHashMap();
    propertyA.put("a", new RecordProperty("a", JSType.INT));
    propertyA.put("b", new RecordProperty("b", JSType.INT));

    Map<String, RecordProperty> propertyB = Maps.newHashMap();
    propertyB.put("a", new RecordProperty("a", JSType.STRING));
    propertyB.put("b", new RecordProperty("b", JSType.INT));
    propertyB.put("c", new RecordProperty("c", JSType.INT));

    RecordType recordTypeA = new RecordType(registry, propertyA);
    RecordType recordTypeB = new RecordType(registry, propertyB);

    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    assertEquals(recordTypeA, greatestSubtype);

    JSTypeType recordTypeC = registry.getNativeType(JSTypeNative.OBJECT_TYPE);
    JSType greatestSubtype2 = recordTypeA.getGreatestSubtypeHelper(recordTypeC);
    assertNotSame(recordTypeA, greatestSubtype2);
    assertEquals(recordTypeC, greatestSubtype2);
  }

  public void testRecordTypeIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertyA = Maps.newHashMap();
    propertyA.put("a", new RecordProperty("a", JSType.INT));
    propertyA.put("b", new RecordProperty("b", JSType.INT));

    Map<String, RecordProperty> propertyB = Maps.newHashMap();
    propertyB.put("a", new RecordProperty("a", JSType.INT));
    propertyB.put("b", new RecordProperty("b", JSType.INT));

    RecordType recordTypeA = new RecordType(registry, propertyA);
    RecordType recordTypeB = new RecordType(registry, propertyB);

    // Test equals for equal record types
    assertTrue(recordTypeA.isEquivalentTo(recordTypeB));

    // Test not equals for different record types
    Map<String, RecordProperty> propertyC = Maps.newHashMap();
    propertyC.put("a", new RecordProperty("a", JSType.INT));
    propertyC.put("c", new RecordProperty("c", JSType.INT));

    RecordType recordTypeC = new RecordType(registry, propertyC);
    assertFalse(recordTypeA.isEquivalentTo(recordTypeC));

    // Test not equals for record types with different properties
    Map<String, RecordProperty> propertyD = Maps.newHashMap();
    propertyD.put("a", new RecordProperty("a", JSType.INT));
    propertyD.put("c", new RecordProperty("c", JSType.INT));

    RecordType recordTypeD = new RecordType(registry, propertyD);
    assertFalse(recordTypeA.isEquivalentTo(recordTypeD));
  }
}