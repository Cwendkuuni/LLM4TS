import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import static com.google.javascript.rhino.NodeFactory.*;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.JSType.FunctionType;
import com.google.javascript.rhino.JSType.FunctionParamBuilder;
import com.google.javascript.rhino.JSType.ArrowType;

import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilderWithSimpleFunction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String functionName = "add";
    Node functionSource = stringLiteral("function add(x, y) { return x + y; }");

    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.withName("x").withType(INT);
    paramBuilder.withName("y").withType(INT);

    FunctionType expectedFunctionType = new FunctionType(registry, functionName, functionSource,
        new ArrowType(registry, paramBuilder.build(), INT, false), VOID, null, false, false);

    functionBuilder
        .withName(functionName)
        .withSourceNode(functionSource)
        .withParams(paramBuilder)
        .withReturnType(INT)
        .build()
        .equals(expectedFunctionType);
  }

  @Test
  public void testFunctionBuilderWithFunctionWithVoidReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String functionName = "printMessage";
    Node functionSource = stringLiteral("function printMessage(message) { console.log(message); }");

    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.withName("message").withType(STRING);

    functionBuilder
        .withName(functionName)
        .withSourceNode(functionSource)
        .withParams(paramBuilder)
        .withReturnType(VOID)
        .build();
  }

  @Test
  public void testFunctionBuilderWithFunctionWithThisType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String functionName = "MyClass";
    ObjectType thisType = new ObjectType(registry, "MyClass", null);

    String functionSource = stringLiteral(
        "function " + functionName + "(param) { this.property = param; }");

    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.withName("param").withType(INT);

    functionBuilder
        .withName(functionName)
        .withSourceNode(functionSource)
        .withParams(paramBuilder)
        .withReturnType(VOID)
        .withTypeOfThis(thisType)
        .build();
  }

  @Test
  public void testFunctionBuilderWithFunctionWithTemplateType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String functionName = "map";
    String templateTypeName = "Array<number>";

    Node functionSource = stringLiteral(
        "function map(array, mapperFunction) { " +
        "  var result = []; " +
        "  for (var i = 0; i < array.length; i++) { " +
        "    result.push(mapperFunction(array[i])); " +
        "  } " +
        "  return result; " +
        "}");

    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.withName("array").withType(new ArrayType(registry, INT));
    paramBuilder.withName("mapperFunction").withType(new FunctionType(
        registry, "mapperFunction", null, new ArrowType(
            registry, new Array<FunctionParamBuilder>().add(
                new FunctionParamBuilder().withName("item").withType(INT)
            ).build(), INT), VOID, null, false, false));

    functionBuilder
        .withName(functionName)
        .withSourceNode(functionSource)
        .withParams(paramBuilder)
        .withReturnType(new ArrayType(registry, INT))
        .withTemplateName(templateTypeName)
        .build();
  }

  @Test
  public void testFunctionBuilderWithConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String constructorName = "MyClass";
    Node constructorSource = stringLiteral(
        "function " + constructorName + "(param1, param2) { " +
        "  this.property1 = param1; " +
        "  this.property2 = param2; " +
        "}");

    FunctionParamBuilder paramBuilder = new FunctionParamBuilder();
    paramBuilder.withName("param1").withType(STRING);
    paramBuilder.withName("param2").withType(NUMBER);

    functionBuilder
        .withName(constructorName)
        .withSourceNode(constructorSource)
        .withParams(paramBuilder)
        .forConstructor()
        .build();
  }

  @Test
  public void testFunctionBuilderWithNativeType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String functionName = "parseInt";
    functionBuilder.forNativeType();

    FunctionType expectedFunctionType = new FunctionType(registry, functionName, null,
        new ArrowType(registry, new Array<FunctionParamBuilder>().add(
            new FunctionParamBuilder().withName("str").withType(STRING)
        ).build(), INT), VOID, null, false, true);

    functionBuilder.build().equals(expectedFunctionType);
  }
}