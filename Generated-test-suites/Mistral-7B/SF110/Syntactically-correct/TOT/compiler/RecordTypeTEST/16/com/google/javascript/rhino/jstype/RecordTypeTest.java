import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RecordTypeTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);
    RecordType expectedRecordType = new RecordType(registry, properties, true);

    assertEquals(expectedRecordType, recordType);
  }

  @Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties, true);
    RecordType recordType2 = new RecordType(registry, properties, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.STRICT));
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.LAX));
  }

  @Test
  public void testRecordTypeEquivalenceFail() {
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("name", new RecordProperty("name", registry.getStringType()));
    properties1.put("age", new RecordProperty("age", registry.getNumberType()));

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("name", new RecordProperty("name", registry.getStringType()));
    properties2.put("age", new RecordProperty("age", registry.getObjectType()));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.STRICT));
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.LAX));
  }

  @Test
  public void testRecordTypeFreeze() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);
    recordType.freeze();

    RecordType expectedRecordType = new RecordType(registry, properties, true);
    expectedRecordType.freeze();

    assertEquals(expectedRecordType, recordType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRecordTypeFreezeAlreadyFrozen() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);
    recordType.freeze();
    recordType.freeze();
  }

  @Test
  public void testRecordTypeIsSynthetic() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, false);
    RecordType expectedRecordType = new RecordType(registry, properties, false);

    assertEquals(expectedRecordType, recordType);
    assertTrue(recordType.isSynthetic());
  }

  @Test
  public void testRecordTypeIsNotSynthetic() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);
    RecordType expectedRecordType = new RecordType(registry, properties, true);

    assertEquals(expectedRecordType, recordType);
    assertFalse(recordType.isSynthetic());
  }

  @Test
  public void testRecordTypeGreatestSubtype() {
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("name", new RecordProperty("name", registry.getStringType()));
    properties1.put("age", new RecordProperty("age", registry.getNumberType()));

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("name", new RecordProperty("name", registry.getStringType()));
    properties2.put("age", new RecordProperty("age", registry.getNumberType()));
    properties2.put("city", new RecordProperty("city", registry.getStringType()));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
    JSType expectedGreatestSubtype = registry.getRecordType(properties2);

    assertEquals(expectedGreatestSubtype, greatestSubtype);
  }

  @Test
  public void testRecordTypeGetImplicitPrototype() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);

    ObjectType implicitPrototype = recordType.getImplicitPrototype();
    ObjectType expectedImplicitPrototype = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    assertEquals(expectedImplicitPrototype, implicitPrototype);
  }

  @Test
  public void testRecordTypeDefineProperty() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);

    assertFalse(recordType.hasProperty("city"));

    recordType.defineProperty("city", registry.getStringType(), false, Node.UNDEFINED);

    assertTrue(recordType.hasProperty("city"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRecordTypeDefinePropertyFrozen() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);
    recordType.freeze();

    try {
      recordType.defineProperty("city", registry.getStringType(), false, Node.UNDEFINED);
    } finally {
      assertFalse(recordType.hasProperty("city"));
    }
  }

  @Test
  public void testRecordTypeHasProperty() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);

    assertTrue(recordType.hasProperty("name"));
    assertTrue(recordType.hasProperty("age"));
    assertFalse(recordType.hasProperty("city"));
  }

  @Test
  public void testRecordTypeGetPropertyType() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals(registry.getStringType(), recordType.getPropertyType("name"));
    assertEquals(registry.getNumberType(), recordType.getPropertyType("age"));
    assertNull(recordType.getPropertyType("city"));
  }

  @Test
  public void testRecordTypeGetPropertyNode() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals(Node.STRING_LITERAL, recordType.getPropertyNode("name"));
    assertEquals(Node.NUMBER_LITERAL, recordType.getPropertyNode("age"));
    assertNull(recordType.getPropertyNode("city"));
  }

  @Test
  public void testRecordTypeGetOwnPropertyNames() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);

    Set<String> expectedPropertyNames = Arrays.asList("name", "age").iterator().asJavaCollection();

    assertEquals(expectedPropertyNames, recordType.getOwnPropertyNames());
  }
}