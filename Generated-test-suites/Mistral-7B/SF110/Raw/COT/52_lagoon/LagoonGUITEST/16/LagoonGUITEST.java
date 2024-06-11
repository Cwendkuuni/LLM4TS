package nu.staldal.lagoon; // package should match the class

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LagoonGUITest {

    // Initialize mocks and objects
    private LagoonGUI appFrame;
    private InputComponent mockSitemapFile;
    private InputComponent mockSourceDir;
    private InputComponent mockTargetURL;
    private Panel mockButtonPanel;
    private Panel mockInputPanel;
    private Panel mockCenterPanel;
    private TextArea mockProgressArea;
    private Label mockStatusLabel;
    private Button mockLoadButton;
    private Button mockSaveButton;
    private Button mockBuildButton;
    private Button mockForceButton;
    private Button mockExitButton;
    private Frame mockFrame;
    private WindowEvent mockWindowEvent;

    @Before
    public void setup() {
        mockSitemapFile = Mockito.mock(InputComponent.class);
        mockSourceDir = Mockito.mock(InputComponent.class);
        mockTargetURL = Mockito.mock(InputComponent.class);
        mockButtonPanel = Mockito.mock(Panel.class);
        mockInputPanel = Mockito.mock(Panel.class);
        mockCenterPanel = Mockito.mock(Panel.class);
        mockProgressArea = Mockito.mock(TextArea.class);
        mockStatusLabel = Mockito.mock(Label.class);
        mockLoadButton = Mockito.mock(Button.class);
        mockSaveButton = Mockito.mock(Button.class);
        mockBuildButton = Mockito.mock(Button.class);
        mockForceButton = Mockito.mock(Button.class);
        mockExitButton = Mockito.mock(Button.class);
        mockFrame = Mockito.mock(Frame.class);
        mockWindowEvent = Mockito.mock(WindowEvent.class);

        appFrame = new LagoonGUI() {
            @Override
            public InputComponent getSitemapFile() {
                return mockSitemapFile;
            }

            @Override
            public InputComponent getSourceDir() {
                return mockSourceDir;
            }

            @Override
            public InputComponent getTargetURL() {
                return mockTargetURL;
            }

            @Override
            public Panel getInputPanel() {
                return mockInputPanel;
            }

            @Override
            public Panel getButtonPanel() {
                return mockButtonPanel;
            }

            @Override
            public Panel getCenterPanel() {
                return mockCenterPanel;
            }

            @Override
            public TextArea getProgressArea() {
                return mockProgressArea;
            }

            @Override
            public Label getStatusLabel() {
                return mockStatusLabel;
            }

            @Override
            public Button getLoadButton() {
                return mockLoadButton;
            }

            @Override
            public Button getSaveButton() {
                return mockSaveButton;
            }

            @Override
            public Button getBuildButton() {
                return mockBuildButton;
            }

            @Override
            public Button getForceButton() {
                return mockForceButton;
            }

            @Override
            public Button getExitButton() {
                return mockExitButton;
            }

            @Override
            public Frame getFrame() {
                return mockFrame;
            }

            @Override
            public void addWindowListener(WindowListener listener) {
                mockFrame.addWindowListener(listener);
            }

            @Override
            public void show() {
                // Do nothing
            }
        };
    }

    @Test
    public void testConstructor() {
        assertNotNull(appFrame);
    }

    @Test
    public void testWindowListeners() {
        Mockito.verify(mockFrame).addWindowListener(appFrame);
    }

    @Test
    public void testLoadButtonActionPerformed() {
        Mockito.when(mockSitemapFile.getValue()).thenReturn("testfile.xml");
        appFrame.loadButton.actionPerformed(Mockito.mock(ActionEvent.class));
        Mockito.verify(appFrame).loadPropertyFile();
    }

    @Test
    public void testSaveButtonActionPerformed() {
        appFrame.saveButton.actionPerformed(Mockito.mock(ActionEvent.class));
        Mockito.verify(appFrame).savePropertyFile();
    }

    @Test
    public void testBuildButtonActionPerformed() {
        // Basic functionality test
        appFrame.buildButton.actionPerformed(Mockito.mock(ActionEvent.class));
        Mockito.verify(appFrame).build(false);

        // Edge cases
        Mockito.when(mockSitemapFile.hasChanged()).thenReturn(true);
        Mockito.when(mockSitemapFile.getValue()).thenReturn("testfile2.xml");
        appFrame.buildButton.actionPerformed(Mockito.mock(ActionEvent.class));
        Mockito.verify(appFrame).build(false);

        Mockito.when(new File(appFrame.sitemapFile.getValue()).lastModified()).thenReturn(System.currentTimeMillis() + 1000L);
        appFrame.buildButton.actionPerformed(Mockito.mock(ActionEvent.class));
        Mockito.verify(appFrame).build(false);

        Mockito.when(appFrame.processor != null).thenReturn(true);
        appFrame.buildButton.actionPerformed(Mockito.mock(ActionEvent.class));
        Mockito.verify(appFrame).build(false);

        // Exceptions
        Mockito.when(new File(appFrame.sitemapFile.getValue()).lastModified()).thenThrow(new FileNotFoundException());
        appFrame.buildButton.actionPerformed(Mockito.mock(ActionEvent.class));
        Mockito.verify(appFrame, Mockito.never()).init();
        Mockito.verify(appFrame).statusLabel.setText("Not initialized");

        Mockito.when(appFrame.processor != null).thenReturn(true);
        Mockito.when(appFrame.processor.build(Mockito.anyBoolean())).thenThrow(new IOException());
        appFrame.buildButton.actionPerformed(Mockito.mock(ActionEvent.class));
        Mockito.verify(appFrame).statusLabel.setText("Not initialized");
    }

    @Test
    public void testForceButtonActionPerformed() {
        // Basic functionality test
        appFrame.forceButton.actionPerformed(Mockito.mock(ActionEvent.class));
        Mockito.verify(appFrame).build(true);

        // Edge case
        Mockito.when(appFrame.processor != null).thenReturn(true);
        appFrame.forceButton.actionPerformed(Mockito.mock(ActionEvent.class));
        Mockito.verify(appFrame).build(true);

        // Exceptions
        Mockito.when(appFrame.processor != null).thenReturn(true);
        Mockito.when(appFrame.processor.build(Mockito.anyBoolean())).thenThrow(new FileNotFoundException());
        appFrame.forceButton.actionPerformed(Mockito.mock(ActionEvent.class));
        Mockito.verify(appFrame).statusLabel.setText("Not initialized");

        Mockito.when(appFrame.processor != null).thenReturn(true);
        Mockito.when(appFrame.processor.build(Mockito.anyBoolean())).thenThrow(new IOException());
        appFrame.forceButton.actionPerformed(Mockito.mock(ActionEvent.class));
        Mockito.verify(appFrame).statusLabel.setText("Not initialized");
    }

    @Test
    public void testExitButtonActionPerformed() {
        appFrame.exitButton.actionPerformed(Mockito.mock(ActionEvent.class));
        Mockito.verify(appFrame).exit();
    }
}