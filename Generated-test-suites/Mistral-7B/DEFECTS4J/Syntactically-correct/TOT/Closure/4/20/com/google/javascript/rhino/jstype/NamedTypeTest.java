import static com.google.javascript.rhino.testing.Assert.*;
import com.google.javascript.rhino.*;
import com.google.javascript.rhino.jstype.*;
import org.junit.Before;
import org.junit.Test;

public class NamedTypeTest {
  private Context context;
  private NamedTypeRegistry registry;

  @Before
  public void setUp() {
    context = new Context();
    registry = new NamedTypeRegistry(context);
  }

  @Test
  public void testSimpleNamedType() {
    registry.define("MyType", FunctionType.VOID);
    NamedType myType = new NamedType(registry, "MyType", "MyFile.js", 1, 1);
    assertEquals("MyType", myType.getReferenceName());
    assertEquals(FunctionType.VOID, myType.getReferencedType());
  }

  @Test
  public void testPropertyContinuations() {
    registry.define("MyType", FunctionType.VOID);
    NamedType myType = new NamedType(registry, "MyType", "MyFile.js", 1, 1);
    myType.defineProperty("myProperty", StringType.INSTANCE, false, null);

    ObjectType referencedObjType = myType.getReferencedObjTypeInternal();
    assertEquals(FunctionType.VOID, referencedObjType.getBaseType());
    PropertyContinuation propertyContinuation = myType.propertyContinuations.get(0);
    assertEquals("myProperty", propertyContinuation.propertyName);
    assertEquals(StringType.INSTANCE, propertyContinuation.type);
    assertEquals(false, propertyContinuation.inferred);
    assertEquals(null, propertyContinuation.propertyNode);

    myType.finishPropertyContinuations();
    assertEquals(StringType.INSTANCE, myType.getPropertyType("myProperty"));
  }

  @Test
  public void testResolutionViaRegistry() {
    registry.define("MyType", IntType.INSTANCE);
    NamedType myType = new NamedType(registry, "MyType", "MyFile.js", 1, 1);
    assertEquals(IntType.INSTANCE, myType.getReferencedType());

    // Resolve the same named type again
    NamedType myType2 = new NamedType(registry, "MyType", "MyFile.js", 1, 1);
    assertEquals(IntType.INSTANCE, myType2.getReferencedType());
    assertEquals(myType, myType2);
  }

  @Test
  public void testResolutionViaProperties() {
    registry.define("MyObject", FunctionType.VOID);
    registry.define("MyObject.myProperty", StringType.INSTANCE);
    NamedType myType = new NamedType(registry, "MyObject", "MyFile.js", 1, 1);
    assertEquals(FunctionType.VOID, myType.getBaseType());
    assertEquals(StringType.INSTANCE, myType.getPropertyType("myProperty"));
  }

  @Test(expected = AssertionError.class)
  public void testUnresolvedType() {
    NamedType unresolvedType = new NamedType(registry, "UnknownType", "MyFile.js", 1, 1);
    unresolvedType.resolve(context.getErrorReporter(), context.getStaticScope());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUnresolvedTypeWithValidator() {
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        throw new RuntimeException();
      }
    };

    NamedType unresolvedType = new NamedType(registry, "UnknownType", "MyFile.js", 1, 1);
    unresolvedType.setValidator(validator);
    unresolvedType.resolve(context.getErrorReporter(), context.getStaticScope());
  }
}