package Model;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;


import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;

public class ControllerQueue implements Runnable{
	
	@FXML
	private static TextArea TxtArea;
	
	private List<String[]> ArquivoString;
	private static boolean FinalizarLeitura = true;
	private static boolean FinalizarConversao = true;
	
	private static List<String[]> FilaParse;
	private static List<String> FilaWrite;
	
	@FXML
	private static ProgressBar ProgressBarConvert;
	
	
	public static void FinalizarTudo() {
		TxtArea.appendText("Conversão finalizada: " + getHora());
	}
	public static void setFinalizarConvert(boolean convert) {
		FinalizarConversao = convert;
	}
	
	public static boolean getFinalizarConvert() {
		return FinalizarConversao;
	}
	Task task = new Task<Void>() {
		@Override
		public Void call() {
			final int max = 100000000;
			                
			for (int i = 1; i <= max; i++) {
				if (isCancelled()) {
					break;
				}
				updateProgress(i, max);

				
			}
			return null;
		}
    };
	public ControllerQueue(List<String[]> file, ProgressBar pgBar) {
		FilaParse = new Vector<String[]>();
		FilaWrite = new Vector<String>();
		this.ArquivoString = file;
		pgBar.progressProperty().bind(task.progressProperty());
		new Thread(task).start();
		
	}
	
	private void AddTarefa(String[] task) {
		FilaParse.add(task);
	}
	
	public static synchronized String[] getTarefa() {
		if (FilaParse.size() > 0)
			return FilaParse.remove(0);
		return null;
	}
	
	public static synchronized String getTarefaEscrever() {
		if (FilaWrite.size() > 0)
			return FilaWrite.remove(0);
		return null;
	}
	
	public static void AddTarefaEscrever(String task) {
		FilaWrite.add(task);
	}
	
	public static boolean AcabouEscrever() {
		if(FinalizarLeitura) {
			return true;
		} 
		if(FinalizarConversao) {
			return true;
		} 
		return  !FilaParse.isEmpty();		
	}
	
	public static boolean Acabou() {
		if(FinalizarLeitura) {
			return true;
		}
		if(!FilaParse.isEmpty()) {
			return true;
		}
		else {
			return !FilaParse.isEmpty();
		}	
	}
	private void ReceberDados() {
		System.out.println("Começar leitura " + getHora());
		long start = System.currentTimeMillis();
		int quantTotalRegistro = ArquivoString.size();
		int quantRegistros = 0;
		do {
			AddTarefa(ArquivoString.get(quantRegistros));
			quantRegistros++;
		}while(quantRegistros < quantTotalRegistro );
		FinalizarLeitura = false;
		System.out.println("Leitura finalizada " + getHora());
		long finish = System.currentTimeMillis();
		System.out.printf("Tempo de leitura: " + "%.3f ms%n", (finish - start) / 1000d);

	}
	
	@Override
	public void run() {
		ReceberDados();
	}
	private static String getHora() {
		return new SimpleDateFormat("HH:mm:ss.SSS z").format(new Date(System.currentTimeMillis()));
	}

}
