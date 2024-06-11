import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {

  private JSTypeRegistry registry;
  private FunctionBuilder functionBuilder;
  private FunctionParamBuilder paramBuilder;
  private ObjectType typeOfThis;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    functionBuilder = new FunctionBuilder(registry);
    paramBuilder = mock(FunctionParamBuilder.class);
    typeOfThis = mock(ObjectType.class);
  }

  @Test
  public void testFunctionBuilderWithDefaultValues() {
    FunctionType functionType = functionBuilder.build();
    assertNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeType());
  }

  @Test
  public void testFunctionBuilderWithName() {
    when(registry.createName("testFunction")).thenReturn(new Name("testFunction"));
    functionBuilder.withName("testFunction");
    FunctionType functionType = functionBuilder.build();
    assertEquals("testFunction", functionType.getReferenceName().getName());
  }

  @Test
  public void testFunctionBuilderWithSourceNode() {
    Node sourceNode = mock(Node.class);
    functionBuilder.withSourceNode(sourceNode);
    FunctionType functionType = functionBuilder.build();
    assertEquals(sourceNode, functionType.getSource());
  }

  @Test
  public void testFunctionBuilderWithParams() {
    when(paramBuilder.build()).thenReturn(new Node(""));
    functionBuilder.withParams(paramBuilder);
    FunctionType functionType = functionBuilder.build();
    assertEquals(new Node(""), functionType.getParametersNode());
  }

  @Test
  public void testFunctionBuilderWithReturnType() {
    JSType returnType = mock(JSType.class);
    functionBuilder.withReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertEquals(returnType, functionType.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSType returnType = mock(JSType.class);
    functionBuilder.withInferredReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertEquals(returnType, functionType.getReturnType());
    assertTrue(functionType.isInferredReturnType());
  }

  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    functionBuilder.withTypeOfThis(typeOfThis);
    FunctionType functionType = functionBuilder.build();
    assertEquals(typeOfThis, functionType.getTypeOfThis());
  }

  @Test
  public void testFunctionBuilderWithTemplateTypeName() {
    functionBuilder.withTemplateName("testTemplate");
    FunctionType functionType = functionBuilder.build();
    assertEquals("testTemplate", functionType.getTemplateTypeName());
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    functionBuilder.forConstructor();
    FunctionType functionType = functionBuilder.build();
    assertTrue(functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    functionBuilder.forNativeType();
    FunctionType functionType = functionBuilder.build();
    assertTrue(functionType.isNativeType());
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    Name name = mock(Name.class);
    Node sourceNode = mock(Node.class);
    Node parametersNode = mock(Node.class);
    ArrowType arrowType = mock(ArrowType.class);
    ObjectType typeOfThis = mock(ObjectType.class);
    String templateTypeName = "testTemplate";
    when(registry.createName("testFunction")).thenReturn(name);
    when(registry.createArrowType(parametersNode, arrowType)).thenReturn(arrowType);

    functionBuilder.copyFromOtherFunction(new FunctionType(registry, name, sourceNode, arrowType, typeOfThis, templateTypeName, true, false));

    FunctionType functionType = functionBuilder.build();

    assertEquals("testFunction", functionType.getReferenceName().getName());
    assertEquals(sourceNode, functionType.getSource());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(arrowType, functionType.getReturnType());
    assertEquals(typeOfThis, functionType.getTypeOfThis());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
    assertTrue(functionType.isConstructor());
    assertFalse(functionType.isNativeType());
  }
}