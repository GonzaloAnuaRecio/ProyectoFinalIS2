package modelo.bean;

import dataAccess.DataAccessHibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


import org.primefaces.event.SelectEvent;

import businessLogic.BLFacadeImplementation;

//BETS
import dominio.Event;
import dominio.Question;

public class ConsultarApuesta {

	private Date fecha;
	private DataAccessHibernate da;
	private BLFacadeImplementation blfi;
	private String mensajeFeedback;
	
	 
	//gestionar partidos
	private Event evento;
	private static List<Event> eventosSeleccionados;
	
	//lista de preguntas de cada partido
	private Question question;
	private static List<Question> listaPreguntas;
	
	
	
	public ConsultarApuesta()
	{
			da = new DataAccessHibernate();
			blfi= new BLFacadeImplementation(da);
			eventosSeleccionados= new ArrayList<Event>();
			listaPreguntas= new ArrayList<Question>();
			fecha=new Date();
			
	}
	
	//evento de la fecha
	public void onDateSelect(SelectEvent event) {
		 setEventosSeleccionados(blfi.getEvents((Date)fecha));
	}
	
	//seleccionar partido
	public void onEventSelect(SelectEvent event) 
	{
		Event eventTemp=(Event)event.getObject();
		
		this.evento=extraerDB(eventTemp);
		if(this.evento != null) {
			this.listaPreguntas=this.evento.getQuestions();
		}
		else{
			System.out.println("Error al recuperar los eventos de la base de datos: ");
		}	
	}
	
	private Event extraerDB(Event et) {
		Iterator<Event> it= blfi.getEvents(fecha).iterator();
		Event e;
		while(it.hasNext()) {
			e=it.next();
			
			if(e.equals(et)) {
				return e;
			}
		}
		return null;
	}
	
	//getters y setters	
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Event getEvento() {
		return evento;
	}
	public List<Event> getEventosSeleccionados() {
		return eventosSeleccionados;
	}

	public void setEventosSeleccionados(List<Event> eventosSeleccionados) {
		this.eventosSeleccionados = eventosSeleccionados;
	}

	public void setEvento(Event evento) {
		this.evento = evento;
	}

	public DataAccessHibernate getDa() {
		return da;
	}

	public void setDa(DataAccessHibernate da) {
		this.da = da;
	}

	public BLFacadeImplementation getBlfi() {
		return blfi;
	}

	public void setBlfi(BLFacadeImplementation blfi) {
		this.blfi = blfi;
	}

	public String getMensajeFeedback() {
		return mensajeFeedback;
	}

	public void setMensajeFeedback(String mensajeFeedback) {
		this.mensajeFeedback = mensajeFeedback;
	}

	public List<Question> getListaPreguntas() {
		return listaPreguntas;
	}

	public void setListaPreguntas(List<Question> listaPreguntas) {
		this.listaPreguntas = listaPreguntas;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}	
}
