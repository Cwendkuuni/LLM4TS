import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.UnionType;

import org.junit.Test;

import java.util.Set;

public class UnionTypeTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testUnionTypeBasicFunctionality() {
    Set<JSType> alternates = ImmutableSet.of(registry.findNamedType("String"), registry.findNamedType("Boolean"));
    UnionType unionType = new UnionType(registry, alternates);

    assertEquals("(String|Boolean)", unionType.toString());
    assertTrue(unionType.matchesStringContext());
    assertTrue(unionType.matchesObjectContext());
    assertFalse(unionType.matchesNumberContext());

    JSType stringType = registry.findNamedType("String");
    JSType booleanType = registry.findNamedType("Boolean");

    assertEquals(stringType, unionType.findPropertyType("length"));
    assertEquals(booleanType, unionType.findPropertyType("toString"));
  }

  @Test
  public void testUnionTypeEdgeCasesAndExceptions() {
    Set<JSType> alternates = ImmutableSet.of(registry.findNamedType("String"), registry.findNamedType("Number"));
    UnionType unionType = new UnionType(registry, alternates);

    try {
      unionType.canAssignTo(registry.findNamedType("Boolean"));
      fail("Should have thrown an exception");
    } catch (AssertionError e) {
      // Expected
    }

    try {
      unionType.canAssignTo(registry.findNamedType("Object"));
      fail("Should have thrown an exception");
    } catch (AssertionError e) {
      // Expected
    }

    assertFalse(unionType.canAssignTo(registry.findNamedType("NullType")));

    try {
      unionType.getAlternates().add(registry.findNamedType("NullType"));
    } catch (UnsupportedOperationException e) {
      // Expected
    }

    unionType.forgiveUnknownNames();

    assertEquals(registry.findNamedType("String"), unionType.getAlternates().iterator().next());
    assertEquals(registry.findNamedType("Number"), unionType.getAlternates().iterator().next());
  }
}