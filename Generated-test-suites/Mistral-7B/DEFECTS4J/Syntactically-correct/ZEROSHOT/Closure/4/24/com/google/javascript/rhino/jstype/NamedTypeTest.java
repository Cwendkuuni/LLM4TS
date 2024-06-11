import static org.junit.Assert.*;
import static com.google.javascript.rhino.testing.TestUtilities.newErrorReporter;
import static com.google.javascript.rhino.testing.TypeFactory.newInstance;
import static com.google.javascript.rhino.testing.TypeFactory.toType;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.StaticScope;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ProxyObjectType;
import com.google.javascript.rhino.TypeFactory;
import com.google.javascript.rhino.testing.TestBase;
import com.google.javascript.rhino.testing.TypeRegistry;

import java.util.List;

public class NamedTypeTest extends TestBase {

  private static class TestNamedTypeRegistry extends TypeRegistry {
    @Override
    JSType getNativeObjectType(JSTypeNative type) {
      return super.getNativeObjectType(type);
    }

    @Override
    JSType getNativeFunctionType(JSTypeNative type) {
      return newInstance(FunctionType.class, type);
    }

    @Override
    JSType getNativeFunctionType(JSType type, boolean isConstructor) {
      return newInstance(FunctionType.class, type);
    }

    @Override
    JSType getNativeEnumType(String name, JSType prototype) {
      return newInstance(EnumType.class, name, prototype);
    }

    @Override
    JSType getNativeEnumElementType(String name, JSType enumType) {
      return newInstance(EnumElementType.class, name, enumType);
    }

    @Override
    JSType getType(String name) {
      switch (name) {
        case "String":
          return toType(String.class);
        case "Number":
          return toType(Number.class);
        case "Boolean":
          return toType(Boolean.class);
        case "Array":
          return newInstance(ArrayType.class, toType(Object.class));
        case "Object":
          return toType(Object.class);
        case "Function":
          return newInstance(FunctionType.class, toType(Object.class),
              toType(Object[].class), false);
        case "NamedType":
          return newInstance(NamedType.class, this, "test", 1, 1);
        default:
          return null;
      }
    }
  }

  @BeforeClass
  public static void setup() {
    TypeRegistry registry = new TestNamedTypeRegistry();
    TestBase.setCurrentGlobal(newTypeScope(registry));
  }

  @AfterClass
  public static void teardown() {
    TestBase.setCurrentGlobal(null);
  }

  @Test
  public void testNamedTypeCreation() {
    JSType namedType = newInstance(NamedType.class, registry, "NamedType",
        "Test", 1, 1);
    assertEquals("NamedType", namedType.getReferenceName());
    assertEquals("Test.NamedType", namedType.toString());
  }

  @Test
  public void testNamedTypeResolution() {
    TypeRegistry registry = new TestNamedTypeRegistry();
    JSType stringType = registry.getType("String");
    JSType namedType = newInstance(NamedType.class, registry, "NamedType",
        "Test", 1, 1);

    assertNull(namedType.getReferencedType());
    assertNull(namedType.resolve(newErrorReporter(1, 1)));

    registry.registerType("NamedType", stringType);
    JSType resolvedType = namedType.resolve(newErrorReporter(1, 1));
    assertEquals(stringType, resolvedType);
    assertEquals(stringType.getReferenceName(), resolvedType.getReferenceName());
  }

  @Test
  public void testNamedTypeResolutionWithValidator() {
    TypeRegistry registry = new TestNamedTypeRegistry();
    JSType stringType = registry.getType("String");
    JSType namedType = newInstance(NamedType.class, registry, "NamedType",
        "Test", 1, 1);

    Predicate<JSType> validator = jst -> jst.isStringType();
    namedType.setValidator(validator);

    assertNull(namedType.getReferencedType());
    assertNull(namedType.resolve(newErrorReporter(1, 1)));

    registry.registerType("NamedType", stringType);
    JSType resolvedType = namedType.resolve(newErrorReporter(1, 1));
    assertEquals(stringType, resolvedType);
    assertEquals(stringType.getReferenceName(), resolvedType.getReferenceName());

    // Validator should have been called
    assertTrue(validator.asPredicate().apply(resolvedType));
  }

  @Test
  public void testNamedTypeResolverViaRegistry() {
    TypeRegistry registry = new TestNamedTypeRegistry();
    JSType stringType = registry.getType("String");
    JSType namedType = newInstance(NamedType.class, registry, "NamedType",
        "Test", 1, 1);

    JSType resolvedType = namedType.resolveViaRegistry(
        newErrorReporter(1, 1), null);
    assertNull(resolvedType);

    registry.registerType("NamedType", stringType);
    resolvedType = namedType.resolveViaRegistry(
        newErrorReporter(1, 1), null);
    assertEquals(stringType, resolvedType);
    assertEquals(stringType.getReferenceName(), resolvedType.getReferenceName());
  }

  @Test
  public void testNamedTypeResolverViaProperties() {
    TypeRegistry registry = new TestNamedTypeRegistry();
    JSType stringType = registry.getType("String");
    JSType arrayType = newInstance(
        ArrayType.class, registry.getType("Object"));
    JSType objectType = registry.getType("Object");
    JSType functionType = newInstance(
        FunctionType.class, registry.getType("Object"),
        arrayType, false);
    JSType namedType = newInstance(NamedType.class, registry, "NamedType",
        "Test", 1, 1);

    StaticScope scope = newStaticScope(registry);
    scope.define("String", stringType);
    scope.define("Array", arrayType);
    scope.define("Object", objectType);
    scope.define("Function", functionType);

    JSType resolvedType = namedType.resolveViaProperties(
        newErrorReporter(1, 1), scope);
    assertEquals(stringType, resolvedType);
    assertEquals(stringType.getReferenceName(), resolvedType.getReferenceName());
  }

  @Test
  public void testNamedTypePropertyContinuation() {
    TypeRegistry registry = new TestNamedTypeRegistry();
    JSType stringType = registry.getType("String");
    JSType namedType = newInstance(NamedType.class, registry, "NamedType",
        "Test", 1, 1);

    PropertyContinuation continuation = new PropertyContinuation(
        "prop", toType(Number.class), false, null);
    namedType.defineProperty("prop", null, false, null);
    namedType.finishPropertyContinuations();
    JSType resolvedType = namedType.resolve(newErrorReporter(1, 1));

    ObjectType target = newInstance(ObjectType.class, registry, "Test$NamedType",
        null);
    target.defineProperty("prop", toType(Number.class), false, null);
    assertEquals(target, resolvedType);
  }

  private static class PropertyContinuation {
    private final String propertyName;
    private final JSType type;
    private final boolean inferred;
    private final Node propertyNode;

    private PropertyContinuation(String propertyName, JSType type,
                                boolean inferred, Node propertyNode) {
      this.propertyName = propertyName;
      this.type = type;
      this.inferred = inferred;
      this.propertyNode = propertyNode;
    }

    void commit(ObjectType target) {
      target.defineProperty(
          propertyName, type, inferred, propertyNode);
    }

    @Override
    public String toString() {
      return String.format(
          "PropertyContinuation [propertyName=%s, type=%s, inferred=%s]",
          propertyName, type, inferred);
    }
  }

  private static class PropertyContinuationList extends List<PropertyContinuation> {
    private PropertyContinuationList(List<PropertyContinuation> list) {
      super(list);
    }

    @Override
    public String toString() {
      return "PropertyContinuationList [size=" + size() +
          ", list=" + Lists.toString(this) + "]";
    }
  }

  @Test
  public void testNamedTypePropertyContinuations() {
    TypeRegistry registry = new TestNamedTypeRegistry();
    JSType stringType = registry.getType("String");
    JSType namedType = newInstance(NamedType.class, registry, "NamedType",
        "Test", 1, 1);

    PropertyContinuation continuation1 = new PropertyContinuation(
        "prop1", toType(Number.class), false, null);
    PropertyContinuation continuation2 = new PropertyContinuation(
        "prop2", toType(Boolean.class), false, null);

    List<PropertyContinuation> propertyContinuations = Lists.newArrayList(
        continuation1, continuation2);

    namedType.defineProperty("prop1", null, false, null);
    namedType.finishPropertyContinuations();

    ObjectType target = newInstance(ObjectType.class, registry, "Test$NamedType",
        null);
    target.defineProperty("prop1", toType(Number.class), false, null);
    target.defineProperty("prop2", toType(Boolean.class), false, null);

    assertEquals(target, namedType.resolve(newErrorReporter(1, 1)));

    assertEquals(1, namedType.propertyContinuations().size());
    assertEquals(propertyContinuations.toString(),
        namedType.propertyContinuations().toString());

    PropertyContinuationList propertyContinuationsList =
        namedType.propertyContinuations();
    PropertyContinuation propertyContinuation = propertyContinuationsList.get(0);
    assertEquals("prop1", propertyContinuation.propertyName);
    assertEquals(toType(Number.class), propertyContinuation.type);
    assertFalse(propertyContinuation.inferred);
    assertEquals(null, propertyContinuation.propertyNode);

    propertyContinuation = propertyContinuationsList.get(1);
    assertEquals("prop2", propertyContinuation.propertyName);
    assertEquals(toType(Boolean.class), propertyContinuation.type);
    assertFalse(propertyContinuation.inferred);
    assertEquals(null, propertyContinuation.propertyNode);
  }
}