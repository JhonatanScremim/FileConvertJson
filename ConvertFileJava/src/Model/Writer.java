package Model;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Writer implements Runnable{

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
		do {

			String tarefa = ControllerQueue.getTarefaEscrever();

			if (tarefa != null) {
				try {
					write.write(tarefa+ "\n");
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
	}
}
