package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.xtree.TreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LagoonGUITest {

    @Mock
    private TextAreaWriter progressAreaMock;
    @Mock
    private MessageDialog messageDialogMock;
    @Mock
    private PasswordDialog passwordDialogMock;
    @Mock
    private YesNoQueryDialog yesNoQueryDialogMock;

    private LagoonGUI lagoonGUI;
    private File sitemapFile;
    private File sourceDir;
    private File targetURL;

    @Before
    public void setUp() {
        this.lagoonGUI = new LagoonGUI();
        this.sitemapFile = Mockito.mock(File.class);
        this.sourceDir = Mockito.mock(File.class);
        this.targetURL = Mockito.mock(File.class);
    }

    @Test
    public void testMainMethod() throws Exception {
        // Stubbing the mocked dependencies
        Mockito.doNothing().when(this.lagoonGUI).loadProperties(ArgumentMatchers.any(File.class));

        LagoonGUI.main(new String[]{"test.properties"});

        // Verification
        Mockito.verify(this.lagoonGUI).loadProperties(ArgumentMatchers.argThat(arg -> arg.equals(new File("test.properties"))));
    }

    @Test
    public void testConstructor() {
        // Test empty constructor
        this.lagoonGUI = new LagoonGUI();

        // Test constructor with arguments
        this.lagoonGUI = new LagoonGUI();
        Mockito.verify(this.lagoonGUI).addWindowListener(ArgumentMatchers.any(WindowListener.class));
        Mockito.verify(this.lagoonGUI).add(ArgumentMatchers.any(Panel.class), "North");
        Mockito.verify(this.lagoonGUI).add(ArgumentMatchers.any(Panel.class), "Center");
        Mockito.verify(this.lagoonGUI).add(ArgumentMatchers.any(Panel.class), "South");
    }

    @Test
    public void testWindowListenerMethods() {
        // Test all window listener methods (windowOpened, windowClosing, etc.) with no side effects
        this.lagoonGUI.windowOpened(Mockito.mock(WindowEvent.class));
        this.lagoonGUI.windowClosing(Mockito.mock(WindowEvent.class));
        this.lagoonGUI.windowClosed(Mockito.mock(WindowEvent.class));
        this.lagoonGUI.windowIconified(Mockito.mock(WindowEvent.class));
        this.lagoonGUI.windowDeiconified(Mockito.mock(WindowEvent.class));
        this.lagoonGUI.windowActivated(Mockito.mock(WindowEvent.class));
        this.lagoonGUI.windowDeactivated(Mockito.mock(WindowEvent.class));
    }

    @Test
    public void testBuildMethodWithNullProcessor() {
        Mockito.when(this.lagoonGUI.processor).thenReturn(null);

        this.lagoonGUI.build(false);

        Mockito.verify(this.lagoonGUI).init();
    }

    @Test
    public void testBuildMethodWithInitializationError() {
        Mockito.when(this.lagoonGUI.processor).thenReturn(Mockito.mock(LagoonProcessor.class));
        Mockito.doThrow(new IOException("Mock IO Exception")).when(this.lagoonGUI.processor).build(false);

        this.lagoonGUI.build(false);

        Mockito.verify(this.lagoonGUI.progressArea).append("--- Build finished with error(s) Mock IO Exception ---");
        Mockito.verify(this.messageDialogMock).show();
    }

    @Test
    public void testBuildMethodWithSuccess() {
        Mockito.when(this.lagoonGUI.processor).thenReturn(Mockito.mock(LagoonProcessor.class));
        Mockito.doReturn(true).when(this.lagoonGUI.processor).build(false);

        this.lagoonGUI.build(false);

        Mockito.verify(this.lagoonGUI.progressArea).append("--- Build finished successfully " + ArgumentMatchers.anyString() + " ---");
    }

    @Test
    public void testBuildMethodWithForceTrue() {
        Mockito.when(this.lagoonGUI.processor).thenReturn(Mockito.mock(LagoonProcessor.class));
        Mockito.doReturn(true).when(this.lagoonGUI.processor).build(true);

        this.lagoonGUI.build(true);

        Mockito.verify(this.lagoonGUI.progressArea).append("--- Build finished successfully " + ArgumentMatchers.anyString() + " ---");
    }

    @Test(expected = NullPointerException.class)
    public void testExitMethodWithNullProcessor() {
        Mockito.when(this.lagoonGUI.processor).thenReturn(null);

        this.lagoonGUI.exit();
    }

    @Test
    public void testInitMethodWithInvalidSitemapFile() {
        Mockito.when(this.lagoonGUI.processor).thenReturn(Mockito.mock(LagoonProcessor.class));
        Mockito.when(this.sitemapFile.exists()).thenReturn(false);

        this.lagoonGUI.init();

        Mockito.verify(this.messageDialogMock).show();
    }

    @Test
    public void testInitMethodWithSitemapFileChanged() {
        Mockito.when(this.lagoonGUI.processor).thenReturn(Mockito.mock(LagoonProcessor.class));
        Mockito.when(this.sitemapFile.lastModified()).thenReturn(System.currentTimeMillis() - 1000L);
        Mockito.when(this.sitemapFile.lastModified()).thenReturn(System.currentTimeMillis() + 1000L);

        this.lagoonGUI.init();

        Mockito.verify(this.lagoonGUI).init();
    }

    @Test
    public void testInitMethodWithSourceDirChanged() {
        Mockito.when(this.lagoonGUI.processor).thenReturn(Mockito.mock(LagoonProcessor.class));

        this.lagoonGUI.init();

        Mockito.verify(this.lagoonGUI).init();
    }

    @Test
    public void testInitMethodWithTargetURLChanged() {
        Mockito.when(this.lagoonGUI.processor).thenReturn(Mockito.mock(LagoonProcessor.class));

        this.lagoonGUI.init();

        Mockito.verify(this.lagoonGUI).init();
    }

    @Test
    public void testLoadPropertiesMethodWithNullPropertyFile() {
        this.lagoonGUI.loadProperties(null);

        // Verification
        Mockito.verifyNoInteractions(this.lagoonGUI.progressArea);
        Mockito.verifyNoInteractions(this.messageDialogMock);
    }

    @Test
    public void testLoadPropertiesMethodWithValidPropertyFile() {
        Mockito.when(this.lagoonGUI.progressArea).thenReturn(this.progressAreaMock);
        Mockito.when(this.messageDialogMock).thenReturn(this.messageDialogMock);

        final Properties prop = new Properties();
        prop.setProperty("sitemapFile", "testSitemapFile.xml");
        prop.setProperty("sourceDir", "testSourceDir");
        prop.setProperty("targetURL", "testTargetURL");
        prop.setProperty("password", "testPassword");

        this.lagoonGUI.loadProperties(new File("test.properties"));

        Mockito.verify(this.lagoonGUI.sitemapFile).setValue("testSitemapFile.xml");
        Mockito.verify(this.lagoonGUI.sourceDir).setValue("testSourceDir");
        Mockito.verify(this.lagoonGUI.targetURL).setValue("testTargetURL");
        Mockito.verify(this.lagoonGUI.password, Mockito.never()).set(ArgumentMatchers.any(String.class));

        Mockito.verify(this.lagoonGUI.progressArea).setText("--- Start build ---\n");
    }

    @Test
    public void testSavePropertyFileMethodWithNullPropertyFile() {
        Mockito.when(this.lagoonGUI.progressArea).thenReturn(this.progressAreaMock);
        Mockito.when(this.messageDialogMock).thenReturn(this.messageDialogMock);

        this.lagoonGUI.savePropertyFile();

        Mockito.verifyNoInteractions(this.lagoonGUI.progressArea);
        Mockito.verifyNoInteractions(this.messageDialogMock);
    }

    @Test
    public void testSavePropertyFileMethodWithValidPropertyFile() {
        Mockito.when(this.lagoonGUI.progressArea).thenReturn(this.progressAreaMock);
        Mockito.when(this.messageDialogMock).thenReturn(this.messageDialogMock);
        Mockito.when(this.yesNoQueryDialogMock).thenReturn(this.yesNoQueryDialogMock);

        final Properties prop = new Properties();
        prop.setProperty("sitemapFile", "testSitemapFile.xml");
        prop.setProperty("sourceDir", "testSourceDir");
        prop.setProperty("targetURL", "testTargetURL");
        prop.setProperty("password", "testPassword");

        this.lagoonGUI.savePropertyFile();

        Mockito.verify(this.lagoonGUI.progressArea).setText("--- Start build ---\n");
        Mockito.verify(this.messageDialogMock).show();
        Mockito.verify(this.yesNoQueryDialogMock).show();
    }

    @Test
    public void testLoadPropertyFileDialogMethodWithNullFileDialog() {
        this.lagoonGUI.loadPropertyFileDialog(null);

        // Verification
        Mockito.verifyNoInteractions(this.lagoonGUI);
    }

    @Test
    public void testLoadPropertyFileDialogMethodWithValidFileDialog() {
        Mockito.when(this.lagoonGUI.loadProperties(ArgumentMatchers.any(File.class))).thenReturn(true);

        this.lagoonGUI.loadPropertyFileDialog(Mockito.mock(FileDialog.class));

        Mockito.verify(this.lagoonGUI.loadProperties(ArgumentMatchers.argThat(arg -> arg.equals(Mockito.argumentCaptor(File.class).capture()))));
        Mockito.verify(this.lagoonGUI).show();
    }
}