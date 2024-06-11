import static com.google.javascript.rhino.JSType.*;
import static org.junit.Assert.*;

import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.node.Node;
import com.google.javascript.rhino.types.ObjectType;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeWithTwoProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", INT));
    properties.put("prop2", new RecordProperty("prop2", STRING));

    RecordType recordType = new RecordType(registry, properties, true);

    // Test property access
    assertEquals("int", recordType.getPropertyType("prop1").getName());
    assertEquals("string", recordType.getPropertyType("prop2").getName());

    // Test record type subtyping
    ObjectType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    RecordType subType = new RecordType(registry, properties, false);
    RecordType superType = new RecordType(registry, properties, true);

    assertTrue(superType.isSubtype(recordType));
    assertTrue(superType.isSubtype(subType));
    assertTrue(subType.isSubtype(objType));
    assertFalse(objType.isSubtype(superType));
    assertFalse(objType.isSubtype(recordType));

    // Test record type equivalence
    RecordType equivalentType = new RecordType(registry, properties, true);
    assertTrue(recordType.checkRecordEquivalenceHelper(equivalentType, false));

    // Test record type with missing property
    RecordType missingPropertyType = new RecordType(registry, Maps.newHashMap(), true);
    assertFalse(recordType.isSubtype(missingPropertyType));

    // Test record type with extra property
    Map<String, RecordProperty> extraProperties = new HashMap<>(properties);
    extraProperties.put("prop3", new RecordProperty("prop3", INT));
    RecordType extraPropertyType = new RecordType(registry, extraProperties, true);
    assertFalse(recordType.isSubtype(extraPropertyType));

    // Test record type with different property types
    Map<String, RecordProperty> differentTypes = new HashMap<>(properties);
    differentTypes.put("prop1", new RecordProperty("prop1", STRING));
    RecordType differentType = new RecordType(registry, differentTypes, true);
    assertFalse(recordType.isSubtype(differentType));
  }
}