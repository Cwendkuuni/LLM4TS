import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.isSubtype;
import static com.google.javascript.rhino.JSType.toMaybeRecordType;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Tests for {@link RecordType} class.
 */
public class RecordTypeTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @ org.junit.BeforeClass
  public static void setUp() {
    registry.init();
  }

  /**
   * Tests the construction of a record type.
   */
  @org.junit.Test
  public void testRecordTypeConstruction() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("x", new RecordProperty("x", registry.stringType()));
    recordProperties.put("y", new RecordProperty("y", registry.numberType()));
    RecordType recordType = new RecordType(registry, recordProperties, true);
    assertNotNull(recordType);
    assertEquals("Record", recordType.getName());
    assertEquals(2, recordType.getDeclaredProperties().size());
  }

  /**
   * Tests the record type's equivalence.
   */
  @org.junit.Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("x", new RecordProperty("x", registry.stringType()));
    recordProperties1.put("y", new RecordProperty("y", registry.numberType()));
    RecordType recordType1 = new RecordType(registry, recordProperties1, true);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("x", new RecordProperty("x", registry.stringType()));
    recordProperties2.put("y", new RecordProperty("y", registry.numberType()));
    recordProperties2.put("z", new RecordProperty("z", registry.numberType()));
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    // Record types are equivalent if they have the same properties.
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));
    assertTrue(recordType2.checkRecordEquivalenceHelper(recordType1, false));

    // Record types are not equivalent if they have different properties.
    Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
    recordProperties3.put("x", new RecordProperty("x", registry.stringType()));
    recordProperties3.put("y", new RecordProperty("y", registry.numberType()));
    recordProperties3.put("z", new RecordProperty("z", registry.booleanType()));
    RecordType recordType3 = new RecordType(registry, recordProperties3, true);
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, false));
    assertFalse(recordType3.checkRecordEquivalenceHelper(recordType1, false));
  }

  /**
   * Tests the record type's property access.
   */
  @org.junit.Test
  public void testRecordTypePropertyAccess() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("x", new RecordProperty("x", registry.stringType()));
    recordProperties.put("y", new RecordProperty("y", registry.numberType()));
    RecordType recordType = new RecordType(registry, recordProperties, true);

    // Accessing a property that exists.
    JSType xType = recordType.getPropertyType("x");
    assertEquals(registry.stringType(), xType);

    // Accessing a property that does not exist.
    JSType yType = recordType.getPropertyType("z");
    assertNull(yType);
  }

  /**
   * Tests the record type's property definition.
   */
  @org.junit.Test
  public void testRecordTypePropertyDefinition() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("x", new RecordProperty("x", registry.stringType()));
    recordProperties.put("y", new RecordProperty("y", registry.numberType()));
    RecordType recordType = new RecordType(registry, recordProperties, true);

    // Defining a property that already exists.
    boolean definitionSuccess = recordType.defineProperty(
        "x", registry.numberType(), false, null);
    assertFalse(definitionSuccess);

    // Defining a new property.
    definitionSuccess = recordType.defineProperty(
        "z", registry.booleanType(), false, null);
    assertTrue(definitionSuccess);
    assertEquals(3, recordType.getDeclaredProperties().size());
  }

  /**
   * Tests the record type's property inheritance.
   */
  @org.junit.Test
  public void testRecordTypePropertyInheritance() {
    Map<String, RecordProperty> baseRecordProperties = Maps.newHashMap();
    baseRecordProperties.put("x", new RecordProperty("x", registry.stringType()));
    RecordType baseRecordType = new RecordType(registry, baseRecordProperties, true);

    Map<String, RecordProperty> derivedRecordProperties = Maps.newHashMap();
    derivedRecordProperties.put("x", new RecordProperty("x", registry.numberType()));
    derivedRecordProperties.put("y", new RecordProperty("y", registry.numberType()));
    RecordType derivedRecordType = new RecordType(registry, derivedRecordProperties, true);

    // Base record type properties should be inherited.
    assertEquals(baseRecordType.getPropertyType("x"), derivedRecordType.getPropertyType("x"));

    // Derived record type properties should not conflict with base record type properties.
    assertNull(derivedRecordType.getPropertyType("x").getLeastSupertype(baseRecordType));

    // Derived record type properties should be added to base record type properties.
    JSType greatestSubtype = baseRecordType.getGreatestSubtype(derivedRecordType);
    assertEquals(2, greatestSubtype.getDeclaredProperties().size());
  }

  /**
   * Tests the record type's subtyping.
   */
  @org.junit.Test
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("x", new RecordProperty("x", registry.numberType()));
    RecordType recordType1 = new RecordType(registry, recordProperties1, true);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("x", new RecordProperty("x", registry.numberType()));
    recordProperties2.put("y", new RecordProperty("y", registry.numberType()));
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    // Record type1 is a subtype of record type2 because it has all of record type2's properties.
    assertTrue(isSubtype(recordType1, recordType2));

    // Record type2 is not a subtype of record type1 because it has additional properties.
    assertFalse(isSubtype(recordType2, recordType1));

    // Record type1 is a subtype of a record type that has all of its properties.
    Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
    recordProperties3.put("x", new RecordProperty("x", registry.numberType()));
    RecordType recordType3 = new RecordType(registry, recordProperties3, true);
    assertTrue(isSubtype(recordType1, recordType3));

    // Record type1 is a subtype of a record type that has a supertype.
    Map<String, RecordProperty> recordProperties4 = Maps.newHashMap();
    recordProperties4.put("x", new RecordProperty("x", registry.numberType()));
    recordProperties4.put("y", new RecordProperty("y", registry.unionType(registry.numberType(), registry.stringType())));
    RecordType recordType4 = new RecordType(registry, recordProperties4, true);
    assertTrue(isSubtype(recordType1, recordType4));

    // Record type1 is not a subtype of a record type with incompatible property types.
    Map<String, RecordProperty> recordProperties5 = Maps.newHashMap();
    recordProperties5.put("x", new RecordProperty("x", registry.numberType()));
    recordProperties5.put("y", new RecordProperty("y", registry.booleanType()));
    RecordType recordType5 = new RecordType(registry, recordProperties5, true);
    assertFalse(isSubtype(recordType1, recordType5));

    // Record type1 is not a subtype of a record type with a missing property.
    Map<String, RecordProperty> recordProperties6 = Maps.newHashMap();
    recordProperties6.put("y", new RecordProperty("y", registry.numberType()));
    RecordType recordType6 = new RecordType(registry, recordProperties6, true);
    assertFalse(isSubtype(recordType1, recordType6));

    // Record type1 is not a subtype of a record type with a missing property and incompatible property types.
    Map<String, RecordProperty> recordProperties7 = Maps.newHashMap();
    recordProperties7.put("z", new RecordProperty("z", registry.numberType()));
    RecordType recordType7 = new RecordType(registry, recordProperties7, true);
    assertFalse(isSubtype(recordType1, recordType7));
  }

  /**
   * Tests the record type's property union types.
   */
  @org.junit.Test
  public void testRecordTypePropertyUnionTypes() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("x", new RecordProperty("x", registry.numberType()));
    recordProperties.put("y", new RecordProperty("y", registry.unionType(registry.numberType(), registry.stringType())));
    RecordType recordType = new RecordType(registry, recordProperties, true);

    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    builder.addAlternate(registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE));
    builder.addAlternate(registry.getNativeObjectType(JSTypeNative.STRING_TYPE));
    JSType expectedUnionType = builder.build();

    JSType actualUnionType = recordType.getPropertyType("y");
    assertEquals(expectedUnionType, actualUnionType);
  }

  /**
   * Tests the record type's property intersection types.
   */
  @org.junit.Test
  public void testRecordTypePropertyIntersectionTypes() {
    Map<String, RecordProperty> baseRecordProperties = Maps.newHashMap();
    baseRecordProperties.put("x", new RecordProperty("x", registry.numberType()));
    RecordType baseRecordType = new RecordType(registry, baseRecordProperties, true);

    Map<String, RecordProperty> derivedRecordProperties = Maps.newHashMap();
    derivedRecordProperties.put("x", new RecordProperty("x", registry.stringType()));
    derivedRecordProperties.put("y", new RecordProperty("y", registry.numberType()));
    RecordType derivedRecordType = new RecordType(registry, derivedRecordProperties, true);

    JSType greatestCommonSubtype = baseRecordType.getGreatestSubtype(derivedRecordType);
    JSType expectedIntersectionType = registry.getIntersectionType(
        baseRecordType.getPropertyType("x"), derivedRecordType.getPropertyType("x"));
    assertEquals(expectedIntersectionType, greatestCommonSubtype.getPropertyType("x"));
  }

  /**
   * Tests the record type's getGreatestSubtype method with null arguments.
   */
  @org.junit.Test(expected = NullPointerException.class)
  public void testRecordTypeGetGreatestSubtypeNullArguments() {
    RecordType recordType1 = new RecordType(registry, Maps.newHashMap(), true);
    recordType1.getGreatestSubtype(null);
  }

  /**
   * Tests the record type's getGreatestSubtype method with non-record types.
   */
  @org.junit.Test(expected = ClassCastException.class)
  public void testRecordTypeGetGreatestSubtypeNonRecordType() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("x", new RecordProperty("x", registry.numberType()));
    RecordType recordType = new RecordType(registry, recordProperties, true);

    JSType nonRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    recordType.getGreatestSubtype(nonRecordType);
  }

  /**
   * Tests the record type's getGreatestSubtype method with identical record types.
   */
  @org.junit.Test
  public void testRecordTypeGetGreatestSubtypeIdenticalRecordTypes() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("x", new RecordProperty("x", registry.numberType()));
    RecordType recordType1 = new RecordType(registry, recordProperties, true);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("x", new RecordProperty("x", registry.numberType()));
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
    assertEquals(recordType1, greatestSubtype);
    assertEquals(recordType2, greatestSubtype);
  }

  /**
   * Tests the record type's getGreatestSubtype method with compatible record types.
   */
  @org.junit.Test
  public void testRecordTypeGetGreatestSubtypeCompatibleRecordTypes() {
    Map<String, RecordProperty> baseRecordProperties = Maps.newHashMap();
    baseRecordProperties.put("x", new RecordProperty("x", registry.numberType()));
    RecordType baseRecordType = new RecordType(registry, baseRecordProperties, true);

    Map<String, RecordProperty> derivedRecordProperties = Maps.newHashMap();
    derivedRecordProperties.put("x", new RecordProperty("x", registry.numberType()));
    derivedRecordProperties.put("y", new RecordProperty("y", registry.numberType()));
    RecordType derivedRecordType = new RecordType(registry, derivedRecordProperties, true);

    JSType greatestSubtype = baseRecordType.getGreatestSubtype(derivedRecordType);
    assertTrue(isSubtype(baseRecordType, greatestSubtype));
    assertTrue(isSubtype(derivedRecordType, greatestSubtype));
  }

  /**
   * Tests the record type's getGreatestSubtype method with incompatible record types.
   */
  @org.junit.Test
  public void testRecordTypeGetGreatestSubtypeIncompatibleRecordTypes() {
    Map<String, RecordProperty> baseRecordProperties = Maps.newHashMap();
    baseRecordProperties.put("x", new RecordProperty("x", registry.numberType()));
    RecordType baseRecordType = new RecordType(registry, baseRecordProperties, true);

    Map<String, RecordProperty> derivedRecordProperties = Maps.newHashMap();
    derivedRecordProperties.put("x", new RecordProperty("x", registry.stringType()));
    derivedRecordProperties.put("y", new RecordProperty("y", registry.numberType()));
    RecordType derivedRecordType = new RecordType(registry, derivedRecordProperties, true);

    JSType greatestSubtype = baseRecordType.getGreatestSubtype(derivedRecordType);
    assertNull(greatestSubtype);
  }
}