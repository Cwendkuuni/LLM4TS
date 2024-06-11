package jaw.entrada.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 }

 // Test case for constructor with ProcessarEntidades and String argument
 @Test
 public void testAbrirWithString() {
 String arquivo = "test.jaw.xml"; // Replace with a valid .jaw.xml file
 Abrir expectedAbrir = new Abrir(form, arquivo);
 abrir = new Abrir(form, arquivo);
 assertEquals(expectedAbrir.xml, abrir.xml);
 }

 // Test case for constructor with ProcessarEntidades and int argument
 @Test
 public void testAbrirWithInt() {
 File file = new File("test.jaw"); // Replace with a valid .jaw file
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(file);
 Abrir expectedAbrir = new Abrir(form, Abrir.ABRIR);
 abrir = new Abrir(form, Abrir.ABRIR);
 assertEquals(expectedAbrir.xml, abrir.xml);
 }

 // Test case for abrirSerializado method with valid jaw file
 @Test
 public void testAbrirSerializadoWithValidJawFile() {
 File file = new File("test.jaw"); // Replace with a valid .jaw file
 abrir.abrirSerializado(file.getAbsolutePath());
 assertNotNull(abrir.form.getEntidades());
 }

 // Test case for abrirSerializado method with invalid jaw file
 @Test(expected = IOException.class)
 public void testAbrirSerializadoWithInvalidJawFile() {
 File file = new File("test.txt"); // Replace with an invalid file
 abrir.abrirSerializado(file.getAbsolutePath());
 }

 // Test case for abrirXML method with valid xml file
 @Test
 public void testAbrirXMLWithValidXmlFile() throws IOException {
 File file = new File("test.jaw.xml"); // Replace with a valid .jaw.xml file
 abrir.abrirXML(file.getAbsolutePath());
 assertNotNull(abrir.form.getEntidades());
 }

 // Test case for abrirXML method with invalid xml file
 @Test(expected = IOException.class)
 public void testAbrirXMLWithInvalidXmlFile() {
 File file = new File("test.txt"); // Replace with an invalid file
 abrir.abrirXML(file.getAbsolutePath());
 }

 // Test case for Filtro2 class
 @Test
 public void testFiltro2Accept() {
 File file = new File("test.jaw.xml"); // Replace with a valid .jaw.xml file
 assertTrue(new Abrir.Filtro2().accept(file));
 }

 // Test case for Filtro2 class with invalid file
 @Test
 public void testFiltro2AcceptInvalidFile() {
 File file = new File("test.txt"); // Replace with an invalid file
 assertFalse(new Abrir.Filtro2().accept(file));
 }

 // Test case for Filtro class
 @Test
 public void testFiltroAccept() {
 File file = new File("test.jaw"); // Replace with a valid .jaw file
 assertTrue(new Abrir.Filtro().accept(file));
 }

 // Test case for Filtro class with invalid file
 @Test
 public void testFiltroAcceptInvalidFile() {
 File file = new File("test.txt"); // Replace with an invalid file
 assertFalse(new Abrir.Filtro().accept(file));
 }

}