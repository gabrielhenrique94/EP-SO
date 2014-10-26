
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Escalonador {
	
	private static BufferedWriter logfile;
	private static Queue <String> filaProcessosProntos = new LinkedList<String>();
	private static Queue <String> filaProcessosBloqueados = new LinkedList<String>();
	private static Map<String, BCP> tabelaProcessos = new HashMap<String, BCP>();
	private static BCP emExecucao = null;
	private static int time = 0;
	private static  int quantum;
	
	public static void printMap(){
		Set<String> ordemInicial = tabelaProcessos.keySet();
		for(String ordem: ordemInicial){
			System.out.println("-"+ordem+"-"+tabelaProcessos.get(ordem).getNomePrograma() );
		}
	}
	
	public static void imprimeArquivos(){
		Set<String> arqs = tabelaProcessos.keySet();
		for(String arq: arqs){
			printlist((tabelaProcessos.get(arq)).instrucoes.iterator());
			System.out.println(" ------------------------------------------------------------");
		}
	}
	
	public static void printlist(Iterator<String> it){
		while(it.hasNext()){
			System.out.println("-"+it.next()+"-" );
		}
	}

	
	/**
	 * Funcao que faz todas as chamadas de inicializacao do escalonador.
	 */
	public static void escalonarProcessos() {
		quantum = Arquivos.carregarQuantum(); 
		logfile = Arquivos.inicializaLogFile("log" + quantum);
		tabelaProcessos.putAll(Arquivos.carregarProcessos(logfile, filaProcessosProntos));
		printlist(filaProcessosProntos.iterator());
		
		System.out.println("\n ------------------------------------------------------------ \n \n");
		
		imprimeArquivos();	
		
		
		escalonar();
		Arquivos.closeLogFile(logfile);
	}

	private static void escalonar() {
		String nomeProcesso;
		System.out.println("Escalonando");
		int x = 0;
		
		while(!tabelaProcessos.isEmpty() &&  x<40) {
			time = 0;
			if (!filaProcessosProntos.isEmpty()) {
				nomeProcesso = filaProcessosProntos.poll();
				emExecucao = tabelaProcessos.get(nomeProcesso);
				Arquivos.escreveLog(logfile, "Executando " + emExecucao.getNomePrograma());
				
				String info = null;
				while ( info==null && time < quantum) {

					String instrucao = emExecucao.getProximaInstrucao();
					info = executaInstrucao(instrucao);
					
				}
				
				if (info != null) 
					Arquivos.escreveLog(logfile, info);
				else{
					Arquivos.escreveLog(logfile, "Interrompendo " + emExecucao.getNomePrograma() + " após " + time + " instruções");
					
					
				
			} else {
				atualizarListaBloqueados();
			}			
			x++;
		}
		
	}
	
	private static boolean executaInstrucao(String instrucao) {
		Boolean continua = true;
		
		switch (instrucao) {
			case "COM":
				System.out.println("Teste "+emExecucao.getOrdemInicializacaoProcessor()+" COM");
				//executa o comando
				time++;
				break;
				
			case "E/S":
				
				if(emExecucao.getAnteriormenteBloqueado()==0){
						
					System.out.println(emExecucao.getNomePrograma()+" E/S");
					//processo de E/S
					
					emExecucao.setBloqueado(true);
					
					emExecucao.setAnteriormenteBloqueado(1);
						
					filaProcessosBloqueados.offer(emExecucao.getNomePrograma());
					
					continua = false;
				}
				else{
					time++;
					emExecucao.setAnteriormenteBloqueado(0);
				}
				break;
				
			case "SAIDA":
				//quandoo processo termina
				System.out.println(emExecucao.getNomePrograma()+" SAIDA");
				tabelaProcessos.remove(emExecucao.getNomePrograma());
				printMap();
				continua = false;
				break;
				
			default:
				//atribuicao
				
				System.out.println(emExecucao.getNomePrograma()+" XouY");
				String[] atribuicao = instrucao.split("=");
				if (atribuicao[0]=="X") {
					int x = Integer.parseInt(atribuicao[1]);
					System.out.println(+x);
					emExecucao.setX(x);
				} else {
					int y = Integer.parseInt(""+instrucao.charAt(2));
					System.out.println(y);
					emExecucao.setY(y);
				}
				time++;
				break;
		}
		
		return continua;
	}

	/**
	 * Como a lista de bloqueados tambem acaba funcionando como uma fila, 
	 * basta pegar o primeiro para passar para a lista de prontos em qualquer
	 * tipo de chamada para o mesmo.
	 */
	private static void passarBloqueadoParaPronto(BCP processo) {
		processo.setAnteriormenteBloqueado(0);
		processo.setBloqueado(false);
		processo.setTempoEsperaBloqueio(2);
		filaProcessosProntos.offer(processo.getNomePrograma());
	}
	
	/**
	 * Cada vez que um Proceso e executado, a variavel espera de bloqueio 
	 * de todos os processos bloqueados deve ser atualizada, e se ja estiverem
	 * o tempo necessario devem passar para a lista de pronto.
	 */
	private static void atualizarListaBloqueados() {
		Queue<String> filaAuxiliar = new LinkedList<String>();
		filaAuxiliar = ((LinkedList<String>) filaProcessosBloqueados).clone();
		
		for(Integer ordemProcesso: listaAuxiliar){
			BCP processo= tabelaProcessos.get(ordemProcesso);
			processo.setTempoEsperaBloqueio(processo.getTempoEsperaBloqueio() - 1);
			if(processo.getTempoEsperaBloqueio() == 0){
				listaProcessosBloqueados.remove(ordemProcesso);
				passarBloqueadoParaPronto(processo);
			}
		}
	}
	
	
	

}
