import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import junit.framework.TestCase;

public class RecordTypeTest extends TestCase {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @SuppressWarnings("unchecked")
  private static <T> T newRecordType(Map<String, RecordProperty> properties) {
    return (T) new RecordType(registry, properties, true);
  }

  @SuppressWarnings("unchecked")
  private static <T> T newRecordType(Map<String, RecordProperty> properties, boolean declared) {
    return (T) new RecordType(registry, properties, declared);
  }

  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = Map.of(
        "a", new RecordProperty("a", IntType.INSTANCE),
        "b", new RecordProperty("b", StringType.INSTANCE)
    );

    RecordType recordType = newRecordType(properties);
    assertEquals("Expected record type with declared properties", 2, recordType.getOwnPropertyNames().size());
    assertEquals("Expected property 'a' of type IntType", IntType.INSTANCE, recordType.getPropertyType("a"));
    assertEquals("Expected property 'b' of type StringType", StringType.INSTANCE, recordType.getPropertyType("b"));
  }

  public void testRecordTypeCreationWithSyntheticProperties() {
    Map<String, RecordProperty> properties = Map.of(
        "a", new RecordProperty("a", IntType.INSTANCE),
        "b", new RecordProperty("b", StringType.INSTANCE)
    );

    RecordType recordType = newRecordType(properties, false);
    assertEquals("Expected record type with synthetic properties", 2, recordType.getOwnPropertyNames().size());
    assertTrue("Expected synthetic property 'a'", recordType.isSynthetic());
    assertTrue("Expected synthetic property 'b'", recordType.isSynthetic());
  }

  public void testRecordEquivalence() {
    Map<String, RecordProperty> properties = Map.of(
        "a", new RecordProperty("a", IntType.INSTANCE),
        "b", new RecordProperty("b", StringType.INSTANCE)
    );

    RecordType record1 = newRecordType(properties);
    RecordType record2 = newRecordType(properties);

    assertTrue("Expected records to be equivalent", record1.checkRecordEquivalenceHelper(record2, EquivalenceMethod.PROTOTYPE_CHAIN_ONLY));
    assertTrue("Expected records to be equivalent", record1.checkRecordEquivalenceHelper(record2, EquivalenceMethod.PROTOTYPE_CHAIN_AND_OWN_PROPERTIES));
  }

  public void testRecordEquivalenceWithDifferentProperties() {
    Map<String, RecordProperty> properties1 = Map.of(
        "a", new RecordProperty("a", IntType.INSTANCE),
        "b", new RecordProperty("b", StringType.INSTANCE)
    );

    Map<String, RecordProperty> properties2 = Map.of(
        "a", new RecordProperty("a", IntType.INSTANCE),
        "c", new RecordProperty("c", BooleanType.INSTANCE)
    );

    RecordType record1 = newRecordType(properties1);
    RecordType record2 = newRecordType(properties2);

    assertFalse("Expected records to be different", record1.checkRecordEquivalenceHelper(record2, EquivalenceMethod.PROTOTYPE_CHAIN_ONLY));
    assertFalse("Expected records to be different", record1.checkRecordEquivalenceHelper(record2, EquivalenceMethod.PROTOTYPE_CHAIN_AND_OWN_PROPERTIES));
  }

  public void testRecordSubclassing() {
    Map<String, RecordProperty> properties = Map.of(
        "a", new RecordProperty("a", IntType.INSTANCE),
        "b", new RecordProperty("b", StringType.INSTANCE)
    );

    RecordType baseRecord = newRecordType(properties);

    Map<String, RecordProperty> extendedProperties = Map.of(
        "c", new RecordProperty("c", StringType.INSTANCE)
    );

    RecordType extendedRecord = newRecordType(extendedProperties);

    UnionType type = (UnionType) baseRecord.getGreatestSubtypeHelper(extendedRecord);
    assertNotNull("Expected greatest subtype to be defined", type);
    assertEquals("Expected greatest subtype to include the extended record type", extendedRecord.getClass(), type.getAlternates().get(0).getClass());
  }

  public void testRecordIsSubtype() {
    Map<String, RecordProperty> propertiesA = Map.of(
        "a", new RecordProperty("a", IntType.INSTANCE),
        "b", new RecordProperty("b", StringType.INSTANCE)
    );

    Map<String, RecordProperty> propertiesB = Map.of(
        "a", new RecordProperty("a", IntType.INSTANCE),
        "c", new RecordProperty("c", StringType.INSTANCE)
    );

    RecordType recordA = newRecordType(propertiesA);
    RecordType recordB = newRecordType(propertiesB);

    assertTrue("Expected record A to be a subtype of record B", recordA.isSubtype(recordB));

    Map<String, RecordProperty> propertiesC = Map.of(
        "d", new RecordProperty("d", BooleanType.INSTANCE)
    );

    RecordType recordC = newRecordType(propertiesC);

    assertFalse("Expected record A not to be a subtype of record C", recordA.isSubtype(recordC));
  }

  public void testRecordIsSubtypeWithEmptyObjectType() {
    RecordType record = new RecordType(registry, Map.of(), false);

    assertTrue("Expected empty record type to be a subtype of ObjectType", record.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
  }

  public void testRecordIsSubtypeWithEmptyRecordType() {
    RecordType recordA = new RecordType(registry, Map.of(), false);
    RecordType recordB = new RecordType(registry, Map.of(), false);

    assertTrue("Expected empty record type to be a subtype of itself", recordA.isSubtype(recordB));
  }

  public void testRecordIsSubtypeWithIncompatiblePropertyTypes() {
    Map<String, RecordProperty> propertiesA = Map.of(
        "a", new RecordProperty("a", IntType.INSTANCE),
        "b", new RecordProperty("b", StringType.INSTANCE)
    );

    Map<String, RecordProperty> propertiesB = Map.of(
        "a", new RecordProperty("a", BooleanType.INSTANCE),
        "b", new RecordProperty("b", StringType.INSTANCE)
    );

    RecordType recordA = newRecordType(propertiesA);
    RecordType recordB = newRecordType(propertiesB);

    assertFalse("Expected record A not to be a subtype of record B", recordA.isSubtype(recordB));
  }

  public void testRecordIsSubtypeWithIncompatiblePropertyDeclarations() {
    Map<String, RecordProperty> propertiesA = Map.of(
        "a", new RecordProperty("a", IntType.INSTANCE, true),
        "b", new RecordProperty("b", StringType.INSTANCE, true)
    );

    Map<String, RecordProperty> propertiesB = Map.of(
        "a", new RecordProperty("a", IntType.INSTANCE, false),
        "b", new RecordProperty("b", StringType.INSTANCE, true)
    );

    RecordType recordA = newRecordType(propertiesA);
    RecordType recordB = newRecordType(propertiesB);

    assertFalse("Expected record A not to be a subtype of record B", recordA.isSubtype(recordB));
  }

  public void testRecordIsSubtypeWithIncompatiblePropertyOrder() {
    Map<String, RecordProperty> propertiesA = Map.of(
        "b", new RecordProperty("b", IntType.INSTANCE, true),
        "a", new RecordProperty("a", StringType.INSTANCE, true)
    );

    Map<String, RecordProperty> propertiesB = Map.of(
        "a", new RecordProperty("a", IntType.INSTANCE, true),
        "b", new RecordProperty("b", StringType.INSTANCE, true)
    );

    RecordType recordA = newRecordType(propertiesA);
    RecordType recordB = newRecordType(propertiesB);

    assertTrue("Expected records to be equivalent by value", recordA.equals(recordB));
    assertTrue("Expected record A to be a subtype of record B", recordA.isSubtype(recordB));
  }

  public void testRecordIsSubtypeWithIncompatiblePropertyNames() {
    Map<String, RecordProperty> propertiesA = Map.of(
        "a1", new RecordProperty("a1", IntType.INSTANCE, true),
        "b1", new RecordProperty("b1", StringType.INSTANCE, true)
    );

    Map<String, RecordProperty> propertiesB = Map.of(
        "a2", new RecordProperty("a2", IntType.INSTANCE, true),
        "b2", new RecordProperty("b2", StringType.INSTANCE, true)
    );

    RecordType recordA = newRecordType(propertiesA);
    RecordType recordB = newRecordType(propertiesB);

    assertFalse("Expected record A not to be a subtype of record B", recordA.isSubtype(recordB));
  }
}