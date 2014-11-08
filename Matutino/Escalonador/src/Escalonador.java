import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Escalonador {
	private static Estatistica estatistica = new Estatistica();
	
	private static BufferedWriter logfile;
	private static Queue <String> filaProcessosProntos = new LinkedList<String>();
	private static LinkedList <String> listaProcessosBloqueados = new LinkedList<String>();
	private static Map<String, BCP> tabelaProcessos = new HashMap<String, BCP>();
	private static BCP emExecucao = null;
	private static int time = 0;
	private static  int quantum;
	private static int PC;
	
	
	public static void printMap(){
		Set<String> processos = tabelaProcessos.keySet();
		for(String processo: processos){
			System.out.println("- "+processo+" -");
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
			System.out.println("- "+it.next()+" -" );
		}
	}

	
	/**
	 * Funcao que faz todas as chamadas de inicializacao do escalonador.
	 */
	public static void escalonarProcessos() {
		quantum = Arquivos.carregarQuantum(); 
		logfile = Arquivos.inicializaLogFile("log" + quantum);
		Arquivos.carregarProcessos(logfile, tabelaProcessos, filaProcessosProntos);
		estatistica.setNumeroDeProcessos(tabelaProcessos.size());
		printlist(filaProcessosProntos.iterator());
		
		System.out.println("\n ------------------------------------------------------------ \n \n");
		
		imprimeArquivos();	
		
		
		escalonar();
		Arquivos.escreveLog(logfile, "MEDIA DE TROCAS: "+String.format("%.2f", estatistica.mediaTrocas()));
		Arquivos.escreveLog(logfile, "MEDIA DE INSTRUCOES: "+String.format("%.2f", estatistica.mediaInstrucoes()));
		Arquivos.escreveLog(logfile, "QUANTUM: "+quantum);
		
		Arquivos.closeLogFile(logfile);
	}

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
				
				Boolean continua = true;
				while ( continua && time < quantum) {
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
						
					System.out.println(emExecucao.getNomePrograma()+" bloq");
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
				
				System.out.println(emExecucao.getNomePrograma()+" XouY  "+valor);
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
		processo.setAnteriormenteBloqueado(1);
		processo.setStatus(TipoStatus.PRONTO);
		processo.setTempoEsperaBloqueio(2);
		filaProcessosProntos.offer(processo.getNomePrograma());
	}
	
	/**
	 * Cada vez que um Proceso e executado, a variavel espera de bloqueio 
	 * de todos os processos bloqueados deve ser atualizada, e se ja estiverem
	 * o tempo necessario devem passar para a lista de pronto.
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

}
