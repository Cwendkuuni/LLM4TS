import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

public class RecordTypeTest {

  private JSTypeRegistry registry;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", INT_TYPE));
    properties.put("y", new RecordProperty("y", STRING_TYPE));

    RecordType type = new RecordType(registry, properties);

    // Check that the record type has the correct properties
    assertEquals("x", type.getPropertyNames().get(0));
    assertEquals("y", type.getPropertyNames().get(1));
    assertEquals(INT_TYPE, type.getPropertyType("x"));
    assertEquals(STRING_TYPE, type.getPropertyType("y"));

    // Test that the record type has the correct prototype
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        type.getImplicitPrototype());
  }

  @Test
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> prop1 = Maps.newHashMap();
    prop1.put("x", new RecordProperty("x", INT_TYPE));

    Map<String, RecordProperty> prop2 = Maps.newHashMap();
    prop2.put("x", new RecordProperty("x", INT_TYPE));
    prop2.put("y", new RecordProperty("y", STRING_TYPE));

    RecordType type1 = new RecordType(registry, prop1);
    RecordType type2 = new RecordType(registry, prop2);

    // Check that type1 is a subtype of type2
    assertTrue(type1.isSubtype(type2));

    // Test that type2 is not a subtype of type1
    assertFalse(type2.isSubtype(type1));
  }

  @Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> prop1 = Maps.newHashMap();
    prop1.put("x", new RecordProperty("x", INT_TYPE));
    prop1.put("y", new RecordProperty("y", STRING_TYPE));

    Map<String, RecordProperty> prop2 = Maps.newHashMap();
    prop2.put("x", new RecordProperty("x", INT_TYPE));
    prop2.put("y", new RecordProperty("y", STRING_TYPE));

    RecordType type1 = new RecordType(registry, prop1);
    RecordType type2 = new RecordType(registry, prop2);

    // Check that type1 and type2 are equivalent
    assertEquals(type1, type2);
  }

  @Test
  public void testRecordTypePropertyAccess() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", INT_TYPE));
    properties.put("y", new RecordProperty("y", STRING_TYPE));

    RecordType type = new RecordType(registry, properties);

    // Access a read-only property
    assertEquals(INT_TYPE, type.getPropertyType("x"));

    // Define a new property
    Node node = Node.NEW_OBJECT();
    type.defineProperty("z", INT_TYPE, false, node);
    assertEquals(INT_TYPE, type.getPropertyType("z"));
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeFrozenPropertyModification() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", INT_TYPE));
    properties.put("y", new RecordProperty("y", STRING_TYPE));

    RecordType type = new RecordType(registry, properties);
    type.isFrozen = true;

    // Attempt to modify the properties of the record type
    try {
      type.defineProperty("z", INT_TYPE, false, Node.NULL());
      fail("Expected an IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }
}