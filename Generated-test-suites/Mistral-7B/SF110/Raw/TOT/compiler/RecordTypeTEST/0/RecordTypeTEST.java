import static org.junit.Assert.*;
import static com.google.javascript.rhino.testing.TypeCheckMode.*;

import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.PrototypeObjectType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.testing.TestContext;
import com.google.javascript.rhino.testing.TypeCheckedFunction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {

  private static RecordType buildRecordType(JSTypeRegistry registry,
      String... propertyNames) {
    Map<String, RecordProperty> properties = new HashMap<>();
    for (String propertyName : propertyNames) {
      properties.put(propertyName,
          new RecordProperty(registry.getInstanceType(String.class), null));
    }
    return new RecordType(registry, properties, true);
  }

  @org.junit.Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = buildRecordType(registry, "a", "b");
    assertEquals(JSType.OBJECT_TYPE, recordType.getImplicitPrototype());
    assertEquals(2, recordType.getOwnPropertyNames().size());
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testRecordTypeCreationWithNullProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", null);
    new RecordType(registry, properties, true);
  }

  @org.junit.Test
  public void testRecordTypeFreeze() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = buildRecordType(registry, "a", "b");
    recordType.freeze();
    assertFalse(recordType.isSynthetic());
  }

  @org.junit.Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = buildRecordType(registry, "a", "b");
    RecordType recordType2 = buildRecordType(registry, "a", "b");
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2,
        EquivalenceMethod.PROTOTYPE));
  }

  @org.junit.Test
  public void testRecordTypeNonEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = buildRecordType(registry, "a", "b");
    RecordTypeBuilder recordType2Builder = new RecordTypeBuilder(registry);
    recordType2Builder.setSynthesized(true);
    recordType2Builder.addProperty("c", registry.getInstanceType(String.class),
        null);
    RecordType recordType2 = recordType2Builder.build();
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2,
        EquivalenceMethod.PROTOTYPE));
  }

  @org.junit.Test
  public void testRecordTypeSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = buildRecordType(registry, "a", "b");
    RecordType recordType2 = buildRecordType(registry, "a", "b", "c");
    assertTrue(recordType1.isSubtype(recordType2));
  }

  @org.junit.Test
  public void testRecordTypeSubtypingFailure() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = buildRecordType(registry, "a", "b");
    RecordType recordType2 = buildRecordType(registry, "c", "d");
    assertFalse(recordType1.isSubtype(recordType2));
  }

  @org.junit.Test
  public void testRecordTypeGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = buildRecordType(registry, "a", "b");
    RecordType recordType2 = buildRecordType(registry, "a", "b", "c");
    RecordType recordType3 = buildRecordType(registry, "a", "b", "c", "d");

    // Record type 1 is a subtype of record type 2, so record type 2 is a
    // greatest subtype of record type 1.
    assertEquals(recordType2, recordType1.getGreatestSubtype(recordType2));

    // Record type 1 is not a subtype of record type 3, but record type 2
    // is. So record type 3 is a greatest subtype of record type 1.
    assertEquals(recordType3, recordType1.getGreatestSubtype(recordType3));

    // Record type 1 is not a subtype of record type 2, so record type 2 is
    // not a greatest subtype of record type 1.
    assertNotNull(recordType1.getGreatestSubtype(recordType1));
    assertNotEquals(recordType1, recordType1.getGreatestSubtype(recordType1));
    assertNotEquals(recordType2, recordType1.getGreatestSubtype(recordType1));
  }

  @org.junit.Test
  public void testRecordTypeGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = buildRecordType(registry, "a", "b");
    RecordType recordType2 = buildRecordType(registry, "a", "b", "c");
    RecordType recordType3 = buildRecordType(registry, "a", "b", "c", "d");

    // Record type 1 and record type 2 are equivalent, so their greatest
    // common supertype is the union of their types.
    JSType greatestSubtype12 = recordType1.getGreatestSubtype(recordType2);
    UnionTypeBuilder unionBuilder = new UnionTypeBuilder(registry);
    unionBuilder.addAlternate(recordType1);
    unionBuilder.addAlternate(recordType2);
    JSType greatestCommonSupertype12 = unionBuilder.build();
    assertEquals(greatestCommonSupertype12, greatestSubtype12);

    // Record type 2 and record type 3 are not equivalent, so their greatest
    // common supertype is the empty type.
    JSType greatestSubtype23 = recordType2.getGreatestSubtype(recordType3);
    assertEquals(registry.getNativeType(JSTypeNative.NO_TYPE),
        greatestSubtype23);

    // Record type 1 and record type 3 are not equivalent, but they both
    // have a common supertype: record type 2. So the least supertype of
    // record type 1 and record type 3 is record type 2.
    JSType leastSupertype13 = recordType1.getLeastSupertype(recordType3);
    assertEquals(recordType2, leastSupertype13);
  }

  @org.junit.Test
  public void testRecordTypePropertyAccess() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = buildRecordType(registry, "a", "b");
    assertNull(recordType.getProperty("c"));
    assertEquals(registry.getInstanceType(String.class),
        recordType.getPropertyType("a"));
    assertEquals(registry.getInstanceType(String.class),
        recordType.getPropertyType("b"));
  }

  @org.junit.Test
  public void testRecordTypePropertyAccessError() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = buildRecordType(registry, "a", "b");
    try {
      recordType.getProperty("c");
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Pass
    }
  }

  @org.junit.Test
  public void testRecordTypePropertyAssignment() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = buildRecordType(registry, "a", "b");
    Node propNode = new Node_String("hello");
    recordType.defineProperty("a", registry.getInstanceType(String.class),
        true, propNode);
    assertEquals("hello", recordType.getProperty("a"));
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testRecordTypePropertyAssignmentError() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = buildRecordType(registry, "a", "b");
    recordType.defineProperty("a", registry.getInstanceType(String.class),
        false, null);
  }

  @org.junit.Test
  public void testRecordTypePropertyAssignmentSynthesized() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = buildRecordType(registry, "a", "b");
    recordType.freeze();
    Node propNode = new Node_String("hello");
    recordType.defineSynthesizedProperty("a", registry.getInstanceType(String.class),
        true, propNode);
    assertEquals("hello", recordType.getProperty("a"));
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testRecordTypePropertyAssignmentErrorSynthesized() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = buildRecordType(registry, "a", "b");
    recordType.freeze();
    recordType.defineSynthesizedProperty("a", registry.getInstanceType(String.class),
        false, null);
  }

  @org.junit.Test
  public void testRecordTypeCustomChecking() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = buildRecordType(registry, "a", "b");
    TypeCheckedFunction<RecordType> checker =
        new TypeCheckedFunction<RecordType>() {
          @Override
          public void check(RecordType type) {
            assertEquals(registry.getInstanceType(String.class),
                type.getPropertyType("a"));
            assertEquals(registry.getInstanceType(String.class),
                type.getPropertyType("b"));
          }
        };
    registry.checkType(recordType, checker, FULL_TYPE_CHECK);
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testRecordTypeCustomCheckingError() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = buildRecordType(registry, "a", "b");
    TypeCheckedFunction<RecordType> checker =
        new TypeCheckedFunction<RecordType>() {
          @Override
          public void check(RecordType type) {
            assertEquals(registry.getInstanceType(Number.class),
                type.getPropertyType("a"));
            assertEquals(registry.getInstanceType(String.class),
                type.getPropertyType("b"));
          }
        };
    registry.checkType(recordType, checker, FULL_TYPE_CHECK);
  }
}