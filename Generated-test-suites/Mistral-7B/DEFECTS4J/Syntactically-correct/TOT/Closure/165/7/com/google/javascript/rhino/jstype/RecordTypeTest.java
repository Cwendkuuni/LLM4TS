import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.PrivatePropertyTester;
import com.google.javascript.rhino.testing.TypeEquivalenceTester;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty(JSType.INT_TYPE, new Node(0)));
    properties.put("b", new RecordProperty(JSType.STRING_TYPE, new Node("")));

    RecordType recordType = new RecordType(registry, properties);
    assertEquals("Object", recordType.getClassName());
    assertEquals("{a: int, b: string}", recordType.toString());

    RecordType recordType2 = new RecordType(registry, properties);
    assertEquals(recordType, recordType2);

    RecordType recordType3 = new RecordType(registry, Maps.newHashMap(properties));
    assertEquals(recordType, recordType3);
  }

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty(JSType.INT_TYPE, new Node(0)));
    properties1.put("b", new RecordProperty(JSType.STRING_TYPE, new Node("")));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty(JSType.INT_TYPE, new Node(0)));
    properties2.put("b", new RecordProperty(JSType.STRING_TYPE, new Node("")));
    properties2.put("c", new RecordProperty(JSType.STRING_TYPE, new Node("hello")));
    RecordType recordType2 = new RecordType(registry, properties2);

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("a", new RecordProperty(JSType.INT_TYPE, new Node(1)));
    properties3.put("b", new RecordProperty(JSType.STRING_TYPE, new Node("world")));
    RecordType recordType3 = new RecordType(registry, properties3);

    TypeEquivalenceTester.assertTypesAreEquivalent(recordType1, recordType1);
    TypeEquivalenceTester.assertTypesAreEquivalent(recordType1, recordType2);
    assertFalse(TypeEquivalenceTester.areTypesEquivalent(recordType1, recordType3));

    Map<String, RecordProperty> properties4 = Maps.newHashMap();
    properties4.put("a", new RecordProperty(JSType.INT_TYPE, new Node(0)));
    properties4.put("b", new RecordProperty(JSType.INT_TYPE, new Node(1)));
    RecordType recordType4 = new RecordType(registry, properties4);

    assertFalse(TypeEquivalenceTester.areTypesEquivalent(recordType1, recordType4));
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty(JSType.INT_TYPE, new Node(0)));
    RecordType recordType1 = new RecordType(registry, properties1);

    recordType1.defineProperty("c", JSType.STRING_TYPE, false, new Node(""));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty(JSType.INT_TYPE, new Node(0)));
    properties2.put("c", new RecordProperty(JSType.STRING_TYPE, new Node("")));

    TypeEquivalenceTester.assertTypesAreEquivalent(recordType1, new RecordType(registry, properties2));
  }

  @Test(expected = IllegalStateException.class)
  public void testFrozenRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty(JSType.INT_TYPE, new Node(0)));

    RecordType recordType = new RecordType(registry, properties);
    recordType.defineProperty("b", JSType.STRING_TYPE, false, new Node(""));

    assertEquals("Object", recordType.getClassName());
    assertEquals("{a: int, b: string}", recordType.toString());

    try {
      recordType.defineProperty("a", JSType.INT_TYPE, false, new Node(42));
      fail("Expected an IllegalStateException");
    } catch (IllegalStateException e) {
      // expected
    }
  }

  @Test
  public void testGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty(JSType.INT_TYPE, new Node(0)));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty(JSType.INT_TYPE, new Node(0)));
    properties2.put("b", new RecordProperty(JSType.STRING_TYPE, new Node("")));
    RecordType recordType2 = new RecordType(registry, properties2);

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("a", new RecordProperty(JSType.INT_TYPE, new Node(0)));
    properties3.put("b", new RecordProperty(JSType.STRING_TYPE, new Node("hello")));
    RecordType recordType3 = new RecordType(registry, properties3);

    Map<String, RecordProperty> properties4 = Maps.newHashMap();
    properties4.put("a", new RecordProperty(JSType.INT_TYPE, new Node(1)));
    properties4.put("b", new RecordProperty(JSType.STRING_TYPE, new Node("world")));
    RecordType recordType4 = new RecordType(registry, properties4);

    JSType greatestSubtype = recordType1.getGreatestSubtype(registry, recordType2);
    assertEquals(recordType2, greatestSubtype);

    greatestSubtype = recordType1.getGreatestSubtype(registry, recordType3);
    assertEquals(recordType3, greatestSubtype);

    greatestSubtype = recordType1.getGreatestSubtype(registry, recordType4);
    assertEquals(recordType1, greatestSubtype);
  }

  @Test
  public void testPropertyAccess() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty(JSType.INT_TYPE, new Node(0)));
    RecordType recordType = new RecordType(registry, properties);

    PrivatePropertyTester tester = new PrivatePropertyTester();
    tester.setType(recordType);
    tester.setPropertyName("properties");

    assertEquals("The field 'properties' is not writable", null, tester.tryWriteValue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), "properties", new HashMap<>()));
    assertNotNull(recordType.getProperties());

    RecordType recordType2 = new RecordType(registry, Maps.newHashMap(properties));
    assertEquals(recordType.getProperties(), recordType2.getProperties());

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty(JSType.STRING_TYPE, new Node("")));
    properties2.put("b", new RecordProperty(JSType.INT_TYPE, new Node(1)));
    RecordType recordType3 = new RecordType(registry, properties2);

    assertEquals(recordType.getProperties(), recordType3.getProperties());
    assertFalse(recordType.getProperties().equals(recordType3.getProperties()));
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty(JSType.INT_TYPE, new Node(0)));
    RecordType recordType1 = new RecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty(JSType.INT_TYPE, new Node(0)));
    properties2.put("b", new RecordProperty(JSType.STRING_TYPE, new Node("")));
    RecordType recordType2 = new RecordType(registry, properties2);

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("a", new RecordProperty(JSType.INT_TYPE, new Node(0)));
    properties3.put("b", new RecordProperty(JSType.STRING_TYPE, new Node("hello")));
    RecordType recordType3 = new RecordType(registry, properties3);

    Map<String, RecordProperty> properties4 = Maps.newHashMap();
    properties4.put("a", new RecordProperty(JSType.INT_TYPE, new Node(1)));
    properties4.put("b", new RecordProperty(JSType.STRING_TYPE, new Node("world")));
    RecordType recordType4 = new RecordType(registry, properties4);

    RecordType recordType5 = new RecordType(registry, Maps.newHashMap());
    RecordType recordType6 = new RecordType(registry, properties1);
    RecordType recordType7 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType2));
    assertTrue(recordType2.isSubtype(recordType1));
    assertFalse(recordType1.isSubtype(recordType3));
    assertFalse(recordType1.isSubtype(recordType4));
    assertFalse(recordType1.isSubtype(recordType5));
    assertTrue(recordType1.isSubtype(recordType6));
    assertTrue(recordType7.isSubtype(recordType1));
  }

  @Test
  public void testToString() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty(JSType.INT_TYPE, new Node(0)));
    RecordType recordType = new RecordType(registry, properties);

    assertEquals("{a: int}", recordType.toString());
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty(JSType.INT_TYPE, new Node(0)));
    RecordType recordType = new RecordType(registry, properties);

    ObjectType implicitPrototype = recordType.getImplicitPrototype();
    assertEquals("Object", implicitPrototype.getClassName());
  }
}