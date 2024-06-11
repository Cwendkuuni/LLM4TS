import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, properties);
    assertEquals("Expected record type creation to succeed", recordType.getClass(), RecordType.class);
  }

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", String.class));
    properties2.put("age", new RecordProperty("age", Integer.class));
    properties2.put("gender", new RecordProperty("gender", String.class));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue("Expected record types to be equivalent when they have the same properties", recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsEquivalentToDifferentTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", Boolean.class));
    properties2.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse("Expected record types to be unequal when they have different properties", recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", String.class));
    properties2.put("age", new RecordProperty("age", Long.class));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue("Expected record type1 to be a subtype of record type2 since record type2 has more properties but matching ones", recordType1.isSubtype(recordType2));
  }

  @Test
  public void testIsSubtypeFail() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", Boolean.class));
    properties2.put("age", new RecordProperty("age", Integer.class));
    properties2.put("gender", new RecordProperty("gender", String.class));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse("Expected record type1 not to be a subtype of record type2 since it lacks the 'gender' property", recordType1.isSubtype(recordType2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDefinePropertyFrozenRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, properties);
    recordType.freeze();

    try {
      recordType.defineProperty("name", String.class, false, null);
    } catch (IllegalStateException e) {
      fail("Expected an IllegalStateException, but got an IllegalArgumentException. Message should be 'RecordType is frozen.'");
    }

    // The IllegalStateException should have been thrown here
  }
}