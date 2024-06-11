import static org.junit.Assert.*;
import static com.mentorgen.tools.profile.runtime.Profile.*;

public class ProfileTest {
    // ...

    @Test
    public void testInitProfiler() {
        initProfiler();
        assertTrue(Controller._profile);
        assertEquals(Runtime.getRuntime().addShutdownHook(new Thread(this)), Profile._controllerThread);
        assertNotNull(Profile._controller);
        assertNull(Profile._controllerThread.getState()); // Thread is not started yet
    }
}