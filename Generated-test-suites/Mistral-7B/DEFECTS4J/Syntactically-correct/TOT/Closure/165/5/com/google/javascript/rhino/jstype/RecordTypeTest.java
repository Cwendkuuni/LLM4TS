import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.types.ObjectType;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getBooleanType()));
    properties.put("b", new RecordProperty("b", registry.getStringType()));

    RecordType recordType = new RecordType(registry, properties);

    // Properties should be defined and accessible
    assertEquals(registry.getBooleanType(), recordType.getPropertyType("a"));
    assertEquals(registry.getStringType(), recordType.getPropertyType("b"));

    // Freeze should be false by default
    assertFalse(recordType.isFrozen());

    // Define property and freeze
    recordType.defineProperty("c", registry.getNumberType(), true, null);
    assertTrue(recordType.isFrozen());
  }

  @Test
  public void testRecordTypeGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();

    RecordType recordType = new RecordType(registry, properties);
    ObjectType prototype = recordType.getImplicitPrototype();

    // Implicit prototype should be ObjectType
    assertSame(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), prototype);
  }

  @Test
  public void testRecordTypeIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", registry.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", registry.getStringType()));
    propertiesB.put("b", new RecordProperty("b", registry.getStringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    // Record types should be equivalent
    assertTrue(recordTypeA.isEquivalentTo(recordTypeB));

    // Record types with different properties should not be equivalent
    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("c", new RecordProperty("c", registry.getStringType()));
    RecordType recordTypeC = new RecordType(registry, propertiesC);
    assertFalse(recordTypeA.isEquivalentTo(recordTypeC));
  }

  @Test
  public void testRecordTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", registry.getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", registry.getStringType()));
    propertiesB.put("b", new RecordProperty("b", registry.getStringType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    // Record type A is a subtype of record type B
    assertTrue(recordTypeA.isSubtype(recordTypeB));

    // Record type B is not a subtype of record type A
    assertFalse(recordTypeB.isSubtype(recordTypeA));

    // ObjectType is a subtype of any record type
    assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordTypeA));

    // RecordType with property of incompatible types is not a subtype
    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("a", new RecordProperty("a", registry.getStringType()));
    propertiesC.put("b", new RecordProperty("b", registry.getBooleanType()));
    RecordType recordTypeC = new RecordType(registry, propertiesC);
    assertFalse(recordTypeA.isSubtype(recordTypeC));

    // RecordType with missing property is not a subtype
    RecordType recordTypeD = new RecordType(registry, Maps.newHashMap());
    assertFalse(recordTypeA.isSubtype(recordTypeD));
  }

  @Test
  public void testRecordTypeGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", registry.getStringType()));
    propertiesA.put("b", new RecordProperty("b", registry.getNumberType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", registry.getStringType()));
    propertiesB.put("b", new RecordProperty("b", registry.getBooleanType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    // Record type A is a subtype of record type B and vice versa
    // Their greatest subtype should be a record type with both "a" and "b"
    RecordType greatestSubtypeAB = (RecordType) recordTypeA.getGreatestSubtype(recordTypeB);
    RecordType greatestSubtypeBA = (RecordType) recordTypeB.getGreatestSubtype(recordTypeA);

    Map<String, RecordProperty> greatestProperties = greatestSubtypeAB.properties;
    assertEquals(propertiesA.size(), greatestProperties.size());
    assertEquals(propertiesA.keySet(), greatestProperties.keySet());
    for (String key : greatestProperties.keySet()) {
      assertEquals(propertiesA.get(key).getType(), greatestProperties.get(key).getType());
    }

    // Record type A with "a" and another record type with "b" should not have a greatest subtype
    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("a", new RecordProperty("a", registry.getStringType()));
    RecordType recordTypeC = new RecordType(registry, propertiesC);
    Map<String, RecordProperty> propertiesD = Maps.newHashMap();
    propertiesD.put("b", new RecordProperty("b", registry.getNumberType()));
    RecordType recordTypeD = new RecordType(registry, propertiesD);
    assertNull(recordTypeA.getGreatestSubtype(recordTypeD));
    assertNull(recordTypeD.getGreatestSubtype(recordTypeA));

    // Record type A with "a" and a union type with "b" should have a greatest subtype
    UnionType unionType = new UnionType(registry);
    unionType.addAlternate(registry.getNativeObjectType(JSTypeNative.NULL_TYPE));
    unionType.addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    unionType.addAlternate(registry.getNativeObjectType(JSTypeNative.BOOLEAN_TYPE));
    RecordType recordTypeE = new RecordType(registry, propertiesA);
    RecordType greatestSubtypeAE = (RecordType) recordTypeE.getGreatestSubtype(unionType);
    Map<String, RecordProperty> greatestPropertiesE = greatestSubtypeAE.properties;
    assertEquals(propertiesA.size() + 1, greatestPropertiesE.size());
    assertEquals(propertiesA.keySet(), greatestPropertiesE.keySet());
    for (String key : greatestPropertiesE.keySet()) {
      assertEquals(propertiesA.get(key).getType(), greatestPropertiesE.get(key).getType());
    }
    assertTrue(greatestPropertiesE.containsKey("b"));
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestPropertiesE.get("b").getType());
  }

  @Test
  public void testRecordTypeToString() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getStringType()));
    properties.put("b", new RecordProperty("b", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);
    String stringRepresentation = recordType.toString();

    // String representation should start with "Record<" and end with ">"
    assertTrue(stringRepresentation.startsWith("Record<"));
    assertTrue(stringRepresentation.endsWith(">"));

    // String representation should contain all property keys and property types
    int indexOfLastComma = stringRepresentation.lastIndexOf(",");
    String typeString = stringRepresentation.substring(indexOfLastComma + 1, stringRepresentation.length());
    int indexOfLastBracket = typeString.lastIndexOf(">");
    String typeStringWithoutBrackets = typeString.substring(0, indexOfLastBracket);
    String[] propertiesInString = typeStringWithoutBrackets.split(" ");
    assertEquals(properties.size(), propertiesInString.length);
    for (int i = 0; i < properties.size(); i++) {
      RecordProperty recordProperty = properties.get(String.valueOf(i + 1));
      String propertyString = propertiesInString[i];
      String[] parts = propertyString.split(":");
      assertEquals(recordProperty.getName(), parts[0]);
      assertEquals(recordProperty.getType().toString(), parts[1]);
    }
  }
}