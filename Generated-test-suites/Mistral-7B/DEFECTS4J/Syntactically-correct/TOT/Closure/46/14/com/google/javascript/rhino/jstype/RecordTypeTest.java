import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  private static JSTypeRegistry registry;

  @org.junit.BeforeClass
  public static void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordProperty recordProperty1 = new RecordProperty("name", String.class);
    RecordProperty recordProperty2 = new RecordProperty("age", Integer.class);
    recordProperties.put("name", recordProperty1);
    recordProperties.put("age", recordProperty2);

    RecordType recordType = new RecordType(registry, recordProperties);

    assertNotNull(recordType);
    assertEquals("RecordType", recordType.getClassName());
    assertEquals(2, recordType.getDeclaredProperties().length);

    String name = "John Doe";
    Integer age = 30;

    Node nameNode = registry.newTextNode(name);
    Node ageNode = registry.newIntLiteralNode(age);

    recordType.defineProperty("name", String.class, true, nameNode);
    recordType.defineProperty("age", Integer.class, true, ageNode);

    Object recordInstance = registry.newObject(recordType);

    assertTrue(recordInstance instanceof Record);
    Record record = (Record) recordInstance;

    assertEquals(name, record.get("name"));
    assertEquals(age, record.get("age"));

    record.set("name", "Jane Doe");
    assertEquals("Jane Doe", record.get("name"));

    record.set("age", 35);
    assertEquals(35, record.get("age"));
  }

  @Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    RecordProperty recordProperty11 = new RecordProperty("name", String.class);
    RecordProperty recordProperty12 = new RecordProperty("age", Integer.class);
    recordProperties1.put("name", recordProperty11);
    recordProperties1.put("age", recordProperty12);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    RecordProperty recordProperty21 = new RecordProperty("name", String.class);
    RecordProperty recordProperty22 = new RecordProperty("age", Integer.class);
    recordProperties2.put("name", recordProperty21);
    recordProperties2.put("age", recordProperty22);

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertEquals(recordType1, recordType1);
    assertEquals(recordType1, recordType2);

    recordType1.properties.put("gender", registry.getBooleanType());
    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    RecordProperty recordProperty11 = new RecordProperty("name", String.class);
    RecordProperty recordProperty12 = new RecordProperty("age", Integer.class);
    recordProperties1.put("name", recordProperty11);
    recordProperties1.put("age", recordProperty12);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    RecordProperty recordProperty21 = new RecordProperty("name", String.class);
    RecordProperty recordProperty22 = new RecordProperty("age", Integer.class);
    RecordProperty recordProperty23 = new RecordProperty("gender", Boolean.class);
    recordProperties2.put("name", recordProperty21);
    recordProperties2.put("age", recordProperty22);
    recordProperties2.put("gender", recordProperty23);

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    RecordType recordType3 = new RecordType(registry, recordProperties1);

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType3));
    assertTrue(recordType3.isSubtype(recordType1));

    assertTrue(recordType1.isSubtype(recordType2));

    RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
    assertFalse(recordType1.isSubtype(recordType4));
  }

  @Test
  public void testRecordTypeLeastSupertype() {
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    RecordProperty recordPropertyA1 = new RecordProperty("name", String.class);
    RecordProperty recordPropertyA2 = new RecordProperty("age", Integer.class);
    recordPropertiesA.put("name", recordPropertyA1);
    recordPropertiesA.put("age", recordPropertyA2);

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    RecordProperty recordPropertyB1 = new RecordProperty("name", String.class);
    RecordProperty recordPropertyB2 = new RecordProperty("age", Integer.class);
    RecordProperty recordPropertyB3 = new RecordProperty("gender", Boolean.class);
    recordPropertiesB.put("name", recordPropertyB1);
    recordPropertiesB.put("age", recordPropertyB2);
    recordPropertiesB.put("gender", recordPropertyB3);

    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    RecordType recordTypeLeast = recordTypeA.getLeastSupertype(recordTypeB);
    assertEquals(recordTypeB, recordTypeLeast);

    RecordType recordTypeC = new RecordType(registry, Maps.newHashMap());
    RecordType recordTypeD = new RecordType(registry, recordPropertiesB);
    RecordType recordTypeE = new RecordType(registry, recordPropertiesA);

    RecordType recordTypeGreatestSubtype = recordTypeE.getGreatestSubtypeHelper(recordTypeD);
    assertEquals(recordTypeA, recordTypeGreatestSubtype);

    RecordType recordTypeF = new RecordType(registry, Maps.newHashMap());
    RecordType recordTypeG = new RecordType(registry, recordPropertiesA);
    RecordType recordTypeH = new RecordType(registry, recordPropertiesB);

    RecordType recordTypeI = new RecordType(registry, Maps.newHashMap());
    RecordType recordTypeJ = new RecordType(registry, Maps.newHashMap());

    RecordType recordTypeK = recordTypeI.getLeastSupertype(recordTypeJ);
    assertEquals(recordTypeI, recordTypeK);
  }

  @Test
  public void testRecordTypeFreeze() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordProperty recordProperty1 = new RecordProperty("name", String.class);
    RecordProperty recordProperty2 = new RecordProperty("age", Integer.class);
    recordProperties.put("name", recordProperty1);
    recordProperties.put("age", recordProperty2);

    RecordType recordType = new RecordType(registry, recordProperties);

    recordType.defineProperty("gender", registry.getBooleanType());

    assertTrue(recordType.isFrozen());
    assertFalse(recordType.defineProperty("gender", registry.getBooleanType(), false, null));
  }
}