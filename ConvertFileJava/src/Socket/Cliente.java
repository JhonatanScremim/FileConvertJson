package Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import application.Main;
import javafx.fxml.FXML;
import application.ControllerMain;

public class Cliente {
	
	public static void main(String[] args) {
		    
		try {
			final Socket cliente = new Socket("127.0.0.1",9999);
			//lendo mensagem do servidor
			new Thread() {
				@Override
				public void run() {
					try {
						new Thread() {
							@Override
							public void run() {
								Main.main(args);
							}
						}.start();
						
						BufferedReader leitor =new BufferedReader(new InputStreamReader(cliente.getInputStream()));
						while(true) {
							String msg = leitor.readLine();
							System.out.println("Servidor: " + msg);
						}
					} catch (IOException e) {
						System.out.println("Impossovel ler a mansagem do servidor");
						e.printStackTrace();
					}
				}
			}.start();
			
			
			//escrevendo para o servidor
			PrintWriter escritor = new PrintWriter(cliente.getOutputStream(), true);
			BufferedReader leitorTerminal = new BufferedReader(new InputStreamReader(System.in));
			String msgTerminal = "";
			while(true) {
				msgTerminal = leitorTerminal.readLine();
				if(msgTerminal == null || msgTerminal.length() == 0) {
					continue;
				}
				escritor.println(msgTerminal);
				if(msgTerminal.equalsIgnoreCase("::Sair")) {
					System.exit(0);
				}
			}
			
		}
		catch(UnknownHostException e) {
			System.out.println("o endereço passado é invalido");
			e.printStackTrace();
		}
		catch (IOException e) {
			System.out.println("O sistema é inválido");
			e.printStackTrace();
		}
	}
}
