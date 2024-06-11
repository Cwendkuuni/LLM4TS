import static com.google.javascript.rhino.jstype.JSTypeFactory.booleanType;
import static com.google.javascript.rhino.jstype.JSTypeFactory.numberType;
import static com.google.javascript.rhino.jstype.JSTypeFactory.stringType;
import static org.junit.Assert.assertEquals;

import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeFactory;
import com.google.javascript.rhino.jstype.UnionType;

import org.junit.Test;

public class UnionTypeTest {

  @Test
  public void testStringBooleanUnionType() {
    JSType stringType = JSTypeFactory.stringType();
    JSType booleanType = JSTypeFactory.booleanType();
    UnionType stringBooleanUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(stringType, booleanType));

    // Test string context
    assertEquals("string", stringBooleanUnionType.matchesStringContext() ? "pass" : "fail");

    // Test boolean context
    assertEquals("boolean", booleanType.matchesStringContext() ? "pass" : "fail");
    assertEquals("boolean", stringBooleanUnionType.matchesStringContext() ? "pass" : "fail");

    // Test number context
    assertEquals("false", stringBooleanUnionType.matchesNumberContext() ? "pass" : "fail");

    // Test object context
    assertEquals("true", stringBooleanUnionType.matchesObjectContext() ? "pass" : "fail");

    // Test property access
    UnionType propertyAccessTest = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(stringType, booleanType));
    assertEquals("string", propertyAccessTest.findPropertyType("length").getName());

    // Test assignment
    JSType testType = JSTypeFactory.anyType();
    assertEquals("true", stringBooleanUnionType.canAssignTo(testType) ? "pass" : "fail");

    // Test callability
    assertEquals("true", stringBooleanUnionType.canBeCalled() ? "pass" : "fail");

    // Test restrict by not null or undefined
    UnionType nullableStringBooleanUnionType = stringBooleanUnionType.restrictByNotNullOrUndefined();
    assertEquals("false", nullableStringBooleanUnionType.isNullable() ? "pass" : "fail");

    // Test equality
    UnionType testUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(booleanType, stringType));
    assertEquals(stringBooleanUnionType, stringBooleanUnionType.testForEquality(testUnionType) ? "pass" : "fail");

    // Test restriction
    UnionType restricted = stringBooleanUnionType.restrictByNotNullOrUndefined();
    assertEquals("string", restricted.getAlternates().iterator().next().getName());
  }

  @Test
  public void testStringNumberUnionType() {
    JSType stringType = JSTypeFactory.stringType();
    JSType numberType = JSTypeFactory.numberType();
    UnionType stringNumberUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(stringType, numberType));

    // Test string context
    assertEquals("string", stringNumberUnionType.matchesStringContext() ? "pass" : "fail");

    // Test number context
    assertEquals("number", numberType.matchesStringContext() ? "pass" : "fail");
    assertEquals("number", stringNumberUnionType.matchesStringContext() ? "pass" : "fail");

    // Test boolean context
    assertEquals("false", stringNumberUnionType.matchesNumberContext() ? "pass" : "fail");

    // Test object context
    assertEquals("true", stringNumberUnionType.matchesObjectContext() ? "pass" : "fail");

    // Test property access
    UnionType propertyAccessTest = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(stringType, numberType));
    assertEquals("number", propertyAccessTest.findPropertyType("length").getName());

    // Test assignment
    JSType testType = JSTypeFactory.anyType();
    assertEquals("true", stringNumberUnionType.canAssignTo(testType) ? "pass" : "fail");

    // Test callability
    assertEquals("true", stringNumberUnionType.canBeCalled() ? "pass" : "fail");

    // Test restrict by not null or undefined
    UnionType nullableStringNumberUnionType = stringNumberUnionType.restrictByNotNullOrUndefined();
    assertEquals("number", nullableStringNumberUnionType.getAlternates().iterator().next().getName());

    // Test equality
    UnionType testUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(numberType, stringType));
    assertEquals(stringNumberUnionType, stringNumberUnionType.testForEquality(testUnionType) ? "pass" : "fail");

    // Test restriction
    UnionType restricted = stringNumberUnionType.restrictByNotNullOrUndefined();
    assertEquals("number", restricted.getAlternates().iterator().next().getName());
  }
}