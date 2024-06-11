import static org.junit.Assert.assertNotNull;

public class CheckGlobalThisTest {

    @Test
    public void testConstructor() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        assertNotNull(checkGlobalThis);
    }
}