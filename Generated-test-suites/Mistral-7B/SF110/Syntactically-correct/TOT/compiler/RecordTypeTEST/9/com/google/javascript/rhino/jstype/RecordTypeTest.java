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
import java.util.Set;

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
    // Test creating a declared record type
    Map<String, RecordProperty> propertiesDeclared = new HashMap<>();
    propertiesDeclared.put("name", new RecordProperty("name", String.class));
    propertiesDeclared.put("age", new RecordProperty("age", Integer.class));

    RecordType recordTypeDeclared = new RecordType(registry, propertiesDeclared, true);

    assertEquals("Record type with declared properties",
        "Record<name: string, age: number>", recordTypeDeclared.toString());

    // Test creating a synthesized record type
    Map<String, RecordProperty> propertiesSynthesized = new HashMap<>();
    propertiesSynthesized.put("name", new RecordProperty("name", String.class));
    propertiesSynthesized.put("age", new RecordProperty("age", Integer.class));

    RecordType recordTypeSynthesized = new RecordType(registry, propertiesSynthesized, false);

    assertEquals("Record type with synthesized properties",
        "Record<name: string, age: number>", recordTypeSynthesized.toString());
  }

  @Test
  public void testIsSynthetic() {
    Map<String, RecordProperty> propertiesDeclared = new HashMap<>();
    propertiesDeclared.put("name", new RecordProperty("name", String.class));
    propertiesDeclared.put("age", new RecordProperty("age", Integer.class));

    RecordType recordTypeDeclared = new RecordType(registry, propertiesDeclared, true);

    assertFalse("Declared record type should not be synthetic", recordTypeDeclared.isSynthetic());

    Map<String, RecordProperty> propertiesSynthesized = new HashMap<>();
    propertiesSynthesized.put("name", new RecordProperty("name", String.class));
    propertiesSynthesized.put("age", new RecordProperty("age", Integer.class));

    RecordType recordTypeSynthesized = new RecordType(registry, propertiesSynthesized, false);

    assertTrue("Synthesized record type should be synthetic", recordTypeSynthesized.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalence() {
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Integer.class));
    propertiesB.put("gender", new RecordProperty("gender", String.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertTrue("Record types A and B should be equivalent",
        recordTypeA.checkRecordEquivalenceHelper(recordTypeB, EquivalenceMethod.IDENTICAL));

    RecordType recordTypeC = new RecordType(registry, propertiesB, true);

    assertTrue("Record types A and C should be equivalent since they have the same properties",
        recordTypeA.checkRecordEquivalenceHelper(recordTypeC, EquivalenceMethod.IDENTICAL));

    RecordType recordTypeD = new RecordType(registry, propertiesB, false);

    assertFalse("Record types A and D should not be equivalent since they have different synthesis flags",
        recordTypeA.checkRecordEquivalenceHelper(recordTypeD, EquivalenceMethod.IDENTICAL));
  }

  @Test
  public void testGetGreatestSubtype() {
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Integer.class));
    propertiesB.put("gender", new RecordProperty("gender", String.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);

    assertEquals("The greatest subtype of record type A and B should be record type A",
        recordTypeA, greatestSubtype);

    Map<String, RecordProperty> propertiesC = new HashMap<>();
    propertiesC.put("name", new RecordProperty("name", String.class));
    propertiesC.put("age", new RecordProperty("age", Integer.class));
    propertiesC.put("gender", new RecordProperty("gender", String.class));
    propertiesC.put("city", new RecordProperty("city", String.class));

    RecordType recordTypeC = new RecordType(registry, propertiesC, true);

    JSType greatestSubtype2 = recordTypeA.getGreatestSubtypeHelper(recordTypeC);

    assertEquals("The greatest subtype of record type A and C should be record type A",
        recordTypeA, greatestSubtype2);

    JSType emptyRecordType = registry.getNativeObjectType(JSTypeNative.EMPTY_OBJECT_TYPE);

    JSType greatestSubtype3 = recordTypeA.getGreatestSubtypeHelper(emptyRecordType);

    assertEquals("The greatest subtype of record type A and the empty record type should be the empty record type",
        emptyRecordType, greatestSubtype3);
  }

  @Test
  public void testDefineProperty() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, properties, true);

    Node namePropertyNode = Node.newName("name");
    JSType nameType = registry.getStringType();

    assertTrue("Record type should allow defining a new property",
        recordType.defineProperty("newProperty", nameType, false, namePropertyNode));

    assertFalse("Record type should not allow redefining an existing property",
        recordType.defineProperty("name", nameType, false, namePropertyNode));

    assertFalse("Record type should not allow defining a property with the same name but different type",
        recordType.defineProperty("name", registry.getBooleanType(), false, namePropertyNode));
  }
}