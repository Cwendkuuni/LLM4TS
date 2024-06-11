import static org.junit.Assert.assertNotNull;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThisConstructor() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    assertNotNull(checker);
  }
}