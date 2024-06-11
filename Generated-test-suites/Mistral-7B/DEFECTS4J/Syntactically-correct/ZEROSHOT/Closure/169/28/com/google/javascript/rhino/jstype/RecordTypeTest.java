import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import static com.google.javascript.rhino.NodeType.*;
import static com.google.javascript.rhino.jstype.RecordType.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

  private static JSTypeRegistry registry;
  private static JSTypeFactory typeFactory;

  @org.junit.BeforeClass
  public static void setUp() {
    registry = new JSTypeRegistry();
    typeFactory = registry.getFactory();
  }

  @org.junit.Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty(STRING_TYPE, VAR_DECL, NUM_INT));
    properties.put("b", new RecordProperty(NUMBER_TYPE, VAR_DECL, STRING_LITERAL));

    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals("RecordType: [a=string, b=number]", recordType.toString());
    assertNotNull(recordType.getImplicitPrototype());
    assertEquals(Object.class, recordType.getImplicitPrototype().getClass());
  }

  @org.junit.Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> prop1 = Maps.newHashMap();
    Map<String, RecordProperty> prop2 = Maps.newHashMap();

    prop1.put("a", new RecordProperty(STRING_TYPE, VAR_DECL, NUM_INT));
    prop1.put("b", new RecordProperty(NUMBER_TYPE, VAR_DECL, STRING_LITERAL));

    prop2.put("a", new RecordProperty(STRING_TYPE, VAR_DECL, NUM_INT));
    prop2.put("b", new RecordProperty(NUMBER_TYPE, VAR_DECL, STRING_LITERAL));

    RecordType recordType1 = new RecordType(registry, prop1, true);
    RecordType recordType2 = new RecordType(registry, prop2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));
    assertTrue(recordType2.checkRecordEquivalenceHelper(recordType1, false));
  }

  @org.junit.Test
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> prop1 = Maps.newHashMap();
    Map<String, RecordProperty> prop2 = Maps.newHashMap();

    prop1.put("a", new RecordProperty(STRING_TYPE, VAR_DECL, NUM_INT));
    prop1.put("b", new RecordProperty(NUMBER_TYPE, VAR_DECL, STRING_LITERAL));

    prop2.put("a", new RecordProperty(STRING_TYPE, VAR_DECL, NUM_INT));
    prop2.put("b", new RecordProperty(NUMBER_TYPE, VAR_DECL, NUM_INT));

    RecordType recordType1 = new RecordType(registry, prop1, true);
    RecordType recordType2 = new RecordType(registry, prop2, true);

    RecordType recordType3 = new RecordType(registry, prop1, false);

    assertTrue(recordType1.isSubtype(recordType3));
    assertFalse(recordType3.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType1));
  }

  @org.junit.Test
  public void testRecordTypeGetGreatestSubtypeHelper() {
    Map<String, RecordProperty> prop1 = Maps.newHashMap();
    Map<String, RecordProperty> prop2 = Maps.newHashMap();
    Map<String, RecordProperty> prop3 = Maps.newHashMap();
    Map<String, RecordProperty> prop4 = Maps.newHashMap();

    prop1.put("a", new RecordProperty(STRING_TYPE, VAR_DECL, NUM_INT));
    prop1.put("b", new RecordProperty(NUMBER_TYPE, VAR_DECL, STRING_LITERAL));

    prop2.put("a", new RecordProperty(STRING_TYPE, VAR_DECL, NUM_INT));
    prop2.put("b", new RecordProperty(NUMBER_TYPE, VAR_DECL, NUM_INT));

    prop3.put("a", new RecordProperty(STRING_TYPE, VAR_DECL, NUM_INT));
    prop3.put("b", new RecordProperty(NUMBER_TYPE, VAR_DECL, STRING_LITERAL));
    prop3.put("c", new RecordProperty(BOOLEAN_TYPE, VAR_DECL, NUM_INT));

    prop4.put("a", new RecordProperty(STRING_TYPE, VAR_DECL, NUM_INT));
    prop4.put("b", new RecordProperty(NUMBER_TYPE, VAR_DECL, STRING_LITERAL));
    prop4.put("c", new RecordProperty(STRING_TYPE, VAR_DECL, STRING_LITERAL));

    RecordType recordType1 = new RecordType(registry, prop1, true);
    RecordType recordType2 = new RecordType(registry, prop2, true);
    RecordType recordType3 = new RecordType(registry, prop3, true);
    RecordType recordType4 = new RecordType(registry, prop4, true);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);

    assertEquals("RecordType: [a=string, b=number, c=boolean]", greatestSubtype.toString());
  }

  @org.junit.Test
  public void testRecordTypeToMaybeRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty(STRING_TYPE, VAR_DECL, NUM_INT));
    properties.put("b", new RecordProperty(NUMBER_TYPE, VAR_DECL, STRING_LITERAL));

    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals(recordType, recordType.toMaybeRecordType());
  }

  @org.junit.Test
  public void testRecordTypeIsSubtype() {
    Map<String, RecordProperty> prop1 = Maps.newHashMap();
    Map<String, RecordProperty> prop2 = Maps.newHashMap();

    prop1.put("a", new RecordProperty(STRING_TYPE, VAR_DECL, NUM_INT));
    prop1.put("b", new RecordProperty(NUMBER_TYPE, VAR_DECL, STRING_LITERAL));

    prop2.put("a", new RecordProperty(STRING_TYPE, VAR_DECL, NUM_INT));
    prop2.put("b", new RecordProperty(NUMBER_TYPE, VAR_DECL, NUM_INT));

    RecordType recordType1 = new RecordType(registry, prop1, true);
    RecordType recordType2 = new RecordType(registry, prop2, true);

    assertTrue(recordType1.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType1));
  }

  @org.junit.Test
  public void testRecordTypeDefineProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty(STRING_TYPE, VAR_DECL, NUM_INT));

    RecordType recordType = new RecordType(registry, properties, true);

    assertFalse(recordType.hasProperty("b"));

    recordType.defineProperty("b", BOOLEAN_TYPE, false, NULL_NODE);

    assertTrue(recordType.hasProperty("b"));
    assertEquals(BOOLEAN_TYPE, recordType.getPropertyType("b"));
  }

  @org.junit.Test
  public void testRecordTypeGetGreatestSubtype() {
    Map<String, RecordProperty> prop1 = Maps.newHashMap();
    Map<String, RecordProperty> prop2 = Maps.newHashMap();

    prop1.put("a", new RecordProperty(STRING_TYPE, VAR_DECL, NUM_INT));
    prop1.put("b", new RecordProperty(NUMBER_TYPE, VAR_DECL, STRING_LITERAL));

    prop2.put("a", new RecordProperty(STRING_TYPE, VAR_DECL, NUM_INT));
    prop2.put("b", new RecordProperty(NUMBER_TYPE, VAR_DECL, NUM_INT));

    RecordType recordType1 = new RecordType(registry, prop1, true);
    RecordType recordType2 = new RecordType(registry, prop2, true);

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

    assertEquals(recordType1, greatestSubtype);
  }

  @org.junit.Test
  public void testRecordTypeToString() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty(STRING_TYPE, VAR_DECL, NUM_INT));
    properties.put("b", new RecordProperty(NUMBER_TYPE, VAR_DECL, STRING_LITERAL));

    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals("RecordType: [a=string, b=number]", recordType.toString());
  }

  @org.junit.Test
  public void testRecordTypeGetImplicitPrototype() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty(STRING_TYPE, VAR_DECL, NUM_INT));

    RecordType recordType = new RecordType(registry, properties, true);

    ObjectType implicitPrototype = recordType.getImplicitPrototype();

    assertEquals(Object.class, implicitPrototype.getClass());
  }
}