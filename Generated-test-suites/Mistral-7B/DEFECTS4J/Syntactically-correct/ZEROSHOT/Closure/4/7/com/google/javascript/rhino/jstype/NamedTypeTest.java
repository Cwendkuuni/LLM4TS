import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.test.BaseTestCase;
import com.google.javascript.rhino.type.ErrorReporter;
import com.google.javascript.rhino.type.ObjectType;
import com.google.javascript.rhino.types.JSTypeNative;
import com.google.javascript.rhino.types.PrimitiveType;
import com.google.javascript.rhino.types.Primitives;
import com.google.javascript.rhino.types.UndefinedType;

public class NamedTypeTest extends BaseTestCase {

  private static ErrorReporter t;
  private static JSTypeRegistry registry;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    t = new ErrorReporter();
    registry = new JSTypeRegistry(new JSTypeRegistry.NormalScope(null, t));
  }

  @org.junit.Test
  public void testResolveViaRegistry() throws Exception {
    String reference = "Int32";
    NamedType namedType = new NamedType(registry, reference, "test.js", 1, 1);

    JSType resolvedType = namedType.resolveInternal(t, null);
    assertEquals( Primitives.Int32, resolvedType );
  }

  @org.junit.Test
  public void testResolveViaProperties() throws Exception {
    String reference = "String";
    Object obj = js.newObject();
    ObjectType objType = ObjectType.cast( registry.getTypeRegistry().getType( "Object" ) );
    objType.defineProperty( "test", registry.getTypeRegistry().getType( "String" ), false, null );
    StaticScope<JSType> scope = new StaticScope<JSType>(objType);
    NamedType namedType = new NamedType(registry, reference, "test.js", 1, 1);

    JSType resolvedType = namedType.resolveInternal(t, scope);
    assertEquals(Primitives.String, resolvedType);
  }

  @org.junit.Test
  public void testResolveWithUnknownType() throws Exception {
    String reference = "UnknownType";
    NamedType namedType = new NamedType(registry, reference, "test.js", 1, 1);

    JSType resolvedType = namedType.resolveInternal(t, null);
    assertEquals( UndefinedType.instance, resolvedType );
  }

  @org.junit.Test
  public void testResolveWithUnresolvedType() throws Exception {
    String reference = "UnresolvedType";
    NamedType namedType = new NamedType(registry, reference, "test.js", 1, 1);

    JSType resolvedType = namedType.resolveInternal(t, null);
    assertEquals( registry.getNativeObjectType( JSTypeNative.NO_RESOLVED_TYPE ), resolvedType );
  }

  @org.junit.Test
  public void testResolveWithCycle() throws Exception {
    String reference = "Cycle";
    ObjectType cycleType = new ObjectType(registry, "Cycle");
    cycleType.setBaseType(registry.getNativeObjectType(JSTypeNative.OBJECT));
    registry.setType(reference, cycleType);
    NamedType namedType = new NamedType(registry, reference, "test.js", 1, 1);

    JSType resolvedType = namedType.resolveInternal(t, null);
    assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), resolvedType);

    t.assertWarnings("1", "Cycle detected in inheritance chain of type Cycle");
  }

  @org.junit.Test
  public void testSetValidator() throws Exception {
    String reference = "TestType";
    ObjectType baseType = new ObjectType(registry, "BaseType");
    NamedType namedType = new NamedType(registry, reference, "test.js", 1, 1);

    Predicate<JSType> validator = new Predicate<JSType>() {
      public boolean apply(JSType type) {
        return type instanceof PrimitiveType;
      }
    };
    namedType.setValidator(validator);

    JSType resolvedType = namedType.resolveInternal(t, null);
    assertFalse(resolvedType instanceof PrimitiveType);

    resolvedType = namedType.resolveInternal(t, null);
    assertTrue(resolvedType instanceof PrimitiveType);
  }
}