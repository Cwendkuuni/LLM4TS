import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createList;
import static com.google.javascript.rhino.NodeFactory.createObject;
import static com.google.javascript.rhino.NodeFactory.createString;
import static com.google.javascript.rhino.NodeFactory.createThis;
import static com.google.javascript.rhino.NodeFactory.createTypeCall;
import static com.google.javascript.rhino.JSTypeFactory.constructArrayType;
import static com.google.javascript.rhino.JSTypeFactory.constructFunctionType;
import static com.google.javascript.rhino.JSTypeFactory.constructObjectType;
import static com.google.javascript.rhino.JSTypeFactory.constructStringType;
import static com.google.javascript.rhino.JSTypeFactory.constructVoidType;
import static com.google.javascript.rhino.JSTypeFactory.getFunctionType;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

public class FunctionBuilderTest {

  private JSTypeFactory typeFactory;
  private FunctionBuilder functionBuilder;

  @Before
  public void setUp() {
    typeFactory = JSTypeFactory.getGlobalScope();
    functionBuilder = new FunctionBuilder(typeFactory);
  }

  @Test
  public void testFunctionBuilderWithSimpleFunction() {
    // Given: a simple function with one parameter and return void

    Node identifier1 = createIdentifier("p1");
    Node parametersNode = createList(identifier1);

    FunctionBuilder functionBuilder = new FunctionBuilder(typeFactory);
    functionBuilder.withName("simpleFunction");
    functionBuilder.withParams(new FunctionParamBuilder().withName("p1").build());
    functionBuilder.withReturnType(constructVoidType());

    FunctionType functionType = functionBuilder.build();

    // When: create a function type using the builder

    FunctionType expectedFunctionType = getFunctionType("simpleFunction",
        createList(constructObjectType("p1", constructStringType("p1"))),
        constructVoidType(), createObject());

    // Then: the function type should match the expected type

    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testFunctionBuilderWithFunctionReturningString() {
    // Given: a function with one parameter and return string

    Node identifier1 = createIdentifier("p1");

    FunctionBuilder functionBuilder = new FunctionBuilder(typeFactory);
    functionBuilder.withName("functionReturnString");
    functionBuilder.withParams(new FunctionParamBuilder().withName("p1").build());
    functionBuilder.withReturnType(constructStringType(""));

    FunctionType functionType = functionBuilder.build();

    // When: create a function type using the builder

    FunctionType expectedFunctionType = getFunctionType("functionReturnString",
        createList(constructObjectType("p1", constructStringType("p1"))),
        constructStringType(""), createObject());

    // Then: the function type should match the expected type

    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testFunctionBuilderWithArrayReturningFunction() {
    // Given: a function with one parameter and return an array of functions

    Node identifier1 = createIdentifier("p1");

    FunctionBuilder arrayFunctionBuilder = new FunctionBuilder(typeFactory);
    FunctionBuilder functionBuilder = new FunctionBuilder(typeFactory);

    functionBuilder.withName("functionReturnFunction");
    functionBuilder.withParams(new FunctionParamBuilder().withName("p1").build());
    functionBuilder.withReturnType(
        constructFunctionType("innerFunction",
            createList(constructObjectType("p1", constructStringType("p1"))),
            constructVoidType(), createObject()));

    arrayFunctionBuilder.withName("arrayFunction");
    arrayFunctionBuilder.withParamsNode(createList(functionBuilder.build()));
    arrayFunctionBuilder.withReturnType(constructArrayType(constructFunctionType("innerFunction",
        createList(constructObjectType("p1", constructStringType("p1"))),
        constructVoidType(), createObject())));

    FunctionType arrayFunctionType = arrayFunctionBuilder.build();

    // When: create a function type using the builder

    FunctionType expectedFunctionType = getFunctionType("arrayFunction",
        createList(),
        constructFunctionType("innerFunction",
            createList(constructObjectType("p1", constructStringType("p1"))),
            constructVoidType(), createObject()),
        createObject());

    // Then: the function type should match the expected type

    assertEquals(expectedFunctionType, arrayFunctionType);
  }

  @Test
  public void testFunctionBuilderWithConstructor() {
    // Given: a constructor with no parameters and return Object

    FunctionBuilder constructorBuilder = new FunctionBuilder(typeFactory);
    constructorBuilder.forConstructor();

    FunctionType constructorType = constructorBuilder.build();

    // When: create a constructor type using the builder

    FunctionType expectedConstructorType = getFunctionType("constructor",
        createList(),
        constructObjectType("", createObject()), createObject());

    // Then: the constructor type should match the expected type

    assertEquals(expectedConstructorType, constructorType);
  }

  @Test
  public void testFunctionBuilderWithNativeFunction() {
    // Given: a native function with no parameters and return void

    FunctionBuilder nativeFunctionBuilder = new FunctionBuilder(typeFactory);
    nativeFunctionBuilder.forNativeType();

    FunctionType nativeFunctionType = nativeFunctionBuilder.build();

    // When: create a native function type using the builder

    FunctionType expectedNativeFunctionType = getFunctionType("nativeFunction",
        createList(),
        constructVoidType(), createObject());

    // Then: the native function type should match the expected type

    assertEquals(expectedNativeFunctionType, nativeFunctionType);
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    // Given: a function with one parameter and inferred return type

    Node identifier1 = createIdentifier("p1");

    FunctionBuilder functionBuilder = new FunctionBuilder(typeFactory);
    functionBuilder.withName("functionWithInferredReturnType");
    functionBuilder.withParams(new FunctionParamBuilder().withName("p1").build());
    functionBuilder.withInferredReturnType(constructStringType(""));

    FunctionType functionType = functionBuilder.build();

    // When: create a function type using the builder

    FunctionType expectedFunctionType = getFunctionType("functionWithInferredReturnType",
        createList(constructObjectType("p1", constructStringType("p1"))),
        constructStringType(""), createObject());

    // Then: the function type should match the expected type

    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testFunctionBuilderWithTemplateTypeName() {
    // Given: a function with a template type name

    FunctionBuilder functionBuilder = new FunctionBuilder(typeFactory);
    functionBuilder.withName("functionWithTemplateTypeName");
    functionBuilder.withTemplateName("MyTemplateFunction");

    FunctionType functionType = functionBuilder.build();

    // When: create a function type using the builder

    FunctionType expectedFunctionType = getFunctionType("functionWithTemplateTypeName",
        createList(),
        constructVoidType(),
        createString("MyTemplateFunction"));

    // Then: the function type should match the expected type

    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testFunctionBuilderWithTypeOfThis() {
    // Given: a function with a "this" type

    ObjectType objectType = constructObjectType("MyObject", createList());

    FunctionBuilder functionBuilder = new FunctionBuilder(typeFactory);
    functionBuilder.withName("functionWithTypeOfThis");
    functionBuilder.withTypeOfThis(objectType);

    FunctionType functionType = functionBuilder.build();

    // When: create a function type using the builder

    FunctionType expectedFunctionType = getFunctionType("functionWithTypeOfThis",
        createList(),
        constructVoidType(),
        objectType);

    // Then: the function type should match the expected type

    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testFunctionBuilderWithSourceNode() {
    // Given: a function with a source node

    Node sourceNode = createObject();

    FunctionBuilder functionBuilder = new FunctionBuilder(typeFactory);
    functionBuilder.withName("functionWithSourceNode");
    functionBuilder.withSourceNode(sourceNode);

    FunctionType functionType = functionBuilder.build();

    // When: create a function type using the builder

    FunctionType expectedFunctionType = getFunctionType("functionWithSourceNode",
        createList(),
        constructVoidType(),
        createObject());

    // Then: the function type should match the expected type

    assertEquals(expectedFunctionType, functionType);
  }
}