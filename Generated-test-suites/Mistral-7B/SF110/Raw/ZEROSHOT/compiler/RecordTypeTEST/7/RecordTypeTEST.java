import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {

  private static RecordType createRecordType(Map<String, RecordProperty> properties) {
    JSTypeRegistry registry = new JSTypeRegistry();
    return new RecordType(registry, properties);
  }

  @org.junit.Test
  public void testRecordTypeConstruction() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = createRecordType(properties);
    assertEquals("RecordType", recordType.getName());
    assertEquals(2, recordType.getOwnPropertyNames().size());
  }

  @org.junit.Test
  public void testRecordTypeIsFrozen() {
    Map<String, RecordProperty> properties = new HashMap<>();
    RecordType recordType = createRecordType(properties);

    assertFalse(recordType.isFrozen());

    recordType.freeze();

    assertTrue(recordType.isFrozen());
  }

  @org.junit.Test
  public void testRecordTypeCheckEquivalenceHelper() {
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    Map<String, RecordProperty> propertiesB = new HashMap<>();

    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Number.class));

    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Number.class));
    propertiesB.put("email", new RecordProperty("email", String.class));

    RecordType recordTypeA = createRecordType(propertiesA);
    RecordType recordTypeB = createRecordType(propertiesB);

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, EquivalenceMethod.STRICT));

    assertFalse(recordTypeA.checkRecordEquivalenceHelper(new ObjectType(new JSTypeRegistry(null), null), EquivalenceMethod.STRICT));

    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, EquivalenceMethod.PROTOTYPE));

    Map<String, RecordProperty> propertiesC = new HashMap<>();
    propertiesC.put("name", new RecordProperty("name", String.class));
    propertiesC.put("age", new RecordProperty("age", Number.class));
    propertiesC.put("email", new RecordProperty("email", String.class));

    RecordType recordTypeC = createRecordType(propertiesC);

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeC, EquivalenceMethod.STRICT));

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeC, EquivalenceMethod.PROTOTYPE));
  }

  @org.junit.Test
  public void testRecordTypeGetImplicitPrototype() {
    Map<String, RecordProperty> properties = new HashMap<>();
    RecordType recordType = createRecordType(properties);

    ObjectType implicitPrototype = recordType.getImplicitPrototype();

    assertEquals(Object.class.getName(), implicitPrototype.getName());
  }

  @org.junit.Test
  public void testRecordTypeDefineProperty() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = createRecordType(properties);

    assertFalse(recordType.hasProperty("color"));

    recordType.defineProperty("color", Int.class, false, null);

    assertTrue(recordType.hasProperty("color"));
  }

  @org.junit.Test
  public void testRecordTypeGetGreatestSubtypeHelper() {
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Number.class));
    propertiesB.put("color", new RecordProperty("color", Int.class));

    RecordType recordTypeA = createRecordType(propertiesA);
    RecordType recordTypeB = createRecordType(propertiesB);

    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);

    UnionType unionType = (UnionType) greatestSubtype;
    assertEquals(3, unionType.getAlternates().size());
    assertTrue(unionType.getAlternates().contains(recordTypeA));
    assertTrue(unionType.getAlternates().contains(recordTypeB));
    assertTrue(unionType.getAlternates().contains(createRecordType(propertiesB)));
  }

  @org.junit.Test
  public void testRecordTypeIsSubtype() {
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Number.class));
    propertiesB.put("color", new RecordProperty("color", Int.class));

    RecordType recordTypeA = createRecordType(propertiesA);
    RecordType recordTypeB = createRecordType(propertiesB);

    assertTrue(recordTypeA.isSubtype(recordTypeB));

    JSType nativeObjectType = new ObjectType(new JSTypeRegistry(null), null);

    assertFalse(recordTypeA.isSubtype(nativeObjectType));

    RecordType recordTypeC = createRecordType(propertiesB);

    assertTrue(recordTypeB.isSubtype(recordTypeC));

    assertTrue(recordTypeA.isSubtype(recordTypeC));

    Map<String, RecordProperty> propertiesD = new HashMap<>();
    propertiesD.put("name", new RecordProperty("name", String.class));
    propertiesD.put("age", new RecordProperty("age", Number.class));
    propertiesD.put("email", new RecordProperty("email", String.class));

    RecordType recordTypeD = createRecordType(propertiesD);

    assertFalse(recordTypeB.isSubtype(recordTypeD));
  }

  @org.junit.Test
  public void testRecordTypeIsSubtypeWithInvariantProperties() {
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Number.class));
    propertiesB.put("email", new RecordProperty("email", String.class));

    RecordType recordTypeA = createRecordType(propertiesA);
    RecordType recordTypeB = createRecordType(propertiesB);

    Map<String, RecordProperty> propertiesC = new HashMap<>();
    propertiesC.put("name", new RecordProperty("name", String.class));
    propertiesC.put("age", new RecordProperty("age", Number.class));

    RecordType recordTypeC = createRecordType(propertiesC);

    RecordType recordTypeD = createRecordType(Arrays.asList("name", "age").toArray(new String[0]));

    assertTrue(recordTypeA.isSubtype(recordTypeB));

    assertTrue(recordTypeA.isSubtype(recordTypeC));

    assertTrue(recordTypeA.isSubtype(recordTypeD));

    RecordType recordTypeE = new RecordType(new JSTypeRegistry(null), Arrays.asList(
        new RecordProperty("name", String.class),
        new RecordProperty("age", Number.class),
        new RecordProperty("email", String.class),
        new RecordProperty("color", Int.class)).toArray(new RecordProperty[0]));

    assertFalse(recordTypeA.isSubtype(recordTypeE));
  }

  @org.junit.Test
  public void testRecordTypeIsSubtypeWithNonInvariantProperties() {
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Number.class));
    propertiesB.put("color", new RecordProperty("color", Object.class));

    RecordType recordTypeA = createRecordType(propertiesA);
    RecordType recordTypeB = createRecordType(propertiesB);

    Map<String, RecordProperty> propertiesC = new HashMap<>();
    propertiesC.put("name", new RecordProperty("name", String.class));
    propertiesC.put("age", new RecordProperty("age", Number.class));
    propertiesC.put("color", new RecordProperty("color", Int.class));

    RecordType recordTypeC = createRecordType(propertiesC);

    RecordType recordTypeD = createRecordType(Arrays.asList("name", "age").toArray(new String[0]));

    assertFalse(recordTypeA.isSubtype(recordTypeB));

    assertFalse(recordTypeA.isSubtype(recordTypeC));

    assertFalse(recordTypeA.isSubtype(recordTypeD));

    RecordType recordTypeE = new RecordType(new JSTypeRegistry(null), Arrays.asList(
        new RecordProperty("name", String.class),
        new RecordProperty("age", Number.class),
        new RecordProperty("email", String.class),
        new RecordProperty("color", Object.class)).toArray(new RecordProperty[0]));

    assertFalse(recordTypeA.isSubtype(recordTypeE));
  }

  @org.junit.Test
  public void testRecordTypeGetPropertyTypeWithDeclaredProperty() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class) {
      @Override
      public boolean isInvariant(JSType other) {
        return false;
      }
    });
    properties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = createRecordType(properties);

    JSType nameType = recordType.getPropertyType("name");

    assertFalse(nameType.isInvariant(String.class));
  }

  @org.junit.Test
  public void testRecordTypeGetPropertyTypeWithUndefinedProperty() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = createRecordType(properties);

    try {
      recordType.getPropertyType("color");
      fail();
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @org.junit.Test
  public void testRecordTypeToString() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = createRecordType(properties);

    String recordTypeString = recordType.toString();

    assertTrue(recordTypeString.contains("RecordType"));
    assertTrue(recordTypeString.contains("name: String"));
    assertTrue(recordTypeString.contains("age: Number"));
  }
}