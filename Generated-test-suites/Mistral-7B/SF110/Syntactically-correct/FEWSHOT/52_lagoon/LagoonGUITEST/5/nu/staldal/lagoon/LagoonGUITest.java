package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import nu.staldal.lagoon.*;
import nu.staldal.lagoon.core.*;
import nu.staldal.xtree.*;
import org.junit.Before;
import org.junit.Test;

public class LagoonGUITest {

    private LagoonGUI appFrame;
    private Properties props;

    @Before
    public void setUp() {
        appFrame = new LagoonGUI();
        props = new Properties();
    }

    @Test
    public void testInitialization() {
        assertNull(appFrame.getProcessor());
        assertEquals("Not initialized", appFrame.getStatusLabel().getText());
        assertFalse(appFrame.isInitialized());

        appFrame.init();
        assertNotNull(appFrame.getProcessor());
        assertEquals("Initializing...", appFrame.getStatusLabel().getText());
        assertTrue(appFrame.isInitialized());

        appFrame.setVisible(true);
        verify(appFrame, times(1)).addWindowListener(any(WindowListener.class));
    }

    @Test
    public void testLoadProperties() {
        when(appFrame.loadPropertyFile()).thenReturn(new File("test.properties"));
        appFrame.loadProperties(new File("test.properties"));
        assertEquals("test.properties", appFrame.getSitemapFile().getValue());
        assertEquals(".", appFrame.getSourceDir().getValue());
        assertEquals(".", appFrame.getTargetURL().getValue());
        assertNull(appFrame.getPassword());

        props.setProperty("sitemapFile", "sitemap.xml");
        props.setProperty("sourceDir", "/src");
        props.setProperty("targetURL", "http://test.com");
        props.setProperty("password", "testpassword");
        appFrame.loadProperties(new File("test.properties"));
        assertEquals("sitemap.xml", appFrame.getSitemapFile().getValue());
        assertEquals("/src", appFrame.getSourceDir().getValue());
        assertEquals("http://test.com", appFrame.getTargetURL().getValue());
        assertEquals("testpassword", appFrame.getPassword());
    }

    @Test
    public void testSaveProperties() {
        appFrame.savePropertyFile();
        assertNull(appFrame.getPassword());
        assertFalse(appFrame.isSavePropertyFileShown());

        appFrame.setSavePropertyFileShown(true);
        appFrame.savePropertyFile();
        assertTrue(appFrame.isSavePropertyFileShown());

        appFrame.setPassword("testpassword");
        appFrame.savePropertyFile();
        assertTrue(appFrame.isSavePropertyFileShown());
        assertTrue(appFrame.getPassword().equals("testpassword"));
    }

    @Test
    public void testBuild() throws IOException, SAXException {
        File sitemapFile = new File("sitemap.xml");
        when(sitemapFile.lastModified()).thenReturn(1L);
        when(appFrame.getProcessor()).thenReturn(mock(LagoonProcessor.class));

        appFrame.build(false);
        verify(appFrame.getProcessor(), never()).build(anyBoolean());
        assertEquals("Not initialized", appFrame.getStatusLabel().getText());

        appFrame.init();
        appFrame.build(false);
        verify(appFrame.getProcessor()).build(false);
        assertEquals("Building...", appFrame.getStatusLabel().getText());

        appFrame.build(true);
        verify(appFrame.getProcessor(), times(2)).build(anyBoolean());
        assertEquals("Building...", appFrame.getStatusLabel().getText());
    }

    @Test
    public void testExit() {
        appFrame.exit();
        assertTrue(appFrame.isDisposed());
    }
}