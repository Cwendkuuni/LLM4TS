package com.allenstudio.ir.ui;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MainMenuTest {

    @Test
    public void testMainMenuConstruction() {
        String[] menus = {"File", "Edit", "View"};
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);

        List<JMenu> menusInMenuBar = Arrays.asList(menu.getComponents());
        for (String menuName : menus) {
            JMenu menuItem = menusInMenuBar.stream().filter(jMenu -> jMenu.getText().equals(menuName)).findFirst().orElseThrow();
            assertNotNull(menuItem);
        }
    }

    @Test
    public void testCreateMenu_SimpleMenu() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        String[] menuItems = {"Item1", "Item2"};
        String menuName = "Menu";

        JMenu menu = menu.createMenu(menuName);
        assertNotNull(menu);

        List<JMenuItem> items = Arrays.asList(menu.getComponents());
        for (String itemName : menuItems) {
            JMenuItem item = items.stream().filter(jMenuItem -> jMenuItem.getText().equals(itemName)).findFirst().orElseThrow();
            assertNotNull(item);
        }
    }

    @Test
    public void testCreateMenu_SubMenu() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        String menuName = "Menu";
        String subMenuName = "SubMenu";

        JMenu menu = menu.createMenu(menuName);
        JMenu subMenu = menu.createMenu(subMenuName);

        menu.add(subMenu);
        List<JMenu> menusInMenuBar = Arrays.asList(menu.getComponents());
        JMenu menuItem = menusInMenuBar.stream()
                .filter(jMenu -> jMenu.getText().equals(menuName))
                .map(jMenu -> (JMenu) jMenu.getComponents()[0])
                .filter(jMenu -> jMenu.getText().equals(subMenuName))
                .findFirst()
                .orElseThrow();

        assertNotNull(menuItem);
    }

    @Test
    public void testCreateMenu_Separator() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        String menuName = "Menu";

        JMenu menu = menu.createMenu(menuName);
        List<Component> components = Arrays.asList(menu.getComponents());
        JMenu component = (JMenu) components.get(components.size() - 1);

        assertTrue(component instanceof JSeparator);
    }

    @Test
    public void testCreateMenu_CheckboxMenuItem() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        String menuName = "Menu";
        String itemName = "#CheckboxItem";

        JMenu menu = menu.createMenu(menuName);
        JCheckBoxMenuItem checkboxMenuItem = menu.createMenu(itemName);

        List<Component> components = Arrays.asList(menu.getComponents());
        JCheckBoxMenuItem component = (JCheckBoxMenuItem) components.get(components.size() - 1);

        assertNotNull(checkboxMenuItem);
        assertEquals(itemName, component.getText());
    }

    @Test
    public void testAddShortcutAndIcon_Shortcut() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        String itemName = "Item";
        String shortcut = "F1";

        JMenuItem item = Mockito.mock(JMenuItem.class);
        menu.addShortcutAndIcon(item, itemName);

        assertEquals(KeyEvent.VK_F1, item.getAccelerator().getKeyCode());
    }

    @Test
    public void testAddShortcutAndIcon_Icon() {
        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mediator);
        String itemName = "Item";
        String iconPath = "icon.png";

        JMenuItem item = Mockito.mock(JMenuItem.class);
        menu.addShortcutAndIcon(item, itemName);

        URL url = menu.getClass().getResource(iconPath);
        assertNotNull(url);
    }
}