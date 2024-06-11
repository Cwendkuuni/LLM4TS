package Newzgrabber;
import org.junit.Assert;
import org.junit.Test;

public class NewzgrabberTest {
    
    @Test
    public void testNewzgrabberConstructor() {
        Newzgrabber newzgrabber = new Newzgrabber();
        Assert.assertNotNull(newzgrabber);
        
    }
}