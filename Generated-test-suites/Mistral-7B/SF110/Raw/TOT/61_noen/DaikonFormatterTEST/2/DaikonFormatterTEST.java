import org.junit.Assert;
import org.junit.Test;
import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatterInitialization() {
        String fileName = "test.dtrace";
        boolean simple = false;
        DaikonFormatter formatter = new DaikonFormatter(fileName, simple);
        Assert.assertEquals(fileName, formatter.getFileName());
        Assert.assertFalse(formatter.isSimple());
        Assert.assertNotNull(formatter.getDeclarations());
        Assert.assertNotNull(formatter.getInputs());
    }
}