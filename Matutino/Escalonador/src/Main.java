import java.io.File;


public class Main {

	public static File folder = new File("src/processos_entrada/");
	static String temp = "";

	public static void main(String[] args) {
		Escalonador.escalonarProcessos();
	}

}
