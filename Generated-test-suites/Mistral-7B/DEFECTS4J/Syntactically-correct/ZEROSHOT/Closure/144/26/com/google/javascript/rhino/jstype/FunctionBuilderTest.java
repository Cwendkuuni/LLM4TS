import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class FunctionBuilderTest {

 private JSTypeRegistry registry;
 private FunctionBuilder funcBuilder;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 funcBuilder = new FunctionBuilder(registry);
 }

 @Test
 public void testFunctionBuilderWithName() {
 funcBuilder.withName("testFunction");
 FunctionType funcType = funcBuilder.build();
 assertEquals("testFunction", funcType.getReferenceName());
 }

 @Test
 public void testFunctionBuilderWithSourceNode() {
 Node sourceNode = new Node();
 funcBuilder.withSourceNode(sourceNode);
 FunctionType funcType = funcBuilder.build();
 assertEquals(sourceNode, funcType.getSource());
 }

 @Test
 public void testFunctionBuilderWithParams() {
 FunctionParamBuilder paramsBuilder = new FunctionParamBuilder();
 paramsBuilder.withName("param1").withType(JSType.STRING);
 Node paramsNode = paramsBuilder.build();
 funcBuilder.withParams(paramsBuilder);
 funcBuilder.withParamsNode(paramsNode);
 FunctionType funcType = funcBuilder.build();
 assertEquals(paramsNode, funcType.getParametersNode());
 }

 @Test
 public void testFunctionBuilderWithReturnType() {
 JSType returnType = JSType.NUMBER;
 funcBuilder.withReturnType(returnType);
 FunctionType funcType = funcBuilder.build();
 assertEquals(returnType, funcType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithInferredReturnType() {
 JSType returnType = JSType.VOID;
 funcBuilder.withInferredReturnType(returnType);
 FunctionType funcType = funcBuilder.build();
 assertTrue(funcType.isInferredReturnType());
 }

 @Test
 public void testFunctionBuilderWithTypeOfThis() {
 ObjectType typeOfThis = new ObjectType(registry, "TestObject");
 funcBuilder.withTypeOfThis(typeOfThis);
 FunctionType funcType = funcBuilder.build();
 assertEquals(typeOfThis, funcType.getTypeOfThis());
 }

 @Test
 public void testFunctionBuilderWithTemplateName() {
 funcBuilder.withTemplateName("TemplateFunction");
 FunctionType funcType = funcBuilder.build();
 assertEquals("TemplateFunction", funcType.getTemplateTypeName());
 }

 @Test
 public void testFunctionBuilderForConstructor() {
 funcBuilder.forConstructor();
 FunctionType funcType = funcBuilder.build();
 assertTrue(funcType.isConstructor());
 }

 @Test
 public void testFunctionBuilderForNativeType() {
 funcBuilder.forNativeType();
 FunctionType funcType = funcBuilder.build();
 assertTrue(funcType.isNativeObjectType());
 }

 @Test
 public void testFunctionBuilderCopyFromOtherFunction() {
 FunctionType otherType = new FunctionType(registry, "otherFunction", null, null, null, null, true, false);
 funcBuilder.copyFromOtherFunction(otherType);
 FunctionType funcType = funcBuilder.build();
 assertEquals(otherType.getReferenceName(), funcType.getReferenceName());
 assertEquals(otherType.getSource(), funcType.getSource());
 assertEquals(otherType.getParametersNode(), funcType.getParametersNode());
 assertEquals(otherType.getReturnType(), funcType.getReturnType());
 assertEquals(otherType.getTypeOfThis(), funcType.getTypeOfThis());
 assertEquals(otherType.getTemplateTypeName(), funcType.getTemplateTypeName());
 assertEquals(otherType.isConstructor(), funcType.isConstructor());
 assertEquals(otherType.isNativeObjectType(), funcType.isNativeObjectType());
 }
}