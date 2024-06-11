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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link RecordType}.
 */
public class RecordTypeTest {

  @Test
  public void testCreateRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordProperty prop1 = new RecordProperty("prop1", registry.intType());
    RecordProperty prop2 = new RecordProperty("prop2", registry.stringType());

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", prop1);
    properties.put("prop2", prop2);

    RecordType recordType = new RecordType(registry, properties);

    assertEquals(RecordType.class, recordType.getClass());
    assertEquals(2, recordType.getDeclaredProperties().length);
    assertEquals("prop1", recordType.getPropertyNames()[0]);
    assertEquals("prop2", recordType.getPropertyNames()[1]);
    assertEquals(registry.intType(), recordType.getPropertyType("prop1"));
    assertEquals(registry.stringType(), recordType.getPropertyType("prop2"));
  }

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordProperty prop1 = new RecordProperty("prop1", registry.intType());
    RecordProperty prop2 = new RecordProperty("prop2", registry.stringType());
    RecordProperty prop3 = new RecordProperty("prop3", registry.intType());

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", prop1);
    properties1.put("prop2", prop2);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", prop1);
    properties2.put("prop2", prop2);
    properties2.put("prop3", prop3);

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isEquivalentTo(recordType1));
    assertTrue(recordType1.isEquivalentTo(recordType2));

    RecordType recordType3 = new RecordType(registry, properties1);
    recordType3.setFrozen(true);

    assertFalse(recordType1.isEquivalentTo(recordType3));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordProperty prop = new RecordProperty("prop", registry.intType());

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop", prop);

    RecordType recordType = new RecordType(registry, properties);

    ObjectType implicitPrototype = recordType.getImplicitPrototype();

    assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getClass());
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordProperty prop = new RecordProperty("prop", registry.intType());

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop", prop);

    RecordType recordType = new RecordType(registry, properties);

    recordType.defineProperty("prop", registry.stringType(), false, null);

    assertEquals(registry.stringType(), recordType.getPropertyType("prop"));
  }

  @Test
  public void testGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordProperty prop1 = new RecordProperty("prop1", registry.intType());
    RecordProperty prop2 = new RecordProperty("prop2", registry.intType());

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", prop1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", prop1);
    properties2.put("prop2", prop2);

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
    unionTypeBuilder.addAlternate(recordType1);
    unionTypeType = unionTypeBuilder.build();

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

    assertEquals(unionTypeType, greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordProperty prop1 = new RecordProperty("prop1", registry.intType());
    RecordProperty prop2 = new RecordProperty("prop2", registry.intType());

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", prop1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", prop1);
    properties2.put("prop2", prop2);

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    assertEquals(true, recordType1.isSubtype(objectType));
    assertEquals(true, objectType.isSubtype(recordType1));

    assertEquals(true, recordType1.isSubtype(recordType1));

    assertEquals(true, recordType1.isSubtype(recordType2));
    assertEquals(false, recordType2.isSubtype(recordType1));

    RecordType recordType3 = new RecordType(registry, properties2);
    assertEquals(true, recordType1.isSubtype(recordType3));
    assertEquals(true, recordType3.isSubtype(recordType1));

    RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
    assertEquals(false, recordType1.isSubtype(recordType4));
    assertEquals(false, recordType4.isSubtype(recordType1));

    RecordType recordType5 = new RecordType(registry, properties1);
    recordType5.setFrozen(true);

    assertEquals(false, recordType1.isSubtype(recordType5));
  }

  @Test
  public void testToString() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordProperty prop = new RecordProperty("prop", registry.intType());

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop", prop);

    RecordType recordType = new RecordType(registry, properties);

    assertEquals(
        "RecordType {prop: Int}", recordType.toString());
  }

  @Test
  public void testToMaybeRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordProperty prop = new RecordProperty("prop", registry.intType());

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop", prop);

    RecordType recordType = new RecordType(registry, properties);

    assertEquals(recordType, recordType.toMaybeRecordType());
  }

  @Test
  public void testGetPropertyTypeNull() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordProperty prop = new RecordProperty("prop", registry.intType());

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop", prop);

    RecordType recordType = new RecordType(registry, properties);

    assertNull(recordType.getPropertyType("unknown"));
  }
}