import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testRecordTypeCreation() {
    RecordProperty recordProperty1 = new RecordProperty("property1", int.class);
    RecordProperty recordProperty2 = new RecordProperty("property2", String.class);

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("property1", recordProperty1);
    properties.put("property2", recordProperty2);

    RecordType expectedRecordType = new RecordType(registry, properties, true);

    RecordType actualRecordType = new RecordType(registry, properties, true);

    assertEquals(expectedRecordType, actualRecordType);
  }

  @Test
  public void testRecordTypeIsSubtypeOfRecordWithSameProperties() {
    RecordProperty recordProperty1 = new RecordProperty("property1", int.class);

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("property1", recordProperty1);

    RecordType recordType1 = new RecordType(registry, properties1, true);

    RecordProperty recordProperty2 = new RecordProperty("property1", int.class);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("property1", recordProperty2);

    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testRecordTypeIsNotSubtypeOfRecordWithDifferentProperties() {
    RecordProperty recordProperty1 = new RecordProperty("property1", int.class);

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("property1", recordProperty1);

    RecordType recordType1 = new RecordType(registry, properties1, true);

    RecordProperty recordProperty2 = new RecordProperty("property2", String.class);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("property2", recordProperty2);

    assertFalse(recordType1.isSubtype(new RecordType(registry, properties2, true)));
  }

  @Test
  public void testRecordTypeIsSubtypeOfObjectType() {
    RecordProperty recordProperty1 = new RecordProperty("property1", int.class);

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("property1", recordProperty1);

    RecordType recordType = new RecordType(registry, properties, true);

    assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordType));
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeWithNullRecordProperty() {
    RecordProperty recordProperty = null;

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("property1", recordProperty);

    try {
      new RecordType(registry, properties, true);
    } catch (IllegalStateException e) {
      assertEquals("RecordProperty associated with a property should not be null!", e.getMessage());
      throw e;
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRecordTypeWithDuplicateProperty() {
    RecordProperty recordProperty1 = new RecordProperty("property1", int.class);

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("property1", recordProperty1);
    properties.put("property1", new RecordProperty("property1", String.class));

    try {
      new RecordType(registry, properties, true);
    } catch (IllegalArgumentException e) {
      assertEquals("Duplicate property 'property1' in RecordType.", e.getMessage());
      throw e;
    }
  }
}