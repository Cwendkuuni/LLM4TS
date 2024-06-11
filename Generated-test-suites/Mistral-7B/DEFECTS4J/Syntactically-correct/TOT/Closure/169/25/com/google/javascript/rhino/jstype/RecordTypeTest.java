import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testRecordTypeCreation() {
    // Test creating a record type with known properties.
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("x", new RecordProperty("x", registry.getIntType()));
    recordProperties.put("y", new RecordProperty("y", registry.getStringType()));
    RecordType recordType = new RecordType(registry, recordProperties, true);

    // Test accessing properties.
    assertEquals(registry.getIntType(), recordType.getPropertyType("x"));
    assertEquals(registry.getStringType(), recordType.getPropertyType("y"));

    // Test creating a record type with inferred properties.
    Map<String, RecordProperty> inferredProperties = Maps.newHashMap();
    inferredProperties.put("z", new RecordProperty("z", registry.getUnknownType()));
    RecordType inferredRecordType = new RecordType(registry, inferredProperties, false);
    assertNull(inferredRecordType.getPropertyType("z"));

    // Test setting and getting a property value.
    JSType value = registry.getIntType().getInstance(42);
    inferredRecordType.defineProperty("z", value, false, Node.NULL);
    assertEquals(value, inferredRecordType.getProperty("z"));

    // Test setting a property value to null.
    inferredRecordType.defineProperty("z", Node.NULL, false, Node.NULL);
    assertNull(inferredRecordType.getProperty("z"));

    // Test checking if a record type has a property.
    assertFalse(recordType.hasProperty("z"));
    assertTrue(recordType.hasProperty("x"));

    // Test checking if two record types are equivalent.
    Map<String, RecordProperty> equivalentRecordProperties =
        Maps.newHashMap(recordProperties);
    RecordType equivalentRecordType = new RecordType(registry, equivalentRecordProperties, true);
    assertTrue(recordType.isEquivalentTo(equivalentRecordType));

    // Test checking if a record type is a subtype of another record type.
    RecordType subtypeRecordType = new RecordType(registry, recordProperties, true);
    RecordType supertypeRecordType = new RecordType(registry, Maps.newHashMap(), true);
    supertypeRecordType.defineProperty("x", registry.getIntType(), false, Node.NULL);
    supertypeRecordType.defineProperty("y", registry.getStringType(), false, Node.NULL);
    assertTrue(subtypeRecordType.isSubtype(supertypeRecordType));

    // Test checking if a record type is a subtype of a non-record type.
    JSType nonRecordType = registry.getNativeType(JSTypeNative.OBJECT_TYPE);
    assertFalse(subtypeRecordType.isSubtype(nonRecordType));

    // Test checking if a record type is a subtype of itself.
    assertTrue(subtypeRecordType.isSubtype(subtypeRecordType));

    // Test checking if a record type is a subtype of a record type with
    // additional properties.
    Map<String, RecordProperty> additionalProperties =
        Maps.newHashMap(recordProperties);
    additionalProperties.put("w", new RecordProperty("w", registry.getUnknownType()));
    RecordType additionalRecordType = new RecordType(registry, additionalProperties, true);
    assertFalse(subtypeRecordType.isSubtype(additionalRecordType));

    // Test checking if a record type is a subtype of a record type with
    // different property types.
    Map<String, RecordProperty> differentProperties =
        Maps.newHashMap(recordProperties);
    differentProperties.put("x", new RecordProperty("x", registry.getStringType()));
    RecordType differentRecordType = new RecordType(registry, differentProperties, true);
    assertFalse(subtypeRecordType.isSubtype(differentRecordType));

    // Test checking if a record type is a subtype of a record type with
    // inferred properties.
    RecordType inferredSubtypeRecordType = new RecordType(registry, inferredProperties, true);
    assertFalse(subtypeRecordType.isSubtype(inferredSubtypeRecordType));

    // Test checking if a record type is a subtype of a record type with
    // missing properties.
    Map<String, RecordProperty> missingProperties = Maps.newHashMap();
    missingProperties.put("x", new RecordProperty("x", registry.getIntType()));
    RecordType missingRecordType = new RecordType(registry, missingProperties, true);
    assertFalse(subtypeRecordType.isSubtype(missingRecordType));

    // Test checking if a record type is a subtype of a record type with
    // properties of different names.
    Map<String, RecordProperty> differentNamesProperties =
        Maps.newHashMap(recordProperties);
    differentNamesProperties.put("w", new RecordProperty("w", registry.getIntType()));
    RecordType differentNamesRecordType = new RecordType(registry, differentNamesProperties, true);
    assertFalse(subtypeRecordType.isSubtype(differentNamesRecordType));

    // Test checking if a record type with inferred properties is a subtype of
    // a record type with known properties.
    RecordType knownSubtypeRecordType = new RecordType(registry, recordProperties, true);
    assertFalse(inferredRecordType.isSubtype(knownSubtypeRecordType));

    // Test checking if a record type with known properties is a subtype of a record type
    // with inferred properties.
    assertFalse(knownSubtypeRecordType.isSubtype(inferredRecordType));

    // Test checking if a record type is a subtype of itself after freezing.
    recordType.freeze();
    assertTrue(recordType.isSubtype(recordType));

    // Test checking if a record type is a subtype of a record type with
    // properties of the same name but different types.
    Map<String, RecordProperty> differentTypesProperties =
        Maps.newHashMap(recordProperties);
    differentTypesProperties.put("x", new RecordProperty("x", registry.getBooleanType()));
    RecordType differentTypesRecordType = new RecordType(registry, differentTypesProperties, true);
    assertFalse(recordType.isSubtype(differentTypesRecordType));

    // Test checking if a record type is a subtype of a record type with
    // properties of the same name but different declarations.
    Map<String, RecordProperty> differentDeclarationsProperties =
        Maps.newHashMap(recordProperties);
    differentDeclarationsProperties.put("x", new RecordProperty("x", registry.getUnknownType()));
    RecordType differentDeclarationsRecordType = new RecordType(registry, differentDeclarationsProperties, true);
    assertFalse(recordType.isSubtype(differentDeclarationsRecordType));

    // Test checking if a record type is a subtype of a record type with
    // properties of the same name but different declarations and types.
    Map<String, RecordProperty> differentDeclarationsAndTypesProperties =
        Maps.newHashMap(recordProperties);
    differentDeclarationsAndTypesProperties.put("x", new RecordProperty("x", registry.getBooleanType()));
    RecordType differentDeclarationsAndTypesRecordType = new RecordType(registry, differentDeclarationsAndTypesProperties, true);
    assertFalse(recordType.isSubtype(differentDeclarationsAndTypesRecordType));

    // Test checking if a record type is a subtype of a record type with
    // properties of the same name but different declarations and inferred types.
    Map<String, RecordProperty> differentDeclarationsAndTypesInferredProperties =
        Maps.newHashMap(recordProperties);
    differentDeclarationsAndTypesInferredProperties.put("x", new RecordProperty("x", registry.getUnknownType()));
    RecordType differentDeclarationsAndTypesInferredRecordType = new RecordType(registry, differentDeclarationsAndTypesInferredProperties, false);
    assertFalse(recordType.isSubtype(differentDeclarationsAndTypesInferredRecordType));

    // Test checking if a record type is a subtype of a record type with
    // properties of the same name but different declarations, inferred types,
    // and different names.
    Map<String, RecordProperty> differentDeclarationsAndTypesAndNamesProperties =
        Maps.newHashMap(recordProperties);
    differentDeclarationsAndTypesAndNamesProperties.put("w", new RecordProperty("w", registry.getBooleanType()));
    RecordType differentDeclarationsAndTypesAndNamesRecordType = new RecordType(registry, differentDeclarationsAndTypesAndNamesProperties, false);
    assertFalse(recordType.isSubtype(differentDeclarationsAndTypesAndNamesRecordType));

    // Test checking if a record type is a subtype of a record type with
    // properties of different names but the same type.
    Map<String, RecordProperty> differentNamesAndSameTypesProperties =
        Maps.newHashMap(recordProperties);
    differentNamesAndSameTypesProperties.put("w", new RecordProperty("w", registry.getIntType()));
    RecordType differentNamesAndSameTypesRecordType = new RecordType(registry, differentNamesAndSameTypesProperties, true);
    assertTrue(recordType.isSubtype(differentNamesAndSameTypesRecordType));

    // Test checking if a record type is a subtype of a record type with
    // properties of different names and different types.
    Map<String, RecordProperty> differentNamesAndDifferentTypesProperties =
        Maps.newHashMap(recordProperties);
    differentNamesAndDifferentTypesProperties.put("w", new RecordProperty("w", registry.getBooleanType()));
    RecordType differentNamesAndDifferentTypesRecordType = new RecordType(registry, differentNamesAndDifferentTypesProperties, true);
    assertFalse(recordType.isSubtype(differentNamesAndDifferentTypesRecordType));

    // Test checking if a record type is a subtype of a record type with
    // properties of the same name but different inferred types.
    Map<String, RecordProperty> sameNamesDifferentInferredTypesProperties =
        Maps.newHashMap(recordProperties);
    sameNamesDifferentInferredTypesProperties.put("x", new RecordProperty("x", registry.getBooleanType()));
    RecordType sameNamesDifferentInferredTypesRecordType = new RecordType(registry, sameNamesDifferentInferredTypesProperties, false);
    assertFalse(recordType.isSubtype(sameNamesDifferentInferredTypesRecordType));

    // Test checking if a record type is a subtype of a record type with
    // properties of the same name but different inferred types and different names.
    Map<String, RecordProperty> sameNamesDifferentInferredTypesAndNamesProperties =
        Maps.newHashMap(recordProperties);
    sameNamesDifferentInferredTypesAndNamesProperties.put("w", new RecordProperty("w", registry.getBooleanType()));
    RecordType sameNamesDifferentInferredTypesAndNamesRecordType = new RecordType(registry, sameNamesDifferentInferredTypesAndNamesProperties, false);
    assertFalse(recordType.isSubtype(sameNamesDifferentInferredTypesAndNamesRecordType));

    // Test checking if a record type is a subtype of a record type with
    // properties of the same name but different types and different inferred types.
    Map<String, RecordProperty> sameNamesDifferentTypesAndInferredTypesProperties =
        Maps.newHashMap(recordProperties);
    sameNamesDifferentTypesAndInferredTypesProperties.put("x", new RecordProperty("x", registry.getBooleanType()));
    RecordType sameNamesDifferentTypesAndInferredTypesRecordType = new RecordType(registry, sameNamesDifferentTypesAndInferredTypesProperties, false);
    assertFalse(recordType.isSubtype(sameNamesDifferentTypesAndInferredTypesRecordType));

    // Test checking if a record type is a subtype of a record type with
    // properties of different names but the same types and inferred types.
    Map<String, RecordProperty> differentNamesSameTypesAndInferredTypesProperties =
        Maps.newHashMap(recordProperties);
    differentNamesSameTypesAndInferredTypesProperties.put("w", new RecordProperty("w", registry.getIntType()));
    RecordType differentNamesSameTypesAndInferredTypesRecordType = new RecordType(registry, differentNamesSameTypesAndInferredTypesProperties, false);
    assertTrue(recordType.isSubtype(differentNamesSameTypesAndInferredTypesRecordType));

    // Test checking if a record type is a subtype of a record type with
    // properties of different names, different types, and different inferred types.
    Map<String, RecordProperty> differentNamesDifferentTypesAndInferredTypesProperties =
        Maps.newHashMap(recordProperties);
    differentNamesDifferentTypesAndInferredTypesProperties.put("w", new RecordProperty("w", registry.getBooleanType()));
    RecordType differentNamesDifferentTypesAndInferredTypesRecordType = new RecordType(registry, differentNamesDifferentTypesAndInferredTypesProperties, false);
    assertFalse(recordType.isSubtype(differentNamesDifferentTypesAndInferredTypesRecordType));

    // Test checking if a record type is a subtype of a record type with
    // properties of the same name and type but different inferred types.
    Map<String, RecordProperty> sameNamesSameTypesDifferentInferredTypesProperties =
        Maps.newHashMap(recordProperties);
    sameNamesSameTypesDifferentInferredTypesProperties.put("x", new RecordProperty("x", registry.getBooleanType()));
    RecordType sameNamesSameTypesDifferentInferredTypesRecordType = new RecordType(registry, sameNamesSameTypesDifferentInferredTypesProperties, false);
    assertFalse(recordType.isSubtype(sameNamesSameTypesDifferentInferredTypesRecordType));

    // Test checking if a record type is a subtype of a record type with
    // properties of the same name, same type, and same inferred types.
    RecordType sameNamesSameTypesSameInferredTypesRecordType = new RecordType(registry, recordProperties, false);
    assertTrue(recordType.isSubtype(sameNamesSameTypesSameInferredTypesRecordType));
  }
}