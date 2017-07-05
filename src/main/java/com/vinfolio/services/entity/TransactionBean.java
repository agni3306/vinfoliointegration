package com.vinfolio.services.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Transaction")
public class TransactionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TransactionBean() {
	};

	public TransactionBean(String createdBy, String batch_onDemand, String status, String reason, Date date) {
		this.createdBy = createdBy;
		this.batch_onDemand = batch_onDemand;
		this.status = status;
		this.reason = reason;
		this.date = date;
	};

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transaction_pk;

	@Column(name = "createdBy")
	private String createdBy;

	@Column(name = "batch_onDemand")
	private String batch_onDemand;

	@Column(name = "status")
	private String status;

	@Column(name = "reason")
	private String reason;

	@Column(name = "date")
	private Date date;

	public Long getTransaction_pk() {
		return transaction_pk;
	}

	public void setTransaction_pk(Long transaction_pk) {
		this.transaction_pk = transaction_pk;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getBatch_onDemand() {
		return batch_onDemand;
	}

	public void setBatch_onDemand(String batch_onDemand) {
		this.batch_onDemand = batch_onDemand;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date2) {
		this.date = date2;
	}

}