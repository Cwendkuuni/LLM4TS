package src.test.java;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import src.Fiefdoms;
import src.GameAutoActions;
import src.Actions;
import src.Knight;
import src.Map;
import src.CurrentPlayers;

public class PurchaseTest {

 private Purchase purchase;
 private Fiefdoms fiefdom;
 private CurrentPlayers currentPlayers;
 private Knight buyer, knight;

 @Before
 public void setup() {
 purchase = new Purchase();
 fiefdom = new Fiefdoms();
 currentPlayers = new CurrentPlayers();
 buyer = new Knight("John", "knight", 1000);
 knight = new Knight("Mike", "knight", 500);
 }

 @Test
 public void testPerform() {
 // Test with valid arguments
 Collection args = new ArrayList<Object>() {{
 add("vassal");
 add(Arrays.asList(2, "John", "fiefdom1").iterator());
 }};
 purchase.perform(args);

 // Test with invalid arguments
 Collection invalidArgs = new ArrayList<Object>() {{
 add("invalidMethod");
 add(Collections.emptyIterator());
 }};
 try {
 purchase.perform(invalidArgs);
 fail("Should have thrown an exception");
 } catch (SecurityException e) {
 // Expected
 } catch (NoSuchMethodException e) {
 // Expected
 } catch (IllegalArgumentException e) {
 // Expected
 } catch (IllegalAccessException e) {
 // Expected
 } catch (InvocationTargetException e) {
 // Expected
 } catch (ClassNotFoundException e) {
 // Expected
 }
 }

 @Test
 public void testVassal() {
 // Test with valid arguments
 Collection vassalArgs = new ArrayList<Object>() {{
 add("vassal");
 add(Arrays.asList(3, "John", "fiefdom1").iterator());
 }};
 purchase.vassal(vassalArgs.iterator());
 assertEquals(3, fiefdom.getFiefdomByName("fiefdom1").getLoyalVassals());
 // Test with insufficient funds
 Collection insufficientFundsArgs = new ArrayList<Object>() {{
 add("vassal");
 add(Arrays.asList(4, "John", "fiefdom1").iterator());
 }};
 purchase.vassal(insufficientFundsArgs.iterator());
 assertEquals(0, fiefdom.getFiefdomByName("fiefdom1").getLoyalVassals());
 // Test with fiefdom not belonging to the kingdom
 Collection notKingdomArgs = new ArrayList<Object>() {{
 add("vassal");
 add(Arrays.asList(1, "John", "fiefdom2").iterator());
 }};
 purchase.vassal(notKingdomArgs.iterator());
 // Test with buyer already owning the fiefdom
 Collection ownFiefdomArgs = new ArrayList<Object>() {{
 add("vassal");
 add(Arrays.asList(3, "John", "fiefdom1").iterator());
 }};
 purchase.vassal(ownFiefdomArgs.iterator());
 purchase.vassal(ownFiefdomArgs.iterator());
 assertEquals(3, fiefdom.getFiefdomByName("fiefdom1").getLoyalVassals());
 // Test with buyer being a king and trying to buy a fiefdom that belongs to another player
 Collection anotherPlayersFiefdomArgs = new ArrayList<Object>() {{
 add("vassal");
 add(Arrays.asList(3, "King", "fiefdom2").iterator());
 }};
 currentPlayers.addPlayer(new Knight("AnotherPlayer", "king", 2000));
 fiefdom.addFiefdom("fiefdom2", new Knight("AnotherPlayer"));
 purchase.vassal(anotherPlayersFiefdomArgs.iterator());
 assertEquals(0, fiefdom.getFiefdomByName("fiefdom2").getLoyalVassals());
 // Test with buyer being a count or duke and trying to buy a fiefdom that belongs to another player
 Collection anotherPlayersCountyFiefdomArgs = new ArrayList<Object>() {{
 add("vassal");
 add(Arrays.asList(3, "Count", "fiefdom2").iterator());
 }};
 currentPlayers.addPlayer(new Knight("AnotherPlayer", "count", 1500));
 fiefdom.addFiefdom("fiefdom2", new Knight("AnotherPlayer"));
 purchase.vassal(anotherPlayersCountyFiefdomArgs.iterator());
 assertEquals(0, fiefdom.getFiefdomByName("fiefdom2").getLoyalVassals());
 }

 @Test
 public void testCastleFiefdom() {
 // Test with valid arguments
 Collection castleFiefdomArgs = new ArrayList<Object>() {{
 add("castleFiefdom");
 add(Arrays.asList(3, "John", "fiefdom1").iterator());
 }};
 purchase.castleFiefdom(castleFiefdomArgs.iterator());
 assertEquals(3, fiefdom.getFiefdomByName("fiefdom1").getCastles().size());
 // Test with insufficient funds
 Collection insufficientFundsArgs = new ArrayList<Object>() {{
 add("castleFiefdom");
 add(Arrays.asList(4, "John", "fiefdom1").iterator());
 }};
 purchase.castleFiefdom(insufficientFundsArgs.iterator());
 assertEquals(3, fiefdom.getFiefdomByName("fiefdom1").getCastles().size());
 // Test with fiefdom not belonging to the kingdom
 Collection notKingdomArgs = new ArrayList<Object>() {{
 add("castleFiefdom");
 add(Arrays.asList(1, "John", "fiefdom2").iterator());
 }};
 purchase.castleFiefdom(notKingdomArgs.iterator());
 // Test with buyer already owning the fiefdom
 Collection ownFiefdomArgs = new ArrayList<Object>() {{
 add("castleFiefdom");
 add(Arrays.asList(3, "John", "fiefdom1").iterator());
 }};
 purchase.castleFiefdom(ownFiefdomArgs.iterator());
 purchase.castleFiefdom(ownFiefdomArgs.iterator());
 assertEquals(3, fiefdom.getFiefdomByName("fiefdom1").getCastles().size());
 // Test with buyer being a king and trying to buy a fiefdom that belongs to another player
 Collection anotherPlayersCastleFiefdomArgs = new ArrayList<Object>() {{
 add("castleFiefdom");
 add(Arrays.asList(3, "King", "fiefdom2").iterator());
 }};
 currentPlayers.addPlayer(new Knight("AnotherPlayer", "king", 2000));
 fiefdom.addFiefdom("fiefdom2", new Knight("AnotherPlayer"));
 purchase.castleFiefdom(anotherPlayersCastleFiefdomArgs.iterator());
 assertEquals(3, fiefdom.getFiefdomByName("fiefdom2").getCastles().size());
 // Test with buyer being a count or duke and trying to buy a fiefdom that belongs to another player
 Collection anotherPlayersCountyCastleFiefdomArgs = new ArrayList<Object>() {{
 add("castleFiefdom");
 add(Arrays.asList(3, "Count", "fiefdom2").iterator());
 }};
 currentPlayers.addPlayer(new Knight("AnotherPlayer", "count", 1500));
 fiefdom.addFiefdom("fiefdom2", new Knight("AnotherPlayer"));
 purchase.castleFiefdom(anotherPlayersCountyCastleFiefdomArgs.iterator());
 assertEquals(3, fiefdom.getFiefdomByName("fiefdom2").getCastles().size());
 // Test with fiefdom having vassals or castles already
 Collection vassalsAndCastlesArgs = new ArrayList<Object>() {{
 add("castleFiefdom");
 add(Arrays.asList(3, "John", "fiefdom1").iterator());
 }};
 fiefdom.addFiefdom("fiefdom1", buyer);
 fiefdom.getFiefdomByName("fiefdom1").getLoyalVassals().add(new Knight("Vassal1"));
 fiefdom.getFiefdomByName("fiefdom1").getLoyalVassals().add(new Knight("Vassal2"));
 purchase.castleFiefdom(vassalsAndCastlesArgs.iterator());
 assertEquals(3, fiefdom.getFiefdomByName("fiefdom1").getCastles().size() + fiefdom.getFiefdomByName("fiefdom1").getLoyalVassals().size());
 }

 @Test
 public void testCastlePersonal() {
 // Test with valid arguments
 Collection castlePersonalArgs = new ArrayList<Object>() {{
 add("castlePersonal");
 add(Arrays.asList(3, "John", "fiefdom1").iterator());
 }};
 purchase.castlePersonal(castlePersonalArgs.iterator());
 assertEquals(3, buyer.getCastles().size());
 // Test with insufficient funds
 Collection insufficientFundsArgs = new ArrayList<Object>() {{
 add("castlePersonal");
 add(Arrays.asList(4, "John", "fiefdom1").iterator());
 }};
 purchase.castlePersonal(insufficientFundsArgs.iterator());
 assertEquals(3, buyer.getCastles().size());
 // Test with fiefdom not belonging to the kingdom
 Collection notKingdomArgs = new ArrayList<Object>() {{
 add("castlePersonal");
 add(Arrays.asList(1, "John", "fiefdom2").iterator());
 }};
 purchase.castlePersonal(notKingdomArgs.iterator());
 // Test with buyer being a king and trying to buy a castle for a count or duke
 Collection anotherPlayersCastlePersonalArgs = new ArrayList<Object>() {{
 add("castlePersonal");
 add(Arrays.asList(3, "King", "fiefdom2").iterator());
 }};
 currentPlayers.addPlayer(new Knight("AnotherPlayer", "count", 1500));
 fiefdom.addFiefdom("fiefdom2", new Knight("AnotherPlayer"));
 purchase.castlePersonal(anotherPlayersCastlePersonalArgs.iterator());
 assertEquals(3, buyer.getCastles().size());
 // Test with buyer being a count or duke and trying to buy a castle for himself
 Collection ownCastleArgs = new ArrayList<Object>() {{
 add("castlePersonal");
 add(Arrays.asList(3, "Count", "fiefdom1").iterator());
 }};
 purchase.castlePersonal(ownCastleArgs.iterator());
 purchase.castlePersonal(ownCastleArgs.iterator());
 assertEquals(3, buyer.getCastles().size());
 // Test with fiefdom having vassals or castles already
 Collection vassalsAndCastlesArgs = new ArrayList<Object>() {{
 add("castlePersonal");
 add(Arrays.asList(3, "John", "fiefdom1").iterator());
 }};
 fiefdom.addFiefdom("fiefdom1", buyer);
 fiefdom.getFiefdomByName("fiefdom1").getLoyalVassals().add(new Knight("Vassal1"));
 fiefdom.getFiefdomByName("fiefdom1").getLoyalVassals().add(new Knight("Vassal2"));
 purchase.castlePersonal(vassalsAndCastlesArgs.iterator());
 assertEquals(3, buyer.getCastles().size() + fiefdom.getFiefdomByName("fiefdom1").getLoyalVassals().size());
 }

 @Test
 public void testMercenary() {
 // Test with valid arguments
 Collection mercenaryArgs = new ArrayList<Object>() {{
 add("mercenary");
 add(Arrays.asList(3, "John", "Mike").iterator());
 }};
 purchase.mercenary(mercenaryArgs.iterator());
 assertEquals(3, knight.getMercenaries());
 // Test with insufficient funds
 Collection insufficientFundsArgs = new ArrayList<Object>() {{
 add("mercenary");
 add(Arrays.asList(4, "John", "Mike").iterator());
 }};
 purchase.mercenary(insufficientFundsArgs.iterator());
 assertEquals(3, knight.getMercenaries());
 // Test with different buyers
 Collection differentBuyersArgs = new ArrayList<Object>() {{
 add("mercenary");
 add(Arrays.asList(3, "Mike", "John").iterator());
 }};
 purchase.mercenary(differentBuyersArgs.iterator());
 assertEquals(0, knight.getMercenaries());
 // Test with buyer and knight being the same
 Collection sameBuyerArgs = new ArrayList<Object>() {{
 add("mercenary");
 add(Arrays.asList(3, "John", "John").iterator());
 }};
 purchase.mercenary(sameBuyerArgs.iterator());
 assertEquals(3, knight.getMercenaries());
 }
}