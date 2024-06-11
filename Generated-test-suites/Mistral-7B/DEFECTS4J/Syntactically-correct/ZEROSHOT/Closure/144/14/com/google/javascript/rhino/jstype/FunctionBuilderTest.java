import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilder() {
    JSTypeRegistry registry = new JSTypeRegistry();

    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    // Test withName method
    String name = "myFunction";
    FunctionBuilder nameFunctionBuilder = functionBuilder.withName(name);
    assertNotNull(nameFunctionBuilder.getName());
    assertEquals(name, nameFunctionBuilder.getName());

    // Test withSourceNode method
    Node sourceNode = new Node();
    FunctionBuilder sourceNodeFunctionBuilder = nameFunctionBuilder.withSourceNode(sourceNode);
    assertNotNull(sourceNodeFunctionBuilder.getSourceNode());
    assertEquals(sourceNode, sourceNodeFunctionBuilder.getSourceNode());

    // Test withParams method
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    FunctionParamBuilder params = paramBuilder.withName("param1").withType(new IntType(registry));
    FunctionBuilder paramsFunctionBuilder = sourceNodeFunctionBuilder.withParams(params);
    assertNotNull(paramsFunctionBuilder.getParametersNode());

    // Test withParamsNode method
    Node paramsNode = new Node();
    FunctionBuilder paramsNodeFunctionBuilder = sourceNodeFunctionBuilder.withParamsNode(paramsNode);
    assertNotNull(paramsNodeFunctionBuilder.getParametersNode());
    assertEquals(paramsNode, paramsNodeFunctionBuilder.getParametersNode());

    // Test withReturnType method
    JSType returnType = new IntType(registry);
    FunctionBuilder returnTypeFunctionBuilder = paramsNodeFunctionBuilder.withReturnType(returnType);
    assertNotNull(returnTypeFunctionBuilder.getReturnType());
    assertEquals(returnType, returnTypeFunctionBuilder.getReturnType());

    // Test withInferredReturnType method
    FunctionBuilder inferredReturnTypeFunctionBuilder = returnTypeFunctionBuilder.withInferredReturnType(returnType);
    assertNotNull(inferredReturnTypeFunctionBuilder.isInferredReturnType());
    assertEquals(true, inferredReturnTypeFunctionBuilder.isInferredReturnType());

    // Test withTypeOfThis method
    ObjectType typeOfThis = new ObjectType(registry);
    FunctionBuilder typeOfThisFunctionBuilder = inferredReturnTypeFunctionBuilder.withTypeOfThis(typeOfThis);
    assertNotNull(typeOfThisFunctionBuilder.getTypeOfThis());
    assertEquals(typeOfThis, typeOfThisFunctionBuilder.getTypeOfThis());

    // Test withTemplateName method
    String templateTypeName = "TemplateFunction";
    FunctionBuilder templateTypeNameFunctionBuilder = typeOfThisFunctionBuilder.withTemplateName(templateTypeName);
    assertNotNull(templateTypeNameFunctionBuilder.getTemplateTypeName());
    assertEquals(templateTypeName, templateTypeNameFunctionBuilder.getTemplateTypeName());

    // Test forConstructor method
    FunctionBuilder constructorFunctionBuilder = templateTypeNameFunctionBuilder.forConstructor();
    assertNotNull(constructorFunctionBuilder.isConstructor());
    assertEquals(true, constructorFunctionBuilder.isConstructor());

    // Test forNativeType method
    FunctionBuilder nativeTypeFunctionBuilder = constructorFunctionBuilder.forNativeType();
    assertNotNull(nativeTypeFunctionBuilder.isNativeType());
    assertEquals(true, nativeTypeFunctionBuilder.isNativeType());

    // Test build method
    FunctionType functionType = nativeTypeFunctionBuilder.build();
    assertNotNull(functionType);
  }
}