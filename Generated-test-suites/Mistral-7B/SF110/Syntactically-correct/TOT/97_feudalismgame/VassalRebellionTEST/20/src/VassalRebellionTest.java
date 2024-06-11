
package src.test;

import org.junit.Before;
import org.junit.Test;
import src.Fiefdoms;
import src.VassalRebellion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

public class VassalRebellionTest {

    private Fiefdoms fiefdomObj;
    private VassalRebellion vassalRebellion;

    @Before
    public void setup() {
        fiefdomObj = new Fiefdoms("TestFiefdom", 10, 5, new ArrayList<>());
        vassalRebellion = new VassalRebbellion();
    }

    // Test cases from Experts 1, 2, and 3

    // Expert 1 test cases
    @Test
    public void testPerformWithValidFiefdomAndLoyalVassals() {
        // Given
        Map<String, Fiefdoms> map = new HashMap<>();
        map.put("TestFiefdom", fiefdomObj);
        vassalRebellion.setMap(map);

        // When
        vassalRebellion.perform(new ArrayList<>(Arrays.asList("TestFiefdom")));

        // Then
        assertTrue(fiefdomObj.isRebellious());
        assertFalse(fiefdomObj.isKingdom());
    }

    @Test
    public void testPerformWithValidFiefdomAndNoLoyalVassals() {
        // Given
        fiefdomObj.setValue(0);

        // When
        vassalRebellion.perform(new ArrayList<>(Arrays.asList("TestFiefdom")));

        // Then
        assertTrue(new RollEvent("TestFiefdom").isRolled());
    }

    // Expert 2 test cases
    @Test
    public void testPerformWithFiefdomNameNull() {
        // Given
        VassalRebellion vassalRebellion = new VassalRebellion();

        // When and Then
        assertThrows(NullPointerException.class, () -> vassalRebellion.perform(null));
    }

    @Test
    public void testPerformWithInvalidFiefdomName() {
        // Given
        VassalRebellion vassalRebellion = new VassalRebbellion();
        Map<String, Fiefdoms> map = new HashMap<>();

        // When
        vassalRebellion.setMap(map);

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> vassalRebbellion.perform(Arrays.asList("InvalidFiefdom")));
    }

    // Expert 3 test cases
    @Test
    public void testPerformWithFiefdomAlreadyRebellious() {
        // Given
        Fiefdoms fiefdomObj = new Fiefdoms("TestFiefdom", 1, 1, new ArrayList<>());
        fiefdomObj.setRebelliousVassals(1, true);
        Map<String, Fiefdoms> map = new HashMap<>();
        map.put("TestFiefdom", fiefdomObj);
        VassalRebbellion vassalRebellion = new VassalRebbellion();
        vassalRebellion.setMap(map);

        // When
        vassalRebellion.perform(new ArrayList<>(Arrays.asList("TestFiefdom")));

        // Then
        assertTrue(fiefdomObj.isRebellious());
    }

    @Test
    public void testPerformWithNoCastles() {
        // Given
        Fiefdoms fiefdomObj = new Fiefdoms("TestFiefdom", 0, 0, new ArrayList<>());
        Map<String, Fiefdoms> map = new HashMap<>();
        map.put("TestFiefdom", fiefdomObj);
        VassalRebbellion vassalRebellion = new VassalRebbellion();
        vassalRebellion.setMap(map);

        // When
        vassalRebellion.perform(new ArrayList<>(Arrays.asList("TestFiefdom")));

        // Then
        assertFalse(fiefdomObj.isRebellious());
    }
}