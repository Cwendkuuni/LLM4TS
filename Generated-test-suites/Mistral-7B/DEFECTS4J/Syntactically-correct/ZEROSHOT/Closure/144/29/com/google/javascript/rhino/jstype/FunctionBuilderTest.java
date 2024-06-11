import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createString;
import static com.google.javascript.rhino.JSTypeFactory.BooleanType;
import static com.google.javascript.rhino.JSTypeFactory.StringType;
import static com.google.javascript.rhino.JSTypeFactory.IntType;
import static com.google.javascript.rhino.JSTypeFactory.ObjectType;
import static com.google.javascript.rhino.JSTypeFactory.createFunctionType;
import static com.google.javascript.rhino.JSTypeFactory.createArrowFunctionType;
import static com.google.javascript.rhino.JSTypeFactory.createFunctionParamType;
import static com.google.javascript.rhino.JSTypeFactory.createInterfaceType;

import org.junit.Test;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

  @Test
  public void testFunctionBuilder() {
    JSTypeRegistry registry = new JSTypeRegistry();

    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    // Test setting name
    functionBuilder.withName("add");
    FunctionType addFunctionType = functionBuilder.build();
    assertEquals("add", addFunctionType.getReferenceName());

    // Test setting source node
    Node addSourceNode = createNode(createString("function add(x, y) { return x + y; }"));
    functionBuilder.withSourceNode(addSourceNode);
    FunctionType addFunctionType2 = functionBuilder.build();
    assertEquals(addSourceNode, addFunctionType2.getSource());

    // Test setting parameters
    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
    paramBuilder.withName("x").withType(IntType);
    Node addParamsNode = paramBuilder.build();
    paramBuilder.withName("y").withType(IntType);
    Node addParamsNode2 = paramBuilder.build();
    Node addParamsNodeList = createNode(createNode(Node.OP_ARRAY), addParamsNode, addParamsNode2);

    functionBuilder.withParams(paramBuilder);
    FunctionType addFunctionType3 = functionBuilder.build();
    assertEquals(addParamsNodeList, addFunctionType3.getParametersNode());

    // Test setting return type
    functionBuilder.withReturnType(IntType);
    FunctionType addFunctionType4 = functionBuilder.build();
    assertEquals(IntType.getInstance(), addFunctionType4.getReturnType());

    // Test setting return type and inferred
    functionBuilder.withInferredReturnType(BooleanType);
    FunctionType addFunctionType5 = functionBuilder.build();
    assertTrue(addFunctionType5.isInferredReturnType());

    // Test setting type of this
    ObjectType addThisType = ObjectType.getObjectType("Add");
    functionBuilder.withTypeOfThis(addThisType);
    FunctionType addFunctionType6 = functionBuilder.build();
    assertEquals(addThisType, addFunctionType6.getTypeOfThis());

    // Test setting template type name
    functionBuilder.withTemplateName("Add");
    FunctionType addFunctionType7 = functionBuilder.build();
    assertEquals("Add", addFunctionType7.getTemplateTypeName());

    // Test constructor
    functionBuilder.forConstructor();
    FunctionType addConstructorType = functionBuilder.build();
    assertTrue(addConstructorType.isConstructor());

    // Test native type
    functionBuilder.forNativeType();
    FunctionType addNativeType = functionBuilder.build();
    assertTrue(addNativeType.isNativeObjectType());

    // Test copying
    FunctionType addFunctionType8 = createFunctionType(registry, "add", null, null, null, null, Boolean.TRUE, Boolean.TRUE);
    functionBuilder.copyFromOtherFunction(addFunctionType8);
    FunctionType addFunctionType9 = functionBuilder.build();
    assertEquals(addFunctionType8.getReferenceName(), addFunctionType9.getReferenceName());
    assertEquals(addFunctionType8.getSource(), addFunctionType9.getSource());
    assertEquals(addFunctionType8.getParametersNode(), addFunctionType9.getParametersNode());
    assertEquals(addFunctionType8.getReturnType(), addFunctionType9.getReturnType());
    assertEquals(addFunctionType8.getTypeOfThis(), addFunctionType9.getTypeOfThis());
    assertEquals(addFunctionType8.getTemplateTypeName(), addFunctionType9.getTemplateTypeName());
    assertEquals(addFunctionType8.isConstructor(), addFunctionType9.isConstructor());
    assertEquals(addFunctionType8.isNativeObjectType(), addFunctionType9.isNativeObjectType());

    // Test arrow function
    FunctionBuilder arrowFunctionBuilder = new FunctionBuilder(registry);
    arrowFunctionBuilder.withName("addArrow");
    arrowFunctionBuilder.withParamsNode(addParamsNodeList);
    arrowFunctionBuilder.withReturnType(IntType);
    Node arrowSourceNode = createNode(Node.OP_FUNCTION, null, addParamsNodeList, createNode(Node.OP_BLOCK, null));
    arrowFunctionBuilder.withSourceNode(arrowSourceNode);
    ArrowType addArrowFunctionType = (ArrowType) arrowFunctionBuilder.build();
    assertEquals("addArrow", addArrowFunctionType.getReferenceName());
    assertEquals(addParamsNodeList, addArrowFunctionType.getParametersNode());
    assertEquals(IntType.getInstance(), addArrowFunctionType.getReturnType());

    // Test copying arrow function
    ArrowType addArrowFunctionType2 = createArrowFunctionType(registry, "addArrow", null, IntType.getInstance(), null, null, Boolean.TRUE);
    arrowFunctionBuilder.copyFromOtherFunction(addArrowFunctionType2);
    ArrowType addArrowFunctionType3 = (ArrowType) arrowFunctionBuilder.build();
    assertEquals(addArrowFunctionType2.getReferenceName(), addArrowFunctionType3.getReferenceName());
    assertEquals(addArrowFunctionType2.getParametersNode(), addArrowFunctionType3.getParametersNode());
    assertEquals(addArrowFunctionType2.getReturnType(), addArrowFunctionType3.getReturnType());

    // Test interface function
    JSTypeRegistry registry2 = new JSTypeRegistry();
    JSTypeInterface addInterfaceType = createInterfaceType(registry2, "Add");
    FunctionBuilder interfaceFunctionBuilder = new FunctionBuilder(registry2);
    interfaceFunctionBuilder.withName("addInterface");
    interfaceFunctionBuilder.withParamsNode(addParamsNodeList);
    interfaceFunctionBuilder.withReturnType(IntType);
    interfaceFunctionBuilder.withSourceNode(addSourceNode);
    interfaceFunctionBuilder.copyFromOtherFunction(addFunctionType8);
    interfaceFunctionBuilder.withTypeOfThis(addThisType);
    interfaceFunctionBuilder.forConstructor();
    FunctionType addInterfaceFunctionType = interfaceFunctionBuilder.build();
    assertEquals(addInterfaceType, addInterfaceFunctionType.getTypeOfThis());
    assertEquals(addFunctionType8.getReferenceName(), addInterfaceFunctionType.getReferenceName());
    assertEquals(addFunctionType8.getSource(), addInterfaceFunctionType.getSource());
    assertEquals(addFunctionType8.getParametersNode(), addInterfaceFunctionType.getParametersNode());
    assertEquals(addFunctionType8.getReturnType(), addInterfaceFunctionType.getReturnType());
    assertEquals(addInterfaceType.getName(), addInterfaceFunctionType.getTemplateTypeName());
    assertTrue(addInterfaceFunctionType.isConstructor());
    assertTrue(addInterfaceFunctionType.isInterface());
  }

}