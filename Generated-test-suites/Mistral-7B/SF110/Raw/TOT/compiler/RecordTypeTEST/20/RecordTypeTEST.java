import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType expectedRecordType = new RecordType(registry, properties, true);
    assertEquals(expectedRecordType.getClass(), RecordType.class);
    assertEquals(expectedRecordType.getPrettyPrint(), true);
    assertEquals(expectedRecordType.isDeclared(), true);
    assertEquals(expectedRecordType.getImplicitPrototype().getClass(), Object.class);

    String propertyName = "name";
    String propertyValue = "John Doe";
    Node propertyNode = new TextNode(propertyValue);

    assertEquals(expectedRecordType.getPropertyType(propertyName), String.class);
    assertEquals(expectedRecordType.getPropertyNode(propertyName), propertyNode);

    String otherPropertyName = "age";
    int otherPropertyValue = 30;
    Node otherPropertyNode = new IntegerLiteralNode(otherPropertyValue);

    properties.put(otherPropertyName, new RecordProperty(otherPropertyName, Integer.class));
    RecordType actualRecordType = new RecordType(registry, properties, false);

    assertEquals(expectedRecordType, actualRecordType);
    assertEquals(expectedRecordType.isSynthetic(), actualRecordType.isSynthetic());
    assertEquals(expectedRecordType.getPropertyType(propertyName), actualRecordType.getPropertyType(propertyName));
    assertEquals(expectedRecordType.getPropertyType(otherPropertyName), actualRecordType.getPropertyType(otherPropertyName));
  }

  @Test
  public void testRecordEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = new java.util.LinkedHashMap<>();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> propertiesB = new java.util.LinkedHashMap<>();
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Integer.class));
    propertiesB.put("city", new RecordProperty("city", String.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, EquivalenceMethod.IDENTITY));
    assertTrue(recordTypeB.checkRecordEquivalenceHelper(recordTypeA, EquivalenceMethod.IDENTITY));

    RecordType recordTypeC = new RecordType(registry, propertiesB, true);
    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeC, EquivalenceMethod.IDENTITY));
    assertFalse(recordTypeC.checkRecordEquivalenceHelper(recordTypeA, EquivalenceMethod.IDENTITY));

    RecordType recordTypeD = new RecordType(registry, propertiesB, false);
    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeD, EquivalenceMethod.IDENTITY));
    assertFalse(recordTypeD.checkRecordEquivalenceHelper(recordTypeA, EquivalenceMethod.IDENTITY));
  }

  @Test
  public void testSubtypeRelationships() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = new java.util.LinkedHashMap<>();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Integer.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new java.util.LinkedHashMap<>();
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Integer.class));
    propertiesB.put("city", new RecordProperty("city", String.class));

    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    Map<String, RecordProperty> propertiesC = new java.util.LinkedHashMap<>();
    propertiesC.put("name", new RecordProperty("name", String.class));
    propertiesC.put("age", new RecordProperty("age", Integer.class));
    propertiesC.put("city", new RecordProperty("city", Integer.class));

    RecordType recordTypeC = new RecordType(registry, propertiesC, true);

    Map<String, RecordProperty> propertiesD = new java.util.LinkedHashMap<>();
    propertiesD.put("name", new RecordProperty("name", String.class));

    RecordType recordTypeD = new RecordType(registry, propertiesD, true);

    RecordType recordTypeE = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    // RecordType A is a subtype of RecordType A (reflexive)
    assertTrue(recordTypeA.isSubtype(recordTypeA));

    // RecordType A is a subtype of RecordType B (transitive)
    assertTrue(recordTypeA.isSubtype(recordTypeB));

    // RecordType B is a supertype of RecordType A (dual of transitivity)
    assertTrue(recordTypeB.isSupertype(recordTypeA));

    // RecordType A is not a subtype of RecordType C (property types don't match)
    assertFalse(recordTypeA.isSubtype(recordTypeC));

    // RecordType C is a subtype of RecordType A (property types are a superset)
    assertTrue(recordTypeC.isSubtype(recordTypeA));

    // RecordType A is not a subtype of RecordType D (has fewer properties)
    assertFalse(recordTypeA.isSubtype(recordTypeD));

    // RecordType D is not a subtype of RecordType A (has fewer properties)
    assertFalse(recordTypeD.isSubtype(recordTypeA));

    // Object type is a supertype of all record types
    assertTrue(recordTypeE.isSupertype(recordTypeA));
    assertTrue(recordTypeE.isSupertype(recordTypeB));
    assertTrue(recordTypeE.isSupertype(recordTypeC));
    assertTrue(recordTypeE.isSupertype(recordTypeD));
  }
}