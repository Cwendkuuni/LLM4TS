import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordProperty recordProperty = new RecordProperty("name", String.class);
    recordProperties.put("name", recordProperty);

    RecordType recordType = new RecordType(registry, recordProperties, true);
    assertEquals("RecordType", recordType.getName().toString());
    assertEquals(RecordType.class, recordType.getClass());
    assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
    assertFalse(recordType.isFrozen());

    assertFalse(recordType.hasProperty("invalidProperty"));
    assertEquals(String.class, recordType.getPropertyType("name"));

    Object recordInstance = recordType.getInstance(registry, null);
    Node namePropertyNode = Node.newProperty("name");
    recordInstance.setProperty(namePropertyNode, "John Doe");
    assertEquals("John Doe", recordInstance.getProperty("name").toString());
  }

  @Test
  public void testRecordTypeWithInferredProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordProperty recordProperty = new RecordProperty("age", Number.class);
    recordProperties.put("age", recordProperty);

    RecordType recordType = new RecordType(registry, recordProperties, false);

    assertFalse(recordType.isFrozen());
    assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
    assertEquals("RecordType", recordType.getName().toString());

    Object recordInstance = recordType.getInstance(registry, null);
    Node agePropertyNode = Node.newProperty("age");
    recordInstance.setProperty(agePropertyNode, 30);
    assertEquals(30, recordInstance.getProperty("age").toInt32());
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeWithNullProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordProperty recordProperty = new RecordProperty("name", String.class);
    recordProperties.put("name", recordProperty);
    RecordProperty nullProperty = null;
    recordProperties.put("invalidProperty", nullProperty);

    try {
      new RecordType(registry, recordProperties, true);
    } catch (IllegalStateException e) {
      assertEquals("RecordProperty associated with a property should not be null!", e.getMessage());
      throw e;
    }
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    RecordProperty recordProperty1 = new RecordProperty("name", String.class);
    recordProperties1.put("name", recordProperty1);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    RecordProperty recordProperty2 = new RecordProperty("name", String.class);
    recordProperties2.put("name", recordProperty2);

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));
    assertTrue(recordType2.checkRecordEquivalenceHelper(recordType1, false));

    Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
    RecordProperty recordProperty3 = new RecordProperty("name", Int32.class);
    recordProperties3.put("name", recordProperty3);

    RecordType recordType3 = new RecordType(registry, recordProperties3, true);
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, false));
    assertFalse(recordType3.checkRecordEquivalenceHelper(recordType1, false));
  }

  @Test
  public void testRecordTypeSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    RecordProperty recordProperty1 = new RecordProperty("name", String.class);
    recordProperties1.put("name", recordProperty1);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    RecordProperty recordProperty2 = new RecordProperty("name", String.class);
    RecordProperty recordProperty3 = new RecordProperty("age", Number.class);
    recordProperties2.put("name", recordProperty2);
    recordProperties2.put("age", recordProperty3);

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    assertTrue(recordType1.isSubtype(recordType2));

    Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
    recordProperties3.put("name", String.class);

    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertTrue(objectType.isSubtype(recordType1));

    Map<String, RecordProperty> recordProperties4 = Maps.newHashMap();
    RecordProperty recordProperty4 = new RecordProperty("name", Int32.class);
    recordProperties4.put("name", recordProperty4);

    RecordType recordType3 = new RecordType(registry, recordProperties3, true);
    assertFalse(recordType1.isSubtype(recordType3));
  }

  @Test
  public void testRecordTypeWithDifferentPropertyNames() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    RecordProperty recordProperty1 = new RecordProperty("name", String.class);
    recordProperties1.put("name", recordProperty1);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    RecordProperty recordProperty2 = new RecordProperty("age", String.class);
    recordProperties2.put("age", recordProperty2);

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    assertFalse(recordType1.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType1));
  }

  @Test
  public void testRecordTypeWithIncompatiblePropertyTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    RecordProperty recordProperty1 = new RecordProperty("name", String.class);
    recordProperties1.put("name", recordProperty1);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    RecordProperty recordProperty2 = new RecordProperty("name", Number.class);
    recordProperties2.put("name", recordProperty2);

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    assertFalse(recordType1.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType1));
  }

  @Test
  public void testRecordTypeWithMismatchedPropertyTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    RecordProperty recordProperty1 = new RecordProperty("name", String.class);
    recordProperties1.put("name", recordProperty1);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    RecordProperty recordProperty2 = new RecordProperty("name", JSTypeNative.NO_TYPE);
    recordProperties2.put("name", recordProperty2);

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    assertFalse(recordType1.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType1));
  }

  @Test
  public void testRecordTypeWithDifferentNumberOfProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    RecordProperty recordProperty1 = new RecordProperty("name", String.class);
    recordProperties1.put("name", recordProperty1);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    RecordProperty recordProperty1 = new RecordProperty("name", String.class);
    RecordProperty recordProperty2 = new RecordProperty("age", Number.class);
    recordProperties2.put("name", recordProperty1);
    recordProperties2.put("age", recordProperty2);

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    assertFalse(recordType1.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType1));
  }

  @Test
  public void testRecordTypeWithFrozenProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordProperty recordProperty = new RecordProperty("name", String.class);
    recordProperties.put("name", recordProperty);

    RecordType recordType = new RecordType(registry, recordProperties, true);
    recordType.freeze();

    try {
      recordType.defineProperty("name", String.class, false, null);
      fail("Expected an IllegalStateException");
    } catch (IllegalStateException e) {
      assertEquals("Cannot define a property on a frozen record type", e.getMessage());
    }
  }
}