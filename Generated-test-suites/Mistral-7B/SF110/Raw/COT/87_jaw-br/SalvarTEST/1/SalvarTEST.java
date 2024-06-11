package jaw.entrada;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JFileChooser.CANCEL_OPTION;

class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;
 private List<Entidade> entidades;
 private JFileChooser fileChooser;
 private File selectedFile;

 @BeforeEach
 void setUp() {
 entidades = new ArrayList<>();
 form = new ProcessarEntidades();
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 }

 @Test
 void testSalvarWithValidInput() {
 // Arrange
 entidades.add(new Entidade("Teste1", 10.0, "Teste1"));
 entidades.add(new Entidade("Teste2", 20.0, "Teste2"));

 // Act
 salvar.salvar(entidades, "teste.jaw");

 // Assert
 assertFalse(salvar.vela);
 assertNotNull(salvar.arquivo);
 assertEquals("teste.jaw", salvar.arquivo);
 }

 @Test
 void testSalvarWithNullEntidades() {
 // Arrange

 // Act
 salvar.salvar(null, "teste.jaw");

 // Assert
 assertFalse(salvar.vela);
 assertNull(salvar.arquivo);
 }

 @Test
 void testSalvarWithEmptyName() {
 // Arrange
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 form.setNomeArquivoSalvo("");

 // Act
 salvar.salvar(entidades, null);

 // Assert
 assertFalse(salvar.vela);
 assertNull(salvar.arquivo);
 }

 @Test
 void testSalvarWithFileChooserCancel() {
 // Arrange
 fileChooser = new JFileChooser();
 selectedFile = null;
 salvar = new Salvar(form, Salvar.SALVAR_XML);

 // Act
 when(fileChooser.showSaveDialog(form)).thenReturn(CANCEL_OPTION);
 salvar.salvar(entidades, fileChooser);

 // Assert
 assertFalse(salvar.vela);
 assertNull(salvar.arquivo);
 }

 @Test
 void testSalvarWithFileChooserSave() {
 // Arrange
 fileChooser = new JFileChooser();
 selectedFile = new File("teste.jaw");
 salvar = new Salvar(form, Salvar.SALVAR_XML);

 // Act
 when(fileChooser.showSaveDialog(form)).thenReturn(APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(selectedFile);
 salvar.salvar(entidades, fileChooser);

 // Assert
 assertTrue(salvar.vela);
 assertEquals("teste.jaw", salvar.arquivo);
 }

 @Test
 void testSalvarXmlWithValidInput() {
 // Arrange
 entidades.clear();
 entidades.add(new Entidade("Teste1", 10.0, "Teste1"));
 entidades.add(new Entidade("Teste2", 20.0, "Teste2"));
 String xmlExpected = "<Entidades>\n<Entidade>\n<Nome>Teste1</Nome>\n<Altura>10.0</Altura>\n<Descricao>Teste1</Descricao>\n</Entidade>\n<Entidade>\n<Nome>Teste2</Nome>\n<Altura>20.0</Altura>\n<Descricao>Teste2</Descricao>\n</Entidade>\n</Entidades>";
 salvar = new Salvar(form, Salvar.SALVAR);

 // Act
 when(fileChooser.showSaveDialog(form)).thenReturn(APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(new File("teste.jaw"));
 salvar.salvar(entidades, fileChooser);

 // Assert
 assertTrue(salvar.vela);
 assertEquals("teste.jaw", salvar.arquivo);
 String xmlActual = salvar.xml;
 assertEquals(xmlExpected, xmlActual);
 }

 @Test
 void testSalvarXmlWithNullEntidades() {
 // Arrange
 salvar = new Salvar(form, Salvar.SALVAR);

 // Act
 salvar.salvar(null, null);

 // Assert
 assertFalse(salvar.vela);
 assertNull(salvar.arquivo);
 assertNull(salvar.xml);
 }

 @Test
 void testSalvarXmlWithEmptyName() {
 // Arrange
 salvar = new Salvar(form, Salvar.SALVAR);
 form.setNomeArquivoSalvo("");

 // Act
 salvar.salvar(entidades, null);

 // Assert
 assertFalse(salvar.vela);
 assertNull(salvar.arquivo);
 assertNull(salvar.xml);
 }

 @Test
 void testSalvarXmlWithFileChooserCancel() {
 // Arrange
 salvar = new Salvar(form, Salvar.SALVAR);
 fileChooser = new JFileChooser();
 selectedFile = null;

 // Act
 when(fileChooser.showSaveDialog(form)).thenReturn(CANCEL_OPTION);
 salvar.salvar(entidades, fileChooser);

 // Assert
 assertFalse(salvar.vela);
 assertNull(salvar.arquivo);
 assertNull(salvar.xml);
 }

 @Test
 void testSalvarXmlWithFileChooserSave() {
 // Arrange
 salvar = new Salvar(form, Salvar.SALVAR);
 fileChooser = new JFileChooser();
 selectedFile = new File("teste.jaw");

 // Act
 when(fileChooser.showSaveDialog(form)).thenReturn(APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(selectedFile);
 salvar.salvar(entidades, fileChooser);

 // Assert
 assertTrue(salvar.vela);
 assertEquals("teste.jaw", salvar.arquivo);
 String xmlActual = salvar.xml;
 String xmlExpected = "<Entidades>\n<Entidade>\n<Nome>Teste1</Nome>\n<Altura>10.0</Altura>\n<Descricao>Teste1</Descricao>\n</Entidade>\n<Entidade>\n<Nome>Teste2</Nome>\n<Altura>20.0</Altura>\n<Descricao>Teste2</Descricao>\n</Entidade>\n</Entidades>";
 assertEquals(xmlExpected, xmlActual);
 }

 @AfterEach
 void tearDown() {
 form = null;
 salvar = null;
 entidades.clear();
 }
}