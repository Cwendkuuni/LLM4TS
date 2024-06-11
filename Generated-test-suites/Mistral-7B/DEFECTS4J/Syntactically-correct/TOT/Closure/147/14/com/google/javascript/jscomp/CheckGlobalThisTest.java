import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;

import static com.google.common.truth.Truth.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {

  @Mock
  private AbstractCompiler compiler;
  @Mock
  private Checker checker;
  @Mock
  private CompilerOptions options;
  @Mock
  private Node node;
  @Mock
  private Node parent;
  @Mock
  private Node assignLhsChild;
  @Mock
  private NodeUtil nodeUtil;
  @Mock
  private DiagnosticType diagnosticType;

  @InjectMocks
  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    Mockito.when(checker.getCompiler()).thenReturn(compiler);
    Mockito.when(checker.getOptions()).thenReturn(options);
    Mockito.when(options.getCheckGlobalThis()).thenReturn(true);

    Mockito.when(compiler.getCheckers()).thenReturn(new Checker[0]);
    Mockito.when(compiler.report).then(invocation -> {});

    Mockito.when(node.getType()).thenReturn(Token.THIS);
    Mockito.when(parent.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(assignLhsChild.getType()).thenReturn(Token.ASSIGN);

    Mockito.when(nodeUtil.isGet(node)).thenReturn(false);
    Mockito.when(nodeUtil.isGet(parent)).thenReturn(false);
  }

  @Test
  public void testSafeUsageOfThisInsideFunction() {
    Mockito.when(node.getParent()).thenReturn(parent);

    checkGlobalThis.visit(new NodeTraversal(checker, null), node, parent);

    Mockito.verifyNoInteractions(compiler);
  }

  @Test
  public void testUnsafeUsageOfThisOnLeftSideOfAssignment() {
    Mockito.when(assignLhsChild.getType()).thenReturn(Token.NAME);
    Mockito.when(nodeUtil.isGet(parent)).thenReturn(false);

    checkGlobalThis.visit(new NodeTraversal(checker, null), node, parent);

    Mockito.verify(compiler).report(Mockito.eq(checker), Mockito.isA(Node.class), Mockito.eq(level), Mockito.eq(diagnosticType));
  }

  @Test
  public void testUnsafeUsageOfThisWithPropertyAccess() {
    Mockito.when(nodeUtil.isGet(parent)).thenReturn(true);

    checkGlobalThis.visit(new NodeTraversal(checker, null), node, parent);

    Mockito.verify(compiler).report(Mockito.eq(checker), Mockito.isA(Node.class), Mockito.eq(level), Mockito.eq(diagnosticType));
  }

  @Test
  public void testSafeUsageOfThisInsidePrototypeMethod() {
    Mockito.when(parent.getLastChild().getString()).thenReturn("prototype");

    checkGlobalThis.visit(new NodeTraversal(checker, null), node, parent);

    Mockito.verifyNoInteractions(compiler);
  }

  @Test
  public void testSafeUsageOfThisInsideFunctionAnnotatedWithConstructor() {
    JSDocInfo jsDoc = Mockito.mock(JSDocInfo.class);
    Mockito.when(jsDoc.isConstructor()).thenReturn(true);
    Mockito.when(node.getJSDocInfo()).thenReturn(jsDoc);

    checkGlobalThis.visit(new NodeTraversal(checker, null), node, parent);

    Mockito.verifyNoInteractions(compiler);
  }

  @Test
  public void testSafeUsageOfThisInsideFunctionAnnotatedWithThis() {
    JSDocInfo jsDoc = Mockito.mock(JSDocInfo.class);
    Mockito.when(jsDoc.hasThisType()).thenReturn(true);
    Mockito.when(node.getJSDocInfo()).thenReturn(jsDoc);

    checkGlobalThis.visit(new NodeTraversal(checker, null), node, parent);

    Mockito.verifyNoInteractions(compiler);
  }

  @Test
  public void testSafeUsageOfThisInFunctionWithNoLogicalPlaceForThisAnnotation() {
    Mockito.when(parent.getFirstChild()).thenReturn(node);

    checkGlobalThis.visit(new NodeTraversal(checker, null), node, parent);

    Mockito.verifyNoInteractions(compiler);
  }
}