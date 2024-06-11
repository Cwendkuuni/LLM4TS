import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", registry.anyType()));
    properties.put("y", new RecordProperty("y", registry.anyType()));

    RecordType recordType = new RecordType(registry, properties);

    assertEquals(JSTypeNative.RECORD_TYPE, recordType.getTypeClass());
    assertFalse(recordType.isSynthesized());
    assertFalse(recordType.isFrozen());

    assertEquals(2, recordType.getPropertyCount());
    assertTrue(recordType.hasProperty("x"));
    assertTrue(recordType.hasProperty("y"));

    assertEquals("x", recordType.getPropertyNames().get(0));
    assertEquals("y", recordType.getPropertyNames().get(1));

    assertSame(registry.anyType(), recordType.getPropertyType("x"));
    assertSame(registry.anyType(), recordType.getPropertyType("y"));

    RecordType frozenRecordType = new RecordType(registry, properties);
    assertTrue(frozenRecordType.isFrozen());

    RecordType emptyRecordType = new RecordType(registry, Maps.newHashMap());
    assertEquals(0, emptyRecordType.getPropertyCount());
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("x", new RecordProperty("x", registry.anyType()));
    propertiesA.put("y", new RecordProperty("y", registry.anyType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("x", new RecordProperty("x", registry.anyType()));
    propertiesB.put("y", new RecordProperty("y", registry.anyType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isEquivalentTo(recordTypeB));

    propertiesB.put("z", new RecordProperty("z", registry.anyType()));
    assertFalse(recordTypeA.isEquivalentTo(new RecordType(registry, propertiesB)));
  }

  @Test
  public void testRecordTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("x", new RecordProperty("x", registry.anyType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("x", new RecordProperty("x", registry.anyType()));
    propertiesB.put("y", new RecordProperty("y", registry.anyType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isSubtype(recordTypeB));

    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("z", new RecordProperty("z", registry.anyType()));

    RecordType recordTypeC = new RecordType(registry, propertiesC);
    assertFalse(recordTypeA.isSubtype(recordTypeC));

    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertTrue(objectType.isSubtype(recordTypeA));
  }

  @Test
  public void testRecordTypeProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", registry.anyType()));
    properties.put("y", new RecordProperty("y", registry.anyType()));

    RecordType recordType = new RecordType(registry, properties);

    assertEquals(2, recordType.getPropertyCount());
    assertTrue(recordType.hasProperty("x"));
    assertTrue(recordType.hasProperty("y"));

    assertEquals("x", recordType.getPropertyNames().get(0));
    assertEquals("y", recordType.getPropertyNames().get(1));

    assertEquals(registry.anyType(), recordType.getPropertyType("x"));
    assertEquals(registry.anyType(), recordType.getPropertyType("y"));

    // Test getPropertyNode
    Node nodeX = registry.newTextNode("x");
    Node nodeY = registry.newTextNode("y");
    RecordProperty propertyX = new RecordProperty("x", registry.anyType());
    RecordProperty propertyY = new RecordProperty("y", registry.anyType());
    properties.put("x", propertyX);
    properties.put("y", propertyY);

    assertEquals(nodeX, recordType.getPropertyNode("x"));
    assertEquals(nodeY, recordType.getPropertyNode("y"));

    // Test getGreatestSubtype
    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
    unionTypeBuilder.addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    unionTypeBuilder.addAlternate(recordType);
    JSType greatestSubtype = unionTypeBuilder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, greatestSubtype.getLeastSupertype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    assertEquals(recordType, greatestSubtype.getLeastSupertype(recordType));
  }

  @Test
  public void testRecordTypeDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", registry.anyType()));

    RecordType recordType = new RecordType(registry, properties);

    assertFalse(recordType.defineProperty("x", registry.anyType(), false, null));
    assertTrue(recordType.defineProperty("z", registry.anyType(), false, null));

    assertEquals(3, recordType.getPropertyCount());
    assertTrue(recordType.hasProperty("x"));
    assertTrue(recordType.hasProperty("y"));
    assertTrue(recordType.hasProperty("z"));

    assertEquals("x", recordType.getPropertyNames().get(0));
    assertEquals("y", recordType.getPropertyNames().get(1));
    assertEquals("z", recordType.getPropertyNames().get(2));

    assertEquals(registry.anyType(), recordType.getPropertyType("x"));
    assertEquals(registry.anyType(), recordType.getPropertyType("y"));
    assertEquals(registry.anyType(), recordType.getPropertyType("z"));
  }

  @Test
  public void testRecordTypeFreeze() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", registry.anyType()));

    RecordType recordType = new RecordType(registry, properties);
    assertFalse(recordType.isFrozen());

    recordType.freeze();
    assertTrue(recordType.isFrozen());

    assertFalse(recordType.defineProperty("x", registry.anyType(), false, null));
    assertFalse(recordType.defineProperty("y", registry.anyType(), false, null));
  }

  @Test
  public void testRecordTypeHasProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", registry.anyType()));

    RecordType recordType = new RecordType(registry, properties);

    assertTrue(recordType.hasProperty("x"));
    assertFalse(recordType.hasProperty("y"));

    recordType.defineProperty("y", registry.anyType(), false, null);

    assertTrue(recordType.hasProperty("x"));
    assertTrue(recordType.hasProperty("y"));
  }

  @Test
  public void testRecordTypeHasPropertyDeclared() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", registry.anyType(), Node.NEW));

    RecordType recordType = new RecordType(registry, properties);

    assertTrue(recordType.isPropertyTypeDeclared("x"));
    assertFalse(recordType.isPropertyTypeDeclared("y"));
  }

  @Test
  public void testRecordTypePropertyTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("x", new RecordProperty("x", registry.anyType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("x", new RecordProperty("x", registry.booleanType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertEquals(registry.anyType(), recordTypeA.getPropertyType("x"));
    assertEquals(registry.booleanType(), recordTypeB.getPropertyType("x"));

    assertTrue(recordTypeA.getPropertyType("x").isSubtype(recordTypeB.getPropertyType("x")));
  }

  @Test
  public void testRecordTypeResolve() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", registry.anyType()));

    RecordType recordType = new RecordType(registry, properties);

    JSType resolvedType = recordType.resolve(null, null);
    assertNotNull(resolvedType);
    assertEquals(recordType.getClass(), resolvedType.getClass());

    properties.put("y", new RecordProperty("y", registry.booleanType()));

    resolvedType = recordType.resolve(null, null);

    assertEquals(3, recordType.getPropertyCount());
    assertTrue(recordType.hasProperty("x"));
    assertTrue(recordType.hasProperty("y"));
  }
}