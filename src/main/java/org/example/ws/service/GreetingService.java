package org.example.ws.service;

import java.util.Collection;

import org.example.ws.model.Greeting;
import org.example.ws.repository.GreetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class GreetingService implements IGreetingService {

	@Autowired
	private GreetingRepository greetingRepository;

	public Collection<Greeting> findAll() {
		return greetingRepository.findAll();
	}

	@Cacheable(value = "greeting", cacheManager = "cacheManager", cacheNames = "testing-cashe", key = "#id", unless = "#result==null")
	public Greeting findOne(Long id) {
		return greetingRepository.findOne(id);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CachePut(value = "greeting", cacheManager = "cacheManager", cacheNames = "testing-cashe", key = "#result.id", unless = "#result==null")
	public Greeting create(Greeting greeting) {
		if (greeting.getId() != null) {
			// cannot create greeting for the same
			return null;
		}
		Greeting saveGreeting = greetingRepository.save(greeting);
		if (saveGreeting.getId() == 4L) {
			throw new RuntimeException("Run time to trigger roll back!");
		}
		return saveGreeting;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CachePut(value = "greeting", cacheManager = "cacheManager", cacheNames = "testing-cashe", key = "#greeting.id", unless = "#result==null")
	public Greeting update(Greeting greeting) {
		Greeting persistantGreeting = findOne(greeting.getId());
		if (persistantGreeting == null) {
			// can not update as greeting for spacified id does not exist
			return null;
		}
		Greeting saveGreeting = greetingRepository.save(greeting);

		return saveGreeting;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CacheEvict(value = "greeting", cacheManager = "cacheManager", cacheNames = "testing-cashe", key = "#id")
	public void delete(Long id) {
		greetingRepository.delete(id);
	}

	@CacheEvict(value = "greeting", cacheManager = "cacheManager", cacheNames = "testing-cashe", allEntries = true)
	public void evictCache() {

	}

}
