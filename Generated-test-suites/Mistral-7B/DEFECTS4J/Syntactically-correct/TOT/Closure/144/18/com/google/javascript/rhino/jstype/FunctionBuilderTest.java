import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import static com.google.javascript.rhino.NodeFactory.*;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

  private JSTypeRegistry registry;

  @ org.junit.Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @ org.junit.Test
  public void testFunctionBuilder() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String functionName = "myFunction";
    FunctionBuilder function = functionBuilder.withName(functionName)
        .withParams(new FunctionParamBuilder().withName("param1").build())
        .withParams(new FunctionParamBuilder().withName("param2").build())
        .withReturnType(booleanType())
        .build();

    FunctionType actualFunctionType = function.build();
    FunctionType expectedFunctionType = new FunctionType(registry, functionName, null,
        new ArrowType(registry, null, booleanType(), false), null, null, false, false);

    assertEquals(expectedFunctionType, actualFunctionType);
  }

  @ org.junit.Test
  public void testFunctionBuilderWithInferredReturnType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String functionName = "myFunction";
    FunctionBuilder function = functionBuilder.withName(functionName)
        .withParams(new FunctionParamBuilder().withName("param1").build())
        .withParams(new FunctionParamBuilder().withName("param2").build())
        .withInferredReturnType(intType())
        .build();

    FunctionType actualFunctionType = function.build();
    FunctionType expectedFunctionType = new FunctionType(registry, functionName, null,
        new ArrowType(registry, null, null, true), null, null, false, false);

    assertEquals(expectedFunctionType, actualFunctionType);
  }

  @ org.junit.Test
  public void testFunctionBuilderWithSourceNode() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String functionName = "myFunction";
    String source = "function myFunction(param1, param2) { return true; }";
    Node sourceNode = parse(source);

    FunctionBuilder function = functionBuilder.withName(functionName)
        .withSourceNode(sourceNode)
        .withParams(new FunctionParamBuilder().withName("param1").build())
        .withParams(new FunctionParamBuilder().withName("param2").build())
        .withReturnType(booleanType())
        .build();

    FunctionType actualFunctionType = function.build();
    FunctionType expectedFunctionType = new FunctionType(registry, functionName, sourceNode,
        new ArrowType(registry, null, booleanType(), false), null, null, false, false);

    assertEquals(expectedFunctionType, actualFunctionType);
  }

  @ org.junit.Test
  public void testFunctionBuilderWithTypeOfThis() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String functionName = "myFunction";
    FunctionBuilder function = functionBuilder.withName(functionName)
        .withParams(new FunctionParamBuilder().withName("param1").build())
        .withParams(new FunctionParamBuilder().withName("param2").build())
        .withReturnType(booleanType())
        .withTypeOfThis(ObjectType.OBJECT)
        .build();

    FunctionType actualFunctionType = function.build();
    FunctionType expectedFunctionType = new FunctionType(registry, functionName, null,
        new ArrowType(registry, null, booleanType(), false), ObjectType.OBJECT, null, false, false);

    assertEquals(expectedFunctionType, actualFunctionType);
  }

  @ org.junit.Test
  public void testFunctionBuilderWithTemplateName() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String functionName = "myFunction";
    String templateTypeName = "MyTemplateType";

    FunctionBuilder function = functionBuilder.withName(functionName)
        .withParams(new FunctionParamBuilder().withName("param1").build())
        .withParams(new FunctionParamBuilder().withName("param2").build())
        .withReturnType(booleanType())
        .withTemplateName(templateTypeName)
        .build();

    FunctionType actualFunctionType = function.build();
    FunctionType expectedFunctionType = new FunctionType(registry, functionName, null,
        new ArrowType(registry, null, booleanType(), false), null, templateTypeName, false, false);

    assertEquals(expectedFunctionType, actualFunctionType);
  }

  @ org.junit.Test
  public void testFunctionBuilderWithConstructor() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String functionName = "myConstructor";
    FunctionBuilder function = functionBuilder.withName(functionName)
        .withParams(new FunctionParamBuilder().withName("param1").build())
        .withParams(new FunctionParamBuilder().withName("param2").build())
        .forConstructor()
        .build();

    FunctionType actualFunctionType = function.build();
    FunctionType expectedFunctionType = new FunctionType(registry, functionName, null,
        new ConstructorType(registry, new ObjectType(registry, "Object"),
            new ArrowType(registry, null, null, false), null), null, null, true, false);

    assertEquals(expectedFunctionType, actualFunctionType);
  }

  @ org.junit.Test
  public void testFunctionBuilderWithNativeType() {
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String functionName = "myFunction";
    FunctionBuilder function = functionBuilder.withName(functionName)
        .forNativeType()
        .build();

    FunctionType actualFunctionType = function.build();
    FunctionType expectedFunctionType = new FunctionType(registry, functionName, null,
        null, null, null, false, true);

    assertEquals(expectedFunctionType, actualFunctionType);
  }
}