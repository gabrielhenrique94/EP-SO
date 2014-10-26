import java.util.ArrayList;

public class Estatistica {
	static private ArrayList<Integer> numeroDeInstrucoes = new ArrayList<>();
	private int numeroDeProcessos = 0;

	public void setNumeroDeProcessos(int numProcessos){
		numeroDeProcessos = numProcessos;
	}

	public void addInstrucoes(int instrucoes){
		numeroDeInstrucoes.add(instrucoes);
	}
	
	public double mediaInstrucoes(){
		int soma = 0;
		for(Integer xi: numeroDeInstrucoes){
			soma = soma+xi;
		}
		return (double) soma/numeroDeInstrucoes.size();
	}
	
	public double mediaTrocas(){
		return (double) (numeroDeInstrucoes.size() - 1)/numeroDeProcessos;
	}
}
