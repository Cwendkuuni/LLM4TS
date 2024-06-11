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
        Fiefdoms testFiefdom = new Fiefdom(fiefdomName, 10, 5, 15, 5);
        map.put(fiefdomName, testFiefdom);
        Collection<String> input = new ArrayList<>();
        input.add(fiefdomName);

        vassalRebellion.perform(input);

        assertTrue(testFiefdom.isRebellious());
        assertFalse(testFiefdom.isKingdom());
        assertEquals(15, testFiefdom.getRebelliousVassals().size());
    }
}