
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Escalonador {
	
	private static BufferedWriter logfile;
	private static LinkedList <Integer> listaProcessosProntos = new LinkedList<Integer>();
	private static LinkedList <Integer> listaProcessosBloqueados = new LinkedList<Integer>();
	private static Map<Integer, BCP> tabelaProcessos = new HashMap<Integer, BCP>();
	private static BCP emExecucao = null;
	private static int time = 0;
	private static  int quantum;
	
	public static void printMap(){
		Set<Integer> ordemInicial = tabelaProcessos.keySet();
		for(Integer ordem: ordemInicial){
			System.out.println("-"+ordem+"-"+tabelaProcessos.get(ordem).getNomePrograma() );
		}
	}
	
	public static void printlist(Iterator<Integer> it){
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
		tabelaProcessos.putAll(Arquivos.carregarProcessos(logfile));
		listaProcessosProntos.addAll(tabelaProcessos.keySet());
		Collections.sort(listaProcessosProntos);
		printlist(listaProcessosProntos.iterator());
		
		
		
		escalonar();
		Arquivos.closeLogFile(logfile);
	}

	private static void escalonar() {
		Integer ordemProcesso= null;
		System.out.println("Escalonando");
		int x = 0;
		
		while(!tabelaProcessos.isEmpty() &&  x<40) {
			time = 0;
			if (!listaProcessosProntos.isEmpty()) {
				ordemProcesso = ((LinkedList<Integer>) listaProcessosProntos).removeFirst();
				emExecucao = tabelaProcessos.get(ordemProcesso);
				Arquivos.escreveLog(logfile, "Executando " + emExecucao.getNomePrograma());
				
				String info = null;
				while ( info==null && time < quantum) {

					String instrucao = emExecucao.getProximaInstrucao();
					info = executaInstrucao(instrucao);
					
				}
				
				if (info != null) 
					Arquivos.escreveLog(logfile, info);
				
				Arquivos.escreveLog(logfile, "Interrompendo " + emExecucao.getNomePrograma() + " após " + time + " instruções");
				
			} else {
				atualizarListaBloqueados();
			}			
			x++;
		}
		
	}
	
	private static String executaInstrucao(String instrucao) {
		String info = null;
		
		switch (instrucao) {
			case "COM":
				//executa o comando
				time++;
				break;
				
			case "E/S":
				//processo de E/S
				if(time < 2) 
					info = "Interrompendo " + emExecucao.getNomePrograma() + " após " + time + " instruçao";
				else
					info = "Interrompendo " + emExecucao.getNomePrograma() + " após " + time + " instruções";
				
				listaProcessosBloqueados.add(emExecucao.getOrdemInicializacaoProcessor());
				break;
				
			case "SAIDA":
				//quandoo processo termina
				System.out.println(emExecucao.getOrdemInicializacaoProcessor());
				tabelaProcessos.remove(emExecucao.getOrdemInicializacaoProcessor());
				printMap();
				
				break;
				
			default:
				//atribuicao
				System.out.println("DEFAULT: " + instrucao);
				if (instrucao.indexOf('X') != -1) {
					int x = Integer.parseInt(instrucao.replace("X=", ""));
					System.out.println(x);
					emExecucao.setX(x);
				} else {
					int y = Integer.parseInt(instrucao.replace("Y=", ""));
					System.out.println(y);
					emExecucao.setY(y);
				}
				time++;
				break;
		}
		
		return info;
	}

	/**
	 * Como a lista de bloqueados tambem acaba funcionando como uma fila, 
	 * basta pegar o primeiro para passar para a lista de prontos em qualquer
	 * tipo de chamada para o mesmo.
	 */
	private static void passarBloqueadoParaPronto(BCP processo) {
		processo.setAnteriormenteBloqueado(1);
		processo.setBloqueado(false);
		processo.setTempoEsperaBloqueio(2);
		listaProcessosProntos.add(processo.getOrdemInicializacaoProcessor());
	}
	
	/**
	 * Cada vez que um Proceso e executado, a variavel espera de bloqueio 
	 * de todos os processos bloqueados deve ser atualizada, e se ja estiverem
	 * o tempo necessario devem passar para a lista de pronto.
	 */
	private static void atualizarListaBloqueados() {
		LinkedList<Integer> listaAuxiliar = new LinkedList<Integer>();
		listaAuxiliar = (LinkedList<Integer>) listaProcessosBloqueados.clone();
		
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
