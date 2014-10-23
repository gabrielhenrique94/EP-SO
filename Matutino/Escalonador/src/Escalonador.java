
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class Escalonador {
	private static BufferedWriter logfile;
	private static LinkedList<String> listaProcessosProntos = new LinkedList<String>();
	private static LinkedList<String> listaProcessosBloqueados = new LinkedList<String>();
	private static Map<String, BCP> tabelaProcessos = new HashMap<String, BCP>();
	private static BCP emExecucao = null;
	private static int time = 0;
	private static  int quantum;
	
	/**
	 * Funcao que faz todas as chamadas de inicializacao do escalonador.
	 */
	public static void escalonarProcessos() {
		tabelaProcessos.putAll(LeitorArquivos.carregarProcessos());
		listaProcessosProntos.addAll(tabelaProcessos.keySet());
		quantum = LeitorArquivos.carregarQuantum(); 
		logfile = LeitorArquivos.inicializaLogFile("log" + quantum);
		
		Set<String> processos = tabelaProcessos.keySet();
		for(String nome: processos){

			//Exemplo de como add log na file
			try {
				logfile.write(nome+'\n');
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LeitorArquivos.closeLogFile(logfile);
		
		escalonar();
		
	}

	private static void escalonar() {
		String nomeProcesso= null;
		
		while(tabelaProcessos.isEmpty()) {
			time = 0;
			if (!listaProcessosProntos.isEmpty()) {
				nomeProcesso = ((LinkedList<String>) listaProcessosProntos).removeFirst();
				emExecucao = tabelaProcessos.get(nomeProcesso);
				String info = null;
				while ( info==null && time < quantum) {
					String instrucao = emExecucao.getProximaInstrucao();
					info = executaInstrucao(instrucao);
				}
				
				
			} else if (!listaProcessosBloqueados.isEmpty()) {
				atualizarListaBloqueados();
			}			
		}
		
	}
	
	private static String executaInstrucao(String instrucao){
		String info = null;
		
		switch (instrucao) {
		case "COM":
			//executa o comando
			time++;
			break;
			
		case "E/S":
			//precesso de E/S
			if(time == 0){
				info = "Interrompendo "+emExecucao.getNomePrograma()+" após "+time+"instruçao (havia um comando antes da E/S)(havia apenas a E/S)";
			}else{
				info = "Interrompendo "+emExecucao.getNomePrograma()+" após "+time+"instruçao (havia "+time+" comando antes da E/S)";
			}
			break;
			
		case "SAIDA":
			//quandoo processo termina
			tabelaProcessos.remove(emExecucao.getNomePrograma());
			info = emExecucao.getNomePrograma()+" terminado. X="+emExecucao.getX()+". Y="+emExecucao.getY();
			break;
			
		default:
			//atribui��o
			if(instrucao.charAt(0)=='X'){
				emExecucao.setX(instrucao.charAt(2));
			}else{
				emExecucao.setY(instrucao.charAt(2));
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
		listaProcessosProntos.add(processo.getNomePrograma());
	}
	
	/**
	 * Cada vez que um Proceso e executado, a variavel espera de bloqueio 
	 * de todos os processos bloqueados deve ser atualizada, e se ja estiverem
	 * o tempo necessario devem passar para a lista de pronto.
	 */
	private static void atualizarListaBloqueados() {
		LinkedList<String> listaAuxiliar = (LinkedList<String>) listaProcessosBloqueados.clone();
		
		for(String processo_name: listaAuxiliar) {
			BCP processo = tabelaProcessos.get(processo_name);
			processo.setTempoEsperaBloqueio(processo.getTempoEsperaBloqueio() - 1);
			if(processo.getTempoEsperaBloqueio() == 0){
				listaProcessosBloqueados.remove(processo_name);
				passarBloqueadoParaPronto(tabelaProcessos.get(processo_name));
			}
		}
	}
	
	

}
