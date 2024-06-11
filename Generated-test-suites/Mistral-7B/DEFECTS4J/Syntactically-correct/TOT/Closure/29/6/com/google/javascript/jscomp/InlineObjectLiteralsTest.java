import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiteral() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    Node rootNode = new Node(Token.SCRIPT_START);

    // Test case for object literal assignment
    Node objLiteralAssignment = new Node(Token.VAR, "obj");
    objLiteralAssignment.addChild(new Node(Token.ASSIGN, null,
        new Node(Token.OBJECT_LITERAL, null,
            new Node(Token.STRING, "name"),
            new Node(Token.NUMBER, 1),
            new Node(Token.NUMBER, 2)
        )));
    objLiteralAssignment.addChild(new Node(Token.SEMICOLON));
    rootNode.addChild(objLiteralAssignment);

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tmp");
    pass.process(null, rootNode);
    Node expected = new Node(Token.VAR, "obj");
    expected.addChild(new Node(Token.ASSIGN, null,
        new Node(Token.VAR, "name_tmp_0"),
        new Node(Token.ASSIGN, null,
            new Node(Token.VAR, "tmp_1"),
            new Node(Token.NUMBER, 1)
        ),
        new Node(Token.ASSIGN, null,
            new Node(Token.VAR, "tmp_2"),
            new Node(Token.NUMBER, 2)
        ),
        new Node(Token.TRUE_OPT)
    ));
    expected.addChild(new Node(Token.SEMICOLON));
    Node actual = rootNode;
    while (actual.getFirstChild() != null) {
      actual = actual.getFirstChild();
    }
    assertEquals(expected, actual);
  }

  @Test
  public void testInlineObjectLiteralWithGetter() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    Node rootNode = new Node(Token.SCRIPT_START);

    // Test case for object literal assignment with getter
    Node objLiteralAssignment = new Node(Token.VAR, "obj");
    objLiteralAssignment.addChild(new Node(Token.ASSIGN, null,
        new Node(Token.OBJECT_LITERAL, null,
            new Node(Token.STRING, "getName"),
            new Node(Token.GET_PROP, null,
                new Node(Token.STRING, "name")
            )
        )));
    objLiteralAssignment.addChild(new Node(Token.SEMICOLON));
    rootNode.addChild(objLiteralAssignment);

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tmp");
    pass.process(null, rootNode);
    Node expected = new Node(Token.VAR, "obj");
    expected.addChild(new Node(Token.ASSIGN, null,
        new Node(Token.VAR, "getName_tmp_0"),
        new Node(Token.GET_PROP, null,
            new Node(Token.VAR, "name_tmp_1")
        )
    ));
    expected.addChild(new Node(Token.SEMICOLON));
    Node actual = rootNode;
    while (actual.getFirstChild() != null) {
      actual = actual.getFirstChild();
    }
    assertEquals(expected, actual);
  }

  @Test
  public void testInlineObjectLiteralWithNestedObject() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    Node rootNode = new Node(Token.SCRIPT_START);

    // Test case for object literal assignment with nested object
    Node objLiteralAssignment = new Node(Token.VAR, "obj");
    objLiteralAssignment.addChild(new Node(Token.ASSIGN, null,
        new Node(Token.OBJECT_LITERAL, null,
            new Node(Token.STRING, "name"),
            new Node(Token.OBJECT_LITERAL, null,
                new Node(Token.STRING, "innerName"),
                new Node(Token.NUMBER, 1)
            )
        )));
    objLiteralAssignment.addChild(new Node(Token.SEMICOLON));
    rootNode.addChild(objLiteralAssignment);

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tmp");
    pass.process(null, rootNode);
    Node expected = new Node(Token.VAR, "obj");
    expected.addChild(new Node(Token.ASSIGN, null,
        new Node(Token.VAR, "name_tmp_0"),
        new Node(Token.ASSIGN, null,
            new Node(Token.VAR, "innerName_tmp_1_0"),
            new Node(Token.NUMBER, 1)
        ),
        new Node(Token.TRUE_OPT)
    ));
    expected.addChild(new Node(Token.SEMICOLON));
    Node actual = rootNode;
    while (actual.getFirstChild() != null) {
      actual = actual.getFirstChild();
    }
    assertEquals(expected, actual);
  }

  @Test
  public void testInlineObjectLiteralWithSelfReference() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    Node rootNode = new Node(Token.SCRIPT_START);

    // Test case for object literal assignment with self-reference
    Node objLiteralAssignment = new Node(Token.VAR, "obj");
    objLiteralAssignment.addChild(new Node(Token.ASSIGN, null,
        new Node(Token.OBJECT_LITERAL, null,
            new Node(Token.STRING, "self"),
            new Node(Token.OBJECT_LITERAL, null,
                new Node(Token.STRING, "name"),
                new Node(Token.GET_PROP, null, objLiteralAssignment)
            )
        )));
    objLiteralAssignment.addChild(new Node(Token.SEMICOLON));
    rootNode.addChild(objLiteralAssignment);

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tmp");
    try {
      pass.process(null, rootNode);
      fail("Expected InlineObjectLiterals to fail with self-reference");
    } catch (RuntimeException e) {
      // Expected failure
    }
  }

  @Test
  public void testInlineObjectLiteralWithUnusedKey() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    Node rootNode = new Node(Token.SCRIPT_START);

    // Test case for object literal assignment with unused key
    Node objLiteralAssignment = new Node(Token.VAR, "obj");
    objLiteralAssignment.addChild(new Node(Token.ASSIGN, null,
        new Node(Token.OBJECT_LITERAL, null,
            new Node(Token.STRING, "name"),
            new Node(Token.STRING, "unused_key")
        )));
    objLiteralAssignment.addChild(new Node(Token.SEMICOLON));
    rootNode.addChild(objLiteralAssignment);

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tmp");
    pass.process(null, rootNode);
    Node expected = new Node(Token.VAR, "obj");
    expected.addChild(new Node(Token.ASSIGN, null,
        new Node(Token.VAR, "name_tmp_0"),
        new Node(Token.TRUE_OPT)
    ));
    expected.addChild(new Node(Token.SEMICOLON));
    Node actual = rootNode;
    while (actual.getFirstChild() != null) {
      actual = actual.getFirstChild();
    }
    assertEquals(expected, actual);
  }

  @Test
  public void testInlineObjectLiteralWithCycle() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    Node rootNode = new Node(Token.SCRIPT_START);

    // Test case for object literal assignment with cycle
    Node objLiteralAssignment = new Node(Token.VAR, "obj1");
    objLiteralAssignment.addChild(new Node(Token.ASSIGN, null,
        new Node(Token.OBJECT_LITERAL, null,
            new Node(Token.STRING, "name1"),
            new Node(Token.OBJECT_LITERAL, null,
                new Node(Token.STRING, "name2"),
                new Node(Token.VAR, "obj2")
            )
        )));
    objLiteralAssignment.addChild(new Node(Token.SEMICOLON));

    Node objLiteralAssignment2 = new Node(Token.VAR, "obj2");
    objLiteralAssignment2.addChild(new Node(Token.ASSIGN, null,
        new Node(Token.OBJECT_LITERAL, null,
            new Node(Token.STRING, "name2"),
            new Node(Token.OBJECT_LITERAL, null,
                new Node(Token.STRING, "name1"),
                new Node(Token.VAR, "obj1")
            )
        )));
    objLiteralAssignment2.addChild(new Node(Token.SEMICOLON));
    rootNode.addChild(objLiteralAssignment);
    rootNode.addChild(objLiteralAssignment2);

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tmp");
    try {
      pass.process(null, rootNode);
      fail("Expected InlineObjectLiterals to fail with cycle");
    } catch (RuntimeException e) {
      // Expected failure
    }
  }

  @Test
  public void testInlineObjectLiteralWithFunctionProperty() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    Node rootNode = new Node(Token.SCRIPT_START);

    // Test case for object literal assignment with function property
    Node objLiteralAssignment = new Node(Token.VAR, "obj");
    objLiteralAssignment.addChild(new Node(Token.ASSIGN, null,
        new Node(Token.OBJECT_LITERAL, null,
            new Node(Token.STRING, "name"),
            new Node(Token.FUNCTION, null, null,
                new Node(Token.IDENTIFIER, "func"),
                new Node(Token.FUNCTION_BODY, null, null)
             )
        )));
    objLiteralAssignment.addChild(new Node(Token.SEMICOLON));
    rootNode.addChild(objLiteralAssignment);

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tmp");
    try {
      pass.process(null, rootNode);
      fail("Expected InlineObjectLiterals to fail with function property");
    } catch (RuntimeException e) {
      // Expected failure
    }
  }

  @Test
  public void testInlineObjectLiteralWithMissingKey() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    Node rootNode = new Node(Token.SCRIPT_START);

    // Test case for object literal assignment with missing key
    Node objLiteralAssignment = new Node(Token.VAR, "obj");
    objLiteralAssignment.addChild(new Node(Token.ASSIGN, null,
        new Node(Token.OBJECT_LITERAL, null,
            new Node(Token.STRING, "name")
        )));
    objLiteralAssignment.addChild(new Node(Token.SEMICOLON));
    rootNode.addChild(objLiteralAssignment);

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tmp");
    Node expected = new Node(Token.VAR, "obj");
    expected.addChild(new Node(Token.ASSIGN, null,
        new Node(Token.VAR, "name_tmp_0"),
        new Node(Token.TRUE_OPT)
    ));
    expected.addChild(new Node(Token.SEMICOLON));
    Node actual = rootNode;
    while (actual.getFirstChild() != null) {
      actual = actual.getFirstChild();
    }
    assertEquals(expected, actual);
  }

  @Test
  public void testInlineObjectLiteralWithArrayProperty() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    Node rootNode = new Node(Token.SCRIPT_START);

    // Test case for object literal assignment with array property
    Node objLiteralAssignment = new Node(Token.VAR, "obj");
    objLiteralAssignment.addChild(new Node(Token.ASSIGN, null,
        new Node(Token.OBJECT_LITERAL, null,
            new Node(Token.STRING, "name"),
            new Node(Token.ARRAY_LITERAL, null,
                new Node(Token.NUMBER, 0),
                new Node(Token.NUMBER, 1)
             )
        )));
    objLiteralAssignment.addChild(new Node(Token.SEMICOLON));
    rootNode.addChild(objLiteralAssignment);

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tmp");
    try {
      pass.process(null, rootNode);
      fail("Expected InlineObjectLiterals to fail with array property");
    } catch (RuntimeException e) {
      // Expected failure
    }
  }

  @Test
  public void testInlineObjectLiteralWithDefaultValue() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    Node rootNode = new Node(Token.SCRIPT_START);

    // Test case for object literal assignment with default value
    Node objLiteralAssignment = new Node(Token.VAR, "obj");
    objLiteralAssignment.addChild(new Node(Token.ASSIGN, null,
        new Node(Token.OBJECT_LITERAL, null,
            new Node(Token.STRING, "name"),
            new Node(Token.OBJECT_LITERAL, null,
                new Node(Token.STRING, "default"),
                new Node(Token.FALSE_OPT)
             )
        )));
    objLiteralAssignment.addChild(new Node(Token.SEMICOLON));
    rootNode.addChild(objLiteralAssignment);

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tmp");
    Node expected = new Node(Token.VAR, "obj");
    expected.addChild(new Node(Token.ASSIGN, null,
        new Node(Token.VAR, "name_tmp_0"),
        new Node(Token.ASSIGN, null,
            new Node(Token.VAR, "default_tmp_1"),
            new Node(Token.FALSE_OPT)
        ),
        new Node(Token.TRUE_OPT)
    ));
    expected.addChild(new Node(Token.SEMICOLON));
    Node actual = rootNode;
    while (actual.getFirstChild() != null) {
      actual = actual.getFirstChild();
    }
    assertEquals(expected, actual);
  }
}