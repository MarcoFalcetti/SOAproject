package com.ATMproject.SOA;

import java.math.BigInteger;
import java.util.ArrayList;

import java.util.List;

import org.apache.cxf.feature.Features;
import org.example.webserviceatm.CartaDebito;
import org.example.webserviceatm.ContoCorrente;
import org.example.webserviceatm.PrelievoRequestSchema;
import org.example.webserviceatm.PrelievoResponseSchema;
import org.example.webserviceatm.VersamentoRequestSchema;
import org.example.webserviceatm.VersamentoResponseSchema;
import org.example.webserviceatm.WebServiceATM;


@Features(features = "org.apache.cxf.feature.LoggingFeature")
public class ATMwsImpl implements WebServiceATM {

	List<ContoCorrente> contiCorrente = new ArrayList<>();
	List<CartaDebito> carteDebito = new ArrayList<>();

	public ATMwsImpl() {
		init();
	}

	public void init() {
		ContoCorrente contoCorrente = new ContoCorrente();
		CartaDebito cartaDebito = new CartaDebito();

		contoCorrente.setIdcontoCorrente(BigInteger.valueOf(1234));
		cartaDebito.setIdcartaDebito(BigInteger.valueOf(4567));

		contoCorrente.setSaldo(BigInteger.valueOf(1000));
		cartaDebito.setPIN(BigInteger.valueOf(0000));

		contoCorrente.setIdUtente(BigInteger.valueOf(0));
		cartaDebito.setIdUtente(BigInteger.valueOf(0));
		
		contoCorrente.setIdcartaDebito(cartaDebito.getIdcartaDebito());
		cartaDebito.setIdcontoCorrente(contoCorrente.getIdcontoCorrente());

		contiCorrente.add(contoCorrente);
		carteDebito.add(cartaDebito);
		
		ContoCorrente contoCorrente2 = new ContoCorrente();
		CartaDebito cartaDebito2 = new CartaDebito();

		contoCorrente2.setIdcontoCorrente(BigInteger.valueOf(2));
		cartaDebito2.setIdcartaDebito(BigInteger.valueOf(2));

		contoCorrente2.setSaldo(BigInteger.valueOf(1000));
		cartaDebito2.setPIN(BigInteger.valueOf(1234));

		contoCorrente2.setIdUtente(BigInteger.valueOf(1));
		cartaDebito2.setIdUtente(BigInteger.valueOf(1));
		
		contoCorrente2.setIdcartaDebito(cartaDebito2.getIdcartaDebito());
		cartaDebito2.setIdcontoCorrente(contoCorrente2.getIdcontoCorrente());

		contiCorrente.add(contoCorrente2);
		carteDebito.add(cartaDebito2);

	}

	@Override
	public PrelievoResponseSchema prelievo(PrelievoRequestSchema request) {
		CartaDebito cartaDebito = request.getCartaDebito();
		BigInteger pin = request.getPin();
		int importo = request.getImporto().intValue();

		PrelievoResponseSchema response = new PrelievoResponseSchema();
		response.setResult(false);
		
		int saldo = contiCorrente.get(0).getSaldo().intValue();
		
		System.out.println("saldo = " + saldo);
		System.out.println("importo = " + importo);
		
		//controllo associazione carta a conto corrente corrispondente tramite i rispettivi id
		//controllo id carta
		if(cartaDebito.getIdcontoCorrente().equals(contiCorrente.get(0).getIdcontoCorrente()) && 
				cartaDebito.getIdcartaDebito().equals(carteDebito.get(0).getIdcartaDebito()))
			//controllo pin carta
			if (pin.equals(carteDebito.get(0).getPIN()))
				//controllo disponibilità fondi
				if(saldo >= importo) {
					contiCorrente.get(0).setSaldo(BigInteger.valueOf(saldo - importo));
					System.out.println("nuovo saldo = " + contiCorrente.get(0).getSaldo().intValue());
					response.setResult(true);
				}
	
		return response;
	}

	@Override
	public VersamentoResponseSchema versamento(VersamentoRequestSchema request) {
		CartaDebito cartaDebito = request.getCartaDebito();
		BigInteger pin = request.getPin();
		int importo = request.getImporto().intValue();

		VersamentoResponseSchema response = new VersamentoResponseSchema();
		response.setResult(false);
		
		int saldo = contiCorrente.get(0).getSaldo().intValue();
		
		System.out.println("saldo = " + saldo);
		System.out.println("importo = " + importo);
		
		//controllo associazione carta a conto corrente corrispondente tramite i rispettivi id
		//controllo id carta
		if(cartaDebito.getIdcontoCorrente().equals(contiCorrente.get(0).getIdcontoCorrente()) && 
				cartaDebito.getIdcartaDebito().equals(carteDebito.get(0).getIdcartaDebito()))
			//controllo pin carta
			if (pin.equals(carteDebito.get(0).getPIN()))
				//controllo disponibilità fondi
					contiCorrente.get(0).setSaldo(BigInteger.valueOf(saldo + importo));
					System.out.println("nuovo saldo = " + contiCorrente.get(0).getSaldo().intValue());
					response.setResult(true);
	
		return response;
	}

}




