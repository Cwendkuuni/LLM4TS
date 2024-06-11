import org.junit.Test;
import static org.junit.Assert.*;

public class ProfileTest {
    @Test
    public void testInitProfiler() {
        // Test normal execution
        Profile.initProfiler();
        // Test multiple calls
        Profile.initProfiler();
        // Test error handling - illegal state exception when profiling is already running
        Profile._controller = new Controller();
        Profile.initProfiler();
        assertThrows(IllegalStateException.class, () -> Profile.initProfiler());
    }
}