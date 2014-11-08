/** <p>Escola de Artes Ciencias e Humanidades
 * da Universidade de Sao Paulo (EACH-USP)</p>
 * <br />
 * <p>Curso de Sistemas de Informacao (Matutino)
 * 2 Semestre de 2014</p>
 * <br />
 * <p>Primeiro Exercicio Programa (EP1)
 * da disciplina de Sistemas Operacionais </p>
 * <br />
 * @author Amandha Adulis
 * @author Gustavo Gamino
 * @author Heloisa Carbone
 * @author Julia Murano
 */

import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Classe principal que realiza o escalonamento dos dados processos.<br />
 * <br />
 * Possui as seguintes Estruturas de Dados:<br />
 * - Fila de processos Prontos;<br />
 * - Lista de processos Bloqueados;<br />
 * - Tabela de processos com seus respectivos BCP's;<br />
 * - Processo em execução;<br />
 */
public class Escalonador {
	
	//********************* VARIAVEIS GLOBAIS *********************//
		
	/**
	 * Objeto para realizar estatísticas.<br />
	 */	
	private static Estatistica estatistica = new Estatistica();
	
	/**
	 * <b>Fila de Processos Prontos</b> para serem executados, <br />
	 * na espera de tempo do processador.<br />
	 */
	private static Queue <String> filaProcessosProntos = new LinkedList<String>();
	
	/**
	 * <b>Lista de Processos Bloqueados</b> na espera de recurso, <br />
	 * no caso dispositivo de E/S, para que possa continuar a ser executado.<br />
	 */
	private static LinkedList <String> listaProcessosBloqueados = new LinkedList<String>();
	
	/**
	 * <b>Table de Processos</b>,representa todos os processos que estão rodando simultanemanete.<br />
	 * Mantêm o nome (identificação) do processo, e suas variáveis de execução (BCP).<br />
	 */
	private static Map<String, BCP> tabelaProcessos = new HashMap<String, BCP>();
	
	/**
	 * <b>Bloco de Controle de Processo</b><br />
	 * Estrutura de dados com todas as instâncias <br />
	 * necessárias para execução do processo <br />
	 */
	private static BCP emExecucao = null;
	
	/**
	 * Buffer de escrita do arquivo de log<br />
	 */
	private static BufferedWriter logfile;
	
	/**
	 * Quantum utilizado pelo escalonador.<br />
	 * Valor atribuido de acordo com arquivo 'quantum.txt'<br />
	 */
	private static final int QUANTUM = Arquivos.carregarQuantum();
	
	/**
	 * Número de quantuns utilizados por um processo<br />
	 * a partir do momento em que foi selecionado para<br />
	 * que fosse executado até o fim do seu quantum ou<br />
	 * chamada ao sistema (E/S).<br />
	 */
	private static int time = 0;
	
	/**
	 * Contador de Programa (PC) que funciona como um ponteiro <br />
	 * para instrução onde o escalonador está a executar. <br />
	 */
	private static int PC;

	//************************** METODOS **************************//
	
	/**
	 * Realiza todas as chamadas de inicializacao do escalonador.<br />
	 * - Cria arquivo de log;<br />
	 * - Cria fila de processos prontos e a tabela de processos;<br />
	 * - Gera estatisticas;<br />
	 */
	private static void escalonarProcessos() {
		//quantum = Arquivos.carregarQuantum(); 
		logfile = Arquivos.inicializaLogFile("log", QUANTUM);
		Arquivos.carregarProcessos(logfile, tabelaProcessos, filaProcessosProntos);
		estatistica.setNumeroDeProcessos(tabelaProcessos.size());
		printlist(filaProcessosProntos.iterator());
		
		System.out.println("\n ------------------------------------------------------------ \n\n");
		
		//imprimeArquivos();	
		
		escalonar();
		
		Arquivos.escreveLog(logfile, "MEDIA DE TROCAS: " + String.format("%.2f", estatistica.mediaTrocas()));
		Arquivos.escreveLog(logfile, "MEDIA DE INSTRUCOES: " + String.format("%.2f", estatistica.mediaInstrucoes()));
		Arquivos.escreveLog(logfile, "QUANTUM: " + QUANTUM);
		Arquivos.closeLogFile(logfile);
	}

	/**
	 * Realiza o escalonamento, de acordo com o quantum que foi definido <br />
	 * e as diretrizes de ação para cada comando da arquitetura.<br />
	 * Manipula estruturas de dados e variáveis gerenciando-os.<br />
	 */
	private static void escalonar() {
		String nomeProcesso;
		System.out.println("Escalonando");
			
		while(!tabelaProcessos.isEmpty()) {
			time = 0;
			if (!filaProcessosProntos.isEmpty()) {
				nomeProcesso = filaProcessosProntos.poll();
				emExecucao = tabelaProcessos.get(nomeProcesso);
				emExecucao.setStatus(TipoStatus.EXECUTANDO);
				PC = emExecucao.getPC();
				Arquivos.escreveLog(logfile, "Executando " + emExecucao.getNomePrograma());
				
				printMap();
				
				Boolean continua = true;
				while (continua && time < QUANTUM) {
					String instrucao = emExecucao.getInstrucao(PC);
					continua = executaInstrucao(instrucao);
				}
				Arquivos.escreveLog(logfile, "Interrompendo " + emExecucao.getNomePrograma() + " apos " + time + " instrucoes");
	
				emExecucao.setPC(PC);
				if(!tabelaProcessos.containsKey(emExecucao.getNomePrograma())){
					Arquivos.escreveLog(logfile, emExecucao.getNomePrograma()+" terminado. X="+emExecucao.getX()+". Y="+emExecucao.getY());
					emExecucao = null;
				}else if(continua){
					emExecucao.setStatus(TipoStatus.PRONTO);
					filaProcessosProntos.offer(emExecucao.getNomePrograma());
					emExecucao = null;
				}
				estatistica.addInstrucoes(time);
			}
			
			atualizarListaBloqueados();		
		}
	}
	
	/**
	 * Executa a instrução na arquitetura definida, realizando o gerenciamento <br />
	 * das variáveis de execução, como PC, lista de status dos processos e etc.<br />
	 * @param instrucao<br />
	 * @return true - se executou a instrução |<br /> 
	 * 		   false - o processo realizou uma chamada a sistema (E/S) 
	 */
	private static boolean executaInstrucao(String instrucao) {
		Boolean continua = true;
		
		switch (instrucao) {
			case "COM":
				System.out.println(emExecucao.getNomePrograma()+" COM");
				//executa o comando
				PC++;
				time++;
				break;
				
			case "E/S":
				
				if(emExecucao.getAnteriormenteBloqueado()==0){
						
					System.out.println(emExecucao.getNomePrograma()+" Bloqueado");
					//processo de E/S
					
					emExecucao.setStatus(TipoStatus.BLOQUEADO);
					
					emExecucao.setAnteriormenteBloqueado(1);
					
					continua = false;
				}else{
					PC++;
					System.out.println(emExecucao.getNomePrograma()+" E/S");
					time++;
					emExecucao.setAnteriormenteBloqueado(0);
				}
				break;
				
			case "SAIDA":
				//quandoo processo termina
				System.out.println(emExecucao.getNomePrograma()+" SAIDA");
				tabelaProcessos.remove(emExecucao.getNomePrograma());
				//printMap();
				continua = false;
				time++;
				break;
				
			default:
				//atribuicao
				PC++;
				String[] atribuicao = instrucao.split("=");
				int valor = Integer.parseInt(atribuicao[1]);
				if (atribuicao[0]=="X") 
					emExecucao.setX(valor);
				else 
					emExecucao.setY(valor);
				
				System.out.println(emExecucao.getNomePrograma()+" " + atribuicao[0] +"="+valor);
				time++;
				break;
		}
		
		return continua;
	}

	/**
	 * Como a lista de bloqueados tambem acaba funcionando como uma fila, <br /> 
	 * basta pegar o primeiro para passar para a lista de prontos em qualquer <br />
	 * tipo de chamada para o mesmo.<br />
	 */
	private static void passarBloqueadoParaPronto(BCP processo) {
		processo.setAnteriormenteBloqueado(1);
		processo.setStatus(TipoStatus.PRONTO);
		processo.setTempoEsperaBloqueio(2);
		filaProcessosProntos.offer(processo.getNomePrograma());
	}
	
	/**
	 * Cada vez que um Proceso e executado, a variavel espera de bloqueio <br /> 
	 * de todos os processos bloqueados deve ser atualizada, e se ja estiverem <br />
	 * o tempo necessario devem passar para a lista de pronto.<br />
	 */
	private static void atualizarListaBloqueados() {
		LinkedList<String> listaAuxiliar = new LinkedList<String>(listaProcessosBloqueados);
				
		for(String nomeProcesso: listaAuxiliar){
			BCP processo= tabelaProcessos.get(nomeProcesso);
			processo.setTempoEsperaBloqueio(processo.getTempoEsperaBloqueio() - 1);
			if(processo.getTempoEsperaBloqueio() == 0){
				listaProcessosBloqueados.remove(nomeProcesso);
				passarBloqueadoParaPronto(processo);
			}
		}
		if(emExecucao != null){
			listaProcessosBloqueados.addLast(emExecucao.getNomePrograma());
			emExecucao = null;
		}
	}
	
	/**
	 * Escreve no console a Tabela de Processos <br />
	 */
	public static void printMap(){
		Set<String> processos = tabelaProcessos.keySet();
		if (processos.isEmpty()) return;
		System.out.println("\n" + " ========================= " + "\n" + "|   TABELA DE PROCESSOS   |" 
					+ "\n" + " ========================= ");
		for(String processo: processos){
			System.out.println("| > " + processo + ": " + tabelaProcessos.get(processo).getPC() 
					+ "/" + tabelaProcessos.get(processo).instrucoes.size() 
					+ ", " + tabelaProcessos.get(processo).getStatus() + " |");
		}
		System.out.println(" ========================= " + "\n");
	}
	
	/**
	 * Escreve no console as instruções de cada processo (rotina).<br />
	 */
	public static void imprimeArquivos(){
		Set<String> arqs = tabelaProcessos.keySet();
		for(String arq: arqs){
			printlist((tabelaProcessos.get(arq)).instrucoes.iterator());
			System.out.println(" ------------------------------------------------------------");
		}
	}
	
	/**
	 * Iterador utilizado para percorrer e escrever no console diferentes listas.<br />
	 * @param it - lista que será percorrida
	 */
	public static void printlist(Iterator<String> it){
		while(it.hasNext()){
			System.out.println("- "+it.next()+" -" );
		}
	}
	
	public static void main(String[] args) {
		escalonarProcessos();
	}

}

/**
 * Tipos de estado em que um processo pode ser <br />
 * classificado: BLOQUEADO, EXECUTANDO ou PRONTO.<br />
 */
enum TipoStatus {
	EXECUTANDO, BLOQUEADO, PRONTO,
}