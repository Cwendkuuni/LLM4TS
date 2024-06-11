import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class RecordTypeTest {
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testCreateRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, properties);
    RecordType expectedRecordType = new RecordType(registry, properties);

    assertEquals(expectedRecordType, recordType);
  }

  @Test
  public void testIsEquivalentTo() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", String.class));
    properties2.put("age", new RecordProperty("age", Integer.class));
    properties2.put("gender", new RecordProperty("gender", String.class));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isEquivalentTo(recordType2));

    RecordType expectedRecordType = new RecordType(registry, properties1);
    assertTrue(recordType1.isEquivalentTo(expectedRecordType));
  }

  @Test
  public void testGetImplicitPrototype() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, properties);
    ObjectType expectedPrototype = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    assertEquals(expectedPrototype, recordType.getImplicitPrototype());
  }

  @Test
  public void testDefineProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, properties);

    assertFalse(recordType.isFrozen());

    recordType.defineProperty("gender", String.class, false, null);

    assertTrue(recordType.isFrozen());

    assertEquals(String.class, recordType.getPropertyType("gender"));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Integer.class));
    propertiesB.put("gender", new RecordProperty("gender", String.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);

    RecordType expectedRecordType = new RecordType(registry, propertiesA);
    expectedRecordType.addProperty("gender", String.class, false, null);

    assertEquals(expectedRecordType, greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Integer.class));
    propertiesB.put("gender", new RecordProperty("gender", String.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(recordTypeB));
    assertFalse(recordTypeB.isSubtype(recordTypeA));
  }

  @Test(expected = IllegalStateException.class)
  public void testDefineProperty_FrozenRecord() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, properties);

    recordType.isFrozen = true;

    recordType.defineProperty("gender", String.class, false, null);

    fail("Expected an IllegalStateException");
  }
}