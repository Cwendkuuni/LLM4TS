import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeTest {
  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new java.util.HashMap<>();
    RecordProperty prop1 = new RecordProperty("prop1", JSType.STRING_TYPE);
    RecordProperty prop2 = new RecordProperty("prop2", JSType.NUMBER_TYPE);
    properties.put("prop1", prop1);
    properties.put("prop2", prop2);

    RecordType recordType = new RecordType(registry, properties, true);
    assertEquals(true, recordType.isDeclared());

    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    RecordType recordTypeBuilderResult = recordTypeBuilder.build();
    assertEquals(false, recordTypeBuilderResult.isDeclared());

    RecordType recordTypeFrozen = new RecordType(registry, properties, true);
    recordTypeFrozen.freeze();
    try {
      recordTypeFrozen.defineProperty("prop1", JSType.STRING_TYPE, false, null);
      fail("Expected an exception");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testRecordEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = new java.util.HashMap<>();
    Map<String, RecordProperty> properties2 = new java.util.HashMap<>();

    RecordProperty prop1 = new RecordProperty("prop1", JSType.STRING_TYPE);
    RecordProperty prop2 = new RecordProperty("prop2", JSType.NUMBER_TYPE);

    properties1.put("prop1", prop1);
    properties1.put("prop2", prop2);

    properties2.put("prop1", prop1);
    properties2.put("prop2", prop2);

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, RecordType.EQ_BY_IDENTITY));
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, RecordType.EQ_BY_VALUE));

    properties2.put("prop2", new RecordProperty("prop2", JSType.BOOLEAN_TYPE));
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, RecordType.EQ_BY_IDENTITY));
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, RecordType.EQ_BY_VALUE));
  }

  @Test
  public void testRecordTypeSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = new java.util.HashMap<>();
    Map<String, RecordProperty> properties2 = new java.util.HashMap<>();

    RecordProperty prop1 = new RecordProperty("prop1", JSType.STRING_TYPE);
    RecordProperty prop2 = new RecordProperty("prop2", JSType.NUMBER_TYPE);

    properties1.put("prop1", prop1);
    properties1.put("prop2", prop2);

    properties2.put("prop1", prop1);
    properties2.put("prop2", new RecordProperty("prop2", JSType.BOOLEAN_TYPE));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertFalse(recordType1.isSubtype(recordType2));

    RecordType recordType2Frozen = new RecordType(registry, properties2, true);
    recordType2Frozen.freeze();

    RecordType recordType3 = new RecordType(registry, properties1, false);
    assertTrue(recordType1.isSubtype(recordType3));

    RecordType recordType3Frozen = new RecordType(registry, properties1, false);
    recordType3Frozen.freeze();

    assertTrue(recordType1.isSubtype(recordType3Frozen));
  }
}