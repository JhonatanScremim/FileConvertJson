package Socket;


import java.net.Socket;
import java.io.IOException;
import java.net.ServerSocket;

public class MainServer {
	public static void main(String[] args) {
		
		ServerSocket server = null;
		try{
			System.out.println("Começar servidor");
			server = new ServerSocket(9999);
			System.out.println("Servidor Pronto");
			while(true) {
				Socket cliente = server.accept();
				new Gerenciador(cliente);
			}
		}
		catch(IOException e) {
			try {
				if(server !=null) {
					server.close();
				}
			}
			catch(IOException el) {

				el.printStackTrace();
			}
			System.err.println("Erro");
			e.printStackTrace();
		}
	}
}
