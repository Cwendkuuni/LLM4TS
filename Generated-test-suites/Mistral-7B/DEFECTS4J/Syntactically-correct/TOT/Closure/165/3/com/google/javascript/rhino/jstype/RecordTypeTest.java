import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSTestCase.compileTypeCheck;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnknownType;

public class RecordTypeTest extends JSTestCase {

  public void testRecordTypeCreation() {
    // Test creating a record type with known properties.
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSTypeFactory.getIntType()));
    properties.put("b", new RecordProperty("b", JSTypeFactory.getIntType()));

    RecordType recordType = new RecordType(typeRegistry, properties);
    JSType recordTypeType = recordType.getType();

    // Test getting properties.
    assertEquals(JSTypeFactory.getIntType(), recordTypeType.getPropertyType("a"));
    assertEquals(JSTypeFactory.getIntType(), recordTypeType.getPropertyType("b"));

    // Test freezing.
    recordType.freeze();
    try {
      recordType.defineProperty("c", JSTypeFactory.getIntType(), false, null);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected.
    }

    // Test subtyping.
    RecordType subType = new RecordType(typeRegistry, Maps.newHashMap());
    subType.defineProperty("a", JSTypeFactory.getIntType(), false, null);
    subType.defineProperty("b", JSTypeFactory.getIntType(), false, null);

    JSType greatestSubtype = recordTypeType.getGreatestSubtype(subType.getType());
    assertEquals(recordTypeType, greatestSubtype);

    // Test with unknown property types.
    RecordType unknownTypeRecord = new RecordType(typeRegistry, Maps.newHashMap());
    unknownTypeRecord.defineProperty("a", new UnknownType(), false, null);
    unknownTypeRecord.defineProperty("b", new UnknownType(), false, null);

    JSType anotherUnknownTypeRecord = new RecordType(typeRegistry, Maps.newHashMap());
    anotherUnknownTypeRecord.defineProperty("b", new UnknownType(), false, null);
    anotherUnknownTypeRecord.defineProperty("c", new UnknownType(), false, null);

    JSType greatestUnknownSubtype = recordTypeType.getGreatestSubtype(anotherUnknownTypeRecord.getType());
    assertNotNull(grearestUnknownSubtype);
    assertTrue(greastestUnknownSubtype.isUnknownType());

    // Test with different property types.
    RecordType differentPropertyTypesRecord = new RecordType(typeRegistry, Maps.newHashMap());
    differentPropertyTypesRecord.defineProperty("a", JSTypeFactory.getIntType(), false, null);
    differentPropertyTypesRecord.defineProperty("b", JSTypeFactory.getBooleanType(), false, null);

    UnionType greatestDifferentSubtype = (UnionType) recordTypeType.getGreatestSubtype(differentPropertyTypesRecord.getType());
    assertNotNull(greastestDifferentSubtype);
    assertTrue(greastestDifferentSubtype.isEmptyType());
  }

  public void testRecordTypeEquivalence() {
    // Test record type equivalence.
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", JSTypeFactory.getIntType()));
    properties1.put("b", new RecordProperty("b", JSTypeFactory.getIntType()));

    RecordType recordType1 = new RecordType(typeRegistry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", JSTypeFactory.getIntType()));
    properties2.put("b", new RecordProperty("b", JSTypeFactory.getIntType()));
    properties2.put("c", new RecordProperty("c", JSTypeFactory.getBooleanType()));

    RecordType recordType2 = new RecordType(typeRegistry, properties2);

    assertFalse(recordType1.isEquivalentTo(recordType2));

    Map<String, RecordProperty> properties3 = new Maps.newHashMap(properties1);
    properties3.put("c", new RecordProperty("c", JSTypeFactory.getBooleanType()));

    RecordType recordType3 = new RecordType(typeRegistry, properties3);

    // Test freezing record types.
    recordType1.freeze();
    recordType2.freeze();
    recordType3.freeze();

    // Test record types with the same properties.
    RecordType recordType4 = new RecordType(typeRegistry, properties1);
    assertTrue(recordType1.isEquivalentTo(recordType4));

    // Test record types with the same properties in a different order.
    RecordType recordType5 = new RecordType(typeRegistry, Maps.newHashMap("b", properties1.get("a")).asMap());
    assertTrue(recordType1.isEquivalentTo(recordType5));

    // Test record types with the sameproperties but with different names.
    RecordType recordType6 = new RecordType(typeRegistry, Maps.newHashMap("x", properties1.get("a")).asMap());
    assertTrue(recordType1.isEquivalentTo(recordType6));

    // Test record types with the same properties but with different types.
    RecordType recordType7 = new RecordType(typeRegistry, properties1);
    recordType7.defineProperty("a", JSTypeFactory.getBooleanType(), false, null);
    assertFalse(recordType1.isEquivalentTo(recordType7));

    // Test record types with different properties.
    RecordType recordType8 = new RecordType(typeRegistry, Maps.newHashMap("c", new RecordProperty("c", JSTypeFactory.getBooleanType())));
    assertFalse(recordType1.isEquivalentTo(recordType8));

    // Test record types with inherited properties.
    RecordType recordType9 = new RecordType(typeRegistry, properties1);
    RecordType recordType10 = new RecordType(typeRegistry, Maps.newHashMap("b", properties1.get("a")).asMap());
    recordType10.defineProperty("a", JSTypeFactory.getBooleanType(), false, null);

    assertTrue(recordType1.isEquivalentTo(recordType9));
    assertTrue(recordType9.isEquivalentTo(recordType10));
  }

  public void testRecordTypeGetPropertyNode() {
    // Test getting property nodes.
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSTypeFactory.getIntType()));
    RecordType recordType = new RecordType(typeRegistry, properties);

    Node node = recordType.getPropertyNode("a");
    assertNotNull(node);
  }

  public void testRecordTypeGetPropertyType() {
    // Test getting property types.
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSTypeFactory.getIntType()));
    properties.put("b", new RecordProperty("b", JSTypeFactory.getBooleanType()));

    RecordType recordType = new RecordType(typeRegistry, properties);

    assertEquals(JSTypeFactory.getIntType(), recordType.getPropertyType("a"));
    assertEquals(JSTypeFactory.getBooleanType(), recordType.getPropertyType("b"));
  }

  public void testRecordTypeHasProperty() {
    // Test has property.
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSTypeFactory.getIntType()));
    properties.put("b", new RecordProperty("b", JSTypeFactory.getBooleanType()));

    RecordType recordType = new RecordType(typeRegistry, properties);

    assertTrue(recordType.hasProperty("a"));
    assertTrue(recordType.hasProperty("b"));
    assertFalse(recordType.hasProperty("c"));
  }

  public void testRecordTypeIsSubtype() {
    // Test isSubtype.
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", JSTypeFactory.getIntType()));
    properties1.put("b", new RecordProperty("b", JSTypeFactory.getIntType()));

    RecordType recordType1 = new RecordType(typeRegistry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", JSTypeFactory.getIntType()));
    properties2.put("b", new RecordProperty("b", JSTypeFactory.getIntType()));
    properties2.put("c", new RecordProperty("c", JSTypeFactory.getBooleanType()));

    RecordType recordType2 = new RecordType(typeRegistry, properties2);

    Map<String, RecordProperty> properties3 = new Maps.newHashMap(properties1);
    properties3.put("c", new RecordProperty("c", JSTypeFactory.getBooleanType()));

    RecordType recordType3 = new RecordType(typeRegistry, properties3);

    Map<String, RecordProperty> properties4 = Maps.newHashMap();
    properties4.put("a", new RecordProperty("a", JSTypeFactory.getIntType()));
    properties4.put("c", new RecordProperty("c", JSTypeFactory.getBooleanType()));

    RecordType recordType4 = new RecordType(typeRegistry, properties4);

    // Test record types with the same properties.
    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType2));
    assertTrue(recordType1.isSubtype(recordType3));
    assertTrue(recordType1.isSubtype(recordType4));

    // Test record types with different properties.
    assertFalse(recordType1.isSubtype(JSTypeNative.OBJECT_TYPE));
    assertFalse(recordType1.isSubtype(JSTypeFactory.getFunctionType(null, null)));
    assertFalse(recordType1.isSubtype(JSTypeFactory.getArrayType(UnknownType.instance(), null)));
    assertFalse(recordType1.isSubtype(JSTypeFactory.getRecordType(null)));
  }

  public void testRecordTypeToString() {
    // Test toString.
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSTypeFactory.getIntType()));
    properties.put("b", new RecordProperty("b", JSTypeFactory.getBooleanType()));

    RecordType recordType = new RecordType(typeRegistry, properties);

    String string = recordType.toString();
    assertTrue(string.contains("RecordType{"));
    assertTrue(string.contains("properties={"));
    assertTrue(string.contains("a: Int"));
    assertTrue(string.contains("b: Boolean"));
  }

  public void testRecordTypeIsFrozen() {
    // Test isFrozen.
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSTypeFactory.getIntType()));
    properties.put("b", new RecordProperty("b", JSTypeFactory.getBooleanType()));

    RecordType recordType = new RecordType(typeRegistry, properties);

    assertTrue(recordType.isFrozen());

    recordType.freeze();

    assertTrue(recordType.isFrozen());
  }

  public void testRecordTypeDefineProperty() {
    // Test define property.
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSTypeFactory.getIntType()));
    properties.put("b", new RecordProperty("b", JSTypeFactory.getBooleanType()));

    RecordType recordType = new RecordType(typeRegistry, properties);

    assertFalse(recordType.defineProperty("c", JSTypeFactory.getIntType(), false, null));

    recordType.freeze();

    assertFalse(recordType.defineProperty("c", JSTypeFactory.getIntType(), false, null));
  }

  public void testRecordTypeGetImplicitPrototype() {
    // Test getImplicitPrototype.
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSTypeFactory.getIntType()));
    properties.put("b", new RecordProperty("b", JSTypeFactory.getBooleanType()));

    RecordType recordType = new RecordType(typeRegistry, properties);

    ObjectType implicitPrototype = recordType.getImplicitPrototype();

    assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype);
  }

  public void testRecordTypeResolve() {
    // Test resolve.
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSTypeFactory.getIntType()));
    properties.put("b", new RecordProperty("b", JSTypeFactory.getBooleanType()));

    RecordType recordType = new RecordType(typeRegistry, properties);

    JSType resolvedType = recordType.resolve(null, null);

    // Test resolved type.
    assertEquals(recordType.getType(), resolvedType);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    typeRegistry = new JSTypeRegistry();
    registry = new JSTypeRegistry();
    registry.setParentRegistry(typeRegistry);
    registry.setTypeFactory(typeFactory);

    compileTypeCheck(new NodeFactory(), new CompilerOptions(),
        new ScriptSourceInfo("RecordTypeTest", null, null),
        new Node("(function() {})"));
  }
}