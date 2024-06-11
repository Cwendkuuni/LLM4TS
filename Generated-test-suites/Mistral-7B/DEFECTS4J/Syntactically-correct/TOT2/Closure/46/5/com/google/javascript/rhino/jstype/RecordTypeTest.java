import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeRegistry;
import com.google.javascript.rhino.testing.TypeEquivalenceTester;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  private RecordTypeRegistry registry;
  private RecordType recordType;

  @Before
  public void setUp() {
    registry = new RecordTypeRegistry();

    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties.put("age", new RecordProperty("age", registry.getNumberType()));

    recordType = new RecordType(registry, recordProperties);
  }

  @Test
  public void testIsEquivalentToSameRecordType() {
    RecordType recordType1 = new RecordType(registry, recordProperties);
    assertTrue(recordType.isEquivalentTo(recordType1));
  }

  @Test
  public void testIsEquivalentToDifferentRecordTypeWithSameProperties() {
    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties2.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType2 = new RecordType(registry, recordProperties2);
    assertTrue(recordType.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsEquivalentToDifferentRecordTypeWithDifferentProperties() {
    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties2.put("age", new RecordProperty("age", registry.getNumberType()));
    recordProperties2.put("city", new RecordProperty("city", registry.getStringType()));

    RecordType recordType2 = new RecordType(registry, recordProperties2);
    assertFalse(recordType.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsEquivalentToNull() {
    assertFalse(recordType.isEquivalentTo(null));
  }

  @Test
  public void testIsEquivalentToNonRecordType() {
    JSType nonRecordType = registry.getBooleanType();
    assertFalse(recordType.isEquivalentTo(nonRecordType));
  }

  @Test
  public void testIsEquivalentToRecordWithMissingProperty() {
    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType2 = new RecordType(registry, recordProperties2);
    assertFalse(recordType.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsSubtypeOfSelf() {
    assertTrue(recordType.isSubtype(recordType));
  }

  @Test
  public void testIsSubtypeOfRecordWithSameProperties() {
    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties2.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType2 = new RecordType(registry, recordProperties2);
    assertTrue(recordType.isSubtype(recordType2));
  }

  @Test
  public void testIsSubtypeOfRecordWithFewerProperties() {
    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType2 = new RecordType(registry, recordProperties2);
    assertTrue(recordType.isSubtype(recordType2));
  }

  @Test
  public void testIsSubtypeOfRecordWithMoreProperties() {
    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties2.put("age", new RecordProperty("age", registry.getNumberType()));
    recordProperties2.put("city", new RecordProperty("city", registry.getStringType()));

    RecordType recordType2 = new RecordType(registry, recordProperties2);
    assertFalse(recordType.isSubtype(recordType2));
  }

  @Test
  public void testIsSubtypeOfNull() {
    assertFalse(recordType.isSubtype(null));
  }

  @Test
  public void testIsSubtypeOfNonRecordType() {
    JSType nonRecordType = registry.getBooleanType();
    assertFalse(recordType.isSubtype(nonRecordType));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSType implicitPrototype = recordType.getImplicitPrototype();
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
  }

  @Test
  public void testDefineProperty() {
    String propertyName = "greeting";
    JSType propertyType = registry.getStringType();

    recordType.defineProperty(propertyName, propertyType, false, null);

    JSType property = recordType.getPropertyType(propertyName);
    assertEquals(propertyType, property);
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyFrozenRecord() {
    recordType.isFrozen = true;
    recordType.defineProperty("name", registry.getStringType(), false, null);
  }

  @Test
  public void testGetLeastSupertype() {
    JSType leastCommonSupertype = recordType.getLeastSupertype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), leastCommonSupertype);

    RecordType recordType2 = new RecordType(registry, recordProperties2);
    JSType leastCommonSupertype2 = recordType.getLeastSupertype(recordType2);
    assertEquals(recordType, leastCommonSupertype2);
  }

  @Test
  public void testGetGreatestSubtype() {
    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties2.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType2 = new RecordType(registry, recordProperties2);
    JSType greatestSubtype = recordType.getGreatestSubtype(recordType2);
    assertEquals(recordType, greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeWithNull() {
    JSType greatestSubtype = recordType.getGreatestSubtype(null);
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeWithDifferentRecordTypes() {
    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties2.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType2 = new RecordType(registry, recordProperties2);
    JSType greatestSubtype = recordType.getGreatestSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype);
  }

  @Test
  public void testToString() {
    String expected = "RecordType{name=class RecordType, properties={name=String, age=Number}}";
    assertEquals(expected, recordType.toString());
  }

  @Test
  public void testIsInstance() {
    RecordType recordTypeInstance = new RecordType(registry, recordProperties);
    assertTrue(recordType.isInstance(recordTypeInstance));
  }

  @Test
  public void testIsInstanceWithDifferentRecordType() {
    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties2.put("age", new RecordProperty("age", registry.getNumberType()));
    recordProperties2.put("city", new RecordProperty("city", registry.getStringType()));

    RecordType differentRecordType = new RecordType(registry, recordProperties2);
    assertFalse(recordType.isInstance(differentRecordType));
  }

  @Test
  public void testIsInstanceWithInvalidType() {
    JSType invalidType = registry.getBooleanType();
    assertFalse(recordType.isInstance(invalidType));
  }

  @Test
  public void testIsInstanceWithNull() {
    assertFalse(recordType.isInstance(null));
  }

  @Test
  public void testIsSubtypeOfRecordWithTypeMismatchedProperty() {
    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties2.put("age", new RecordProperty("age", registry.getNumberType()));
    recordProperties2.put("city", new RecordProperty("city", registry.getBooleanType()));

    RecordType recordType2 = new RecordType(registry, recordProperties2);
    assertFalse(recordType.isSubtype(recordType2));
  }

  @Test
  public void testIsSubtypeOfRecordWithMissingProperty() {
    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType2 = new RecordType(registry, recordProperties2);
    assertFalse(recordType.isSubtype(recordType2));
  }

  @Test
  public void testIsSubtypeOfRecordWithExtraProperty() {
    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties2.put("age", new RecordProperty("age", registry.getNumberType()));
    recordProperties2.put("city", new RecordProperty("city", registry.getStringType()));

    RecordType recordType2 = new RecordType(registry, recordProperties2);
    assertFalse(recordType.isSubtype(recordType2));
  }

  @Test
  public void testTypeEquivalence() {
    TypeEquivalenceTester tester = new TypeEquivalenceTester();
    tester.testTypesAreEquivalent(recordType, recordType);
  }

  @Test
  public void testTypeEquivalenceWithDifferentRecordTypes() {
    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties2.put("age", new RecordProperty("age", registry.getNumberType()));
    recordProperties2.put("city", new RecordProperty("city", registry.getStringType()));

    RecordType recordType2 = new RecordType(registry, recordProperties2);
    tester.testTypesAreNotEquivalent(recordType, recordType2);
  }

  @Test
  public void testTypeEquivalenceWithNull() {
    tester.testTypesAreNotEquivalent(recordType, null);
  }

  @Test
  public void testTypeEquivalenceWithNonRecordType() {
    JSType nonRecordType = registry.getBooleanType();
    tester.testTypesAreNotEquivalent(recordType, nonRecordType);
  }
}