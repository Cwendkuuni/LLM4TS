import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSTestCase.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StylusObject;
import com.google.javascript.rhino.jstype.*;

/**
 * Tests for {@code NamedType} class.
 */
public class NamedTypeTest extends JSTestCase {

  private static final String REFERENCE = "TestType";
  private static final String SOURCE_NAME = "NamedTypeTest";
  private static final int LINE_NUMBER = 1;
  private static final int CHAR_NUMBER = 1;
  private static final ErrorReporter T_ = null;
  private static final JSTypeRegistry REGISTRY = new JSTypeRegistry();
  private static final StaticScope<JSType> ENCLOSING = new StylusObject();

  @BeforeClass
  public static void setUp() {
    REGISTRY.init(T_);
    REGISTRY.put(REFERENCE, new FunctionType(
        JSTypeNative.VOID_TYPE, Lists.newArrayList(), true));
  }

  /**
   * Tests constructor.
   */
  public void testNamedTypeConstructor() {
    NamedType namedType = new NamedType(REGISTRY, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    assertEquals(REFERENCE, namedType.getReferenceName());
    assertEquals(SOURCE_NAME, namedType.toStringHelper(false).toString());
    assertNotNull(namedType.isValidator());
  }

  /**
   * Tests defineProperty method when type is unresolved.
   */
  public void testDefinePropertyWhenTypeIsUnresolved() {
    NamedType namedType = new NamedType(REGISTRY, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    PropertyContinuation propertyContinuation = new PropertyContinuation(
        "propertyName", ObjectType.CAST(new IntType()), true, null);

    namedType.defineProperty("propertyName", ObjectType.CAST(new IntType()), true, null);

    assertTrue(namedType.propertyContinuations != null);
    assertEquals(1, namedType.propertyContinuations.size());
    assertEquals(propertyContinuation, namedType.propertyContinuations.get(0));
  }

  /**
   * Tests finishPropertyContinuations method when type is resolved.
   */
  public void testFinishPropertyContinuationsWhenTypeIsResolved() {
    NamedType namedType = new NamedType(REGISTRY, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    FunctionType resolvedType = new FunctionType(
        JSTypeNative.INT_TYPE, Lists.newArrayList(), true);
    namedType.setReferencedAndResolvedType(resolvedType, T_, ENCLOSING);

    namedType.finishPropertyContinuations();

    assertNull(namedType.propertyContinuations);
  }

  /**
   * Tests getReferencedType method.
   */
  public void testGetReferencedType() {
    NamedType namedType = new NamedType(REGISTRY, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    FunctionType referencedType = new FunctionType(
        JSTypeNative.VOID_TYPE, Lists.newArrayList(), true);
    namedType.setReferencedAndResolvedType(referencedType, T_, ENCLOSING);

    assertEquals(referencedType, namedType.getReferencedType());
  }

  /**
   * Tests resolveInternal method with valid registration in registry.
   */
  public void testResolveInternalWithValidRegistration() {
    NamedType namedType = new NamedType(REGISTRY, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    FunctionType referencedType = new FunctionType(
        JSTypeNative.INT_TYPE, Lists.newArrayList(), true);
    namedType.setReferencedAndResolvedType(referencedType, T_, ENCLOSING);

    JSType actualType = namedType.resolveInternal(T_, ENCLOSING);

    assertEquals(referencedType, actualType);
  }

  /**
   * Tests resolveInternal method with invalid registration in registry.
   */
  public void testResolveInternalWithInvalidRegistration() {
    NamedType namedType = new NamedType(REGISTRY, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);

    JSType actualType = namedType.resolveInternal(T_, ENCLOSING);

    assertTrue(actualType instanceof UnknownType);
  }

  /**
   * Tests resolveInternal method with properties.
   */
  public void testResolveInternalWithProperties() {
    NamedType namedType = new NamedType(REGISTRY, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    FunctionType firstComponentType = new FunctionType(
        JSTypeNative.INT_TYPE, Lists.newArrayList(), true);
    ObjectType parentClass = new ObjectType(REGISTRY, "ParentClass", null);
    parentClass.setPropertyType("property", new IntType());

    StaticSlot<JSType> slot = ENCLOSING.put(REFERENCE, firstComponentType);
    StaticSlot<ObjectType> parentSlot = ENCLOSING.put("ParentClass", parentClass);

    JSType actualType = namedType.resolveInternal(T_, ENCLOSING);

    assertEquals(firstComponentType.getInstanceType(), actualType);
  }

  /**
   * Tests setValidator method.
   */
  public void testSetValidator() {
    NamedType namedType = new NamedType(REGISTRY, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input instanceof IntType;
      }
    };

    namedType.setValidator(validator);

    JSType referencedType = new IntType();
    namedType.setReferencedAndResolvedType(referencedType, T_, ENCLOSING);

    assertTrue(validator.apply(referencedType));
  }
}