# spring-security-jwt
Incorporate JWT to Spring Security

* This project is based-on spring-security-mvc (https://github.com/MountKingX/spring-security-mvc)
* To make our api endpoint safe, we added `JwtLoginAuthenticationFilter` and `JwtTokenVerificationFilter`
* If client makes a `/auth/login` POST request call, the `JwtLoginAuthenticationFilter` will handle it 
    - if username/password pair is verified from Spring-Security, 
    system will send generated token to client in response header
* All other request will go through `JwtTokenVerificationFilter`
    - If you have a valid token in request header (tokenPrefix="Bearer"), 
    your request will gain the role/authority assigned in the token and you are authenticated/authorized for resources
    - header does not contain the token or token is not valid, this request will have no permission to our resources
* It is suggested to use POSTMAN (https://www.postman.com/) as our client for testing

* Start the app by running MySecurityApplication class's main method
* In your browser, hit `http://localhost:8080/devTest` to populate three default users and two task items;
* Perform a POST request to `localhost:8080/auth/login` to get the token with the username/password below:
    - dev/dev for SUPER_ADMIN permission;
    - normal/111 for NORMAL permission;
    - admin/222 for ADMIN permission;
    
    for example, in the request body, I 
```bash
    {
	    "username": "dev",
	    "password": "dev"
    }
```    
    
* Add the token to your header for GET request of those API end point
    - `api/v1/todolists` for all authenticated accounts
    - `api/v1/accounts` for ADMIN or SUPER_ADMIN only
    
    For instance, in my GET Header for `localhost:8080/api/v1/todolists`, I put:
    - key: Authorization
    - value: BearereyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJzYSIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJhY2NvdW50OnJlYWQifSx7ImF1dGhvcml0eSI6InRhc2s6cmVhZCJ9LHsiYXV0aG9yaXR5IjoiYWNjb3VudDp3cml0ZSJ9LHsiYXV0aG9yaXR5IjoiYWRtaW46cmVhZCJ9LHsiYXV0aG9yaXR5IjoidGFzazp3cml0ZSJ9LHsiYXV0aG9yaXR5IjoiYWRtaW46d3JpdGUifSx7ImF1dGhvcml0eSI6IlJPTEVfU1VQRVJfQURNSU4ifV0sImlhdCI6MTU4ODAzODg4NSwiZXhwIjoxNTg5MTczMjAwfQ.HNxUVl8AC8K6uGzOQDLCLeIpKZJc9zrundMEg6jpVvYNsQ5tMkHCHEs5xZf3hHgr
* Token is set to be expired in 14 days, as can be alter in application.properties's `webapp.jwt.tokenExpirationAfterDays=14`

last updated on May-5-2020