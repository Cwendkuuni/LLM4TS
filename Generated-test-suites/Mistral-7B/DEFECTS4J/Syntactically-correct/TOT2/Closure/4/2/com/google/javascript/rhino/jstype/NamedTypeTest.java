import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.FunctionType;
import com.google.javascript.rhino.EnumType;
import com.google.javascript.rhino.EnumElementType;
import com.google.javascript.rhino.ProxyObjectType;
import com.google.javascript.rhino.NamedType;

public class NamedTypeTest {

  @Test
  public void testNamedTypeWithSimpleReference() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();

    String reference = "String";
    NamedType namedType = new NamedType(registry, reference, "Source", 1, 1);

    // Basic functionality test
    JSType resolvedType = namedType.resolveInternal(t, enclosing);
    assertTrue(resolvedType instanceof JSTypeRegistry.BuiltinType);
    assertEquals(JSTypeNative.STRING_TYPE, resolvedType);

    // Test with validator
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input instanceof JSTypeRegistry.BuiltinType;
      }
    };
    namedType.setValidator(validator);
    resolvedType = namedType.resolveInternal(t, enclosing);
    assertTrue(validator.apply(resolvedType));

    // Test with property continuations
    namedType.defineProperty("testProperty", JSTypeRegistry.getBooleanType(), true, null);
    namedType.finishPropertyContinuations();
    ObjectType objectType = (ObjectType) namedType.resolveInternal(t, enclosing);
    assertTrue(objectType.hasProperty("testProperty"));

    // Test with different reference names
    String reference2 = "Array";
    NamedType namedType2 = new NamedType(registry, reference2, "Source2", 2, 2);
    resolvedType = namedType2.resolveInternal(t, enclosing);
    assertTrue(resolvedType instanceof JSTypeRegistry.BuiltinType);
    assertEquals(JSTypeNative.ARRAY_TYPE, resolvedType);

    // Test with invalid reference
    String reference3 = "";
    NamedType namedType3 = new NamedType(registry, reference3, "Source3", 3, 3);
    namedType3.resolveInternal(t, enclosing);
    assertNotNull(namedType3.getReferencedType());
    assertEquals(JSTypeRegistry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType3.getReferencedType());
  }

  @Test
  public void testNamedTypeWithFunctionTypeReference() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();

    String reference = "Math";
    NamedType namedType = new NamedType(registry, reference, "Source", 1, 1);

    // Basic functionality test
    JSType resolvedType = namedType.resolveInternal(t, enclosing);
    assertTrue(resolvedType instanceof FunctionType);

    // Test with validator
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input instanceof FunctionType;
      }
    };
    namedType.setValidator(validator);
    resolvedType = namedType.resolveInternal(t, enclosing);
    assertTrue(validator.apply(resolvedType));

    // Test with property continuations
    namedType.defineProperty("testProperty", JSTypeRegistry.getBooleanType(), true, null);
    namedType.finishPropertyContinuations();
    ObjectType objectType = (ObjectType) namedType.resolveInternal(t, enclosing);
    assertTrue(objectType.hasProperty("testProperty"));

    // Test with different reference names
    String reference2 = "Math.random";
    NamedType namedType2 = new NamedType(registry, reference2, "Source2", 2, 2);
    resolvedType = namedType2.resolveInternal(t, enclosing);
    assertTrue(resolvedType instanceof FunctionType);

    // Test with invalid reference
    String reference3 = "";
    NamedType namedType3 = new NamedType(registry, reference3, "Source3", 3, 3);
    namedType3.resolveInternal(t, enclosing);
    assertNotNull(namedType3.getReferencedType());
    assertEquals(JSTypeRegistry.getNativeFunctionType(JSTypeNative.NO_OBJECT_TYPE), namedType3.getReferencedType());
  }

  @Test
  public void testNamedTypeWithEnumTypeReference() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();

    String reference = "Color";
    String enumName = "Colors";
    String enumElementName = "RED";

    // Create an EnumType and add an EnumElementType to it
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    FunctionType enumTypeFunction = typeRegistry.getFunctionType(new NodeList(), null);
    JSType enumType = new EnumType(enumTypeFunction, "Colors", "Colors", null);
    EnumElementType enumElement = new EnumElementType(enumType, enumElementName, null);
    enumType.defineProperty(enumElementName, enumElement.getType(), true, null);

    // Create a NamedType with the EnumType reference
    NamedType namedType = new NamedType(registry, reference, "Source", 1, 1);
    namedType.setReferencedType(enumType);

    // Basic functionality test
    JSType resolvedType = namedType.resolveInternal(t, enclosing);
    assertTrue(resolvedType instanceof EnumType);
    assertEquals(enumType, resolvedType);

    // Test with validator
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input instanceof EnumType;
      }
    };
    namedType.setValidator(validator);
    resolvedType = namedType.resolveInternal(t, enclosing);
    assertTrue(validator.apply(resolvedType));

    // Test with property continuations
    namedType.defineProperty("testProperty", JSTypeRegistry.getBooleanType(), true, null);
    namedType.finishPropertyContinuations();
    ObjectType objectType = (ObjectType) namedType.resolveInternal(t, enclosing);
    assertTrue(objectType.hasProperty("testProperty"));

    // Test with different reference names
    String reference2 = "Colors.RED";
    NamedType namedType2 = new NamedType(registry, reference2, "Source2", 2, 2);
    resolvedType = namedType2.resolveInternal(t, enclosing);
    assertTrue(resolvedType instanceof EnumElementType);
    assertEquals(enumElement, resolvedType);

    // Test with invalid reference
    String reference3 = "";
    NamedType namedType3 = new NamedType(registry, reference3, "Source3", 3, 3);
    namedType3.resolveInternal(t, enclosing);
    assertNotNull(namedType3.getReferencedType());
    assertEquals(JSTypeRegistry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType3.getReferencedType());
  }

  @Test
  public void testNamedTypeWithUnknownTypeReference() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();

    String reference = "UnknownType";
    NamedType namedType = new NamedType(registry, reference, "Source", 1, 1);

    // Basic functionality test
    JSType resolvedType = namedType.resolveInternal(t, enclosing);
    assertNotNull(namedType.getReferencedType());
    assertEquals(JSTypeRegistry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());
    assertEquals(JSTypeRegistry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), resolvedType);

    // Test with validator
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input instanceof JSTypeRegistry.BuiltinType && input == JSTypeRegistry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE);
      }
    };
    namedType.setValidator(validator);
    resolvedType = namedType.resolveInternal(t, enclosing);
    assertTrue(validator.apply(resolvedType));

    // Test with property continuations
    namedType.defineProperty("testProperty", JSTypeRegistry.getBooleanType(), true, null);
    namedType.finishPropertyContinuations();
    ObjectType objectType = (ObjectType) namedType.resolveInternal(t, enclosing);
    assertTrue(objectType.hasProperty("testProperty"));

    // Test with different reference names
    String reference2 = "AnotherUnknownType";
    NamedType namedType2 = new NamedType(registry, reference2, "Source2", 2, 2);
    assertNotSame(namedType, namedType2);
    assertNotNull(namedType2.getReferencedType());
    assertEquals(JSTypeRegistry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType2.getReferencedType());
  }

  @Test
  public void testNamedTypeWithEnumElementCycle() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();

    String reference = "Color";
    String enumName = "Colors";
    String enumElementName = "RED";

    // Create an EnumType and add an EnumElementType to it
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    FunctionType enumTypeFunction = typeRegistry.getFunctionType(new NodeList(), null);
    JSType enumType = new EnumType(enumTypeFunction, "Colors", "Colors", null);
    EnumElementType enumElement = new EnumElementType(enumType, enumElementName, null);
    enumType.defineProperty(enumElementName, enumElement.getType(), true, null);

    // Create a NamedType with the EnumElementType reference
    NamedType namedType = new NamedType(registry, enumElementName, "Source", 1, 1);
    namedType.setReferencedType(enumElement);

    // Create a NamedType with the EnumType reference
    NamedType namedType2 = new NamedType(registry, reference, "Source2", 2, 2);
    namedType2.setReferencedType(enumType);

    // Test with validator
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input instanceof EnumElementType && input == enumElement;
      }
    };
    namedType.setValidator(validator);
    JSType resolvedType = namedType.resolveInternal(t, enclosing);
    assertTrue(validator.apply(resolvedType));

    // Test with EnumElementCycle
    Predicate<JSType> enumElementCycleValidator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        if (input instanceof EnumElementType) {
          EnumElementType element = (EnumElementType) input;
          if (element.getPrimitiveType() == namedType2) {
            return true;
          }
        }
        return false;
      }
    };
    namedType2.setValidator(enumElementCycleValidator);
    resolvedType = namedType2.resolveInternal(t, enclosing);
    assertTrue(enumElementCycleValidator.apply(resolvedType));
  }
}