package Model;

import java.awt.TextArea;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;

import javafx.fxml.FXML;


public class Parse implements Runnable{
	
	@FXML
	private TextArea TxtArea;
	
	@Override
	public void run() {
		System.out.println("Começar conversão " + getHora());
		TxtArea.setText("Começar conversão " + getHora());
		do {

			String[] tarefa = ControllerQueue.getTarefa();

			if (tarefa != null) {
				String[] campos = tarefa;
				Json newJson = new Json();
			
				setCampos(campos, newJson);
				ControllerQueue.AddTarefaEscrever(new Gson().toJson(newJson));
			}

		} while (ControllerQueue.Acabou());
		ControllerQueue.setFinalizarConvert(false);
		System.out.println("Conversão finalizada " + getHora());
		TxtArea.setText("Conversão finalizada " + getHora());
	}
	private String getHora() {
		return new SimpleDateFormat("HH:mm:ss.SSS z").format(new Date(System.currentTimeMillis()));
	}

	
	private void setCampos(String[] campos, Json json) {
		json.setNumber(Long.parseLong(campos[0])); 
		json.setGender(campos[1]);
		json.setNameSet(campos[2]);
		json.setTitle(campos[3]);
		json.setGivenName(campos[4]);
		json.setSurname(campos[5]);
		json.setStreetAddress(campos[6]);
		json.setCity(campos[7]);
		json.setState(campos[8]);
		json.setZipCode(campos[9]);
		json.setCountryFull(campos[10]);
		json.setEmailAddress(campos[11]);
		json.setUsername(campos[12]);
		json.setPassword(campos[13]);
		json.setTelephoneNumber(campos[14]);
		json.setBirthday(campos[15]);
		json.setCCType(campos[16]);
		json.setCCNumber(Long.parseLong(campos[17]));
		json.setCVV2(Integer.parseInt(campos[18]));
		json.setCCExpires(campos[19]);
		json.setNationalID(campos[20]);
		json.setColor(campos[21]);
		json.setKilograms(Double.parseDouble(campos[22]));
		json.setCentimeters(Integer.parseInt(campos[23]));
		json.setGUID(campos[24]);
	}
	
}
