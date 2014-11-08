/** Escola de Artes Ciencias e Humanidades
 * da Universidade de Sao Paulo (EACH-USP)
 * 
 * Curso de Sistemas de Informacao (Matutino)
 * 2 Semestre de 2014
 * 
 * Primeiro Exercicio Programa (EP1) 
 * da disciplina de Sistemas Operacionais 
 * 
 * @author Amandha Adulis
 * @author Gustavo Gamino
 * @author Heloisa Carbone
 * @author Julia Murano
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Classe destinada a leitura de arquivos e criação Processos
 */
public class Arquivos {
	
	//************************** METODOS **************************//
	
	/**
	 * Carrega o valor inteiro do quantum que e disponibilizado em um arquivo dentro da 
	 * pasta 'src/processos' com o nome de 'quantum.txt'. 
	 * @return quantum - valor do quantum fornecido pelo arquivo de entrada
	 * Se for -1 houve um erro de leitura / execução no método
	 */
	public static int carregarQuantum() {
		try {			 
			int quantum;
 
			BufferedReader arquivoProcesso = new BufferedReader(new FileReader("src/processos/quantum.txt"));
 			quantum = Integer.parseInt(arquivoProcesso.readLine());

			arquivoProcesso.close();
			return quantum;
			
 		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro de leitura do valor do quantum ou carregamento do arquivo");
		} 
		
		return -1;
	}
	
	/**
	 * Carrega todos os arquivos de processos, e suas respectivas instruções,
	 * que se encontram na pasta 'src/processos' e atualiza a Lista de Processos Prontos 
	 * @param logfile - Arquivo de log
	 * @param tabela - Tabela de Processos do escalonador
	 * @param fila - Fila de Processos Prontos
	 */
	public static void carregarProcessos(BufferedWriter logfile,  Map<String, BCP> tabela, Queue<String> fila) {
		BCP aux;
		File folder = new File("src/processos/");

		for (int i = 1; i < folder.listFiles().length; i++) {
			String numProcesso;
			
			numProcesso = (i < 10) ? "0" + i : Integer.toString(i);
			
			LinkedList<String> instrucoes = lerProcesso("src/processos/" + numProcesso + ".txt");
			aux = null;
			if (instrucoes != null) {
				aux = new BCP(instrucoes.removeFirst(), instrucoes);
				escreveLog(logfile, "Carregando " + aux.getNomePrograma());
				tabela.put(aux.getNomePrograma(), aux);
				fila.offer(aux.getNomePrograma());
			}
		}
	}
	
	/**
	 * Le o arquivo que contem as instrucoes destinadas a um Processo, 
	 * e devolve-as numa lista ligada
	 * @param nomeProcesso
	 * @return processo - Lista ligada de instruções do processo
	 */
	public static LinkedList<String> lerProcesso (String nomeProcesso) {
		LinkedList<String> processo = new LinkedList<String>();
		 
		try {
 			String instrucao;
 
			BufferedReader arquivoProcesso = new BufferedReader(new FileReader(nomeProcesso));
 
			while ((instrucao = arquivoProcesso.readLine()) != null) {
				processo.add(instrucao);
			}
			
			arquivoProcesso.close();
			return processo;
 
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro de leitura das instrucoes do processo " + nomeProcesso);
		} 
		
		return null;
	}
	
	/**
	 * Cria a o arquivo de log no diretório "src/saida/" e retorna o buffer
	 * para realizar a escrita no arquivo.
	 * @param name - nome do arquivo ("log" como foi definido por padrão)
	 * @param quantum - valor do quantum
	 * @return out - BufferdWriter
	 */
	public static BufferedWriter inicializaLogFile(String name, int quantum) {
	    try {
	    	// Adiciona digito zero ('0') para quantum menor igual a 9,
	    	// mantendo nome do log com dois digitos
	    	name += quantum < 10 ? "0" + quantum : "" + quantum;
	    	
	    	BufferedWriter out = new BufferedWriter(new FileWriter("src/saida/" + name + ".txt"));
	    	return out;
 
	    } catch (IOException e) {  
	        e.printStackTrace();
	        System.out.println("Erro ao inicializar arquivo de log");
	    }
	    
		return null;  
	}
	
	/**
	 * Fecha o buffer de escrita do arquivo.
	 * @param file - arquivo que deverá ser fechado
	 */
	public static void closeLogFile(BufferedWriter file) {
		try {
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro ao fechar arquivo de log");
		}
	}
	
	/**
	 * Realiza a escrita ddo conteúdo da String 'info' 
	 * no arquivo definidopelo buffer de escrita da variável 'log'
	 * @param log - arquivo para escrita
	 * @param info - conteúdo que deverá ser escrito
	 */
	public static void escreveLog(BufferedWriter log, String info) {
		try {
			log.write(info + "\n");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro de escrita no arquivo de log");
		}
	}
}

