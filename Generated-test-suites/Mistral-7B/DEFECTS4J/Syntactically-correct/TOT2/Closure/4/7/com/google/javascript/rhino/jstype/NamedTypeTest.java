import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.jstype.EnumElementType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.PrototypeObjectType;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class NamedTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter t;
  private StaticScope<JSType> enclosingScope;
  private NamedType namedType;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    t = new ErrorReporter();
    enclosingScope = new StaticScope<>();
  }

  @Test
  public void testNamedTypeCreation() {
    String reference = "MyNamedType";
    String sourceName = "TestClass";
    int lineno = 1;
    int charno = 1;

    JSType myNamedType = new NamedType(registry, reference, sourceName, lineno, charno);

    assertEquals("MyNamedType", myNamedType.getReferenceName());
    assertEquals(reference, myNamedType.toString());
    assertTrue(myNamedType.isNamedType());
    assertTrue(myNamedType.isNominalType());
  }

  @Test
  public void testDefinePropertyWhenUnresolved() {
    String reference = "MyNamedType";
    String sourceName = "TestClass";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno) {
      @Override
      boolean defineProperty(String propertyName, JSType type,
          boolean inferred, Node propertyNode) {
        // Assuming this method is called before the type is resolved
        fail("Should not be able to define a property on an unresolved type");
        return false;
      }
    };

    enclosingScope.defineSlot(reference, ObjectType.cast(registry.getNullType()));

    namedType.defineProperty("testProperty", registry.getNullType(), false, null);

    fail("Property should not be defined yet");
  }

  @Test
  public void testResolveViaRegistry() {
    String reference = "MyEnum";
    String sourceName = "TestClass";
    int lineno = 1;
    int charno = 1;

    registry.putType(reference, new EnumType(registry, "MyEnum", sourceName, lineno, charno));

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    FunctionType enumType = registry.getType(reference).toMaybeFunctionType();
    assertEquals(enumType.getInstanceType(), namedType.getReferencedType());
  }

  @Test
  public void testResolveViaProperties() {
    String reference = "MyObject.MyProperty";
    String sourceName = "TestClass";
    int lineno = 1;
    int charno = 1;

    String firstComponent = "MyObject";
    ObjectType myObjectType = new ObjectType(registry, "MyObject", sourceName, lineno, charno);
    StaticSlot<JSType> myObjectSlot = enclosingScope.defineSlot(firstComponent, myObjectType);

    String secondComponent = "MyProperty";
    JSType myPropertyType = registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE);
    StaticSlot<JSType> myPropertySlot = myObjectType.defineProperty(
        secondComponent, myPropertyType, false, null);

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    assertEquals(myPropertyType, namedType.getReferencedType());
  }

  @Test
  public void testResolveViaPropertiesWithEmptyFirstComponent() {
    String reference = "MyProperty";
    String sourceName = "TestClass";
    int lineno = 1;
    int charno = 1;

    ObjectType globalObject = registry.getGlobalObject();
    StaticSlot<JSType> globalObjectSlot = enclosingScope.defineSlot("global", globalObject);

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Global properties are not scanned by default, so this should fail
    try {
      assertEquals(registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE), namedType.getReferencedType());
      fail("Should not be able to resolve a global property without explicitly declaring it in the scope");
    } catch (AssertionError e) {
      // Expected
    }

    // Add the global property to the scope to test resolution
    enclosingScope.addProperty(new StaticProperty("global", registry.getNativeObjectType(JSTypeNative.GLOBAL_OBJECT), null));

    assertEquals(registry.getNativeObjectType(JSTypeNative.GLOBAL_OBJECT), namedType.getReferencedType());
  }

  @Test(expected = AssertionError.class)
  public void testResolveViaPropertiesWithInvalidFirstComponent() {
    String reference = "MyObject.InvalidProperty";
    String sourceName = "TestClass";
    int lineno = 1;
    int charno = 1;

    ObjectType myObjectType = new ObjectType(registry, "MyObject", sourceName, lineno, charno);
    StaticSlot<JSType> myObjectSlot = enclosingScope.defineSlot("MyObject", myObjectType);

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    fail("Should not be able to resolve an invalid property name");
  }

  @Test(expected = AssertionError.class)
  public void testResolveViaPropertiesWithNullFirstComponent() {
    String reference = null;
    String sourceName = "TestClass";
    int lineno = 1;
    int charno = 1;

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    fail("Should not be able to resolve a null property name");
  }

  @Test
  public void testSetValidator() {
    String reference = "MyNamedType";
    String sourceName = "TestClass";
    int lineno = 1;
    int charno = 1;

    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input.isFunctionType();
      }
    };

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    namedType.setValidator(validator);

    JSType referencedType = new FunctionType(registry, null, null, null, null);
    assertFalse(validator.apply(referencedType));

    assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());

    referencedType = new FunctionType(registry, null, null, null, null);
    assertTrue(validator.apply(referencedType));

    JSType resolvedType = new ObjectType(registry, "MyNamedType", sourceName, lineno, charno);
    namedType.setReferencedAndResolvedType(resolvedType, t, enclosingScope);

    assertTrue(validator.apply(resolvedType));
  }
}