package src.test.java;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import src.*;
import src.exceptions.InsufficientFundsException;
import src.exceptions.InvalidFiefdomException;
import src.exceptions.InvalidRankException;
import src.mocks.*;

public class PurchaseTest {

 private Purchase purchase;
 private Iterator<Object> args;
 private Fiefdoms fiefdom;
 private Knight buyerObj, knightObj, anotherKnightObj;

 @Before
 public void setup() {
 purchase = new Purchase();
 fiefdom = MockFiefdoms.getFiefdom("testFiefdom");
 buyerObj = MockKnights.getKnight("testBuyer");
 knightObj = MockKnights.getKnight("testKnight");
 anotherKnightObj = MockKnights.getKnight("anotherTestKnight");
 args = Collections.asList(new Object[] { 5, buyerObj.getName(), fiefdom, knightObj }).iterator();
 }

 @Test
 public void testPerform() {
 purchase.perform(args);
 verify(purchase, times(1)).vassal(args);
 }

 @Test(expected = SecurityException.class)
 public void testPerformWithSecurityException() {
 purchase.perform(args);
 doThrow(new SecurityException()).when(purchase).getClass();
 purchase.perform(args);
 }

 @Test(expected = NoSuchMethodException.class)
 public void testPerformWithNoSuchMethodException() {
 purchase.perform(args);
 doThrow(new NoSuchMethodException()).when(Class.forName("feudalism.Purchase")).getMethod(args.next(), Iterator.class);
 purchase.perform(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPerformWithIllegalArgumentException() {
 purchase.perform(args);
 doThrow(new IllegalArgumentException()).when(args).next();
 purchase.perform(args);
 }

 @Test(expected = IllegalAccessException.class)
 public void testPerformWithIllegalAccessException() {
 purchase.perform(args);
 doThrow(new IllegalAccessException()).when(methodObj).invoke(purchase, args);
 }

 @Test(expected = InvocationTargetException.class)
 public void testPerformWithInvocationTargetException() {
 purchase.perform(args);
 doThrow(new InvocationTargetException(new RuntimeException())).when(methodObj).invoke(purchase, args);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testPerformWithClassNotFoundException() {
 purchase.perform(args);
 doThrow(new ClassNotFoundException()).when(Class.forName("feudalism.Purchase"));
 purchase.perform(args);
 }

 @Test
 public void testVassal() throws InsufficientFundsException, InvalidFiefdomException, InvalidRankException {
 purchase.vassal(args);
 assertEquals(5, fiefdom.getLoyalVassals().size());
 }

 @Test(expected = InsufficientFundsException.class)
 public void testVassalWithInsufficientFundsException() {
 args = Collections.asList(new Object[] { 10, buyerObj.getName(), fiefdom }).iterator();
 purchase.vassal(args);
 }

 @Test(expected = InvalidFiefdomException.class)
 public void testVassalWithInvalidFiefdomException() {
 args = Collections.asList(new Object[] { 5, buyerObj.getName(), null }).iterator();
 purchase.vassal(args);
 }

 @Test(expected = InvalidRankException.class)
 public void testVassalWithInvalidRankException() {
 args = Collections.asList(new Object[] { 5, "queen", fiefdom }).iterator();
 purchase.vassal(args);
 }

 @Test
 public void testCastleFiefdom() throws InsufficientFundsException, InvalidFiefdomException, InvalidRankException {
 purchase.castleFiefdom(args);
 assertEquals(5, fiefdom.getCastles().size());
 }

 @Test(expected = InsufficientFundsException.class)
 public void testCastleFiefdomWithInsufficientFundsException() {
 args = Collections.asList(new Object[] { 10, buyerObj.getName(), fiefdom }).iterator();
 purchase.castleFiefdom(args);
 }

 @Test(expected = InvalidFiefdomException.class)
 public void testCastleFiefdomWithInvalidFiefdomException() {
 args = Collections.asList(new Object[] { 5, buyerObj.getName(), null }).iterator();
 purchase.castleFiefdom(args);
 }

 @Test(expected = InvalidRankException.class)
 public void testCastleFiefdomWithInvalidRankException() {
 args = Collections.asList(new Object[] { 5, "queen", fiefdom }).iterator();
 purchase.castleFiefdom(args);
 }

 @Test
 public void testCastlePersonal() throws InsufficientFundsException, InvalidFiefdomException, InvalidRankException {
 purchase.castlePersonal(args);
 assertEquals(5, fiefdom.getPersonalCastles().size());
 }

 @Test(expected = InsufficientFundsException.class)
 public void testCastlePersonalWithInsufficientFundsException() {
 args = Collections.asList(new Object[] { 10, buyerObj.getName(), fiefdom }).iterator();
 purchase.castlePersonal(args);
 }

 @Test(expected = InvalidFiefdomException.class)
 public void testCastlePersonalWithInvalidFiefdomException() {
 args = Collections.asList(new Object[] { 5, buyerObj.getName(), null }).iterator();
 purchase.castlePersonal(args);
 }

 @Test(expected = InvalidRankException.class)
 public void testCastlePersonalWithInvalidRankException() {
 args = Collections.asList(new Object[] { 5, "queen", fiefdom }).iterator();
 purchase.castlePersonal(args);
 }

 @Test
 public void testMercenary() throws InsufficientFundsException {
 purchase.mercenary(args);
 assertEquals(5, knightObj.getMercenaries().size());
 }

 @Test(expected = InsufficientFundsException.class)
 public void testMercenaryWithInsufficientFundsException() {
 args = Collections.asList(new Object[] { 10, buyerObj.getName(), knightObj }).iterator();
 purchase.mercenary(args);
 }

 @Test(expected = InvalidKnightException.class)
 public void testMercenaryWithInvalidKnightException() {
 args = Collections.asList(new Object[] { 5, buyerObj.getName(), null }).iterator();
 purchase.mercenary(args);
 }

 @Test(expected = SameNameException.class)
 public void testMercenaryWithSameNameException() {
 args = Collections.asList(new Object[] { 5, buyerObj.getName(), buyerObj }).iterator();
 purchase.mercenary(args);
 }

 @Test
 public void testMercenaryWithDifferentPlayer() {
 purchase.mercenary(args);
 assertNotEquals(knightObj, buyerObj);
 }

}