package com.dev.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "suggestion_info")

public class Suggestion {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int suggId;

	@Column(name = "user_id")
	private int userid;

	private String suggest;

	public int getSuggId() {
		return suggId;
	}

	public void setSuggId(int suggId) {
		this.suggId = suggId;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	@Override
	public String toString() {
		return "Suggestion [suggId=" + suggId + ", userid=" + userid + ", suggest=" + suggest + "]";
	}

}
