package Model;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;


public class Writer implements Runnable{
	
	@FXML
	private ProgressBar ProgresBarWrite;
	
	Task task = new Task<Void>() {
		@Override
		public Void call() {
			final int max = 100000000;
			int centena = 0;
			for (int i = 1; i <= max; i++) {
				if (isCancelled()) {
					break;
				}
				updateProgress(i, max);

				
			}
			return null;
		}
    };

	FileWriter write;
	public Writer(FileWriter wf) {
	  this.write = wf;
	}
	private String getHora() {
		return new SimpleDateFormat("HH:mm:ss.SSS z").format(new Date(System.currentTimeMillis()));
	}

	@Override
	public void run() {

		System.out.println("Escrevendo " + getHora());
		long start = System.currentTimeMillis();
		do {
			
			String tarefa = ControllerQueue.getTarefaEscrever();

			if (tarefa != null) {
				try {
					write.write(tarefa+ "\n");
					//ProgresBarWrite.progressProperty().bind(task.progressProperty());
		    		//new Thread(task).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		} while (ControllerQueue.AcabouEscrever());
		try {
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ControllerQueue.FinalizarTudo();
		System.out.println("Escrita finalizada " + getHora());
		long finish = System.currentTimeMillis();
		System.out.printf("Tempo de escrição: " + "%.3f ms%n", (finish - start) / 1000d);

	}
}
