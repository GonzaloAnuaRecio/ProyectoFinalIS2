package dataAccess;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Query;
import org.hibernate.Session;

import HibernateUtil.HibernateUtil;
import configuration.UtilDate;
import dominio.Event;
import dominio.Question;
import exceptions.QuestionAlreadyExist;

public class DataAccessHibernate implements DataAccessHibernateInterface{

	public DataAccessHibernate() {
		initializeDB();
	}

	@Override
	public void initializeDB() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			
			session.beginTransaction();

			Calendar today = Calendar.getInstance();

			int month = today.get(Calendar.MONTH);
			month += 1;
			int year = today.get(Calendar.YEAR);
			if (month == 12) {
				month = 0;
				year += 1;
			}
			
			List result = session.createQuery("from Event").list();
			
			if(result.isEmpty()) {
				System.out.println("Creando eventos...");
				
				// Creamos eventos
				Event ev1 = new Event("Real Madrid - Barcelona", UtilDate.newDate(year, month, 17));
				Event ev2 = new Event("Lakers - Celtics", UtilDate.newDate(year, month, 17));
				Event ev3 = new Event("Federer vs. Nadal", UtilDate.newDate(year, month, 17));
				Event ev4 = new Event("Gran Premio de Mónaco", UtilDate.newDate(year, month, 17));
				Event ev5 = new Event("Campeonato League of Legends", UtilDate.newDate(year, month, 17));
				Event ev6 = new Event("Campeonato Mundial de Ajedrez", UtilDate.newDate(year, month, 17));
				Event ev7 = new Event("Castellon - Valencia", UtilDate.newDate(year, month, 17));

				Event ev8 = new Event("Vuelta a España", UtilDate.newDate(year, month, 1));
				Event ev9 = new Event("Alaves - Athletic", UtilDate.newDate(year, month, 1));
				Event ev10 = new Event("Djokovic vs. Nadal", UtilDate.newDate(year, month, 1));
				Event ev11 = new Event("Real Madrid - Mallorca", UtilDate.newDate(year, month, 1));
				Event ev12 = new Event("Baskonia - TAU", UtilDate.newDate(year, month, 1));
				Event ev13 = new Event("Valladolid - Osasuna", UtilDate.newDate(year, month, 1));

				Event ev14 = new Event("Baskonia - Juventud", UtilDate.newDate(year, month, 28));
				Event ev15 = new Event("Real Madrid - Juventus", UtilDate.newDate(year, month, 28));
				Event ev16 = new Event("PSG - PSV", UtilDate.newDate(year, month, 28));
				Event ev17 = new Event("Open de Australia", UtilDate.newDate(year, month, 28));

				
				System.out.println("Creando preguntas...");
				//Creamos preguntas
				//Guardamos la pregunta porque hay que hacer persist() y que se guarde en la base de datos
				Question q1 = ev1.addQuestion("Marcara Vinicius", 10);
				Question q2 = ev1.addQuestion("Habra empate", 1);
				
				Question q3 = ev2.addQuestion("Fallaran 3 triples", 100);
				Question q4 = ev2.addQuestion("Se cancela el partido", 200);
				
				Question q5 = ev10.addQuestion("Nadal gana", 20);
				Question q6 = ev10.addQuestion("Djokovic no ira", 310);
				
				Question q7 = ev14.addQuestion("Pau Gasol aparece", 54);
				Question q8 = ev14.addQuestion("Quedan 30-1", 23);
	   
				session.save(ev1);
				session.save(ev2);
				session.save(ev3);
				session.save(ev4);
				session.save(ev5);
				session.save(ev6);
				session.save(ev7);
				session.save(ev8);
				session.save(ev9);
				session.save(ev10);
				session.save(ev11);
				session.save(ev12);
				session.save(ev13);
				session.save(ev14);
				session.save(ev15);
				session.save(ev16);
				session.save(ev17);
				
				session.save(q1);
				session.save(q2);
				session.save(q3);
				session.save(q4);
				session.save(q5);
				session.save(q6);
				session.save(q7);
				session.save(q8);
				
				
				session.getTransaction().commit();
				System.out.println("Se ha creado la base de datos");
			}
			else {
				System.out.println("La base de datos ya estaba creada");
				session.getTransaction().rollback();
			}
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		
		
		
		
	}

	@Override
	public Question createQuestion(Event event, String question, float betMinimum) throws QuestionAlreadyExist {
		System.out.println(">> DataAccess: createQuestion=> event= "+event+" question= "+question+" betMinimum="+betMinimum);
		Question q = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			
			session.beginTransaction();
			
			Event ev = (Event) session.get(Event.class, event.getEventNumber());
			
			if (ev.DoesQuestionExists(question)) throw new QuestionAlreadyExist(ResourceBundle.getBundle("Etiquetas").getString("ErrorQueryAlreadyExist"));
				
			
			q = ev.addQuestion(question, betMinimum);
			session.save(ev);
			//Tambien debemos poner la pregunta por separado para que se guarde correctamente en la BBDD
			session.save(q);
			session.getTransaction().commit();			
		}
		catch (Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		
		return q;
		
	}

	@Override
	public List<Event> getEvents(Date date) {
		System.out.println(">> DataAccess: getEvents");
		
		//Es necesario declararlo fuera para que el return  no de conflictos
		List<Event> eventos= new ArrayList<Event>();		
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();

			
			Query q= session.createQuery("from Event where eventDate= :fecha");
			q.setParameter("fecha", date);
			List result=q.list(); 
			
			
			//Añadimos todos los resultados a la lista de respuesta
			Iterator<Event> it = result.iterator();
			while(it.hasNext()) {
				eventos.add(it.next());	
			}
			
			System.out.println("Eventos obtenidos: " + eventos.toString());
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return eventos;
	}

	@Override
	public List<Date> getEventsMonth(Date date) {
		System.out.println(">> DataAccess: getEventsMonth");
		List<Date> res = new ArrayList<Date>();

		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();

			Date firstDayMonthDate = UtilDate.firstDayMonth(date);
			Date lastDayMonthDate = UtilDate.lastDayMonth(date);

			Query q = session.createQuery("SELECT DISTINCT ev.eventDate FROM Event ev WHERE ev.eventDate BETWEEN :diaInicio and :diaFin ");
			q.setParameter("diaInicio", firstDayMonthDate);
			q.setParameter("diaFin", lastDayMonthDate);
			List<Date> dates = q.list();
			for (Date d : dates) {
				System.out.println(d.toString());
				res.add(d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public boolean existQuestion(Event event, String question) {
		System.out.println(">> DataAccess: existQuestion=> event= "+event+" question= "+question);
		boolean res=false;
		
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			
			Event ev = (Event) session.get(Event.class, event.getEventNumber());
			res = ev.DoesQuestionExists(question);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}

}
