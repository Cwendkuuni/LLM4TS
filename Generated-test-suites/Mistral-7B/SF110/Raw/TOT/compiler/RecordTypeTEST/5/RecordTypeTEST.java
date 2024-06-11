import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.stringType()));
    properties.put("age", new RecordProperty("age", registry.numberType()));

    RecordType recordType = new RecordType(registry, properties);
    assertEquals(recordType.getClass(), RecordType.class);

    // Test getters
    assertEquals(recordType.getDeclared(), true);
    assertFalse(recordType.isFrozen());

    Set<String> keys = recordType.getOwnPropertyNames();
    assertEquals(keys.size(), 2);
    assertTrue(keys.contains("name"));
    assertTrue(keys.contains("age"));

    // Test methods
    JSType nameType = recordType.getPropertyType("name");
    assertEquals(nameType.getClass(), registry.stringType().getClass());

    recordType = new RecordType(registry, properties, false);
    assertEquals(recordType.getClass(), RecordType.class);
    assertEquals(recordType.getDeclared(), false);
  }

  @Test
  public void testRecordEquivalence() {
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("name", new RecordProperty("name", registry.stringType()));
    properties1.put("age", new RecordProperty("age", registry.numberType()));

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("name", new RecordProperty("name", registry.stringType()));
    properties2.put("age", new RecordProperty("age", registry.numberType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2,
        EquivalenceMethod.IDENTICAL));

    // Test non-identical equivalence
    properties2.put("address", new RecordProperty("address", registry.stringType()));
    RecordType recordType3 = new RecordType(registry, properties2);
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3,
        EquivalenceMethod.IDENTICAL));
  }

  @Test
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.stringType()));
    properties.put("age", new RecordProperty("age", registry.numberType()));

    RecordType subType = new RecordType(registry, properties);
    RecordType parentType =
        new RecordType(registry, Arrays.asList("name", "age"), true);

    assertTrue(parentType.isSubtype(subType));
  }

  @Test
  public void testRecordTypeFrozen() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.stringType()));
    properties.put("age", new RecordProperty("age", registry.numberType()));

    RecordType recordType = new RecordType(registry, properties);

    recordType.freeze();

    assertFalse(recordType.defineProperty("newProperty", registry.stringType(),
        false, null));
  }
}