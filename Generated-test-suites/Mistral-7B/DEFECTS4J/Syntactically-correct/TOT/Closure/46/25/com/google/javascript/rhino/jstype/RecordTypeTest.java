import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
    RecordProperty prop2 = new RecordProperty("prop2", registry.getNumberType());
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", prop1);
    properties.put("prop2", prop2);

    RecordType recordType = new RecordType(registry, properties);

    assertNotNull(recordType);
    assertEquals("RecordType", recordType.getClassName());
    assertEquals(2, recordType.getProperties().size());
    assertTrue(recordType.isRecordType());
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
    RecordProperty prop2 = new RecordProperty("prop2", registry.getNumberType());

    Map<String, RecordProperty> props1 = Maps.newHashMap();
    props1.put("prop1", prop1);
    Map<String, RecordProperty> props2 = Maps.newHashMap();
    props2.put("prop1", prop1);
    props2.put("prop2", prop2);

    RecordType recordType1 = new RecordType(registry, props1);
    RecordType recordType2 = new RecordType(registry, props2);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testRecordTypeNonEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
    RecordProperty prop2 = new RecordProperty("prop2", registry.getNumberType());
    RecordProperty prop3 = new RecordProperty("prop3", registry.getNumberType());

    Map<String, RecordProperty> props1 = Maps.newHashMap();
    props1.put("prop1", prop1);
    Map<String, RecordProperty> props2 = Maps.newHashMap();
    props2.put("prop1", prop1);
    props2.put("prop2", prop2);
    Map<String, RecordProperty> props3 = Maps.newHashMap();
    props3.put("prop1", prop1);
    props3.put("prop3", prop3);

    RecordType recordType1 = new RecordType(registry, props1);
    RecordType recordType2 = new RecordType(registry, props2);
    RecordType recordType3 = new RecordType(registry, props3);

    assertFalse(recordType1.isEquivalentTo(recordType2));
    assertFalse(recordType1.isEquivalentTo(recordType3));
  }

  @Test
  public void testRecordTypeSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
    RecordProperty prop2 = new RecordProperty("prop2", registry.getNumberType());
    Map<String, RecordProperty> props1 = Maps.newHashMap();
    props1.put("prop1", prop1);
    Map<String, RecordProperty> props2 = Maps.newHashMap();
    props2.put("prop1", prop1);
    props2.put("prop2", prop2);

    RecordType recordType1 = new RecordType(registry, props1);
    RecordType recordType2 = new RecordType(registry, props2);

    JSType subtype = recordType1.getLeastSupertype(recordType2);
    assertTrue(recordType1.isSubtype(subtype));
    assertTrue(recordType2.isSubtype(subtype));
  }

  @Test
  public void testRecordTypeGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
    RecordProperty prop2 = new RecordProperty("prop2", registry.getNumberType());
    RecordProperty prop3 = new RecordProperty("prop3", registry.getBooleanType());

    Map<String, RecordProperty> props1 = Maps.newHashMap();
    props1.put("prop1", prop1);
    Map<String, RecordProperty> props2 = Maps.newHashMap();
    props2.put("prop1", prop1);
    props2.put("prop2", prop2);
    Map<String, RecordProperty> props3 = Maps.newHashMap();
    props3.put("prop1", prop1);
    props3.put("prop2", prop2);
    props3.put("prop3", prop3);

    RecordType recordType1 = new RecordType(registry, props1);
    RecordType recordType2 = new RecordType(registry, props2);
    RecordType recordType3 = new RecordType(registry, props3);

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType3);
    assertEquals(recordType3, greatestSubtype);
  }

  @Test
  public void testRecordTypeAssignment() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());
    RecordProperty prop2 = new RecordProperty("prop2", registry.getNumberType());

    Map<String, RecordProperty> props1 = Maps.newHashMap();
    props1.put("prop1", prop1);
    Map<String, RecordProperty> props2 = Maps.newHashMap();
    props2.put("prop1", prop1);
    props2.put("prop2", prop2);

    RecordType recordType1 = new RecordType(registry, props1);
    RecordType recordType2 = new RecordType(registry, props2);
    JSType assignmentType = registry.getNativeType(JSTypeNative.OBJECT_TYPE);

    assertEquals(recordType1, recordType1.getImplicitPrototype());
    assertEquals(assignmentType, recordType2.getImplicitPrototype());

    // Test assignment of a compatible record type
    RecordType recordType3 = new RecordType(registry, props2);
    Object record1 = RecordType.createRecordInstance(recordType1);
    Object record2 = RecordType.createRecordInstance(recordType2);
    record1 = record1.getClass().cast(recordType3.getClass());
    record1.put("prop2", 42);

    assertEquals(record1, record2);

    // Test assignment of a non-compatible record type
    RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
    assertFalse(record1 instanceof RecordType);
    assertFalse(record2 instanceof RecordType);

    record1 = record1.getClass().cast(recordType4.getClass());
    record1.put("prop3", true);

    assertFalse(record1.isEquivalentTo(record2));
  }

  @Test
  public void testRecordTypeUnion() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordProperty prop1 = new RecordProperty("prop1", registry.getStringType());

    Map<String, RecordProperty> props1 = Maps.newHashMap();
    props1.put("prop1", prop1);
    Map<String, RecordProperty> props2 = Maps.newHashMap();
    props2.put("prop1", prop1);
    props2.put("prop2", registry.getNumberType());

    RecordType recordType1 = new RecordType(registry, props1);
    RecordType recordType2 = new RecordType(registry, props2);

    UnionTypeBuilder unionBuilder = new UnionTypeBuilder(registry);
    unionBuilder.addAlternate(recordType1);
    UnionType unionType = unionBuilder.build();

    JSType record3 = RecordType.createRecordInstance(recordType2);
    record3.put("prop2", 42);

    JSType type3 = unionType.resolve(null, null);
    assertEquals(recordType2, type3);

    RecordType record4 = RecordType.createRecordInstance(recordType1);
    record4.put("prop1", "abc");

    JSType type4 = unionType.resolve(null, null);
    assertTrue(type4.isTypeOf(record4));
  }
}