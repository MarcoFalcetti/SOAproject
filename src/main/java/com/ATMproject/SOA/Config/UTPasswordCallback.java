package com.ATMproject.SOA.Config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.wss4j.common.ext.WSPasswordCallback;

public class UTPasswordCallback implements CallbackHandler {

	//semplificazione: in memory database per lo storage di username e password corrispondenti
		//che possono essere utilizzate dai client
		private Map<String, String> passwords = new HashMap<>();
		
		//i client devono usare questi username e password altrimenti non
		//possono usare questa web application
		public UTPasswordCallback() {
			passwords.put("pippo", "segreto");
		}
		
		@Override
		public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
			//callbacks hanno lo username che arriva nella request, bisogna trovare
			//la password corretta (match) e settare la password nel callback relativa
			
			for(Callback callback : callbacks) {
				WSPasswordCallback passwordCallBack = (WSPasswordCallback) callback;
				//getIdentifier ritorna lo username poi prendiamo la password corrispondete
				//dall'hashmap
				String password = passwords.get(passwordCallBack.getIdentifier());
				//se l'utente ha una password nell'hashmap settiamo la password
				//nel passwordCallBack
				if(password != null) {
					passwordCallBack.setPassword(password);
					return;
				}
			}

		}


}
