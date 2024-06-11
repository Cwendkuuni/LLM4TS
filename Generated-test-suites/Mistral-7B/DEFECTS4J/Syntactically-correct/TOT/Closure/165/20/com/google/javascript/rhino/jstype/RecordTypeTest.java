import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSTestCase.*;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.TypeRegistry;

public class RecordTypeTest extends JSTestCase {

  private static TypeRegistry registry;
  private static RecordProperty propA;
  private static RecordProperty propB;

  @BeforeClass
  public static void setUp() {
    registry = new TypeRegistry();
    propA = new RecordProperty("a", registry.getNumberType());
    propB = new RecordProperty("b", registry.getBooleanType());
  }

  /**
   * Test creating a simple record type.
   */
  public void testSimpleRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", propA);
    RecordType recordType = new RecordType(registry, properties);
    assertEquals("{ a : Number }", recordType.toString());
  }

  /**
   * Test creating a record type with a frozen property.
   */
  public void testFrozenRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", propA);
    RecordType recordType = new RecordType(registry, properties);
    recordType.freeze();

    try {
      recordType.defineProperty("b", registry.getStringType(), true, null);
      fail("Expected an IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  /**
   * Test creating a record type with inferred properties.
   */
  public void testInferredProperties() {
    RecordType recordType = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());
    RecordProperty propC = new RecordProperty("c", registry.getBooleanType());
    recordType.defineProperty("c", propC.getType(), true, null);

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", propA);
    properties.put("b", propB);
    properties.put("c", propC);

    RecordType derivedRecordType = new RecordType(registry, properties);
    assertEquals("{ a : Number, b : Boolean, c : Boolean }", derivedRecordType.toString());
  }

  /**
   * Test record type subtyping.
   */
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> propsA = Maps.newHashMap();
    propsA.put("a", propA);
    RecordType recordTypeA = new RecordType(registry, propsA);

    Map<String, RecordProperty> propsB = Maps.newHashMap();
    propsB.put("a", propA);
    propsB.put("b", propB);
    RecordType recordTypeB = new RecordType(registry, propsB);

    // recordTypeA is a subtype of recordTypeB
    assertTrue(recordTypeA.isSubtype(recordTypeB));

    // recordTypeB is not a subtype of recordTypeA
    assertFalse(recordTypeB.isSubtype(recordTypeA));
  }

  /**
   * Test record type intersection.
   */
  public void testRecordTypeIntersection() {
    Map<String, RecordProperty> propsA = Maps.newHashMap();
    propsA.put("a", propA);
    RecordType recordTypeA = new RecordType(registry, propsA);

    Map<String, RecordProperty> propsB = Maps.newHashMap();
    propsB.put("a", propA);
    propsB.put("b", propB);
    RecordType recordTypeB = new RecordType(registry, propsB);

    RecordType recordTypeC = recordTypeA.getGreatestSubtype(recordTypeB);
    assertEquals("{ a : Number }", recordTypeC.toString());
  }

  /**
   * Test record type union.
   */
  public void testRecordTypeUnion() {
    Map<String, RecordProperty> propsA = Maps.newHashMap();
    propsA.put("a", propA);
    RecordType recordTypeA = new RecordType(registry, propsA);

    Map<String, RecordProperty> propsB = Maps.newHashMap();
    propsB.put("b", propB);
    RecordType recordTypeB = new RecordType(registry, propsB);

    Map<String, RecordProperty> propsC = Maps.newHashMap();
    propsC.put("c", propB);
    RecordType recordTypeC = new RecordType(registry, propsC);

    UnionType recordTypeUnion = recordTypeA.getLeastSupertype(recordTypeB);
    assertEquals("{ a : Number, b : Boolean }", recordTypeUnion.toString());

    recordTypeUnion = recordTypeUnion.getLeastSupertype(recordTypeC);
    assertEquals("{ a : Number, b : Boolean, c : Boolean }", recordTypeUnion.toString());
  }

  /**
   * Test record type equality.
   */
  public void testRecordTypeEquality() {
    Map<String, RecordProperty> propsA = Maps.newHashMap();
    propsA.put("a", propA);
    RecordType recordTypeA = new RecordType(registry, propsA);

    Map<String, RecordProperty> propsB = Maps.newHashMap();
    propsB.put("a", propA);
    propsB.put("b", propB);
    RecordType recordTypeB = new RecordType(registry, propsB);

    assertEquals(recordTypeA, recordTypeB);

    Map<String, RecordProperty> propsC = Maps.newHashMap();
    propsC.put("b", propB);
    RecordType recordTypeC = new RecordType(registry, propsC);

    assertNotEquals(recordTypeA, recordTypeC);

    Map<String, RecordProperty> propsD = Maps.newHashMap();
    propsD.put("c", propB);
    RecordType recordTypeD = new RecordType(registry, propsD);

    assertNotEquals(recordTypeA, recordTypeD);
  }

  /**
   * Test record type with null properties.
   */
  public void testRecordTypeNullProperties() {
    Map<String, RecordProperty> props = Maps.newHashMap();
    props.put("a", propA);
    RecordType recordType = new RecordType(registry, props);

    try {
      recordType.defineProperty("b", registry.getNativeType(JSTypeNative.NULL), true, null);
      fail("Expected an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  /**
   * Test record type with undefined properties.
   */
  public void testRecordTypeUndefinedProperties() {
    Map<String, RecordProperty> props = Maps.newHashMap();
    props.put("a", propA);
    RecordType recordType = new RecordType(registry, props);

    try {
      recordType.defineProperty("b", registry.getNativeType(JSTypeNative.UNDEFINED), true, null);
      fail("Expected an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  /**
   * Test record type with invalid property nodes.
   */
  public void testRecordTypeInvalidPropertyNodes() {
    Map<String, RecordProperty> props = Maps.newHashMap();
    props.put("a", propA);
    RecordType recordType = new RecordType(registry, props);

    try {
      recordType.defineProperty("b", registry.getNativeType(JSTypeNative.OBJECT_TYPE), true, null);
      fail("Expected an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  /**
   * Test record type with duplicate properties.
   */
  public void testRecordTypeDuplicateProperties() {
    Map<String, RecordProperty> props = Maps.newHashMap();
    props.put("a", propA);
    props.put("a", propA);
    RecordType recordType = new RecordType(registry, props);

    try {
      recordType.defineProperty("a", propA.getType(), true, null);
      fail("Expected an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  /**
   * Test record type with unknown property types.
   */
  public void testRecordTypeUnknownPropertyTypes() {
    Map<String, RecordProperty> props = Maps.newHashMap();
    props.put("a", propA);
    props.put("b", new RecordProperty("b", null));
    RecordType recordType = new RecordType(registry, props);

    try {
      recordType.resolve(null, null);
      fail("Expected an IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  /**
   * Test record type with incompatible property types.
   */
  public void testRecordTypeIncompatiblePropertyTypes() {
    Map<String, RecordProperty> props = Maps.newHashMap();
    props.put("a", propA);
    props.put("b", new RecordProperty("b", registry.getNumberType().getLeastSubtype(registry.getBooleanType())));
    RecordType recordType = new RecordType(registry, props);

    try {
      recordType.resolve(null, null);
      fail("Expected an IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  /**
   * Test record type with invalid property names.
   */
  public void testRecordTypeInvalidPropertyNames() {
    Map<String, RecordProperty> props = Maps.newHashMap();
    props.put("a", propA);
    props.put("\u0000", registry.getBooleanType());
    RecordType recordType = new RecordType(registry, props);

    try {
      recordType.resolve(null, null);
      fail("Expected an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }
}