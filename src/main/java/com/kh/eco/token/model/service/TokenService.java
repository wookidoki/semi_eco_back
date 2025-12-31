package com.kh.eco.token.model.service;

import java.util.Map;

public interface TokenService {
	
	Map<String, String> generateToken(String username);
	
	Map<String, String> validateToken(String refreshToken);

}
