import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        // Given
        String type = "png";
        String description = "Export image";
        String nameExtension = "export";
        ModeController controller = new StubModeController(); // Replace with real implementation

        // When
        File result = new ExportHook().chooseFile(type, description, nameExtension, controller);

        // Then
        if (result != null) {
            String expectedFileName = "map.export.png";
            String expectedFilePath = "target/test-classes/expectedFilePath/";
            File expectedFile = new File(expectedFilePath + expectedFileName);
            controller.setMapFile(expectedFile); // Set up the map file for the test
            assertEquals(expectedFile, result);
        } else {
            assertNull(result);
        }
    }
}