import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

import org.junit.Test;

public class FunctionBuilderTest {

  private JSTypeRegistry registry = mock(JSTypeRegistry.class);
  private FunctionBuilder functionBuilder = null;

  @Test
  public void testFunctionBuilderWithNameAndReturnType() {
    when(registry.createType("String")).thenReturn(mock(ObjectType.class));

    functionBuilder = new FunctionBuilder(registry).withName("testFunction")
        .withReturnType(mock(ObjectType.class));
    FunctionType functionType = functionBuilder.build();

    assertNotNull(functionType);
    assertNotNull(functionType.getName());
    assertNotNull(functionType.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithNameAndReturnTypeAndSourceNode() {
    when(registry.createType("String")).thenReturn(mock(ObjectType.class));

    Node sourceNode = mock(Node.class);
    functionBuilder = new FunctionBuilder(registry).withName("testFunction")
        .withReturnType(mock(ObjectType.class))
        .withSourceNode(sourceNode);
    FunctionType functionType = functionBuilder.build();

    assertNotNull(functionType);
    assertNotNull(functionType.getName());
    assertNotNull(functionType.getReturnType());
    assertNotNull(functionType.getSource());
  }

  @Test
  public void testFunctionBuilderWithNameAndParametersAndReturnType() {
    when(registry.createType("String")).thenReturn(mock(ObjectType.class));
    when(registry.createType("Number")).thenReturn(mock(ObjectType.class));

    FunctionParamBuilder paramBuilder = mock(FunctionParamBuilder.class);
    when(paramBuilder.build()).thenReturn(mock(Node.class));

    Node parametersNode = mock(Node.class);
    when(parametersNode.getFirstChild()).thenReturn(paramBuilder);

    functionBuilder = new FunctionBuilder(registry).withName("testFunction")
        .withParams(paramBuilder)
        .withReturnType(mock(ObjectType.class));
    FunctionType functionType = functionBuilder.build();

    assertNotNull(functionType);
    assertNotNull(functionType.getName());
    assertNotNull(functionType.getReturnType());
    assertNotNull(functionType.getParametersNode());
  }

  @Test
  public void testFunctionBuilderWithNameAndParametersAndReturnTypeAndSourceNode() {
    when(registry.createType("String")).thenReturn(mock(ObjectType.class));
    when(registry.createType("Number")).thenReturn(mock(ObjectType.class));

    FunctionParamBuilder paramBuilder = mock(FunctionParamBuilder.class);
    when(paramBuilder.build()).thenReturn(mock(Node.class));

    Node parametersNode = mock(Node.class);
    Node sourceNode = mock(Node.class);

    when(parametersNode.getFirstChild()).thenReturn(paramBuilder);
    when(sourceNode.getFirstChild()).thenReturn(parametersNode);

    functionBuilder = new FunctionBuilder(registry).withName("testFunction")
        .withParams(paramBuilder)
        .withReturnType(mock(ObjectType.class))
        .withSourceNode(sourceNode);
    FunctionType functionType = functionBuilder.build();

    assertNotNull(functionType);
    assertNotNull(functionType.getName());
    assertNotNull(functionType.getReturnType());
    assertNotNull(functionType.getParametersNode());
    assertNotNull(functionType.getSource());
  }

  @Test
  public void testFunctionBuilderWithNameAndParametersAndReturnTypeAndTypeOfThis() {
    when(registry.createType("String")).thenReturn(mock(ObjectType.class));
    when(registry.createType("Number")).thenReturn(mock(ObjectType.class));
    when(registry.createObjectType("Object")).thenReturn(mock(ObjectType.class));

    FunctionParamBuilder paramBuilder = mock(FunctionParamBuilder.class);
    when(paramBuilder.build()).thenReturn(mock(Node.class));

    Node parametersNode = mock(Node.class);
    Node sourceNode = mock(Node.class);

    when(parametersNode.getFirstChild()).thenReturn(paramBuilder);
    when(sourceNode.getFirstChild()).thenReturn(parametersNode);

    ObjectType typeOfThis = mock(ObjectType.class);

    functionBuilder = new FunctionBuilder(registry).withName("testFunction")
        .withParams(paramBuilder)
        .withReturnType(mock(ObjectType.class))
        .withTypeOfThis(typeOfThis)
        .withSourceNode(sourceNode);
    FunctionType functionType = functionBuilder.build();

    assertNotNull(functionType);
    assertNotNull(functionType.getName());
    assertNotNull(functionType.getReturnType());
    assertNotNull(functionType.getParametersNode());
    assertNotNull(functionType.getTypeOfThis());
  }

  @Test
  public void testFunctionBuilderWithNameAndParametersAndReturnTypeAndTemplateTypeName() {
    when(registry.createType("String")).thenReturn(mock(ObjectType.class));
    when(registry.createType("Number")).thenReturn(mock(ObjectType.class));

    FunctionParamBuilder paramBuilder = mock(FunctionParamBuilder.class);
    when(paramBuilder.build()).thenReturn(mock(Node.class));

    Node parametersNode = mock(Node.class);
    Node sourceNode = mock(Node.class);

    when(parametersNode.getFirstChild()).thenReturn(paramBuilder);
    when(sourceNode.getFirstChild()).thenReturn(parametersNode);

    String templateTypeName = "TestFunctionTemplate";

    functionBuilder = new FunctionBuilder(registry).withName("testFunction")
        .withParams(paramBuilder)
        .withReturnType(mock(ObjectType.class))
        .withTemplateName(templateTypeName)
        .withSourceNode(sourceNode);
    FunctionType functionType = functionBuilder.build();

    assertNotNull(functionType);
    assertNotNull(functionType.getName());
    assertNotNull(functionType.getReturnType());
    assertNotNull(functionType.getParametersNode());
    assertNotNull(functionType.getTemplateTypeName());
  }

  @Test
  public void testFunctionBuilderWithNameAndParametersAndReturnTypeAndInferredReturnType() {
    when(registry.createType("String")).thenReturn(mock(ObjectType.class));
    when(registry.createType("Number")).thenReturn(mock(ObjectType.class));

    FunctionParamBuilder paramBuilder = mock(FunctionParamBuilder.class);
    when(paramBuilder.build()).thenReturn(mock(Node.class));

    Node parametersNode = mock(Node.class);
    Node sourceNode = mock(Node.class);

    when(parametersNode.getFirstChild()).thenReturn(paramBuilder);
    when(sourceNode.getFirstChild()).thenReturn(parametersNode);

    JSType inferredReturnType = mock(ObjectType.class);

    functionBuilder = new FunctionBuilder(registry).withName("testFunction")
        .withParams(paramBuilder)
        .withReturnType(null)
        .withInferredReturnType(inferredReturnType)
        .withSourceNode(sourceNode);
    FunctionType functionType = functionBuilder.build();

    assertNotNull(functionType);
    assertNotNull(functionType.getName());
    assertNull(functionType.getReturnType());
    assertNotNull(functionType.getParametersNode());
    assertNotNull(functionType.getInferredReturnType());
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    when(registry.createType("String")).thenReturn(mock(ObjectType.class));

    functionBuilder = new FunctionBuilder(registry).withName("testConstructor")
        .forConstructor();
    FunctionType functionType = functionBuilder.build();

    assertNotNull(functionType);
    assertTrue(functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    when(registry.createType("String")).thenReturn(mock(ObjectType.class));

    functionBuilder = new FunctionBuilder(registry).forNativeType();
    FunctionType functionType = functionBuilder.build();

    assertNotNull(functionType);
    assertTrue(functionType.isNativeType());
  }
}