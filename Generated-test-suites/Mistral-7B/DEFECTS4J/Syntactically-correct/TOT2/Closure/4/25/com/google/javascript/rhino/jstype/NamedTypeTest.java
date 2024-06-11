import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.AssertThrows;
import com.google.javascript.rhino.testing.TypeErrorLike;
import com.google.javascript.rhino.testing.Value;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class NamedTypeTest {
  private ErrorReporter errorReporter;
  private StaticScope<JSType> enclosing;
  private NamedType namedType;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter();
    enclosing = new StaticScope<>();
  }

  // Test method for constructor
  @Test
  public void testNamedTypeConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "String";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;
    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(reference, namedType.getReferenceName());
  }

  // Test method for defineProperty
  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "Int32";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;
    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    JSTypeRegistry registry2 = new JSTypeRegistry();
    String reference2 = "Boolean";
    String sourceName2 = "NamedTypeTest";
    int lineno2 = 1;
    int charno2 = 1;

    StaticScope<JSType> enclosingScope = new StaticScope<>();
    StaticScope<JSType> innerScope = new StaticScope<>();
    enclosingScope.setParent(enclosing);
    innerScope.setParent(enclosingScope);

    namedType.defineProperty("prop", registry2.getType("Boolean"), false, null);

    assertEquals("Boolean", namedType.getPropertyNames()[0]);
    assertTrue(namedType.hasProperty("prop"));
  }

  // Test method for getReferencedType
  @Test
  public void testGetReferencedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "String";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;
    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    JSType referencedType = namedType.getReferencedType();
    assertEquals("String", referencedType.getClassName());
  }

  // Test method for isResolved
  @Test
  public void testIsResolved() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "Int32";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;
    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    assertFalse(namedType.isResolved());

    JSType referencedType = registry.getType("Int32");
    namedType.setReferencedType(referencedType);
    namedType.setResolvedType(namedType);

    assertTrue(namedType.isResolved());
  }

  // Test method for lookupViaProperties
  @Test
  public void testLookupViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "Int32.length";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;
    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    JSType registryType = registry.getType("Int32");
    ObjectType int32Type = ObjectType.cast(registryType);
    PropertyType lengthProperty = int32Type.getPropertyType("length");

    assertEquals(lengthProperty, namedType.lookupViaProperties(errorReporter, enclosing));
  }

  // Test method for resolveInternal
  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "Int32";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;
    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    StaticScope<JSType> enclosingScope = new StaticScope<>();
    StaticScope<JSType> innerScope = new StaticScope<>();
    enclosingScope.setParent(enclosing);
    innerScope.setParent(enclosingScope);

    JSType referencedType = registry.getType("Int32");
    namedType.setReferencedType(referencedType);
    namedType.resolveInternal(errorReporter, innerScope);

    assertEquals(Int32.class, namedType.getReferencedType().toMaybeObjectType().getBaseType());
    assertEquals(Int32.class, namedType.getResolvedType().toMaybeObjectType().getBaseType());
  }

  // Test method for resolveViaRegistry
  @Test
  public void testResolveViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "Int32";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosing);

    if (registry.isLastGeneration()) {
      assertTrue(resolvedType instanceof JSTypeNative);
      assertEquals(JSTypeNative.UNKNOWN_TYPE, resolvedType);
    } else {
      JSType referencedType = registry.getType("Int32");
      assertEquals(referencedType, resolvedType);
    }
  }

  // Test method for resolveViaProperties
  @Test
  public void testResolveViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "Int32.length";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    StaticScope<JSType> enclosingScope = new StaticScope<>();
    StaticScope<JSType> innerScope = new StaticScope<>();
    enclosingScope.setParent(enclosing);
    innerScope.setParent(enclosingScope);

    JSType registryType = registry.getType("Int32");
    ObjectType int32Type = ObjectType.cast(registryType);
    PropertyType lengthProperty = int32Type.getPropertyType("length");

    namedType.resolveInternal(errorReporter, innerScope);

    assertEquals(lengthProperty, namedType.getReferencedType());
  }

  // Test method for setReferencedAndResolvedType
  @Test
  public void testSetReferencedAndResolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "Int32";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    JSType referencedType = registry.getType("Int32");

    namedType.setReferencedAndResolvedType(referencedType, errorReporter, enclosing);

    assertEquals(referencedType, namedType.getReferencedType());
    assertEquals(referencedType, namedType.getResolvedType());
  }

  // Test method for handleTypeCycle
  @Test
  public void testHandleTypeCycle() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "NamedTypeCycle";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    errorReporter.setThrowOnError(true);

    namedType.handleTypeCycle(errorReporter);

    assertThrows(TypeErrorLike.class, () -> namedType.getReferencedType());
  }

  // Test method for setValidator
  @Test
  public void testSetValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "String";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    Predicate<JSType> validator = (type) -> type.isStringType();
    namedType.setValidator(validator);

    JSType referencedType = registry.getType("Int32");
    namedType.setReferencedType(referencedType);
    namedType.resolveInternal(errorReporter, enclosing);

    assertThrows(AssertionError.class, () -> {
      namedType.getReferencedType();
    });
  }

  // Test method for handleUnresolvedType
  @Test
  public void testHandleUnresolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "UnknownType";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    errorReporter.setThrowOnError(true);

    namedType.handleUnresolvedType(errorReporter, false);

    assertThrows(TypeErrorLike.class, () -> namedType.getReferencedType());
  }
}