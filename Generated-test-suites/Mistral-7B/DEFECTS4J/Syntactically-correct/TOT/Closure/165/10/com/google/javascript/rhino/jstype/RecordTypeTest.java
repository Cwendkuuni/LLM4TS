import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.types.RecordType;
import com.google.javascript.rhino.types.ReferenceType;
import com.google.javascript.rhino.types.UnionType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Integer.class));
    RecordType recordType = new RecordType(registry, recordProperties);

    assertEquals("RecordType", recordType.getClassName());
    assertEquals(2, recordType.getNumProperties());

    assertEquals(Integer.class, recordType.getPropertyType("age"));
    assertEquals(String.class, recordType.getPropertyType("name"));

    ReferenceType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertEquals(objectType, recordType.getImplicitPrototype());
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Integer.class));
    RecordType recordType1 = new RecordType(registry, recordProperties);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Integer.class));
    recordProperties2.put("gender", new RecordProperty("gender", String.class));
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    RecordType recordType3 = new RecordType(registry, recordProperties);
    RecordType recordType4 = new RecordType(registry, recordProperties2);

    assertTrue(recordType1.isEquivalentTo(recordType3));
    assertFalse(recordType1.isEquivalentTo(recordType2));
    assertFalse(recordType2.isEquivalentTo(recordType3));
    assertFalse(recordType2.isEquivalentTo(recordType4));
  }

  @Test
  public void testRecordTypeSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Integer.class));
    RecordType recordType1 = new RecordType(registry, recordProperties);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Integer.class));
    recordProperties2.put("gender", new RecordProperty("gender", String.class));
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
    recordProperties3.put("age", new RecordProperty("age", Integer.class));
    RecordType recordType3 = new RecordType(registry, recordProperties3);

    assertEquals(recordType1, recordType1.getGreatestSubtype(recordType1));
    assertEquals(recordType1, recordType1.getGreatestSubtype(recordType2));
    assertEquals(recordType1, recordType1.getGreatestSubtype(recordType3));

    assertFalse(recordType1.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType1));
    assertFalse(recordType2.isSubtype(recordType3));
    assertFalse(recordType3.isSubtype(recordType1));
    assertFalse(recordType3.isSubtype(recordType2));
  }

  @Test
  public void testRecordTypePropertyAccess() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Integer.class));
    RecordType recordType = new RecordType(registry, recordProperties);

    Node recordObjectNode = new Node.ObjectLiteral();
    recordObjectNode.setProperties(Maps.newTreeMap());

    recordObjectNode.setProperty("name", new Node.String("John Doe"));
    recordObjectNode.setProperty("age", new Node.Int(30));

    JSType recordObjectType = registry.getTypeFromTree(recordObjectNode);

    assertEquals(recordType, recordObjectType.getGreatestSubtype(recordObjectType));
    assertSame(String.class, recordObjectType.getPropertyType("name"));
    assertSame(Integer.class, recordObjectType.getPropertyType("age"));
    assertEquals("John Doe", recordObjectType.getProperty("name").getValue());
    assertEquals(30, recordObjectType.getProperty("age").getValue().toInt32());
  }

  @Test
  public void testRecordTypePropertyInference() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Integer.class));
    RecordType recordType = new RecordType(registry, recordProperties);

    Node recordObjectNode = new Node.ObjectLiteral();
    recordObjectNode.setProperties(Maps.newTreeMap());

    recordObjectNode.setProperty("name", new Node.String("John Doe"));

    JSType recordObjectType = registry.getTypeFromTree(recordObjectNode);
    assertEquals(recordType, recordObjectType);

    Node recordObjectNodeWithAge = new Node.ObjectLiteral();
    recordObjectNodeWithAge.setProperties(Maps.newTreeMap());

    recordObjectNodeWithAge.setProperty("name", new Node.String("John Doe"));
    recordObjectNodeWithAge.setProperty("age", new Node.Int(30));

    JSType recordObjectTypeWithAge = registry.getTypeFromTree(recordObjectNodeWithAge);
    assertEquals(recordType, recordObjectTypeWithAge);

    JSType propertyAccessType = recordObjectTypeWithAge.getPropertyType("age");
    assertEquals(Integer.class, propertyAccessType);

    JSType propertyAccessTypeUnknown = recordObjectType.getPropertyType("age");
    assertTrue(propertyAccessTypeUnknown.isUnknownType());
  }

  @Test
  public void testRecordTypeUnion() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Integer.class));
    RecordType recordType = new RecordType(registry, recordProperties);

    UnionType unionType = new UnionType(registry);
    unionType.addAlternate(recordType);
    unionType.addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));

    Node recordObjectNode = new Node.ObjectLiteral();
    recordObjectNode.setProperties(Maps.newTreeMap());

    recordObjectNode.setProperty("name", new Node.String("John Doe"));
    recordObjectNode.setProperty("age", new Node.Int(30));

    JSType recordObjectType = registry.getTypeFromTree(recordObjectNode);

    JSType unionTypeWithRecord = unionType.getGreatestSubtype(recordObjectType);
    JSType unionTypeWithObject = unionType.getGreatestSubtype(
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));

    assertEquals(unionType, unionTypeWithRecord);
    assertEquals(unionType, unionTypeWithObject);

    UnionType unionTypeWithUnknown = unionType.getGreatestSubtype(
        new NativeType(registry, JSTypeNative.UNDEFINED));

    assertFalse(unionType.isEquivalentTo(unionTypeWithUnknown));
  }
}