package Model;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;

public class ControllerQueue implements Runnable{
	
	private List<String[]> ArquivoString;
	private static boolean FinalizarLeitura = true;
	private static boolean FinalizarConversao = true;
	
	private static List<String[]> FilaParse;
	private static List<String> FilaWrite;
	
	@FXML
	private static ProgressBar ProgressBarConvert;
	
	
	public static void FinalizarTudo() {
		Platform.runLater(new Runnable() {
            @Override public void run() {
            	
            }
        });
	}
	public static void setFinalizarConvert(boolean convert) {
		FinalizarConversao = convert;
	}
	
	public static boolean getFinalizarConvert() {
		return FinalizarConversao;
	}
	
	public ControllerQueue(List<String[]> file, ProgressBar pgBar) {
		FilaParse = new Vector<String[]>();
		FilaWrite = new Vector<String>();
		this.ArquivoString = file;
		ControllerQueue.ProgressBarConvert = pgBar;
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
		int quantTotalRegistro = ArquivoString.size();
		int quantRegistros = 0;
		do {
			AddTarefa(ArquivoString.get(quantRegistros));
			quantRegistros++;
		}while(quantRegistros < quantTotalRegistro );
		FinalizarLeitura = false;
		System.out.println("Leitura finalizada " + getHora());
	}
	
	@Override
	public void run() {
		ReceberDados();
	}
	private String getHora() {
		return new SimpleDateFormat("HH:mm:ss.SSS z").format(new Date(System.currentTimeMillis()));
	}

}
