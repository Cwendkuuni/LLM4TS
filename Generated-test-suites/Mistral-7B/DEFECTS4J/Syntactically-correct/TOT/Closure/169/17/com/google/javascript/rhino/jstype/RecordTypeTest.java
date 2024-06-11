import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

public class RecordTypeTest {

  private JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", intType()));
    properties.put("y", new RecordProperty("y", stringType()));

    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals("x and y are properties of the record type", 2, recordType.getProperties().size());
    assertEquals("Type of property 'x' is int", intType(), recordType.getPropertyType("x"));
    assertEquals("Type of property 'y' is string", stringType(), recordType.getPropertyType("y"));
  }

  @Test
  public void testRecordTypeAssignment() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("x", new RecordProperty("x", intType()));
    recordProperties.put("y", new RecordProperty("y", stringType()));

    RecordType recordType = new RecordType(registry, recordProperties, true);
    RecordType anotherRecordType = new RecordType(registry, recordProperties, false);

    Object record = new Object() {{
      put("x", 1);
      put("y", "hello");
    }};

    RecordType frozenRecordType = new RecordType(registry, recordProperties, true);

    assertEquals("Record type is assignable to itself", recordType, recordType);
    assertEquals("Record type is assignable to another record type with the same properties", recordType, anotherRecordType);
    assertEquals("Record type is not assignable to a different type", recordType, booleanType());
    assertEquals("Frozen record type is not assignable to a mutable record type", frozenRecordType, recordType);
    assertEquals("Mutable record type is assignable to a frozen record type", recordType, frozenRecordType);
    assertEquals("Record is assignable to its record type", record, recordType.valueOf(registry, record));
  }

  @Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("x", new RecordProperty("x", intType()));
    recordProperties1.put("y", new RecordProperty("y", stringType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("x", new RecordProperty("x", intType()));
    recordProperties2.put("y", new RecordProperty("y", stringType()));
    recordProperties2.put("z", new RecordProperty("z", booleanType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    // Record types with different properties are not equivalent
    assertFalse("Record types with different properties are not equivalent", recordType1.isEquivalentTo(recordType2));

    // Record types with the same properties and same types are equivalent
    RecordType recordType3 = new RecordType(registry, recordProperties1, true);
    assertTrue("Record types with the same properties and same types are equivalent", recordType1.isEquivalentTo(recordType3));

    // Record types with the same properties but different types are not equivalent
    recordProperties1.put("y", new RecordProperty("y", numberType()));
    RecordType recordType4 = new RecordType(registry, recordProperties1, true);
    assertFalse("Record types with the same properties but different types are not equivalent", recordType1.isEquivalentTo(recordType4));

    // Records with the same properties and types but different values are not equivalent
    recordType1.setProperty("x", doubleValue(1.5));
    recordType4.setProperty("x", intValue(2));
    assertFalse("Records with the same properties and types but different values are not equivalent", recordType1.isEquivalentTo(recordType4));

    // Records with the same properties and same values are equivalent
    recordType1.setProperty("x", intValue(1));
    recordType4.setProperty("x", intValue(1));
    assertTrue("Records with the same properties and same values are equivalent", recordType1.isEquivalentTo(recordType4));
  }

  @Test
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("x", new RecordProperty("x", intType()));
    recordProperties1.put("y", new RecordProperty("y", stringType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("x", new RecordProperty("x", intType()));
    recordProperties2.put("y", new RecordProperty("y", stringType()));
    recordProperties2.put("z", new RecordProperty("z", booleanType()));

    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    // Record type is a subtype of itself
    assertTrue("Record type is a subtype of itself", recordType1.isSubtype(recordType1));

    // Record type is a subtype of another record type with the same properties
    assertTrue("Record type is a subtype of another record type with the same properties", recordType1.isSubtype(recordType2));

    // Record type is not a subtype of a different type
    assertFalse("Record type is not a subtype of a different type", recordType1.isSubtype(booleanType()));

    // Record type with fewer properties is not a subtype of a record type with more properties
    Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
    recordProperties3.put("x", new RecordProperty("x", intType()));
    RecordType recordType3 = new RecordType(registry, recordProperties3, true);
    assertFalse("Record type with fewer properties is not a subtype of a record type with more properties", recordType1.isSubtype(recordType3));

    // Record type with different property types is not a subtype of another record type
    recordProperties2.put("z", new RecordProperty("z", intType()));
    RecordType recordType4 = new RecordType(registry, recordProperties2, true);
    assertFalse("Record type with different property types is not a subtype of another record type", recordType1.isSubtype(recordType4));
  }

  @Test
  public void testRecordTypeFreezing() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("x", new RecordProperty("x", intType()));
    recordProperties.put("y", new RecordProperty("y", stringType()));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    // Record type can be frozen
    recordType.freeze();

    // Record type is immutable after freezing
    try {
      recordType.setProperty("x", intValue(42));
      fail("Failed to throw an IllegalStateException when freezing a record type");
    } catch (IllegalStateException e) {
      // Expected exception
    }

    // Frozen record type cannot be unfrozen
    assertFalse("Frozen record type cannot be unfrozen", recordType.canBeFrozen());

    // A new record type with the same properties can be created
    RecordType newRecordType = new RecordType(registry, recordProperties, true);
    assertTrue("A new record type with the same properties can be created", newRecordType.isSubtype(recordType));
  }

  @Test
  public void testRecordTypeGetGreatestSubtype() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("x", new RecordProperty("x", intType()));
    recordProperties1.put("y", new RecordProperty("y", stringType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("x", new RecordProperty("x", intType()));
    recordProperties2.put("y", new RecordProperty("y", intType()));

    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    // Record type is a subtype of itself
    assertEquals("Record type is a subtype of itself", recordType1, recordType1.getGreatestSubtype(recordType1));

    // Record type is a subtype of another record type with the same properties
    assertEquals("Record type is a subtype of another record type with the same properties", recordType1, recordType1.getGreatestSubtype(recordType2));

    // Record type is a subtype of a record type with fewer properties but compatible types
    Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
    recordProperties3.put("x", new RecordProperty("x", intType()));
    RecordType recordType3 = new RecordType(registry, recordProperties3, true);
    assertEquals("Record type is a subtype of a record type with fewer properties but compatible types", recordType1, recordType1.getGreatestSubtype(recordType3));

    // Record type is not a subtype of a record type with incompatible property types
    Map<String, RecordProperty> recordProperties4 = Maps.newHashMap();
    recordProperties4.put("x", new RecordProperty("x", intType()));
    recordProperties4.put("y", new RecordProperty("y", booleanType()));
    RecordType recordType4 = new RecordType(registry, recordProperties4, true);
    assertEquals("Record type is not a subtype of a record type with incompatible property types", registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), recordType1.getGreatestSubtype(recordType4));

    // Record type is the least upper bound of two record types with the same properties but different values
    RecordType recordType5 = new RecordType(registry, recordProperties1, true);
    recordType5.setProperty("x", doubleValue(1.5));
    RecordType recordType6 = new RecordType(registry, recordProperties1, true);
    recordType6.setProperty("x", intValue(2));
    assertEquals("Record type is the least upper bound of two record types with the same properties but different values", recordType1, recordType1.getGreatestSubtype(recordType5));
    assertEquals("Record type is the least upper bound of two record types with the same properties but different values", recordType1, recordType1.getGreatestSubtype(recordType6));

    // Record type is the least upper bound of two record types with the same properties but different values and one frozen
    recordType5.freeze();
    assertEquals("Record type is the least upper bound of two record types with the same properties but different values and one frozen", recordType1, recordType1.getGreatestSubtype(recordType5));

    // Record type is the least upper bound of two record types with different properties but compatible types
    Map<String, RecordProperty> recordProperties5 = Maps.newHashMap();
    recordProperties5.put("x", new RecordProperty("x", intType()));
    recordProperties5.put("z", new RecordProperty("z", intType()));
    RecordType recordType5_1 = new RecordType(registry, recordProperties5, true);
    assertEquals("Record type is the least upper bound of two record types with different properties but compatible types", recordType1, recordType1.getGreatestSubtype(recordType5_1));
  }
}