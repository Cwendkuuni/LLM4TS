import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {
  private ErrorReporter errorReporter;
  private JSTypeRegistry registry;
  private RecordType recordType;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter();
    registry = new JSTypeRegistry(errorReporter);

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getIntegerType()));
    properties.put("b", new RecordProperty("b", registry.getBooleanType()));
    recordType = new RecordType(registry, properties);
  }

  @Test
  public void testIsEquivalentTo() {
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);

    assertTrue("RecordType1 should be equal to RecordType2",
        recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSType prototype = recordType.getImplicitPrototype();
    assertEquals("The implicit prototype should be ObjectType",
        JSTypeNative.OBJECT_TYPE, prototype);
  }

  @Test
  public void testDefineProperty() {
    recordType.defineProperty("c", registry.getStringType(), false, null);
    assertNotNull("The record type should have a property 'c'",
        recordType.getPropertyNode("c"));
  }

  @Test(expected = IllegalStateException.class)
  public void testFreezeAndDefineProperty() {
    recordType.defineProperty("a", registry.getStringType(), false, null);
    recordType.freeze();
    recordType.defineProperty("a", registry.getIntegerType(), false, null);
  }

  @Test
  public void testGetLeastSupertype() {
    RecordProperty recordPropertyA = new RecordProperty("a", registry.getIntegerType());
    RecordProperty recordPropertyB = new RecordProperty("b", registry.getBooleanType());
    RecordProperty recordPropertyC = new RecordProperty("c", registry.getBooleanType());

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", recordPropertyA);
    propertiesA.put("b", recordPropertyB);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", recordPropertyA);
    propertiesB.put("b", recordPropertyB);
    propertiesB.put("c", recordPropertyC);

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    JSType leastSupertype = recordTypeA.getLeastSupertype(recordTypeB);
    assertNotNull("The least supertype of recordTypeA and recordTypeB should not be null", leastSupertype);
    assertTrue("The least supertype of recordTypeA and recordTypeB should be a record type", leastSupertype instanceof RecordType);
    assertEquals("The least supertype of recordTypeA and recordTypeB should have the same properties as recordTypeB",
        propertiesB, leastSupertype.getProperties());
  }

  @Test
  public void testIsSubtype() {
    RecordProperty recordPropertyA = new RecordProperty("a", registry.getIntegerType());
    RecordProperty recordPropertyB = new RecordProperty("b", registry.getBooleanType());
    RecordProperty recordPropertyC = new RecordProperty("c", registry.getBooleanType());

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", recordPropertyA);
    propertiesA.put("b", recordPropertyB);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", recordPropertyA);

    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("a", recordPropertyA);
    propertiesC.put("c", new RecordProperty("c", registry.getBooleanType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);
    RecordType recordTypeC = new RecordType(registry, propertiesC);

    assertTrue("RecordTypeA should be a subtype of RecordTypeB",
        recordTypeA.isSubtype(recordTypeB));
    assertFalse("RecordTypeB should not be a subtype of RecordTypeA",
        recordTypeB.isSubtype(recordTypeA));
    assertFalse("RecordTypeA should not be a subtype of RecordTypeC",
        recordTypeA.isSubtype(recordTypeC));
  }

  @Test
  public void testGreatestSubtype() {
    RecordProperty recordPropertyA = new RecordProperty("a", registry.getIntegerType());
    RecordProperty recordPropertyB = new RecordProperty("b", registry.getBooleanType());
    RecordProperty recordPropertyC = new RecordProperty("c", registry.getBooleanType());

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", recordPropertyA);
    propertiesA.put("b", recordPropertyB);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("b", recordPropertyB);
    propertiesB.put("c", new RecordProperty("c", registry.getBooleanType()));

    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("a", recordPropertyA);
    propertiesC.put("c", new RecordProperty("c", registry.getBooleanType()));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);
    RecordType recordTypeC = new RecordType(registry, propertiesC);

    JSType greatestSubtype = recordTypeA.getGreatestSubtype(recordTypeC);
    assertNotNull("The greatest subtype of recordTypeA and recordTypeC should not be null", greatestSubtype);
    assertTrue("The greatest subtype of recordTypeA and recordTypeC should be a record type", greatestSubtype instanceof RecordType);
    assertEquals("The greatest subtype of recordTypeA and recordTypeC should have the same properties as recordTypeA",
        propertiesA, greatestSubtype.getProperties());

    greatestSubtype = recordTypeB.getGreatestSubtype(recordTypeA);
    assertNull("The greatest subtype of recordTypeB and recordTypeA should be null", greatestSubtype);
  }
}