import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

import org.junit.Before;
import org.junit.Test;

public class FunctionBuilderTest {

  private JSTypeRegistry registry;
  private NodeFactory nodeFactory;
  private FunctionParamBuilder paramBuilder;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    nodeFactory = mock(NodeFactory.class);
    paramBuilder = new FunctionParamBuilder(registry, nodeFactory);
  }

  @Test
  public void testFunctionBuilderWithAllFields() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "testFunction";
    Node sourceNode = nodeFactory.createIntLiteral(1);
    Node parametersNode = paramBuilder.withName("param1").build().build();
    ObjectType returnType = new ObjectType(registry, null);
    ObjectType typeOfThis = new ObjectType(registry, null);
    String templateTypeName = "TemplateTypeName";
    functionBuilder.withName(name)
        .withSourceNode(sourceNode)
        .withParams(paramBuilder.withName("param1").build())
        .withReturnType(returnType)
        .withTypeOfThis(typeOfThis)
        .withTemplateName(templateTypeName)
        .forConstructor();

    FunctionType functionType = functionBuilder.build();

    assertNotNull(functionType);
    assertNotNull(functionType.getName());
    assertNotNull(functionType.getSource());
    assertNotNull(functionType.getParametersNode());
    assertNotNull(functionType.getReturnType());
    assertNotNull(functionType.getTypeOfThis());
    assertNotNull(functionType.getTemplateTypeName());
    assertTrue(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithOnlySourceNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    Node sourceNode = nodeFactory.createIntLiteral(1);
    functionBuilder.withSourceNode(sourceNode);

    FunctionType functionType = functionBuilder.build();

    assertNotNull(functionType);
    assertNull(functionType.getName());
    assertNotNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithOnlyReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    JSType returnType = new ObjectType(registry, null);
    functionBuilder.withReturnType(returnType);

    FunctionType functionType = functionBuilder.build();

    assertNotNull(functionType);
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNotNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithOnlyTypeOfThis() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    ObjectType typeOfThis = new ObjectType(registry, null);
    functionBuilder.withTypeOfThis(typeOfThis);

    FunctionType functionType = functionBuilder.build();

    assertNotNull(functionType);
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNotNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithOnlyTemplateTypeName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String templateTypeName = "TemplateTypeName";
    functionBuilder.withTemplateName(templateTypeName);

    FunctionType functionType = functionBuilder.build();

    assertNotNull(functionType);
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNotNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithOnlyIsConstructor() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.forConstructor();

    FunctionType functionType = functionBuilder.build();

    assertNotNull(functionType);
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertTrue(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithOnlyIsNativeObjectType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    functionBuilder.forNativeType();

    FunctionType functionType = functionBuilder.build();

    assertNotNull(functionType);
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertTrue(functionType.isNativeObjectType());
  }

  @Test
  public void testFunctionBuilderWithAllFieldsAndCopy() {
    FunctionBuilder functionBuilder1 = new FunctionBuilder(registry);
    String name1 = "testFunction1";
    Node sourceNode1 = nodeFactory.createIntLiteral(1);
    Node parametersNode1 = paramBuilder.withName("param1").build().build();
    ObjectType returnType1 = new ObjectType(registry, null);
    ObjectType typeOfThis1 = new ObjectType(registry, null);
    String templateTypeName1 = "TemplateTypeName";
    functionBuilder1.withName(name1)
        .withSourceNode(sourceNode1)
        .withParams(paramBuilder.withName("param1").build())
        .withReturnType(returnType1)
        .withTypeOfThis(typeOfThis1)
        .withTemplateName(templateTypeName1)
        .forConstructor();

    FunctionBuilder functionBuilder2 = new FunctionBuilder(registry);
    FunctionType functionType1 = functionBuilder1.build();
    functionBuilder2.copyFromOtherFunction(functionType1);

    FunctionType functionType2 = functionBuilder2.build();

    assertNotNull(functionType2);
    assertNotNull(functionType2.getName());
    assertNotNull(functionType2.getSource());
    assertNotNull(functionType2.getParametersNode());
    assertNotNull(functionType2.getReturnType());
    assertNotNull(functionType2.getTypeOfThis());
    assertNotNull(functionType2.getTemplateTypeName());
    assertTrue(functionType2.isConstructor());
    assertFalse(functionType2.isNativeObjectType());
    assertEquals(functionType1.getName(), functionType2.getName());
    assertEquals(functionType1.getSource(), functionType2.getSource());
    assertEquals(functionType1.getParametersNode(), functionType2.getParametersNode());
    assertEquals(functionType1.getReturnType(), functionType2.getReturnType());
    assertEquals(functionType1.getTypeOfThis(), functionType2.getTypeOfThis());
    assertEquals(functionType1.getTemplateTypeName(), functionType2.getTemplateTypeName());
    assertTrue(functionType2.isConstructor());
    assertFalse(functionType2.isNativeObjectType());
  }
}