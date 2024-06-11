import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeTest {
  @Test
  public void testRecordTypeConstruction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    RecordType recordType = new RecordType(registry, properties, true);

    // Basic functionality test
    assertEquals(recordType.getClass(), RecordType.class);
    assertEquals(recordType.properties.size(), 1);
    assertEquals(recordType.getImplicitPrototype().getClass(),
        JSTypeNative.ObjectType.class);

    // Edge case 1: empty record type
    RecordType emptyRecordType = new RecordType(registry, Maps.newHashMap(), false);
    assertTrue(emptyRecordType.isSynthetic());
    assertTrue(emptyRecordType.getImplicitPrototype()
        .isEquivalentTo(JSTypeNative.ObjectType.class));

    // Edge case 2: record type with incompatible property types
    properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", Integer.class));
    RecordType incompatibleRecordType = new RecordType(registry, properties, true);
    assertFalse(incompatibleRecordType.checkRecordEquivalenceHelper(
        recordType, false));

    // Error scenario: attempting to add a null RecordProperty
    properties = Maps.newHashMap();
    properties.put(null, new RecordProperty(null, String.class));
    try {
      new RecordType(registry, properties, true);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      assertEquals("RecordProperty associated with a property should not be null!",
          e.getMessage());
    }

    // Test record type subtyping
    JSType intType = registry.getIntType();
    JSType recordType1 = new RecordType(registry, Maps.newHashMap(), true)
        .defineProperty("x", intType, false, null);
    JSType recordType2 = new RecordType(registry, Maps.newHashMap(), true)
        .defineProperty("x", intType, false, null)
        .defineProperty("y", intType, false, null);
    RecordType recordType3 = new RecordType(registry, Maps.newHashMap(), true)
        .defineProperty("x", intType, false, null);

    assertTrue(recordType1.isSubtype(recordType3));
    assertTrue(recordType3.isSubtype(recordType1));
    assertFalse(recordType1.isSubtype(registry.getNativeObjectType(
        JSTypeNative.OBJECT_TYPE)));
    assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
        .isSubtype(recordType1));

    // Test record type equivalence
    RecordType recordType4 = new RecordType(registry, properties, true);
    RecordType recordType5 = new RecordType(registry, properties, true);
    assertTrue(recordType4.checkRecordEquivalenceHelper(recordType5, false));

    // Test record type consistency
    RecordType recordType6 = new RecordType(registry, Maps.newHashMap(), true)
        .defineProperty("x", intType, false, null)
        .defineProperty("x", String.class, false, null);
    assertFalse(recordType6.checkRecordEquivalenceHelper(recordType4, false));

    // Test record type with the same properties but different order
    RecordType recordType7 = new RecordTypeBuilder(registry)
        .addProperty("x", intType, false)
        .addProperty("y", intType, false)
        .addProperty("x", intType, false)
        .build();
    assertTrue(recordType4.checkRecordEquivalenceHelper(recordType7, false));

    // Test record type with duplicate properties
    RecordType recordType8 = new RecordTypeBuilder(registry)
        .addProperty("x", intType, false)
        .addProperty("x", intType, false)
        .build();
    assertFalse(recordType4.checkRecordEquivalenceHelper(recordType8, false));

    // Test record type getGreatestSubtypeHelper
    RecordType recordType9 = new RecordTypeBuilder(registry)
        .addProperty("x", intType, false)
        .addProperty("y", String.class, false)
        .build();
    RecordType recordType10 = new RecordTypeBuilder(registry)
        .addProperty("y", String.class, false)
        .addProperty("x", intType, false)
        .build();
    RecordType recordType11 = new RecordTypeBuilder(registry)
        .addProperty("x", intType, false)
        .addProperty("y", String.class, false)
        .build();
    JSType recordType12 = recordType4.getGreatestSubtypeHelper(recordType11);
    assertTrue(recordType11.isSubtype(recordType12));
    assertTrue(recordType12.isSubtype(recordType11));
    assertEquals(recordType12.getClass(), RecordType.class);
  }
}