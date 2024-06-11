import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.testing.JSTestCase;
import com.google.javascript.rhino.testing.PolymerTestRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UnionTypeTest extends JSTestCase {

  public static void main(String[] args) {
    PolymerTestRunner runner = new PolymerTestRunner();
    runner.run(UnionTypeTest.class);
  }

  /**
   * Test case for the UnionType class with two alternates: Number and String.
   */
  public void testUnionTypeNumberAndString() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> numberSet = new HashSet<>(Arrays.asList(registry.getNumberType(), registry.getStringType()));
    UnionType unionType = new UnionType(registry, numberSet);

    // Test matching Number context
    assertTrue(unionType.matchesNumberContext());

    // Test matching String context
    assertTrue(unionType.matchesStringContext());

    // Test finding a property type
    assertEquals(registry.getStringType(), unionType.findPropertyType("length"));

    // Test canAssignTo
    JSType numberType = registry.getNumberType();
    JSType stringType = registry.getStringType();
    assertTrue(unionType.canAssignTo(numberType));
    assertTrue(unionType.canAssignTo(stringType));

    // Test restriction by null or undefined
    UnionType restrictedUnionType = unionType.restrictByNotNullOrUndefined();
    assertTrue(restrictedUnionType.isSubtype(numberType));
    assertTrue(restrictedUnionType.isSubtype(stringType));
    assertFalse(restrictedUnionType.isSubtype(registry.getNullType()));
    assertFalse(restrictedUnionType.isSubtype(registry.getUndefinedType()));

    // Test canBeCalled
    assertTrue(unionType.canBeCalled());

    // Test getLeastSupertype
    JSType leastSupertype = unionType.getLeastSupertype(registry.getObjectType());
    assertEquals(registry.getObjectType(), leastSupertype);

    // Test meet
    UnionType meetUnionType = new UnionType(registry, Arrays.asList(numberType, stringType));
    JSType result = unionType.meet(meetUnionType);
    assertEquals(unionType, result);

    // Test equals and hashCode
    UnionType equalUnionType = new UnionType(registry, numberSet);
    assertEquals(unionType, equalUnionType);
    assertEquals(unionType.hashCode(), equalUnionType.hashCode());

    // Test isUnionType
    assertTrue(unionType.isUnionType());

    // Test isObject
    assertTrue(unionType.isObject());

    // Test contains
    Set<JSType> unionSet = new HashSet<>(Arrays.asList(numberType, stringType));
    assertTrue(unionType.contains(numberType));
    assertTrue(unionType.contains(stringType));
    assertFalse(unionType.contains(registry.getNullType()));

    // Test getRestrictedUnion
    JSType restrictedNumberType = registry.getNumberType();
    JSType restrictedStringType = registry.getStringType();
    UnionType restrictedUnionTypeNumber = unionType.getRestrictedUnion(restrictedNumberType);
    assertEquals(restrictedStringType, restrictedUnionTypeNumber);

    UnionType restrictedUnionTypeString = unionType.getRestrictedUnion(restrictedStringType);
    assertEquals(restrictedNumberType, restrictedUnionTypeString);

    // Test toString
    assertEquals("(number, string)", unionType.toString());

    // Test isSubtype
    assertTrue(unionType.isSubtype(numberType));
    assertTrue(unionType.isSubtype(stringType));
    assertTrue(numberType.isSubtype(unionType));
    assertTrue(stringType.isSubtype(unionType));

    // Test getPossibleToBooleanOutcomes
    BooleanLiteralSet booleanLiteralSet = unionType.getPossibleToBooleanOutcomes();
    assertTrue(booleanLiteralSet.contains(BooleanLiteral.TRUE));
    assertTrue(booleanLiteralSet.contains(BooleanLiteral.FALSE));

    // Test getTypesUnderEquality
    TypePair typePair = unionType.getTypesUnderEquality(numberType);
    assertNull(typePair.typeA);
    assertNull(typePair.typeB);

    typePair = unionType.getTypesUnderEquality(stringType);
    assertNull(typePair.typeA);
    assertNull(typePair.typeB);

    // Test getTypesUnderInequality
    typePair = unionType.getTypesUnderInequality(numberType);
    assertNull(typePair.typeA);
    assertNull(typePair.typeB);

    typePair = unionType.getTypesUnderInequality(stringType);
    assertNull(typePair.typeA);
    assertNull(typePair.typeB);

    // Test getTypesUnderShallowInequality
    typePair = unionType.getTypesUnderShallowInequality(numberType);
    assertNull(typePair.typeA);
    assertNull(typePair.typeB);

    typePair = unionType.getTypesUnderShallowInequality(stringType);
    assertNull(typePair.typeA);
    assertNull(typePair.typeB);

    // Test visit
    assertEquals(unionType, unionType.visit(new Visitor<JSType>() {
      @Override
      public JSType visitArrayType(ArrayType arrayType) {
        fail("Should not visit ArrayType");
        return null;
      }

      @Override
      public JSType visitBooleanType(BooleanType booleanType) {
        fail("Should not visit BooleanType");
        return null;
      }

      @Override
      public JSType visitFunctionType(FunctionType functionType) {
        fail("Should not visit FunctionType");
        return null;
      }

      @Override
      public JSType visitNullType(NullType nullType) {
        fail("Should not visit NullType");
        return null;
      }

      @Override
      public JSType visitNumberType(NumberType numberType) {
        fail("Should not visit NumberType");
        return null;
      }

      @Override
      public JSType visitObjectType(ObjectType objectType) {
        fail("Should not visit ObjectType");
        return null;
      }

      @Override
      public JSType visitStringType(StringType stringType) {
        fail("Should not visit StringType");
        return null;
      }

      @Override
      public JSType visitUnionType(UnionType unionType) {
        return unionType;
      }

      @Override
      public JSType visitVoidType(VoidType voidType) {
        fail("Should not visit VoidType");
        return null;
      }
    }));
  }
}