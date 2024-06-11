package src.test;

import org.junit.Assert;
import org.junit.Test;
import src.Fiefdoms;
import src.VassalRebellion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        // Test case for basic functionality
        Fiefdoms fiefdom = new Fiefdoms("TestFiefdom", 10, 5, new ArrayList<>(), 100);
        List<String> input = new ArrayList<>();
        input.add(fiefdom.getName());
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.setFiefdomObj(fiefdom);
        vassalRebellion.perform(input);
        Assert.assertFalse(fiefdom.isKingdom());
        // Add more assertions here to check the state of the fiefdom object
    }

    @Test
    public void testEmptyCollection() {
        // Test case for empty collection
        VassalRebellion vassalRebellion = new VassalRebellion();
        List<String> input = new ArrayList<>();
        vassalRebellion.perform(input);
        // Add assertions here to check the state of the fiefdom object and exception handling
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonExistentFiefdom() {
        // Test case for non-existent fiefdom in the map
        Fiefdoms fiefdom = new Fiefdoms("TestFiefdom", 10, 5, new ArrayList<>(), 100);
        Map<String, Fiefdoms> map = new HashMap<>();
        map.put("TestFiefdom", fiefdom);
        List<String> input = new ArrayList<>();
        input.add("TestFiefdom2");
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.perform(input);
        // Add assertions here to check the exception handling
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeValues() {
        // Test case for negative values
        Fiefdoms fiefdom = new Fiefdoms("TestFiefdom", -5, 5, new ArrayList<>(), 100);
        List<String> input = new ArrayList<>();
        input.add(fiefdom.getName());
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.setFiefdomObj(fiefdom);
        vassalRebellion.perform(input);
        // Add assertions here to check the exception handling
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyOrNullLoyalVassals() {
        // Test case for empty or null list of loyal vassals
        Fiefdoms fiefdom = new Fiefdoms("TestFiefdom", 10, 5, null, 100);
        List<String> input = new ArrayList<>();
        input.add(fiefdom.getName());
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.setFiefdomObj(fiefdom);
        vassalRebellion.perform(input);
        // Add assertions here to check the exception handling
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueLessThanZero() {
        // Test case for value less than 0
        Fiefdoms fiefdom = new Fiefdoms("TestFiefdom", 10, 5, new ArrayList<>(), -1);
        List<String> input = new ArrayList<>();
        input.add(fiefdom.getName());
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.setFiefdomObj(fiefdom);
        vassalRebellion.perform(input);
        // Add assertions here to check the exception handling
    }
}