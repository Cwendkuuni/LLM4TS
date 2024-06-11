package src.test;

import org.junit.Test;
import static org.junit.Assert.*;
import src.Fiefdoms;
import src.VassalRebellion;

public class VassalRebellionTest {
    @Test
    public void testPerform_LoyalVassalsAndPersonalCastle() {
        // Given
        Fiefdoms fiefdomMap = new Fiefdoms();
        String fiefdomName = "TestFiefdom";
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setName(fiefdomName);
        fiefdomObj.setLoyalVassals(10);
        fiefdomObj.setPersonalCastles(1);
        fiefdomObj.setValue(5);
        fiefdomMap.put(fiefdomName, fiefdomObj);

        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.setFiefdomObj(fiefdomObj);

        // When
        vassalRebellion.perform(Collections.singletonList(fiefdomName));

        // Then
        assertTrue(fiefdomObj.isRebellious());
        assertEquals(5, fiefdomObj.getLoyalVassals().size()); // Loyal vassals should not change
        assertEquals(10, fiefdomMap.get(fiefdomName).getRebelliousVassals().size());
    }
}