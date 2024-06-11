import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("property1", new RecordProperty("property1", JSType.STRING));
    recordProperties.put("property2", new RecordProperty("property2", JSType.NUMBER));

    RecordType recordType = new RecordType(registry, recordProperties);

    assertEquals("RecordType", recordType.getClassName());
    assertEquals(2, recordType.getPropertyCount());

    // Test getPropertyType
    assertEquals(JSType.STRING, recordType.getPropertyType("property1"));
    assertEquals(JSType.NUMBER, recordType.getPropertyType("property2"));

    // Test isSubtype
    assertTrue(recordType.isSubtype(new RecordType(registry, recordProperties)));
    assertFalse(recordType.isSubtype(new ObjectType(registry, null, null)));

    // Test isEquivalentTo
    assertTrue(recordType.isEquivalentTo(new RecordType(registry, recordProperties)));
    assertFalse(recordType.isEquivalentTo(new ObjectType(registry, null, null)));

    // Test defineProperty
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    recordTypeBuilder.addProperty("property1", JSType.STRING, null);
    recordTypeBuilder.addProperty("property2", JSType.NUMBER, null);
    RecordType recordType2 = recordTypeBuilder.build();

    // Add new property to recordType
    recordType.defineProperty("property3", JSType.BOOLEAN, false, null);
    assertFalse(recordType.isEquivalentTo(recordType2));

    // Test getLeastSupertype
    RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
    recordType3.defineProperty("property2", JSType.STRING, false, null);

    assertTrue(recordType.isSubtype(recordType3.getLeastSupertype(recordType)));
    assertTrue(recordType3.isSubtype(recordType.getLeastSupertype(recordType3)));

    // Test getGreatestSubtypeHelper
    RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
    recordType4.defineProperty("property1", JSType.NUMBER, false, null);

    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(recordType4);

    assertEquals(JSType.RECORD, greatestSubtype.getTypeClass());
    Map<String, JSType> properties = greatestSubtype.toMaybeRecordType().properties;
    assertEquals(2, properties.size());
    assertEquals("property1", properties.keySet().iterator().next());
    assertEquals("property2", properties.keySet().iterator().next());
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeFrozenError() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("property1", new RecordProperty("property1", JSType.STRING));
    recordProperties.put("property2", new RecordProperty("property2", JSType.NUMBER));

    RecordType recordType = new RecordType(registry, recordProperties);

    recordType.defineProperty("property3", JSType.BOOLEAN, false, null);
  }
}