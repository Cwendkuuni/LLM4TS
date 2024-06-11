package src.test;

import org.junit.Test;
import static org.junit.Assert.*;
import src.Fiefdoms;
import src.GameAutoActions;
import src.CurrentPlayers;
import src.Map;
import src.Knight;
import src.Purchase;
import java.util.*;

public class PurchaseTest {

 private Purchase purchase;
 private Fiefdoms fiefdom;
 private CurrentPlayers currentPlayers;
 private Map map;
 private Knight knight;

 @Before
 public void setup() {
 purchase = new Purchase();
 fiefdom = new Fiefdoms();
 currentPlayers = new CurrentPlayers();
 map = new Map();
 knight = new Knight("knight1", "count");
 currentPlayers.put(knight.getName(), knight);
 }

 @Test
 public void testVassal() {
 Iterator<Object> arg = Arrays.asList("1", "player1", "fiefdom1").iterator();
 fiefdom.setName("fiefdom1");
 fiefdom.setOwner(null);
 map.put(fiefdom.getName(), fiefdom);
 CurrentPlayers.currentPlayers = currentPlayers;
 Map.map = map;

 purchase.perform(Arrays.asList(arg));

 assertEquals(1, fiefdom.getLoyalVassals());
 }

 @Test
 public void testVassal_InsufficientFunds() {
 Iterator<Object> arg = Arrays.asList("2", "player1", "fiefdom1").iterator();
 fiefdom.setName("fiefdom1");
 fiefdom.setOwner(null);
 map.put(fiefdom.getName(), fiefdom);
 CurrentPlayers.currentPlayers = currentPlayers;
 Map.map = map;
 knight.setTotalMoney(10);

 purchase.perform(Arrays.asList(arg));

 assertEquals(0, fiefdom.getLoyalVassals());
 }

 @Test
 public void testVassal_FiefdomNotBelongingToTheKingdom() {
 Iterator<Object> arg = Arrays.asList("1", "player1", "fiefdom2").iterator();
 fiefdom.setName("fiefdom2");
 fiefdom.setOwner(new Knight("king1", "king"));
 map.put(fiefdom.getName(), fiefdom);
 CurrentPlayers.currentPlayers = currentPlayers;
 Map.map = map;

 purchase.perform(Arrays.asList(arg));

 assertEquals(0, fiefdom.getLoyalVassals());
 }

 @Test
 public void testVassal_KingOwnsFiefdom() {
 Iterator<Object> arg = Arrays.asList("1", "king1", "fiefdom1").iterator();
 fiefdom.setName("fiefdom1");
 fiefdom.setOwner(new Knight("king1", "king"));
 map.put(fiefdom.getName(), fiefdom);
 CurrentPlayers.currentPlayers = currentPlayers;
 Map.map = map;

 purchase.perform(Arrays.asList(arg));

 assertEquals(1, fiefdom.getLoyalVassals());
 }

 @Test
 public void testVassal_FiefdomAlreadyHasMaxVassals() {
 Iterator<Object> arg = Arrays.asList("2", "player1", "fiefdom1").iterator();
 fiefdom.setName("fiefdom1");
 fiefdom.setOwner(null);
 fiefdom.setLoyalVassals(10);
 map.put(fiefdom.getName(), fiefdom);
 CurrentPlayers.currentPlayers = currentPlayers;
 Map.map = map;

 purchase.perform(Arrays.asList(arg));

 assertEquals(10, fiefdom.getLoyalVassals());
 }

 @Test
 public void testCastleFiefdom() {
 Iterator<Object> arg = Arrays.asList("1", "player1", "fiefdom1").iterator();
 fiefdom.setName("fiefdom1");
 fiefdom.setOwner(null);
 map.put(fiefdom.getName(), fiefdom);
 CurrentPlayers.currentPlayers = currentPlayers;
 Map.map = map;
 knight.setTotalMoney(100);

 purchase.perform(Arrays.asList(arg));

 assertEquals(1, fiefdom.getCastleCount());
 }

 @Test
 public void testCastleFiefdom_InsufficientFunds() {
 Iterator<Object> arg = Arrays.asList("1", "player1", "fiefdom1").iterator();
 fiefdom.setName("fiefdom1");
 fiefdom.setOwner(null);
 map.put(fiefdom.getName(), fiefdom);
 CurrentPlayers.currentPlayers = currentPlayers;
 Map.map = map;
 knight.setTotalMoney(50);

 purchase.perform(Arrays.asList(arg));

 assertEquals(0, fiefdom.getCastleCount());
 }

 @Test
 public void testCastleFiefdom_FiefdomNotBelongingToTheKingdom() {
 Iterator<Object> arg = Arrays.asList("1", "player1", "fiefdom2").iterator();
 fiefdom.setName("fiefdom2");
 fiefdom.setOwner(new Knight("king1", "king"));
 map.put(fiefdom.getName(), fiefdom);
 CurrentPlayers.currentPlayers = currentPlayers;
 Map.map = map;

 purchase.perform(Arrays.asList(arg));

 assertEquals(0, fiefdom.getCastleCount());
 }

 @Test
 public void testCastleFiefdom_KingOwnsFiefdom() {
 Iterator<Object> arg = Arrays.asList("1", "king1", "fiefdom1").iterator();
 fiefdom.setName("fiefdom1");
 fiefdom.setOwner(new Knight("king1", "king"));
 map.put(fiefdom.getName(), fiefdom);
 CurrentPlayers.currentPlayers = currentPlayers;
 Map.map = map;

 purchase.perform(Arrays.asList(arg));

 assertEquals(1, fiefdom.getCastleCount());
 }

 @Test
 public void testCastleFiefdom_MaxCastlesReached() {
 Iterator<Object> arg = Arrays.asList("1", "player1", "fiefdom1").iterator();
 fiefdom.setName("fiefdom1");
 fiefdom.setOwner(null);
 fiefdom.setCastleCount(10);
 map.put(fiefdom.getName(), fiefdom);
 CurrentPlayers.currentPlayers = currentPlayers;
 Map.map = map;
 knight.setTotalMoney(100);

 purchase.perform(Arrays.asList(arg));

 assertEquals(10, fiefdom.getCastleCount());
 }

 @Test
 public void testCastlePersonal() {
 Iterator<Object> arg = Arrays.asList("1", "player1", "fiefdom1").iterator();
 fiefdom.setName("fiefdom1");
 fiefdom.setOwner(null);
 map.put(fiefdom.getName(), fiefdom);
 CurrentPlayers.currentPlayers = currentPlayers;
 Map.map = map;
 knight.setTotalMoney(100);

 purchase.perform(Arrays.asList(arg));

 assertEquals(1, fiefdom.getPersonalCastleCount());
 }

 @Test
 public void testCastlePersonal_InsufficientFunds() {
 Iterator<Object> arg = Arrays.asList("1", "player1", "fiefdom1").iterator();
 fiefdom.setName("fiefdom1");
 fiefdom.setOwner(null);
 map.put(fiefdom.getName(), fiefdom);
 CurrentPlayers.currentPlayers = currentPlayers;
 Map.map = map;
 knight.setTotalMoney(50);

 purchase.perform(Arrays.asList(arg));

 assertEquals(0, fiefdom.getPersonalCastleCount());
 }

 @Test
 public void testCastlePersonal_FiefdomNotBelongingToTheKingdom() {
 Iterator<Object> arg = Arrays.asList("1", "player1", "fiefdom2").iterator();
 fiefdom.setName("fiefdom2");
 fiefdom.setOwner(new Knight("king1", "king"));
 map.put(fiefdom.getName(), fiefdom);
 CurrentPlayers.currentPlayers = currentPlayers;
 Map.map = map;

 purchase.perform(Arrays.asList(arg));

 assertEquals(0, fiefdom.getPersonalCastleCount());
 }

 @Test
 public void testCastlePersonal_KingOwnsFiefdom() {
 Iterator<Object> arg = Arrays.asList("1", "king1", "fiefdom1").iterator();
 fiefdom.setName("fiefdom1");
 fiefdom.setOwner(new Knight("king1", "king"));
 map.put(fiefdom.getName(), fiefdom);
 CurrentPlayers.currentPlayers = currentPlayers;
 Map.map = map;

 purchase.perform(Arrays.asList(arg));

 assertEquals(1, fiefdom.getPersonalCastleCount());
 }

 @Test
 public void testCastlePersonal_MaxCastlesReached() {
 Iterator<Object> arg = Arrays.asList("1", "player1", "fiefdom1").iterator();
 fiefdom.setName("fiefdom1");
 fiefdom.setOwner(null);
 fiefdom.setPersonalCastleCount(10);
 map.put(fiefdom.getName(), fiefdom);
 CurrentPlayers.currentPlayers = currentPlayers;
 Map.map = map;
 knight.setTotalMoney(100);

 purchase.perform(Arrays.asList(arg));

 assertEquals(10, fiefdom.getPersonalCastleCount());
 }

 @Test
 public void testMercenary() {
 Iterator<Object> arg = Arrays.asList("2", "player1", "knight1").iterator();
 knight.setName("knight1");
 knight.setTotalMoney(100);
 CurrentPlayers.currentPlayers = currentPlayers;
 Map.map = map;

 purchase.perform(Arrays.asList(arg));

 assertEquals(2, knight.getMercenaries());
 }

 @Test
 public void testMercenary_InsufficientFunds() {
 Iterator<Object> arg = Arrays.asList("2", "player1", "knight1").iterator();
 knight.setName("knight1");
 knight.setTotalMoney(50);
 CurrentPlayers.currentPlayers = currentPlayers;
 Map.map = map;

 purchase.perform(Arrays.asList(arg));

 assertEquals(0, knight.getMercenaries());
 }

 @Test
 public void testMercenary_SameName() {
 Iterator<Object> arg = Arrays.asList("2", "player1", "knight1").iterator();
 knight.setName("knight1");
 CurrentPlayers.currentPlayers = currentPlayers;
 Map.map = map;

 purchase.perform(Arrays.asList(arg));

 assertEquals(2, knight.getMercenaries());
 }

 @Test
 public void testMercenary_KingBuysMercenary() {
 Iterator<Object> arg = Arrays.asList("2", "king1", "knight1").iterator();
 knight.setName("knight1");
 CurrentPlayers.currentPlayers = currentPlayers;
 Map.map = map;

 purchase.perform(Arrays.asList(arg));

 assertEquals(2, knight.getMercenaries());
 }

 @Test
 public void testMercenary_MercenariesLimitExceeded() {
 Iterator<Object> arg = Arrays.asList("2", "player1", "knight1").iterator();
 knight.setName("knight1");
 knight.setMercenaries(10);
 CurrentPlayers.currentPlayers = currentPlayers;
 Map.map = map;

 purchase.perform(Arrays.asList(arg));

 assertEquals(10, knight.getMercenaries());
 }
}