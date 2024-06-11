package nu.staldal.lagoon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import nu.staldal.lagoon.core.*;
import nu.staldal.lagoon.core.TextAreaWriter;
import nu.staldal.xtree.Element;
import org.junit.*;
import org.junit.Test;
import org.mockito.*;

public class LagoonGUITest {

    private LagoonGUI appFrame;
    private TextAreaWriter progressAreaMock;
    private LagoonProcessor processorMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.appFrame = new LagoonGUI();
        this.progressAreaMock = mock(TextAreaWriter.class);
        when(this.appFrame.new LagoonProcessor(anyString(), any(Element.class), any(File.class), anyString(), this.progressAreaMock, this.progressAreaMock)).thenReturn(this.processorMock);
    }

    @Test
    public void testBuild_CallsInitWhenSitemapFileOrSourceDirHasChanged() {
        final File sitemapFile = new File("sitemap.xml");
        when(this.appFrame.sitemapFile.hasChanged()).thenReturn(true);
        appFrame.init();
        verify(this.appFrame).init();
    }

    // More test cases here

}