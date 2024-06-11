import org.junit.Assert;
import org.junit.Test;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatterWithSimpleTrue() {
        // Given
        String fileName = "test.dtrace";
        boolean simple = true;

        // When
        DaikonFormatter formatter = new DaikonFormatter(fileName, simple);

        // Then
        Assert.assertNotNull(formatter);
        Assert.assertNull(formatter.getInputs());

        // Verify that the header, footer, and fileNameExtension are correct
        String expectedHeader = "decl-version 2.0\nvar-comparability implicit\n";
        String header = formatter.header();
        Assert.assertEquals(expectedHeader, header);

        String expectedFooter = "";
        String footer = formatter.footer();
        Assert.assertEquals(expectedFooter, footer);

        String expectedFileNameExtension = "dtrace";
        String fileNameExtension = formatter.fileNameExtension();
        Assert.assertEquals(expectedFileNameExtension, fileNameExtension);
    }
}