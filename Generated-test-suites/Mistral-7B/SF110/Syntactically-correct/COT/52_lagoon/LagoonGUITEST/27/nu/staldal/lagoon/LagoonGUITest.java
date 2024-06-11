package nu.staldal.lagoon; // adjust the package name if needed

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.io.File;

public class LagoonGUITest {
    // create instance of LagoonGUI for testing
    private LagoonGUI appFrame;

    @Before
    public void setUp() {
        appFrame = new LagoonGUI();
    }

    

    // Test constructor
    @Test
    public void testLagoonGUIConstructor() {
        // check if the frame is initialized correctly
        assertEquals("Lagoon", appFrame.getTitle());
        assertNotNull(appFrame.getInputPanel());
        assertNotNull(appFrame.getCenterPanel());
        assertNotNull(appFrame.getButtonPanel());
        assertNotNull(appFrame.getLoadButton());
        assertNotNull(appFrame.getSaveButton());
        assertNotNull(appFrame.getBuildButton());
        assertNotNull(appFrame.getForceButton());
        assertNotNull(appFrame.getExitButton());
        assertNotNull(appFrame.getSitemapFile());
        assertNotNull(appFrame.getSourceDir());
        assertNotNull(appFrame.getTargetURL());
        assertNull(appFrame.getProcessor());
        assertNull(appFrame.getStatusLabel());
        assertNotNull(appFrame.getProgressArea());
    }

    // Test loadPropertyFile method
    @Test
    public void testLoadPropertyFile() {
        // create a test file for loading properties
        File propertyFile = new File("test.properties");
        try {
            // write some test data to the file
            FileWriter fileWriter = new FileWriter(propertyFile);
            fileWriter.write("sitemapFile=test.xml\nsourceDir=testDir\ntargetURL=http://test.com\npassword=testPassword");
            fileWriter.close();

            // simulate user action to load the property file
            ActionEvent loadEvent = new ActionEvent(appFrame.getLoadButton(), 0, "actionPerformed");
            appFrame.loadButton.dispatchEvent(loadEvent);

            // check if the properties have been loaded correctly
            assertEquals("test.xml", appFrame.getSitemapFile().getValue());
            assertEquals("testDir", appFrame.getSourceDir().getValue());
            assertEquals("http://test.com", appFrame.getTargetURL().getValue());
            assertNull(appFrame.getPassword());

            // delete the test file
            propertyFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Test savePropertyFile method
    @Test
    public void testSavePropertyFile() {
        // simulate user action to save properties
        ActionEvent saveEvent = new ActionEvent(appFrame.getSaveButton(), 0, "actionPerformed");
        appFrame.saveButton.dispatchEvent(saveEvent);

        // create a test file for checking if the properties have been saved correctly
        File propertyFile = new File("test.properties");
        try {
            // read the contents of the file
            FileReader fileReader = new FileReader(propertyFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
            fileReader.close();

            // check if the properties have been saved correctly
            String savedProperties = sb.toString();
            assertTrue(savedProperties.contains("sitemapFile="));
            assertTrue(savedProperties.contains("sourceDir="));
            assertTrue(savedProperties.contains("targetURL="));
            assertTrue(savedProperties.contains("password="));

            // delete the test file
            propertyFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Test init method
    @Test
    public void testInit() {
        // simulate user action to initialize the application
        appFrame.buildButton.doClick();

        // check if the initialization was successful
        assertNotNull(appFrame.getProcessor());
        assertNotNull(appFrame.getStatusLabel());
    }

    // Test build method with no force flag
    @Test
    public void testBuildWithoutForce() {
        // simulate user action to initialize the application
        appFrame.buildButton.doClick();

        // check if the build process was started
        assertTrue(appFrame.getBuildButton().isEnabled() == false);

        // check if the build process was finished successfully
        assertTrue(appFrame.getStatusLabel().getText().contains("Building..."));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(appFrame.getStatusLabel().getText().contains("Build finished successfully"));

        // check if the build button is enabled again
        assertTrue(appFrame.getBuildButton().isEnabled() == true);
    }

    // Test build method with force flag
    @Test
    public void testBuildWithForce() {
        // simulate user action to initialize the application
        appFrame.forceButton.doClick();

        // check if the build process was started
        assertTrue(appFrame.getBuildButton().isEnabled() == false);

        // check if the build process was finished successfully
        assertTrue(appFrame.getStatusLabel().getText().contains("Building..."));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(appFrame.getStatusLabel().getText().contains("Build finished successfully"));

        // check if the build button is enabled again
        assertTrue(appFrame.getBuildButton().isEnabled() == true);

        // simulate user action to initialize the application again
        appFrame.buildButton.doClick();

        // check if the build process was skipped because the sitemap file has not changed
        assertTrue(appFrame.getStatusLabel().getText().contains("Not initialized"));
    }

    // Test exit method
    @Test
    public void testExit() {
        // simulate user action to exit the application
        appFrame.exitButton.doClick();

        // check if the application has been closed
        assertFalse(appFrame.isDisplayable());
    }

    
}