import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.VariableMap;

import org.junit.Test;

public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypes() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());

    CompilerPass renamer = new RenamePrototypes(compiler, true, null, null);

    CompilerInput input = new CompilerInput("test.js", "test.js", null);

    Node rootNode = new NodeToken(Token.ROOT, 1, 1, 0);

    ComplierPassManager manager = mock(CompilerPassManager.class);
    when(manager.addPass(renamer)).thenReturn(manager);

    renamer.process(null, rootNode);

    VariableMap map = renamer.getPropertyMap();
    assertNotNull(map);
  }
}

class CodingConvention {
  public boolean isExported(String name) {
    return false;
  }

  public boolean isPrivate(String name) {
    return false;
  }
}