import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.common.base.Predicate;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.ProxyObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.TypeChecker;

import java.util.List;

public class NamedTypeTest {

  private static JSTypeRegistry registry;
  private ErrorReporter t;
  private StaticScope<JSType> enclosing;
  private JSTypeType typeChecker;

  @BeforeClass
  public static void setUp() {
    registry = new JSTypeRegistry();
    registry.put("String", String.class);
    registry.put("Number", Number.class);
  }

  @Before
  public void setUpBeforeEach() {
    t = new ErrorReporter();
    enclosing = new StaticScope<>(registry, null);
    typeChecker = new TypeChecker(registry, t);
  }

  @Test
  public void testNamedType_resolveByRegistry() {
    String reference = "String";
    JSType type = new NamedType(registry, reference, "testFile", 1, 1);
    assertEquals(reference, type.getReferenceName());
    assertEquals(String.class, type.resolveInternal(t, enclosing).getTypeClass());
  }

  @Test
  public void testNamedType_resolveByProperties() {
    String reference = "testType";
    String testFile = "testFile";
    int lineNumber = 1;
    int charPositionInLine = 1;

    ObjectType testType = new ObjectType(registry, typeChecker, "testType", null);
    testType.putProperty("prop", new JSTypeNative(ANY));
    enclosing.putSlot(reference, testType);

    String propertyName = "prop";
    JSType expectedType = new JSTypeNative(ANY);

    JSType type = new NamedType(registry, reference, testFile, lineNumber, charPositionInLine);
    assertEquals(reference, type.getReferenceName());
    assertEquals(expectedType, type.getReferencedType());

    type.resolveInternal(t, enclosing);
    assertEquals(testType, type.getTypeObject());
    assertEquals(expectedType, testType.getPropertyType(propertyName));
  }

  @Test
  public void testNamedType_propertyContinuation() {
    String reference = "testType";
    String testFile = "testFile";
    int lineNumber = 1;
    int charPositionInLine = 1;

    ObjectType testType = new ObjectType(registry, typeChecker, "testType", null);
    testType.putProperty("prop", new JSTypeNative(ANY));
    enclosing.putSlot(reference, testType);

    String propertyName = "prop";
    JSType expectedType = new JSTypeNative(ANY);

    JSType type = new NamedType(registry, reference, testFile, lineNumber, charPositionInLine);
    type.defineProperty(propertyName, new JSTypeNative(NUMBER), false, null);

    type.finishPropertyContinuations();

    ObjectType definedType = (ObjectType) type;
    assertEquals(testType, definedType.getTypeObject());
    assertEquals(expectedType, definedType.getPropertyType(propertyName));
  }

  @Test
  public void testNamedType_resolveByRegistryVsProperties() {
    String reference = "testType";
    String testFile = "testFile";
    int lineNumber = 1;
    int charPositionInLine = 1;

    ObjectType testType = new ObjectType(registry, typeChecker, "testType", null);
    testType.putProperty("prop", new JSTypeNative(ANY));
    enclosing.putSlot(reference, testType);

    String propertyName = "prop";
    JSType expectedType = new JSTypeNative(ANY);

    JSType type = new NamedType(registry, reference, testFile, lineNumber, charPositionInLine);
    assertEquals(reference, type.getReferenceName());

    // Check if resolution via registry takes precedence
    assertEquals(String.class, type.resolveInternal(t, enclosing).getTypeClass());

    type.resolveInternal(t, enclosing);
    assertEquals(testType, type.getTypeObject());
    assertEquals(expectedType, testType.getPropertyType(propertyName));
  }

  @Test
  public void testNamedType_handleTypeCycle() {
    String reference = "testType";
    String testFile = "testFile";
    int lineNumber = 1;
    int charPositionInLine = 1;

    ObjectType testType = new ObjectType(registry, typeChecker, "testType", null);
    testType.putProperty("prop", testType);
    enclosing.putSlot(reference, testType);

    JSType type = new NamedType(registry, reference, testFile, lineNumber, charPositionInLine);

    type.resolveInternal(t, enclosing);
    assertEquals(testType, type.getTypeObject());

    // Check for type cycle warning
    List<String> warnings = t.getWarnings();
    assertEquals(1, warnings.size());
    String warning = warnings.get(0);
    assertTrue(warning.contains("Cycle detected in inheritance chain"));
  }

  @Test
  public void testNamedType_handleEnumElementCycle() {
    String reference = "EnumType";
    String testFile = "testFile";
    int lineNumber = 1;
    int charPositionInLine = 1;

    ObjectType enumType = new ObjectType(registry, typeChecker, "EnumType", null);
    ObjectType enumElementType = new EnumElementType(
        registry, enumType, "ENUM_ELEMENT", registry.getNativeObjectType(NUMBER));
    enumType.putElementType(enumElementType);
    enclosing.putSlot(reference, enumType);

    JSType type = new NamedType(registry, reference, testFile, lineNumber, charPositionInLine);

    type.resolveInternal(t, enclosing);
    assertEquals(enumType, type.getTypeObject());

    // Check for enum element cycle warning
    List<String> warnings = t.getWarnings();
    assertEquals(1, warnings.size());
    String warning = warnings.get(0);
    assertTrue(warning.contains("Cycle detected in enum element inheritance"));
  }

  @Test
  public void testNamedType_handleUnresolvedType() {
    String reference = "testType";
    String testFile = "testFile";
    int lineNumber = 1;
    int charPositionInLine = 1;

    JSType type = new NamedType(registry, reference, testFile, lineNumber, charPositionInLine);

    type.resolveInternal(t, enclosing);
    assertEquals(registry.getNativeObjectType(NO_RESOLVED_TYPE), type.getTypeObject());

    // Check for unresolved type warning
    List<String> warnings = t.getWarnings();
    assertEquals(1, warnings.size());
    String warning = warnings.get(0);
    assertTrue(warning.contains("Unknown type"));
  }

  @Test
  public void testNamedType_setValidator() {
    String reference = "testType";
    String testFile = "testFile";
    int lineNumber = 1;
    int charPositionInLine = 1;

    ObjectType testType = new ObjectType(registry, typeChecker, "testType", null);
    testType.putProperty("prop", new JSTypeNative(ANY));
    enclosing.putSlot(reference, testType);

    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input.isObjectType();
      }
    };

    JSType type = new NamedType(registry, reference, testFile, lineNumber, charPositionInLine);
    type.setValidator(validator);

    JSType resolvedType = type.resolveInternal(t, enclosing);
    assertTrue(validator.apply(resolvedType));
  }
}