# Microservices Ecosystem

### What we have
![Components Diagram](https://github.com/HernanUrban/urban-ms-ecosystem/blob/master/Zuul%20as%20API%20GW.png)


### How to Run the system
Build and install docker images:  
``$ ./mvnw clean install -DskipTests dockerfile:build``

Run via docker compose:  
``$ docker-compose up``

### What's next?
We know that auth server and resource servers are separated services in the ecosystem, but we have the api gateway as a proxy and filter the requests/responses.  
Now let's give it a try...  
- create a user.
- "authenticate" that user.
- hit a resource endpoint with the user token.

1. Get a token to create an user:  

``$ curl -u register-app:secret -d grant_type=client_credentials http://localhost:8080/api/auth/oauth/token``  

Response:  

```json
{
 "access_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiYXV0aF9zZXJ2ZXIiXSwic2NvcGUiOlsicmVhZCJdLCJleHAiOjE1MjIzNzQ0NTcsImF1dGhvcml0aWVzIjpbIlJPTEVfUkVHSVNURVIiXSwianRpIjoiOTk0MDc1ZjYtNGJmMS00YzI1LTlkZTUtNzk0MTQzNzZiZGM3IiwiY2xpZW50X2lkIjoicmVnaXN0ZXItYXBwIn0.QZwZ7gRTnKKH84eE9VnI64wcC9za5UzRLFZQWgqdfUs",
 "token_type":"bearer",
 "expires_in":43199,
 "scope":"read",
 "jti":"994075f6-4bf1-4c25-9de5-79414376bdc7"
}
```  
Notes: The app won't let you generate anonymous tokens, we've used the client credentials(register-app/secret) to create a token.  

2. Create a user:  

``
$ curl -u trusted-app:secret -H "Content-Type: application/json" -X POST http://localhost:8080/api/auth/users/create
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiYXV0aF9zZXJ2ZXIiXSwic2NvcGUiOlsicmVhZCJdLCJleHAiOjE1MjIzNzQ0NTcsImF1dGhvcml0aWVzIjpbIlJPTEVfUkVHSVNURVIiXSwianRpIjoiOTk0MDc1ZjYtNGJmMS00YzI1LTlkZTUtNzk0MTQzNzZiZGM3IiwiY2xpZW50X2lkIjoicmVnaXN0ZXItYXBwIn0.QZwZ7gRTnKKH84eE9VnI64wcC9za5UzRLFZQWgqdfUs"
-d  '{"username":"hurban", "password":"test123", "firstName":"Hernan", "lastName":"Urban", "email":"urbanehrnan@gmail.com"}'
``  

Response:  

```json
{"id":10,"username":"hurban","firstName":"Hernan","lastName":"Urban","email":"urbanehrnan@gmail.com","enabled":true}
```  

Note: We've created our own user. We can generate a token with the credentials.  


3. Create a token to hit a secure resource  

``$ curl -u trusted-app:secret -X POST http://localhost:8080/api/auth/oauth/token -d username=hurban -d password=test123 -d grant_type=password``  

Response:  

```json
{"access_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidXJiYW4tc2VydmljZSIsImF1dGhfc2VydmVyIl0sInVzZXJfbmFtZSI6Imh1cmJhbiIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE1MjIzNzYwODAsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiI2MTg3MzAzZi1jNWE5LTQ0NDMtYTA5Ny1iZDk2NWU1NWRhN2YiLCJjbGllbnRfaWQiOiJ0cnVzdGVkLWFwcCJ9.WXay7rL9BhPHIxdce-Tmq4dELh16OirXAlSGAGZuVA8","token_type":"bearer","refresh_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidXJiYW4tc2VydmljZSIsImF1dGhfc2VydmVyIl0sInVzZXJfbmFtZSI6Imh1cmJhbiIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJhdGkiOiI2MTg3MzAzZi1jNWE5LTQ0NDMtYTA5Ny1iZDk2NWU1NWRhN2YiLCJleHAiOjE1MjQ5MjQ4ODAsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiI4NzFhNjUwZC02NGY5LTQzYmQtODUxNC00OTU2MTMwNjcxZTAiLCJjbGllbnRfaWQiOiJ0cnVzdGVkLWFwcCJ9.H2XGR_hL2O6vwtCL7Til0aYSJha5FEniMmWjQ9eJM1o","expires_in":43199,"scope":"read write","jti":"6187303f-c5a9-4443-a097-bd965e55da7f"}
```  

Note: Now we have the user token :)  

4. Hit a secure resource endpoint  

``$ curl -u trusted-app:secret http://localhost:8080/api/resources/user -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR 5cCI6IkpXVCJ9.eyJhdWQiOlsidXJiYW4tc2VydmljZSIsImF1dGhfc2VydmVyIl0sInVzZXJfbmFtZSI6Imh1cmJhbiIsInNjb3BlIjpbInJlYWQiLCJ3c ml0ZSJdLCJleHAiOjE1MjIzNzYwODAsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiI2MTg3MzAzZi1jNWE5LTQ0NDMtYTA5Ny1iZDk2NWU1 NWRhN2YiLCJjbGllbnRfaWQiOiJ0cnVzdGVkLWFwcCJ9.WXay7rL9BhPHIxdce-Tmq4dELh16OirXAlSGAGZuVA8"``  

Response:  

```
Hello hurban
```  

Note: If you hit it with an invalid token you will get an error.  

