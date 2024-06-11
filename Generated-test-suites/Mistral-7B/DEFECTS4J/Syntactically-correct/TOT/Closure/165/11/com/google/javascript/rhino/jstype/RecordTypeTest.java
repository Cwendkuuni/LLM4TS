import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RecordTypeTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties);

    // Test properties
    assertEquals(2, recordType.getProperties().size());
    assertEquals("name", recordType.getPropertyNames().get(0));
    assertEquals("age", recordType.getPropertyNames().get(1));
    assertEquals(String.class, recordType.getPropertyType("name"));
    assertEquals(Number.class, recordType.getPropertyType("age"));

    // Test subtyping
    RecordType subTypeRecord = new RecordType(registry, recordProperties);
    RecordType superTypeRecord = new RecordType(registry, recordProperties);
    superTypeRecord.addProperty("address", String.class, true, Node.NULL);

    assertEquals(superTypeRecord, recordType.getGreatestSubtype(subTypeRecord));
    assertEquals(subTypeRecord, recordType.getGreatestSubtype(superTypeRecord));

    // Test equivalence
    RecordType equivalentRecord = new RecordType(registry, recordProperties);
    assertEquals(recordType, equivalentRecord);

    // Test frozen property
    assertEquals(false, recordType.isFrozen());
    recordType.freeze();
    assertEquals(true, recordType.isFrozen());
  }

  @Test
  public void testRecordTypeGetImplicitPrototype() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));

    RecordType recordType = new RecordType(registry, recordProperties);
    ObjectType prototype = recordType.getImplicitPrototype();

    assertEquals(Object.class, prototype.getClass());
    assertEquals("constructor", recordType.getPropertyNames().get(0), prototype.getPropertyNames().get(0));
  }

  @Test
  public void testRecordTypeIsSubtype() {
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("name", new RecordProperty("name", String.class));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("name", new RecordProperty("name", String.class));
    recordPropertiesB.put("age", new RecordProperty("age", Number.class));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    Map<String, RecordProperty> recordPropertiesC = Maps.newHashMap();
    recordPropertiesC.put("name", new RecordProperty("name", Number.class));
    RecordType recordTypeC = new RecordType(registry, recordPropertiesC);

    // Test A is a subtype of A
    assertTrue(recordTypeA.isSubtype(recordTypeA));

    // Test A is a subtype of B
    assertTrue(recordTypeA.isSubtype(recordTypeB));

    // Test A is not a subtype of C
    assertFalse(recordTypeA.isSubtype(recordTypeC));
  }

  @Test
  public void testRecordTypeDefinition() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties);

    // Test define property
    recordType.defineProperty("city", String.class, false, Node.NULL);
    assertEquals(3, recordType.getProperties().size());
    assertEquals("city", recordType.getPropertyNames().get(2));

    // Test frozen property
    recordType.freeze();

    try {
      recordType.defineProperty("name", String.class, false, Node.NULL);
      fail();
    } catch (IllegalStateException e) {
      // Expected
    }

    try {
      recordType.defineProperty("age", Number.class, false, Node.NULL);
      fail();
    } catch (IllegalStateException e) {
      // Expected
    }
  }
}