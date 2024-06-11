package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {
  private FunctionBuilder functionBuilder;
  private NodeOperations nodeOperations;
  private FunctionParamBuilder functionParamBuilder;

  @Before
  public void setup() {
    nodeOperations = mock(NodeOperations.class);
    functionParamBuilder = mock(FunctionParamBuilder.class);
    functionBuilder = new FunctionBuilder(mock(JSTypeRegistry.class))
        .withName("testFunction")
        .withSourceNode(createNode("function testFunction(a) {}", nodeOperations))
        .withParams(functionParamBuilder)
        .withParamsNode(createNode("(a)", nodeOperations))
        .withReturnType(createStringType("String"))
        .withTypeOfThis(createObjectType("Object"));
  }

  @Test
  public void testFunctionBuilderWithName() {
    FunctionType functionType = functionBuilder.build();
    assertEquals("testFunction", functionType.getReferenceName());
  }

  @Test
  public void testFunctionBuilderWithSourceNode() {
    FunctionType functionType = functionBuilder.build();
    assertEquals(nodeOperations.toString(functionType.getSource()),
        "function testFunction(a) {}", nodeOperations);
  }

  @Test
  public void testFunctionBuilderWithParams() {
    FunctionType functionType = functionBuilder.build();
    FunctionParamBuilder params = mock(FunctionParamBuilder.class);
    when(params.build()).thenReturn(createObjectType("FunctionParamType"));

    Node parametersNode = nodeOperations.newList(new Node[] {
        createIdent("a"), nodeOperations.newText(":")
    });

    functionBuilder = new FunctionBuilder(mock(JSTypeRegistry.class))
        .withName("testFunction")
        .withSourceNode(createNode("function testFunction(a)", nodeOperations))
        .withParams(params)
        .withParamsNode(parametersNode);

    functionType = functionBuilder.build();
    assertEquals(parametersNode, functionType.getParametersNode());
    assertEquals(createObjectType("FunctionParamType"), functionType.getParametersType());
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    FunctionType functionType = functionBuilder
        .withReturnType(null)
        .withInferredReturnType(createObjectType("Object"))
        .build();

    assertEquals(createObjectType("Object"), functionType.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithTemplateTypeName() {
    FunctionType functionType = functionBuilder
        .withTemplateName("FunctionTemplate")
        .build();

    assertEquals("FunctionTemplate", functionType.getTemplateTypeName());
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    FunctionType functionType = functionBuilder.forConstructor().build();
    assertEquals(true, functionType.isConstructor());
  }

  @Test
  public void testFunctionBuilderForNativeType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(mock(JSTypeRegistry.class))
        .forNativeType();

    assertNull(functionBuilder.getName());
    assertNull(functionBuilder.getSourceNode());
    assertNull(functionBuilder.getParametersNode());
    assertNull(functionBuilder.getReturnType());
    assertNull(functionBuilder.getTypeOfThis());
    assertNull(functionBuilder.getTemplateTypeName());
    assertTrue(functionBuilder.isNativeType());
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    FunctionType sourceFunctionType = createFunctionType();
    functionBuilder.copyFromOtherFunction(sourceFunctionType);

    FunctionType functionType = functionBuilder.build();
    assertEquals(sourceFunctionType.getReferenceName(), functionType.getReferenceName());
    assertEquals(sourceFunctionType.getSource(), functionType.getSource());
    assertEquals(sourceFunctionType.getParametersNode(), functionType.getParametersNode());
    assertEquals(sourceFunctionType.getParametersType(), functionType.getParametersType());
    assertEquals(sourceFunctionType.getReturnType(), functionType.getReturnType());
    assertEquals(sourceFunctionType.getTypeOfThis(), functionType.getTypeOfThis());
    assertEquals(sourceFunctionType.getTemplateTypeName(), functionType.getTemplateTypeName());
    assertEquals(sourceFunctionType.isConstructor(), functionType.isConstructor());
    assertEquals(sourceFunctionType.isNativeObjectType(), functionType.isNativeObjectType());
  }

  private Node createIdent(String name) {
    return nodeOperations.newIdent(name);
  }

  private Node createNode(String text, NodeOperations nodeOperations) {
    return nodeOperations.newText(text);
  }

  private Node createNode(String text) {
    return createNode(text, mock(NodeOperations.class));
  }

  private Node createObjectType(String name) {
    ObjectType objectType = new ObjectType(mock(JSTypeRegistry.class), name);
    return objectType.build();
  }

  private FunctionType createFunctionType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(mock(JSTypeRegistry.class))
        .withName("testFunction")
        .withSourceNode(createNode("function testFunction(a)", nodeOperations))
        .withParams(functionParamBuilder)
        .withParamsNode(createNode("(a)", nodeOperations))
        .withReturnType(createStringType("String"))
        .withTypeOfThis(createObjectType("Object"))
        .withTemplateName("FunctionTemplate")
        .build();

    return functionBuilder.build();
  }

  private JSType createStringType(String name) {
    return new StringType(mock(JSTypeRegistry.class), name);
  }
}