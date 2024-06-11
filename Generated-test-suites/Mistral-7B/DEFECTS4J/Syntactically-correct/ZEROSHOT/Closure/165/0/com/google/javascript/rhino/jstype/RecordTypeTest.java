import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @After
  public void tearDown() {
    registry = null;
  }

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("x", new RecordProperty("x", registry.getNumberType()));
    recordProperties.put("y", new RecordProperty("y", registry.getStringType()));

    RecordType expectedRecordType = new RecordType(registry, recordProperties);
    RecordType actualRecordType = new RecordType(registry, recordProperties);

    assertEquals(expectedRecordType.getClass(), actualRecordType.getClass());
    assertEquals(expectedRecordType.getPrettyPrint(), actualRecordType.getPrettyPrint());
    assertEquals(expectedRecordType.isFrozen(), actualRecordType.isFrozen());
    assertEquals(expectedRecordType.getProperties(), actualRecordType.getProperties());
  }

  @Test
  public void testRecordTypeEquals() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("x", new RecordProperty("x", registry.getNumberType()));
    recordProperties.put("y", new RecordProperty("y", registry.getStringType()));

    RecordType expectedRecordType1 = new RecordType(registry, recordProperties);
    RecordType actualRecordType1 = new RecordType(registry, recordProperties);
    RecordType expectedRecordType2 = new RecordType(registry, recordProperties);

    assertEquals(expectedRecordType1, actualRecordType1);
    assertEquals(expectedRecordType1.hashCode(), actualRecordType1.hashCode());
    assertEquals(expectedRecordType1, expectedRecordType2);
    assertEquals(expectedRecordType1.hashCode(), expectedRecordType2.hashCode());
  }

  @Test
  public void testRecordTypeIsEquivalentTo() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("x", new RecordProperty("x", registry.getNumberType()));
    recordProperties1.put("y", new RecordProperty("y", registry.getStringType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("x", new RecordProperty("x", registry.getNumberType()));
    recordProperties2.put("y", new RecordProperty("y", registry.getStringType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testRecordTypeIsNotEquivalentToDifferentProperties() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("x", new RecordProperty("x", registry.getNumberType()));
    recordProperties1.put("y", new RecordProperty("y", registry.getStringType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("x", new RecordProperty("x", registry.getNumberType()));
    recordProperties2.put("z", new RecordProperty("z", registry.getStringType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testRecordTypeGetImplicitPrototype() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("x", new RecordProperty("x", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, recordProperties);

    ObjectType expectedPrototype = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    ObjectType actualPrototype = recordType.getImplicitPrototype();

    assertEquals(expectedPrototype.getClass(), actualPrototype.getClass());
    assertEquals(expectedPrototype.getPrettyPrint(), actualPrototype.getPrettyPrint());
  }

  @Test
  public void testRecordTypeDefineProperty() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("x", new RecordProperty("x", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, recordProperties);

    recordType.defineProperty("y", registry.getStringType(), false, null);

    assertTrue(recordType.hasProperty("y"));
    assertEquals(registry.getStringType(), recordType.getPropertyType("y"));
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeDefinePropertyFrozen() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("x", new RecordProperty("x", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, recordProperties);
    recordType.isFrozen = true;

    recordType.defineProperty("y", registry.getStringType(), false, null);

    fail("Expected an IllegalStateException");
  }

  @Test
  public void testRecordTypeGetGreatestSubtype() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("x", new RecordProperty("x", registry.getNumberType()));
    recordProperties1.put("y", new RecordProperty("y", registry.getStringType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("x", new RecordProperty("x", registry.getNumberType()));
    recordProperties2.put("z", new RecordProperty("z", registry.getStringType()));

    Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
    recordProperties3.put("x", new RecordProperty("x", registry.getNumberType()));
    recordProperties3.put("y", new RecordProperty("y", registry.getStringType()));
    recordProperties3.put("z", new RecordProperty("z", registry.getStringType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);
    RecordType recordType3 = new RecordType(registry, recordProperties3);

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

    assertEquals(recordType3.getClass(), greatestSubtype.getClass());
    assertEquals(recordType3.getPrettyPrint(), greatestSubtype.getPrettyPrint());
  }

  @Test
  public void testRecordTypeGetGreatestSubtypeEmptyObjectType() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();

    RecordType recordType = new RecordType(registry, recordProperties);

    JSType greatestSubtype = recordType.getGreatestSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getClass(), greatestSubtype.getClass());
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getPrettyPrint(), greatestSubtype.getPrettyPrint());
  }

  @Test
  public void testRecordTypeGetGreatestSubtypeNoType() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();

    RecordType recordType = new RecordType(registry, recordProperties);

    JSType greatestSubtype = recordType.getGreatestSubtype(registry.getNativeType(JSTypeNative.NO_TYPE));

    assertEquals(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE).getClass(), greatestSubtype.getClass());
    assertEquals(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE).getPrettyPrint(), greatestSubtype.getPrettyPrint());
  }

  @Test
  public void testRecordTypeIsSubtype() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("x", new RecordProperty("x", registry.getNumberType()));
    recordProperties1.put("y", new RecordProperty("y", registry.getStringType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("x", new RecordProperty("x", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testRecordTypeIsNotSubtypeDifferentProperties() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("x", new RecordProperty("x", registry.getNumberType()));
    recordProperties1.put("y", new RecordProperty("y", registry.getStringType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("x", new RecordProperty("x", registry.getNumberType()));
    recordProperties2.put("z", new RecordProperty("z", registry.getStringType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testRecordTypeIsSubtypeNoRecordType() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();

    RecordType recordType = new RecordType(registry, recordProperties);

    assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
  }

  @Test
  public void testRecordTypeIsSubtypeRecordTypeWithLessProperties() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("x", new RecordProperty("x", registry.getNumberType()));
    recordProperties1.put("y", new RecordProperty("y", registry.getStringType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("x", new RecordProperty("x", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertTrue(recordType2.isSubtype(recordType1));
  }

  @Test
  public void testRecordTypeResolveInternal() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("x", new RecordProperty("x", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, recordProperties);
    Node xPropertyNode = new ObjectLiteral();

    JSType resolvedType = recordType.resolveInternal(null, null);
    JSType expectedType = new RecordType(registry, recordProperties);

    assertEquals(expectedType.getClass(), resolvedType.getClass());
    assertEquals(expectedType.getPrettyPrint(), resolvedType.getPrettyPrint());

    recordType.getPropertyType("x").resolveInternal(null, null);
    xPropertyNode.setType(recordType);
    resolvedType = recordType.resolveInternal(null, null);

    assertEquals(expectedType.getClass(), resolvedType.getClass());
    assertEquals(expectedType.getPrettyPrint(), resolvedType.getPrettyPrint());
  }
}