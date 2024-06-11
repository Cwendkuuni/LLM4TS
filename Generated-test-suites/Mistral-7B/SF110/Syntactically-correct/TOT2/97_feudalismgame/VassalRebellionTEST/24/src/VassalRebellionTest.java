import org.junit.Before;
import org.junit.Test;
import src.VassalRebellion;
import src.Fiefdoms;
import java.util.*;

public class VassalRebellionTest {
    private VassalRebellion vassalRebellion;
    private Fiefdoms fiefdoms;

    @Before
    public void setUp() {
        fiefdoms = new Fiefdoms();
        vassalRebellion = new VassalRebellion() {
            @Override
            public void perform(Collection<String> col) {
                super.perform(col);
            }
        };
        vassalRebellion.setFiefdomObj(fiefdoms.createFiefdom("TestFiefdom"));
    }

    @Test
    public void testPerform() {
        Collection<String> input = new ArrayList<>();
        input.add("TestFiefdom");
        vassalRebellion.perform(input);
        // Add assertions here
    }
}