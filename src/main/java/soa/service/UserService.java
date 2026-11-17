package soa.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import soa.model.Users;
import soa.repo.UserRepo;

@Service
public class UserService {
	UserRepo UR;
	JwtService JWT;
	public UserService(UserRepo UR, JwtService JWT) {
		this.UR=UR;
		this.JWT=JWT;
	}
	public Object validateUser(String username, String password) {
		Map<String, Object> response=new HashMap<>();
		try {
			Users user = UR.findByUsernameAndPassword(username, password);
			 if(user == null)
				 throw new Exception("Invalid user!");
			 Object token = JWT.generateToken(user.getUsername(), user.getRole());
			 response.put("status", "success");
			 response.put("message", "Login successful");
			 response.put("token", token);
			
		}catch(Exception e) {
			response.put("status", "error");
			response.put("message", e.getMessage());
		}
		return response;
	}
	
	
	

}
