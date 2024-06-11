import org.junit.Test;
import static org.junit.Assert.*;
import static com.mentorgen.tools.profile.runtime.Profile.*;

public class ProfileTest {

    @Test
    public void testInitProfiler() {
        initProfiler();
        assertTrue(Controller._profile);
    }
}