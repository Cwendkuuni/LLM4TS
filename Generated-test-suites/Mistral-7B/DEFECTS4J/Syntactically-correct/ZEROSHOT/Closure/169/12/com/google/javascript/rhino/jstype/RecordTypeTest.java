import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JsContext;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

  private static JSTypeRegistry registry;
  private static ErrorReporter t;
  private static JsContext context;

  @org.junit.BeforeClass
  public static void setUp() {
    registry = new JSTypeRegistry();
    t = new ErrorReporter();
    context = JsContext.newJsContext(JsContext.JSMODE_STRICT);
  }

  @org.junit.Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    assertNotNull(recordType);
    assertEquals("RecordType", recordType.getClassName());
    assertEquals(2, recordType.getPropertyCount());
    assertFalse(recordType.isFrozen());
  }

  @org.junit.Test
  public void testRecordTypeFrozen() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    assertFalse(recordType.isFrozen());

    recordType.freeze();
    assertTrue(recordType.isFrozen());
  }

  @org.junit.Test
  public void testRecordTypeGetProperty() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    JSType nameType = recordType.getPropertyType("name");
    assertEquals(String.class, nameType.getClassName());

    JSType ageType = recordType.getPropertyType("age");
    assertEquals(Number.class, ageType.getClassName());
  }

  @org.junit.Test
  public void testRecordTypeHasProperty() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    assertTrue(recordType.hasProperty("name"));
    assertTrue(recordType.hasProperty("age"));
    assertFalse(recordType.hasProperty("unknownProperty"));
  }

  @org.junit.Test
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Number.class));
    recordProperties2.put("gender", new RecordProperty("gender", String.class));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType2));
    assertTrue(recordType2.isSubtype(recordType1));
  }

  @org.junit.Test
  public void testRecordTypeSubtypingWithUnknownProperties() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Number.class));
    recordProperties2.put("gender", new RecordProperty("gender", Object.class));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    assertFalse(recordType1.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType1));

    RecordType recordType1Subset = new RecordType(registry, recordProperties1, false);
    RecordType recordType2Subset = new RecordType(registry, recordProperties2, false);

    assertTrue(recordType1Subset.isSubtype(recordType2Subset));
    assertTrue(recordType2Subset.isSubtype(recordType1Subset));
  }

  @org.junit.Test
  public void testRecordTypeGreatestSubtype() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Number.class));
    recordProperties2.put("gender", new RecordProperty("gender", String.class));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

    assertEquals(recordType1.getClass(), greatestSubtype.getClass());

    Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
    recordProperties3.put("name", new RecordProperty("name", Object.class));
    recordProperties3.put("age", new RecordProperty("age", Object.class));
    recordProperties3.put("gender", new RecordProperty("gender", Object.class));

    RecordType recordType3 = new RecordType(registry, recordProperties3, true);
    greatestSubtype = recordType1.getGreatestSubtype(recordType3);

    assertEquals(recordType3.getClass(), greatestSubtype.getClass());
  }

  @org.junit.Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Number.class));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));
  }

  @org.junit.Test
  public void testRecordTypeEquivalenceWithUnknownProperties() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Number.class));
    recordProperties2.put("gender", new RecordProperty("gender", Object.class));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));

    Map<String, RecordProperty> recordProperties1Subset = Maps.newHashMap();
    recordProperties1Subset.put("name", new RecordProperty("name", String.class));

    RecordType recordType1Subset = new RecordType(registry, recordProperties1Subset, false);
    RecordType recordType2Subset = new RecordType(registry, recordProperties2, false);

    assertTrue(recordType1Subset.checkRecordEquivalenceHelper(recordType2Subset, true));
  }

  @org.junit.Test
  public void testRecordTypeUnionSubtyping() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    JSType unionType = new UnionTypeBuilder(registry)
        .addAlternate(recordType)
        .addAlternate(Object.getType(registry))
        .build();

    JSType nativeObjectType = registry.getNativeObjectType(
        JSTypeNative.OBJECT_TYPE);

    assertTrue(unionType.isSubtype(nativeObjectType));
    assertTrue(nativeObjectType.isSubtype(unionType));
  }

  @org.junit.Test
  public void testRecordTypeToString() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    String expected = "{ name: String, age: Number }";

    assertEquals(expected, recordType.toString());
  }

  @org.junit.Test
  public void testRecordTypeFreeze() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    recordType.freeze();

    assertFalse(recordType.isFrozen());
    try {
      recordType.addProperty("newProperty", Object.class, null);
      fail();
    } catch (IllegalStateException e) {
      // expected
    }
  }
}