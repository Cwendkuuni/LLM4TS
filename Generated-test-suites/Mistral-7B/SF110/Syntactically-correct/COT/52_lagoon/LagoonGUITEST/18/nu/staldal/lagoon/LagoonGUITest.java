package nu.staldal.lagoon; // Import necessary packages

import static org.junit.Assert.*; // Import assert methods
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito; // For mocking dependencies
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LagoonGUITest {

    // Create mock objects for dependencies
    private static final LagoonProcessor mockProcessor = Mockito.mock(LagoonProcessor.class);
    private static final TextAreaWriter mockProgressArea = Mockito.mock(TextAreaWriter.class);
    private static final InputComponent mockSitemapFile = Mockito.mock(InputComponent.class);
    private static final InputComponent mockSourceDir = Mockito.mock(InputComponent.class);
    private static final InputComponent mockTargetURL = Mockito.mock(InputComponent.class);

    // Create instance of LagoonGUI for testing
    private LagoonGUI gui;

    @Before
    public void setup() {
        this.gui = new LagoonGUI();
        Mockito.when(mockSitemapFile.hasChanged()).thenReturn(false); // Set return value for hasChanged() method
        Mockito.when(mockSourceDir.hasChanged()).thenReturn(false);
        Mockito.when(mockTargetURL.hasChanged()).thenReturn(false);
        this.gui.sitemapFile = mockSitemapFile;
        this.gui.sourceDir = mockSourceDir;
        this.gui.targetURL = mockTargetURL;
        this.gui.progressArea = mockProgressArea;
        this.gui.processor = mockProcessor;
    }

    @Test
    public void testConstructor() {
        // Check if LagoonGUI constructor is called correctly
        assertEquals("Lagoon", this.gui.getTitle());
    }

    @Test
    public void testLoadProperties() {
        // Mock file input stream and properties
        Mockito.doReturn(new FileInputStream(Mockito.mock(File.class))).when(mockSitemapFile).getValue();
        Mockito.doReturn(new Properties()).when(mockSitemapFile).load;

        // Call loadProperties() method
        this.gui.loadProperties(new File("test.properties"));

        // Check if properties are loaded correctly
        Mockito.verify(mockSitemapFile).setValue("test.properties.sitemapFile");
        Mockito.verify(mockSourceDir).setValue("test.properties.sourceDir");
        Mockito.verify(mockTargetURL).setValue("test.properties.targetURL");
    }

    @Test
    public void testBuildWithSuccess() {
        // Mock build() method to return true
        Mockito.when(mockProcessor.build(false)).thenReturn(true);

        // Call build() method
        this.gui.build(false);

        // Check if build() method is called with correct parameters and behaves correctly
        Mockito.verify(mockProcessor).build(false);
    }

    @Test(expected = AuthenticationMissingException.class)
    public void testBuildWithMissingPassword() {
        // Mock build() method to throw AuthenticationMissingException
        Mockito.when(mockProcessor.build(false)).thenThrow(new AuthenticationMissingException());

        // Call build() method
        this.gui.build(false);

        // Check if build() method is called with correct parameters and throws expected exception
        Mockito.verify(mockProcessor).build(false);
    }

    @Test(expected = AuthenticationException.class)
    public void testBuildWithInvalidPassword() {
        // Mock build() method to throw AuthenticationException
        Mockito.when(mockProcessor.build(false)).thenThrow(new AuthenticationException());

        // Call build() method
        this.gui.build(false);

        // Check if build() method is called with correct parameters and throws expected exception
        Mockito.verify(mockProcessor).build(false);
    }

    @Test(expected = FileNotFoundException.class)
    public void testBuildWithMissingSitemapFile() {
        // Mock build() method to throw FileNotFoundException
        Mockito.when(mockSitemapFile.getValue()).thenReturn(null);
        Mockito.when(mockProcessor.build(false)).thenThrow(new FileNotFoundException());

        // Call build() method
        this.gui.build(false);

        // Check if build() method is called with correct parameters and throws expected exception
        Mockito.verify(mockProcessor).build(false);
    }

    @Test(expected = IOException.class)
    public void testBuildWithIOException() {
        // Mock build() method to throw IOException
        Mockito.when(mockProcessor.build(false)).thenThrow(new IOException());

        // Call build() method
        this.gui.build(false);

        // Check if build() method is called with correct parameters and throws expected exception
        Mockito.verify(mockProcessor).build(false);
    }

    @Test(expected = LagoonException.class)
    public void testBuildWithLagoonException() {
        // Mock build() method to throw LagoonException
        Mockito.when(mockProcessor.build(false)).thenThrow(new LagoonException());

        // Call build() method
        this.gui.build(false);

        // Check if build() method is called with correct parameters and throws expected exception
        Mockito.verify(mockProcessor).build(false);
    }
}