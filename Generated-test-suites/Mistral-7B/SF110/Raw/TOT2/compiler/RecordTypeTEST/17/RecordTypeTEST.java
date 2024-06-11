import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @Before
  public void setup() {
    registry.init();
  }

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.TYPE));

    RecordType recordType = new RecordType(registry, properties, true);
    assertEquals(String.class, recordType.getPropertyType("name"));
    assertEquals(Integer.TYPE, recordType.getPropertyType("age"));

    final String name = "John Doe";
    final int age = 30;

    RecordType recordInstance = RecordType.createInstance(recordType,
        JSType.fromValues(name, age));
    assertEquals(name, recordInstance.getProperty("name"));
    assertEquals(age, recordInstance.getProperty("age"));
  }

  @Test
  public void testRecordTypeFrozen() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.TYPE));

    RecordType recordType = new RecordType(registry, properties, true);
    recordType.freeze();

    Map<String, RecordProperty> newProperties = new HashMap<>();
    newProperties.put("name", new RecordProperty("name", String.class));
    newProperties.put("age", new RecordProperty("age", Double.TYPE));

    RecordType newRecordType = new RecordType(registry, newProperties, false);
    assertFalse(recordType.isEquivalentTo(newRecordType));
  }

  @Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Integer.TYPE));

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("name", new RecordProperty("name", String.class));
    properties2.put("age", new RecordProperty("age", Integer.TYPE));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2,
        EquivalenceMethod.ByName));
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2,
        EquivalenceMethod.ByValue));
  }

  @Test
  public void testRecordTypeUnequalEquivalence() {
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Integer.TYPE));

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("name", new RecordProperty("name", String.class));
    properties2.put("age", new RecordProperty("age", Long.TYPE));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2,
        EquivalenceMethod.ByName));
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2,
        EquivalenceMethod.ByValue));
  }

  @Test
  public void testRecordTypeGreatestSubtype() {
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Integer.TYPE));

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("name", new RecordProperty("name", String.class));
    properties2.put("age", new RecordProperty("age", Long.TYPE));
    properties2.put("city", new RecordProperty("city", String.class));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(JSType.getObjectType(), greatestSubtype);

    properties2.remove("city");
    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(recordType2, greatestSubtype);
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeNullProperty() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put(null, new RecordProperty(null, Integer.TYPE));

    new RecordType(registry, properties, true);
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordPropertyNullType() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", null));

    new RecordType(registry, properties, true);
  }

  @Test
  public void testRecordTypeToMaybeRecordType() {
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Integer.TYPE));

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("name", new RecordProperty("name", String.class));
    properties2.put("age", new RecordProperty("age", Integer.TYPE));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, false);

    assertSame(recordType1, recordType1.toMaybeRecordType());
    assertSame(recordType2, recordType2.toMaybeRecordType());
  }

  @Test
  public void testRecordTypeIsSubtype() {
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Integer.TYPE));

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("name", new RecordProperty("name", String.class));
    properties2.put("age", new RecordProperty("age", Integer.TYPE));
    properties2.put("city", new RecordProperty("city", String.class));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testRecordTypeNotSubtype() {
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Integer.TYPE));

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("name", new RecordProperty("name", String.class));
    properties2.put("age", new RecordProperty("age", Double.TYPE));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testRecordTypeIsSubtypeNull() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.TYPE));

    RecordType recordType = new RecordType(registry, properties, true);

    JSType nullType = JSType.getNullType();

    assertFalse(recordType.isSubtype(nullType));
  }

  @Test
  public void testRecordTypeIsSubtypeEmptyObject() {
    Map<String, RecordProperty> properties = new HashMap<>();

    RecordType recordType = new RecordType(registry, properties, true);

    ObjectType emptyObject = registry.getNativeObjectType(
        JSTypeNative.OBJECT_TYPE);

    assertTrue(recordType.isSubtype(emptyObject));
  }

  @Test
  public void testRecordTypeIsSubtypeRecordWithLessProperties() {
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Integer.TYPE));

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("name", new RecordProperty("name", String.class));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testRecordTypeIsSubtypeRecordWithMoreProperties() {
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Integer.TYPE));

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("name", new RecordProperty("name", String.class));
    properties2.put("age", new RecordProperty("age", Integer.TYPE));
    properties2.put("city", new RecordProperty("city", String.class));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.isSubtype(recordType2));
  }
}