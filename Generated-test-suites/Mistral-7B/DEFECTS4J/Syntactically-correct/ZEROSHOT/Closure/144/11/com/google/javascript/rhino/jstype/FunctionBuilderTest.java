import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createStringLiteral;
import static com.google.javascript.rhino.jstype.JSTypeFactory.createAnonymousObjectType;
import static com.google.javascript.rhino.jstype.JSTypeFactory.createFunctionType;
import static com.google.javascript.rhino.jstype.JSTypeFactory.createObjectType;
import static com.google.javascript.rhino.jstype.JSTypeFactory.createStringType;
import static com.google.javascript.rhino.jstype.JSTypeFactory.createVoidType;

import org.junit.Test;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.TypeBuilder;
import com.google.javascript.rhino.jstype.TypeOfThis;

public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilder() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    // Test withName method
    FunctionBuilder functionBuilder1 = functionBuilder.withName("myFunction");

    // Test withSourceNode method
    Node sourceNode = createNode(createStringLiteral("function myFunction() {}"));
    FunctionBuilder functionBuilder2 = functionBuilder1.withSourceNode(sourceNode);

    // Test withParams method
    FunctionParamBuilder paramsBuilder = new FunctionParamBuilder();
    FunctionParamBuilder paramBuilder1 = paramsBuilder.withName("param1").withType(createStringType());
    FunctionParamBuilder paramBuilder2 = paramsBuilder.withName("param2").withType(createStringType());
    FunctionBuilder functionBuilder3 = functionBuilder2.withParams(paramsBuilder.build());

    // Test withParamsNode method
    Node parametersNode = createNode(createStringLiteral("function(param1: string, param2: string) {}"));
    FunctionBuilder functionBuilder4 = functionBuilder3.withParamsNode(parametersNode);

    // Test withReturnType method
    FunctionBuilder functionBuilder5 = functionBuilder4.withReturnType(createStringType());

    // Test withInferredReturnType method
    FunctionBuilder functionBuilder6 = functionBuilder5.withInferredReturnType(createStringType());

    // Test withTypeOfThis method
    ObjectType typeOfThis = createAnonymousObjectType();
    FunctionBuilder functionBuilder7 = functionBuilder6.withTypeOfThis(typeOfThis);

    // Test withTemplateName method
    FunctionBuilder functionBuilder8 = functionBuilder7.withTemplateName("MyTemplate");

    // Test forConstructor method
    FunctionBuilder functionBuilder9 = functionBuilder8.forConstructor();

    // Test forNativeType method
    FunctionBuilder functionBuilder10 = functionBuilder9.forNativeType();

    // Test build method
    FunctionType functionType = functionBuilder10.build();
    TypeBuilder typeBuilder = new TypeBuilder(registry);
    TypeOfThis typeOfThisNew = typeBuilder.withTypeAnnotation("this", typeOfThis).build();
    FunctionType expectedFunctionType = createFunctionType(registry, "myFunction", sourceNode, new ArrowType(registry, parametersNode, createStringType(), true), typeOfThisNew, "MyTemplate", true, true);
    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testFunctionBuilderCopyFromOtherFunction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    // Create source function type
    Node sourceNode = createNode(createStringLiteral("function myFunction(param1: string, param2: number): string {}"));
    FunctionType sourceFunctionType = createFunctionType(registry, "myFunction", sourceNode,
        new ArrowType(registry, createNode(createStringLiteral("param1: string, param2: number")), createStringType(), false),
        createAnonymousObjectType(), null, true, false);

    // Test copyFromOtherFunction method
    FunctionBuilder functionBuilder1 = new FunctionBuilder(registry);
    functionBuilder1.copyFromOtherFunction(sourceFunctionType);
    FunctionType functionType = functionBuilder1.build();

    TypeBuilder typeBuilder = new TypeBuilder(registry);
    TypeOfThis typeOfThis = typeBuilder.withTypeAnnotation("this", createAnonymousObjectType()).build();
    FunctionType expectedFunctionType = createFunctionType(registry, "myFunction", sourceNode,
        new ArrowType(registry, createNode(createStringLiteral("param1: string, param2: number")), createStringType(), false),
        typeOfThis, null, true, false);
    assertEquals(expectedFunctionType, functionType);
  }

  @Test
  public void testFunctionBuilderWithVoidReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    // Test withReturnType method with void type
    FunctionBuilder functionBuilder1 = functionBuilder.withReturnType(createVoidType());

    FunctionType functionType = functionBuilder1.build();
    TypeBuilder typeBuilder = new TypeBuilder(registry);
    TypeOfThis typeOfThis = typeBuilder.withTypeAnnotation("this", createAnonymousObjectType()).build();
    FunctionType expectedFunctionType = createFunctionType(registry, null, null, new ArrowType(registry, null, createVoidType(), false), typeOfThis, null, false, false);
    assertEquals(expectedFunctionType, functionType);
  }
}