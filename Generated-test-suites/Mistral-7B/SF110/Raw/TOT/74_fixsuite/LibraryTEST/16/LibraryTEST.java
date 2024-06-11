import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LibraryTest {

    private Library library;
    private Logger logger;
    private File path;

    @Before
    public void setUp() {
        logger = Mockito.mock(Logger.class);
        library = new Library();
        path = Mockito.mock(File.class);
    }

    @Test
    public void testLoadFromDirectoryWithValidDirectory() throws IOException {
        when(path.isDirectory()).thenReturn(true);
        when(path.listFiles()).thenReturn(new File[]{new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), new File("MsgType.xml"), new File("MsgContents.xml")});

        library.loadFromDirectory(path.getAbsolutePath());

        OngoingStubbing<Logger> loggerStub = Mockito.doNothing().when(logger).debug(Mockito.anyString());
        OngoingStubbing<Logger> loggerErrorStub = Mockito.doNothing().when(logger).error(Mockito.anyString());

        library.getDictionaries().size();

        loggerStub.verify(logger, Mockito.times(3));
        loggerErrorStub.verify(logger, Mockito.never());
    }

    // Add other test cases for edge cases and error scenarios
}