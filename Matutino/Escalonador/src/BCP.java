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

import java.util.LinkedList;

/**
 * <b>Bloco de Controle de Processo (BCP)</b> <br />
 * Armazenas as informações necessárias para o processo <br />
 * após ser interrompido, voltar ao estado de execução. <br />
 * 
 * Possui:<br />
 * - Nome do Programa;<br />
 * - Contador de Programa (PC);<br />
 * - 2x Registradores de uso geral (X e Y);<br />
 * - O estado do processo (status);<br />
 * - Lista com as instruções de rotina do programa;<br />
 * - Prioridade do processo;<br />
 */
public class BCP {

	//************************ CONSTRUTOR ************************//
	
	/**
	 * Construtor atribui valores padrão:<br />
	 * - PC = 0;<br />
	 * - Espera de E/S = 2;<br />
	 * - Status do processo = PRONTO;<br />  
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

	//********************* VARIAVEIS GLOBAIS *********************//
	
    /**
     * Nome do processo definido pelo arquivo de entrada na primeira linha.<br />
     */
	private String nomePrograma;

	/**
	 * Contador de Programa (PC): Registrador de uso especifico.<br />
	 */
	private int PC = 0;

	/**
	 * X: Registrador de uso geral.<br />
	 */
	private int X;
	
	/**
	 * Y: Registrador de uso geral.<br />
	 */
	private int Y;

	/**
	 * Status atual no qual o processo se encontra.<br />
	 * Pode ser classificado em BLOQUEADO, EXECUTANDO ou PRONTO.<br />
	 */
    private TipoStatus status;
    
	/**
	 * Instruções que devem ser executadas para esse processo.<br />
	 */
	public LinkedList<String> instrucoes = new LinkedList<String> ();

	/**
	 * Prioridade do processo (por padrão vale 1).<br />
	 * Quanto maior o número, maior a prioridade.<br />
	 */
	private int prioridade = 1 ;

	/**
	 * Avisa se a instrucao anterior entrou para a conta do PC. Pois caso tenha<br />
	 * tido uma E/S a instrucao nao entra na contagem e o processo e bloqueado<br />
	 * 0 -> entrou para conta, 1 -> nao entrou para a conta.<br />
	 */
	private int anteriormenteBloqueado = 0;

	/**
	 * Quando o processo for bloqueado, ele deve esperar outros dois processos executarem para retornar.<br />
	 * A variavel e entao utilizada quando o processo e bloqueado.<br />
	 */
	private int tempoEsperaBloqueio = 2;

	//********************* METODOS GETTERS E SETTERS *********************//
	
	/**
	 * Retorna o valor atual do registrador (X) do tipo inteiro.<br />
	 * @return X
	 */
	public int getX() {
		return X;
	}

	/**
	 * Atribui valor ao registrador X.<br />
	 * @param x - valor inteiro que será atribuido
	 */
	public void setX(int x) {
		X = x;
	}

	/**
	 * Retorna o valor atual do registrador (Y) do tipo inteiro.<br />
	 * @return Y
	 */
	public int getY() {
		return Y;
	}

	/**
	 * Atribui valor ao registrador Y.<br />
	 * @param y - valor inteiro que será atribuido
	 */
	public void setY(int y) {
		Y = y;
	}

	/**
	 * Retorna o valor atual Contador de Programa (PC) do tipo inteiro.<br />
	 * @return PC
	 */
	public int getPC() {
		return PC;
	}

	/**
	 * Atribui valor ao Contador de Programa (PC).<br />
	 * @param pc - valor inteiro que será atribuido
	 */
	public void setPC(int pc) {
		PC = pc;
	}

	/**
	 * Retorna a instrucao na linha i do arquivo do processo.<br />
	 * @param i - indice da instrucao
	 * @return instrucao - de indice 'i' do processo
	 */
	public String getInstrucao(int i) {
		return instrucoes.get(i);
	}

	/**
	 * Retorna uma string com nome do programa.<br />
	 * @return nomePrograma
	 */
	public String getNomePrograma() {
		return nomePrograma;
	}

	/**
	 * Defini um nome ao processo,<br />
	 * funcionando como uma identificação única.<br />
	 * @param nomePrograma
	 */
	public void setNomePrograma(String nomePrograma) {
		this.nomePrograma = nomePrograma;
	}

	/**
	 * Retorna a prioridade do processo.<br />
	 * @return prioridade - Quanto maior o valor inteiro maior a prioridade.
	 */
	public int getPrioridade() {
		return prioridade;
	}
	
	/**
	 * Defini a prioridade do processo.<br />
	 * @param prioridade - Quanto maior o valor inteiro maior a prioridade.
	 */
	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
	}
	
	/**
	 * Retorna estado atual do processo.<br />
	 * @return status - Podendo ele ser EXECUTANDO, BLOQUEADO ou PRONTO
	 */
	public TipoStatus getStatus(){
		return status;
	}
	
	/**
	 * Atribui um novo estado ao processo.<br />
	 * @param tipo - Podendo ele ser EXECUTANDO, BLOQUEADO ou PRONTO.
	 */
	public void setStatus(TipoStatus tipo){
		status = tipo;
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

}
