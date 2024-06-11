import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.common.collect.ImmutableSet;

import org.junit.Test;

public class UnionTypeTest {

  @Test
  public void testUnionTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.stringType();
    JSType booleanType = registry.booleanType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, booleanType));
    assertEquals("(String, Boolean)", unionType.toString());
  }

  @Test
  public void testUnionTypeGetters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.stringType();
    JSType booleanType = registry.booleanType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, booleanType));

    assertEquals(ImmutableSet.of(stringType, booleanType), unionType.getAlternates());
    assertEquals(true, unionType.matchesNumberContext());
    assertEquals(true, unionType.matchesStringContext());
    assertEquals(true, unionType.matchesObjectContext());
  }

  @Test
  public void testUnionTypeRestriction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.stringType();
    JSType numberType = registry.numberType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));
    UnionType restrictedUnionType = unionType.restrictByNotNullOrUndefined();

    assertEquals(ImmutableSet.of(), restrictedUnionType.getAlternates());
  }

  @Test
  public void testUnionTypeMeet() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.stringType();
    JSType numberType = registry.numberType();
    UnionType unionTypeA = new UnionType(registry, ImmutableSet.of(stringType));
    UnionType unionTypeB = new UnionType(registry, ImmutableSet.of(numberType));
    UnionType meetType = unionTypeA.meet(unionTypeB);

    assertEquals(ImmutableSet.of(), meetType.getAlternates());
  }

  @Test
  public void testUnionTypeEquality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.stringType();
    JSType numberType = registry.numberType();
    UnionType unionTypeA = new UnionType(registry, ImmutableSet.of(stringType));
    UnionType unionTypeB = new UnionType(registry, ImmutableSet.of(numberType));
    UnionType unionTypeC = new UnionType(registry, ImmutableSet.of(stringType, numberType));

    assertEquals(unionTypeC, unionTypeA.meet(unionTypeB));
    assertTrue(unionTypeA.equals(unionTypeC));
    assertFalse(unionTypeA.equals(unionTypeB));
  }

  @Test
  public void testUnionTypeFindPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.stringType();
    JSType numberType = registry.numberType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));
    Type unionTypeType = unionType.getType();
    JSType propertyType = unionType.findPropertyType("length");

    assertEquals(numberType, propertyType);
  }

  @Test
  public void testUnionTypeCanAssignTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.stringType();
    JSType numberType = registry.numberType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));

    JSType stringInstance = registry.getGlobalVar("String");
    JSType numberInstance = registry.getGlobalVar("Number");

    assertTrue(unionType.canAssignTo(stringInstance));
    assertTrue(unionType.canAssignTo(numberInstance));
  }

  @Test
  public void testUnionTypeCanBeCalled() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType functionType = new FunctionType(registry, ImmutableSet.of(registry.stringType(), registry.booleanType()));
    UnionType unionType = new UnionType(registry, ImmutableSet.of(functionType));

    assertTrue(unionType.canBeCalled());
  }

  @Test
  public void testUnionTypeIsNullable() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType nullType = registry.nullType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(nullType));

    assertFalse(unionType.isNullable());
  }

  @Test
  public void testUnionTypeIsUnknownType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    UnionType unionType = new UnionType(registry, ImmutableSet.of());

    assertTrue(unionType.isUnknownType());
  }

  @Test
  public void testUnionTypeGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.stringType();
    JSType numberType = registry.numberType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));
    JSType objectType = registry.objectType();

    assertEquals(objectType, unionType.getLeastSupertype(objectType));
  }
}