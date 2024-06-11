import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jaw.entrada.Entidade;
import jaw.entrada.Filtro;
import jaw.entrada.Filtro2;
import jaw.entrada.ProcessarEntidades;
import jaw.entrada.Salvar;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SalvarTest {

 private ProcessarEntidades form;
 private Salvar salvar;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 form = Mockito.spy(new ProcessarEntidades());
 entidades = new ArrayList<>();
 }

 @Test
 public void testSalvarXmlWithValidFile() {
 entidades.add(new Entidade("Teste1", "Teste1", "Teste1"));
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 Mockito.doReturn(new File("test.jaw.xml")).when(form).getNomeArquivoSalvo();
 Mockito.doReturn(entidades).when(form).getEntidades();
 salvar.salvar(entidades, "test.jaw.xml");
 assertTrue(new File("test.jaw.xml").exists());
 }

 @Test
 public void testSalvarXmlWithInvalidFileExtension() {
 entidades.add(new Entidade("Teste1", "Teste1", "Teste1"));
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 Mockito.doReturn(new File("test.xml")).when(form).getNomeArquivoSalvo();
 Mockito.doReturn(entidades).when(form).getEntidades();
 salvar.salvar(entidades, "test.xml");
 assertNull(new File("test.xml"));
 }

 @Test
 public void testSalvarWithValidFile() {
 entidades.add(new Entidade("Teste1", "Teste1", "Teste1"));
 salvar = new Salvar(form, Salvar.SALVAR);
 Mockito.doReturn(new File("test.jaw")).when(form).getNomeArquivoSalvo();
 Mockito.doReturn(entidades).when(form).getEntidades();
 salvar.salvar(entidades, "test.jaw");
 assertTrue(new File("test.jaw").exists());
 }

 @Test
 public void testSalvarWithNullFile() {
 salvar = new Salvar(form, Salvar.SALVAR);
 Mockito.doReturn(null).when(form).getNomeArquivoSalvo();
 Mockito.doReturn(entidades).when(form).getEntidades();
 salvar.salvar(entidades, null);
 assertNull(new File("test.jaw"));
 }

 @Test(expected = NullPointerException.class)
 public void testSalvarWithEmptyList() {
 salvar = new Salvar(form, Salvar.SALVAR);
 Mockito.doReturn(new File("test.jaw")).when(form).getNomeArquivoSalvo();
 Mockito.doReturn(null).when(form).getEntidades();
 salvar.salvar(null, "test.jaw");
 }

 @Test
 public void testSalvarComoWithValidFile() {
 entidades.add(new Entidade("Teste1", "Teste1", "Teste1"));
 salvar = new Salvar(form, Salvar.SALVAR_COMO);
 Mockito.doReturn(new File("test.jaw")).when(form).getNomeArquivoSalvo();
 Mockito.doReturn(entidades).when(form).getEntidades();
 salvar.salvar(entidades, "test.jaw");
 assertTrue(new File("test.jaw").exists());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarComoWithInvalidTipoSalvar() {
 salvar = new Salvar(form, 40);
 Mockito.doReturn(new File("test.jaw")).when(form).getNomeArquivoSalvo();
 Mockito.doReturn(entidades).when(form).getEntidades();
 salvar.salvar(entidades, "test.jaw");
 }

 @Test
 public void testFileChooserWithXmlFilter() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 Filtro2 filter = new Filtro2();
 assertFalse(filter.accept(new File("test.jaw")));
 assertTrue(filter.accept(new File("test.jaw.xml")));
 }

 @Test
 public void testFileChooserWithJawFilter() {
 salvar = new Salvar(form, Salvar.SALVAR);
 Filtro filter = new Filtro();
 assertFalse(filter.accept(new File("test.jaw.xml")));
 assertTrue(filter.accept(new File("test.jaw")));
 }

 @Test
 public void testFileChooserWithNullFile() {
 salvar = new Salvar(form, Salvar.SALVAR);
 Filtro filter = new Filtro();
 assertFalse(filter.accept(null));
 }

 @Test
 public void testExportXmlWithValidFile() {
 entidades.add(new Entidade("Teste1", "Teste1", "Teste1"));
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 Mockito.doReturn(new File("test.jaw.xml")).when(form).getNomeArquivoSalvo();
 Mockito.doReturn(entidades).when(form).getEntidades();
 String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n<entidade>\n<classe>Teste1</classe>\n<nome>Teste1</nome>\n<descricao>Teste1</descricao>\n</entidade>\n</entidades>";
 Mockito.doReturn(xml).when(salvar).xml;
 try {
 salvar.salvarXml(entidades, "test.jaw.xml");
 String result = new String(new File("test.jaw.xml").getText());
 assertEquals(xml, result);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testExportXmlWithInvalidFile() {
 entidades.add(new Entidade("Teste1", "Teste1", "Teste1"));
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 Mockito.doReturn(new File("test.jaw.xml")).when(form).getNomeArquivoSalvo();
 Mockito.doReturn(entidades).when(form).getEntidades();
 String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n<entidade>\n<classe>Teste1</classe>\n<nome>Teste1</nome>\n<descricao>Teste1</descricao>\n</entidade>\n</entidades>";
 Mockito.doReturn(xml).when(salvar).xml;
 try {
 salvar.salvarXml(entidades, "test.jaw");
 } catch (IOException e) {
 assertTrue(e.getMessage().contains("test.jaw"));
 }
 }
}