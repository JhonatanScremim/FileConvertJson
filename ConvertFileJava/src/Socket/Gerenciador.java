package Socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;

import application.ControllerMain;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Gerenciador extends Thread {
    
	private Socket cliente;
	private String nomeCliente;
	private PrintWriter escritor;
	private BufferedReader leitor;
	private static final Map<String,Gerenciador> clientes = new HashMap<String,Gerenciador>();
	
	
	public Gerenciador(Socket cliente) {
		this.cliente = cliente;
		start();
	}
	
	@Override
	public void run() {
		
		try {
			
			leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
			escritor = new PrintWriter(cliente.getOutputStream(), true);
			escritor.println("por favor escreva seu nome");
			String msg = leitor.readLine();
			this.nomeCliente = msg;
			escritor.println("Olá " + msg);
			clientes.put(this.nomeCliente, this);
			
			while(true) {

				msg = leitor.readLine();
				if(msg.equalsIgnoreCase("::Sair")) {
					this.cliente.close();
				}
				else if(msg.toLowerCase().startsWith("::msg")){
					String nomeDestinatario = msg.substring(5, msg.length());
					System.out.println("Enviando para " + nomeDestinatario);
					Gerenciador destinatario = clientes.get(nomeDestinatario);
					if(destinatario == null) {
						escritor.println("Cliente informado não existe");
						
					}
					else {
						escritor.println("Digite uma mensagem para " + destinatario.getNomeCliente());
						destinatario.getEscritor().println(this.nomeCliente + "Disse: " + leitor.readLine());
					}
				}
				else {
					escritor.println(this.nomeCliente + " Você disse: " + msg);
				}
			}
			
		}
		catch(Exception e) {
			System.err.println("O cliente fechou a conexão");
			e.printStackTrace();
		}
		
		
		
	}
	public String getNomeCliente() {
		return nomeCliente;
	}
	public PrintWriter getEscritor() {
		return escritor;
	}
	public BufferedReader getLeitor() {
		return leitor;
	}
	
}
