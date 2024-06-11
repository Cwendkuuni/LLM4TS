import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeFactory.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.testing.TestDrive;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeWithTwoParametersAndKnownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node functionNode = Node.newFunctionNode(
        null, null, null, null, null, null, null, null, null, new Node[0]);
    JSTypeRegistry scope = new JSTypeRegistry(registry);
    StaticScope<JSType> staticScope = new StaticScope<>(scope);

    JSType parametersType = createParametersWithVarArgs(registry, null,
        createParameter(registry, "a", INT), createParameter(registry, "b", INT));
    ArrowType arrowType = new ArrowType(registry, parametersType, INT);

    TestDrive td = new TestDrive();
    td.setErrorReporter(new ErrorReporter() {
      @Override
      public void error(String message) {
        fail(message);
      }

      @Override
      public void warning(String message) {
        fail(message);
      }
    });

    JSType resolvedArrowType = arrowType.resolveInternal(
        new ErrorReporter() {
          @Override
          public void error(String message) {
            fail(message);
          }

          @Override
          public void warning(String message) {
            fail(message);
          }
        }, staticScope);

    td.addTopLevelCode("var f = function(" + functionNode.toString() + ") {" +
        "return function(a, b) {" +
        "  return a + b;" +
        "};");

    JSTypeFunction functionType = (JSTypeFunction) td.getGlobalScope().get("f")
        .getType();
    JSType expectedFunctionType = new ArrowType(registry, parametersType, INT);

    assertEquals(expectedFunctionType, resolvedArrowType);
  }

  @Test
  public void testArrowTypeWithOneParameterAndInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node functionNode = Node.newFunctionNode(
        null, null, null, null, null, null, null, null, null, new Node[0]);
    JSTypeRegistry scope = new JSTypeRegistry(registry);
    StaticScope<JSType> staticScope = new StaticScope<>(scope);

    JSType parametersType = createParametersWithVarArgs(registry, null,
        createParameter(registry, "a", INT));
    ArrowType arrowType = new ArrowType(registry, parametersType, null, true);

    TestDrive td = new TestDrive();
    td.setErrorReporter(new ErrorReporter() {
      @Override
      public void error(String message) {
        fail(message);
      }

      @Override
      public void warning(String message) {
        fail(message);
      }
    });

    JSType resolvedArrowType = arrowType.resolveInternal(
        new ErrorReporter() {
          @Override
          public void error(String message) {
            fail(message);
          }

          @Override
          public void warning(String message) {
            fail(message);
          }
        }, staticScope);

    td.addTopLevelCode("var f = function(" + functionNode.toString() + ") {" +
        "  return function(a) {" +
        "    return a;" +
        "  };" +
        "};");

    JSTypeFunction functionType = (JSTypeFunction) td.getGlobalScope().get("f")
        .getType();
    JSType expectedFunctionType = new ArrowType(registry, parametersType, UNKNOWN_TYPE, true);

    assertEquals(expectedFunctionType, resolvedArrowType);
  }

  @Test
  public void testArrowTypeWithTwoParametersAndInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node functionNode = Node.newFunctionNode(
        null, null, null, null, null, null, null, null, null, new Node[0]);
    JSTypeRegistry scope = new JSTypeRegistry(registry);
    StaticScope<JSType> staticScope = new StaticScope<>(scope);

    JSType parametersType = createParametersWithVarArgs(registry, null,
        createParameter(registry, "a", INT), createParameter(registry, "b", INT));
    ArrowType arrowType = new ArrowType(registry, parametersType, null, true);

    TestDrive td = new TestDrive();
    td.setErrorReporter(new ErrorReporter() {
      @Override
      public void error(String message) {
        fail(message);
      }

      @Override
      public void warning(String message) {
        fail(message);
      }
    });

    JSType resolvedArrowType = arrowType.resolveInternal(
        new ErrorReporter() {
          @Override
          public void error(String message) {
            fail(message);
          }

          @Override
          public void warning(String message) {
            fail(message);
          }
        }, staticScope);

    td.addTopLevelCode("var f = function(" + functionNode.toString() + ") {" +
        "  return function(a, b) {" +
        "  };" +
        "};");

    JSTypeFunction functionType = (JSTypeFunction) td.getGlobalScope().get("f")
        .getType();
    JSType expectedFunctionType = new ArrowType(registry, parametersType, UNKNOWN_TYPE, true);

    assertEquals(expectedFunctionType, resolvedArrowType);
  }

  @Test
  public void testArrowTypeComparison() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node functionNode = Node.newFunctionNode(
        null, null, null, null, null, null, null, null, null, new Node[0]);
    JSTypeRegistry scope = new JSTypeRegistry(registry);
    StaticScope<JSType> staticScope = new StaticScope<>(scope);

    JSType parametersType1 = createParametersWithVarArgs(registry, null,
        createParameter(registry, "a", INT), createParameter(registry, "b", INT));
    JSType returnType1 = INT;
    ArrowType arrowType1 = new ArrowType(registry, parametersType1, returnType1);

    JSType parametersType2 = createParametersWithVarArgs(registry, null,
        createParameter(registry, "a", INT), createParameter(registry, "b", INT));
    JSType returnType2 = INT;
    ArrowType arrowType2 = new ArrowType(registry, parametersType2, returnType2);

    JSType parametersType3 = createParametersWithVarArgs(registry, null,
        createParameter(registry, "a", INT), createParameter(registry, "b", INT));
    JSType returnType3 = STRING;
    ArrowType arrowType3 = new ArrowType(registry, parametersType3, returnType3);

    assertTrue(arrowType1.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType1.isSubtype(arrowType3));
    assertFalse(arrowType2.isSubtype(arrowType1));
    assertFalse(arrowType2.isSubtype(arrowType3));
    assertFalse(arrowType3.isSubtype(arrowType1));
    assertFalse(arrowType3.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeWithMissingParameter() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node functionNode = Node.newFunctionNode(
        null, null, null, null, null, null, null, null, null, new Node[0]);
    JSTypeRegistry scope = new JSTypeRegistry(registry);
    StaticScope<JSType> staticScope = new StaticScope<>(scope);

    JSType parametersType = createParametersWithVarArgs(registry, null,
        createParameter(registry, "a", INT), createParameter(registry, "b", INT));
    ArrowType arrowType = new ArrowType(registry, parametersType, INT);

    TestDrive td = new TestDrive();
    td.setErrorReporter(new ErrorReporter() {
      @Override
      public void error(String message) {
        fail(message);
      }

      @Override
      public void warning(String message) {
        fail(message);
      }
    });

    JSType resolvedArrowType = arrowType.resolveInternal(
        new ErrorReporter() {
          @Override
          public void error(String message) {
            fail(message);
          }

          @Override
          public void warning(String message) {
            fail(message);
          }
        }, staticScope);

    td.addTopLevelCode("var f = function(" + functionNode.toString() + ") {" +
        "  return function(a) {" +
        "    return a + b; // missing b parameter" +
        "  };" +
        "};");

    try {
      td.evaluate();
      fail("Expected an error");
    } catch (RuntimeException e) {
      // Expected error
    }

    JSTypeFunction functionType = (JSTypeFunction) td.getGlobalScope().get("f")
        .getType();
    JSType expectedErrorType = JSTypeNative.ERROR;

    assertTrue(expectedErrorType.isSubtype(functionType.getTypeAt(0).getType()));
  }

  @Test
  public void testArrowTypeWithVarArgsParameter() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node functionNode = Node.newFunctionNode(
        null, null, null, null, null, null, null, null, null, new Node[0]);
    JSTypeRegistry scope = new JSTypeRegistry(registry);
    StaticScope<JSType> staticScope = new StaticScope<>(scope);

    JSType parametersType = createParametersWithVarArgs(registry, null,
        createParameter(registry, "a", INT), createParameter(registry, "b", INT));
    ArrowType arrowType = new ArrowType(registry, parametersType, INT);

    TestDrive td = new TestDrive();
    td.setErrorReporter(new ErrorReporter() {
      @Override
      public void error(String message) {
        fail(message);
      }

      @Override
      public void warning(String message) {
        fail(message);
      }
    });

    JSType resolvedArrowType = arrowType.resolveInternal(
        new ErrorReporter() {
          @Override
          public void error(String message) {
            fail(message);
          }

          @Override
          public void warning(String message) {
            fail(message);
          }
        }, staticScope);

    td.addTopLevelCode("var f = function(" + functionNode.toString() + ") {" +
        "  return function(a, ...args) {" +
        "    return a + args[0];" +
        "  };" +
        "};");

    JSTypeFunction functionType = (JSTypeFunction) td.getGlobalScope().get("f")
        .getType();
    JSType expectedFunctionType = new ArrowType(registry, parametersType, INT);

    assertEquals(expectedFunctionType, resolvedArrowType);
  }

  @Test
  public void testArrowTypeWithOptionalParameter() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node functionNode = Node.newFunctionNode(
        null, null, null, null, null, null, null, null, null, new Node[0]);
    JSTypeRegistry scope = new JSTypeRegistry(registry);
    StaticScope<JSType> staticScope = new StaticScope<>(scope);

    JSType parametersType = createParametersWithVarArgs(registry, null,
        createOptionalParameter(registry, "a", INT), createParameter(registry, "b", INT));
    ArrowType arrowType = new ArrowType(registry, parametersType, INT);

    TestDrive td = new TestDrive();
    td.setErrorReporter(new ErrorReporter() {
      @Override
      public void error(String message) {
        fail(message);
      }

      @Override
      public void warning(String message) {
        fail(message);
      }
    });

    JSType resolvedArrowType = arrowType.resolveInternal(
        new ErrorReporter() {
          @Override
          public void error(String message) {
            fail(message);
          }

          @Override
          public void warning(String message) {
            fail(message);
          }
        }, staticScope);

    td.addTopLevelCode("var f = function(" + functionNode.toString() + ") {" +
        "  return function(a, b) {" +
        "    if (b) {" +
        "      return a + b;" +
        "    } else {" +
        "      return a;" +
        "    }" +
        "  };" +
        "};");

    JSTypeFunction functionType = (JSTypeFunction) td.getGlobalScope().get("f")
        .getType();
    JSType expectedFunctionType = new ArrowType(registry, parametersType, INT);

    assertEquals(expectedFunctionType, resolvedArrowType);
  }
}