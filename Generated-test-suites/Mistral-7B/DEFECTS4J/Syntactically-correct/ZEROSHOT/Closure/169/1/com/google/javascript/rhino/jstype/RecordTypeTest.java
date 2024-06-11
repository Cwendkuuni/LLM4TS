import static org.junit.Assert.*;
import static com.google.javascript.rhino.driver.testing.Assertions.assertTypesEqual;
import static com.google.javascript.rhino.driver.testing.Assertions.assertTypesSubType;
import static com.google.javascript.rhino.driver.testing.Assertions.assertTypesEqualCombined;
import static com.google.javascript.rhino.jstype.JSType.isSubtype;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.BooleanType;
import com.google.javascript.rhino.jstype.DoubleType;
import com.google.javascript.rhino.jstype.IntType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.ReferenceType;
import com.google.javascript.rhino.jstype.StringType;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter();
  }

  @Test
  public void testRecordType() {
    RecordProperty prop1 = new RecordProperty("prop1", new IntType());
    RecordProperty prop2 = new RecordProperty("prop2", new StringType());

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", prop1);
    properties.put("prop2", prop2);

    RecordType recordType = new RecordType(registry, properties, true);
    assertEquals("RecordType", recordType.getClass().getSimpleName());

    assertEquals(recordType.getName(), "RecordType{prop1: int, prop2: string}");
    assertEquals(recordType.toFlattenedType().toString(),
        "{prop1: int, prop2: string}");

    assertEquals(recordType.getCanonicalClassName(),
        "RecordType{prop1: int, prop2: string}");

    // Check that we can construct a record type from a map of properties
    RecordType recordTypeFromMap = new RecordType(registry, properties, false);
    assertEquals(recordTypeFromMap, recordType);

    // Check that we can define properties on the record type
    RecordProperty prop3 = new RecordProperty("prop3", new BooleanType());
    recordType.defineProperty("prop3", new BooleanType(), false, null);
    assertEquals(recordType.getPropertyTypes().size(), 3);
    assertTypesEqual(recordType.getPropertyType("prop1"), new IntType());
    assertTypesEqual(recordType.getPropertyType("prop2"), new StringType());
    assertTypesEqual(recordType.getPropertyType("prop3"), new BooleanType());

    // Check that we can't add a property to a frozen record type
    RecordType frozenRecordType = new RecordType(registry, properties, true);
    try {
      frozenRecordType.defineProperty("prop3", new BooleanType(), false, null);
      fail("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }

    // Check that we can check equivalence of record types
    RecordType recordType1 = new RecordType(registry, properties, true);
    RecordType recordType2 = new RecordType(registry, properties, true);
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));

    // Check that we can check subtyping of record types
    RecordType recordType3 = new RecordType(registry, properties, true);
    RecordType recordType4 = new RecordType(registry, properties, false);
    assertTrue(isSubtype(recordType3, recordType4));
    assertFalse(isSubtype(recordType4, recordType3));

    // Check that we can define a record type with a single property
    RecordType singlePropRecordType = new RecordType(registry, Maps.newHashMap(), true);
    assertEquals(singlePropRecordType.getName(), "RecordType{[property]: undefined}");
    assertEquals(singlePropRecordType.toFlattenedType().toString(),
        "{[property]: undefined}");

    // Check that we can construct a UnionType of two RecordTypes
    RecordType recordType5 = new RecordType(registry, properties, true);
    RecordType recordType6 = new RecordType(registry, properties, true);
    UnionType unionType = new UnionTypeBuilder(registry).addAlternate(recordType5)
        .addAlternate(recordType6).build();
    assertEquals(unionType.getName(), "UnionType(RecordType{prop1: int, prop2: string}, RecordType{prop1: int, prop2: string})");
    assertEquals(unionType.toFlattenedType().toString(),
        "{prop1: int, prop2: string} | {prop1: int, prop2: string}");

    // Check that we can assign a RecordType to a variable of type Object
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    RecordType recordType7 = new RecordType(registry, properties, true);
    RecordType recordType8 = new RecordType(registry, properties, true);
    ReferenceType referenceType = new ReferenceType(registry, null, null);
    referenceType.setPrototype(objectType);
    assertTypesEquivalent(recordType7.getImplicitPrototype(), objectType);
    referenceType.setPrototype(recordType7);
    assertTypesSubType(recordType8, referenceType);

    // Check that we can assign a RecordType to a variable of type RecordType
    RecordType recordType9 = new RecordType(registry, properties, true);
    RecordType recordType10 = new RecordType(registry, properties, true);
    assertTypesEqualCombined(recordType9, recordType10);

    // Check that we can assign a RecordType to a variable of any subtype
    RecordType recordType11 = new RecordType(registry, properties, true);
    RecordType recordType12 = new RecordType(registry, properties, false);
    assertTypesEqualCombined(recordType11, recordType12);

    // Check that we can assign a subtype of a RecordType to a variable of type RecordType
    RecordType recordType13 = new RecordType(registry, properties, false);
    RecordType recordType14 = new RecordType(registry, properties, true);
    assertTypesEqualCombined(recordType13, recordType14);

    // Check that we can assign a RecordType to a variable of type UnionType of RecordTypes
    UnionType unionType2 = new UnionTypeBuilder(registry)
        .addAlternate(recordType11)
        .addAlternate(recordType12)
        .build();
    assertTypesEqualCombined(recordType13, unionType2);

    // Check that we can assign a RecordType to a variable of type UnionType with other types
    UnionType unionType3 = new UnionTypeBuilder(registry)
        .addAlternate(recordType11)
        .addAlternate(new DoubleType())
        .build();
    try {
      assertTypesEqualCombined(recordType13, unionType3);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }
}