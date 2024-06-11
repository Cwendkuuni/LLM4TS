import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;

public class LagoonGUITest {
    @org.junit.Before
    public void setUp() {
        LagoonGUI.main(new String[0]);
    }

    @org.junit.Test
    public void testLoadButton() {
        LagoonGUI.loadButton.doClick();
        assertTrue(LagoonGUI.this.loadPropertyFileWasCalled); // Assuming loadPropertyFile was called in loadButton actionPerformed
    }

    @org.junit.Test
    public void testSaveButton() {
        LagoonGUI.saveButton.doClick();
        assertTrue(LagoonGUI.this.savePropertyFileWasCalled); // Assuming savePropertyFile was called in saveButton actionPerformed
    }

    @org.junit.Test
    public void testBuildButtonWithValidInputs() {
        // Given
        LagoonGUI.sitemapFile.setValue("sitemap.xml");
        LagoonGUI.sourceDir.setValue("src");
        LagoonGUI.targetURL.setValue("http://example.com");

        // When
        LagoonGUI.buildButton.doClick();

        // Then
        assertTrue(LagoonGUI.this.buildWasCalled); // Assuming build was called in buildButton actionPerformed
    }

    // Add test cases for other methods and edge cases

    // ...
}