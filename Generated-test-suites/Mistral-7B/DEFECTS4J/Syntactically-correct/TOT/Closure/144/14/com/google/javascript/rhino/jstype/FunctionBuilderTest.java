package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;

public class FunctionBuilderTest {

  @org.junit.Test
  public void testFunctionBuilderWithSimpleParametersAndReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    // Set the name of the function type.
    String functionName = "add";
    functionBuilder.withName(functionName);

    // Set the source node of the function type.
    Node functionSourceNode = Node.newJsonObject();

    // Set the parameters of the function type from a FunctionParamBuilder.
    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder(registry);
    functionParamBuilder.withName("a").withType(IntType.INSTANCE);
    functionParamBuilder.withName("b").withType(IntType.INSTANCE);
    Node parametersNode = functionParamBuilder.build();
    functionBuilder.withParams(functionParamBuilder);

    // Set the return type.
    JSType returnType = IntType.INSTANCE;
    functionBuilder.withReturnType(returnType);

    // Build the function type.
    FunctionType functionType = functionBuilder.build();

    // Assert the function type is built correctly.
    assertEquals(functionName, functionType.getReferenceName());
    assertEquals(functionSourceNode, functionType.getSource());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(returnType, functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @org.junit.Test
  public void testFunctionBuilderWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    // Set the name of the function type.
    String functionName = "add";
    functionBuilder.withName(functionName);

    // Set the source node of the function type.
    Node functionSourceNode = Node.newJsonObject();

    // Set the parameters of the function type from a FunctionParamBuilder.
    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder(registry);
    functionParamBuilder.withName("a").withType(IntType.INSTANCE);
    functionParamBuilder.withName("b").withType(IntType.INSTANCE);
    Node parametersNode = functionParamBuilder.build();
    functionBuilder.withParams(functionParamBuilder);

    // Set the return type and mark it as inferred.
    JSType returnType = VoidType.INSTANCE;
    functionBuilder.withInferredReturnType(returnType);

    // Build the function type.
    FunctionType functionType = functionBuilder.build();

    // Assert the function type is built correctly.
    assertEquals(functionName, functionType.getReferenceName());
    assertEquals(functionSourceNode, functionType.getSource());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(returnType, functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
    assertTrue(functionType.isInferredReturnType());
  }

  @org.junit.Test
  public void testFunctionBuilderWithTemplateTypeName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    // Set the name of the function type.
    String functionName = "add";
    functionBuilder.withName(functionName);

    // Set the source node of the function type.
    Node functionSourceNode = Node.newJsonObject();

    // Set the parameters of the function type from a FunctionParamBuilder.
    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder(registry);
    functionParamBuilder.withName("a").withType(IntType.INSTANCE);
    functionParamBuilder.withName("b").withType(IntType.INSTANCE);
    Node parametersNode = functionParamBuilder.build();
    functionBuilder.withParams(functionParamBuilder);

    // Set the return type.
    JSType returnType = StringType.INSTANCE;
    functionBuilder.withReturnType(returnType);

    // Set the template type name.
    String templateTypeName = "MyTemplate";
    functionBuilder.withTemplateName(templateTypeName);

    // Build the function type.
    FunctionType functionType = functionBuilder.build();

    // Assert the function type is built correctly.
    assertEquals(functionName, functionType.getReferenceName());
    assertEquals(functionSourceNode, functionType.getSource());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(returnType, functionType.getReturnType());
    assertEquals(templateTypeName, functionType.getTemplateTypeName());
    assertNull(functionType.getTypeOfThis());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @org.junit.Test
  public void testFunctionBuilderWithTypeOfThis() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    // Set the name of the function type.
    String functionName = "add";
    functionBuilder.withName(functionName);

    // Set the source node of the function type.
    Node functionSourceNode = Node.newJsonObject();

    // Set the parameters of the function type from a FunctionParamBuilder.
    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder(registry);
    functionParamBuilder.withName("a").withType(IntType.INSTANCE);
    functionParamBuilder.withName("b").withType(IntType.INSTANCE);
    Node parametersNode = functionParamBuilder.build();
    functionBuilder.withParams(functionParamBuilder);

    // Set the return type.
    JSType returnType = IntType.INSTANCE;
    functionBuilder.withReturnType(returnType);

    // Set the "this" type.
    ObjectType typeOfThis = ObjectType.INSTANCE;
    functionBuilder.withTypeOfThis(typeOfThis);

    // Build the function type.
    FunctionType functionType = functionBuilder.build();

    // Assert the function type is built correctly.
    assertEquals(functionName, functionType.getReferenceName());
    assertEquals(functionSourceNode, functionType.getSource());
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(returnType, functionType.getReturnType());
    assertEquals(typeOfThis, functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
    assertFalse(functionType.isConstructor());
    assertFalse(functionType.isNativeObjectType());
  }

  @org.junit.Test
  public void testFunctionBuilderWithConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    // Set the name of the constructor type.
    String constructorName = "Person";
    functionBuilder.withName(constructorName);

    // Set the source node of the constructor type.
    Node constructorSourceNode = Node.newJsonObject();

    // Set the parameters of the constructor type from a FunctionParamBuilder.
    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder(registry);
    functionParamBuilder.withName("name").withType(StringType.INSTANCE);
    functionParamBuilder.withName("age").withType(IntType.INSTANCE);
    Node constructorParametersNode = functionParamBuilder.build();
    functionBuilder.withParams(functionParamBuilder);

    // Set the return type to be the constructor's object type.
    functionBuilder.forConstructor();

    // Build the constructor type.
    FunctionType constructorType = functionBuilder.build();

    // Assert the constructor type is built correctly.
    assertEquals(constructorName, constructorType.getReferenceName());
    assertEquals(constructorSourceNode, constructorType.getSource());
    assertEquals(constructorParametersNode, constructorType.getParametersNode());
    assertNull(constructorType.getReturnType());
    assertNull(constructorType.getTypeOfThis());
    assertNull(constructorType.getTemplateTypeName());
    assertTrue(constructorType.isConstructor());
    assertFalse(constructorType.isNativeObjectType());
  }

  @org.junit.Test
  public void testFunctionBuilderWithNativeType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    // Set the name of the native type.
    String nativeFunctionName = "Math.sqrt";
    functionBuilder.withName(nativeFunctionName);

    // Set the source node of the native type.
    Node nativeFunctionSourceNode = Node.newJsonObject();

    // Set the parameters of the native type from a FunctionParamBuilder.
    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder(registry);
    functionParamBuilder.withName("number").withType(NumberType.INSTANCE);
    Node nativeFunctionParametersNode = functionParamBuilder.build();
    functionBuilder.withParamsNode(nativeFunctionParametersNode);

    // Make the function type a native type.
    functionBuilder.forNativeType();

    // Build the native type.
    FunctionType nativeFunctionType = functionBuilder.build();

    // Assert the native type is built correctly.
    assertEquals(nativeFunctionName, nativeFunctionType.getReferenceName());
    assertEquals(nativeFunctionSourceNode, nativeFunctionType.getSource());
    assertEquals(nativeFunctionParametersNode, nativeFunctionType.getParametersNode());
    assertNull(nativeFunctionType.getReturnType());
    assertNull(nativeFunctionType.getTypeOfThis());
    assertNull(nativeFunctionType.getTemplateTypeName());
    assertFalse(nativeFunctionType.isConstructor());
    assertTrue(nativeFunctionType.isNativeObjectType());
  }

  @org.junit.Test
  public void testFunctionBuilderWithCopyFromOtherFunction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionType otherFunctionType = new FunctionType(registry, "otherFunction", Node.newJsonObject(),
        new ArrowType(registry, Node.newJsonArray(), IntType.INSTANCE, false),
        ObjectType.INSTANCE, null, false, false);

    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    // Copy all the information from another function type.
    functionBuilder.copyFromOtherFunction(otherFunctionType);

    // Build the copied function type.
    FunctionType copiedFunctionType = functionBuilder.build();

    // Assert the copied function type is built correctly.
    assertEquals(otherFunctionType.getReferenceName(), copiedFunctionType.getReferenceName());
    assertEquals(otherFunctionType.getSource(), copiedFunctionType.getSource());
    assertEquals(otherFunctionType.getParametersNode(), copiedFunctionType.getParametersNode());
    assertEquals(otherFunctionType.getReturnType(), copiedFunctionType.getReturnType());
    assertEquals(otherFunctionType.getTypeOfThis(), copiedFunctionType.getTypeOfThis());
    assertEquals(otherFunctionType.getTemplateTypeName(), copiedFunctionType.getTemplateTypeName());
    assertEquals(otherFunctionType.isConstructor(), copiedFunctionType.isConstructor());
    assertEquals(otherFunctionType.isNativeObjectType(), copiedFunctionType.isNativeObjectType());
  }
}