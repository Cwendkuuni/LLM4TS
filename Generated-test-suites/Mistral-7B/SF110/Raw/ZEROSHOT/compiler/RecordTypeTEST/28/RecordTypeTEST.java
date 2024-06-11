import static org.junit.Assert.*;
import static com.google.javascript.rhino.testing.TypeChecker.*;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.TypeChecker;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RecordTypeTest {

  @org.junit.BeforeClass
  public static void setup() {
    TypeChecker.init();
  }

  @org.junit.Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = TypeChecker.getRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("x", new RecordProperty("x", registry.getNumberType()));
    properties.put("y", new RecordProperty("y", registry.getStringType()));

    RecordType recordType = new RecordType(registry, properties, true);
    assertEquals(recordType.getClass(), RecordType.class);
    assertEquals(recordType.getDeclared(), true);

    RecordType synthesizedRecordType = new RecordType(registry, properties, false);
    assertEquals(synthesizedRecordType.getClass(), RecordType.class);
    assertEquals(synthesizedRecordType.getDeclared(), false);

    assertEquals(recordType.getImplicitPrototype(),
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
  }

  @org.junit.Test
  public void testRecordTypeEquals() {
    JSTypeRegistry registry = TypeChecker.getRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("x", new RecordProperty("x", registry.getNumberType()));
    properties.put("y", new RecordProperty("y", registry.getStringType()));

    RecordType recordType1 = new RecordType(registry, properties, true);
    RecordType recordType2 = new RecordType(registry, properties, true);

    assertEquals(recordType1, recordType2);
  }

  @org.junit.Test
  public void testRecordTypeFrozen() {
    JSTypeRegistry registry = TypeChecker.getRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("x", new RecordProperty("x", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);
    recordType.freeze();

    assertFalse(recordType.defineProperty("x", registry.getNumberType(), false, null));
  }

  @org.junit.Test
  public void testRecordTypeGetProperty() {
    JSTypeRegistry registry = TypeChecker.getRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("x", new RecordProperty("x", registry.getNumberType()));
    properties.put("y", new RecordProperty("y", registry.getStringType()));

    RecordType recordType = new RecordType(registry, properties, true);

    JSType typeX = recordType.getPropertyType("x");
    assertEquals(typeX, registry.getNumberType());

    JSType typeY = recordType.getPropertyType("y");
    assertEquals(typeY, registry.getStringType());

    JSType typeUnknown = recordType.getPropertyType("z");
    assertNull(typeUnknown);
  }

  @org.junit.Test
  public void testRecordTypeGetOwnPropertyNames() {
    JSTypeRegistry registry = TypeChecker.getRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("x", new RecordProperty("x", registry.getNumberType()));
    properties.put("y", new RecordProperty("y", registry.getStringType()));

    RecordType recordType = new RecordType(registry, properties, true);

    Set<String> getOwnPropertyNames = recordType.getOwnPropertyNames();
    assertEquals(getOwnPropertyNames.size(), 2);
    assertTrue(getOwnPropertyNames.containsAll(Collections.singletonList("x")));
    assertTrue(getOwnPropertyNames.containsAll(Collections.singletonList("y")));
  }

  @org.junit.Test
  public void testRecordTypeCheckEquivalence() {
    JSTypeRegistry registry = TypeChecker.getRegistry();

    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("x", new RecordProperty("x", registry.getNumberType()));
    properties1.put("y", new RecordProperty("y", registry.getStringType()));

    RecordType recordType1 = new RecordType(registry, properties1, true);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("x", new RecordProperty("x", registry.getNumberType()));
    properties2.put("y", new RecordProperty("y", registry.getStringType()));

    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTICAL));
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.SUBTYPES));
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.SUPERTYPES));

    RecordType recordType3 = new RecordType(registry, properties1, true);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.IDENTICAL));
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.SUBTYPES));
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.SUPERTYPES));
  }

  @org.junit.Test
  public void testRecordTypeGreatestSubtype() {
    JSTypeRegistry registry = TypeChecker.getRegistry();

    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("x", new RecordProperty("x", registry.getNumberType()));
    properties1.put("y", new RecordProperty("y", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1, true);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("x", new RecordProperty("x", registry.getNumberType()));
    properties2.put("y", new RecordProperty("y", registry.getBooleanType()));

    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertEquals(recordType1.getGreatestSubtype(recordType2), recordType1);

    Map<String, RecordProperty> properties3 = new HashMap<>();
    properties3.put("x", new RecordProperty("x", registry.getNumberType()));
    properties3.put("y", new RecordProperty("y", registry.getStringType()));

    RecordType recordType3 = new RecordType(registry, properties3, true);

    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    builder.addAlternate(recordType2);
    builder.addAlternate(recordType3);

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
    assertEquals( greatestSubtype, builder.build());
  }

  @org.junit.Test
  public void testRecordTypeSubtype() {
    JSTypeRegistry registry = TypeChecker.getRegistry();

    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("x", new RecordProperty("x", registry.getNumberType()));
    properties1.put("y", new RecordProperty("y", registry.getStringType()));

    RecordType recordType1 = new RecordType(registry, properties1, true);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("x", new RecordProperty("x", registry.getNumberType()));
    properties2.put("y", new RecordProperty("y", registry.getStringType()));

    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.isSubtype(recordType2));

    Map<String, RecordProperty> properties3 = new HashMap<>();
    properties3.put("x", new RecordProperty("x", registry.getNumberType()));
    properties3.put("z", new RecordProperty("z", registry.getBooleanType()));

    RecordType recordType3 = new RecordType(registry, properties3, true);

    assertFalse(recordType1.isSubtype(recordType3));
  }
}