package nu.staldal.lagoon;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

@RunWith(MockitoJUnitRunner.class)
public class LagoonGUITest {

    @InjectMocks
    private LagoonGUI lagoonGUI;

    @Test
    public void testInit() {
        lagoonGUI.init();
        Mockito.verify(lagoonGUI).statusLabel.setText("Initializing...");
        Mockito.verify(lagoonGUI).processor = null;
    }

    @Test
    public void testInitWithSitemapFileChanged() {
        lagoonGUI.init();
        final File sitemapFile = Mockito.mock(File.class);
        Mockito.when(sitemapFile.lastModified()).thenReturn(System.currentTimeMillis() + 1000L);
        lagoonGUI.sitemapFile = new InputComponent(lagoonGUI, "Sitemap file", sitemapFile.getAbsolutePath(), true);
        lagoonGUI.init();
        Mockito.verify(lagoonGUI).statusLabel.setText("Initializing...");
        Mockito.verify(lagoonGUI).processor = null;
        Mockito.verify(lagoonGUI).processor = Mockito.mock(LagoonProcessor.class);
    }

    @Test
    public void testInitWithSourceDirChanged() {
        lagoonGUI.init();
        final File sourceDir = Mockito.mock(File.class);
        lagoonGUI.sourceDir = new InputComponent(lagoonGUI, "Source directory", sourceDir.getAbsolutePath(), true);
        lagoonGUI.init();
        Mockito.verify(lagoonGUI).statusLabel.setText("Initializing...");
        Mockito.verify(lagoonGUI).processor = null;
    }

    @Test
    public void testInitWithTargetURLChanged() {
        lagoonGUI.init();
        final File targetURL = Mockito.mock(File.class);
        lagoonGUI.targetURL = new InputComponent(lagoonGUI, "Target URL", targetURL.getAbsolutePath(), true);
        lagoonGUI.init();
        Mockito.verify(lagoonGUI).statusLabel.setText("Initializing...");
        Mockito.verify(lagoonGUI).processor = null;
    }

    @Test
    public void testBuild() throws IOException {
        lagoonGUI.init();
        final boolean force = false;
        lagoonGUI.build(force);
        Mockito.verify(lagoonGUI).statusLabel.setText("Building...");
        Mockito.verify(lagoonGUI.processor).build(force);
        Mockito.verify(lagoonGUI).statusLabel.setText("Ready");
    }

    @Test
    public void testBuildWithForce() throws IOException {
        lagoonGUI.init();
        final boolean force = true;
        lagoonGUI.build(force);
        Mockito.verify(lagoonGUI).statusLabel.setText("Building...");
        Mockito.verify(lagoonGUI.processor).build(force);
        Mockito.verify(lagoonGUI).statusLabel.setText("Ready");
    }

    @Test
    public void testExit() {
        lagoonGUI = new LagoonGUI();
        lagoonGUI.exit();
        Assert.assertEquals(0, System.exit(0));
    }

    @Test
    public void testLoadPropertyFile() {
        final File propertyFile = Mockito.mock(File.class);
        Mockito.when(propertyFile.exists()).thenReturn(true);
        lagoonGUI.loadPropertyFile();
        Mockito.verify(lagoonGUI).statusLabel.setText("Loading...");
        Mockito.verify(lagoonGUI).loadProperties(propertyFile);
    }

    @Test
    public void testLoadProperties() throws IOException, LagoonException {
        final Properties prop = new Properties();
        prop.setProperty("sitemapFile", "testSitemapFile.xml");
        prop.setProperty("sourceDir", "testSourceDir");
        prop.setProperty("targetURL", "testTargetURL");
        lagoonGUI.loadProperties(new File("testPropertyFile.xml"));
        Mockito.verify(lagoonGUI.sitemapFile).setValue("testSitemapFile.xml");
        Mockito.verify(lagoonGUI.sourceDir).setValue("testSourceDir");
        Mockito.verify(lagoonGUI.targetURL).setValue("testTargetURL");
    }

    @Test
    public void testSavePropertyFile() throws IOException {
        final File propertyFile = Mockito.mock(File.class);
        Mockito.when(propertyFile.exists()).thenReturn(true);
        lagoonGUI.savePropertyFile();
        Mockito.verify(lagoonGUI.statusLabel).setText("Saving...");
        Mockito.verify(lagoonGUI).saveProperties(propertyFile);
    }

    @Test
    public void testSaveProperties() throws IOException {
        final Properties prop = new Properties();
        prop.setProperty("sitemapFile", "testSitemapFile.xml");
        prop.setProperty("sourceDir", "testSourceDir");
        prop.setProperty("targetURL", "testTargetURL");
        lagoonGUI.saveProperties(new File("testPropertyFile.xml"));
        Mockito.verify(lagoonGUI.sitemapFile).getValue().equals("testSitemapFile.xml");
        Mockito.verify(lagoonGUI.sourceDir).getValue().equals("testSourceDir");
        Mockito.verify(lagoonGUI.targetURL).getValue().equals("testTargetURL");
        Mockito.verify(lagoonGUI.password != null, Mockito.times(2));
    }
}