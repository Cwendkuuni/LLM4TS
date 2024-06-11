import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeCoercer.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.UnionType;

import org.junit.Test;

public class UnionTypeTest {

  private static JSTypeRegistry registry = new JSTypeRegistry();
  static {
    registry.putTypeName("String", String.class);
    registry.putTypeName("Number", Double.class);
    registry.putTypeName("Boolean", Boolean.class);
    registry.putTypeName("Object", Object.class);
    registry.putTypeName("Null", NullType.getInstance());
    registry.putTypeName("Void", VoidType.getInstance());
  }

  @Test
  public void testUnionTypeCreationWithStringAndNumberAlternates() {
    Set<JSType> alternates = ImmutableSet.of(
        registry.getTypeByName("String"),
        registry.getTypeByName("Number")
    );
    UnionType unionType = new UnionType(registry, alternates);

    // Basic functionality test
    assertTrue(unionType.matchesStringContext());
    assertTrue(unionType.matchesNumberContext());

    // Edge cases and exception handling scenarios

    // Test nullability
    UnionType nullableUnionType = new UnionType(registry, ImmutableSet.of(
        registry.getTypeByName("String"),
        registry.getTypeByName("Null")
    ));
    assertFalse(nullableUnionType.isNullable());

    UnionType nullUnionType = new UnionType(registry, ImmutableSet.of(
        registry.getTypeByName("Null")
    ));
    assertTrue(nullUnionType.isNullable());

    UnionType voidUnionType = new UnionType(registry, ImmutableSet.of(
        registry.getTypeByName("Void")
    ));
    assertTrue(voidUnionType.isNullable());

    // Test unknown types
    UnionType unknownUnionType = new UnionType(registry, ImmutableSet.of(
        registry.getTypeByName("String"),
        registry.getTypeByName("Unknown")
    ));
    assertFalse(unknownUnionType.matchesStringContext());

    UnionType unknownUnknownUnionType = new UnionType(registry, ImmutableSet.of(
        registry.getTypeByName("Unknown"),
        registry.getTypeByName("Unknown")
    ));
    assertFalse(unknownUnknownUnionType.matchesStringContext());

    // Test subtyping
    UnionType stringNumberUnionType = new UnionType(registry, ImmutableSet.of(
        registry.getTypeByName("String"),
        registry.getTypeByName("Number")
    ));
    UnionType numberStringUnionType = new UnionType(registry, ImmutableSet.of(
        registry.getTypeByName("Number"),
        registry.getTypeByName("String")
    ));

    assertTrue(stringNumberUnionType.isSubtype(numberStringUnionType));
    assertTrue(numberStringUnionType.isSubtype(stringNumberUnionType));

    UnionType anyType = new UnionType(registry, ImmutableSet.of(
        registry.getTypeByName("Object")
    ));
    assertTrue(anyType.isSubtype(stringNumberUnionType));

    // Test type restriction
    UnionType restrictedStringNumberUnionType = stringNumberUnionType.getRestrictedUnion(
        registry.getTypeByName("Number")
    );
    assertFalse(restrictedStringNumberUnionType.matchesStringContext());

    UnionType restrictedAnyTypeUnionType = anyType.getRestrictedUnion(
        registry.getTypeByName("Number")
    );
    assertFalse(restrictedAnyTypeUnionType.matchesStringContext());

    // Test type intersection
    UnionType stringStringUnionType = new UnionType(registry, ImmutableSet.of(
        registry.getTypeByName("String"),
        registry.getTypeByName("String")
    ));
    UnionType stringNumberAndStringStringUnionType = new UnionType(registry, ImmutableSet.of(
        registry.getTypeByName("String"),
        registry.getTypeByName("Number"),
        registry.getTypeByName("String")
    ));

    assertTrue(stringStringUnionType.equals(stringNumberAndStringStringUnionType));

    UnionType unionTypeWithNullAlternate = new UnionType(registry, ImmutableSet.of(
        registry.getTypeByName("String"),
        registry.getTypeByName("Null")
    ));

    // Test canAssignTo
    assertTrue(stringNumberUnionType.canAssignTo(registry.getTypeByName("Number")));
    assertFalse(stringNumberUnionType.canAssignTo(registry.getTypeByName("Boolean")));

    // Test canBeCalled
    assertTrue(stringNumberUnionType.canBeCalled());

    // Test findPropertyType
    UnionType stringObjectUnionType = new UnionType(registry, ImmutableSet.of(
        registry.getTypeByName("String"),
        registry.getTypeByName("Object")
    ));

    assertEquals(registry.getTypeByName("Object"), stringObjectUnionType.findPropertyType("length"));
  }
}