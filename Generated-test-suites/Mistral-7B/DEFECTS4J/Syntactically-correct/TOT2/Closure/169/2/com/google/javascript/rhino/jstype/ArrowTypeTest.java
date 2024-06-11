import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JSTypeRegistryRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;

public class ArrowTypeTest {
  @org.junit.Before
  public void setup() {
    JSTypeRegistryRegistry.setRegistry(new JSTypeRegistry());
  }

  @org.junit.Test
  public void testArrowTypeConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newList();
    Node param1 = Node.newIdent("a");
    param1.setJSType(registry.createNumberType());
    Node param2 = Node.newIdent("b");
    param2.setJSType(registry.createNumberType());
    parameters.addChild(param1);
    parameters.addChild(param2);

    ArrowType arrowType = new ArrowType(registry, parameters, registry.createNumberType());

    assertEquals("ArrowType:[ArrowType:[NumberType, NumberType]]", arrowType.toString());
  }

  @org.junit.Test
  public void testArrowTypeIsSubtype() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    ErrorReporter errorReporter = mock(ErrorReporter.class);

    ArrowType arrowType1 = new ArrowType(registry, Node.newList(), registry.createNumberType(), false);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(), registry.createStringType(), false);
    ArrowType arrowType3 = new ArrowType(registry, Node.newList(Node.newIdent("a"), Node.newIdent("b")), registry.createNumberType(), false);

    ArrowType arrowType4 = new ArrowType(registry, Node.newList(Node.newIdent("a"), Node.newIdent("b")), registry.createStringType(), false);

    ArrowType arrowType5 = new ArrowType(registry, null, registry.createBooleanType(), true);

    ArrowType arrowType6 = new ArrowType(registry, Node.newList(), null, true);

    ArrowType arrowType7 = new ArrowType(registry, Node.newList(Node.newIdent("a")), registry.createNumberType(), true);

    when(registry.createParametersWithVarArgs(any(JSType.class))).thenReturn(parameters);
    when(registry.createNumberType()).thenReturn(registry.createNumberType());
    when(registry.createStringType()).thenReturn(registry.createStringType());
    when(registry.createBooleanType()).thenReturn(registry.createBooleanType());

    verify(registry, times(7)).createParametersWithVarArgs(any(JSType.class));
    verify(registry, times(3)).createNumberType();
    verify(registry, times(1)).createStringType();
    verify(registry, times(1)).createBooleanType();

    assertTrue(arrowType1.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType4));
    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
    assertFalse(arrowType1.isSubtype(arrowType3));
    assertFalse(arrowType3.isSubtype(arrowType1));
    assertFalse(arrowType5.isSubtype(arrowType3));
    assertFalse(arrowType3.isSubtype(arrowType5));
    assertTrue(arrowType4.isSubtype(arrowType7));
    assertFalse(arrowType7.isSubtype(arrowType4));
    assertTrue(arrowType6.isSubtype(arrowType1));
    assertFalse(arrowType1.isSubtype(arrowType6));
  }
}