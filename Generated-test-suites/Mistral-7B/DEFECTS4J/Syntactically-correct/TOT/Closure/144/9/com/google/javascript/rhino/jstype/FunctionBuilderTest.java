import static org.junit.Assert.*;
import static com.google.javascript.rhino.testing.Assertions.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.testing.TestBase;

public class FunctionBuilderTest extends TestBase {

  @Test
  public void testFunctionBuilderWithSimpleFunction() {
    FunctionBuilder functionBuilder = new FunctionBuilder(typeRegistry);

    String functionName = "add";
    Node functionSourceNode = sourceFor("function add(x, y) { return x + y; }");

    Node parametersNode = new ArrayType(typeRegistry, new ArrayType(typeRegistry, new IntType(), "*"));

    FunctionBuilder functionBuilderWithParams = functionBuilder
        .withName(functionName)
        .withSourceNode(functionSourceNode)
        .withParams(new FunctionParamBuilder()
            .withName("x")
            .withType(new IntType())
            .withOptional(false)
            .build())
        .withParams(new FunctionParamBuilder()
            .withName("y")
            .withType(new IntType())
            .withOptional(false)
            .build())
        .withReturnType(new IntType())
        .build();

    FunctionType expectedFunctionType = new FunctionType(typeRegistry, functionName, functionSourceNode,
        new ArrowType(typeRegistry, parametersNode, new IntType(), false), null, null, false, false);

    FunctionType actualFunctionType = functionBuilderWithParams.build();

    assertEquals(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testFunctionBuilderWithVoidReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(typeRegistry);

    String functionName = "print";
    Node functionSourceNode = sourceFor("function print(x) { console.log(x); }");

    Node parametersNode = new ArrayType(typeRegistry, new ArrayType(typeRegistry, new IntType(), "*"));

    FunctionBuilder functionBuilderWithParams = functionBuilder
        .withName(functionName)
        .withSourceNode(functionSourceNode)
        .withParams(new FunctionParamBuilder()
            .withName("x")
            .withType(new IntType())
            .withOptional(false)
            .build())
        .withReturnType(VoidType.VOID)
        .build();

    FunctionType expectedFunctionType = new FunctionType(typeRegistry, functionName, functionSourceNode,
        new ArrowType(typeRegistry, parametersNode, VoidType.VOID, false), null, null, false, false);

    FunctionType actualFunctionType = functionBuilderWithParams.build();

    assertEquals(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testFunctionBuilderWithThisType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(typeRegistry);

    String functionName = "printThis";
    Node functionSourceNode = sourceFor("function printThis() { console.log(this); }");

    Node parametersNode = new ArrayType(typeRegistry, new ArrayType(typeRegistry, new ArrayType(typeRegistry, new IntType(), "*"), "*"));

    ObjectType objectType = new ObjectType(typeRegistry, "Object", null);

    FunctionBuilder functionBuilderWithThis = functionBuilder
        .withName(functionName)
        .withSourceNode(functionSourceNode)
        .withParams(new FunctionParamBuilder().withName("args").withType(parametersNode).build())
        .withTypeOfThis(objectType)
        .build();

    FunctionType expectedFunctionType = new FunctionType(typeRegistry, functionName, functionSourceNode,
        new ArrowType(typeRegistry, new ArrayType(typeRegistry, new ArrayType(typeRegistry, new IntType(), "*"), "*"), false), objectType, null, false, false);

    FunctionType actualFunctionType = functionBuilderWithThis.build();

    assertEquals(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testFunctionBuilderWithInferredReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(typeRegistry);

    String functionName = "identity";
    Node functionSourceNode = sourceFor("function identity(x) { return x; }");

    Node parametersNode = new ArrayType(typeRegistry, new ArrayType(typeRegistry, new IntType(), "*"));

    FunctionBuilder functionBuilderWithParams = functionBuilder
        .withName(functionName)
        .withSourceNode(functionSourceNode)
        .withParams(new FunctionParamBuilder()
            .withName("x")
            .withType(new IntType())
            .withOptional(false)
            .build())
        .withReturnType(new IntType())
        .withInferredReturnType(new IntType())
        .build();

    FunctionType expectedFunctionType = new FunctionType(typeRegistry, functionName, functionSourceNode,
        new ArrowType(typeRegistry, parametersNode, new IntType(), true), null, null, false, false);

    FunctionType actualFunctionType = functionBuilderWithParams.build();

    assertEquals(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testFunctionBuilderWithConstructor() {
    FunctionBuilder functionBuilder = new FunctionBuilder(typeRegistry);

    String functionName = "Person";
    Node functionSourceNode = sourceFor("function Person(name, age) { this.name = name; this.age = age; }");

    Node parametersNode = new ArrayType(typeRegistry, new ArrayType(typeRegistry, new StringType(), "*"), "*");

    ObjectType constructorThisType = new ObjectType(typeRegistry, "Person", null);

    FunctionBuilder constructorFunctionBuilder = functionBuilder
        .withName(functionName)
        .withSourceNode(functionSourceNode)
        .withParams(new FunctionParamBuilder()
            .withName("name")
            .withType(new StringType())
            .withOptional(false)
            .build())
        .withParams(new FunctionParamBuilder()
            .withName("age")
            .withType(new IntType())
            .withOptional(false)
            .build())
        .forConstructor()
        .withTypeOfThis(constructorThisType)
        .build();

    FunctionType expectedFunctionType = new FunctionType(typeRegistry, functionName, functionSourceNode,
        new ConstructorType(typeRegistry, constructorThisType, parametersNode), null, null, true, false);

    FunctionType actualFunctionType = constructorFunctionBuilder.build();

    assertEquals(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testFunctionBuilderWithNativeType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(typeRegistry);

    String functionName = "alert";
    Node functionSourceNode = null;

    Node parametersNode = new ArrayType(typeRegistry, new ArrayType(typeRegistry, new StringType(), "*"));

    FunctionBuilder nativeFunctionBuilder = functionBuilder
        .withName(functionName)
        .withSourceNode(functionSourceNode)
        .withParams(new FunctionParamBuilder()
            .withName("message")
            .withType(new StringType())
            .withOptional(false)
            .build())
        .forNativeType()
        .build();

    FunctionType expectedFunctionType = new FunctionType(typeRegistry, functionName, functionSourceNode,
        new ArrowType(typeRegistry, parametersNode, VoidType.VOID, false), null, null, false, true);

    FunctionType actualFunctionType = nativeFunctionBuilder.build();

    assertEquals(expectedFunctionType, actualFunctionType);
  }

  @Test
  public void testFunctionBuilderWithTemplateTypeName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(typeRegistry);

    String functionName = "map";
    Node functionSourceNode = sourceFor("function map(array, mapper) { var result = []; for (var i = 0; i < array.length; i++) result.push(mapper(array[i])); return result; }");

    Node parametersNode = new ArrayType(typeRegistry, new ArrayType(typeRegistry, new FunctionType(typeRegistry, "mapper", null, null, null, null, false, false), "*"));

    FunctionBuilder functionBuilderWithTemplateTypeName = functionBuilder
        .withName(functionName)
        .withSourceNode(functionSourceNode)
        .withParams(new FunctionParamBuilder()
            .withName("array")
            .withType(new ArrayType(typeRegistry, new IntType(), "*"))
            .withOptional(false)
            .build())
        .withParams(new FunctionParamBuilder()
            .withName("mapper")
            .withType(new ArrowType(typeRegistry, new ArrayType(typeRegistry, new IntType(), "*"), new IntType(), false))
            .withOptional(false)
            .build())
        .withTemplateName("Array<T>->Array<U>")
        .build();

    FunctionType expectedFunctionType = new FunctionType(typeRegistry, functionName, functionSourceNode,
        new ArrowType(typeRegistry, new ArrayType(typeRegistry, new IntType(), "*"), new ArrayType(typeRegistry, new IntType(), "*"), false), null, "Array<T>->Array<U>", false, false);

    FunctionType actualFunctionType = functionBuilderWithTemplateTypeName.build();

    assertEquals(expectedFunctionType, actualFunctionType);
  }
}