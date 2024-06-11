import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  private JSTypeRegistry registry;

  @Before
  public void setup() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);
    assertNotNull(recordType);
    assertEquals("RecordType", recordType.getClassName());
    assertTrue(recordType.isRecordType());

    RecordType recordTypeSynthetic = new RecordType(registry, properties, false);
    assertNotNull(recordTypeSynthetic);
    assertEquals("RecordType", recordTypeSynthetic.getClassName());
    assertTrue(recordTypeSynthetic.isSynthetic());
  }

  @Test
  public void testRecordTypeIsFrozen() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);
    recordType.freeze();

    assertFalse(recordType.defineProperty("newProperty", registry.getStringType(), false, null));
  }

  @Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties, true);
    RecordType recordType2 = new RecordType(registry, properties, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.STRICT));
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.LAX));

    RecordTypeBuilder recordType3Builder = new RecordTypeBuilder(registry);
    recordType3Builder.setSynthesized(true);
    recordType3Builder.addProperty("name", registry.getStringType(), null);
    recordType3Builder.addProperty("age", registry.getNumberType(), null);

    RecordType recordType3 = recordType3Builder.build();

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.STRICT));
  }

  @Test
  public void testRecordTypeGreatestSubtype() {
    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, recordProperties, true);

    Map<String, RecordProperty> recordProperties2 = new HashMap<>();
    recordProperties2.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties2.put("age", new RecordProperty("age", registry.getNumberType()));
    recordProperties2.put("city", new RecordProperty("city", registry.getStringType()));

    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

    assertTrue(greatestSubtype instanceof RecordType);
    assertEquals(recordType2.getClass().getName(), greatestSubtype.getClass().getName());
  }

  @Test
  public void testRecordTypeIsSubtype() {
    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", registry.getStringType()));
    recordProperties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, recordProperties, true);
    RecordType recordType2 = new RecordType(registry, recordProperties, true);

    assertTrue(recordType1.isSubtype(recordType2));
    assertTrue(recordType2.isSubtype(recordType1));

    RecordType recordType3 = new RecordType(registry, recordProperties, false);
    assertFalse(recordType1.isSubtype(recordType3));
    assertFalse(recordType3.isSubtype(recordType1));
  }
}