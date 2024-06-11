package jaw.entrada.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;

public class AbrirTest {

 private Abrir abrir;
 private ProcessarEntidades form;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 abrir = new Abrir(form, null);
 }

 @Test
 public void testAbrirXMLWithValidXmlFile() {
 String xmlFile = "testData/validXmlFile.jaw.xml";
 abrir = new Abrir(form, xmlFile);
 assertNotNull(abrir.getXml());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWithInvalidXmlFile() {
 String xmlFile = "testData/invalidXmlFile";
 abrir = new Abrir(form, xmlFile);
 fail("Should throw IllegalArgumentException with invalid xml file");
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirXMLWithMissingFile() {
 String xmlFile = "testData/missingFile.jaw.xml";
 abrir = new Abrir(form, xmlFile);
 fail("Should throw FileNotFoundException with missing file");
 }

 @Test
 public void testAbrirSerializadoWithValidJawFile() {
 String jawFile = "testData/validJawFile.jaw";
 abrir = new Abrir(form, Abrir.ABRIR, jawFile);
 assertNotNull(abrir.getXml());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoWithInvalidJawFile() {
 String jawFile = "testData/invalidJawFile";
 abrir = new Abrir(form, Abrir.ABRIR, jawFile);
 fail("Should throw IllegalArgumentException with invalid jaw file");
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirSerializadoWithMissingFile() {
 String jawFile = "testData/missingFile.jaw";
 abrir = new Abrir(form, Abrir.ABRIR, jawFile);
 fail("Should throw FileNotFoundException with missing file");
 }

 @Test
 public void testOpenFileDialogWithXmlFilter() {
 JFileChooser escolhedor = new JFileChooser();
 FileFilter xmlFilter = new Filtro2();
 escolhedor.setFileFilter(xmlFilter);
 File selectedFile = new File("testData/selectedXmlFile.jaw.xml");
 escolhedor.setSelectedFile(selectedFile);
 int returnValue = abrir.openFileDialog(escolhedor);
 assertEquals(JFileChooser.APPROVE_OPTION, returnValue);
 }

 @Test
 public void testOpenFileDialogWithJawFilter() {
 JFileChooser escolhedor = new JFileChooser();
 FileFilter jawFilter = new Filtro();
 escolhedor.setFileFilter(jawFilter);
 File selectedFile = new File("testData/selectedJawFile.jaw");
 escolhedor.setSelectedFile(selectedFile);
 int returnValue = abrir.openFileDialog(escolhedor);
 assertEquals(JFileChooser.APPROVE_OPTION, returnValue);
 }

 @Test
 public void testOpenFileDialogWithEmptySelection() {
 JFileChooser escolhedor = new JFileChooser();
 int returnValue = abrir.openFileDialog(escolhedor);
 assertEquals(JFileChooser.CANCEL_OPTION, returnValue);
 }

 @Test(expected = NullPointerException.class)
 public void testNullProcessarEntidades() {
 abrir = new Abrir(null, "testData/validXmlFile.jaw.xml");
 fail("Should throw NullPointerException with null ProcessarEntidades");
 }
}