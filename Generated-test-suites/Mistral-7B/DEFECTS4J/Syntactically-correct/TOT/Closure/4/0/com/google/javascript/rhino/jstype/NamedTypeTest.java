import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.TestBase;
import com.google.javascript.rhino.testing.types.JSTypeRegistry;

import java.util.List;

public class NamedTypeTest extends TestBase {

  private static final String NAMED_TYPE_REFERENCE = "MyNamedType";
  private static final String NAMED_TYPE_REFERENCE_WITH_DOT =
      "MyNamedType.InnerType";
  private static final String ENUM_TYPE_NAME = "MyEnum";
  private static final String ENUM_ELEMENT_TYPE_NAME = "MyEnumElement";
  private static final String UNKNOWN_TYPE_REFERENCE = "UnknownType";

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    JSTypeRegistry registry = getTestContext().getTypeRegistry();
    registry.put(ENUM_TYPE_NAME, new EnumType(null, null, null, null, null));
  }

  public void testNamedTypeFromRegistry() {
    JSTypeRegistry registry = getTestContext().getTypeRegistry();
    JSType namedType = registry.getType(NAMED_TYPE_REFERENCE);
    assertTrue(namedType instanceof NamedType);
    assertEquals(NAMED_TYPE_REFERENCE, namedType.getReferenceName());
  }

  public void testNamedTypeFromProperties() {
    JSType registryType = new ObjectType(null, null);
    registryType.setPropertyType("MyNamedType", new NamedType(
        getTestContext().getTypeRegistry(), NAMED_TYPE_REFERENCE, null, 0, 0));

    StaticScope<JSType> scope = new StaticScope<>();
    JSType resolvedType = new NamedType(
        getTestContext().getTypeRegistry(), NAMED_TYPE_REFERENCE_WITH_DOT, null, 0, 0)
        .resolveInternal(new ErrorReporter(new StderrReporter()), scope);

    assertEquals(registryType, resolvedType);
  }

  public void testGetReferencedType() {
    JSTypeRegistry registry = getTestContext().getTypeRegistry();
    JSType namedType = new NamedType(
        registry, NAMED_TYPE_REFERENCE, null, 0, 0);

    // Unresolved
    assertNull(namedType.getReferencedType());

    registry.put(NAMED_TYPE_REFERENCE, new ObjectType(null, null));
    assertEquals(new ObjectType(null, null), namedType.getReferencedType());

    // EnumType
    registry.put(ENUM_TYPE_NAME, new EnumType(null, null, null, null, null));
    assertEquals(registry.getType(ENUM_TYPE_NAME), namedType.getReferencedType());

    // FunctionType
    registry.put(NAMED_TYPE_REFERENCE,
        new FunctionType(null, null, null, new List<JSType>(Lists.newArrayList())));
    assertEquals(
        new FunctionType(
            null, null, null, new List<JSType>(Lists.newArrayList())),
        namedType.getReferencedType());

    // ObjectType
    registry.put(NAMED_TYPE_REFERENCE, new ObjectType(null, null));
    assertEquals(new ObjectType(null, null), namedType.getReferencedType());

    // Unknown Type
    assertNull(registry.getType(UNKNOWN_TYPE_REFERENCE));
    namedType = new NamedType(
        registry, UNKNOWN_TYPE_REFERENCE, null, 0, 0);
    assertNull(namedType.getReferencedType());
  }

  public void testSetValidator() {
    JSTypeRegistry registry = getTestContext().getTypeRegistry();
    JSType namedType = new NamedType(
        registry, NAMED_TYPE_REFERENCE, null, 0, 0);

    // Validator should be applied when resolved
    Predicate<JSType> validator = (type) -> type instanceof FunctionType;
    namedType.setValidator(validator);

    StaticScope<JSType> scope = new StaticScope<>();
    JSType resolvedType = new NamedType(
        getTestContext().getTypeRegistry(), NAMED_TYPE_REFERENCE_WITH_DOT, null, 0, 0)
        .resolveInternal(new ErrorReporter(new StderrReporter()), scope);
    assertTrue(resolvedType instanceof FunctionType);

    // Validator should not be applied when unresolved
    assertEquals(new ObjectType(null, null), namedType.getReferencedType());
    assertFalse(namedType.isResolved());
    assertNull(namedType.getResolvedType());
  }

  public void testResolveWithEnumType() {
    JSTypeRegistry registry = getTestContext().getTypeRegistry();

    StaticScope<JSType> scope = new StaticScope<>();
    registry.put(ENUM_TYPE_NAME, new EnumType(null, null, null, null, null));

    // Resolve with a valid enum type
    JSType namedType = new NamedType(
        registry, ENUM_TYPE_NAME, null, 0, 0);
    StaticScope<JSType> innerScope = new StaticScope<>();
    JSType resolvedType = namedType
        .resolveInternal(new ErrorReporter(new StderrReporter()), innerScope);
    assertEquals(registry.getType(ENUM_TYPE_NAME), resolvedType);

    // Resolve with a non-existent enum type
    namedType = new NamedType(
        registry, "InvalidEnumType", null, 0, 0);
    resolvedType = namedType
        .resolveInternal(new ErrorReporter(new StderrReporter()), innerScope);
    assertNull(resolvedType);
  }

  public void testResolveWithEnumElementType() {
    JSTypeRegistry registry = getTestContext().getTypeRegistry();

    StaticScope<JSType> scope = new StaticScope<>();
    registry.put(ENUM_TYPE_NAME, new EnumType(null, null, null, null, null));
    registry.put(ENUM_ELEMENT_TYPE_NAME, new EnumElementType(
        null, null, null, null, registry.getType(ENUM_TYPE_NAME), null));

    // Resolve with a valid enum element type
    JSType namedType = new NamedType(
        registry, ENUM_ELEMENT_TYPE_NAME, null, 0, 0);
    StaticScope<JSType> innerScope = new StaticScope<>();
    JSType resolvedType = namedType
        .resolveInternal(new ErrorReporter(new StderrReporter()), innerScope);
    assertEquals(registry.getType(ENUM_ELEMENT_TYPE_NAME), resolvedType);

    // Resolve with a non-existent enum element type
    namedType = new NamedType(
        registry, "InvalidEnumElementType", null, 0, 0);
    resolvedType = namedType
        .resolveInternal(new ErrorReporter(new StderrReporter()), innerScope);
    assertNull(resolvedType);
  }

  public void testPropertyContinuations() {
    JSTypeRegistry registry = getTestContext().getTypeRegistry();

    StaticScope<JSType> scope = new StaticScope<>();
    JSType namedType = new NamedType(
        registry, "MyNamedType", null, 0, 0);

    namedType.defineProperty("prop1", new IntType(null), false, null);
    namedType.defineProperty("prop2", new StringType(null), false, null);

    StaticScope<JSType> innerScope = new StaticScope<>();

    // Resolve with a valid named type
    JSType resolvedType = namedType
        .resolveInternal(new ErrorReporter(new StderrReporter()), innerScope);
    assertTrue(resolvedType instanceof ObjectType);
    ObjectType objectType = (ObjectType) resolvedType;
    assertEquals(2, objectType.getPropertyCount());
    assertEquals("prop1", objectType.getPropertyNames().get(0));
    assertEquals("prop2", objectType.getPropertyNames().get(1));

    // Resolve with an unresolved named type
    namedType = new NamedType(
        registry, "MyUnresolvedNamedType", null, 0, 0);
    resolvedType = namedType
        .resolveInternal(new ErrorReporter(new StderrReporter()), innerScope);
    assertNull(resolvedType);
  }

  public void testPropertyContinuationsWithCycle() {
    JSTypeRegistry registry = getTestContext().getTypeRegistry();

    StaticScope<JSType> scope = new StaticScope<>();
    JSType namedType = new NamedType(
        registry, "MyNamedType", null, 0, 0);
    namedType.defineProperty("prop1", namedType, false, null);

    StaticScope<JSType> innerScope = new StaticScope<>();

    try {
      namedType.resolveInternal(new ErrorReporter(new StderrReporter()), innerScope);
      fail("Resolve should have failed due to a type cycle");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  public void testPropertyContinuationsWithInvalidType() {
    JSTypeRegistry registry = getTestContext().getTypeRegistry();

    StaticScope<JSType> scope = new StaticScope<>();
    JSType namedType = new NamedType(
        registry, "MyNamedType", null, 0, 0);
    namedType.defineProperty("prop1", new IntType(null), false, null);

    StaticScope<JSType> innerScope = new StaticScope<>();

    try {
      namedType.defineProperty("prop1", new StringType(null), false, null);
      fail("Assigning a wrong type to a property continuation should fail");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  public void testPropertyContinuationsWithNoType() {
    JSTypeRegistry registry = getTestContext().getTypeRegistry();

    StaticScope<JSType> scope = new StaticScope<>();
    JSType namedType = new NamedType(
        registry, "MyNamedType", null, 0, 0);

    StaticScope<JSType> innerScope = new StaticScope<>();
    namedType.defineProperty("prop1", null, false, null);

    try {
      namedType.resolveInternal(new ErrorReporter(new StderrReporter()), innerScope);
      fail("Assigning a null type to a property continuation should fail");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  public void testPropertyContinuationsWithForwardDeclaredType() {
    JSTypeRegistry registry = getTestContext().getTypeRegistry();
    registry.putForward(NAMED_TYPE_REFERENCE, new ObjectType(null, null));

    StaticScope<JSType> scope = new StaticScope<>();
    JSType namedType = new NamedType(
        registry, NAMED_TYPE_REFERENCE, null, 0, 0);
    namedType.defineProperty("prop1", new IntType(null), false, null);

    StaticScope<JSType> innerScope = new StaticScope<>();

    JSType resolvedType = namedType
        .resolveInternal(new ErrorReporter(new StderrReporter()), innerScope);
    assertTrue(resolvedType instanceof ObjectType);
    ObjectType objectType = (ObjectType) resolvedType;
    assertEquals(1, objectType.getPropertyCount());
    assertEquals("prop1", objectType.getPropertyNames().get(0));
  }
}