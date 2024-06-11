package src.test.java;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import src.Fiefdom;
import src.Map;
import src.VassalRebellion;

public class VassalRebellionTest {

	private Map map;
	private Fiefdom fiefdomA, fiefdomB;
	private VassalRebellion vassalRebellion;

	@Before
	public void setup() {
		map = new Map();
		fiefdomA = new Fiefdom("A", 10, 5, 5, 10);
		fiefdomB = new Fiefdom("B", 5, 3, 2, 7);
		map.put(fiefdomA.getName(), fiefdomA);
		map.put(fiefdomB.getName(), fiefdomB);
		vassalRebellion = new VassalRebellion();
	}

	@Test
	public void testPerformWithLoyalVassals() {
		vassalRebellion.perform(Arrays.asList(fiefdomA.getName()));
		assertTrue(fiefdomA.isRebellious());
		assertTrue(fiefdomA.isKingdom());
		assertFalse(fiefdomB.isRebellious());
		assertFalse(fiefdomB.isKingdom());
		assertTrue(fiefdomA.getRebelliousVassals().size() > 0);
		assertTrue(fiefdomA.getPersonalCastles() > 0 || fiefdomA.getFiefdomCastles() > 0);
		assertTrue(fiefdomA.getLoyalVassals().size() - (fiefdomA.getPersonalCastles() * 10 + fiefdomA.getFiefdomCastles() * 25) > 0);
		assertTrue(fiefdomA.getRebelliousVassals().size() <= fiefdomA.getLoyalVassals().size() - (fiefdomA.getPersonalCastles() * 10 + fiefdomA.getFiefdomCastles() * 25));
	}

	@Test
	public void testPerformWithNoLoyalVassals() {
		map.put(fiefdomB.getName(), null);
		vassalRebellion.perform(Arrays.asList(fiefdomB.getName()));
		assertNull(map.get(fiefdomB.getName()));
		new RollEvent(fiefdomB.getName());
	}

	@Test
	public void testPerformWithPersonalCastles() {
		fiefdomA.setPersonalCastles(2);
		vassalRebellion.perform(Arrays.asList(fiefdomA.getName()));
		assertTrue(fiefdomA.isRebellious());
		assertTrue(fiefdomA.isKingdom());
		assertFalse(fiefdomB.isRebellious());
		assertFalse(fiefdomB.isKingdom());
		assertTrue(fiefdomA.getRebelliousVassals().size() > 0);
		assertTrue(fiefdomA.getPersonalCastles() > 0);
		assertTrue(fiefdomA.getFiefdomCastles() > 0 || fiefdomA.getLoyalVassals().size() - (fiefdomA.getPersonalCastles() * 10 + fiefdomA.getFiefdomCastles() * 25) > 0);
		assertTrue(fiefdomA.getRebelliousVassals().size() <= fiefdomA.getLoyalVassals().size() - (fiefdomA.getPersonalCastles() * 10 + fiefdomA.getFiefdomCastles() * 25));
	}

	@Test
	public void testPerformWithFiefdomCastles() {
		fiefdomA.setFiefdomCastles(3);
		vassalRebbellion.perform(Arrays.asList(fiefdomA.getName()));
		assertTrue(fiefdomA.isRebellious());
		assertTrue(fiefdomA.isKingdom());
		assertFalse(fiefdomB.isRebellious());
		assertFalse(fiefdomB.isKingdom());
		assertTrue(fiefdomA.getRebelliousVassals().size() > 0);
		assertTrue(fiefdomA.getPersonalCastles() > 0 || fiefdomA.getFiefdomCastles() > 0);
		assertTrue(fiefdomA.getRebelliousVassals().size() <= fiefdomA.getLoyalVassals().size() - (fiefdomA.getPersonalCastles() * 10 + fiefdomA.getFiefdomCastles() * 25));
	}

	@Test
	public void testPerformWithNoVassalsLeft() {
		fiefdomA.setLoyalVassals(new ArrayList<String>());
		vassalRebellion.perform(Arrays.asList(fiefdomA.getName()));
		assertFalse(fiefdomA.isRebellious());
		assertFalse(fiefdomA.isKingdom());
		assertNull(map.get(fiefdomA.getName()));
	}
}