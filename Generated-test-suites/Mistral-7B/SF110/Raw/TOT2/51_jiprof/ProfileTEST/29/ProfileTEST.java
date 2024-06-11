import static org.junit.Assert.*;
import com.mentorgen.tools.profile.runtime.Profile;

public class ProfileTest {
    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertTrue(Profile._controller != null);
        assertFalse(Profile._controllerThread.isAlive());
    }
}