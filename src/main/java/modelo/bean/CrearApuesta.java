package modelo.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;


import org.primefaces.event.SelectEvent;

import businessLogic.BLFacadeImplementation;
import dataAccess.DataAccessHibernate;
//BETS
import dominio.Event;
import dominio.Question;
import exceptions.EventFinished;
import exceptions.QuestionAlreadyExist;

public class CrearApuesta {

	private Date fecha;
	private DataAccessHibernate da;
	private String pregunta;
	private String apuestaMinima;
	private BLFacadeImplementation blfi;
	private String mensajeFeedback;
	
	 
	//gestionar partidos
	private Event evento;
	private static List<Event> eventosSeleccionados;
	
	//lista de preguntas de cada partido
	private Question question;
	private static List<Question> listaPreguntas;
	
	
	
	public CrearApuesta()
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
	
	
	
	//Patron para reconocer si la apuesta es un numero entero positivo con posibles decimales
	private Pattern p= Pattern.compile("[1-9]\\d*((\\.)\\d\\d?)?");
	
	//Creacion de la pregunta
	public void crearPregunta()
	{
		if (apuestaMinima.equals("") || pregunta.equals(""))
		{
			mensajeFeedback="Por favor, rellene ambos campos";
		}
		else if(evento==null) 
		{
			mensajeFeedback="Por favor, seleccione un evento";
		}
		else if(!(p.matcher(apuestaMinima).matches())) 
		{
			mensajeFeedback="Debe ser un numero positivo con máximo dos decimales (Si tiene decimales de debe escribir con .)";
		}		
		else
		{
			float apuestaMinimaNum= Float.parseFloat(apuestaMinima);			
			try {
				blfi.createQuestion(evento, pregunta, apuestaMinimaNum);				
				mensajeFeedback="Pregunta creada ";
			} catch (EventFinished e) {
				e.printStackTrace();
			} catch (QuestionAlreadyExist e) {
				mensajeFeedback="La pregunta ya existe para este evento";
			} catch (Exception e) {
				mensajeFeedback="La pregunta ya existe para este evento";
			}
		}
		limpiarCampos();
	}
	
	private void limpiarCampos(){
		this.apuestaMinima = null;
		this.pregunta = "";
	}
	
	
	
	//getters y setters
	public String getPregunta() {
		return pregunta;
	}
	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}
	
	public String getApuestaMinima() {
		return apuestaMinima;
	}
	public void setApuestaMinima(String apuestaMinima) {
		this.apuestaMinima = apuestaMinima;
	}
	
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

