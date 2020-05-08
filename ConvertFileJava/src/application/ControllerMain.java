package application;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import Model.ControllerQueue;
import Model.Parse;
import Model.Writer;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ControllerMain {
	
		@FXML
		private TextField TxtCsv;

	    @FXML
	    private Button BtCsv;
	    
	    @FXML
	    private TextField TxtExit;

	    @FXML
	    private Button BtExit;

	    @FXML
	    private ProgressBar ProgressBarConvert;

	    @FXML
	    private Button BtConvert;
	    
	    @FXML
	    private ExecutorService Exec; 
	    
	    public static boolean comecou = false;
	    

	    private static final int quantidadeCpu = Runtime.getRuntime().availableProcessors();
	   
	    public String ProcurarArquivo() {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV", "*.csv"));
			File file = fileChooser.showOpenDialog(new Stage());	
			if(file != null) {
				return file.getAbsolutePath();	
			}
			return null;
			
		}
	    
	    public String ProcurarPasta() {
	    	DirectoryChooser directoryChooser = new DirectoryChooser();
	    	File file = directoryChooser.showDialog(new Stage());
			if(file != null) {
				return file.getAbsolutePath();	
			}
			return null;
		}
	    
	    public JsonArray LerArquivo(String arquivo) {
	    	JsonArray resultado = new JsonArray();
	    	
	    	try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
	    		
	    		String line;
	    		boolean flag = true;
	    		List<String> coluna = null;
	    		
	    		while ((line = leitor.readLine()) != null) {
	    			if (flag) {
	    				flag = false;
	    				
	    				coluna = Arrays.asList(line.split(","));
	    			} else {
	    				JsonObject obj = new JsonObject();
	    				List<String> pedacos = Arrays.asList(line.split(","));
	    				
	    				for (int i = 0; i < coluna.size(); i++) {
							obj.addProperty(coluna.get(i), pedacos.get(i));
						}
	    				
	    				resultado.add(obj);
	    			}
	    		}
	    		
	    		return resultado;
	    	} catch(FileNotFoundException fnfe) {
	            Alert error = new Alert(Alert.AlertType.ERROR);
	            error.setHeaderText("Erro");
	    		error.setContentText("Arquivo n�o encontrado!");
	    		error.showAndWait();
	            return null;
	        } catch(IOException io) {
	            Alert error = new Alert(Alert.AlertType.ERROR);
	            error.setHeaderText("Erro");
	    		error.setContentText("Erro ao ler o arquivo!");
	    		error.showAndWait();
	            return null;
	        }
	    }
	    
	    private void ComecarConvert() {
	    	Reader reader;
	    	
	    	try {
	    		reader = Files.newBufferedReader(Paths.get(TxtCsv.getText()));
	    		
	    		CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
	    		
	    		List<String[]> lines = csvReader.readAll();
	    		ProgressBarConvert.setProgress(-1);

	    	    Exec.execute(new ControllerQueue(lines, ProgressBarConvert));
	    	    Exec.execute(new Parse());
	    	    Exec.execute(new Writer(new FileWriter(TxtExit.getText() + "\\arquivo.json")));
	    	} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	    
	    public String ConverterArquivo(JsonArray dados) {
	    	Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
	    	return gson.toJson(dados);
	    }
	    
	    public Boolean SalvarArquivo(String conteudo, String NomeArquivo) {
	    	try {
	    		FileWriter escrever = new FileWriter(NomeArquivo);
	    		escrever.write(conteudo);
	    		escrever.close();
	    		return true;
	    	} catch (IOException e) {
	    		Alert error = new Alert(Alert.AlertType.ERROR);
	            error.setHeaderText("Erro");
	    		error.setContentText("Erro ao salvar o arquivo!");
	    		error.showAndWait();
	    		return false;
		    }
	    	
	    	
	    }
	    @FXML 
	    public void Iniciar() {
	    	comecou = true;
	    	ProgressBarConvert.setProgress(0);		
			Exec = Executors.newFixedThreadPool(quantidadeCpu);
	    	
	    	BtCsv.setOnMouseClicked((MouseEvent e)->{	
				TxtCsv.setText(ProcurarArquivo());
			});
	    	
	    	BtExit.setOnMouseClicked((MouseEvent e)->{	
	    		TxtExit.setText(ProcurarPasta());
			});
	    	
	    	BtConvert.setOnMouseClicked((MouseEvent e)->{	
	    		ProgressBarConvert.setVisible(true);
	    		ProgressBarConvert.setProgress(0);
	    		// progressBar.setProgress(0.05F);
	    		
	    		File file = new File(TxtCsv.getText());
	    		
	    		if(file.exists() && !TxtCsv.getText().equals("")) {
	    			ComecarConvert();
	    		}

	    		else {
	    			Alert error = new Alert(Alert.AlertType.ERROR);
	                error.setHeaderText("Erro");
	        		error.setContentText("Arquivo n�o encontrado!");
	        		error.showAndWait();
	    		}
	    	});
	    }
	    
	    

}
