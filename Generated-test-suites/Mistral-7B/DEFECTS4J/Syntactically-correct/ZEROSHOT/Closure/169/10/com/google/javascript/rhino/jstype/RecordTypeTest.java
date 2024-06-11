import static org.junit.Assert.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("name", new RecordProperty("name", registry.stringType()));
    recordProperties.put("age", new RecordProperty("age", registry.numberType()));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    assertEquals("RecordType", recordType.getClassName());
    assertEquals(2, recordType.getDeclaredPropertyNames().length);

    assertEquals("name", recordType.getPropertyNames()[0]);
    assertEquals("age", recordType.getPropertyNames()[1]);

    assertEquals(registry.stringType(), recordType.getPropertyType("name"));
    assertEquals(registry.numberType(), recordType.getPropertyType("age"));

    assertEquals(recordType, recordType.getImplicitPrototype());
  }

  @Test
  public void testCheckRecordEquivalence() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", registry.stringType()));
    recordProperties1.put("age", new RecordProperty("age", registry.numberType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.stringType()));
    recordProperties2.put("age", new RecordProperty("age", registry.numberType()));
    recordProperties2.put("city", new RecordProperty("city", registry.stringType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));

    recordProperties2.put("city", null);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));

    recordProperties1.put("city", new RecordProperty("city", registry.stringType()));

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));
  }

  @Test
  public void testIsSubtype() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", registry.stringType()));
    recordProperties1.put("age", new RecordProperty("age", registry.numberType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.stringType()));
    recordProperties2.put("age", new RecordProperty("age", registry.numberType()));
    recordProperties2.put("city", new RecordProperty("city", registry.stringType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    RecordType recordType3 = new RecordType(registry, recordProperties1, false);

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType2));
    assertTrue(recordType2.isSubtype(recordType1));

    assertFalse(recordType1.isSubtype(registry.objectType));
    assertFalse(recordType1.isSubtype(registry.numberType));

    assertFalse(recordType3.isSubtype(recordType1));
    assertFalse(recordType1.isSubtype(recordType3));

    // Record with a missing property should not be a subtype of the record with all properties
    Map<String, RecordProperty> recordProperties4 = Maps.newHashMap();
    recordProperties4.put("age", new RecordProperty("age", registry.numberType()));

    RecordType recordType4 = new RecordType(registry, recordProperties4, true);

    assertFalse(recordType1.isSubtype(recordType4));
  }

  @Test
  public void testGetGreatestSubtype() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("name", new RecordProperty("name", registry.stringType()));
    recordProperties1.put("age", new RecordProperty("age", registry.numberType()));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("name", new RecordProperty("name", registry.stringType()));
    recordProperties2.put("age", new RecordProperty("age", registry.numberType()));
    recordProperties2.put("city", new RecordProperty("city", registry.stringType()));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);

    Map<String, RecordProperty> recordProperties3 = Maps.newHashMap();
    recordProperties3.put("name", new RecordProperty("name", registry.stringType()));
    recordProperties3.put("age", new RecordProperty("age", registry.numberType()));
    recordProperties3.put("city", new RecordProperty("city", registry.numberType()));

    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    Map<String, RecordProperty> recordProperties4 = Maps.newHashMap();
    recordProperties4.put("name", new RecordProperty("name", registry.numberType()));
    recordProperties4.put("age", new RecordProperty("age", registry.numberType()));
    recordProperties4.put("city", new RecordProperty("city", registry.stringType()));

    RecordType recordType3 = new RecordType(registry, recordProperties3, true);

    Map<String, RecordProperty> recordProperties5 = Maps.newHashMap();
    recordProperties5.put("name", new RecordProperty("name", registry.stringType()));
    recordProperties5.put("age", new RecordProperty("age", registry.numberType()));
    recordProperties5.put("city", new RecordProperty("city", registry.numberType()));
    recordProperties5.put("country", new RecordProperty("country", registry.stringType()));

    RecordType recordType4 = new RecordType(registry, recordProperties5, true);

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

    assertEquals(recordType2, greatestSubtype);

    greatestSubtype = recordType2.getGreatestSubtype(recordType3);

    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
    unionTypeBuilder.addAlternate(recordType1);
    unionTypeBuilder.addAlternate(recordType2);
    JSType unionType = unionTypeBuilder.build();

    assertEquals(unionType, greatestSubtype);

    greatestSubtype = recordType3.getGreatestSubtype(recordType4);

    RecordType recordType5 = new RecordType(registry, recordProperties5, false);

    greatestSubtype = greatestSubtype.getLeastSupertype(recordType5);

    assertEquals(recordType1, greatestSubtype);
  }
}