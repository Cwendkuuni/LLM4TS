import org.junit.Test;
import org.junit.Assert.*;
import src.Purchase;
import src.Fiefdoms;
import src.Knight;
import src.CurrentPlayers;
import src.Actions;
import src.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class PurchaseTest {

 private Purchase purchase;
 private Fiefdoms map;
 private CurrentPlayers currentPlayers;
 private Knight knight;

 @Before
 public void setup() {
 purchase = new Purchase();
 map = new Fiefdoms();
 currentPlayers = new CurrentPlayers();
 knight = new Knight("John", "count");
 map.map.put("Fiefdom1", new Fiefdoms("Fiefdom1", "John"));
 currentPlayers.currentPlayers.put("John", knight);
 }

 @Test
 public void testPerform() {
 Collection args = Arrays.asList(new Object[]{"vassal", Arrays.asList(3, "John", "Fiefdom1").iterator()});
 purchase.perform(args);
 }

 @Test
 public void testVassalWithValidInput() {
 Iterator arg = Arrays.asList(3, "John", "Fiefdom1").iterator();
 purchase.vassal(arg);
 Fiefdoms expectedFiefdom = map.map.get("Fiefdom1");
 Knight expectedBuyer = currentPlayers.currentPlayers.get("John");
 assertTrue(expectedFiefdom.getLoyalVassals() == 3);
 assertTrue(expectedBuyer.getTotalMoney() < 0);
 }

 @Test
 public void testVassalWithInvalidInput() {
 Iterator arg = Arrays.asList("InvalidInput", "John", "Fiefdom1").iterator();
 purchase.vassal(arg);
 Fiefdoms expectedFiefdom = map.map.get("Fiefdom1");
 Knight expectedBuyer = currentPlayers.currentPlayers.get("John");
 assertEquals(expectedFiefdom.getLoyalVassals(), 0);
 assertEquals(expectedBuyer.getTotalMoney(), 0);
 }

 @Test
 public void testVassalWithInvalidFiefdom() {
 Iterator arg = Arrays.asList(3, "John", "InvalidFiefdom").iterator();
 purchase.vassal(arg);
 Fiefdoms expectedFiefdom = map.map.get("Fiefdom1");
 Knight expectedBuyer = currentPlayers.currentPlayers.get("John");
 assertEquals(expectedFiefdom.getLoyalVassals(), 0);
 assertEquals(expectedBuyer.getTotalMoney(), 0);
 }

 @Test
 public void testVassalForKing() {
 Iterator arg = Arrays.asList(4, "John", "Fiefdom1").iterator();
 purchase.vassal(arg);
 Fiefdoms expectedFiefdom = map.map.get("Fiefdom1");
 Knight expectedBuyer = currentPlayers.currentPlayers.get("John");
 assertTrue(expectedFiefdom.getLoyalVassals() == 4);
 assertTrue(expectedBuyer.getTotalMoney() < 0);
 }

 @Test
 public void testVassalForFiefdomOwner() {
 Iterator arg = Arrays.asList(2, "John", "Fiefdom1").iterator();
 purchase.vassal(arg);
 Fiefdoms expectedFiefdom = map.map.get("Fiefdom1");
 Knight expectedBuyer = currentPlayers.currentPlayers.get("John");
 assertTrue(expectedFiefdom.getLoyalVassals() == 2);
 assertTrue(expectedBuyer.getTotalMoney() >= 0);
 }

 @Test
 public void testCastleFiefdomWithValidInput() {
 Iterator arg = Arrays.asList(2, "John", "Fiefdom1").iterator();
 purchase.castleFiefdom(arg);
 Fiefdoms expectedFiefdom = map.map.get("Fiefdom1");
 Knight expectedBuyer = currentPlayers.currentPlayers.get("John");
 assertTrue(expectedFiefdom.getCastleQty() == 2);
 assertTrue(expectedBuyer.getTotalMoney() < 0);
 }

 @Test
 public void testCastleFiefdomWithInvalidInput() {
 Iterator arg = Arrays.asList(3, "John", "Fiefdom1").iterator();
 purchase.castleFiefdom(arg);
 Fiefdoms expectedFiefdom = map.map.get("Fiefdom1");
 Knight expectedBuyer = currentPlayers.currentPlayers.get("John");
 assertEquals(expectedFiefdom.getCastleQty(), 0);
 assertEquals(expectedBuyer.getTotalMoney(), 0);
 }

 @Test
 public void testCastleFiefdomForFiefdomOwner() {
 Iterator arg = Arrays.asList(1, "John", "Fiefdom1").iterator();
 purchase.castleFiefdom(arg);
 Fiefdoms expectedFiefdom = map.map.get("Fiefdom1");
 Knight expectedBuyer = currentPlayers.currentPlayers.get("John");
 assertTrue(expectedFiefdom.getCastleQty() == 1);
 assertTrue(expectedBuyer.getTotalMoney() >= 0);
 }

 @Test
 public void testCastleFiefdomForKing() {
 Iterator arg = Arrays.asList(2, "John", "Fiefdom1").iterator();
 purchase.castleFiefdom(arg);
 Fiefdoms expectedFiefdom = map.map.get("Fiefdom1");
 Knight expectedBuyer = currentPlayers.currentPlayers.get("John");
 assertTrue(expectedFiefdom.getCastleQty() == 2);
 assertTrue(expectedBuyer.getTotalMoney() < 0);
 }

 @Test
 public void testCastlePersonalWithValidInput() {
 Iterator arg = Arrays.asList(2, "John", "Fiefdom1").iterator();
 purchase.castlePersonal(arg);
 Fiefdoms expectedFiefdom = map.map.get("Fiefdom1");
 Knight expectedBuyer = currentPlayers.currentPlayers.get("John");
 assertTrue(expectedFiefdom.getPersonalCastleQty() == 2);
 assertTrue(expectedBuyer.getTotalMoney() < 0);
 }

 @Test
 public void testCastlePersonalWithInvalidInput() {
 Iterator arg = Arrays.asList(3, "John", "Fiefdom1").iterator();
 purchase.castlePersonal(arg);
 Fiefdoms expectedFiefdom = map.map.get("Fiefdom1");
 Knight expectedBuyer = currentPlayers.currentPlayers.get("John");
 assertEquals(expectedFiefdom.getPersonalCastleQty(), 0);
 assertEquals(expectedBuyer.getTotalMoney(), 0);
 }

 @Test
 public void testCastlePersonalForFiefdomOwner() {
 Iterator arg = Arrays.asList(1, "John", "Fiefdom1").iterator();
 purchase.castlePersonal(arg);
 Fiefdoms expectedFiefdom = map.map.get("Fiefdom1");
 Knight expectedBuyer = currentPlayers.currentPlayers.get("John");
 assertTrue(expectedFiefdom.getPersonalCastleQty() == 1);
 assertTrue(expectedBuyer.getTotalMoney() >= 0);
 }

 @Test
 public void testCastlePersonalForKing() {
 Iterator arg = Arrays.asList(2, "John", "Fiefdom1").iterator();
 purchase.castlePersonal(arg);
 Fiefdoms expectedFiefdom = map.map.get("Fiefdom1");
 Knight expectedBuyer = currentPlayers.currentPlayers.get("John");
 assertTrue(expectedFiefdom.getPersonalCastleQty() == 2);
 assertTrue(expectedBuyer.getTotalMoney() < 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMercenaryWithInvalidKnight() {
 Iterator arg = Arrays.asList(3, "InvalidKnight", "John").iterator();
 purchase.mercenary(arg);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMercenaryWithInvalidBuyer() {
 Iterator arg = Arrays.asList(3, "John", "InvalidBuyer").iterator();
 purchase.mercenary(arg);
 }

 @Test
 public void testMercenaryWithValidInput() {
 Iterator arg = Arrays.asList(3, "John", "John").iterator();
 purchase.mercenary(arg);
 Knight expectedBuyer = currentPlayers.currentPlayers.get("John");
 assertTrue(expectedBuyer.getMercenaries() > 0);
 }

 @Test
 public void testMercenaryForKing() {
 Iterator arg = Arrays.asList(3, "King", "John").iterator();
 purchase.mercenary(arg);
 Knight expectedBuyer = currentPlayers.currentPlayers.get("John");
 assertTrue(expectedBuyer.getMercenaries() > 0);
 }

 @After
 public void tearDown() {
 purchase = null;
 map = null;
 currentPlayers = null;
 knight = null;
 }
}