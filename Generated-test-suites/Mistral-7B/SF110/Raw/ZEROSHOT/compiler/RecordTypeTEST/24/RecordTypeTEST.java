import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.RecordType;
import com.google.javascript.jscomp.type.RecordTypeBuilder;
import com.google.javascript.jscomp.type.TypeUtils;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class RecordTypeTest {

  @Test
  public void testCreateRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();

    properties.put("name", new RecordProperty("name", String.class, Node.IDENT("name")));
    properties.put("age", new RecordProperty("age", Integer.TYPE, Node.IDENT("age")));

    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals("RecordType", recordType.getName());
    assertTrue(recordType.isRecordType());
    assertEquals( properties.size(), recordType.getOwnPropertyNames().size());

    // Check properties
    assertEquals(String.class, recordType.getPropertyType("name"));
    assertEquals(Integer.TYPE, recordType.getPropertyType("age"));

    // Check equality
    RecordType recordType1 = new RecordType(registry, properties, true);
    RecordType recordType2 = new RecordType(registry, properties, true);

    assertEquals(recordType1, recordType2);

    // Check inequality
    RecordType recordType3 = new RecordType(registry, properties, false);
    assertFalse(recordType1.equals(recordType3));

    assertEquals(recordType2.getImplicitPrototype(),
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("name", new RecordProperty("name", String.class, Node.IDENT("name")));
    properties1.put("age", new RecordProperty("age", Integer.TYPE, Node.IDENT("age")));

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("name", new RecordProperty("name", String.class, Node.IDENT("name")));
    properties2.put("age", new RecordProperty("age", Integer.TYPE, Node.IDENT("age")));
    properties2.put("gender", new RecordProperty("gender", String.class, Node.IDENT("gender")));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTICAL));
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.STRUCTURAL));
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.PROTOTYPE));

    RecordType recordType3 = new RecordType(registry, properties1, false);
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.IDENTICAL));
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.STRUCTURAL));
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.PROTOTYPE));

    RecordType recordType4 = new RecordType(registry, properties2, true);
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType4, EquivalenceMethod.IDENTICAL));
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType4, EquivalenceMethod.STRUCTURAL));
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType4, EquivalenceMethod.PROTOTYPE));
  }

  @Test
  public void testRecordTypeSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("name", new RecordProperty("name", String.class, Node.IDENT("name")));
    properties1.put("age", new RecordProperty("age", Integer.TYPE, Node.IDENT("age")));

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("name", new RecordProperty("name", String.class, Node.IDENT("name")));
    properties2.put("age", new RecordProperty("age", Integer.TYPE, Node.IDENT("age")));
    properties2.put("gender", new RecordProperty("gender", String.class, Node.IDENT("gender")));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    JSType recordType1Subtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(recordType1, recordType1Subtype);

    JSType recordType2Subtype = recordType2.getGreatestSubtypeHelper(recordType1);
    assertEquals(recordType2, recordType2Subtype);

    JSType recordType3 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    JSType recordType1SubtypeObj = recordType1.getGreatestSubtypeHelper(recordType3);
    assertEquals(recordType1, recordType1SubtypeObj);

    // Check type equivalence
    RecordType recordType4 = new RecordType(registry, properties2, true);
    RecordType recordType5 = new RecordType(registry, properties1, true);

    // RecordType1 is a subtype of RecordType2 because RecordType2 is a supertype of RecordType1
    assertTrue(recordType2.isSubtype(recordType1));

    // RecordType1 is not a subtype of RecordType3 because RecordType3 is not a record type
    assertFalse(recordType3.isSubtype(recordType1));

    // RecordType1 is not a subtype of RecordType4 because RecordType1 does not have a property "gender"
    assertFalse(recordType4.isSubtype(recordType1));

    // RecordType1 is a subtype of RecordType5 because RecordType5 is a subtype of RecordType1
    assertTrue(recordType5.isSubtype(recordType1));

    // RecordType5 is a subtype of RecordType1 because RecordType1 is a supertype of RecordType5
    assertTrue(recordType1.isSubtype(recordType5));

    // RecordType1 is a subtype of RecordType2 because all properties in RecordType1 are subtypes of their counterparts in RecordType2
    assertTrue(RecordType.isSubtype(recordType1, recordType2));

    // RecordType2 is not a subtype of RecordType1 because RecordType1 has less properties than RecordType2
    assertFalse(RecordType.isSubtype(recordType2, recordType1));

    // RecordType1 is a subtype of RecordType4 because all properties in RecordType1 are subtypes of their counterparts in RecordType4
    assertTrue(RecordType.isSubtype(recordType1, recordType4));

    // RecordType4 is not a subtype of RecordType1 because RecordType1 has less properties than RecordType4
    assertFalse(RecordType.isSubtype(recordType4, recordType1));

    // RecordType5 is a subtype of RecordType2 because all properties in RecordType5 are subtypes of their counterparts in RecordType2
    assertTrue(RecordType.isSubtype(recordType5, recordType2));

    // RecordType2 is not a subtype of RecordType5 because RecordType5 has an additional property "gender"
    assertFalse(RecordType.isSubtype(recordType2, recordType5));
  }

  @Test
  public void testRecordTypeGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("name", new RecordProperty("name", String.class, Node.IDENT("name")));
    properties1.put("age", new RecordProperty("age", Integer.TYPE, Node.IDENT("age")));

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("name", new RecordProperty("name", String.class, Node.IDENT("name")));
    properties2.put("age", new RecordProperty("age", Integer.TYPE, Node.IDENT("age")));
    properties2.put("gender", new RecordProperty("gender", String.class, Node.IDENT("gender")));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

    // RecordType1 is not a subtype of RecordType2 because RecordType2 has an additional property "gender"
    assertNull(greatestSubtype);

    // RecordType2 is a supertype of RecordType1 because it has all the properties of RecordType1 plus an additional one
    assertEquals(recordType2, greatestSubtype);
  }

  @Test
  public void testRecordTypeInstantiation() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class, Node.IDENT("name")));
    properties.put("age", new RecordProperty("age", Integer.TYPE, Node.IDENT("age")));

    RecordType recordType = new RecordType(registry, properties, true);

    RecordType instanceType = new RecordType(registry, properties, false);

    assertEquals(recordType.toString(), instanceType.toString());

    JSType instance = TypeUtils.coerceToPrimitive(recordType, new Object());

    assertEquals(recordType.getPropertyType("name"), instance.getPropertyType("name"));
    assertEquals(recordType.getPropertyType("age"), instance.getPropertyType("age"));
  }

  enum EquivalenceMethod {
    IDENTICAL,
    STRUCTURAL,
    PROTOTYPE
  }
}