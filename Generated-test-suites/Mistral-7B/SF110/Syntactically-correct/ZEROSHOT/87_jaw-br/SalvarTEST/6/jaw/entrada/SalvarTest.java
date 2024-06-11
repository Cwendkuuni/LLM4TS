package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.*;
import org.junit.Test;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 salvar = new Salvar(form, Salvar.SALVAR);
 }

 @Test
 public void testSalvarWithValidFile() {
 // Arrange
 String expectedFilePath = "test/output/SalvarTest/SalvarTest_output.jaw";
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(new File(expectedFilePath));

 // Act
 salvar.setEscolhedor(escolhedor);
 salvar.salvar();

 // Assert
 assertTrue(salvar.getArquivo().equals(expectedFilePath));
}

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarWithNullFileChooser() {
 // Arrange
 salvar.setEscolhedor(null);

 // Act and Assert
 salvar.salvar();
}

 @Test
 public void testSalvarWithEmptyFileName() {
 // Arrange
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(new File(""));
 salvar.setEscolhedor(escolhedor);

 // Act
 salvar.salvar();

 // Assert
 assertNull(salvar.getArquivo());
}

 @Test
 public void testSalvarWithInvalidFileType() {
 // Arrange
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(new File("test/output/SalvarTest/SalvarTest_output.txt"));
 salvar.setEscolhedor(escolhedor);

 // Act and Assert
 salvar.salvar();
 assertNull(salvar.getArquivo());
}

 @Test
 public void testSalvarXMLWithValidFile() {
 // Arrange
 String expectedFilePath = "test/output/SalvarTest/SalvarTest_output.jaw.xml";
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(new File(expectedFilePath));

 // Act
 salvar.setEscolhedor(escolhedor);
 salvar.setTipoSalvar(Salvar.SALVAR_XML);
 salvar.salvar();

 // Assert
 assertTrue(salvar.getArquivo().endsWith(".jaw.xml"));
}

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarXMLWithNullFileChooser() {
 // Arrange
 salvar.setEscolhedor(null);
 salvar.setTipoSalvar(Salvar.SALVAR_XML);

 // Act and Assert
 salvar.salvar();
}

 @Test
 public void testSalvarXMLWithEmptyFileName() {
 // Arrange
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(new File(""));
 salvar.setEscolhedor(escolhedor);
 salvar.setTipoSalvar(Salvar.SALVAR_XML);

 // Act
 salvar.salvar();

 // Assert
 assertNull(salvar.getArquivo());
}

 @Test
 public void testSalvarXMLWithInvalidFileType() {
 // Arrange
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(new File("test/output/SalvarTest/SalvarTest_output.jaw"));
 salvar.setEscolhedor(escolhedor);
 salvar.setTipoSalvar(Salvar.SALVAR_XML);

 // Act and Assert
 salvar.salvar();
 assertNull(salvar.getArquivo());
}

 @Test
 public void testSaveXMLWithValidContent() {
 // Arrange
 String expectedXml = "<entidades>\n" +
         "<entidade>\n" +
         "<nome>Entidade1</nome>\n" +
         "</entidade>\n" +
         "<entidade>\n" +
         "<nome>Entidade2</nome>\n" +
         "</entidade>\n" +
         "</entidades>";
 Formas forma = new Formas();
 forma.setNome("Forma1");
 Entidade entidade1 = new Entidade("Entidade1");
 entidade1.setForma(forma);
 Entidade entidade2 = new Entidade("Entidade2");
 form.setEntidades(new ArrayList<>(Arrays.asList(entidade1, entidade2)));
 salvar.setTipoSalvar(Salvar.SALVAR_XML);

 // Act
 salvar.salvar();

 // Assert
 assertTrue(salvar.getXml().equals(expectedXml));
}

 @Test
 public void testSaveXMLWithEmptyList() {
 // Arrange
 salvar.setTipoSalvar(Salvar.SALVAR_XML);

 // Act
 salvar.salvar();

 // Assert
 assertTrue(salvar.getXml().isEmpty());
}

 @Test
 public void testSaveXMLWithNullList() {
 // Arrange
 salvar.setTipoSalvar(Salvar.SALVAR_XML);
 form.setEntidades(null);

 // Act
 salvar.salvar();

 // Assert
 assertTrue(salvar.getXml().isEmpty());
}
}