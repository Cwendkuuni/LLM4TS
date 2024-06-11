package src.test.java;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import src.Actions;
import src.Fiefdom;
import src.Fiefdoms;

public class VassalRebellionTest
{
    private Fiefdoms fiefdoms;
    private Fiefdom fiefdom1, fiefdom2;
    private VassalRebellion vassalRebellion;

    @Before
    public void setUp()
    {
        fiefdoms = new Fiefdoms();
        fiefdom1 = new Fiefdom("Fiefdom 1", 10, 5, 10, new HashMap<String, Integer>());
        fiefdom2 = new Fiefdom("Fiefdom 2", 5, 15, 5, new HashMap<String, Integer>());
        fiefdoms.addFiefdom(fiefdom1);
        fiefdoms.addFiefdom(fiefdom2);
        vassalRebellion = new VassalRebellion();
    }

    @Test
    public void testPerform_LoyalVassals_NoRebellion()
    {
        vassalRebellion.perform(fiefdom1.getLoyalVassals().keySet());
        assertFalse(fiefdom1.isRebellious());
        assertFalse(fiefdom1.isKingdom());
    }

    @Test
    public void testPerform_LoyalVassals_Rebellion()
    {
        fiefdom1.setLoyalVassals(new HashMap<String, Integer>() {{
            put("Vassal 1", 15);
            put("Vassal 2", 10);
            put("Vassal 3", 5);
        }} );

        vassalRebellion.perform(fiefdom1.getLoyalVassals().keySet());
        assertTrue(fiefdom1.isRebellious());
        assertFalse(fiefdom1.isKingdom());
    }

    @Test
    public void testPerform_NoLoyalVassals()
    {
        vassalRebellion.perform(fiefdom2.getLoyalVassals().keySet());
        assertNull(fiefdom2.getVassalRebellionEvent());
    }

    @Test
    public void testPerform_PersonalCastlesAndFiefdomCastles_NoRebellion()
    {
        fiefdom1.setLoyalVassals(new HashMap<String, Integer>() {{
            put("Vassal 1", 15);
            put("Vassal 2", 10);
            put("Vassal 3", 5);
        }} );
        fiefdom1.setPersonalCastles(2);
        fiefdom1.setFiefdomCastles(3);

        vassalRebbellion.perform(fiefdom1.getLoyalVassals().keySet());
        assertFalse(fiefdom1.isRebellious());
        assertFalse(fiefdom1.isKingdom());
    }

    @Test
    public void testPerform_PersonalCastlesAndFiefdomCastles_Rebellion()
    {
        fiefdom1.setLoyalVassals(new HashMap<String, Integer>() {{
            put("Vassal 1", 15);
            put("Vassal 2", 10);
            put("Vassal 3", 5);
        }} );
        fiefdom1.setPersonalCastles(2);
        fiefdom1.setFiefdomCastles(3);

        vassalRebellion.perform(fiefdom1.getLoyalVassals().keySet());
        assertTrue(fiefdom1.isRebellious());
        assertFalse(fiefdom1.isKingdom());

        List<String> expectedEvents = new ArrayList<String>() {{
            add(fiefdom1.getName());
        }};

        List<String> actualEvents = fiefdom1.getVassalRebellionEvent();
        assertTrue(actualEvents.containsAll(expectedEvents));
    }
}