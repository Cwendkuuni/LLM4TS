import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", JSType.STRING_TYPE));
    properties.put("age", new RecordProperty("age", JSType.NUMBER_TYPE));

    RecordType recordType = new RecordType(registry, properties);
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordType.getImplicitPrototype());

    // Basic test for isEquivalentTo method
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertTrue(recordType1.isEquivalentTo(recordType2));

    // Test for defineProperty method
    recordType1.defineProperty("gender", JSType.STRING_TYPE, false, null);
    assertFalse(recordType1.isEquivalentTo(recordType2));

    // Test for getLeastSupertype method
    RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
    RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
    recordType3.defineProperty("name", JSType.STRING_TYPE, false, null);
    recordType3.defineProperty("age", JSType.NUMBER_TYPE, false, null);
    recordType4.defineProperty("name", JSType.STRING_TYPE, false, null);
    recordType4.defineProperty("age", JSType.NUMBER_TYPE, false, null);
    recordType4.defineProperty("gender", JSType.STRING_TYPE, false, null);

    JSType leastSupertype = recordType1.getLeastSupertype(recordType3);
    assertEquals(recordType1, leastSupertype);

    leastSupertype = recordType1.getLeastSupertype(recordType4);
    assertEquals(recordType1, leastSupertype);

    // Test for getGreatestSubtypeHelper method
    RecordType recordType5 = new RecordType(registry, Maps.newHashMap());
    recordType5.defineProperty("name", JSType.STRING_TYPE, false, null);
    recordType5.defineProperty("age", JSType.NUMBER_TYPE, false, null);
    recordType5.defineProperty("gender", JSType.STRING_TYPE, false, null);
    recordType5.defineProperty("city", JSType.STRING_TYPE, false, null);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType5);
    assertEquals(recordType5, greatestSubtype);

    // Test for isSubtype method
    RecordType recordType6 = new RecordType(registry, Maps.newHashMap());
    recordType6.defineProperty("name", JSType.STRING_TYPE, false, null);
    recordType6.defineProperty("age", JSType.NUMBER_TYPE, false, null);
    recordType6.defineProperty("city", JSType.STRING_TYPE, false, null);

    assertFalse(recordType1.isSubtype(recordType6));

    // Test for defineProperty with already defined property
    recordType1.defineProperty("name", JSType.STRING_TYPE, false, null);
    recordType1.defineProperty("name", JSType.STRING_TYPE, false, null);
    assertEquals(recordType1, recordType1);

    // Test for defineProperty with null RecordProperty
    try {
      recordType1.defineProperty("name", null, false, null);
      fail();
    } catch (IllegalArgumentException e) {
      // Expected
    }

    // Test for defineProperty with null propertyName
    try {
      recordType1.defineProperty(null, JSType.STRING_TYPE, false, null);
      fail();
    } catch (NullPointerException e) {
      // Expected
    }
  }
}