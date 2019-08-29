package com.dev.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.dev.beans.Admin;
import com.dev.beans.Available;
import com.dev.beans.Bus;
import com.dev.beans.Suggestion;
import com.dev.beans.Ticket;
import com.dev.beans.User;
import com.dev.exception.RegisterException;

public class BusBookingJPAImpl implements BusBookingDAO {

	private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistanceUnit");

	@Override
	public Boolean createUser(User user) throws RegisterException {
		Boolean state = false;
		try {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			em.persist(user);
			em.getTransaction().commit();
			em.close();
			state = true;
		} catch (Exception e) {
			throw new RegisterException("Registration Exception Occured"); // throw custom exception

		}
		return state;
	}

	@Override
	public Boolean updateUser(User user) {
		Boolean state = false;
		try {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			User user1 = em.find(User.class, user.getUserId());
			if (user1.getUserPassword().equals(user.getUserPassword())) {
				user1.setUserName(user.getUserName());
				user1.setEmail(user.getEmail());
				user1.setContact(user.getContact());
			}
			em.getTransaction().commit();
			em.close();
			state = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return state;
	}

	@Override
	public Boolean deleteUser(int user_id, String password) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		User user = em.find(User.class, user_id);
		if (user.getUserPassword().equals(password)) {
			em.remove(user);
			em.getTransaction().commit();
			em.close();
			return true;
		}
		return false;
	}

	@Override
	public User loginUser(int user_id, String password) {
		User user = null;
		try {
			EntityManager em = emf.createEntityManager();
			TypedQuery<User> query = em.createQuery("from User u where userId= :id and userPassword= :passwd",
					User.class);
			query.setParameter("id", user_id);
			query.setParameter("passwd", password);
			List<User> users = query.getResultList();
			if (users.size() > 0) {
				user = users.get(0);
			}
			em.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User searchUser(int user_id) {
		EntityManager em = emf.createEntityManager();
		User user = em.find(User.class, user_id);
		em.close();
		return user;
	}

	@Override
	public Boolean createBus(Bus bus) {
		Boolean state = false;
		try {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			em.persist(bus);
			em.getTransaction().commit();
			em.close();
			state = true;
		} catch (Exception e) {
			System.out.println("Error occured");
		}
		return state;
	}

	@Override
	public Boolean updateBus(Bus bus) {
		Boolean state = false;
		try {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			Bus bus1 = em.find(Bus.class, bus.getBusId());
			bus1.setBusName(bus.getBusName());
			bus1.setBusType(bus.getBusType());
			bus1.setSource(bus.getSource());
			bus1.setDestination(bus.getDestination());
			bus1.setTotalSeats(bus.getTotalSeats());
			bus1.setPrice(bus.getPrice());
			em.getTransaction().commit();
			em.close();
			state = true;
		} catch (Exception e) {
			// Custom Exception
			e.printStackTrace();
		}
		return state;
	}

	@Override
	public Bus searchBus(int bus_id) {

		EntityManager em = emf.createEntityManager();
		Bus bus = em.find(Bus.class, bus_id);
		em.close();
		return bus;

	}

	@Override
	public Boolean deletebus(int bus_id) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Bus bus = em.find(Bus.class, bus_id);
		em.remove(bus);
		em.getTransaction().commit();
		em.close();
		return true;
	}

	@Override
	public Admin adminLogin(int admin_id, String password) {
		Admin admin = null;
		try {
			EntityManager em = emf.createEntityManager();
			TypedQuery<Admin> query = em.createQuery("from Admin a where adminId= :id and adminPassword= :passwd",
					Admin.class);
			query.setParameter("id", admin_id);
			query.setParameter("passwd", password);
			List<Admin> admins = query.getResultList();
			if (admins.size() > 0) {
				admin = admins.get(0);
			}
			em.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return admin;
	}

	@Override
	public Ticket bookTicket(Ticket ticket) {
		try {
			EntityManager em = emf.createEntityManager();
			TypedQuery<Available> query = em.createQuery("from Available a where availableDate= :adate and busid= :id",
					Available.class);

			int totalAvailSeats = checkAvailability(ticket.getBusid(), ticket.getJourneyDate());

			em.getTransaction().begin();
			em.persist(ticket);

			query.setParameter("adate", ticket.getJourneyDate());
			query.setParameter("id", ticket.getBusid());
			List<Available> avail = query.getResultList();
			if (avail.size() > 0) {
				Available available = avail.get(0);

				available.setAvailableSeats(totalAvailSeats - ticket.getNoofSeats());
			}
			em.getTransaction().commit();
			em.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ticket;
	}

	@Override
	public Boolean cancelTicket(int booking_id) {
		EntityManager em = emf.createEntityManager();
		TypedQuery<Available> query = em.createQuery("from Available a where busid=:bid", Available.class);

		em.getTransaction().begin();

		Ticket ticket = em.find(Ticket.class, booking_id);
		query.setParameter("bid", ticket.getBusid());
		em.remove(ticket); // delete ticket
		Available available = query.getSingleResult();
		if (available != null) {
			available.setAvailableSeats(available.getAvailableSeats() + ticket.getNoofSeats()); // updating available
																								// seats after
																								// cancelling tickets
			em.getTransaction().commit();
			em.close();
			return true;
		} else {
			return false;
		}

	}

	@Override
	public Ticket getTicket(int booking_id, int userid) {
		Ticket ticket = null;

		EntityManager em = emf.createEntityManager();
		TypedQuery<Ticket> query = em.createQuery("from Ticket t where bookingId= :bid and userid= :uid", Ticket.class);

		query.setParameter("bid", booking_id);
		query.setParameter("uid", userid);
		List<Ticket> tickets = query.getResultList();
		if (tickets.size() > 0) {
			ticket = tickets.get(0);
		}
		em.close();
		return ticket;
	}

	@Override
	public List<Available> checkAvailability(String source, String destination, Date date) {
		List<Available> availList = new ArrayList<Available>();
		List<Available> resulList = null;
		List<Bus> busList = null;
		try {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			TypedQuery<Bus> query = em
					.createQuery("from Bus b where source= :busSource and destination= :busDestination", Bus.class);
			query.setParameter("busSource", source);
			query.setParameter("busDestination", destination);
			busList = query.getResultList();
			TypedQuery<Available> availQuery = em
					.createQuery("from Available a where busid= :bid and availableDate= :aDate", Available.class);
			if (busList.size() > 0) {
				for (Bus bus : busList) {

					availQuery.setParameter("bid", bus.getBusId());
					availQuery.setParameter("aDate", date);

					resulList = availQuery.getResultList(); // get the result from table

					availList.addAll(resulList); // store the Availability in availList

				}
			}
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return availList;

	}

	@Override
	public Integer checkAvailability(int bus_id, Date date) {

		EntityManager em = emf.createEntityManager();
		TypedQuery<Integer> query = em.createQuery(
				"select a.availableSeats from Available a where busid= :id and availableDate= :date", Integer.class);
		query.setParameter("id", bus_id);
		query.setParameter("date", date);
		Integer availableSeats = query.getSingleResult();
		em.close();
		return availableSeats;

	}

	@Override
	public Boolean giveFeedback(Suggestion sugg) {
		Boolean state = false;
		try {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			em.persist(sugg); // persist is to insert details
			em.getTransaction().commit();
			em.close();
			state = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return state;
	}

	@Override
	public List<Suggestion> getAllSuggestions(Suggestion sugg) {
		EntityManager em = emf.createEntityManager();
		TypedQuery<Suggestion> query = em.createQuery("from Suggestion s", Suggestion.class);
		List<Suggestion> list = query.getResultList();
		if (list.size() > 0) {
			list.add(sugg);
		}
		return list;

	}

	@Override
	public Boolean setAvailability(Available available) {
		Boolean state = false;
		try {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			em.persist(available);
			em.getTransaction().commit();
			em.close();
			state = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return state;
	}

	@Override
	public List<Ticket> getAllTicket(int userId) {
		List<Ticket> ticketLi = null;
		try {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			TypedQuery<Ticket> query = em.createQuery("from Ticket t where userid= :uid", Ticket.class);
			query.setParameter("uid", userId);
			ticketLi = query.getResultList();
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ticketLi;
	}

}
