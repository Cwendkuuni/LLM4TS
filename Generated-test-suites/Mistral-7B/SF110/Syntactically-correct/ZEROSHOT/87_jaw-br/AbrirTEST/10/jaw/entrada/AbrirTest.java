package jaw.entrada.test;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.Test;
import jaw.entrada.Abrir;
import jaw.gui.ProcessarEntidades;
import javax.swing.filechooser.FileFilter;

public class AbrirTest {

 private ProcessarEntidades form;

 @Before
 public void setup() {
 form = new ProcessarEntidades();
 }

 @Test
 public void testAbrirXML() {
 String arquivoXML = "teste.jaw.xml";
 Abrir abrir = new Abrir(form, arquivoXML);
 assertTrue("xml should not be empty", !abrir.xml.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLInvalidArquivo() {
 String arquivoXML = "teste.txt";
 Abrir abrir = new Abrir(form, arquivoXML);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testAbrirSerializado() {
 String arquivoJaw = "teste.jaw";
 Abrir abrir = new Abrir(form, 20, arquivoJaw);
 assertTrue("xml should not be empty", !abrir.xml.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoInvalidArquivo() {
 String arquivoJaw = "teste.xml";
 Abrir abrir = new Abrir(form, 20, arquivoJaw);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testAbrirSerializadoUsingFileChooser() {
 File arquivo = new File("teste.jaw");
 Abrir abrir = new Abrir(form, 20, arquivo);
 assertTrue("xml should not be empty", !abrir.xml.isEmpty());
 }

 @Test(expected = IllegalStateException.class)
 public void testAbrirXMLUsingFileChooserReturnsNull() {
 JFileChooser escolhedor = new JFileChooser();
 Abrir abrir = new Abrir(form, 10, escolhedor);
 fail("Should throw IllegalStateException");
 }

 @Test
 public void testFiltros() {
 FileFilter filtroXML = new Abrir().new Filtro2();
 File fileXML = new File("teste.jaw.xml");
 assertTrue("File should be accepted by XML filter", filtroXML.accept(fileXML));

 FileFilter filtroJaw = new Abrir().new Filtro();
 File fileJaw = new File("teste.jaw");
 assertTrue("File should be accepted by JAW filter", filtroJaw.accept(fileJaw));

 File fileText = new File("teste.txt");
 assertFalse("File should be rejected by JAW filter", filtroJaw.accept(fileText));

 File fileFolder = new File("teste");
 assertFalse("Folder should be rejected by JAW filter", filtroJaw.accept(fileFolder));

 assertFalse("File should be rejected by XML filter if it's not XML", filtroXML.accept(fileText));
 assertFalse("Folder should be rejected by XML filter", filtroXML.accept(fileFolder));
 }
}