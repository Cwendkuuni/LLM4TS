import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatterConstructionWithFileName() throws IOException {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        Set<String> declarations = formatter.getDeclarations();
        Assert.assertNotNull(declarations);
        Assert.assertFalse(formatter.isSimple());
        Assert.assertNotNull(formatter.getInputs());

        DaikonFormatter simpleFormatter = new DaikonFormatter("test.dtrace", true);
        Assert.assertNull(simpleFormatter.getInputs());
    }

    @Test
    public void testDaikonFormatterConstructionWithInputs() throws IOException {
        String[] inputNames = {"input1", "input2", "input3"};
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", Arrays.asList(inputNames));
        Set<String> declarations = formatter.getDeclarations();
        Assert.assertNotNull(declarations);
        Assert.assertFalse(formatter.isSimple());
        Collection<String> inputs = formatter.getInputs();
        Assert.assertEquals(Arrays.asList(inputNames), inputs);
    }

    @Test
    public void testDaikonFormatterMethods() {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        String header = formatter.header();
        String footer = formatter.footer();
        String fileNameExtension = formatter.fileNameExtension();

        Assert.assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);
        Assert.assertEquals("", footer);
        Assert.assertEquals("dtrace", fileNameExtension);
    }
}