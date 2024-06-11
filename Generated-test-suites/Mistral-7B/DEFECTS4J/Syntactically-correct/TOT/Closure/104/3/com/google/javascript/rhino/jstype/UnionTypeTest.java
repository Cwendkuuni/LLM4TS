import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnionTypeTest {
  private JSTypeRegistry registry;

  @Before
  public void setup() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testUnionType() {
    Set<JSType> alternates = new LinkedHashSet<>();
    JSType stringType = registry.getStringType();
    JSType booleanType = registry.getBooleanType();
    JSType numberType = registry.getNumberType();

    alternates.add(stringType);
    alternates.add(booleanType);

    UnionType unionType = new UnionType(registry, alternates);

    // Test equality
    assertEquals(unionType, unionType);

    // Test inequality with same instance
    assertFalse(unionType.isSubtype(unionType));

    // Test inequality with different instance of the same union
    Set<JSType> otherAlternates = new LinkedHashSet<>(alternates);
    UnionType otherUnionType = new UnionType(registry, otherAlternates);
    assertFalse(unionType.isSubtype(otherUnionType));
    assertFalse(otherUnionType.isSubtype(unionType));

    // Test subtyping with a subtype of one of the alternates
    JSType stringSubtype = registry.getStringType().getLeastSupertype(registry.getNumberType());
    assertTrue(unionType.isSubtype(new UnionType(registry, Arrays.asList(stringSubtype))));

    // Test subtyping with a supertype of one of the alternates
    JSType numberSupertype = registry.getNumberType().getLeastSupertype(registry.getStringType(), registry.getBooleanType());
    assertTrue(new UnionType(registry, Arrays.asList(numberType)).isSubtype(unionType));

    // Test equality with restricted union
    UnionType restrictedUnion = unionType.getRestrictedUnion(numberType);
    assertEquals(new UnionType(registry, Arrays.asList(booleanType)), restrictedUnion);

    // Test inequality with a non-subtype
    JSType voidType = registry.getVoidType();
    assertFalse(unionType.isSubtype(voidType));

    // Test meet
    UnionType meetUnionType = unionType.meet(new UnionType(registry, Arrays.asList(booleanType)));
    assertEquals(unionType, meetUnionType);

    // Test toString
    assertEquals("(boolean, String)", unionType.toString());

    // Test canAssignTo
    JSType intType = registry.getIntType();
    assertTrue(unionType.canAssignTo(intType));

    // Test canBeCalled
    assertTrue(unionType.canBeCalled());

    // Test getLeastSupertype
    JSType anyType = registry.getAnyType();
    assertEquals(anyType, unionType.getLeastSupertype(anyType));

    // Test testForEquality
    UnionType otherUnionTypeWithNumber = new UnionType(registry, Arrays.asList(numberType, booleanType));
    assertEquals(UNKNOWN, unionType.testForEquality(otherUnionTypeWithNumber));

    // Test getAlternates
    assertEquals(alternates, unionType.getAlternates());

    // Test isNullable
    assertFalse(unionType.isNullable());

    // Test isUnknownType
    assertFalse(unionType.isUnknownType());
  }
}