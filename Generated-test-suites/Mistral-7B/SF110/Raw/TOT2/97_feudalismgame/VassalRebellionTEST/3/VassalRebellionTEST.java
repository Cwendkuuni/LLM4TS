import org.junit.Test;
import static org.junit.Assert.*;
import src.VassalRebellion;
import src.Fiefdoms;
import java.util.*;

public class VassalRebellionTest {
    @Test
    public void testPerform() {
        Fiefdoms map = new Fiefdoms();
        VassalRebellion vassalRebellion = new VassalRebellion();
        String fiefdomName = "testFiefdom";
        Fiefdom testFiefdom = new Fiefdom(fiefdomName, 10, 5, 5, 5, 5);
        map.put(fiefdomName, testFiefdom);
        Collection<String> collection = new ArrayList<>();
        collection.add(fiefdomName);
        vassalRebellion.perform(collection);
        assertTrue(testFiefdom.isRebellious());
    }
}