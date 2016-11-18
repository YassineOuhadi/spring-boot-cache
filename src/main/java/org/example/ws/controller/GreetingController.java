package org.example.ws.controller;

import java.util.Collection;

import org.example.ws.model.Greeting;
import org.example.ws.service.IGreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
	
	@Autowired
	private IGreetingService greetingService;
	
	@RequestMapping(
			value="/api/greetings",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<Collection<Greeting>> getGreetings() {
		Collection<Greeting> result = greetingService.findAll();
		return new ResponseEntity<Collection<Greeting>>(result,HttpStatus.OK);
	}
	
	@RequestMapping(
			value="/api/greetings/{id}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<Greeting> getGreeting(@PathVariable("id") Long id) {
		Greeting result = greetingService.findOne(id);
		if(result==null) {
			return new ResponseEntity<Greeting>(HttpStatus.NOT_FOUND); 
		}
		return new ResponseEntity<Greeting>(result,HttpStatus.OK);
	}
	
	
	@RequestMapping(
			value="/api/greetings",
			method=RequestMethod.POST,
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<Greeting> saveGreeting(@RequestBody Greeting greeting) {
		Greeting result = greetingService.create(greeting);
		if(result==null) {
			return new ResponseEntity<Greeting>(HttpStatus.INTERNAL_SERVER_ERROR); 
		}
		return new ResponseEntity<Greeting>(result,HttpStatus.OK);
	}
	
	
	@RequestMapping(
			value="/api/greetings/{id}",
			method=RequestMethod.PUT,
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<Greeting> updateGreeting(@RequestBody Greeting greeting,@PathVariable("id") Long id) {
		greeting.setId(id);
		Greeting result = greetingService.update(greeting);
		if(result==null) {
			return new ResponseEntity<Greeting>(HttpStatus.INTERNAL_SERVER_ERROR); 
		}
		return new ResponseEntity<Greeting>(result,HttpStatus.OK);
	}
	
	
	@RequestMapping(
			value="/api/greetings/{id}",
			method=RequestMethod.DELETE,
			produces=MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<Greeting> deleteGreeting(@PathVariable("id") Long id) {
		greetingService.delete(id);
		return new ResponseEntity<Greeting>(HttpStatus.OK);
	}

}
