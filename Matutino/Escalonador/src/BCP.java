import java.util.LinkedList;

/**
 * 
 * Representa um processo
 *
 */
public class BCP {
	  
    private TipoStatus status;
	
	private String nomePrograma;
	
	/**
	 * Contador do programa, que e um registrador de uso especifico
	 */
	private int PC = 0;
	
	/**
	 * Registrador de uso geral
	 */
	private int X;
	
	/**
	 * Registrador de uso geral
	 */
	private int Y;
	
	/**
	 * Instrucoes que devem ser executadas para esse processo
	 */
	public LinkedList<String> instrucoes = new LinkedList<String> ();
	
	/**
	 * Prioridade do processo
	 */
	private int prioridade = 1 ;
	
	/**
	 * Avisa se a instrucao anterior entrou para a conta do PC. Pois caso tenha 
	 * tido uma E/S a instrucao nao entra na contagem e o processo e bloqueado
	 * 0 -> entrou para conta, 1 -> nao entrou para a conta. 
	 */
	private int anteriormenteBloqueado = 0; 
	
	/**
	 * Quando o processo for bloqueado, ele deve esperar outros dois processos executarem para retornar.
	 * A variavel e entao utilizada quando o processo e bloqueado.
	 */
	private int tempoEsperaBloqueio;
	
	/**
	 * Construtor da classe.
	 * @param nomePrograma
	 * @param instrucoes
	 */
	public BCP(String nomePrograma, LinkedList<String> instrucoes) {
		this.nomePrograma = nomePrograma;
		PC = 0;
		tempoEsperaBloqueio = 2;
		anteriormenteBloqueado = 0;
		this.instrucoes = instrucoes;
		status = TipoStatus.PRONTO;
	}
	
	
	//*  Getters e Setters   *//
	
	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}
	
	public int getPC() {
		return PC;
	}

	public void setPC(int pc) {
		PC = pc;
	}
	
	public String getInstrucao(int i) {
		return instrucoes.get(i);
	}

	public int getTempoEsperaBloqueio() {
		return tempoEsperaBloqueio;
	}

	public void setTempoEsperaBloqueio(int esperaDoBloqueio) {
		this.tempoEsperaBloqueio = esperaDoBloqueio;
	}

	public int getAnteriormenteBloqueado() {
		return anteriormenteBloqueado;
	}

	public void setAnteriormenteBloqueado(int anteriormenteBloqueado) {
		this.anteriormenteBloqueado = anteriormenteBloqueado;
	}

	public String getNomePrograma() {
		return nomePrograma;
	}

	public void setNomePrograma(String nomePrograma) {
		this.nomePrograma = nomePrograma;
	}

	public int getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
	}

	public TipoStatus getStatus(){
		return status;
	}
	
	public void setStatus(TipoStatus tipo){
		status = tipo;
	}
	
}
