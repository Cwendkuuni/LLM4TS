import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import static com.google.javascript.rhino.JSType.Numeric.INT;
import static com.google.javascript.rhino.JSType.Numeric.NUMBER;
import static com.google.javascript.rhino.JSType.String.STRING;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

 private FunctionBuilder functionBuilder;
 private FunctionType functionType;

 @Before
 public void setUp() {
 JSTypeRegistry registry = new JSTypeRegistry();
 functionBuilder = new FunctionBuilder(registry);
 }

 @Test
 public void testFunctionBuilderWithNameAndReturnType() {
 functionType = functionBuilder.withName("add")
 .withReturnType(NUMBER)
 .build();
 assertEquals("add", functionType.getReferenceName());
 assertEquals(NUMBER, functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithNameAndParametersAndReturnType() {
 FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder("x");
 FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder("y");
 Node parametersNode = paramBuilder1.build().concat(paramBuilder2.build());
 functionType = functionBuilder.withName("add")
 .withParams(parametersBuilder -> parametersBuilder.build())
 .withParamsNode(parametersNode)
 .withReturnType(NUMBER)
 .build();
 assertEquals("add", functionType.getReferenceName());
 assertEquals(NUMBER, functionType.getReturnType());
 assertEquals(new ArrayType(INT), functionType.getParameters()[0].getType());
 assertEquals(INT, functionType.getParameters()[1].getType());
 }

 @Test
 public void testFunctionBuilderWithNameAndReturnTypeAndInferred() {
 functionType = functionBuilder.withName("add")
 .withReturnType(NUMBER)
 .withInferredReturnType(NUMBER)
 .build();
 assertEquals("add", functionType.getReferenceName());
 assertEquals(NUMBER, functionType.getReturnType());
 assertTrue(functionType.isInferredReturnType());
 }

 @Test
 public void testFunctionBuilderWithNameAndTemplateTypeName() {
 functionType = functionBuilder.withName("add")
 .withTemplateName("IntegerFunction")
 .build();
 assertEquals("add", functionType.getReferenceName());
 assertEquals("IntegerFunction", functionType.getTemplateTypeName());
 }

 @Test
 public void testFunctionBuilderWithNameAndSourceNode() {
 Node sourceNode = new ObjectLiteral();
 functionType = functionBuilder.withName("add")
 .withSourceNode(sourceNode)
 .build();
 assertEquals("add", functionType.getReferenceName());
 assertEquals(sourceNode, functionType.getSource());
 }

 @Test
 public void testFunctionBuilderWithNameAndParametersAndReturnTypeAndTemplateTypeName() {
 FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder("x");
 FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder("y");
 Node parametersNode = paramBuilder1.build().concat(paramBuilder2.build());
 functionType = functionBuilder.withName("add")
 .withParams(parametersBuilder -> parametersBuilder.build())
 .withParamsNode(parametersNode)
 .withTemplateName("IntegerFunction")
 .withReturnType(NUMBER)
 .build();
 assertEquals("add", functionType.getReferenceName());
 assertEquals("IntegerFunction", functionType.getTemplateTypeName());
 assertEquals(new ArrayType(INT), functionType.getParameters()[0].getType());
 assertEquals(INT, functionType.getParameters()[1].getType());
 assertEquals(NUMBER, functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithNameAndParametersAndReturnTypeAndTypeOfThis() {
 ObjectType typeOfThis = new ObjectType("Object");
 functionType = functionBuilder.withName("add")
 .withParams(parametersBuilder -> parametersBuilder.build())
 .withParamsNode(NodeFactory.createList(new StringLiteral("x"), new StringLiteral("y")))
 .withReturnType(NUMBER)
 .withTypeOfThis(typeOfThis)
 .build();
 assertEquals("add", functionType.getReferenceName());
 assertEquals(typeOfThis, functionType.getTypeOfThis());
 }

 @Test
 public void testFunctionBuilderWithNameAndParametersAndReturnTypeAndTypeOfThisAndTemplateTypeName() {
 FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder("x");
 FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder("y");
 Node parametersNode = paramBuilder1.build().concat(paramBuilder2.build());
 ObjectType typeOfThis = new ObjectType("Object");
 functionType = functionBuilder.withName("add")
 .withParams(parametersBuilder -> parametersBuilder.build())
 .withParamsNode(parametersNode)
 .withTemplateName("IntegerFunction")
 .withReturnType(NUMBER)
 .withTypeOfThis(typeOfThis)
 .build();
 assertEquals("add", functionType.getReferenceName());
 assertEquals("IntegerFunction", functionType.getTemplateTypeName());
 assertEquals(typeOfThis, functionType.getTypeOfThis());
 assertEquals(new ArrayType(INT), functionType.getParameters()[0].getType());
 assertEquals(INT, functionType.getParameters()[1].getType());
 assertEquals(NUMBER, functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithNameAndParametersAndReturnTypeAndTypeOfThisAndTemplateTypeNameAndIsConstructor() {
 FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder("x");
 FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder("y");
 Node parametersNode = paramBuilder1.build().concat(paramBuilder2.build());
 ObjectType typeOfThis = new ObjectType("Object");
 functionType = functionBuilder.withName("add")
 .withParams(parametersBuilder -> parametersBuilder.build())
 .withParamsNode(parametersNode)
 .withTemplateName("IntegerFunction")
 .withReturnType(NUMBER)
 .withTypeOfThis(typeOfThis)
 .forConstructor()
 .build();
 assertEquals("add", functionType.getReferenceName());
 assertEquals("IntegerFunction", functionType.getTemplateTypeName());
 assertEquals(typeOfThis, functionType.getTypeOfThis());
 assertEquals(new ArrayType(INT), functionType.getParameters()[0].getType());
 assertEquals(INT, functionType.getParameters()[1].getType());
 assertEquals(NUMBER, functionType.getReturnType());
 assertTrue(functionType.isConstructor());
 }

 @Test
 public void testFunctionBuilderWithNameAndParametersAndReturnTypeAndTypeOfThisAndTemplateTypeNameAndIsNativeType() {
 FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder("x");
 FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder("y");
 Node parametersNode = paramBuilder1.build().concat(paramBuilder2.build());
 ObjectType typeOfThis = new ObjectType("Object");
 functionType = functionBuilder.withName("add")
 .withParams(parametersBuilder -> parametersBuilder.build())
 .withParamsNode(parametersNode)
 .withTemplateName("IntegerFunction")
 .withReturnType(NUMBER)
 .withTypeOfThis(typeOfThis)
 .forNativeType()
 .build();
 assertEquals("add", functionType.getReferenceName());
 assertEquals("IntegerFunction", functionType.getTemplateTypeName());
 assertEquals(typeOfThis, functionType.getTypeOfThis());
 assertEquals(new ArrayType(INT), functionType.getParameters()[0].getType());
 assertEquals(INT, functionType.getParameters()[1].getType());
 assertEquals(NUMBER, functionType.getReturnType());
 assertTrue(functionType.isNativeObjectType());
 }

 @Test
 public void testFunctionBuilderCopy() {
 FunctionType originalType = functionBuilder.withName("add")
 .withParams(parametersBuilder -> parametersBuilder.build())
 .withParamsNode(NodeFactory.createList(new StringLiteral("x"), new StringLiteral("y")))
 .withReturnType(NUMBER)
 .build();
 functionBuilder.copyFromOtherFunction(originalType);
 FunctionType copyType = functionBuilder.build();
 assertEquals(originalType, copyType);
 }
}