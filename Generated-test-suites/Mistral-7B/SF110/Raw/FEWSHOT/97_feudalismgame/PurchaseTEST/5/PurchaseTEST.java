package src.test.java;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Test;
import org.mockito.Mockito;

import src.*;
import src.actions.*;
import src.model.*;

public class PurchaseTest {

    @Test
    public void testPerform_EmptyArgs() {
        Purchase purchase = new Purchase();
        Collection args = Collections.emptyList();

        purchase.perform(args);

        // No exceptions thrown, so test passes
    }

    @Test(expected = SecurityException.class)
    public void testPerform_SecurityException() {
        Purchase purchase = new Purchase();
        Collection args = Arrays.asList("args");

        doThrow(new SecurityException()).when(purchase).getClass();

        purchase.perform(args);
    }

    @Test(expected = NoSuchMethodException.class)
    public void testPerform_NoSuchMethodException() {
        Purchase purchase = new Purchase();
        Collection args = Arrays.asList("args", "iterator");
        Method method = mock(Method.class);

        doThrow(new NoSuchMethodException()).when(purchase.getClass())
                .getMethod(eq("args").get(0), Iterator.class);

        purchase.perform(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerform_IllegalArgumentException() {
        Purchase purchase = new Purchase();
        Collection args = Arrays.asList("args", "iterator", (Iterator) mock(Iterator.class).stubReturn(null));

        doThrow(new IllegalArgumentException()).when(purchase).perform(eq("args").get(0));

        purchase.perform(args);
    }

    @Test(expected = IllegalAccessException.class)
    public void testPerform_IllegalAccessException() {
        Purchase purchase = new Purchase();
        Collection args = Arrays.asList("args", "iterator");
        Method method = mock(Method.class);

        doThrow(new IllegalAccessException()).when(method).invoke(eq(purchase), anyArray());

        purchase.perform(args);
    }

    @Test(expected = InvocationTargetException.class)
    public void testPerform_InvocationTargetException() {
        Purchase purchase = new Purchase();
        Collection args = Arrays.asList("args", "iterator");
        Method method = mock(Method.class);

        doThrow(new InvocationTargetException(new Exception())).when(method).invoke(eq(purchase), anyArray());

        purchase.perform(args);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testPerform_ClassNotFoundException() {
        Purchase purchase = new Purchase();
        Collection args = Arrays.asList("args", "iterator", "feudalism.Purchase");

        doThrow(new ClassNotFoundException("feudalism.Purchase")).when(purchase).getClass();

        purchase.perform(args);
    }

    @Test
    public void testVassal() {
        Purchase purchase = new Purchase();
        Iterator arg = mock(Iterator.class).stubReturn(new Object[]{"3", "John", "Fiefdom1"}).stubReturn(null);
        Fiefdoms fiefdom = mock(Fiefdoms.class).stubReturn(new Fiefdom("Fiefdom1", null)).when(Map.map).get("Fiefdom1");
        Knight buyerObj = mock(Knight.class).stubReturn(new Knight("John", "knight")).when(CurrentPlayers.currentPlayers).get("John");

        purchase.vassal(arg);

        verify(buyerObj).setTotalMoney(eq(3), eq(false));
        verify(fiefdom).setLoyalVassals(eq(3), eq(true));
    }

    @Test
    public void testVassal_FiefdomNotBelong() {
        Purchase purchase = new Purchase();
        Iterator arg = mock(Iterator.class).stubReturn(new Object[]{"3", "John", "Fiefdom2"});
        Fiefdoms fiefdom = mock(Fiefdoms.class).stubReturn(new Fiefdom("Fiefdom2", null)).when(Map.map).get("Fiefdom2");
        Knight buyerObj = mock(Knight.class).stubReturn(new Knight("John", "knight"));

        purchase.vassal(arg);

        verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom: Fiefdom2 owner: null");
    }

    @Test
    public void testVassal_King() {
        Purchase purchase = new Purchase();
        Iterator arg = mock(Iterator.class).stubReturn(new Object[]{"3", "John", "Fiefdom1"});
        Fiefdoms fiefdom = mock(Fiefdoms.class).stubReturn(new Fiefdom("Fiefdom1", null)).when(Map.map).get("Fiefdom1");
        Knight buyerObj = mock(Knight.class).stubReturn(new Knight("King", "king"));

        purchase.vassal(arg);

        verify(System.out, times(1)).println("You can only buy a castle for a fiefdom that belongs to you");
    }

    @Test
    public void testVassal_AlreadyOwner() {
        Purchase purchase = new Purchase();
        Iterator arg = mock(Iterator.class).stubReturn(new Object[]{"3", "John", "Fiefdom1"});
        Fiefdoms fiefdom = mock(Fiefdoms.class).stubReturn(new Fiefdom("Fiefdom1", new Knight("John", "knight")));
        Knight buyerObj = mock(Knight.class).stubReturn(new Knight("John", "knight"));

        purchase.vassal(arg);

        verify(System.out, times(1)).println("You cannot buy more vassals, you have insufficient funds");
    }

    @Test
    public void testCastleFiefdom() {
        Purchase purchase = new Purchase();
        Iterator arg = mock(Iterator.class).stubReturn(new Object[]{"3", "John", "Fiefdom1"});
        Fiefdoms fiefdom = mock(Fiefdoms.class).stubReturn(new Fiefdom("Fiefdom1", null)).when(Map.map).get("Fiefdom1");
        Knight buyerObj = mock(Knight.class).stubReturn(new Knight("John", "knight"));

        purchase.castleFiefdom(arg);

        verify(buyerObj).setTotalMoney(eq(75), eq(false));
        verify(fiefdom).buyFiefdomCastles(eq(3));
    }

    @Test
    public void testCastleFiefdom_FiefdomNotBelong() {
        Purchase purchase = new Purchase();
        Iterator arg = mock(Iterator.class).stubReturn(new Object[]{"3", "John", "Fiefdom2"});
        Fiefdoms fiefdom = mock(Fiefdoms.class).stubReturn(new Fiefdom("Fiefdom2", null)).when(Map.map).get("Fiefdom2");
        Knight buyerObj = mock(Knight.class).stubReturn(new Knight("John", "knight"));

        purchase.castleFiefdom(arg);

        verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom: Fiefdom2 owner: null");
    }

    @Test
    public void testCastleFiefdom_AlreadyCastle() {
        Purchase purchase = new Purchase();
        Iterator arg = mock(Iterator.class).stubReturn(new Object[]{"3", "John", "Fiefdom1"});
        Fiefdoms fiefdom = mock(Fiefdoms.class).stubReturn(new Fiefdom("Fiefdom1", new Knight("John", "knight"))).when(Map.map).get("Fiefdom1");
        Knight buyerObj = mock(Knight.class).stubReturn(new Knight("John", "knight"));

        purchase.castleFiefdom(arg);

        verify(System.out, times(1)).println("You cannot buy more Fiefdom castles, you have insufficient funds");
    }

    @Test
    public void testCastlePersonal() {
        Purchase purchase = new Purchase();
        Iterator arg = mock(Iterator.class).stubReturn(new Object[]{"3", "John", "Fiefdom1"});
        Fiefdoms fiefdom = mock(Fiefdoms.class).stubReturn(new Fiefdom("Fiefdom1", null)).when(Map.map).get("Fiefdom1");
        Knight buyerObj = mock(Knight.class).stubReturn(new Knight("John", "knight"));

        purchase.castlePersonal(arg);

        verify(buyerObj).setTotalMoney(eq(30), eq(false));
        verify(fiefdom).buyPersonalCastles(eq(3));
    }

    @Test
    public void testCastlePersonal_FiefdomNotBelong() {
        Purchase purchase = new Purchase();
        Iterator arg = mock(Iterator.class).stubReturn(new Object[]{"3", "John", "Fiefdom2"});
        Fiefdoms fiefdom = mock(Fiefdoms.class).stubReturn(new Fiefdom("Fiefdom2", null)).when(Map.map).get("Fiefdom2");
        Knight buyerObj = mock(Knight.class).stubReturn(new Knight("John", "knight"));

        purchase.castlePersonal(arg);

        verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom: Fiefdom2 owner: null");
    }

    @Test
    public void testCastlePersonal_AlreadyCastle() {
        Purchase purchase = new Purchase();
        Iterator arg = mock(Iterator.class).stubReturn(new Object[]{"3", "John", "Fiefdom1"});
        Fiefdoms fiefdom = mock(Fiefdoms.class).stubReturn(new Fiefdom("Fiefdom1", new Knight("John", "knight"))).when(Map.map).get("Fiefdom1");
        Knight buyerObj = mock(Knight.class).stubReturn(new Knight("John", "knight"));

        purchase.castlePersonal(arg);

        verify(System.out, times(1)).println("You cannot buy a Personal Castle for Counts or Dukes");
    }

    @Test
    public void testMercenary() {
        Purchase purchase = new Purchase();
        Iterator arg = mock(Iterator.class).stubReturn(new Object[]{"3", "John", "Knight1"}).stubReturn(new Object[]{"3", "King", "Knight1"});
        Knight buyerObj = mock(Knight.class).stubReturn(new Knight("John", "knight"));
        Knight knight = mock(Knight.class).stubReturn(new Knight("Knight1", "knight"));

        purchase.mercenary(arg);

        verify(buyerObj).setTotalMoney(eq(3), eq(false));
        verify(knight).addMercenaries(eq(3));
    }

    @Test
    public void testMercenary_InexistentKnight() {
        Purchase purchase = new Purchase();
        Iterator arg = mock(Iterator.class).stubReturn(new Object[]{"3", "John", "InexistentKnight"});
        Knight buyerObj = mock(Knight.class).stubReturn(new Knight("John", "knight"));

        purchase.mercenary(arg);

        verify(System.out, times(1)).println("Inexistent Knight");
    }

    @Test
    public void testMercenary_NotBuyer() {
        Purchase purchase = new Purchase();
        Iterator arg = mock(Iterator.class).stubReturn(new Object[]{"3", "John", "Knight1"}).stubReturn(new Object[]{"3", "Bob", "Knight1"});
        Knight buyerObj = mock(Knight.class).stubReturn(new Knight("John", "knight"));
        Knight knight = mock(Knight.class).stubReturn(new Knight("Knight1", "knight"));

        purchase.mercenary(arg);

        verify(System.out, times(1)).println("Inexistent Knight: Bob");
    }
}