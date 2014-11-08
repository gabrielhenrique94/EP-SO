/** Escola de Artes Ciências e Humanidades
 * da Universidade de Sao Paulo (EACH-USP)
 * 
 * Curso de Sistemas de Informação (Matutino)
 * 2 Semestre de 2014
 * 
 * Primeiro Exercício Programa (EP1) 
 * Disciplina: Sistemas Operacionais 
 * 
 * @author Amandha Adulis
 * @author Gustavo Gamino
 * @author Heloisa Carbone
 * @author Julia Murano
 */

import java.util.LinkedList;

/**
 * Bloco de Controle de Processo (BCP)
 * Armazenas as informações necessárias para o processo
 * após ser interrompido, voltar ao estado de execução.
 * 
 * Possui:
 * - Nome do Programa
 * - Contador de Programa (PC)
 * - 2x Registradores de uso geral (X e Y)
 * - O estado do processo (status)
 * - Ponteiro e lista com as instruções de rotina do programa
 * - Prioridade do processo
 */
public class BCP {

	//************************ CONSTRUTOR ************************//
	
	/**
	 * Construtor atribui valores padrão:
	 * > PC = 0;
	 * > Espera de E/S = 2;
	 * > Status do processo = PRONTO;  
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
     * Nome do processo definido pelo arquivo de entrada na primeira linha
     */
	private String nomePrograma;

	/**
	 * Contador de Programa (PC): Registrador de uso especifico
	 */
	private int PC = 0;

	/**
	 * X: Registrador de uso geral
	 */
	private int X;
	
	/**
	 * Y: Registrador de uso geral
	 */
	private int Y;

	/**
	 * Status atual no qual o processo se encontra.
	 * Pode ser classificado em BLOQUEADO, EXECUTANDO ou PRONTO.
	 */
    private TipoStatus status;
    
	/**
	 * Instruções que devem ser executadas para esse processo.
	 */
	public LinkedList<String> instrucoes = new LinkedList<String> ();

	/**
	 * Prioridade do processo (por padrão vale 1).
	 * Quanto maior o número, maior a prioridade.
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
	private int tempoEsperaBloqueio = 2;

	//********************* METODOS GETTERS E SETTERS *********************//
	
	/**
	 * Retorna o valor atual do registrador (X) do tipo inteiro.
	 * @return X
	 */
	public int getX() {
		return X;
	}

	/**
	 * Atribui valor ao registrador X.
	 * @param x - valor inteiro que será atribuido
	 */
	public void setX(int x) {
		X = x;
	}

	/**
	 * Retorna o valor atual do registrador (Y) do tipo inteiro.
	 * @return Y
	 */
	public int getY() {
		return Y;
	}

	/**
	 * Atribui valor ao registrador Y.
	 * @param y - valor inteiro que será atribuido
	 */
	public void setY(int y) {
		Y = y;
	}

	/**
	 * Retorna o valor atual Contador de Programa (PC) do tipo inteiro.
	 * @return PC
	 */
	public int getPC() {
		return PC;
	}

	/**
	 * Atribui valor ao Contador de Programa (PC).
	 * @param pc - valor inteiro que será atribuido
	 */
	public void setPC(int pc) {
		PC = pc;
	}

	/**
	 * Retorna a instrucao na linha i do arquivo do processo.
	 * @param i - indice da instrucao
	 * @return instrucao - de indice 'i' do processo
	 */
	public String getInstrucao(int i) {
		return instrucoes.get(i);
	}

	/**
	 * Retorna uma string com nome do programa.
	 * @return nomePrograma
	 */
	public String getNomePrograma() {
		return nomePrograma;
	}

	/**
	 * Defini um nome ao processo,
	 * funcionando como uma identificação única.
	 * @param nomePrograma
	 */
	public void setNomePrograma(String nomePrograma) {
		this.nomePrograma = nomePrograma;
	}

	/**
	 * Retorna a prioridade do processo.
	 * @return prioridade - Quanto maior o valor inteiro maior a prioridade.
	 */
	public int getPrioridade() {
		return prioridade;
	}
	
	/**
	 * Defini a prioridade do processo.
	 * @param prioridade - Quanto maior o valor inteiro maior a prioridade.
	 */
	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
	}
	
	/**
	 * Retorna estado atual do processo.
	 * @return status - Podendo ele ser EXECUTANDO, BLOQUEADO ou PRONTO
	 */
	public TipoStatus getStatus(){
		return status;
	}
	
	/**
	 * Atribui um novo estado ao processo.
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
