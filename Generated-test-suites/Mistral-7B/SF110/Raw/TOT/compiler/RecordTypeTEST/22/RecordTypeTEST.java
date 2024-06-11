import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", StringType.getInstance()));
    properties.put("age", new RecordProperty("age", NumberType.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);
    RecordType expectedRecordType = registry.getRecordType("RecordType", properties);

    assertEquals(expectedRecordType, recordType);
  }

  @Test
  public void testRecordTypeCreationWithNullRecordProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();

    // Should throw an IllegalStateException
    assertFalse(new RecordType(registry, properties, true) instanceof RecordType);
  }

  @Test
  public void testIsSynthetic() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", StringType.getInstance()));
    properties.put("age", new RecordProperty("age", NumberType.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, false);
    assertTrue(recordType.isSynthetic());

    RecordType declaredRecordType = new RecordType(registry, properties, true);
    assertFalse(declaredRecordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("name", new RecordProperty("name", StringType.getInstance()));
    properties1.put("age", new RecordProperty("age", NumberType.getNumberType()));

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("name", new RecordProperty("name", StringType.getInstance()));
    properties2.put("age", new RecordProperty("age", NumberType.getNumberType()));
    properties2.put("gender", new RecordProperty("gender", StringType.getInstance()));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.DEFAULT));
  }

  @Test
  public void testCheckRecordEquivalenceHelperWithDifferentProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("name", new RecordProperty("name", StringType.getInstance()));
    properties1.put("age", new RecordProperty("age", NumberType.getNumberType()));

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("name", new RecordProperty("name", StringType.getInstance()));
    properties2.put("city", new RecordProperty("city", StringType.getInstance()));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.DEFAULT));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, new HashMap<>(), true);

    ObjectType expectedPrototype = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    ObjectType actualPrototype = recordType.getImplicitPrototype();

    assertEquals(expectedPrototype, actualPrototype);
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, new HashMap<>(), true);

    String propertyName = "property";
    JSType propertyType = StringType.getInstance();
    Node propertyNode = new Node();

    assertTrue(recordType.defineProperty(propertyName, propertyType, false, propertyNode));
  }

  @Test
  public void testDefineProperty_FrozenRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, new HashMap<>(), true);
    recordType.setFrozen(true);

    String propertyName = "property";
    JSType propertyType = StringType.getInstance();
    Node propertyNode = new Node();

    assertFalse(recordType.defineProperty(propertyName, propertyType, false, propertyNode));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Create record type A
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", StringType.getInstance()));
    propertiesA.put("age", new RecordProperty("age", NumberType.getNumberType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    // Create record type B with a super property
    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", StringType.getInstance()));
    propertiesB.put("superProperty", new RecordProperty("superProperty", recordTypeA));

    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);

    // Record type B should be the greatest subtype of record type A
    assertEquals(recordTypeB, greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeHelper_WithEmptyObjectType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType greatestSubtype = RecordType.EMPTY_RECORD_TYPE.getGreatestSubtypeHelper(
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeHelper_WithRecordTypeAndUnionType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", StringType.getInstance()));
    propertiesA.put("age", new RecordProperty("age", NumberType.getNumberType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", StringType.getInstance()));
    propertiesB.put("age", new RecordProperty("age", NumberType.getNumberType()));

    UnionType unionType = new UnionTypeBuilder(registry)
        .addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE))
        .addAlternate(recordTypeA)
        .build();

    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(unionType);

    assertEquals(recordTypeA, greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", StringType.getInstance()));
    propertiesA.put("age", new RecordProperty("age", NumberType.getNumberType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", StringType.getInstance()));
    propertiesB.put("age", new RecordProperty("age", NumberType.getNumberType()));
    propertiesB.put("superProperty", new RecordProperty("superProperty", recordTypeA));

    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testIsSubtype_DifferentRecordTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", StringType.getInstance()));
    propertiesA.put("age", new RecordProperty("age", NumberType.getNumberType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", StringType.getInstance()));
    propertiesB.put("age", new RecordProperty("age", NumberType.getNumberType()));
    propertiesB.put("city", new RecordProperty("city", StringType.getInstance()));

    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }
}