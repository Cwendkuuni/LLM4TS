import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
    RecordType expectedRecordType = new RecordTypeBuilder(registry)
        .addProperty("name", registry.getStringType())
        .addProperty("age", registry.getNumberType())
        .build();

    assertEquals(expectedRecordType, recordType);
  }

  @Test
  public void testRecordTypeFrozen() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);
    recordType.freeze();

    assertFalse(recordType.isWritable());
    try {
      recordType.defineProperty("newProperty", registry.getStringType(), false, null);
      fail("Expected an IllegalStateException");
    } catch (IllegalStateException e) {
      // expected
    }
  }

  @Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", registry.getStringType()));
    propertiesA.put("age", new RecordProperty("age", registry.getNumberType()));

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", registry.getStringType()));
    propertiesB.put("age", new RecordProperty("age", registry.getNumberType()));
    propertiesB.put("gender", new RecordProperty("gender", registry.getStringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, false);

    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, RecordType.EquivalenceMethod.IDENTITY));
  }

  @Test
  public void testRecordTypeGetImplicitPrototype() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);

    ObjectType expectedPrototype = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    ObjectType actualPrototype = recordType.getImplicitPrototype();

    assertTrue(expectedPrototype.isEquivalentTo(actualPrototype));
  }

  @Test
  public void testRecordTypeDefineProperty() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, false);

    String newPropertyName = "newProperty";
    JSType newPropertyType = registry.getNumberType();

    recordType.defineProperty(newPropertyName, newPropertyType, false, null);

    assertTrue(recordType.hasProperty(newPropertyName));
    assertEquals(newPropertyType, recordType.getPropertyType(newPropertyName));
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeDefinePropertyFrozen() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);
    recordType.freeze();

    String newPropertyName = "newProperty";
    JSType newPropertyType = registry.getNumberType();

    recordType.defineProperty(newPropertyName, newPropertyType, false, null);
  }

  @Test
  public void testRecordTypeGetGreatestSubtype() {
    Map<String, RecordProperty> recordAProperties = new HashMap<>();
    recordAProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordAProperties.put("age", new RecordProperty("age", registry.getNumberType()));
    RecordType recordA = new RecordType(registry, recordAProperties, true);

    Map<String, RecordProperty> recordBProperties = new HashMap<>();
    recordBProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordBProperties.put("age", new RecordProperty("age", registry.getNumberType()));
    recordBProperties.put("gender", new RecordProperty("gender", registry.getStringType()));
    RecordType recordB = new RecordType(registry, recordBProperties, false);

    ObjectType expectedObjectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    JSType actualGreatestSubtype = recordA.getGreatestSubtypeHelper(expectedObjectType);

    assertTrue(actualGreatestSubtype.isEquivalentTo(expectedObjectType));

    Map<String, RecordProperty> recordCProperties = new HashMap<>();
    recordCProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordCProperties.put("age", new RecordProperty("age", registry.getNumberType()));
    recordCProperties.put("gender", new RecordProperty("gender", registry.getStringType()));
    RecordType recordC = new RecordType(registry, recordCProperties, false);

    JSType actualGreatestSubtypeC = recordA.getGreatestSubtypeHelper(recordC);

    assertEquals(recordC, actualGreatestSubtypeC);
  }

  @Test
  public void testRecordTypeIsSubtype() {
    Map<String, RecordProperty> recordAProperties = new HashMap<>();
    recordAProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordAProperties.put("age", new RecordProperty("age", registry.getNumberType()));
    RecordType recordA = new RecordType(registry, recordAProperties, true);

    Map<String, RecordProperty> recordBProperties = new HashMap<>();
    recordBProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordBProperties.put("age", new RecordProperty("age", registry.getNumberType()));
    recordBProperties.put("gender", new RecordProperty("gender", registry.getStringType()));
    RecordType recordB = new RecordType(registry, recordBProperties, false);

    Map<String, RecordProperty> recordCProperties = new HashMap<>();
    recordCProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordCProperties.put("age", new RecordProperty("age", registry.getNumberType()));
    recordCProperties.put("gender", new RecordProperty("gender", registry.getStringType()));
    RecordType recordC = new RecordType(registry, recordCProperties, false);

    Map<String, RecordProperty> recordDProperties = new HashMap<>();
    recordDProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordDProperties.put("age", new RecordProperty("age", registry.getNumberType()));
    recordDProperties.put("gender", new RecordProperty("gender", registry.getStringType()));
    recordDProperties.put("city", new RecordProperty("city", registry.getStringType()));
    RecordType recordD = new RecordType(registry, recordDProperties, false);

    assertTrue(recordA.isSubtype(recordA));
    assertTrue(recordA.isSubtype(recordB));
    assertTrue(recordB.isSubtype(recordA));
    assertTrue(recordA.isSubtype(recordC));
    assertTrue(recordC.isSubtype(recordA));
    assertFalse(recordA.isSubtype(recordD));
    assertFalse(recordD.isSubtype(recordA));
  }
}