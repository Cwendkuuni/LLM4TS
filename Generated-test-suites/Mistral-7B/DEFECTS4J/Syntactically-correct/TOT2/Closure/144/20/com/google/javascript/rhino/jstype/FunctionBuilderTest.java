import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

public class FunctionBuilderTest {

  JSTypeRegistry registry;
  FunctionBuilder functionBuilder;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    functionBuilder = new FunctionBuilder(registry);
  }

  @Test
  public void testFunctionBuilderConstructor() {
    assertNotNull(functionBuilder);
  }

  @Test
  public void testFunctionBuilderWithName() {
    String name = "myFunction";
    FunctionBuilder functionBuilder = functionBuilder.withName(name);
    assertEquals(name, functionBuilder.getName());
  }

  @Test
  public void testFunctionBuilderWithSourceNode() {
    Node sourceNode = new Node();
    FunctionBuilder functionBuilder = functionBuilder.withSourceNode(sourceNode);
    assertSame(sourceNode, functionBuilder.getSourceNode());
  }

  @Test
  public void testFunctionBuilderWithParams() {
    FunctionParamBuilder params = new FunctionParamBuilder();
    FunctionBuilder functionBuilder = functionBuilder.withParams(params);
    assertSame(params.build(), functionBuilder.getParametersNode());
  }

  @Test
  public void testFunctionBuilderWithParamsNode() {
    Node parametersNode = new Node();
    FunctionBuilder functionBuilder = functionBuilder.withParamsNode(parametersNode);
    assertSame(parametersNode, functionBuilder.getParametersNode());
  }

  @Test
  public void testFunctionBuilderWithReturnType() {
    JSType returnType = new JSType();
    FunctionBuilder functionBuilder = functionBuilder.withReturnType(returnType);
    assertSame(returnType, functionBuilder.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSType returnType = new JSType();
    FunctionBuilder functionBuilder = functionBuilder.withInferredReturnType(returnType);
    assertTrue(functionBuilder.isInferredReturnType());
  }

  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    ObjectType typeOfThis = new ObjectType();
    FunctionBuilder functionBuilder = functionBuilder.withTypeOfThis(typeOfThis);
    assertSame(typeOfThis, functionBuilder.getTypeOfThis());
  }

  @Test
  public void testFunctionBuilderWithTemplateName() {
    String templateTypeName = "myTemplate";
    FunctionBuilder functionBuilder = functionBuilder.withTemplateName(templateTypeName);
    assertEquals(templateTypeName, functionBuilder.getTemplateTypeName());
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    FunctionBuilder functionBuilder = functionBuilder.forConstructor();
    assertTrue(functionBuilder.isConstructor());
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    FunctionBuilder functionBuilder = functionBuilder.forNativeType();
    assertTrue(functionBuilder.isNativeType());
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    FunctionType otherType = new FunctionType();
    FunctionBuilder functionBuilder = functionBuilder.copyFromOtherFunction(otherType);
    assertEquals(otherType.getName(), functionBuilder.getName());
    assertEquals(otherType.getSource(), functionBuilder.getSourceNode());
    assertEquals(otherType.getParametersNode(), functionBuilder.getParametersNode());
    assertEquals(otherType.getReturnType(), functionBuilder.getReturnType());
    assertEquals(otherType.getTypeOfThis(), functionBuilder.getTypeOfThis());
    assertEquals(otherType.getTemplateTypeName(), functionBuilder.getTemplateTypeName());
    assertTrue(functionBuilder.isConstructor(), otherType.isConstructor());
    assertTrue(functionBuilder.isNativeType(), otherType.isNativeType());
  }

  @Test
  public void testFunctionBuilderBuild() {
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
  }

  @Test(expected = NullPointerException.class)
  public void testFunctionBuilderNullConstructorArgument() {
    JSTypeRegistry registry = null;
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    // Should throw a NullPointerException
  }

  @Test(expected = NullPointerException.class)
  public void testFunctionBuilderNullNameArgument() {
    FunctionBuilder functionBuilder = functionBuilder.withName(null);
    // Should throw a NullPointerException
  }

  @Test(expected = NullPointerException.class)
  public void testFunctionBuilderNullSourceNodeArgument() {
    Node sourceNode = null;
    FunctionBuilder functionBuilder = functionBuilder.withSourceNode(sourceNode);
    // Should throw a NullPointerException
  }

  @Test(expected = NullPointerException.class)
  public void testFunctionBuilderNullParamsArgument() {
    FunctionParamBuilder params = null;
    FunctionBuilder functionBuilder = functionBuilder.withParams(params);
    // Should throw a NullPointerException
  }

  @Test(expected = NullPointerException.class)
  public void testFunctionBuilderNullParamsNodeArgument() {
    Node parametersNode = null;
    FunctionBuilder functionBuilder = functionBuilder.withParamsNode(parametersNode);
    // Should throw a NullPointerException
  }

  @Test(expected = NullPointerException.class)
  public void testFunctionBuilderNullReturnTypeArgument() {
    JSType returnType = null;
    FunctionBuilder functionBuilder = functionBuilder.withReturnType(returnType);
    // Should throw a NullPointerException
  }

  @Test(expected = NullPointerException.class)
  public void testFunctionBuilderNullInferredReturnTypeArgument() {
    JSType inferredReturnType = null;
    FunctionBuilder functionBuilder = functionBuilder.withInferredReturnType(inferredReturnType);
    // Should throw a NullPointerException
  }

  @Test(expected = NullPointerException.class)
  public void testFunctionBuilderNullTypeOfThisArgument() {
    ObjectType typeOfThis = null;
    FunctionBuilder functionBuilder = functionBuilder.withTypeOfThis(typeOfThis);
    // Should throw a NullPointerException
  }

  @Test(expected = NullPointerException.class)
  public void testFunctionBuilderNullTemplateTypeNameArgument() {
    String templateTypeName = null;
    FunctionBuilder functionBuilder = functionBuilder.withTemplateName(templateTypeName);
    // Should throw a NullPointerException
  }

  @Test
  public void testFunctionBuilderWithEmptyConstructor() {
    FunctionType functionType = functionBuilder.build();
    assertNull(functionType.getName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeType());
  }

  @Test
  public void testFunctionBuilderWithNullReturnType() {
    JSType returnType = null;
    FunctionBuilder functionBuilder = functionBuilder.withReturnType(returnType);
    assertNull(functionBuilder.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithInferredNullReturnType() {
    JSType inferredReturnType = null;
    FunctionBuilder functionBuilder = functionBuilder.withInferredReturnType(inferredReturnType);
    assertFalse(functionBuilder.isInferredReturnType());
  }

  @Test
  public void testFunctionBuilderWithNullTypeOfThis() {
    ObjectType typeOfThis = null;
    FunctionBuilder functionBuilder = functionBuilder.withTypeOfThis(typeOfThis);
    assertNull(functionBuilder.getTypeOfThis());
  }

  @Test
  public void testFunctionBuilderWithEmptyTemplateTypeName() {
    FunctionBuilder functionBuilder = functionBuilder.withTemplateName("");
    assertNull(functionBuilder.getTemplateTypeName());
  }

  @Test
  public void testFunctionBuilderWithEmptyTemplateTypeNameAndIsConstructorFalse() {
    FunctionBuilder functionBuilder = functionBuilder.withTemplateName("").forConstructor();
    assertNull(functionBuilder.getTemplateTypeName());
    assertFalse(functionBuilder.isConstructor());
  }

  @Test
  public void testFunctionBuilderWithEmptyTemplateTypeNameAndIsConstructorTrue() {
    FunctionBuilder functionBuilder = functionBuilder.withTemplateName("").forConstructor();
    assertNull(functionBuilder.getTemplateTypeName());
    assertTrue(functionBuilder.isConstructor());
  }
}