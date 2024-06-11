import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThisConstructor() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, INFO);
    assertNotNull(checker);
  }
}