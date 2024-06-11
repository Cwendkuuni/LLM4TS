import com.google.javascript.rhino.testing.JSTestCase;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.UnionType;

public class UnionTypeTest extends JSTestCase {

  public void testUnionTypeBasic() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> stringSet = ImmutableSet.of(registry.stringType(), registry.booleanType());
    UnionType unionType = new UnionType(registry, stringSet);

    assertEquals(unionType.getAlternates(), stringSet);
    assertTrue(unionType.matchesNumberContext());
    assertTrue(unionType.matchesStringContext());
    assertTrue(unionType.matchesObjectContext());

    JSType stringType = unionType.findPropertyType("toString");
    assertEquals(registry.stringType(), stringType);

    JSType booleanType = unionType.findPropertyType("valueOf");
    assertEquals(registry.booleanType(), booleanType);

    assertFalse(unionType.canAssignTo(registry.numberType())); // matchesNumberContext() is not enough
    assertFalse(unionType.canAssignTo(registry.objectType())); // matchesObjectContext() is not enough

    assertTrue(unionType.canAssignTo(unionType));
    assertTrue(unionType.canAssignTo(registry.stringType()));
    assertTrue(unionType.canAssignTo(registry.booleanType()));

    assertTrue(unionType.canBeCalled());

    assertEquals(unionType.toString(), "(String|boolean)");

    UnionType restrictedType = unionType.restrictByNotNullOrUndefined();
    assertEquals(registry.booleanType(), restrictedType.getAlternates().iterator().next());

    UnionType restrictedTypeByString = unionType.getRestrictedUnion(registry.stringType());
    assertEquals(registry.stringType(), restrictedTypeByString.getAlternates().iterator().next());

    UnionType restrictedTypeByNull = unionType.getRestrictedUnion(registry.nullType());
    assertTrue(restrictedTypeByNull.getAlternates().isEmpty());

    UnionType restrictedTypeByError = unionType.getRestrictedUnion(registry.errorType());
    assertTrue(restrictedTypeByError.getAlternates().isEmpty());
  }

  public void testUnionTypeNested() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> stringSet = ImmutableSet.of(registry.stringType(), registry.booleanType());
    Set<JSType> numberSet = ImmutableSet.of(registry.numberType(), registry.booleanType());

    UnionType unionType = new UnionType(registry, unionSet);
    UnionType nestedUnionType = new UnionType(registry, numberSet);

    Set<JSType> unionSetWithNested = new ImmutableSet.Builder<JSType>()
        .addAll(unionSet)
        .add(nestedUnionType)
        .build();

    UnionType nestedUnionInUnionType = new UnionType(registry, unionSetWithNested);

    assertEquals(nestedUnionInUnionType.getAlternates(), unionSetWithNested);
  }

  public void testUnionTypeEquality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> stringSet = ImmutableSet.of(registry.stringType(), registry.booleanType());
    UnionType unionType1 = new UnionType(registry, stringSet);
    UnionType unionType2 = new UnionType(registry, stringSet);

    assertEquals(unionType1, unionType2);
  }

  public void testUnionTypeInequality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> stringSet = ImmutableSet.of(registry.stringType(), registry.booleanType());
    UnionType unionType1 = new UnionType(registry, stringSet);
    UnionType numberType = registry.numberType();

    assertFalse(unionType1.equals(numberType));
  }

  public void testUnionTypeHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> stringSet = ImmutableSet.of(registry.stringType(), registry.booleanType());
    UnionType unionType1 = new UnionType(registry, stringSet);
    UnionType unionType2 = new UnionType(registry, stringSet);

    assertEquals(unionType1.hashCode(), unionType2.hashCode());
  }

  public void testUnionTypeRestriction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> stringSet = ImmutableSet.of(registry.stringType(), registry.booleanType());
    UnionType unionType = new UnionType(registry, stringSet);
    JSType booleanType = unionType.getAlternates().iterator().next();

    UnionType restrictedType = unionType.restrictByNotNullOrUndefined();
    assertFalse(restrictedType.getAlternates().contains(booleanType));
  }

  public void testUnionTypeRestrictionByType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> stringSet = ImmutableSet.of(registry.stringType(), registry.booleanType());
    UnionType unionType = new UnionType(registry, stringSet);

    UnionType restrictedTypeByString = unionType.getRestrictedUnion(registry.stringType());
    assertEquals(registry.stringType(), restrictedTypeByString.getAlternates().iterator().next());

    UnionType restrictedTypeByNumber = unionType.getRestrictedUnion(registry.numberType());
    assertTrue(restrictedTypeByNumber.getAlternates().isEmpty());
  }

  public void testUnionTypeRestrictionByNull() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> stringSet = ImmutableSet.of(registry.stringType(), registry.booleanType());
    UnionType unionType = new UnionType(registry, stringSet);

    UnionType restrictedTypeByNull = unionType.getRestrictedUnion(registry.nullType());
    assertTrue(restrictedTypeByNull.getAlternates().isEmpty());
  }

  public void testUnionTypeRestrictionByError() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> stringSet = ImmutableSet.of(registry.stringType(), registry.booleanType());
    UnionType unionType = new UnionType(registry, stringSet);

    UnionType restrictedTypeByError = unionType.getRestrictedUnion(registry.errorType());
    assertTrue(restrictedTypeByError.getAlternates().isEmpty());
  }

  public void testUnionTypeRestrictionByObject() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> stringSet = ImmutableSet.of(registry.stringType(), registry.booleanType());
    UnionType unionType = new UnionType(registry, stringSet);

    UnionType restrictedTypeByObject = unionType.getRestrictedUnion(registry.objectType());
    assertEquals(registry.noType(), restrictedTypeByObject);
  }

  public void testUnionTypeRestrictionByUndefined() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> stringSet = ImmutableSet.of(registry.stringType(), registry.booleanType());
    UnionType unionType = new UnionType(registry, stringSet);

    UnionType restrictedTypeByUndefined = unionType.getRestrictedUnion(registry.undefinedType());
    assertEquals(registry.undefinedType(), restrictedTypeByUndefined);
  }

  public void testUnionTypeRestrictionByFunction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> stringSet = ImmutableSet.of(registry.stringType(), registry.booleanType());
    UnionType unionType = new UnionType(registry, stringSet);

    UnionType restrictedTypeByFunction = unionType.getRestrictedUnion(registry.functionType());
    assertEquals(registry.noType(), restrictedTypeByFunction);
  }

  public void testUnionTypeRestrictionByArray() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> stringSet = ImmutableSet.of(registry.stringType(), registry.booleanType());
    UnionType unionType = new UnionType(registry, stringSet);

    UnionType restrictedTypeByArray = unionType.getRestrictedUnion(registry.arrayType());
    assertEquals(registry.noType(), restrictedTypeByArray);
  }

  public void testUnionTypeRestrictionByNativeObject() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> stringSet = ImmutableSet.of(registry.stringType(), registry.booleanType());
    UnionType unionType = new UnionType(registry, stringSet);

    UnionType restrictedTypeByNativeObject = unionType.getRestrictedUnion(registry.noObjectType());
    assertEquals(registry.noObjectType(), restrictedTypeByNativeObject);
  }

  public void testUnionTypeRestrictionByNativeFunction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> stringSet = ImmutableSet.of(registry.stringType(), registry.booleanType());
    UnionType unionType = new UnionType(registry, stringSet);

    UnionType restrictedTypeByNativeFunction = unionType.getRestrictedUnion(registry.noFunctionType());
    assertEquals(registry.noType(), restrictedTypeByNativeFunction);
  }

  public void testUnionTypeRestrictionByNativeArray() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> stringSet = ImmutableSet.of(registry.stringType(), registry.booleanType());
    UnionType unionType = new UnionType(registry, stringSet);

    UnionType restrictedTypeByNativeArray = unionType.getRestrictedUnion(registry.noArrayType());
    assertEquals(registry.noType(), restrictedTypeByNativeArray);
  }
}