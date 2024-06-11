import static org.junit.Assert.*;
import com.mentorgen.tools.profile.runtime.*;

public class ProfileTest {
    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertTrue(Profile._threadDictionary instanceof ThreadDictionary);
        assertTrue(Profile._frameList instanceof ArrayList);
        assertTrue(Profile._methodDictionary instanceof HashMap);
        assertTrue(Profile._allocList instanceof HashMap);
    }
}