import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.check.Check;
import com.google.javascript.jscomp.parser.ast.ASTUtil;
import com.google.javascript.jscomp.parser.ast.ExpressionStatement;
import com.google.javascript.jscomp.parser.ast.FunctionDeclaration;
import com.google.javascript.jscomp.parser.ast.Node;
import com.google.javascript.jscomp.parser.ast.SourceFile;
import com.google.javascript.jscomp.parser.parser.Parser;

import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    // given
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    String code = "function foo() {" + System.lineSeparator() + "  alert('bar');"
        + System.lineSeparator() + "}";
    SourceFile sourceFile = Parser.parseJavaScript(code, null).get(0);
    FunctionDeclaration functionDeclaration = (FunctionDeclaration) ASTUtil
        .findTopLevelDeclarations(sourceFile, FunctionDeclaration.class).get(0);
    Node node = functionDeclaration.getBody();
    JSError error = new JSError("Expected ';', but '(' found.", null, 1, 10, node);

    when(source.getExcerpt(Mockito.any(String.class), Mockito.anyInt(), Mockito.anyInt(), Mockito.eq(LINE)))
        .thenReturn("function foo() {" + System.lineSeparator() + "  alert('bar');"
            + System.lineSeparator() + "}");

    // when
    String message = formatter.formatError(error);

    // then
    String expected = "1:10: ERROR - Unexpected '(', but ';' expected\n"
        + "function foo() {" + System.lineSeparator() + "  alert('bar');"
        + System.lineSeparator() + "}\n";
    assertEquals(expected, message);
  }

  @Test
  public void testFormatWarning() {
    // given
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);

    String code = "function foo() {" + System.lineSeparator() + "  alert('bar');"
        + System.lineSeparator() + "}";
    SourceFile sourceFile = Parser.parseJavaScript(code, null).get(0);
    FunctionDeclaration functionDeclaration = (FunctionDeclaration) ASTUtil
        .findTopLevelDeclarations(sourceFile, FunctionDeclaration.class).get(0);
    Node node = functionDeclaration.getBody();
    JSError warning = new JSError("Use strict directive is not supported.", null, 1, 1, node);

    when(source.getExcerpt(Mockito.any(String.class), Mockito.anyInt(), Mockito.anyInt(), Mockito.eq(LINE)))
        .thenReturn("function foo() {" + System.lineSeparator() + "  alert('bar');"
            + System.lineSeparator() + "}");

    // when
    String message = formatter.formatWarning(warning);

    // then
    String expected = "1:1: WARNING - Use strict directive is not supported\n"
        + "function foo() {" + System.lineSeparator() + "  alert('bar');"
        + System.lineSeparator() + "}\n";
    assertEquals(expected, message);
  }

  @Test
  public void testFormatWithoutSource() {
    // given
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();

    String code = "function foo() {" + System.lineSeparator() + "  alert('bar');"
        + System.lineSeparator() + "}";
    SourceFile sourceFile = Parser.parseJavaScript(code, null).get(0);
    FunctionDeclaration functionDeclaration = (FunctionDeclaration) ASTUtil
        .findTopLevelDeclarations(sourceFile, FunctionDeclaration.class).get(0);
    Node node = functionDeclaration.getBody();
    JSError error = new JSError("Expected ';', but '(' found.", null, 1, 10, node);

    // when
    String message = formatter.formatError(error);

    // then
    String expected = "Unexpected '(', but ';' expected\n"
        + "function foo() {" + System.lineSeparator() + "  alert('bar');"
        + System.lineSeparator() + "}\n";
    assertEquals(expected, message);
  }
}