package com.example.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.remondis.cdc.consumer.pactbuilder.ConsumerBuilder;
import com.remondis.cdc.consumer.pactbuilder.ConsumerBuilderImpl;
import com.remondis.cdc.consumer.pactbuilder.ConsumerExpects;
import com.remondis.resample.Samples;
import org.junit.Rule;
import org.junit.Test;

import au.com.dius.pact.core.model.annotations.Pact;
import org.mockito.Spy;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class DemoApplicationTests {

	@Rule
	public PactProviderRule provider = new PactProviderRule("JSONPlaceHolder", "localhost", 0, this);

	@Test
	@PactVerification(value = "JSONPlaceHolder", fragment = "getAllShouldHave10User")
	public void getAllUserShouldHave10UserInList() throws UnirestException {
		UserService userService = new UserService(provider.getUrl());

		List<User> users = userService.getAll();

		assertEquals(users.size(), 10);
	}

	@Pact(provider = "JSONPlaceHolder", consumer = "MyComputer")
	public RequestResponsePact getAllShouldHave10User(PactDslWithProvider builder) {
		return builder
				.given("All User is 10")
				.uponReceiving("a request for json data")
				.path("/users")
				.method("GET")
				.willRespondWith()
				.status(200)
				.body(
						PactDslJsonArray.arrayEachLike(10)
							.integerType("id", 3)
							.stringValue("name", "Clementine Bauch")
							.stringValue("username", "Samantha")
							.stringValue("email", "Nathan@yesenia.net")
							.closeObject()
				)
				.toPact();
	}

	@Test
	@PactVerification(value = "JSONPlaceHolder", fragment = "getAllShouldHave10Users")
	public void contextLoads() throws UnirestException {
		UserService userService = new UserService(provider.getUrl());

		List<User> users = userService.getAll();

		assertEquals(10, users.size());
	}

	@Spy
	private Supplier<PactDslJsonBody> supplier = new Supplier<PactDslJsonBody>() {

		@Override
		public PactDslJsonBody get() {
			return PactDslJsonArray.arrayEachLike(10);
		}
	};

	@Pact(provider = "JSONPlaceHolder", consumer = "Vipera")
	public RequestResponsePact getAllShouldHave10Users(PactDslWithProvider builder) {
		User bomb = new User();
		bomb.setId(new Random(10).nextLong());
		bomb.setName("Karan Sivarat");
		bomb.setEmail("bomb0069@gmail.com");
		bomb.setAddress(new Address());
		bomb.setCompany(new Company());


		PactDslJsonArray allUser = ConsumerExpects.collectionOf(User.class)
				.useArraySupplier(supplier)
				.arrayContent(ConsumerExpects.type(User.class))
				.build(bomb);

		return builder
				.given("All User is 10 with Real Object")
				.uponReceiving("a request for json data")
				.path("/users")
				.method("GET")
				.willRespondWith()
				.status(200)
				.body(allUser)
				.toPact();
	}

	@Test
	@PactVerification(value = "JSONPlaceHolder", fragment = "getAllShouldHave10UsersWithSample")
	public void getAllShouldHave10UsersWithSampleShouldBe10 () throws UnirestException {
		UserService userService = new UserService(provider.getUrl());

		List<User> users = userService.getAll();

		assertEquals(10, users.size());
	}

	@Pact(provider = "JSONPlaceHolder", consumer = "Vipera")
	public RequestResponsePact getAllShouldHave10UsersWithSample(PactDslWithProvider builder) {
		User bomb = new User();
		bomb.setId(new Random(10).nextLong());
		bomb.setName("Karan Sivarat");
		bomb.setEmail("bomb0069@gmail.com");
		bomb.setAddress(new Address());
		bomb.setCompany(new Company());


		PactDslJsonArray allUser = ConsumerExpects.collectionOf(User.class)
				.useArraySupplier(supplier)
				.arrayContent(ConsumerExpects.type(User.class))
				.build(Samples.Default.of(User.class).get());

		return builder
				.given("All User is 10 with Real Object")
				.uponReceiving("a request for json data")
				.path("/users")
				.method("GET")
				.willRespondWith()
				.status(200)
				.body(allUser)
				.toPact();
	}

}
