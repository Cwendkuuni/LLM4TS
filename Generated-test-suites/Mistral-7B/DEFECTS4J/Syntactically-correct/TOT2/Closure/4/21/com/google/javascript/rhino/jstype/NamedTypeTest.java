import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.testing.AssertThrows;
import com.google.javascript.rhino.testing.TestBase;

import java.util.List;

public class NamedTypeTest extends TestBase {

  private static final String NAMED_TYPE_REFERENCE = "MyNamedType";
  private static final String REFERENCED_TYPE_NAME = "ReferencedType";

  @Override
  public void setUp() throws Exception {
    super.setUp();

    JSTypeRegistry typeRegistry = new JSTypeRegistry(getContext(), getErrorReporter());
    typeRegistry.registerType(ReferenceType.class, REFERENCED_TYPE_NAME);
  }

  // Test case for constructor
  public void testNamedTypeConstructor() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry(getContext(), getErrorReporter());
    NamedType namedType = new NamedType(typeRegistry, NAMED_TYPE_REFERENCE,
        "SourceFile.java", 1, 1);
    assertNotNull(namedType);
    assertEquals(NAMED_TYPE_REFERENCE, namedType.getReferenceName());
  }

  // Test case for defineProperty method
  public void testNamedTypeDefineProperty() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry(getContext(), getErrorReporter());
    NamedType namedType = new NamedType(typeRegistry, NAMED_TYPE_REFERENCE,
        "SourceFile.java", 1, 1);

    StaticScope<JSType> staticScope = new StaticScope<>();
    namedType.defineProperty("myProperty", String.class, true, null);

    ObjectType referencedObjType = typeRegistry.getType(REFERENCED_TYPE_NAME);
    ObjectType target = new ObjectType(typeRegistry, referencedObjType);
    namedType.finishPropertyContinuations();

    assertTrue(target.hasProperty("myProperty"));
  }

  // Test case for getReferencedType method
  public void testNamedTypeGetReferencedType() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry(getContext(), getErrorReporter());
    NamedType namedType = new NamedType(typeRegistry, REFERENCED_TYPE_NAME,
        "SourceFile.java", 1, 1);

    JSType referencedType = new ReferenceType(typeRegistry, "ReferencedType");
    namedType.setReferencedAndResolvedType(referencedType, getErrorReporter(), null);

    assertEquals(referencedType, namedType.getReferencedType());
  }

  // Test case for resolve method - resolution via registry
  public void testNamedTypeResolveViaRegistry() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry(getContext(), getErrorReporter());
    NamedType namedType = new NamedType(typeRegistry, REFERENCED_TYPE_NAME,
        "SourceFile.java", 1, 1);

    JSType referencedType = new ReferenceType(typeRegistry, "ReferencedType");
    namedType.setReferencedAndResolvedType(referencedType, getErrorReporter(), null);

    assertSame(referencedType, namedType.resolve(getContext(), null));
  }

  // Test case for resolve method - resolution via properties (forward reference)
  public void testNamedTypeResolveViaPropertiesForwardReference() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry(getContext(), getErrorReporter());

    // Create a forward reference to a non-existent type
    NamedType namedType = new NamedType(typeRegistry, "ForwardReferenceType",
        "SourceFile.java", 1, 1);

    StaticScope<JSType> staticScope = new StaticScope<>();
    staticScope.define("ReferencedType", new ObjectType(typeRegistry, null));

    // Define a property with the forward reference
    namedType.defineProperty("myProperty", String.class, true, null);

    ObjectType referencedObjType = typeRegistry.getType("ReferencedType");
    ObjectType target = new ObjectType(typeRegistry, referencedObjType);
    namedType.finishPropertyContinuations();

    // Resolve the forward reference
    namedType.resolve(getContext(), staticScope);

    assertTrue(target.hasProperty("myProperty"));
  }

  // Test case for resolve method - resolution via properties (unresolved reference)
  public void testNamedTypeResolveViaPropertiesUnresolvedReference() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry(getContext(), getErrorReporter());

    // Create an unresolved reference to a non-existent type
    NamedType namedType = new NamedType(typeRegistry, "UnresolvedType",
        "SourceFile.java", 1, 1);

    StaticScope<JSType> staticScope = new StaticScope<>();
    staticScope.define("ReferencedType", new ObjectType(typeRegistry, null));

    // Define a property with the unresolved reference
    namedType.defineProperty("myProperty", String.class, true, null);

    ObjectType referencedObjType = typeRegistry.getType("ReferencedType");
    ObjectType target = new ObjectType(typeRegistry, referencedObjType);
    namedType.finishPropertyContinuations();

    try {
      // Resolve the unresolved reference
      namedType.resolve(getContext(), staticScope);
      fail("Expected Error: Unknown type name: UnresolvedType");
    } catch (AssertThrows.AssertionError e) {
      // Expected error
    }

    // The target should not have the property
    assertFalse(target.hasProperty("myProperty"));
  }

  // Test case for resolve method - resolution via properties (cycle)
  public void testNamedTypeResolveViaPropertiesCycle() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry(getContext(), getErrorReporter());

    // Create a cycle between two named types
    NamedType namedType1 = new NamedType(typeRegistry, "NamedType1",
        "SourceFile1.java", 1, 1);
    NamedType namedType2 = new NamedType(typeRegistry, "NamedType2",
        "SourceFile2.java", 1, 1);

    StaticScope<JSType> staticScope1 = new StaticScope<>();
    staticScope1.define("NamedType2", namedType2);

    StaticScope<JSType> staticScope2 = new StaticScope<>();
    staticScope2.define("NamedType1", namedType1);

    namedType1.defineProperty("myProperty", String.class, true, null);
    namedType2.defineProperty("myProperty", String.class, true, null);

    // Try to resolve the cycle
    namedType1.resolve(getContext(), staticScope1);
    namedType2.resolve(getContext(), staticScope2);

    try {
      namedType1.resolve(getContext(), staticScope1);
      fail("Expected Error: Type cycle detected: NamedType1 <- NamedType2");
    } catch (AssertThrows.AssertionError e) {
      // Expected error
    }

    try {
      namedType2.resolve(getContext(), staticScope2);
      fail("Expected Error: Type cycle detected: NamedType2 <- NamedType1");
    } catch (AssertThrows.AssertionError e) {
      // Expected error
    }
  }
}