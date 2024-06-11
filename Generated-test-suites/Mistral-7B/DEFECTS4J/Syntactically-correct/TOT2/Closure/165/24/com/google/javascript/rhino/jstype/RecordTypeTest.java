import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    // Given a record type registry and a map of properties
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
    properties.put("x", new RecordProperty("x", registry.getTypeByName("Number")));
    properties.put("y", new RecordProperty("y", registry.getTypeByName("String")));

    // When creating a new record type
    RecordType recordType = new RecordType(registry, properties);

    // Then the record type should have the expected properties
    SortedMap<String, JSType> actualProperties = recordType.properties;
    Assert.assertEquals("Unexpected number of properties.", 2, actualProperties.size());
    Assert.assertEquals("Unexpected property type for 'x'.", registry.getTypeByName("Number"), actualProperties.get("x"));
    Assert.assertEquals("Unexpected property type for 'y'.", registry.getTypeByName("String"), actualProperties.get("y"));
  }

  @Test
  public void testRecordTypeSubtyping() {
    // Given two record type registries and maps of properties
    JSTypeRegistry registryA = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newLinkedHashMap();
    propertiesA.put("x", new RecordProperty("x", registryA.getTypeByName("Number")));

    JSTypeRegistry registryB = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesB = Maps.newLinkedHashMap();
    propertiesB.put("x", new RecordProperty("x", registryB.getTypeByName("Number")));
    propertiesB.put("y", new RecordProperty("y", registryB.getTypeByName("String")));

    // When creating new record types from the properties
    RecordType recordTypeA = new RecordType(registryA, propertiesA);
    RecordType recordTypeB = new RecordType(registryB, propertiesB);

    // Then record type A is a subtype of record type B
    Assert.assertTrue("Unexpected subtyping result.", recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeSubtypingWithIncompatiblePropertyTypes() {
    // Given two record type registries and maps of properties
    JSTypeRegistry registryA = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newLinkedHashMap();
    propertiesA.put("x", new RecordProperty("x", registryA.getTypeByName("Number")));

    JSTypeRegistry registryB = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesB = Maps.newLinkedHashMap();
    propertiesB.put("x", new RecordProperty("x", registryB.getTypeByName("String")));
    propertiesB.put("y", new RecordProperty("y", registryB.getTypeByName("String")));

    // When creating new record types from the properties
    RecordType recordTypeA = new RecordType(registryA, propertiesA);
    RecordType recordTypeB = new RecordType(registryB, propertiesB);

    // Then record type A is not a subtype of record type B
    Assert.assertFalse("Unexpected subtyping result.", recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeSubtypingWithMissingProperty() {
    // Given two record type registries and maps of properties
    JSTypeRegistry registryA = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newLinkedHashMap();
    propertiesA.put("x", new RecordProperty("x", registryA.getTypeByName("Number")));

    JSTypeRegistry registryB = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesB = Maps.newLinkedHashMap();
    propertiesB.put("y", new RecordProperty("y", registryB.getTypeByName("String")));

    // When creating new record types from the properties
    RecordType recordTypeA = new RecordType(registryA, propertiesA);
    RecordType recordTypeB = new RecordType(registryB, propertiesB);

    // Then record type A is a subtype of record type B (since every record type is a subtype of an empty record type)
    Assert.assertTrue("Unexpected subtyping result.", recordTypeA.isSubtype(recordTypeB));

    // But if property 'x' is missing in record type B, the result changes
    propertiesB.remove("x");
    RecordType recordTypeBModified = new RecordType(registryB, propertiesB);

    // Then record type A is not a subtype of record type B modified
    Assert.assertFalse("Unexpected subtyping result.", recordTypeA.isSubtype(recordTypeBModified));
  }

  @Test
  public void testRecordTypeGreatestSubtype() {
    // Given three record type registries and maps of properties
    JSTypeRegistry registryA = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newLinkedHashMap();
    propertiesA.put("x", new RecordProperty("x", registryA.getTypeByName("Number")));
    propertiesA.put("y", new RecordProperty("y", registryA.getTypeByName("String")));

    JSTypeRegistry registryB = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesB = Maps.newLinkedHashMap();
    propertiesB.put("x", new RecordProperty("x", registryB.getTypeByName("Number")));
    propertiesB.put("z", new RecordProperty("z", registryB.getTypeByName("Number")));

    JSTypeRegistry registryC = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesC = Maps.newLinkedHashMap();
    propertiesC.put("x", new RecordProperty("x", registryC.getTypeByName("Number")));
    propertiesC.put("y", new RecordProperty("y", registryC.getTypeByName("String")));
    propertiesC.put("z", new RecordProperty("z", registryC.getTypeByName("Number")));

    // When creating new record types from the properties
    RecordType recordTypeA = new RecordType(registryA, propertiesA);
    RecordType recordTypeB = new RecordType(registryB, propertiesB);
    RecordType recordTypeC = new RecordType(registryC, propertiesC);

    // Then the greatest subtype of record types A and B is record type C
    JSType greatestSubtype = recordTypeA.getGreatestSubtype(recordTypeB);
    Assert.assertEquals("Unexpected greatest subtype.", recordTypeC, greatestSubtype);
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeFrozenPropertyModification() {
    // Given a record type registry and a map of properties
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newLinkedHashMap();
    properties.put("x", new RecordProperty("x", registry.getTypeByName("Number")));

    // When creating a new record type and freezing it
    RecordType recordType = new RecordType(registry, properties);
    recordType.freeze();

    // Then attempting to modify a property should throw an IllegalStateException
    recordType.defineProperty("y", registry.getTypeByName("String"), false, null);
  }
}