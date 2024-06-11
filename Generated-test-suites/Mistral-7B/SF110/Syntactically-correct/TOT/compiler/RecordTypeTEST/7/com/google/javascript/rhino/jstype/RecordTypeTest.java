import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.TypeFactory;
import com.google.javascript.rhino.jstype.UnionType;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    RecordProperty prop1 = new RecordProperty("prop1", int.class);
    RecordProperty prop2 = new RecordProperty("prop2", String.class);

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("prop1", prop1);
    properties.put("prop2", prop2);

    RecordType record = new RecordType(registry, properties, true);

    assertEquals("Record", record.getName());
    assertEquals(true, record.isDeclared());
    assertEquals(Arrays.asList("prop1", "prop2"), record.getOwnPropertyNames());
    assertEquals(int.class, record.getPropertyType("prop1"));
    assertEquals(String.class, record.getPropertyType("prop2"));
    assertFalse(record.isFrozen());

    RecordType frozenRecord = new RecordType(registry, properties, true);
    frozenRecord.freeze();
    assertTrue(frozenRecord.isFrozen());
  }

  @Test
  public void testRecordTypeCreationWithNullProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    RecordProperty prop1 = new RecordProperty("prop1", int.class);
    RecordProperty prop2 = null;

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("prop1", prop1);
    properties.put("prop2", prop2);

    try {
      new RecordType(registry, properties, true);
      fail();
    } catch (IllegalStateException e) {
      // expected
    }
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    RecordProperty prop1 = new RecordProperty("prop1", int.class);
    RecordProperty prop2 = new RecordProperty("prop2", String.class);

    Map<String, RecordProperty> properties1 = new HashMap<>();
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties1.put("prop1", prop1);
    properties1.put("prop2", prop2);
    properties2.put("prop2", prop2);
    properties2.put("prop1", prop1);

    RecordType record1 = new RecordType(registry, properties1, true);
    RecordType record2 = new RecordType(registry, properties2, true);

    assertTrue(record1.checkRecordEquivalenceHelper(record2, EquivalenceMethod.PROTOTYPE));
    assertTrue(record2.checkRecordEquivalenceHelper(record1, EquivalenceMethod.PROTOTYPE));

    RecordType record3 = new RecordType(registry, properties1, false);
    assertFalse(record1.checkRecordEquivalenceHelper(record3, EquivalenceMethod.PROTOTYPE));
    assertFalse(record3.checkRecordEquivalenceHelper(record1, EquivalenceMethod.PROTOTYPE));
  }

  @Test
  public void testRecordTypeGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType record = new RecordType(registry, new HashMap<>(), true);

    ObjectType implicitPrototype = record.getImplicitPrototype();
    assertEquals("Object", implicitPrototype.getName());
  }

  @Test
  public void testRecordTypeDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    RecordProperty prop = new RecordProperty("prop", int.class);

    RecordType record = new RecordType(registry, new HashMap<>(), false);
    assertTrue(record.defineProperty("prop", TypeFactory.INT, false, null));
    assertEquals(int.class, record.getPropertyType("prop"));
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeDefinePropertyFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    RecordType record = new RecordType(registry, new HashMap<>(), true);
    record.freeze();

    record.defineProperty("prop", TypeFactory.INT, false, null);
  }

  @Test
  public void testRecordTypeGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType typeA = registry.createRecordType("RecordA", new HashMap<>(), true);
    JSType recordB = registry.createRecordType("RecordB", new HashMap<>(), false);
    JSType typeC = registry.createRecordType("RecordC", new HashMap<>(), true);

    RecordProperty prop1 = new RecordProperty("prop1", int.class);
    RecordProperty prop2 = new RecordProperty("prop2", String.class);

    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("prop1", prop1);
    propertiesA.put("prop2", prop2);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    Map<String, RecordProperty> propertiesC = new HashMap<>();

    propertiesB.put("prop1", prop1);
    propertiesB.put("prop2", prop2);
    propertiesB.put("prop3", prop1);

    propertiesC.put("prop1", prop1);
    propertiesC.put("prop2", prop2);

    RecordType recordAB = new RecordType(registry, propertiesA, true);
    RecordType recordBC = new RecordType(registry, propertiesB, true);
    RecordType recordC = new RecordType(registry, propertiesC, true);

    JSType greatestSubtypeAB = recordAB.getGreatestSubtype(recordBC);
    assertEquals(recordBC, greatestSubtypeAB);

    JSType greatestSubtypeAC = recordAB.getGreatestSubtype(recordC);
    JSType greatestSubtypeCA = recordC.getGreatestSubtype(recordAB);

    UnionType greatestSubtypeACUnion = (UnionType) greatestSubtypeAC;
    assertEquals(2, greatestSubtypeACUnion.getAlternates().size());
    assertEquals(recordAB, greatestSubtypeACUnion.getAlternates().get(0));
    assertEquals(recordC, greatestSubtypeACUnion.getAlternates().get(1));

    assertEquals(greatestSubtypeAC, greatestSubtypeCA);
  }

  @Test
  public void testRecordTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType typeA = registry.createRecordType("RecordA", new HashMap<>(), true);
    JSType recordB = registry.createRecordType("RecordB", new HashMap<>(), false);
    JSType typeC = registry.createRecordType("RecordC", new HashMap<>(), true);

    RecordProperty prop1 = new RecordProperty("prop1", int.class);
    RecordProperty prop2 = new RecordProperty("prop2", String.class);

    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("prop1", prop1);
    propertiesA.put("prop2", prop2);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    Map<String, RecordProperty> propertiesC = new HashMap<>();

    propertiesB.put("prop1", prop1);
    propertiesB.put("prop2", prop2);
    propertiesB.put("prop3", prop1);

    propertiesC.put("prop1", prop1);
    propertiesC.put("prop2", prop2);

    RecordType recordAB = new RecordType(registry, propertiesA, true);
    RecordType recordBC = new RecordType(registry, propertiesB, true);
    RecordType recordC = new RecordType(registry, propertiesC, true);

    assertTrue(recordAB.isSubtype(typeA));
    assertTrue(recordAB.isSubtype(recordAB));
    assertTrue(recordAB.isSubtype(recordBC));
    assertTrue(recordAB.isSubtype(typeC));

    assertFalse(recordAB.isSubtype(int.class));
    assertFalse(recordAB.isSubtype(float.class));
    assertFalse(recordAB.isSubtype(RecordType.class));

    assertTrue(typeC.isSubtype(recordAB));
    assertTrue(typeC.isSubtype(typeC));
    assertTrue(typeC.isSubtype(recordBC));
    assertTrue(typeC.isSubtype(typeA));

    assertFalse(typeC.isSubtype(int.class));
    assertFalse(typeC.isSubtype(float.class));
    assertFalse(typeC.isSubtype(RecordType.class));
  }
}