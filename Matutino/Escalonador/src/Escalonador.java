
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class Escalonador {
	private static BufferedWriter logfile;
	private static List <String> listaProcessosProntos = new LinkedList<String>();
	private static List <String> listaProcessosBloqueados = new LinkedList<String>();
	private static Map<String, BCP> tabelaProcessos = new HashMap<String, BCP>();
	private static BCP emExecucao = null;
	private static int time = 0;
	private static  int quantum;
	
	public static void printMap(){
		Set<String> nomes = tabelaProcessos.keySet();
		for(String nome: nomes){
			System.out.println("-"+nome+"-"+tabelaProcessos.get(nome) );
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
		tabelaProcessos.putAll(LeitorArquivos.carregarProcessos());
		listaProcessosProntos.addAll(tabelaProcessos.keySet());
		Collections.sort(listaProcessosProntos);
		printlist(listaProcessosProntos.iterator());
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
		escalonar();
		LeitorArquivos.closeLogFile(logfile);
	}

	private static void escalonar() {
		String nomeProcesso= null;
		System.out.println("Escalonando");
		int x = 0;
		while(!tabelaProcessos.isEmpty()&&  x<40) {
			System.out.println("Ainda ha processos");
			time = 0;
			if (!listaProcessosProntos.isEmpty()) {
				System.out.println("tem processo pronto");
				nomeProcesso = ((LinkedList<String>) listaProcessosProntos).removeFirst();
				emExecucao = tabelaProcessos.get(nomeProcesso);
				try{
					System.out.println("Carregando "+emExecucao.getNomePrograma());
					logfile.write("Carregando "+emExecucao.getNomePrograma());
				}catch(IOException e) {
					e.printStackTrace();
				}
				String info = null;
				while ( info==null && time < quantum) {

					String instrucao = emExecucao.getProximaInstrucao();
					info = executaInstrucao(instrucao);
				}
				try{
					if(info != null){
						logfile.write(info);
					}else{
						System.out.println("Interrompendo "+emExecucao.getNomePrograma()+" após "+time+" instruções");
						logfile.write("Interrompendo "+emExecucao.getNomePrograma()+" após "+time+" instruções");
					}
				}catch(IOException e) {
					e.printStackTrace();
				}
				
			} else {
				atualizarListaBloqueados();
			}			
			x++;
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
				info = "Interrompendo "+emExecucao.getNomePrograma()+" após "+time+"instruÃ§ao (havia um comando antes da E/S)(havia apenas a E/S)\n";
			}else{
				info = "Interrompendo "+emExecucao.getNomePrograma()+" após "+time+"instruções (havia "+time+" comando antes da E/S)\n";
			}
			listaProcessosBloqueados.add(emExecucao.getNomePrograma());
			break;
			
		case "SAIDA":
			//quandoo processo termina
			tabelaProcessos.remove(emExecucao.getNomePrograma());
			info = emExecucao.getNomePrograma()+" terminado. X="+emExecucao.getX()+". Y="+emExecucao.getY()+"\n";
			break;
			
		default:
			//atribuiï¿½ï¿½o
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
		LinkedList<String> listaAuxiliar = (LinkedList<String>) ( (LinkedList<String>) listaProcessosBloqueados).clone();
		
		for(String nomeProcesso: listaAuxiliar){
			BCP processo= tabelaProcessos.get(nomeProcesso);
			processo.setTempoEsperaBloqueio(processo.getTempoEsperaBloqueio() - 1);
			if(processo.getTempoEsperaBloqueio() == 0){
				listaProcessosBloqueados.remove(nomeProcesso);
				passarBloqueadoParaPronto(processo);
			}
		}
	}
	
	

}
