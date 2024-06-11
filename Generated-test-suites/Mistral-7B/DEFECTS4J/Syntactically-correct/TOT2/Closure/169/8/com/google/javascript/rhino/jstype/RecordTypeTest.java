import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);
    Assert.assertEquals("RecordType", recordType.getClassName());
    Assert.assertEquals(2, recordType.getPropertyCount());

    // Basic functionality test
    RecordType testRecordType = new RecordType(registry, recordProperties, false);
    Assert.assertTrue(recordType.isSubtype(testRecordType));
    testRecordType = new RecordType(registry, recordProperties, true);
    Assert.assertTrue(recordType.isSubtype(testRecordType));
  }

  @Test
  public void testRecordTypePropertyGetters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);
    Assert.assertEquals("RecordType", recordType.getClassName());
    Assert.assertEquals(2, recordType.getPropertyCount());

    // Get property types
    Assert.assertEquals(String.class, recordType.getPropertyType("name"));
    Assert.assertEquals(Integer.class, recordType.getPropertyType("age"));

    // Get property nodes
    Assert.assertNotNull(recordType.getPropertyNode("name"));
    Assert.assertNotNull(recordType.getPropertyNode("age"));
  }

  @Test
  public void testRecordTypeIsFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);
    recordType.defineProperty("address", String.class, true, null);
    Assert.assertFalse(recordType.isFrozen());

    recordType.freeze();
    Assert.assertTrue(recordType.isFrozen());

    try {
      recordType.defineProperty("address", String.class, true, null);
      Assert.fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("name", new RecordProperty("name", String.class));
    recordPropertiesA.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("name", new RecordProperty("name", String.class));
    recordPropertiesB.put("age", new RecordProperty("age", Integer.class));
    recordPropertiesB.put("address", new RecordProperty("address", String.class));

    RecordType recordTypeA = new RecordType(registry, recordPropertiesA, true);
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB, true);

    Assert.assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));

    recordTypeB.removeProperty("address");
    Assert.assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));
  }

  @Test
  public void testRecordTypeSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("name", new RecordProperty("name", String.class));
    recordPropertiesA.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("name", new RecordProperty("name", String.class));
    recordPropertiesB.put("age", new RecordProperty("age", Integer.class));
    recordPropertiesB.put("address", new RecordProperty("address", String.class));

    RecordType recordTypeA = new RecordType(registry, recordPropertiesA, true);
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB, true);

    Assert.assertFalse(recordTypeA.isSubtype(recordTypeB));

    // Test with a subtype that shares all properties
    Map<String, RecordProperty> recordPropertiesC = Maps.newHashMap();
    recordPropertiesC.putAll(recordPropertiesA);
    RecordType recordTypeC = new RecordType(registry, recordPropertiesC, true);
    Assert.assertTrue(recordTypeA.isSubtype(recordTypeC));
    Assert.assertTrue(recordTypeC.isSubtype(recordTypeA));

    // Test with a subtype that has additional properties
    Map<String, RecordProperty> recordPropertiesD = Maps.newHashMap();
    recordPropertiesD.putAll(recordPropertiesA);
    recordPropertiesD.put("address", new RecordProperty("address", String.class));
    RecordType recordTypeD = new RecordType(registry, recordPropertiesD, true);
    Assert.assertFalse(recordTypeA.isSubtype(recordTypeD));
    Assert.assertTrue(recordTypeD.isSubtype(recordTypeA));

    // Test with a subtype that has identical properties, but with different types
    Map<String, RecordProperty> recordPropertiesE = Maps.newHashMap();
    recordPropertiesE.put("name", new RecordProperty("name", Integer.class));
    recordPropertiesE.put("age", new RecordProperty("age", Long.class));
    RecordType recordTypeE = new RecordType(registry, recordPropertiesE, true);
    Assert.assertFalse(recordTypeA.isSubtype(recordTypeE));
    Assert.assertFalse(recordTypeE.isSubtype(recordTypeA));
  }

  @Test
  public void testRecordTypeGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("name", new RecordProperty("name", String.class));
    recordPropertiesA.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("name", new RecordProperty("name", String.class));
    recordPropertiesB.put("age", new RecordProperty("age", Integer.class));
    recordPropertiesB.put("address", new RecordProperty("address", String.class));

    RecordType recordTypeA = new RecordType(registry, recordPropertiesA, true);
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB, true);

    RecordType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    Assert.assertSame(recordTypeB, greatestSubtype);

    // Test with different types
    RecordType recordTypeC = new RecordType(registry, recordPropertiesA, true);
    RecordType recordTypeD = new RecordType(registry, recordPropertiesB, true);
    greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeD);
    Assert.assertEquals(recordTypeA.getClass(), greatestSubtype.getClass());

    // Test with empty record type
    RecordType emptyRecordType = new RecordType(registry, Maps.newTreeMap(), true);
    greatestSubtype = recordTypeA.getGreatestSubtypeHelper(emptyRecordType);
    Assert.assertEquals(recordTypeA.getClass(), greatestSubtype.getClass());

    // Test with non-record type
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    greatestSubtype = recordTypeA.getGreatestSubtypeHelper(objectType);
    Assert.assertEquals(objectType.getClass(), greatestSubtype.getClass());

    // Test with undefined type
    UnionType undefinedType = new UnionType(registry, Arrays.asList(registry.getUndefinedType()));
    greatestSubtype = recordTypeA.getGreatestSubtypeHelper(undefinedType);
    Assert.assertEquals(registry.getUndefinedType().getClass(), greatestSubtype.getClass());
  }
}