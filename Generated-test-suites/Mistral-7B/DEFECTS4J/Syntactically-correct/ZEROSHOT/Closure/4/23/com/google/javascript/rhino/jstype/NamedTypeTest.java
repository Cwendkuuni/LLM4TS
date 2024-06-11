import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;
import static com.google.javascript.rhino.jstype.JSType.*;

import com.google.common.base.Predicates;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JsContext;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.ScriptContext;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;

import java.util.List;

/**
 * Test file for the NamedType class.
 */
public class NamedTypeTest {

  private static JSTypeRegistry registry;
  private static ErrorReporter errorReporter;
  private static ScriptContext context;
  private static NodeFactory nodeFactory;

  @BeforeClass
  public static void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter();
    context = new ScriptContext();
    nodeFactory = context.getFactory();
  }

  /**
   * Test constructor.
   */
  @Test
  public void constructorTest() {
    String reference = "Foo";
    String sourceName = "TestSource";
    int lineno = 10;
    int charno = 20;
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(reference, namedType.getReferenceName());
    assertEquals(sourceName, namedType.getSourceName());
    assertEquals(lineno, namedType.getLineNumber());
    assertEquals(charno, namedType.getCharacterOffset());
    assertFalse(namedType.isResolved());
  }

  /**
   * Test defining property continuations.
   */
  @Test
  public void definePropertyContinuationTest() {
    String reference = "Foo";
    String sourceName = "TestSource";
    int lineno = 10;
    int charno = 20;
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    String propertyName = "bar";
    JSType type = StringType;
    Node propertyNode = nodeFactory.newName("bar");

    namedType.defineProperty(propertyName, type, false, propertyNode);
    assertFalse(namedType.isResolved());

    ObjectType referencedObjType = namedType.getReferencedObjTypeInternal();
    assertNull(referencedObjType);

    namedType.finishPropertyContinuations();

    referencedObjType = namedType.getReferencedObjTypeInternal();
    assertTrue(referencedObjType instanceof ObjectType);
    ObjectType objType = (ObjectType) referencedObjType;
    assertTrue(objType.hasProperty(propertyName));
    assertEquals(type, objType.getPropertyType(propertyName));
  }

  /**
   * Test getting referenced type.
   */
  @Test
  public void getReferencedTypeTest() {
    String reference = "Foo";
    String sourceName = "TestSource";
    int lineno = 10;
    int charno = 20;
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    registry.put(reference, StringType);

    JSType referencedType = namedType.getReferencedType();
    assertEquals(StringType, referencedType);
    assertTrue(namedType.isResolved());
  }

  /**
   * Test isNamedType.
   */
  @Test
  public void isNamedTypeTest() {
    String reference = "Foo";
    String sourceName = "TestSource";
    int lineno = 10;
    int charno = 20;
    NamedType namedType = new NamedType(null, reference, sourceName, lineno, charno);
    assertTrue(namedType.isNamedType());
  }

  /**
   * Test isNominalType.
   */
  @Test
  public void isNominalTypeTest() {
    String reference = "Foo";
    String sourceName = "TestSource";
    int lineno = 10;
    int charno = 20;
    NamedType namedType = new NamedType(null, reference, sourceName, lineno, charno);
    assertTrue(namedType.isNominalType());
  }

  /**
   * Test hashCode.
   */
  @Test
  public void hashCodeTest() {
    String reference = "Foo";
    String sourceName = "TestSource";
    int lineno = 10;
    int charno = 20;
    NamedType namedType1 = new NamedType(null, reference, sourceName, lineno, charno);
    NamedType namedType2 = new NamedType(null, reference, sourceName, lineno, charno);
    assertEquals(namedType1.hashCode(), namedType2.hashCode());
  }

  /**
   * Test toStringHelper.
   */
  @Test
  public void toStringHelperTest() {
    String reference = "Foo";
    String sourceName = "TestSource";
    int lineno = 10;
    int charno = 20;
    NamedType namedType = new NamedType(null, reference, sourceName, lineno, charno);
    assertEquals(reference, namedType.toStringHelper(false));
  }

  /**
   * Test resolve.
   */
  @Test
  public void resolveTest() {
    String reference = "Foo";
    String sourceName = "TestSource";
    int lineno = 10;
    int charno = 20;
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    registry.put(reference, StringType);

    JSType referencedType = namedType.resolveInternal(errorReporter, enclosing);
    assertEquals(StringType, referencedType);
    assertTrue(namedType.isResolved());
  }

  /**
   * Test resolve via registry.
   */
  @Test
  public void resolveViaRegistryTest() {
    String reference = "Foo";
    String sourceName = "TestSource";
    int lineno = 10;
    int charno = 20;
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    registry.put(reference, StringType);

    JSType referencedType = namedType.resolveInternal(errorReporter, enclosing);
    assertEquals(StringType, referencedType);
    assertTrue(namedType.isResolved());
  }

  /**
   * Test resolve via properties.
   */
  @Test
  public void resolveViaPropertiesTest() {
    String reference = "Foo.Bar.Baz";
    String sourceName = "TestSource";
    int lineno = 10;
    int charno = 20;
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    registry.put("Foo", ObjectType.CAST(registry.getNativeObjectType("Object")));

    ObjectType fooType = (ObjectType) registry.get("Foo");
    fooType.setPropertyType("Bar", FunctionType.CAST(registry.getNativeFunctionType("Function")));

    FunctionType barFunctionType = (FunctionType) fooType.getPropertyType("Bar");
    barFunctionType.setPrototype(registry.getNativeObjectType("Object"));
    barFunctionType.setInstanceType(ObjectType.CAST(registry.getNativeObjectType("Object")));

    ObjectType barInstanceType = (ObjectType) barFunctionType.getInstanceType();
    barInstanceType.setPropertyType("Baz", StringType);

    JSType referencedType = namedType.resolveInternal(errorReporter, enclosing);
    assertEquals(StringType, referencedType);
    assertTrue(namedType.isResolved());
  }

  /**
   * Test resolve with cycle.
   */
  @Test(expected = AssertionError.class)
  public void resolveWithCycleTest() {
    String reference = "A.B.A";
    String sourceName = "TestSource";
    int lineno = 10;
    int charno = 20;
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    registry.put("A", new ObjectType(registry, "Object") {
      @Override
      public JSType getPrototype() {
        return new ObjectType(registry, "Object") {
          @Override
          public JSType getPrototype() {
            return namedType;
          }
        };
      }
    });

    namedType.resolveInternal(errorReporter, enclosing);
  }

  /**
   * Test resolve with unresolved type.
   */
  @Test
  public void resolveWithUnresolvedTypeTest() {
    String reference = "Foo";
    String sourceName = "TestSource";
    int lineno = 10;
    int charno = 20;
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Unresolved type should not trigger a warning.
    JSType unresolvedType = new ObjectType(registry, "Object");
    registry.put(reference, unresolvedType);

    JSType referencedType = namedType.resolveInternal(errorReporter, enclosing);
    assertSame(unresolvedType, referencedType);
    assertFalse(namedType.isResolved());
  }

  /**
   * Test resolve with invalid type.
   */
  @Test
  public void resolveWithInvalidTypeTest() {
    String reference = "Foo";
    String sourceName = "TestSource";
    int lineno = 10;
    int charno = 20;
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Invalid type should trigger a warning.
    JSType invalidType = new ObjectType(registry, "InvalidType");
    registry.put(reference, invalidType);

    String warningMessage = "Bad type annotation. Unknown type " + reference;
   registry.setWarningHandler(errorReporter, Predicates.isEqual(warningMessage));

    JSType referencedType = namedType.resolveInternal(errorReporter, enclosing);
    assertSame(invalidType, referencedType);
    assertFalse(namedType.isResolved());
    assertTrue(errorReporter.hasWarnings());
  }

  /**
   * Test setValidator.
   */
  @Test
  public void setValidatorTest() {
    String reference = "Foo";
    String sourceName = "TestSource";
    int lineno = 10;
    int charno = 20;
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    Predicate<JSType> validator = Predicates.isInstanceOf(IntType.class);

    // Validator should not be applied when type is unresolved.
    assertFalse(namedType.setValidator(validator));

    // Validator should be applied when type is resolved.
    registry.put(reference, IntType);
    assertTrue(namedType.setValidator(validator));

    JSType referencedType = namedType.getReferencedType();
    assertTrue(validator.apply(referencedType));
  }
}