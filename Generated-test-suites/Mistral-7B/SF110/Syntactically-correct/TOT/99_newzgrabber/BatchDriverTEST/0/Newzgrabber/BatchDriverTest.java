import org.junit.Assert;
import org.junit.Test;

public class BatchDriverTest {
    // 

    @Test
    public void testGetGroups() {
        String newzini = "test/input/newzini.ini";
        String batchfile = "test/input/batchfile.ini";

        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        String[] expectedGroups = {"group1", "group2", "group3"};
        String[] actualGroups = batchDriver.getGroups();

        Assert.assertArrayEquals(expectedGroups, actualGroups);
    }

    // 
}