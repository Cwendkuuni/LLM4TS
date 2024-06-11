import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FunctionBuilderTest {

 @Test
 public void testFunctionBuilder() {
 FunctionBuilder functionBuilder = new FunctionBuilder(new JSTypeRegistry());

 // Test withName method
 FunctionBuilder functionBuilder1 = functionBuilder.withName("add");

 // Test withSourceNode method
 Node sourceNode = new Node("add(a, b)");
 FunctionBuilder functionBuilder2 = functionBuilder1.withSourceNode(sourceNode);

 // Test withParams method
 FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder("a", JSType.NUMBER);
 FunctionBuilder functionBuilder3 = functionBuilder2.withParams(functionParamBuilder.build());
 FunctionBuilder functionBuilder4 = functionBuilder3.withParams(new FunctionParamBuilder("b", JSType.NUMBER).build());

 // Test withReturnType method
 JSType returnType = JSType.NUMBER;
 FunctionBuilder functionBuilder5 = functionBuilder4.withReturnType(returnType);

 // Test withInferredReturnType method
 JSType inferredReturnType = JSType.BOOLEAN;
 FunctionBuilder functionBuilder6 = functionBuilder5.withInferredReturnType(inferredReturnType);

 // Test withTypeOfThis method
 ObjectType typeOfThis = new ObjectType("Object");
 FunctionBuilder functionBuilder7 = functionBuilder6.withTypeOfThis(typeOfThis);

 // Test withTemplateName method
 String templateTypeName = "TemplateFunction";
 FunctionBuilder functionBuilder8 = functionBuilder7.withTemplateName(templateTypeName);

 // Test forConstructor method
 FunctionBuilder functionBuilder9 = functionBuilder8.forConstructor();

 // Test copyFromOtherFunction method
 FunctionType otherType = new FunctionType(new JSTypeRegistry(), "add", null, new ArrowType(new JSTypeRegistry(), null, JSType.NUMBER, false), null, null, true, false);
 FunctionBuilder functionBuilder10 = functionBuilder2.copyFromOtherFunction(otherType);

 FunctionType functionType = functionBuilder10.build();

 // Assertions
 assertNotNull(functionType.getName());
 assertEquals(sourceNode, functionType.getSource());
 assertNotNull(functionType.getParametersNode());
 assertEquals(returnType, functionType.getReturnType());
 assertNotNull(functionType.getTypeOfThis());
 assertEquals(templateTypeName, functionType.getTemplateTypeName());
 assertEquals(true, functionType.isConstructor());
 assertEquals(false, functionType.isNativeObjectType());
 }
}