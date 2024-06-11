package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilderWithNameAndReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    String functionName = "add";
    JSType returnType = new NumberType(3);
    functionBuilder.withName(functionName).withReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals(functionName, functionType.getReferenceName());
    assertEquals(returnType, functionType.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithParamsAndReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    String functionName = "add";
    FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder(new JSTypeRegistry());
    paramBuilder1.withName("num1").withType(new NumberType(3));
    FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder(new JSTypeRegistry());
    paramBuilder2.withName("num2").withType(new NumberType(3));
    Node parametersNode = paramBuilder1.build().concat(paramBuilder2.build());
    JSType returnType = new NumberType(3);
    functionBuilder.withName(functionName).withParams(paramBuilder1).withParamsNode(parametersNode)
        .withReturnType(returnType);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals(functionName, functionType.getReferenceName());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(returnType, functionType.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    String functionName = "add";
    FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder(new JSTypeRegistry());
    paramBuilder1.withName("num1").withType(new NumberType(3));
    FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder(new JSTypeRegistry());
    paramBuilder2.withName("num2").withType(new NumberType(3));
    Node parametersNode = paramBuilder1.build().concat(paramBuilder2.build());
    JSType returnType = new VoidType();
    functionBuilder.withName(functionName).withParams(paramBuilder1).withParamsNode(parametersNode)
        .withInferredReturnType(new NumberType(3));
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals(functionName, functionType.getReferenceName());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertNotNull(functionType.getReturnType());
    assertEquals(new NumberType(3), functionType.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithTemplateTypeName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    String functionName = "add";
    FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder(new JSTypeRegistry());
    paramBuilder1.withName("num1").withType(new NumberType(3));
    FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder(new JSTypeRegistry());
    paramBuilder2.withName("num2").withType(new NumberType(3));
    Node parametersNode = paramBuilder1.build().concat(paramBuilder2.build());
    JSType returnType = new NumberType(3);
    functionBuilder.withName(functionName).withParams(paramBuilder1).withParamsNode(parametersNode)
        .withReturnType(returnType).withTemplateName("MyAddTemplate");
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals(functionName, functionType.getReferenceName());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(returnType, functionType.getReturnType());
    assertEquals("MyAddTemplate", functionType.getTemplateTypeName());
  }

  @Test
  public void testFunctionBuilderWithConstructor() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    String functionName = "MyConstructor";
    FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder(new JSTypeRegistry());
    paramBuilder1.withName("num1").withType(new NumberType(3));
    Node parametersNode = paramBuilder1.build();
    ObjectType thisType = new ObjectType("MyObject");
    functionBuilder.withName(functionName).withParams(paramBuilder1).withParamsNode(parametersNode)
        .forConstructor().withTypeOfThis(thisType);
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals(functionName, functionType.getReferenceName());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(thisType, functionType.getTypeOfThis());
    assertEquals(true, functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderWithNativeType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());
    String functionName = "document.write";
    FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder(new JSTypeRegistry());
    paramBuilder1.withName("text").withType(new StringType());
    Node parametersNode = paramBuilder1.build();
    functionBuilder.withName(functionName).withParams(paramBuilder1).withParamsNode(parametersNode)
        .forNativeType();
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
    assertEquals(functionName, functionType.getReferenceName());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(true, functionType.isNativeType());
  }
}