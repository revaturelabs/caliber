package com.revature.caliber.data;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.revature.caliber.beans.Address;

/**
 *
 * @author Christian Acosta
 *
 */
@Repository
public class AddressDAO {

	private static final Logger log = Logger.getLogger(AddressDAO.class);
	private SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Save an address to the database
	 *
	 * @param address
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(Address address) {
		log.debug("Saving Address " + address);
		sessionFactory.getCurrentSession().save(address);
	}

	/**
	 *
	 * @return a list of all addresses as String in the database
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<String> getAll() {
		log.debug("Fetching all Addresses");
		List<String> addresses = new ArrayList<>();
		List<Address> addList = sessionFactory.getCurrentSession().createQuery("FROM Address ORDER BY state").list();
		for (Address address : addList) {
			addresses.add(address.toString());
		}
		return addresses;
	}

	/**
	 * Find all locations.
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Address> findAll() {
		log.debug("Finding all locations");
		return sessionFactory.getCurrentSession().createCriteria(Address.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	/**
	 * Save location
	 *
	 * @param id
	 * @return the address with the specified id
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Address getAddressById(int id) {
		log.debug("Fetching address with id " + id);
		return (Address) sessionFactory.getCurrentSession().get(Address.class, id);
	}

	/**
	 * Updates a location in the database.
	 *
	 * @param location
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void update(Address toUpdate) {
		log.debug("Updating " + toUpdate);
		sessionFactory.getCurrentSession().saveOrUpdate(toUpdate);
	}
}