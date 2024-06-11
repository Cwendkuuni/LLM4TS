package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;

public class LagoonGUITest {

    private LagoonGUI appFrame;

    @Test
    public void testLagoonGUIConstruction() {
        appFrame = new LagoonGUI();
        assertNotNull(appFrame);
        assertEquals("Lagoon", appFrame.getTitle());
    }

    @Test
    public void testLoadButtonActionPerformed() {
        appFrame = new LagoonGUI();
        appFrame.loadButton.doClick();
        assertTrue(appFrame.loadPropertyFileCalled);
    }

    @Test
    public void testSaveButtonActionPerformed() {
        appFrame = new LagoonGUI();
        appFrame.saveButton.doClick();
        assertTrue(appFrame.savePropertyFileCalled);
    }

    @Test
    public void testBuildButtonActionPerformed() {
        appFrame = new LagoonGUI();
        appFrame.buildButton.doClick();
        assertTrue(appFrame.buildCalled);
    }

    @Test
    public void testForceButtonActionPerformed() {
        appFrame = new LagoonGUI();
        appFrame.forceButton.doClick();
        assertTrue(appFrame.buildCalledWithForce);
    }

    @Test
    public void testExitButtonActionPerformed() {
        appFrame = new LagoonGUI();
        appFrame.exitButton.doClick();
        assertEquals(0, appFrame.exitCode);
    }

    @Test
    public void testBuildSuccess() {
        appFrame = new LagoonGUI();
        appFrame.initSpy();
        appFrame.loadButton.doClick();
        appFrame.sitemapFile.setValue("sitemap.xml");
        appFrame.sourceDir.setValue("src");
        appFrame.targetURL.setValue("http://example.com");
        appFrame.buildButton.doClick();
        assertTrue(appFrame.buildSuccess);
    }

    @Test
    public void testBuildFailure() {
        appFrame = new LagoonGUI();
        appFrame.initSpy();
        appFrame.loadButton.doClick();
        appFrame.sitemapFile.setValue("sitemap.xml");
        appFrame.sourceDir.setValue("src");
        appFrame.targetURL.setValue("http://nonexistentserver.com");
        appFrame.buildButton.doClick();
        assertFalse(appFrame.buildSuccess);
    }

    @Test
    public void testSitemapFileChanged() {
        appFrame = new LagoonGUI();
        appFrame.initSpy();
        appFrame.loadButton.doClick();
        appFrame.sitemapFile.setValue("sitemap1.xml");
        appFrame.buildButton.doClick();
        assertTrue(appFrame.sitemapFileChanged);

        appFrame.sitemapFile.setValue("sitemap1.xml");
        appFrame.buildButton.doClick();
        assertFalse(appFrame.sitemapFileChanged);
    }

    @Test
    public void testSourceDirChanged() {
        appFrame = new LagoonGUI();
        appFrame.initSpy();
        appFrame.loadButton.doClick();
        appFrame.sitemapFile.setValue("sitemap.xml");
        appFrame.sourceDir.setValue("src1");
        appFrame.buildButton.doClick();
        assertTrue(appFrame.sourceDirChanged);

        appFrame.sourceDir.setValue("src1");
        appFrame.buildButton.doClick();
        assertFalse(appFrame.sourceDirChanged);
    }

    @Test
    public void testTargetURLChanged() {
        appFrame = new LagoonGUI();
        appFrame.initSpy();
        appFrame.loadButton.doClick();
        appFrame.sitemapFile.setValue("sitemap.xml");
        appFrame.targetURL.setValue("http://example.com1");
        appFrame.buildButton.doClick();
        assertTrue(appFrame.targetURLChanged);

        appFrame.targetURL.setValue("http://example.com1");
        appFrame.buildButton.doClick();
        assertFalse(appFrame.targetURLChanged);
    }

    @Test(expected = LagoonException.class)
    public void testInitFailure() {
        appFrame = new LagoonGUI();
        appFrame.initSpy();
        appFrame.loadButton.doClick();
        appFrame.sitemapFile.setValue("nonexistentfile.xml");
        appFrame.buildButton.doClick();
    }

    private void initMocks(LagoonGUI appFrame) {
        appFrame.loadPropertyFileCalled = false;
        appFrame.savePropertyFileCalled = false;
        appFrame.buildCalled = false;
        appFrame.buildCalledWithForce = false;
        appFrame.exitCode = 0;
        appFrame.buildSuccess = true;
        appFrame.sitemapFileChanged = false;
        appFrame.sourceDirChanged = false;
        appFrame.targetURLChanged = false;
        appFrame.loadButton.addActionListener(e -> appFrame.loadPropertyFileCalled = true);
        appFrame.saveButton.addActionListener(e -> appFrame.savePropertyFileCalled = true);
        appFrame.buildButton.addActionListener(e -> {
            appFrame.buildCalled = true;
            appFrame.buildSuccess = true;
        });
        appFrame.forceButton.addActionListener(e -> {
            appFrame.buildCalled = true;
            appFrame.buildCalledWithForce = true;
        });
        appFrame.exitButton.addActionListener(e -> appFrame.exitCode = 1);
    }

    private void initSpy(LagoonGUI appFrame) {
        appFrame.init();
        initMocks(appFrame);
    }

}