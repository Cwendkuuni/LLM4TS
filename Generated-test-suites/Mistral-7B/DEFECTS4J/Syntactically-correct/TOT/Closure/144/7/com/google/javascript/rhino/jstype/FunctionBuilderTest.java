import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createArray;
import static com.google.javascript.rhino.NodeFactory.createBoolean;
import static com.google.javascript.rhino.NodeFactory.createNull;
import static com.google.javascript.rhino.NodeFactory.createNumber;
import static com.google.javascript.rhino.NodeFactory.createString;
import static com.google.javascript.rhino.NodeFactory.createThis;
import static com.google.javascript.rhino.NodeFactory.createUndefined;
import static com.google.javascript.rhino.JSType.ARROW;
import static com.google.javascript.rhino.JSType.BOOLEAN;
import static com.google.javascript.rhino.JSType.INT;
import static com.google.javascript.rhino.JSType.OBJECT;
import static com.google.javascript.rhino.JSType.STRING;
import static com.google.javascript.rhino.JSType.VOID;

import org.junit.Test;

import com.google.javascript.rhino.Context;
import com.google.javascript.rhino.FunctionType;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class FunctionBuilderTest {

  @Test
  public void testCreateSimpleFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
    paramBuilder.withName("param1").withType(INT);

    Node parametersNode = paramBuilder.build();

    FunctionType functionType = builder.withName("add")
        .withParams(paramBuilder)
        .withParamsNode(parametersNode)
        .withReturnType(INT)
        .build();

    assertNotNull(functionType);
    assertEquals("add", functionType.getReferenceName());
    assertEquals(ARROW, functionType.getType());
    assertEquals(INT, functionType.getReturnType());

    Context context = Context.enter();
    Node args = createArray(context, createNumber(1), createNumber(2));
    Node result = context.callFunction(functionType, createThis(context), args);
    assertEquals(createNumber(3), result);

    Context.exit();
  }

  @Test
  public void testCreateFunctionTypeWithObjectTypeParam() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
    paramBuilder.withName("param1").withType(new ObjectType(registry, "MyObjectType"));

    Node parametersNode = paramBuilder.build();

    FunctionType functionType = builder.withName("myFunction")
        .withParams(paramBuilder)
        .withParamsNode(parametersNode)
        .withReturnType(VOID)
        .build();

    assertNotNull(functionType);
    assertEquals("myFunction", functionType.getReferenceName());
    assertEquals(ARROW, functionType.getType());
    assertNull(functionType.getReturnType());

    Context context = Context.enter();
    Node myObject = new ObjectType(registry, "MyObjectType").getInstance(context);
    Node args = createArray(context, myObject);
    context.callFunction(functionType, createThis(context), args);
    assertEquals(createNull(), context.getReturnValue());

    Context.exit();
  }

  @Test
  public void testCreateFunctionTypeWithErrorScenarios() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    try {
      builder.build();
      fail("expected exception");
    } catch (IllegalArgumentException e) {
      //expected
    }

    try {
      FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
      paramBuilder.build();
      fail("expected exception");
    } catch (IllegalArgumentException e) {
      //expected
    }

    try {
      FunctionBuilder builder2 = new FunctionBuilder(registry);
      FunctionType functionType = builder2.build();
      fail("expected exception");
    } catch (IllegalArgumentException e) {
      //expected
    }
  }

  @Test
  public void testCreateFunctionTypeWithTemplateTypeName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    FunctionParamBuilder paramBuilder = new FunctionParamBuilder(registry);
    paramBuilder.withName("param1").withType(INT);

    Node parametersNode = paramBuilder.build();

    FunctionType functionType = builder.withName("add")
        .withTemplateName("Add")
        .withParams(paramBuilder)
        .withParamsNode(parametersNode)
        .withReturnType(INT)
        .build();

    assertNotNull(functionType);
    assertEquals("Add", functionType.getTemplateTypeName());
    assertEquals("add", functionType.getReferenceName());
    assertEquals(ARROW, functionType.getType());
    assertEquals(INT, functionType.getReturnType());

    Context context = Context.enter();
    Node args = createArray(context, createNumber(1), createNumber(2));
    Node result = context.callFunction(functionType, createThis(context), args);
    assertEquals(createNumber(3), result);

    Context.exit();
  }

  @Test
  public void testCreateNativeFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);

    FunctionType functionType = builder.forNativeType().build();
    assertNotNull(functionType);
    assertTrue(functionType.isNativeObjectType());
    assertNull(functionType.getReferenceName());
    assertNull(functionType.getSource());
    assertNull(functionType.getParametersNode());
    assertNull(functionType.getReturnType());
    assertNull(functionType.getTypeOfThis());
    assertNull(functionType.getTemplateTypeName());
  }
}