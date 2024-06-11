import static org.junit.Assert.*;
import com.mentorgen.tools.profile.runtime.Profile;

public class ProfileTest {
    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertNotNull(Profile._controller);
        assertTrue(Profile._controllerThread != null && Profile._controllerThread.isAlive());
    }
}