import static org.junit.Assert.assertNotNull;

public class FunctionBuilderTest {
  @Test
  public void testBuildFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String functionName = "myFunction";
    Node functionSourceNode = null; // Create a valid source node
    FunctionParamBuilder functionParams = new FunctionParamBuilder();
    Node functionParamsNode = functionParams.build();
    JSType returnType = new IntType();
    ObjectType typeOfThis = new ObjectType("Object");
    String templateTypeName = "TemplateFunction";
    boolean isConstructor = false;
    boolean isNativeType = false;

    functionBuilder
        .withName(functionName)
        .withSourceNode(functionSourceNode)
        .withParams(functionParams)
        .withReturnType(returnType)
        .withTypeOfThis(typeOfThis)
        .withTemplateName(templateTypeName)
        .forConstructor() // Or forNativeType()
        .copyFromOtherFunction(null) // Or another FunctionType
        .build();

    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
  }
}