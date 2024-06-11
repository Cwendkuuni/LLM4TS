import static org.junit.Assert.assertNotNull;

public class CheckGlobalThisTest {

  @Test
  public void testConstructor() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    assertNotNull(checkGlobalThis);
  }
}