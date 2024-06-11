import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.AssertThrows;
import com.google.javascript.rhino.testing.StaticScopeTest;
import com.google.javascript.rhino.testing.TypeEquality;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.google.javascript.rhino.testing.AssertThrows.assertThrows;

/**
 * Test for {@code NamedType} class.
 */
public class NamedTypeTest extends StaticScopeTest {

  @Test
  public void testCreation() {
    StaticScope enclosing = new StaticScope<>();
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "java.lang.String";
    NamedType namedType = new NamedType(registry, reference, "MySource.java", 1, 1);
    assertThat(namedType.getReferenceName()).isEqualTo(reference);
    assertTypeEquals(JSTypeNative.STRING_TYPE, namedType.getReferencedType());
  }

  @Test
  public void testCreateUnresolvedNamedType() {
    StaticScope enclosing = new StaticScope<>();
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "MyUnknownType";
    NamedType namedType = new NamedType(registry, reference, "MySource.java", 1, 1);
    assertThat(namedType.getReferenceName()).isEqualTo(reference);
    assertTypeEquals(JSTypeNative.NO_RESOLVED_TYPE, namedType.getReferencedType());
  }

  @Test
  public void testDefinitionProperty() {
    StaticScope enclosing = new StaticScope<>();
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "MyType";
    NamedType namedType = new NamedType(registry, reference, "MySource.java", 1, 1);

    String propertyName = "myProperty";
    JSType propertyType = JSTypeNative.NUMBER_TYPE;
    Node propertyNode = null; // create a stub Node later

    namedType.defineProperty(propertyName, propertyType, false, propertyNode);

    ObjectType target = new ObjectType(registry, "MyType", "MySource.java", 1, 1);
    assertThat(target.hasProperty(propertyName)).isTrue();
    assertTypeEquals(propertyType, target.getPropertyType(propertyName));
  }

  @Test
  public void testFinishPropertyContinuations() {
    StaticScope enclosing = new StaticScope<>();
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "MyType";
    NamedType namedType = new NamedType(registry, reference, "MySource.java", 1, 1);

    String propertyName1 = "property1";
    JSType propertyType1 = JSTypeNative.STRING_TYPE;
    Node propertyNode1 = null;

    String propertyName2 = "property2";
    JSType propertyType2 = JSTypeNative.BOOLEAN_TYPE;
    Node propertyNode2 = null;

    namedType.defineProperty(propertyName1, propertyType1, false, propertyNode1);
    namedType.defineProperty(propertyName2, propertyType2, false, propertyNode2);

    ObjectType target = new ObjectType(registry, reference, "MySource.java", 1, 1);
    target.finishPropertyContinuations();

    assertThat(target.hasProperty(propertyName1)).isTrue();
    assertTypeEquals(propertyType1, target.getPropertyType(propertyName1));
    assertThat(target.hasProperty(propertyName2)).isTrue();
    assertTypeEquals(propertyType2, target.getPropertyType(propertyName2));
  }

  @Test
  public void testResolveTypeViaRegistry() {
    StaticScope enclosing = new StaticScope<>();
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "java.lang.String";
    NamedType namedType = new NamedType(registry, reference, "MySource.java", 1, 1);

    JSType expectedType = JSTypeNative.STRING_TYPE;
    JSType resolvedType = namedType.resolveInternal(registry, enclosing);

    assertTypeEquals(expectedType, resolvedType);
  }

  @Test
  public void testResolveTypeViaProperties() {
    StaticScope enclosing = new StaticScope<>();
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "java.util.ArrayList";
    NamedType namedType = new NamedType(registry, reference, "MySource.java", 1, 1);

    StaticScope childScope = new StaticScope<>();
    childScope.setParent(enclosing);

    JSType expectedType = JSTypeNative.FUNCTION_TYPE; // ArrayList constructor
    JSType resolvedType = namedType.resolveInternal(registry, childScope);

    assertTypeEquals(expectedType, resolvedType);
  }

  @Test
  public void testResolveTypeViaProperties_unknownType() {
    StaticScope enclosing = new StaticScope<>();
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "MyUnknownType";
    NamedType namedType = new NamedType(registry, reference, "MySource.java", 1, 1);

    StaticScope childScope = new StaticScope<>();
    childScope.setParent(enclosing);

    JSType expectedType = JSTypeNative.NO_RESOLVED_TYPE;
    JSType resolvedType = namedType.resolveInternal(registry, childScope);

    assertTypeEquals(expectedType, resolvedType);
  }

  @Test
  public void testResolveTypeViaRegistry_unknownType() {
    StaticScope enclosing = new StaticScope<>();
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "MyUnknownType";
    NamedType namedType = new NamedType(registry, reference, "MySource.java", 1, 1);

    JSType expectedType = JSTypeNative.NO_RESOLVED_TYPE;
    JSType resolvedType = namedType.resolveInternal(registry, enclosing);

    assertTypeEquals(expectedType, resolvedType);
  }

  @Test
  public void testResolveTypeViaProperties_cycle() {
    StaticScope enclosing = new StaticScope<>();
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "MyCircularType";
    NamedType namedType1 = new NamedType(registry, reference, "MySource1.java", 1, 1);
    NamedType namedType2 = new NamedType(registry, reference, "MySource2.java", 1, 1);

    StaticScope childScope = new StaticScope<>();
    childScope.setParent(enclosing);

    StaticScope childScope1 = new StaticScope();
    childScope1.setParent(childScope);

    StaticScope childScope2 = new StaticScope();
    childScope2.setParent(childScope);

    namedType1.setReferencedAndResolvedType(namedType2);
    namedType2.setReferencedAndResolvedType(namedType1);

    JSType expectedType = JSTypeNative.NO_RESOLVED_TYPE;
    JSType resolvedType = namedType1.resolveInternal(registry, childScope1);

    assertTypeEquals(expectedType, resolvedType);

    // Verify warning message was printed
    String warningMessage = "Cycle detected in inheritance chain of type " + reference;
    assertThrows(AssertionError.class, () -> {
      String actualMessage = registry.getErrors().get(0).getMessage();
      assertThat(actualMessage).isEqualTo(warningMessage);
    });
  }

  @Test
  public void testResolveTypeViaProperties_enum_element_cycle() {
    StaticScope enclosing = new StaticScope<>();
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "MyEnum";
    NamedType namedType = new NamedType(registry, reference, "MySource.java", 1, 1);

    StaticScope childScope = new StaticScope<>();
    childScope.setParent(enclosing);

    EnumType enumType = new EnumType(registry, reference, "MySource.java", 1, 1);
    EnumElementType enumElementType = new EnumElementType(
        registry, "MyEnumElement", reference, "MySource.java", 1, 1,
        JSTypeNative.STRING_TYPE);

    JSType expectedType = JSTypeNative.NO_RESOLVED_TYPE;
    JSType resolvedType = namedType.resolveInternal(registry, childScope);

    assertTypeEquals(expectedType, resolvedType);

    enumType.addElement(enumElementType);
    namedType.setReferencedAndResolvedType(enumType);

    JSType resolvedTypeAfterCycle = namedType.resolveInternal(registry, childScope);

    assertTypeEquals(expectedType, resolvedTypeAfterCycle);

    // Verify warning message was printed
    String warningMessage = "Cycle detected in inheritance chain of type " + reference;
    assertThrows(AssertionError.class, () -> {
      String actualMessage = registry.getErrors().get(0).getMessage();
      assertThat(actualMessage).isEqualTo(warningMessage);
    });
  }

  @Test
  public void testResolveTypeViaRegistry_enum() {
    StaticScope enclosing = new StaticScope<>();
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "MyEnum";
    NamedType namedType = new NamedType(registry, reference, "MySource.java", 1, 1);

    EnumType expectedEnumType = new EnumType(registry, reference, "MySource.java", 1, 1);
    JSType resolvedType = namedType.resolveInternal(registry, enclosing);

    assertTypeEquals(expectedEnumType, resolvedType);
  }

  @Test
  public void testResolveTypeViaProperties_function() {
    StaticScope enclosing = new StaticScope<>();
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "java.util.ArrayList";
    NamedType namedType = new NamedType(registry, reference, "MySource.java", 1, 1);

    StaticScope childScope = new StaticScope<>();
    childScope.setParent(enclosing);

    FunctionType expectedFunctionType = new FunctionType(
        registry, JSTypeNative.VOID_TYPE, JSTypeNative.ARRAY_TYPE, null, null);

    JSType resolvedType = namedType.resolveInternal(registry, childScope);

    assertTypeEquals(expectedFunctionType, resolvedType);
  }

  @Test
  public void testResolveTypeViaProperties_interface() {
    StaticScope enclosing = new StaticScope<>();
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "java.util.List";
    NamedType namedType = new NamedType(registry, reference, "MySource.java", 1, 1);

    StaticScope childScope = new StaticScope<>();
    childScope.setParent(enclosing);

    InterfaceType expectedInterfaceType = new InterfaceType(
        registry, reference, "MySource.java", 1, 1);

    JSType resolvedType = namedType.resolveInternal(registry, childScope);

    assertTypeEquals(expectedInterfaceType, resolvedType);
  }
}