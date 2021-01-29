package com.ATMproject.SOA.Config;

import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
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

		// l'endpoint del ws viene esposto a questo url
		endpoint.publish("/ATMservice");

		// ------------ INPUT INTERCEPTOR: autenticazione, decifratura, verifica firma
		// --------

		Map<String, Object> inProps = new HashMap<>();

		// azioni che deve compiere l'input interceptor
		inProps.put(ConfigurationConstants.ACTION, "UsernameToken Encrypt Signature Timestamp");

		// autenticazione
		inProps.put(ConfigurationConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
		// PW_CALLBACK_CLASS fornisce info sulla password per un dato utente
		inProps.put(ConfigurationConstants.PW_CALLBACK_CLASS, UTPasswordCallback.class.getName());

		// decifratura
		inProps.put(ConfigurationConstants.DEC_PROP_FILE, "etc/serviceKeystore.properties");

		// verfica firma
		inProps.put(ConfigurationConstants.SIG_PROP_FILE, "etc/serviceKeystore.properties");

		// input interceptor usato per gestire una richiesta in ingresso
		WSS4JInInterceptor wssIn = new WSS4JInInterceptor(inProps);
		// configurazione dell'interceptor nell'endpoint
		endpoint.getInInterceptors().add(wssIn);

		// ----------- OUT INTERCEPTOR: cifratura, firma, timestamp ----------------
		Map<String, Object> outProps = new HashMap<>();

		// azioni che deve compiere l'output interceptor
		outProps.put(ConfigurationConstants.ACTION, "Timestamp Signature Encrypt");
		// PW_CALLBACK_CLASS fornisce info sulla password per un dato utente
		outProps.put(ConfigurationConstants.PW_CALLBACK_CLASS, UTPasswordCallback.class.getName());

		// cifratura
		outProps.put(ConfigurationConstants.ENC_PROP_FILE, "etc/serviceKeystore.properties");
		outProps.put(ConfigurationConstants.ENCRYPTION_USER, "myclientkey");
		// per verdere l'elemento SIgnature commentare la riga sotto
		outProps.put(ConfigurationConstants.ENCRYPTION_PARTS,
				"{Element}{http://www.w3.org/2000/09/xmldsig#}Signature;{Content}{http://schemas.xmlsoap.org/soap/envelope/}Body");

		// firma
		outProps.put(ConfigurationConstants.SIG_PROP_FILE, "etc/serviceKeystore.properties");
		outProps.put(ConfigurationConstants.SIGNATURE_USER, "myservicekey");
		outProps.put(ConfigurationConstants.SIGNATURE_PARTS,
				"{Element}{http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd}Timestamp;{Element}{http://schemas.xmlsoap.org/soap/envelope/}Body");

		// tempo di vita del timestamp (di default è 5 minuti)
		outProps.put("timeToLive", "30");

		// output interceptor usato per gestire i messaggi in uscita
		WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
		// configurazione dell'interceptor nell'endpoint
		endpoint.getOutInterceptors().add(wssOut);

		return endpoint;
	}

}
