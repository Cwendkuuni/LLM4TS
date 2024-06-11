import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.*;
import static com.google.common.collect.ImmutableSet.*;

import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

public class UnionTypeTest {

  @Test
  public void testUnionTypeCreation() {
    Set<JSType> alternates = of(NUMBER, STRING);
    UnionType type = new UnionType(new JSTypeRegistry(), alternates);
    assertEquals("(number, string)", type.toString());
  }

  @Test
  public void testGetAlternates() {
    Set<JSType> alternates = of(NUMBER, STRING);
    UnionType type = new UnionType(new JSTypeRegistry(), alternates);
    assertEquals(alternates, type.getAlternates());
  }

  @Test
  public void testMatchesNumberContext() {
    Set<JSType> alternates = of(NUMBER, STRING);
    UnionType type = new UnionType(new JSTypeRegistry(), alternates);
    assertTrue(type.matchesNumberContext());
  }

  @Test
  public void testMatchesStringContext() {
    Set<JSType> alternates = of(NUMBER, STRING);
    UnionType type = new UnionType(new JSTypeRegistry(), alternates);
    assertTrue(type.matchesStringContext());
  }

  @Test
  public void testMatchesObjectContext() {
    Set<JSType> alternates = of(NUMBER, STRING);
    UnionType type = new UnionType(new JSTypeRegistry(), alternates);
    assertTrue(type.matchesObjectContext());
  }

  @Test
  public void testFindPropertyType() {
    Set<JSType> alternates = of(NUMBER, STRING);
    UnionType type = new UnionType(new JSTypeRegistry(), alternates);

    assertEquals(NUMBER, type.findPropertyType("length"));
    assertEquals(STRING, type.findPropertyType("toString"));
    assertEquals(NULL, type.findPropertyType("toString")); // null case
  }

  @Test
  public void testCanAssignTo() {
    Set<JSType> alternates = of(NUMBER, STRING, NULL);
    UnionType type = new UnionType(new JSTypeRegistry(), alternates);

    assertTrue(type.canAssignTo(NUMBER));
    assertTrue(type.canAssignTo(STRING));
    assertTrue(type.canAssignTo(NULL));

    // test unknown type
    UnionType unknownType = new UnionType(new JSTypeRegistry(), of(UNKNOWN));
    assertTrue(unknownType.canAssignTo(NUMBER));
    assertTrue(unknownType.canAssignTo(STRING));
    assertTrue(unknownType.canAssignTo(NULL));
  }

  @Test
  public void testCanBeCalled() {
    Set<JSType> alternates = of(NUMBER, STRING, FUNCTION);
    UnionType type = new UnionType(new JSTypeRegistry(), alternates);
    assertTrue(type.canBeCalled());
  }

  @Test(expected = AssertionError.class)
  public void testCannotBeCalledWithVoidType() {
    Set<JSType> alternates = of(NUMBER, STRING, VOID);
    UnionType type = new UnionType(new JSTypeRegistry(), alternates);
    type.canBeCalled();
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    Set<JSType> alternates = of(NUMBER, STRING, NULL, UNDEFINED);
    UnionType type = new UnionType(new JSTypeRegistry(), alternates);
    UnionType restricted = type.restrictByNotNullOrUndefined();

    assertNotNull(restricted);
    assertFalse(restricted.contains(NULL));
    assertFalse(restricted.contains(UNDEFINED));
  }

  @Test
  public void testEquals() {
    Set<JSType> alternatesA = of(NUMBER, STRING);
    UnionType typeA = new UnionType(new JSTypeRegistry(), alternatesA);

    Set<JSType> alternatesB = of(STRING, NUMBER);
    UnionType typeB = new UnionType(new JSTypeRegistry(), alternatesB);

    assertEquals(typeA, typeA);
    assertEquals(typeA, typeA);
    assertNotEquals(typeA, typeB);
  }

  @Test
  public void testHashCode() {
    Set<JSType> alternates = of(NUMBER, STRING);
    UnionType typeA = new UnionType(new JSTypeRegistry(), alternates);
    UnionType typeB = new UnionType(new JSTypeRegistry(), alternates);

    int hashcodeA = typeA.hashCode();
    int hashcodeB = typeB.hashCode();

    assertEquals(hashcodeA, hashcodeB);
  }

  @Test
  public void testIsUnionType() {
    Set<JSType> alternates = of(NUMBER, STRING);
    UnionType type = new UnionType(new JSTypeRegistry(), alternates);
    assertTrue(type.isUnionType());
  }

  @Test
  public void testIsObject() {
    Set<JSType> alternates = of(NUMBER, STRING);
    UnionType type = new UnionType(new JSTypeRegistry(), alternates);
    assertTrue(type.isObject());
  }

  @Test
  public void testToString() {
    Set<JSType> alternates = of(NUMBER, STRING);
    UnionType type = new UnionType(new JSTypeRegistry(), alternates);

    assertEquals("(number, string)", type.toString());
  }

  @Test
  public void testIsSubtype() {
    Set<JSType> alternatesA = of(NUMBER, STRING);
    UnionType typeA = new UnionType(new JSTypeRegistry(), alternatesA);

    Set<JSType> alternatesB = of(STRING, NUMBER);
    UnionType typeB = new UnionType(new JSTypeRegistry(), alternatesB);

    assertTrue(typeA.isSubtype(typeB));
    assertFalse(typeB.isSubtype(typeA));
  }

  @Test
  public void testGetRestrictedUnion() {
    Set<JSType> alternates = of(NUMBER, STRING);
    UnionType type = new UnionType(new JSTypeRegistry(), alternates);

    JSType numberType = NUMBER;
    JSType stringType = STRING;

    assertEquals(type, type.getRestrictedUnion(NUMBER));
    assertEquals(type, type.getRestrictedUnion(STRING));
    assertEquals(NUMBER, type.getRestrictedUnion(NUMBER).getAlternates().iterator()
        .next());
    assertEquals(STRING, type.getRestrictedUnion(STRING).getAlternates().iterator()
        .next());
  }

  @Test
  public void testMeet() {
    Set<JSType> alternatesA = of(NUMBER, STRING);
    UnionType typeA = new UnionType(new JSTypeRegistry(), alternatesA);

    Set<JSType> alternatesB = of(STRING, NUMBER);
    UnionType typeB = new UnionType(new JSTypeRegistry(), alternatesB);

    UnionType result = typeA.meet(typeB);

    assertEquals(typeA, result);
    assertEquals(2, result.getAlternates().size());
    assertTrue(result.contains(NUMBER));
    assertTrue(result.contains(STRING));
  }

  @Test
  public void testGetTypesUnderEquality() {
    Set<JSType> alternatesA = of(NUMBER, STRING);
    UnionType typeA = new UnionType(new JSTypeRegistry(), alternatesA);

    Set<JSType> alternatesB = of(STRING, NUMBER);
    UnionType typeB = new UnionType(new JSTypeRegistry(), alternatesB);

    TypePair result = typeA.getTypesUnderEquality(typeB);

    assertEquals(typeA, result.typeA);
    assertEquals(typeB, result.typeB);
  }

  @Test
  public void testGetTypesUnderInequality() {
    Set<JSType> alternatesA = of(NUMBER, STRING);
    UnionType typeA = new UnionType(new JSTypeRegistry(), alternatesA);

    Set<JSType> alternatesB = of(STRING, NUMBER);
    UnionType typeB = new UnionType(new JSTypeRegistry(), alternatesB);

    TypePair result = typeA.getTypesUnderInequality(typeB);

    assertEquals(typeB, result.typeA);
    assertEquals(typeA, result.typeB);
  }

  @Test
  public void testGetTypesUnderShallowInequality() {
    Set<JSType> alternatesA = of(NUMBER, STRING);
    UnionType typeA = new UnionType(new JSTypeRegistry(), alternatesA);

    Set<JSType> alternatesB = of(NUMBER, STRING);
    UnionType typeB = new UnionType(new JSTypeRegistry(), alternatesB);

    TypePair result = typeA.getTypesUnderShallowInequality(typeB);

    assertEquals(typeA, result.typeA);
    assertEquals(typeB, result.typeB);
  }

  @Test
  public void testVisit() {
    Set<JSType> alternates = of(NUMBER, STRING);
    UnionType type = new UnionType(new JSTypeRegistry(), alternates);

    Object result = type.visit(new Visitor<Object>() {
      @Override
      public Object caseUnionType(UnionType unionType) {
        return Arrays.asList(unionType.toString(), unionType.getAlternates());
      }

      @Override
      public Object caseNullType(NullType nullType) {
        return nullType.toString();
      }

      @Override
      public Object caseUndefinedType(UndefinedType undefinedType) {
        return undefinedType.toString();
      }

      @Override
      public Object caseNumber(NumberType numberType) {
        return numberType.toString();
      }

      @Override
      public Object caseString(StringType stringType) {
        return stringType.toString();
      }

      @Override
      public Object caseFunction(FunctionType functionType) {
        return functionType.toString();
      }

      @Override
      public Object caseVoidType(VoidType voidType) {
        return voidType.toString();
      }
    });

    if (result != null) {
      assertTrue(result instanceof List);
      List list = (List) result;
      assertEquals(2, list.size());
      assertEquals("(number, string)", list.get(0));
      assertEquals(ImmutableSet.of(NUMBER, STRING), list.get(1));
    }
  }

  @Test
  public void testResolveInternal() {
    Set<JSType> alternates = of(NUMBER, STRING, UNDEFINED);
    UnionType type = new UnionType(new JSTypeRegistry(), alternates);

    ErrorReporter errorReporter = Mockito.mock(ErrorReporter.class);
    StaticScope<JSType> scope = new StaticScope<>();

    type.resolveInternal(errorReporter, scope);

    // no error reported
  }

  @Test(expected = AssertionError.class)
  public void testResolveInternalWithCircularDependency() {
    Set<JSType> alternates = Arrays.asList(
        new UnionType(new JSTypeRegistry(), of(NUMBER, STRING)),
        new UnionType(new JSTypeRegistry(), of(STRING, NUMBER))).stream()
        .map(type -> type).collect(Collectors.toSet());

    UnionType type = new UnionType(new JSTypeRegistry(), alternates);

    ErrorReporter errorReporter = Mockito.mock(ErrorReporter.class);
    StaticScope<JSType> scope = new StaticScope<>();

    type.resolveInternal(errorReporter, scope);
  }

  @Test
  public void testToStringWithEmptyAlternates() {
    UnionType type = new UnionType(new JSTypeRegistry(), of());

    assertEquals("()", type.toString());
  }

  @Test
  public void testToStringWithSingleAlternate() {
    Set<JSType> alternates = of(NUMBER);
    UnionType type = new UnionType(new JSTypeRegistry(), alternates);

    assertEquals("(number)", type.toString());
  }

  @Test
  public void testToStringWithMultipleAlternates() {
    Set<JSType> alternates = Arrays.asList(NUMBER, STRING).stream()
        .collect(Collectors.toSet());
    UnionType type = new UnionType(new JSTypeRegistry(), alternates);

    assertEquals("(number, string)", type.toString());
  }
}