package jaw.entrada;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jaw.gui.*;
import jaw.metamodelo.*;
import jaw.util.SerializacaoXML;

public class SalvarTest {

 private ProcessarEntidades form;
 private Salvar salvar;
 private List<Entidade> entidade;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 entidade = new ArrayList<Entidade>();
 }

 @Test
 public void testSalvarComArquivoValidoEEntidadesValidas() {
 // Cenário: Salvar com arquivo válido e entidades válidas
 salvar = new Salvar(form, Salvar.SALVAR);
 entidade.add(new Entidade("Entidade1", 1, "Tipo1", new Endereco("Rua1", "Bairro1", "Cidade1", "UF1", "CEP1")));
 salvar.entidade = entidade;
 JFileChooser escolhedor = new JFileChooser();
 File arquivo = new File("teste.jaw");
 escolhedor.setSelectedFile(arquivo);
 salvar.arquivo = arquivo.getAbsolutePath();
 salvar.vela = true;
 assertTrue(salvar.salvar(entidade, arquivo.getAbsolutePath()));
 }

 @Test
 public void testSalvarComArquivoValidoESsemEntidades() {
 // Cenário: Salvar com arquivo válido e sem entidades
 salvar = new Salvar(form, Salvar.SALVAR);
 JFileChooser escolhedor = new JFileChooser();
 File arquivo = new File("teste.jaw");
 escolhedor.setSelectedFile(arquivo);
 salvar.arquivo = arquivo.getAbsolutePath();
 salvar.vela = true;
 assertFalse(salvar.salvar(null, arquivo.getAbsolutePath()));
 }

 @Test(expected = Exception.class)
 public void testSalvarComArquivoInvalido() {
 // Cenário: Salvar com arquivo inválido
 salvar = new Salvar(form, Salvar.SALVAR);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(null);
 salvar.vela = false;
 salvar.arquivo = "";
 try {
 salvar.salvar(entidade, "");
 fail("Deve lançar exceção");
 } catch (Exception e) {
 //Testa se a exceção é lançada
 }
 }

 @Test(expected = Exception.class)
 public void testSalvarComEntidadesInvalidas() {
 // Cenário: Salvar com entidades inválidas
 salvar = new Salvar(form, Salvar.SALVAR);
 entidade.add(new Entidade("Entidade1", null, null, new Endereco("Rua1", "Bairro1", "Cidade1", "UF1", "CEP1")));
 salvar.entidade = entidade;
 JFileChooser escolhedor = new JFileChooser();
 File arquivo = new File("teste.jaw");
 escolhedor.setSelectedFile(arquivo);
 salvar.arquivo = arquivo.getAbsolutePath();
 salvar.vela = true;
 try {
 salvar.salvar(entidade, arquivo.getAbsolutePath());
 fail("Deve lançar exceção");
 } catch (Exception e) {
 //Testa se a exceção é lançada
 }
 }

 @Test
 public void testSalvarComArquivoValidoESemEntidades() {
 // Cenário: Salvar com arquivo válido e sem entidades
 salvar = new Salvar(form, Salvar.SALVAR);
 JFileChooser escolhedor = new JFileChooser();
 File arquivo = new File("teste.jaw");
 escolhedor.setSelectedFile(arquivo);
 salvar.arquivo = arquivo.getAbsolutePath();
 salvar.vela = true;
 assertFalse(salvar.salvar(null, arquivo.getAbsolutePath()));
 }

 @Test
 public void testSalvarComArquivoValidoESemArquivoSelecionado() {
 // Cenário: Salvar com arquivo válido e sem arquivo selecionado
 salvar = new Salvar(form, Salvar.SALVAR);
 JFileChooser escolhedor = new JFileChooser();
 salvar.vela = false;
 salvar.arquivo = "";
 try {
 salvar.salvar(entidade, "");
 fail("Deve não salvar");
 } catch (Exception e) {
 //Testa se a exceção é lançada
 }
 }

 @Test(expected = Exception.class)
 public void testSalvarComNomeInvalido() {
 // Cenário: Salvar com nome inválido
 salvar = new Salvar(form, Salvar.SALVAR);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(null);
 salvar.vela = false;
 salvar.arquivo = "";
 try {
 salvar.salvar(entidade, "");
 fail("Deve lançar exceção");
 } catch (Exception e) {
 //Testa se a exceção é lançada
 }
 }

 @Test
 public void testSalvarComArquivoValidoESemNomeArquivo() {
 // Cenário: Salvar com arquivo válido e sem nome de arquivo
 salvar = new Salvar(form, Salvar.SALVAR);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(null);
 salvar.vela = false;
 salvar.arquivo = "";
 try {
 salvar.salvar(entidade, "");
 fail("Deve não salvar");
 } catch (Exception e) {
 //Testa se a exceção é lançada
 }
 }

 @Test
 public void testSalvarXmlComArquivoValidoESsemEntidades() {
 // Cenário: Salvar xml com arquivo válido e sem entidades
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 JFileChooser escolhedor = new JFileChooser();
 File arquivo = new File("teste.jaw.xml");
 escolhedor.setSelectedFile(arquivo);
 salvar.arquivo = arquivo.getAbsolutePath();
 salvar.vela = true;
 assertFalse(salvar.salvar(null, arquivo.getAbsolutePath()));
 }

 @Test(expected = Exception.class)
 public void testSalvarXmlComArquivoInvalido() {
 // Cenário: Salvar xml com arquivo inválido
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(null);
 salvar.vela = false;
 salvar.arquivo = "";
 try {
 salvar.salvar(entidade, "");
 fail("Deve lançar exceção");
 } catch (Exception e) {
 //Testa se a exceção é lançada
 }
 }

 @Test
 public void testSalvarXmlComNomeInvalido() {
 // Cenário: Salvar xml com nome inválido
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(null);
 salvar.vela = false;
 salvar.arquivo = "";
 try {
 salvar.salvar(entidade, "");
 fail("Deve não salvar");
 } catch (Exception e) {
 //Testa se a exceção é lançada
 }
 }

 @Test
 public void testSalvarXmlComArquivoValidoEEntidadeValida() {
 // Cenário: Salvar xml com arquivo válido e entidade válida
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 Entidade entidade1 = new Entidade("Entidade1", "Tipo1", new Endereco("Rua1", "Bairro1", "Cidade1", "UF1", "CEP1"));
 salvar.entidade.add(entidade1);
 JFileChooser escolhedor = new JFileChooser();
 File arquivo = new File("teste.jaw.xml");
 escolhedor.setSelectedFile(arquivo);
 salvar.arquivo = arquivo.getAbsolutePath();
 salvar.vela = true;
 String xml = salvar.salvarXml(entidade, arquivo.getAbsolutePath());
 assertTrue(xml.matches(".*<entidades>.*<entidade>.*<id>.*<tipo>.*<endereco>.*<rua>.*<bairro>.*<cidade>.*<uf>.*<cep>.*</entidade>.*</entidades>"));
 }

 @Test(expected = IOException.class)
 public void testSalvarXmlComArquivoJaExistente() {
 // Cenário: Salvar xml com arquivo já existente
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 Entidade entidade1 = new Entidade("Entidade1", "Tipo1", new Endereco("Rua1", "Bairro1", "Cidade1", "UF1", "CEP1"));
 salvar.entidade.add(entidade1);
 File arquivo = new File("teste.jaw.xml");
 String xml = salvar.salvarXml(entidade, arquivo.getAbsolutePath());
 salvar.salvarXml(entidade, arquivo.getAbsolutePath());
 }

 @After
 public void tearDown() {
 form = null;
 salvar = null;
 entidade = null;
 }

}