import static org.junit.Assert.*;
import static com.google.javascript.rhino.testing.Assertions.assertTypeEquals;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeFactory;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Arrays;
import java.util.Map;

public class RecordTypeTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();
  private static final ErrorReporter errorReporter = new ErrorReporter();

  @org.junit.BeforeClass
  public static void setUp() {
    registry.init();
  }

  @org.junit.AfterClass
  public static void tearDown() {
    registry.shutdown();
  }

  @org.junit.Test
  public void testRecordTypeCreation() {
    // Test creating a record type with two properties
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", JSTypeFactory.booleanType()));
    properties.put("prop2", new RecordProperty("prop2", JSTypeFactory.stringType()));

    RecordType recordType = new RecordType(registry, properties, true);
    assertEquals(RecordType.class, recordType.getClass());
    assertFalse(recordType.isSynthetic());

    // Test creating a synthesized record type
    properties.clear();
    properties.put("prop1", new RecordProperty("prop1", JSTypeFactory.booleanType()));
    properties.put("prop2", new RecordProperty("prop2", JSTypeFactory.stringType()));

    recordType = new RecordType(registry, properties, false);
    assertEquals(RecordType.class, recordType.getClass());
    assertTrue(recordType.isSynthetic());
  }

  @org.junit.Test
  public void testRecordTypeProperties() {
    // Test adding properties to a record type
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", JSTypeFactory.booleanType()));
    properties.put("prop2", new RecordProperty("prop2", JSTypeFactory.stringType()));

    RecordType recordType = new RecordType(registry, properties, true);

    // Test getting and setting properties
    assertEquals(JSTypeFactory.booleanType(), recordType.getPropertyType("prop1"));
    assertEquals(JSTypeFactory.stringType(), recordType.getPropertyType("prop2"));

    recordType.defineProperty("prop3", JSTypeFactory.numberType(), false, null);
    assertEquals(JSTypeFactory.numberType(), recordType.getPropertyType("prop3"));
  }

  @org.junit.Test
  public void testRecordTypeEquivalence() {
    // Test record type equivalence
    Map<String, RecordProperty> props1 = Maps.newHashMap();
    props1.put("prop1", new RecordProperty("prop1", JSTypeFactory.booleanType()));
    props1.put("prop2", new RecordProperty("prop2", JSTypeFactory.stringType()));

    Map<String, RecordProperty> props2 = Maps.newHashMap();
    props2.put("prop1", new RecordProperty("prop1", JSTypeFactory.booleanType()));
    props2.put("prop2", new RecordProperty("prop2", JSTypeFactory.stringType()));
    props2.put("prop3", new RecordProperty("prop3", JSTypeFactory.numberType()));

    RecordType recordType1 = new RecordType(registry, props1, true);
    RecordType recordType2 = new RecordType(registry, props2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));

    // Test non-equivalent record types
    RecordType recordType3 = new RecordType(registry, props1, true);
    RecordType recordType4 = new RecordType(registry, props2, true);
    assertFalse(recordType3.checkRecordEquivalenceHelper(recordType4, false));

    // Test non-equivalent record types with tolerating unknowns
    props1.put("prop3", new RecordProperty("prop3", JSTypeFactory.unknownType()));
    props2.put("prop3", new RecordProperty("prop3", JSTypeFactory.numberType()));

    recordType1 = new RecordType(registry, props1, true);
    recordType2 = new RecordType(registry, props2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
  }

  @org.junit.Test
  public void testRecordTypeSubtyping() {
    // Test record type subtyping
    Map<String, RecordProperty> props1 = Maps.newHashMap();
    props1.put("prop1", new RecordProperty("prop1", JSTypeFactory.booleanType()));
    props1.put("prop2", new RecordProperty("prop2", JSTypeFactory.stringType()));

    RecordType recordType1 = new RecordType(registry, props1, true);

    Map<String, RecordProperty> props2 = Maps.newHashMap();
    props2.put("prop1", new RecordProperty("prop1", JSTypeFactory.booleanType()));
    props2.put("prop2", new RecordProperty("prop2", JSTypeFactory.stringType()));
    props2.put("prop3", new RecordProperty("prop3", JSTypeFactory.numberType()));

    RecordType recordType2 = new RecordType(registry, props2, true);

    // Test identity subtyping
    assertEquals(recordType1, recordType1.getGreatestSubtypeHelper(recordType1));

    // Test reflexive subtyping
    assertEquals(recordType1, recordType1.getGreatestSubtypeHelper(recordType2));
    assertEquals(recordType2, recordType2.getGreatestSubtypeHelper(recordType1));

    // Test transitive subtyping
    RecordType recordType3 =
        new RecordType(registry, Arrays.asList("prop1", "prop2").asMap(), true);

    assertEquals(recordType1, recordType1.getGreatestSubtypeHelper(recordType3));
    assertEquals(recordType3, recordType3.getGreatestSubtypeHelper(recordType2));
    assertEquals(recordType2, recordType2.getGreatestSubtypeHelper(recordType1));

    // Test non-subtyping
    Map<String, RecordProperty> propsNonSubtyping1 = Maps.newHashMap();
    propsNonSubtyping1.put("prop1", new RecordProperty("prop1", JSTypeFactory.booleanType()));
    propsNonSubtyping1.put("prop2", new RecordProperty("prop2", JSTypeFactory.numberType()));

    RecordType recordTypeNonSubtyping1 = new RecordType(registry, propsNonSubtyping1, true);

    Map<String, RecordProperty> propsNonSubtyping2 = Maps.newHashMap();
    propsNonSubtyping2.put("prop1", new RecordProperty("prop1", JSTypeFactory.booleanType()));
    propsNonSubtyping2.put("prop2", new RecordProperty("prop2", JSTypeFactory.stringType()));

    RecordType recordTypeNonSubtyping2 = new RecordType(registry, propsNonSubtyping2, true);

    assertFalse(recordType1.isSubtype(recordTypeNonSubtyping2));
    assertFalse(recordType1.isSubtype(recordTypeNonSubtyping1));
    assertFalse(recordTypeNonSubtyping2.isSubtype(recordType1));
    assertFalse(recordTypeNonSubtyping2.isSubtype(recordTypeNonSubtyping1));

    // Test subtyping with a property of the same name but different types
    Map<String, RecordProperty> propsDifferentTypes1 = Maps.newHashMap();
    propsDifferentTypes1.put("prop1", new RecordProperty("prop1", JSTypeFactory.booleanType()));
    propsDifferentTypes1.put("prop2", new RecordProperty("prop2", JSTypeFactory.numberType()));

    RecordType recordTypeDifferentTypes1 = new RecordType(registry, propsDifferentTypes1, true);

    Map<String, RecordProperty> propsDifferentTypes2 = Maps.newHashMap();
    propsDifferentTypes2.put("prop1", new RecordProperty("prop1", JSTypeFactory.booleanType()));
    propsDifferentTypes2.put("prop2", new RecordProperty("prop2", JSTypeFactory.stringType()));

    RecordType recordTypeDifferentTypes2 = new RecordType(registry, propsDifferentTypes2, true);

    assertFalse(recordType1.isSubtype(recordTypeDifferentTypes2));
    assertFalse(recordType1.isSubtype(recordTypeDifferentTypes1));
    assertFalse(recordTypeDifferentTypes2.isSubtype(recordType1));
    assertFalse(recordTypeDifferentTypes2.isSubtype(recordTypeDifferentTypes1));

    // Test subtyping with a property of the same name but an incompatible type
    Map<String, RecordProperty> propsIncompatible1 = Maps.newHashMap();
    propsIncompatible1.put("prop1", new RecordProperty("prop1", JSTypeFactory.booleanType()));
    propsIncompatible1.put("prop2", new RecordProperty("prop2", JSTypeFactory.numberType()));

    RecordType recordTypeIncompatible1 = new RecordType(registry, propsIncompatible1, true);

    Map<String, RecordProperty> propsIncompatible2 = Maps.newHashMap();
    propsIncompatible2.put("prop1", new RecordProperty("prop1", JSTypeFactory.booleanType()));
    propsIncompatible2.put("prop2", new RecordProperty("prop2", JSTypeFactory.booleanType()));

    RecordType recordTypeIncompatible2 = new RecordType(registry, propsIncompatible2, true);

    assertFalse(recordType1.isSubtype(recordTypeIncompatible2));
    assertFalse(recordType1.isSubtype(recordTypeIncompatible1));
    assertFalse(recordTypeIncompatible2.isSubtype(recordType1));
    assertFalse(recordTypeIncompatible2.isSubtype(recordTypeIncompatible1));

    // Test subtyping with a missing property
    Map<String, RecordProperty> propsMissing1 = Maps.newHashMap();
    propsMissing1.put("prop1", new RecordProperty("prop1", JSTypeFactory.booleanType()));

    RecordType recordTypeMissing1 = new RecordType(registry, propsMissing1, true);

    Map<String, RecordProperty> propsMissing2 = Maps.newHashMap();
    propsMissing2.put("prop1", new RecordProperty("prop1", JSTypeFactory.booleanType()));
    propsMissing2.put("prop3", new RecordProperty("prop3", JSTypeFactory.numberType()));

    RecordType recordTypeMissing2 = new RecordType(registry, propsMissing2, true);

    assertFalse(recordType1.isSubtype(recordTypeMissing2));
    assertFalse(recordType1.isSubtype(recordTypeMissing1));
    assertFalse(recordTypeMissing2.isSubtype(recordType1));
    assertFalse(recordTypeMissing2.isSubtype(recordTypeMissing1));
  }

  @org.junit.Test
  public void testRecordTypeGetGreatestSubtype() {
    // Test record type getGreatestSubtype
    Map<String, RecordProperty> props1 = Maps.newHashMap();
    props1.put("prop1", new RecordProperty("prop1", JSTypeFactory.booleanType()));
    props1.put("prop2", new RecordProperty("prop2", JSTypeFactory.stringType()));

    RecordType recordType1 = new RecordType(registry, props1, true);

    Map<String, RecordProperty> props2 = Maps.newHashMap();
    props2.put("prop1", new RecordProperty("prop1", JSTypeFactory.booleanType()));
    props2.put("prop2", new RecordProperty("prop2", JSTypeFactory.stringType()));
    props2.put("prop3", new RecordProperty("prop3", JSTypeFactory.numberType()));

    RecordType recordType2 = new RecordType(registry, props2, true);

    UnionTypeBuilder unionBuilder = new UnionTypeBuilder(registry);

    // Test getting the greatest subtype with an empty record type
    JSType emptyRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    JSType greatestSubtype1 = recordType1.getGreatestSubtypeHelper(emptyRecordType);
    assertEquals(recordType1, greatestSubtype1);

    // Test getting the greatest subtype with a record type that is a subtype
    JSType greatestSubtype2 = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(recordType2, greatestSubtype2);

    // Test getting the greatest subtype with a record type that is not a subtype
    Map<String, RecordProperty> propsNotSubtype1 = Maps.newHashMap();
    propsNotSubtype1.put("prop1", new RecordProperty("prop1", JSTypeFactory.booleanType()));
    propsNotSubtype1.put("prop2", new RecordProperty("prop2", JSTypeFactory.numberType()));

    RecordType recordTypeNotSubtype1 = new RecordType(registry, propsNotSubtype1, true);

    JSType greatestSubtype3 = recordType1.getGreatestSubtypeHelper(recordTypeNotSubtype1);
    assertEquals(recordType1, greatestSubtype3);

    // Test getting the greatest subtype with an unknown type
    JSType greatestSubtype4 = recordType1.getGreatestSubtypeHelper(JSTypeFactory.unknownType());
    assertEquals(JSTypeFactory.objectType(), greatestSubtype4);

    // Test getting the greatest subtype with a union type
    unionBuilder.addAlternate(recordType1);
    unionBuilder.addAlternate(recordType2);
    JSType greatestSubtype5 = recordType1.getGreatestSubtypeHelper(unionBuilder.build());
    assertEquals(recordType2, greatestSubtype5);
  }

  @org.junit.Test
  public void testRecordTypeToString() {
    // Test record type toString
    Map<String, RecordProperty> props = Maps.newHashMap();
    props.put("prop1", new RecordProperty("prop1", JSTypeFactory.booleanType()));
    props.put("prop2", new RecordProperty("prop2", JSTypeFactory.stringType()));

    RecordType recordType = new RecordType(registry, props, true);

    String stringRepresentation = recordType.toString();
    assertEquals("RecordType({prop1: boolean, prop2: string})", stringRepresentation);
  }
}