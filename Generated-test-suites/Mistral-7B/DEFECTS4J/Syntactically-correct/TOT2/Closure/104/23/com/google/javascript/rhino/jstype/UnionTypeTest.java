import static org.junit.Assert.*;
import static com.google.javascript.rhino.testing.TypeChecker.checkType;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.testing.TypeChecker;

import java.util.Set;

import org.junit.Test;

public class UnionTypeTest {

 @Test
 public void testUnionTypeCreation() {
 UnionType stringOrNumberType = new UnionType(TypeChecker.registry(),
 ImmutableSet.of(TypeChecker.stringType(), TypeChecker.numberType()));
 assertEquals("(String, Number)", stringOrNumberType.toString());
 }

 @Test
 public void testUnionTypeGetAlternates() {
 UnionType stringOrNumberType = new UnionType(TypeChecker.registry(),
 ImmutableSet.of(TypeChecker.stringType(), TypeChecker.numberType()));
 Set<JSType> alternates = stringOrNumberType.getAlternates();
 assertTrue(alternates.contains(TypeChecker.stringType()));
 assertTrue(alternates.contains(TypeChecker.numberType()));
 }

 @Test
 public void testUnionTypeMatchesContext() {
 UnionType stringOrNumberType = new UnionType(TypeChecker.registry(),
 ImmutableSet.of(TypeChecker.stringType(), TypeChecker.numberType()));

 // String context
 String str = "hello";
 assertEquals(TypeChecker.stringType(), stringOrNumberType.matchesStringContext());
 assertEquals(TypeChecker.stringType(), stringOrNumberType.findPropertyType("length"));

 // Number context
 int num = 42;
 assertEquals(TypeChecker.numberType(), stringOrNumberType.matchesNumberContext());
 assertEquals(TypeChecker.numberType(), stringOrNumberType.findPropertyType("toString"));
 }

 @Test
 public void testUnionTypeCanAssign() {
 UnionType stringOrNumberType = new UnionType(TypeChecker.registry(),
 ImmutableSet.of(TypeChecker.stringType(), TypeChecker.numberType()));

 // String to Number
 boolean result = stringOrNumberType.canAssignTo(TypeChecker.numberType());
 assertTrue(result);

 // Number to String
 result = TypeChecker.numberType().canAssignTo(stringOrNumberType);
 assertTrue(result);

 // String to String
 result = stringOrNumberType.canAssignTo(stringOrNumberType);
 assertTrue(result);

 // Number to Number
 result = TypeChecker.numberType().canAssignTo(TypeChecker.numberType());
 assertTrue(result);
 }

 @Test
 public void testUnionTypeSubtype() {
 UnionType stringOrNumberType = new UnionType(TypeChecker.registry(),
 ImmutableSet.of(TypeChecker.stringType(), TypeChecker.numberType()));
 UnionType numberOrStringType = new UnionType(TypeChecker.registry(),
 ImmutableSet.of(TypeChecker.numberType(), TypeChecker.stringType()));

 assertTrue(stringOrNumberType.isSubtype(numberOrStringType));
 assertTrue(numberOrStringType.isSubtype(stringOrNumberType));
 }

 @Test(expected = AssertionError.class)
 public void testUnionTypeInvalidAssignment() {
 UnionType stringOrNumberType = new UnionType(TypeChecker.registry(),
 ImmutableSet.of(TypeChecker.stringType(), TypeChecker.numberType()));

 // String to Boolean
 assertFalse(stringOrNumberType.canAssignTo(TypeChecker.booleanType()));

 // Boolean to String
 assertFalse(TypeChecker.booleanType().canAssignTo(stringOrNumberType));

 // String to Null
 assertFalse(stringOrNumberType.canAssignTo(TypeChecker.nullType()));

 // Null to String
 assertFalse(TypeChecker.nullType().canAssignTo(stringOrNumberType));
 }

 @Test(expected = AssertionError.class)
 public void testUnionTypeInvalidCall() {
 UnionType stringOrNumberType = new UnionType(TypeChecker.registry(),
 ImmutableSet.of(TypeChecker.stringType(), TypeChecker.numberType()));

 // String call method
 assertFalse(stringOrNumberType.canBeCalled());

 // Number call method
 assertFalse(stringOrNumberType.canBeCalled());
 }

 @Test(expected = AssertionError.class)
 public void testUnionTypeInvalidPropertyAccess() {
 UnionType stringOrNumberType = new UnionType(TypeChecker.registry(),
 ImmutableSet.of(TypeChecker.stringType(), TypeChecker.numberType()));

 // String property access
 assertNull(stringOrNumberType.findPropertyType("length"));

 // Number property access
 assertNull(stringOrNumberType.findPropertyType("toString"));
 }

 @Test(expected = AssertionError.class)
 public void testUnionTypeInvalidEquality() {
 UnionType stringOrNumberType = new UnionType(TypeChecker.registry(),
 ImmutableSet.of(TypeChecker.stringType(), TypeChecker.numberType()));
 UnionType stringOrBooleanType = new UnionType(TypeChecker.registry(),
 ImmutableSet.of(TypeChecker.stringType(), TypeChecker.booleanType()));
 Boolean result = stringOrNumberType.testForEquality(stringOrBooleanType);
 assertNotNull(result);
 assertFalse(result.isTrue());
 }

 @Test
 public void testUnionTypeRestrictedBySubtype() {
 UnionType stringOrNumberType = new UnionType(TypeChecker.registry(),
 ImmutableSet.of(TypeChecker.stringType(), TypeChecker.numberType()));
 UnionType stringOrBooleanType = new UnionType(TypeChecker.registry(),
 ImmutableSet.of(TypeChecker.stringType(), TypeChecker.booleanType()));
 JSType restrictedType = stringOrNumberType.getRestrictedUnion(TypeChecker.numberType());
 assertFalse(stringOrBooleanType.isSubtype(restrictedType));
 assertFalse(restrictedType.isSubtype(stringOrBooleanType));
 assertEquals(TypeChecker.stringType(), restrictedType.getLeastSupertype(TypeChecker.stringType()));
 assertEquals(TypeChecker.numberType(), restrictedType.getLeastSupertype(TypeChecker.numberType()));
 }
}