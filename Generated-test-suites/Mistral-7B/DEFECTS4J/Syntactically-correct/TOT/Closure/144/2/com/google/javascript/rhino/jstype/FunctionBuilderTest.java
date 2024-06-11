package com.google.javascript.rhino.jstype.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

public class FunctionBuilderTest {
  private JSTypeRegistry registry;
  private FunctionBuilder builder;

  @Before
  public void setup() {
    registry = mock(JSTypeRegistry.class);
    builder = new FunctionBuilder(registry);
  }

  @Test
  public void testFunctionBuilderWithNameAndReturnType() {
    String name = "add";
    JSType returnType = registry.createNumberType();

    FunctionType functionType = builder
        .withName(name)
        .withReturnType(returnType)
        .build();

    assertNotNull(functionType);
    assertNotNull(functionType.getReferenceName());
    assertNotNull(functionType.getReturnType());
  }

  @Test
  public void testFunctionBuilderWithNameAndParameters() {
    String name = "multiply";
    FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder(registry);
    Node paramNode1 = paramBuilder1.withName("a").withType(registry.createNumberType()).build();

    FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder(registry);
    Node paramNode2 = paramBuilder2.withName("b").withType(registry.createNumberType()).build();

    Node parametersNode = new Node.ListNode(new Node[] { paramNode1, paramNode2 });

    FunctionType functionType = builder
        .withName(name)
        .withParamsNode(parametersNode)
        .build();

    assertNotNull(functionType);
    assertNotNull(functionType.getReferenceName());
    assertNotNull(functionType.getParametersNode());
  }

  @Test
  public void testFunctionBuilderWithTemplateTypeName() {
    String name = "promise";
    String templateTypeName = "Promise";

    FunctionType functionType = builder
        .withName(name)
        .withTemplateName(templateTypeName)
        .build();

    assertNotNull(functionType);
    assertNotNull(functionType.getTemplateTypeName());
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    String name = "square";
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
    Node paramNode = paramBuilder.withName("x").withType(registry.createNumberType()).build();

    Node parametersNode = new Node.ListNode(new Node[] { paramNode });
    Node sourceNode = new Node.CallNode(null, registry.createFunctionType("Math", null, null, null).getReferenceName(), new Node[] { new Node.IntegerLiteralNode(2) });

    FunctionType functionType = builder
        .withName(name)
        .withParamsNode(parametersNode)
        .withSourceNode(sourceNode)
        .withInferredReturnType(registry.createNumberType())
        .build();

    assertNotNull(functionType);
    assertNotNull(functionType.getReferenceName());
    assertNotNull(functionType.getReturnType());
    assertNotNull(functionType.getInferredReturnType());
  }

  @Test
  public void testFunctionBuilderForConstructor() {
    String name = "MyClass";

    FunctionType constructorType = builder
        .withName(name)
        .forConstructor()
        .build();

    assertNotNull(constructorType);
    assertTrue(constructorType.isConstructor());
  }
}