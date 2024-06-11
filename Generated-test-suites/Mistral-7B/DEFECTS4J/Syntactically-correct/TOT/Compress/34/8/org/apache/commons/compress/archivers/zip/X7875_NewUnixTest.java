import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    @Test
    public void testX7875_NewUnix_BasicFunctionality() throws ZipException {
        // Create an instance of X7875_NewUnix
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();

        // Set UID and GID values
        x7875NewUnix.setUID(1001);
        x7875NewUnix.setGID(1002);

        // Get UID and GID values and compare with set values
        long actualUID = x7875NewUnix.getUID();
        long actualGID = x7875NewUnix.getGID();
        Assert.assertEquals(1001, actualUID);
        Assert.assertEquals(1002, actualGID);

        // Reset UID and GID values
        x7875NewUnix.reset();

        // Verify UID and GID values are reset to defaults
        actualUID = x7875NewUnix.getUID();
        actualGID = x7875NewUnix.getGID();
        Assert.assertEquals(1000, actualUID);
        Assert.assertEquals(1000, actualGID);
    }

    @Test(expected = ZipException.class)
    public void testX7875_NewUnix_InvalidUID() throws ZipException {
        // Create an instance of X7875_NewUnix
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();

        // Set an invalid UID value
        x7875NewUnix.setUID(-1);

        // Get UID value and throw ZipException since it's less than 0
        x7875NewUnix.getUID();
    }

    @Test(expected = ZipException.class)
    public void testX7875_NewUnix_InvalidGID() throws ZipException {
        // Create an instance of X7875_NewUnix
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();

        // Set an invalid GID value
        x7875NewUnix.setGID(-1);

        // Get GID value and throw ZipException since it's less than 0
        x7875NewUnix.getGID();
    }

    @Test
    public void testX7875_NewUnix_DataHandling() throws ZipException {
        // Create an instance of X7875_NewUnix
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();

        // Set UID and GID values
        x7875NewUnix.setUID(1001);
        x7875NewUnix.setGID(1002);

        // Get local file data and verify it matches the set values
        byte[] localFileData = x7875NewUnix.getLocalFileDataData();
        X7875_NewUnix parsedX7875NewUnix = new X7875_NewUnix();
        parsedX7875NewUnix.parseFromLocalFileData(localFileData, 0, localFileData.length);
        long actualUID = parsedX7875NewUnix.getUID();
        long actualGID = parsedX7875NewUnix.getGID();
        Assert.assertEquals(1001, actualUID);
        Assert.assertEquals(1002, actualGID);

        // Reset state and verify parsed values are now defaults
        parsedX7875NewUnix.reset();
        actualUID = parsedX7875NewUnix.getUID();
        actualGID = parsedX7875NewUnix.getGID();
        Assert.assertEquals(1000, actualUID);
        Assert.assertEquals(1000, actualGID);
    }

    @Test
    public void testX7875_NewUnix_Clone() throws CloneNotSupportedException {
        // Create an instance of X7875_NewUnix
        X7875_NewUnix originalX7875NewUnix = new X7875_NewUnix();
        originalX7875NewUnix.setUID(1001);
        originalX7875NewUnix.setGID(1002);

        // Create a clone of the instance and verify the cloned instance has the same values
        X7875_NewUnix clonedX7875NewUnix = (X7875_NewUnix) originalX7875NewUnix.clone();
        long actualUID = clonedX7875NewUnix.getUID();
        long actualGID = clonedX7875NewUnix.getGID();
        Assert.assertEquals(1001, actualUID);
        Assert.assertEquals(1002, actualGID);
    }
}