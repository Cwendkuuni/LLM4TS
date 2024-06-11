import com.google.javascript.rhino.JSType;
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
import static org.junit.Assert.assertNotNull;
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
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, properties, true);
    assertEquals("RecordType", recordType.getClassName());
    assertTrue(recordType.isRecordType());
    assertFalse(recordType.isFunctionType());

    ObjectType prototype = recordType.getImplicitPrototype();
    assertEquals("Object", prototype.getClassName());

    RecordType frozenRecordType = new RecordType(registry, properties, true);
    RecordType synthesizedRecordType = new RecordType(registry, properties, false);

    // Test equivalence
    assertTrue(recordType.isEquivalentTo(frozenRecordType));
    assertTrue(recordType.isEquivalentTo(synthesizedRecordType));

    // Test subtype relationship
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    RecordType objectRecordType = registry.getNativeRecordType(JSTypeNative.OBJECT_TYPE);

    assertTrue(recordType.isSubtype(objectType));
    assertTrue(objectRecordType.isSubtype(recordType));
    assertFalse(objectType.isSubtype(recordType));
    assertFalse(recordType.isSubtype(registry.getNativeType(JSTypeNative.BOOL_TYPE)));

    // Test property access
    assertEquals(String.class, recordType.getPropertyType("name"));
    assertEquals(Integer.class, recordType.getPropertyType("age"));
    assertNull(recordType.getPropertyType("unknownProperty"));

    // Test defining a property
    recordType.defineProperty("newProperty", String.class, false, null);
    assertEquals(String.class, recordType.getPropertyType("newProperty"));

    // Test freezing the record type
    recordType.freeze();
    assertTrue(recordType.isFrozen());
    assertFalse(recordType.defineProperty("newProperty", String.class, false, null));
  }

  @Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType1 = new RecordType(registry, properties, true);
    RecordType recordType2 = new RecordType(registry, properties, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.STRICT));
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.LAX));

    properties.put("newProperty", new RecordProperty("newProperty", String.class));
    RecordType recordType3 = new RecordType(registry, properties, true);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.STRICT));
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.LAX));
  }

  @Test
  public void testRecordTypeSubtypeRelationship() {
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Integer.class));
    propertiesB.put("newProperty", new RecordProperty("newProperty", String.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    // RecordTypeA is a subtype of RecordTypeB
    assertTrue(recordTypeA.isSubtype(recordTypeB));

    // RecordTypeB is not a subtype of RecordTypeA
    assertFalse(recordTypeB.isSubtype(recordTypeA));

    // RecordTypeA is a subtype of RecordTypeC if RecordTypeC has all the properties of RecordTypeA
    RecordType recordTypeC = new RecordType(registry, propertiesA, true);
    assertTrue(recordTypeA.isSubtype(recordTypeC));

    // RecordTypeA is not a subtype of RecordTypeD if RecordTypeD has more properties than RecordTypeA
    RecordType recordTypeD = new RecordType(registry, new HashMap<>(), true);
    assertFalse(recordTypeA.isSubtype(recordTypeD));

    // RecordTypeA is a subtype of RecordTypeE if RecordTypeE is an interface type and has all the properties of RecordTypeA
    RecordType recordTypeE = new RecordTypeBuilder(registry).buildInterfaceType("IRecordType", propertiesA.keySet(), propertiesA.values().toArray(new RecordProperty[0]));
    assertTrue(recordTypeA.isSubtype(recordTypeE));
  }
}