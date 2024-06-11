import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.testing.Forge;
import com.google.javascript.rhino.testing.PropertyTester;
import com.google.javascript.rhino.testing.TypeTester;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NamedTypeTest {

  @Mock
  private ErrorReporter errorReporter;

  @Mock
  private StaticScope<JSType> staticScope;

  @Mock
  private StaticSlot<JSType> staticSlot;

  @Mock
  private JSTypeRegistry typeRegistry;

  @Mock
  private JSTypeFactory typeFactory;

  @Mock
  private JSType referencedType;

  @Mock
  private FunctionType functionType;

  @Mock
  private EnumType enumType;

  @Mock
  private EnumElementType enumElementType;

  @Mock
  private Node node;

  private NamedType namedType;

  @Before
  public void setUp() {
    when(typeRegistry.getType("Type1")).thenReturn(referencedType);
    when(typeFactory.getFunctionType(null, null)).thenReturn(functionType);
    when(typeFactory.getEnumType("EnumType")).thenReturn(enumType);
    when(typeFactory.getEnumElementType("EnumElement", enumType)).thenReturn(enumElementType);

    namedType = new NamedType(typeRegistry, "Type1", "SourceName", 1, 1);
  }

  @Test
  public void testIsNamedType() {
    assertThat(namedType.isNamedType()).isTrue();
  }

  @Test
  public void testIsNominalType() {
    assertThat(namedType.isNominalType()).isTrue();
  }

  @Test
  public void testIsResolved() {
    assertThat(namedType.isResolved()).isFalse();
  }

  @Test
  public void testGetReferenceName() {
    assertThat(namedType.getReferenceName()).isEqualTo("Type1");
  }

  @Test
  public void testToStringHelper() {
    assertThat(namedType.toStringHelper(false)).isEqualTo("Type1");
  }

  @Test
  public void testHasReferenceName() {
    assertThat(namedType.hasReferenceName()).isTrue();
  }

  @Test
  public void testResolveViaRegistry() {
    when(staticScope.getType("ObjectType")).thenReturn(referencedType);

    namedType = new NamedType(typeRegistry, "ObjectType", "SourceName", 1, 1);
    namedType.resolveInternal(errorReporter, staticScope);

    assertThat(namedType.getReferencedType()).isSameAs(referencedType);
  }

  @Test
  public void testResolveViaProperties() {
    when(staticSlot.getType()).thenReturn(null);
    when(staticScope.getSlot("ObjectType")).thenReturn(staticSlot);

    when(staticSlot.getType()).thenReturn(functionType);

    when(functionType.getInstanceType()).thenReturn(referencedType);

    namedType = new NamedType(typeRegistry, "ObjectType.instanceType", "SourceName", 1, 1);
    namedType.resolveInternal(errorReporter, staticScope);

    assertThat(namedType.getReferencedType()).isSameAs(referencedType);
  }

  @Test
  public void testResolveViaEnumType() {
    when(staticSlot.getType()).thenReturn(enumType);

    when(enumType.getElementsType()).thenReturn(enumElementType);

    namedType = new NamedType(typeRegistry, "EnumType.EnumElement", "SourceName", 1, 1);
    namedType.resolveInternal(errorReporter, staticScope);

    assertThat(namedType.getReferencedType()).isSameAs(enumElementType.getPrimitiveType());
  }

  @Test
  public void testResolveViaUnknownType() {
    when(staticSlot.getType()).thenReturn(null);

    namedType = new NamedType(typeRegistry, "UnknownType", "SourceName", 1, 1);
    namedType.resolveInternal(errorReporter, staticScope);

    assertThat(namedType.getReferencedType()).isSameAs(JSTypeNative.NO_RESOLVED_TYPE);
  }

  @Test
  public void testResolveViaNoObjectType() {
    when(staticSlot.getType()).thenReturn(JSTypeNative.NO_OBJECT_TYPE);

    namedType = new NamedType(typeRegistry, "NoObjectType", "SourceName", 1, 1);
    namedType.resolveInternal(errorReporter, staticScope);

    assertThat(namedType.getReferencedType()).isSameAs(JSTypeNative.NO_OBJECT_TYPE);
  }

  @Test
  public void testResolveViaFunctionType() {
    when(staticSlot.getType()).thenReturn(functionType);

    namedType = new NamedType(typeRegistry, "FunctionType", "SourceName", 1, 1);
    namedType.resolveInternal(errorReporter, staticScope);

    assertThat(namedType.getReferencedType()).isSameAs(JSTypeNative.NO_RESOLVED_TYPE);
  }

  @Test
  public void testResolveViaUnknownTypeWithValidator() {
    JSType validatorType = Forge.objectType("ValidatorType");
    when(typeRegistry.getType("ValidatorType")).thenReturn(validatorType);

    PropertyTester propertyTester = new PropertyTester();
    when(errorReporter.attachErrorHandler(propertyTester)).thenReturn(propertyTester);

    namedType = new NamedType(typeRegistry, "UnknownTypeWithValidator", "SourceName", 1, 1);
    namedType.setValidator(new TypeTester(validatorType));

    namedType.resolveInternal(errorReporter, staticScope);

    propertyTester.assertWarning("Bad type annotation. Unknown type UnknownTypeWithValidator",
        "SourceName", 1, 1);
  }

  @Test
  public void testResolveWithCycle() {
    when(staticScope.getType("ObjectType")).thenReturn(referencedType);

    when(staticScope.getSlot("ObjectType")).thenReturn(staticSlot);

    when(staticSlot.getType()).thenReturn(referencedType);

    namedType = new NamedType(typeRegistry, "ObjectType", "SourceName", 1, 1);
    namedType.resolveInternal(errorReporter, staticScope);

    assertThat(namedType.getReferencedType()).isSameAs(referencedType);

    namedType.resolveInternal(errorReporter, staticScope);

    namedType.getReferencedType(); // Trigger warning
  }
}