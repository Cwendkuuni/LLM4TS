import static org.junit.Assert.*;
import static com.mentorgen.tools.profile.runtime.Profile.*;

public class ProfileTest {

    @BeforeClass
    public static void setupClass() {
        // Initialize class-level variables
        initProfiler();
    }

    @Test
    public void testInitProfiler() {
        assertTrue("Java Interactive Profiler: starting", isPrintedToErrStream());
        assertNotNull(Profile._controller);
        assertNotNull(Profile._controllerThread);
        assertEquals(1, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._allocList.size());
    }

    // Helper method to check if a specific message is printed to the err stream
    private boolean isPrintedToErrStream() {
        List<String> errLines = new ArrayList<>();
        StringSystemStream errStream = new StringSystemStream(System.err, errLines);
        PrintStream originalErr = System.err;
        try {
            System.setErr(errStream);
            initProfiler();
            return errLines.contains("Java Interactive Profiler: starting");
        } finally {
            System.setErr(originalErr);
        }
    }
}