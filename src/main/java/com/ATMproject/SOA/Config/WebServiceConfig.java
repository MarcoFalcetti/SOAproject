package com.ATMproject.SOA.Config;

import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.wss4j.common.ConfigurationConstants;
import org.apache.wss4j.dom.WSConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ATMproject.SOA.*;

@Configuration
public class WebServiceConfig {
	
	@Autowired
	private Bus bus;
	
	@Bean
	public Endpoint endpoint() {
		EndpointImpl endpoint = new EndpointImpl(bus, new ATMwsImpl());
		
		//l'endpoint del ws viene esposto a questo url
		endpoint.publish("/ATMservice");
		
		//hashmap che conterrà le specifiche per l'interceptor
		Map<String, Object> inProps = new HashMap<>();
		
		//set delle azioni che deve fare l'interceptor
		inProps.put(ConfigurationConstants.ACTION, ConfigurationConstants.USERNAME_TOKEN);
		//affinchè l'azione definita prima funzioni bisogna definire 2 parametri
		//di che tipo è la password
		inProps.put(ConfigurationConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
		//PW_CALLBACK_CLASS fornisce info sulla password per un dato utente
		//UTPasswordCallback deve essere creata come classe, estende l'interfaccia CallbackHandler
		inProps.put(ConfigurationConstants.PW_CALLBACK_CLASS, UTPasswordCallback.class.getName());
		
		//input interceptor usato per gestire una richiesta in ingresso
		WSS4JInInterceptor wssIn = new WSS4JInInterceptor(inProps);
		//configurazione dell'interceptor nell'endpoint
		endpoint.getInInterceptors().add(wssIn);
		
		
		//output interceptor usato per gestire i messaggi in uscita

		
		
		
		return endpoint;
	}

}



