package com.dev.beans;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "booking_info")

public class Ticket {

	@Id
	@Column(name = "booking_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int bookingId;

	@Temporal(TemporalType.DATE)
	private Date journeyDate;

	private int noofSeats;

	@Temporal(TemporalType.DATE)
	private Date dateTime;

	@Column(name = "bus_id")
	private int busid;

	@Column(name = "user_id")
	private int userid;

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public Date getJourneyDate() {
		return journeyDate;
	}

	public void setJourneyDate(Date journeyDate) {
		this.journeyDate = journeyDate;
	}

	public int getNoofSeats() {
		return noofSeats;
	}

	public void setNoofSeats(int noofSeats) {
		this.noofSeats = noofSeats;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public int getBusid() {
		return busid;
	}

	public void setBusid(int busid) {
		this.busid = busid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	@Override
	public String toString() {
		return "Ticket [bookingId=" + bookingId + ", journeyDate=" + journeyDate + ", noofSeats=" + noofSeats
				+ ", dateTime=" + dateTime + ", busid=" + busid + ", userid=" + userid + "]";
	}

}
