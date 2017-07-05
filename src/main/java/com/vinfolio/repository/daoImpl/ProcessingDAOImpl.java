package com.vinfolio.repository.daoImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.vinfolio.services.entity.MappingBean;
import com.vinfolio.services.entity.TransactionBean;
import com.vinfolio.utils.HibernateUtil;

import antlr.preprocessor.PreprocessorTokenTypes;

public class ProcessingDAOImpl implements PreprocessorTokenTypes{

	private Session session;

	public void buildSession() {

		/*
		 * SessionFactory sessionFactory = new
		 * Configuration().configure().buildSessionFactory(); this.session =
		 * sessionFactory.openSession(); session.beginTransaction();
		 */

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		this.session = sessionFactory.openSession();
		session.beginTransaction();

	}

	public void destroySession() {
		this.session.close();
	}

	public void createEntryInTransactionTable() {

		this.buildSession();

		// create entry in transaction table

		TransactionBean trans = new TransactionBean();
		trans.setCreatedBy("BatchUser");
		trans.setBatch_onDemand("batch");
		trans.setDate(new Date());
		trans.setStatus("Executing");
		session.save(trans);

		loadStaticMappingData();
		session.getTransaction().commit();

		this.destroySession();

	}

	/**
	 * Method is written to load the static fields mapping stored in Database
	 * table Mapping.
	 * 
	 * @Return Map
	 * 
	 */
	public static HashMap<String, String> objMappingFields = new HashMap<String, String>();

	public Map<String, String> loadStaticMappingData() {
		fetchLastSuccessfulExecution();
		if (objMappingFields.size() == 0) {

			this.buildSession();

			Query query = session.createQuery("select m from MappingBean m");

			for (MappingBean tempMap : (ArrayList<MappingBean>) query.list()) {
				objMappingFields.put(tempMap.getSourceField(), tempMap.getTargetField());
			}
			return objMappingFields;

		} else {
			return objMappingFields;
		}

	}

	public Date fetchLastSuccessfulExecution() {

		this.buildSession();
		Query query = session.createQuery("from TransactionBean txn where status = :status");

		query.setString("status", "success").uniqueResult();

		TransactionBean tempTransactionBean = (TransactionBean) query.list().get(0);

		// Pass the where condition for date comparison also to get the latest
		// record with status='success'. by adding query.SetString() method

		return tempTransactionBean.getDate();

	}

}
