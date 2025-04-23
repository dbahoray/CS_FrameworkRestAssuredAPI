package api.testcases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpints.userEndPoints;
import api.payload.user;
import io.restassured.response.Response;

public class UserTest {
	
	Faker faker;
	user userPayload;
	
	public static Logger logger;
	
	@BeforeClass()
	public void generateTestData()
	{
		faker = new Faker();
		userPayload = new user();
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5, 10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
		logger = LogManager.getLogger("RestAssuredAutomationFramework");
	}
	
	@Test(priority = 1)
	public void testCreateUser()
	{
		Response response = userEndPoints.createUser(userPayload);
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("Create User executed");
		
	}
	
	@Test(priority = 2, dependsOnMethods = "testCreateUser")
	public void testGetUserData()
	{
		Response response = userEndPoints.GetUser(this.userPayload.getUsername());
		System.out.println("Get User Data");
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("Get User executed. Modified for Jenkins Test.");
		
	}
	
	@Test(priority = 3)
	public void testUpdateUser()
	{
		userPayload.setFirstName(faker.name().firstName());
		Response response = userEndPoints.UpdateUser(this.userPayload.getUsername(), userPayload);
		
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		
		//to get the updated data of user
		Response responsePostUpdate = userEndPoints.GetUser(this.userPayload.getUsername());
		System.out.println("Updated User Data");
		responsePostUpdate.then().log().all();
		
		logger.info("Updated User executed");
	}
	
	@Test(priority = 4)
	public void testDeleteUser()
	{
		Response response = userEndPoints.DeleteUser(this.userPayload.getUsername());
		System.out.println("Deleted User Data");
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(),200);
		
		logger.info("Delete User executed");
		
	}
}
