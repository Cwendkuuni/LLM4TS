import static com.google.javascript.rhino.JSType.*;
import static org.junit.Assert.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
  private JSTypeRegistry registry;
  private ErrorReporter t;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    t = new ErrorReporter();
  }

  @Test
  public void testBasicRecordType() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordProperty("a", INT));
    recordProperties.put("b", new RecordProperty("b", STRING));
    RecordType recordType = new RecordType(registry, recordProperties);

    // Test assigning a record type to a variable of the same type
    Object record = new Object() {
      public RecordType getRecordType() {
        return recordType;
      }
    };
    JSType recordJSType = registry.getTypeFactory().createReferenceType(record.getClass());
    JSType recordInstance = new RecordTypeInstance(recordJSType);
    assertEquals(recordType, recordInstance.getRecordType());

    // Test setting properties on the record
    recordInstance.setProperty("a", 42);
    recordInstance.setProperty("b", "hello");
    assertEquals(INT, recordInstance.getPropertyType("a"));
    assertEquals(STRING, recordInstance.getPropertyType("b"));
    assertEquals(42, recordInstance.getProperty("a").asInt());
    assertEquals("hello", recordInstance.getProperty("b").asString());

    // Test getting properties from the record
    JSType recordPropertyA = recordInstance.getPropertyType("a");
    JSType recordPropertyB = recordInstance.getPropertyType("b");
    assertEquals(INT, recordPropertyA);
    assertEquals(STRING, recordPropertyB);

    // Test getting properties from the record using getPropertyNames
    String[] propertyNames = recordInstance.getPropertyNames();
    assertEquals(2, propertyNames.length);
    assertEquals("a", propertyNames[0]);
    assertEquals("b", propertyNames[1]);

    // Test freezing the record
    recordType.freeze();
    try {
      recordInstance.setProperty("a", "foo");
      fail("Expected an IllegalStateException when setting a property on a frozen record.");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testSubtypingRecordTypes() {
    // Create a record type with properties 'a' and 'b'
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("a", new RecordProperty("a", INT));
    recordPropertiesA.put("b", new RecordProperty("b", STRING));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

    // Create a record type with properties 'a' and 'b', and an additional property 'c'
    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap(recordPropertiesA);
    recordPropertiesB.put("c", new RecordProperty("c", BOOL));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    // Test that recordTypeB is a subtype of recordTypeA
    assertTrue(recordTypeA.isSubtype(recordTypeB));

    // Test that recordTypeA is not a subtype of recordTypeB
    assertFalse(recordTypeB.isSubtype(recordTypeA));
  }

  @Test
  public void testGetLeastSupertypeRecordTypes() {
    // Create record types 'A' and 'B'
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("a", new RecordProperty("a", INT));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("a", new RecordProperty("a", INT));
    recordPropertiesB.put("b", new RecordProperty("b", STRING));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    // Test that the least supertype of 'A' and 'B' is 'Object'
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordTypeA.getLeastSupertype(recordTypeB));
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordTypeB.getLeastSupertype(recordTypeA));

    // Create record type 'C' with properties 'a' and 'b'
    Map<String, RecordProperty> recordPropertiesC = Maps.newHashMap(recordPropertiesA);
    recordPropertiesC.put("c", new RecordProperty("c", BOOL));
    RecordType recordTypeC = new RecordType(registry, recordPropertiesC);

    // Test that the least supertype of 'A' and 'C' is 'A'
    assertEquals(recordTypeA, recordTypeA.getLeastSupertype(recordTypeC));
    assertEquals(recordTypeC, recordTypeC.getLeastSupertype(recordTypeA));

    // Test that the least supertype of 'B' and 'C' is 'C'
    assertEquals(recordTypeC, recordTypeB.getLeastSupertype(recordTypeC));
    assertEquals(recordTypeC, recordTypeC.getLeastSupertype(recordTypeB));

    // Test that the least supertype of 'A' and 'B' with a common supertype 'D' is 'D'
    Map<String, RecordProperty> recordPropertiesD = Maps.newHashMap();
    recordPropertiesD.put("a", new RecordProperty("a", INT));
    recordPropertiesD.put("b", new RecordProperty("b", STRING));
    recordPropertiesD.put("c", new RecordProperty("c", BOOL));
    RecordType recordTypeD = new RecordType(registry, recordPropertiesD);

    assertEquals(recordTypeD, recordTypeA.getLeastSupertype(recordTypeD));
    assertEquals(recordTypeD, recordTypeB.getLeastSupertype(recordTypeD));
  }

  @Test
  public void testGreatestSubtypeRecordTypes() {
    // Create record types 'A' and 'B'
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("a", new RecordProperty("a", INT));
    recordPropertiesA.put("b", new RecordProperty("b", STRING));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("a", new RecordProperty("a", INT));
    recordPropertiesB.put("c", new RecordProperty("c", BOOL));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    // Test that the greatest subtype of 'A' and 'B' is empty
    assertEquals(JSTypeNative.NO_TYPE, recordTypeA.getGreatestSubtype(recordTypeB));
    assertEquals(JSTypeNative.NO_TYPE, recordTypeB.getGreatestSubtype(recordTypeA));

    // Create record type 'C' with properties 'a' and 'b'
    Map<String, RecordProperty> recordPropertiesC = Maps.newHashMap(recordPropertiesA);
    recordPropertiesC.put("c", new RecordProperty("c", BOOL));
    RecordType recordTypeC = new RecordType(registry, recordPropertiesC);

    // Test that the greatest subtype of 'A' and 'C' is 'C'
    assertEquals(recordTypeC, recordTypeA.getGreatestSubtype(recordTypeC));
    assertEquals(recordTypeC, recordTypeC.getGreatestSubtype(recordTypeA));

    // Test that the greatest subtype of 'B' and 'C' is 'B'
    assertEquals(recordTypeB, recordTypeB.getGreatestSubtype(recordTypeC));
    assertEquals(recordTypeB, recordTypeC.getGreatestSubtype(recordTypeB));

    // Test that the greatest subtype of 'A' and 'B' with a common supertype 'D' is 'D'
    Map<String, RecordProperty> recordPropertiesD = Maps.newHashMap();
    recordPropertiesD.put("a", new RecordProperty("a", INT));
    recordPropertiesD.put("b", new RecordProperty("b", STRING));
    recordPropertiesD.put("c", new RecordProperty("c", BOOL));
    RecordType recordTypeD = new RecordType(registry, recordPropertiesD);

    assertEquals(recordTypeD, recordTypeA.getGreatestSubtype(recordTypeD));
    assertEquals(recordTypeD, recordTypeB.getGreatestSubtype(recordTypeD));
  }
}