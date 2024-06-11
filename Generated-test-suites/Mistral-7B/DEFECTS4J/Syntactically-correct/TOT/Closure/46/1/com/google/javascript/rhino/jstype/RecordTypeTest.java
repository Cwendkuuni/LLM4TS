import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.TypeCheckerTestingUtils;
import com.google.javascript.rhino.testing.ValueChecker;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = TypeCheckerTestingUtils.createRegistry();

    // Create a record type with two properties: 'a' of type Number and 'b' of type String.
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty(registry, "a", JSType.NUMBER));
    properties.put("b", new RecordProperty(registry, "b", JSType.STRING));
    JSType recordType = new RecordType(registry, properties);

    // Check the record type's properties.
    SortedMap<String, JSType> propertiesMap = recordType.properties;
    JSType aType = propertiesMap.get("a");
    JSType bType = propertiesMap.get("b");
    assertThat(aType).isInstanceOf(JSType.Number.class);
    assertThat(bType).isInstanceOf(JSType.String.class);
  }

  @Test
  public void testRecordTypeSubtyping() {
    JSTypeRegistry registry = TypeCheckerTestingUtils.createRegistry();

    // Create a record type 'A' with properties 'a' of type Number and 'b' of type String.
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty(registry, "a", JSType.NUMBER));
    propertiesA.put("b", new RecordProperty(registry, "b", JSType.STRING));
    JSType recordTypeA = new RecordType(registry, propertiesA);

    // Create a record type 'B' with properties 'a' of type Number and 'b' of type String,
    // and an additional property 'c' of type Boolean.
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty(registry, "a", JSType.NUMBER));
    propertiesB.put("b", new RecordProperty(registry, "b", JSType.STRING));
    propertiesB.put("c", new RecordProperty(registry, "c", JSType.BOOLEAN));
    JSType recordTypeB = new RecordType(registry, propertiesB);

    // Check that 'A' is a supertype of 'B'.
    JSType leastCommonSupertype = recordTypeA.getLeastSupertype(recordTypeB);
    JSType greatestSubtype = recordTypeB.getGreatestSubtypeHelper(recordTypeA);
    assertThat(leastCommonSupertype).isEqualTo(greestSubtype);
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = TypeCheckerTestingUtils.createRegistry();

    // Create two similar record types 'A' and 'B'.
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty(registry, "a", JSType.NUMBER));
    propertiesA.put("b", new RecordProperty(registry, "b", JSType.STRING));
    JSType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty(registry, "a", JSType.NUMBER));
    propertiesB.put("b", new RecordProperty(registry, "b", JSType.STRING));
    propertiesB.put("c", new RecordProperty(registry, "c", JSType.BOOLEAN));
    JSType recordTypeB = new RecordType(registry, propertiesB);

    // Check that 'A' and 'B' are not equivalent.
    JSType aEqualsB = recordTypeA.isEquivalentTo(recordTypeB);
    assertThat(aEqualsB).isFalse();
  }

  @Test
  public void testRecordTypeAssignment() {
    JSTypeRegistry registry = TypeCheckerTestingUtils.createRegistry();

    // Create a record type 'A' with properties 'a' of type Number and 'b' of type String.
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty(registry, "a", JSType.NUMBER));
    propertiesA.put("b", new RecordProperty(registry, "b", JSType.STRING));
    JSType recordTypeA = new RecordType(registry, propertiesA);

    // Create a record type 'B' with properties 'a' and 'c', where 'a' is of type 'A' and 'c' is of type Boolean.
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty(registry, "a", recordTypeA));
    propertiesB.put("c", new RecordProperty(registry, "c", JSType.BOOLEAN));
    JSType recordTypeB = new RecordType(registry, propertiesB);

    // Check that an object of type 'A' can be assigned to a variable of type 'B'.
    JSType assignmentResult = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
        .getLeastSupertype(recordTypeB);
    JSType assignedObject = registry.newObject(assignmentResult);
    JSType assignedValue = recordTypeA.resolve(null, null);
    JSType assignmentResultType = assignedObject.set("a", assignedValue)
        .set("b", ValueChecker.createStringValue("test"))
        .set("c", ValueChecker.createBooleanValue(true));

    JSType expectedType = recordTypeB.resolve(null, null);
    assertThat(assignmentResultType).isEqualTo(expectedType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRecordTypeFreeze() {
    JSTypeRegistry registry = TypeCheckerTestingUtils.createRegistry();

    // Create a record type with two properties: 'a' of type Number and 'b' of type String.
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty(registry, "a", JSType.NUMBER));
    properties.put("b", new RecordProperty(registry, "b", JSType.STRING));
    JSType recordType = new RecordType(registry, properties);

    // Attempt to modify the record type after it's frozen.
    recordType.defineProperty("c", JSType.BOOLEAN, false, null);
  }
}