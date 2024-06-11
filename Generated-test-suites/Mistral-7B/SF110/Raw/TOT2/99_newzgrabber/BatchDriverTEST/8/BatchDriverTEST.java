import org.junit.Test;
import static org.junit.Assert.*;
import Newzgrabber.BatchDriver;

public class BatchDriverTest {

    @Test
    public void testGetGroups() {
        String newzini = "testconfig.ini";
        String batchfile = "testbatch.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        String[] expectedGroups = {"group1", "group2", "group3"};
        String[] actualGroups = batchDriver.getGroups();
        assertArrayEquals(expectedGroups, actualGroups);
    }

    // Add edge cases and exception handling tests here
}