import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.UnionType;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    // Given
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    // When
    RecordType recordType = new RecordType(registry, properties);

    // Then
    assertEquals(String.class, recordType.getPropertyType("name"));
    assertEquals(Integer.class, recordType.getPropertyType("age"));
    assertTrue(recordType.isEquivalentTo(new RecordType(registry, properties)));
  }

  @Test
  public void testRecordTypeSubtyping() {
    // Given
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Integer.class));
    propertiesB.put("gender", new RecordProperty("gender", String.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    // When
    UnionType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);

    // Then
    assertEquals(recordTypeB, greatestSubtype.toMaybeRecordType());
  }

  @Test
  public void testRecordTypeFrozen() {
    // Given
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, properties);

    // When
    recordType.defineProperty("gender", String.class, true, null);

    // Then
    assertFalse(recordType.isFrozen());

    // When
    recordType.freeze();

    // Then
    assertTrue(recordType.isFrozen());

    // Given
    Map<String, RecordProperty> newProperties = Maps.newHashMap();
    newProperties.put("color", new RecordProperty("color", String.class));

    // When
    recordType.defineProperty("color", String.class, true, null);

    // Then
    assertTrue(recordType.hasProperty("color"));
    assertEquals(String.class, recordType.getPropertyType("color"));
  }
}