package com.dev.service;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dev.beans.Admin;
import com.dev.beans.Available;
import com.dev.beans.Bus;
import com.dev.beans.Suggestion;
import com.dev.beans.Ticket;
import com.dev.beans.User;
import com.dev.dao.BusBookingDAO;
import com.dev.dao.BusBookingJPAImpl;

public class ServiceImpl implements Services {

	BusBookingDAO db = new BusBookingJPAImpl();

	public Boolean createUser(User user) {
		Boolean state = false;
		try {
			return db.createUser(user);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return state;
	}

	public Boolean updateUser(User user) {
		return db.updateUser(user);
	}

	public Boolean deleteUser(int user_id, String password) {

		return db.deleteUser(user_id, password);
	}

	public User loginUser(int user_id, String password) {
		return db.loginUser(user_id, password);
	}

	public User searchUser(int user_id) {
		return db.searchUser(user_id);
	}

	public Boolean createBus(Bus bus) {
		return db.createBus(bus);
	}

	public Boolean updateBus(Bus bus) {
		return db.updateBus(bus);
	}

	public Bus searchBus(int bus_id) {
		return db.searchBus(bus_id);
	}

	public Boolean deletebus(int bus_id) {
		return db.deletebus(bus_id);
	}

	public Admin adminLogin(int admin_id, String password) {
		return db.adminLogin(admin_id, password);
	}

	public Ticket bookTicket(Ticket ticket) {
		return db.bookTicket(ticket);
	}

	public Boolean cancelTicket(int booking_id) {
		return db.cancelTicket(booking_id);
	}

	public Ticket getTicket(int booking_id, int userid) {
		return db.getTicket(booking_id, userid);

	}

	public List<Available> checkAvailability(String source, String destination, Date date) {
		return db.checkAvailability(source, destination, date);
	}

	public Integer checkAvailability(int bus_id, Date date) {
		return db.checkAvailability(bus_id, date);
	}

	@Override
	public Boolean setAvailability(Available available) {
		return db.setAvailability(available);
	}

	public Boolean giveFeedback(Suggestion sugg) {
		return db.giveFeedback(sugg);
	}

	public List<Suggestion> getAllSuggestions(Suggestion sugg) {
		return db.getAllSuggestions(sugg);
	}

	public Integer regex(String id) {
		Pattern pat = Pattern.compile("\\d+");
		Matcher mat = pat.matcher(id);
		if (mat.matches()) {
			return Integer.parseInt(id);
		} else {
			return null;
		}

	}

	public String regexemail(String email) {
		Pattern pat = Pattern.compile("\\w+\\@\\w+\\.\\w+");
		Matcher mat = pat.matcher(email);
		if (mat.matches()) {
			return email;
		} else {
			return null;
		}

	}

	public Long regexcontact(String contact) {
		Pattern pat = Pattern.compile("\\d{10}");
		Matcher mat = pat.matcher(contact);
		if (mat.matches()) {
			return Long.parseLong(contact);
		} else {
			return null;
		}
	}

}
