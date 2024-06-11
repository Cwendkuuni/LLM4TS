import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.*;

public class RecordTypeTest {
  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordProperty recordPropertyA = new RecordProperty("A", String.class);
    RecordProperty recordPropertyB = new RecordProperty("B", Integer.class);
    recordProperties.put("A", recordPropertyA);
    recordProperties.put("B", recordPropertyB);

    RecordType recordType = new RecordType(registry, recordProperties);

    // Basic functionality test
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordType.getImplicitPrototype());

    // Test property access
    assertEquals(String.class, recordType.getPropertyType("A"));
    assertEquals(Integer.class, recordType.getPropertyType("B"));

    // Test property definition
    recordType.defineProperty("C", String.class, false, null);
    assertEquals(String.class, recordType.getPropertyType("C"));

    // Test subtyping
    RecordType recordTypeSubtype = new RecordType(registry, recordProperties);
    RecordType recordTypeSupertype = new RecordType(registry, recordProperties);
    recordTypeSupertype.addProperty("C", String.class, false, null);
    assertTrue(recordTypeSubtype.isSubtype(recordTypeSupertype));

    // Test property type equivalence
    RecordType recordTypeEquivalent = new RecordType(registry, recordProperties);
    RecordType recordTypeEquivalentModified = new RecordType(registry, recordProperties);
    recordTypeEquivalentModified.addProperty("A", Number.class, false, null);
    assertFalse(recordTypeEquivalent.isEquivalentTo(recordTypeEquivalentModified));

    // Test frozen record type
    recordType.freeze();
    assertFalse(recordType.defineProperty("C", String.class, false, null));

    // Test greatest subtype
    RecordType recordTypeDifferent = new RecordType(registry, Maps.newHashMap());
    recordTypeDifferent.addProperty("D", Boolean.class, false, null);
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE),
        recordType.getGreatestSubtype(recordTypeDifferent));

    // Test least supertype
    RecordType recordTypeLeastSupertype = new RecordType(registry, Maps.newHashMap());
    recordTypeLeastSupertype.addProperty("E", String.class, false, null);
    JSType leastSupertype = recordType.getLeastSupertype(recordTypeLeastSupertype);
    assertEquals(recordType.getClass(), leastSupertype.getClass());

    // Test edge cases
    RecordType recordTypeWithEmptyProperties = new RecordType(registry, Maps.newHashMap());
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordTypeWithEmptyProperties.getImplicitPrototype());

    // Test null property type
    Map<String, RecordProperty> recordPropertiesWithNull =
        Maps.newHashMap();
    RecordProperty recordPropertyWithNull = new RecordProperty("A", null);
    recordPropertiesWithNull.put("A", recordPropertyWithNull);
    RecordType recordTypeWithNull = new RecordType(registry, recordPropertiesWithNull);
    assertEquals(registry.getNativeObjectType(JSTypeNative.ERROR_TYPE),
        recordTypeWithNull.getPropertyType("A"));

    // Test exception handling
    try {
      recordType.defineProperty("A", null, false, null);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }
}