package src.test.java;

import org.junit.Before;
import org.junit.Test;
import src.Actions;
import src.Fiefdoms;
import src.VassalRebellion;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;
    private Fiefdoms fiefdoms;

    @Before
    public void setup() {
        fiefdoms = new Fiefdoms();
        vassalRebellion = new VassalRebellion();
        fiefdoms.put("Albion", new Fiefdoms("Albion", 5, 3, 10, 5));
    }

    @Test
    public void testPerform_ValidInput_RebellionOccurs() {
        List<String> input = new ArrayList<>();
        input.add("Albion");
        vassalRebellion.perform(input);

        assertFalse(fiefdoms.get("Albion").isKingdom());
        assertTrue(fiefdoms.get("Albion").getRebelliousVassals().size() > 0);
    }

    @Test
    public void testGetPersonalCastles_ValidInput_ReturnsCorrectValue() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Fiefdoms fiefdom = new Fiefdoms("Albion", 5, 3, 10, 5);
        assertEquals(5, vassalRebellion.getFiefdomObj(fiefdom).getPersonalCastles());
    }

    @Test
    public void testGetFiefdomCastles_ValidInput_ReturnsCorrectValue() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        Fiefdoms fiefdom = new Fiefdoms("Albion", 5, 3, 10, 5);
        assertEquals(3, vassalRebellion.getFiefdomObj(fiefdom).getFiefdomCastles());
    }

    // Test edge cases and exception handling scenarios here

    private Fiefdoms getFiefdomObj(String name) {
        return fiefdoms.get(name);
    }
}