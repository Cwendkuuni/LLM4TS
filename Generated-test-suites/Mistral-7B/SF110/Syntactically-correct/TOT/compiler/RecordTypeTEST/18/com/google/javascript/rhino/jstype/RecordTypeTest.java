import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.TypeRegistry;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    TypeRegistry registry = new TypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", JSType.STRING));
    properties.put("age", new RecordProperty("age", JSType.NUMBER));

    RecordType recordType = new RecordType(registry, properties, true);
    assertEquals("RecordType", recordType.getClass().getSimpleName());
    assertTrue(recordType.isRecordType());
    assertTrue(recordType.isSynthetic());

    RecordTypeBuilder builder = registry.getRecordTypeBuilder();
    builder.setSynthesized(false);
    RecordType expectedRecordType = builder.build(properties);
    assertEquals(recordType, expectedRecordType);
  }

  @Test
  public void testDefineProperty() {
    TypeRegistry registry = new TypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", JSType.STRING));

    RecordType recordType = new RecordType(registry, properties, false);
    assertFalse(recordType.defineProperty("name", JSType.NUMBER, false, null));
    assertNull(recordType.getPropertyNode("name"));

    JSType numberType = JSType.NUMBER;
    Node nameNode = registry.newJsFunctionExpression(null, null);
    recordType.defineProperty("name", numberType, false, nameNode);
    assertEquals(numberType, recordType.getPropertyType("name"));
    assertNotNull(recordType.getPropertyNode("name"));
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    TypeRegistry registry = new TypeRegistry();

    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", JSType.STRING));
    propertiesA.put("age", new RecordProperty("age", JSType.NUMBER));

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", JSType.STRING));
    propertiesB.put("age", new RecordProperty("age", JSType.NUMBER));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, null));

    propertiesB.put("gender", new RecordProperty("gender", JSType.STRING));
    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, null));

    propertiesB.remove("gender");
    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, null));
  }

  @Test
  public void testGetImplicitPrototype() {
    TypeRegistry registry = new TypeRegistry();
    RecordType recordType = new RecordType(registry, new HashMap<>(), true);
    ObjectType implicitPrototype = recordType.getImplicitPrototype();

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    TypeRegistry registry = new TypeRegistry();
    JSType stringType = JSType.STRING;
    JSType numberType = JSType.NUMBER;
    JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    RecordType recordTypeA = new RecordType(registry, new HashMap<>(), true);
    RecordType recordTypeB = new RecordType(registry, new HashMap<>(), true);
    RecordType recordTypeC = new RecordType(registry, new HashMap<>(), true);
    recordTypeC.defineProperty("name", stringType, false, null);

    JSType greatestSubtypeAB = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    assertEquals(objectType, greatestSubtypeAB);

    greatestSubtypeAB = recordTypeA.getGreatestSubtypeHelper(recordTypeC);
    assertEquals(recordTypeC, greatestSubtypeAB);

    greatestSubtypeAB = recordTypeC.getGreatestSubtypeHelper(recordTypeA);
    assertEquals(recordTypeC, greatestSubtypeAB);

    greatestSubtypeAB = recordTypeC.getGreatestSubtypeHelper(recordTypeB);
    assertEquals(recordTypeC, greatestSubtypeAB);
  }

  @Test
  public void testIsSubtype() {
    TypeRegistry registry = new TypeRegistry();

    RecordType recordType1 = new RecordType(registry, new HashMap<>(), true);
    RecordType recordType2 = new RecordType(registry, new HashMap<>(), true);
    RecordType recordType3 = new RecordType(registry, new HashMap<>(), true);
    recordType3.defineProperty("name", JSType.STRING, false, null);

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType2));
    assertTrue(recordType1.isSubtype(recordType3));
    assertTrue(recordType2.isSubtype(recordType1));
    assertTrue(recordType2.isSubtype(recordType2));
    assertTrue(recordType2.isSubtype(recordType3));
    assertTrue(recordType3.isSubtype(recordType1));
    assertTrue(recordType3.isSubtype(recordType2));
    assertTrue(recordType3.isSubtype(recordType3));

    assertFalse(recordType1.isSubtype(JSTypeNative.BOOL));
    assertFalse(recordType1.isSubtype(JSTypeNative.NUMBER));
    assertFalse(recordType1.isSubtype(JSTypeNative.STRING));
    assertFalse(JSTypeNative.OBJECT_TYPE.isSubtype(recordType1));
    assertFalse(recordType1.isSubtype(new JSTypeArrayType(registry, JSType.STRING)));
  }
}