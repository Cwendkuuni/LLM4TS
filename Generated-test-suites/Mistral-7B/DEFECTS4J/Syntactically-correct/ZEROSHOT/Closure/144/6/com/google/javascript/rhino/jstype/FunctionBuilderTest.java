import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class FunctionBuilderTest {
  private FunctionBuilder functionBuilder;
  private FunctionType functionType;
  private FunctionParamBuilder functionParamBuilder;
  private ObjectType objectType;
  private Node node;
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    functionBuilder = new FunctionBuilder(registry);
    functionParamBuilder = mock(FunctionParamBuilder.class);
    objectType = mock(ObjectType.class);
    node = mock(Node.class);
    functionType = mock(FunctionType.class);

    when(functionParamBuilder.build()).thenReturn(node);
    when(registry.createVoidType()).thenReturn(JSType.VOID);
  }

  @Test
  public void testFunctionBuilderWithDefaultValues() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    FunctionType functionType = functionBuilder.build();

    assertNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderSimpleFunction() {
    String name = "testFunction";
    Node source = Node.INT_LITERAL(1);
    ArrowType arrowType = new ArrowType(registry, Node.NULL, JSType.INT, false);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withName(name)
        .withSourceNode(source).withReturnType(JSType.INT).build();

    FunctionType functionType = functionBuilder.build();

    assertEquals(name, functionType.getReferenceName());
    assertEquals(source, functionType.getSource());
    assertEquals(Node.NULL, functionType.getParametersNode());
    assertEquals(arrowType, functionType.getParametersType());
    assertEquals(JSType.INT, functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderFunctionWithParams() {
    String name = "testFunction";
    Node source = Node.INT_LITERAL(1);
    FunctionParamBuilder functionParamBuilder = mock(FunctionParamBuilder.class);
    Node parametersNode = Node.NAME("param1");

    when(functionParamBuilder.build()).thenReturn(parametersNode);

    ArrowType arrowType = new ArrowType(registry, parametersNode, JSType.INT, false);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withName(name)
        .withSourceNode(source).withReturnType(JSType.INT)
        .withParams(functionParamBuilder).build();

    FunctionType functionType = functionBuilder.build();

    assertEquals(name, functionType.getReferenceName());
    assertEquals(source, functionType.getSource());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(arrowType, functionType.getParametersType());
    assertEquals(JSType.INT, functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderFunctionWithTemplateTypeName() {
    String name = "testFunction";
    Node source = Node.INT_LITERAL(1);
    FunctionParamBuilder functionParamBuilder = mock(FunctionParamBuilder.class);
    Node parametersNode = Node.NAME("param1");
    String templateTypeName = "TestTemplate";

    when(functionParamBuilder.build()).thenReturn(parametersNode);

    ArrowType arrowType = new ArrowType(registry, parametersNode, JSType.INT, false);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withName(name)
        .withSourceNode(source).withReturnType(JSType.INT)
        .withParams(functionParamBuilder).withTemplateName(templateTypeName).build();

    FunctionType functionType = functionBuilder.build();

    assertEquals(name, functionType.getReferenceName());
    assertEquals(source, functionType.getSource());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(arrowType, functionType.getParametersType());
    assertEquals(JSType.INT, functionType.getReturnType());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
    assertNull(functionType.getTypeOfThis());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderFunctionWithTypeOfThis() {
    String name = "testFunction";
    Node source = Node.INT_LITERAL(1);
    FunctionParamBuilder functionParamBuilder = mock(FunctionParamBuilder.class);
    Node parametersNode = Node.NAME("param1");
    ObjectType objectType = mock(ObjectType.class);

    when(functionParamBuilder.build()).thenReturn(parametersNode);
    when(registry.createObjectType("TestObject")).thenReturn(objectType);

    ArrowType arrowType = new ArrowType(registry, parametersNode, JSType.INT, false);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withName(name)
        .withSourceNode(source).withReturnType(JSType.INT)
        .withParams(functionParamBuilder).withTypeOfThis(objectType).build();

    FunctionType functionType = functionBuilder.build();

    assertEquals(name, functionType.getReferenceName());
    assertEquals(source, functionType.getSource());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(arrowType, functionType.getParametersType());
    assertEquals(JSType.INT, functionType.getReturnType());
    assertSame(objectType, functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderFunctionWithInferredReturnType() {
    String name = "testFunction";
    Node source = Node.INT_LITERAL(1);
    FunctionParamBuilder functionParamBuilder = mock(FunctionParamBuilder.class);
    Node parametersNode = Node.NAME("param1");
    ObjectType objectType = mock(ObjectType.class);

    when(functionParamBuilder.build()).thenReturn(parametersNode);
    when(registry.createObjectType("TestObject")).thenReturn(objectType);

    ArrowType arrowType = new ArrowType(registry, parametersNode, Node.NULL, false);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withName(name)
        .withSourceNode(source).withParams(functionParamBuilder)
        .withTypeOfThis(objectType).withInferredReturnType(JSType.INT).build();

    FunctionType functionType = functionBuilder.build();

    assertEquals(name, functionType.getReferenceName());
    assertEquals(source, functionType.getSource());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(arrowType, functionType.getParametersType());
    assertEquals(JSType.INT, functionType.getReturnType());
    assertSame(objectType, functionType.getTypeOfThis());
    assertTrue(functionType.isInferredReturnType());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderFunctionWithNativeType() {
    String name = "testFunction";
    Node source = Node.INT_LITERAL(1);
    FunctionParamBuilder functionParamBuilder = mock(FunctionParamBuilder.class);
    Node parametersNode = Node.NAME("param1");

    when(functionParamBuilder.build()).thenReturn(parametersNode);

    ArrowType arrowType = new ArrowType(registry, parametersNode, JSType.INT, false);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withName(name)
        .withSourceNode(source).withReturnType(JSType.INT)
        .withParams(functionParamBuilder).forNativeType().build();

    FunctionType functionType = functionBuilder.build();

    assertEquals(name, functionType.getReferenceName());
    assertEquals(source, functionType.getSource());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(arrowType, functionType.getParametersType());
    assertEquals(JSType.INT, functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertTrue(functionType.isNativeObjectType());
    assertFalse(functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderConstructorFunction() {
    String name = "testFunction";
    Node source = Node.INT_LITERAL(1);
    FunctionParamBuilder functionParamBuilder = mock(FunctionParamBuilder.class);
    Node parametersNode = Node.NAME("param1");

    when(functionParamBuilder.build()).thenReturn(parametersNode);

    ArrowType arrowType = new ArrowType(registry, parametersNode, Node.NULL, false);
    FunctionBuilder functionBuilder = new FunctionBuilder(registry).withName(name)
        .withSourceNode(source).withReturnType(JSType.VOID)
        .withParams(functionParamBuilder).forConstructor().build();

    FunctionType functionType = functionBuilder.build();

    assertEquals(name, functionType.getReferenceName());
    assertEquals(source, functionType.getSource());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(arrowType, functionType.getParametersType());
    assertEquals(JSType.VOID, functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertTrue(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    String name = "testFunction";
    Node source = Node.INT_LITERAL(1);
    FunctionParamBuilder functionParamBuilder = mock(FunctionParamBuilder.class);
    Node parametersNode = Node.NAME("param1");
    ObjectType objectType = mock(ObjectType.class);
    String templateTypeName = "TestTemplate";

    when(functionParamBuilder.build()).thenReturn(parametersNode);

    ArrowType arrowType = new ArrowType(registry, parametersNode, JSType.INT, false);
    FunctionType originalFunctionType = new FunctionType(registry, name, source,
        arrowType, objectType, templateTypeName, true, false);

    FunctionBuilder functionBuilder = new FunctionBuilder(registry).copyFromOtherFunction(originalFunctionType);

    FunctionType functionType = functionBuilder.build();

    assertEquals(name, functionType.getReferenceName());
    assertEquals(source, functionType.getSource());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(arrowType, functionType.getParametersType());
    assertEquals(JSType.INT, functionType.getReturnType());
    assertSame(objectType, functionType.getTypeOfThis());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
    assertTrue(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }
}