# Bruno Demo

## 1. Create new collection "Todo List"
- Name: Todo List
- Location: projectpath/bruno

## 2. HealthCheck request

1. New request
   - Name: Healthcheck
   - Type: GET
   - URL: http://localhost/actuator/health

2. Send request
3. Create environment "local"
4. Add variable "base_url" -> http://localhost
5. Adjust URL: {{base_url}}/actuator/health
6. Send request
7. Create environment "dev"
8. Add variable "base_url" -> http://dev.todo.local
9. Create environment "prod"
10. Add variable "base_url" -> http://todo.local
11. send request for dev & prod

## 3. Add a Todo (Basic Auth)

1. New request
   - Name: Add a todo
   - Type: POST
   - URL: {{base_url}}/todos
2. Add body
   ```json
   {
     "title": "Clean the car"
   }
   ```
3. Add title var in vars tab
   ```json
   {
     "title": "{{title}}"
   }
   ```
4. Add basic auth credentials in environments

   **local**
   - username: todo-user-local
   - password: password-local
   
   **dev**
   - username: todo-user-dev
   - password: password-dev
   
   **prod**
   - username: todo-user
   
   password: password
5. Use variables in request auth
   - {{username}}
   - {{password}}
6. Send request

## 4. Get todos

1. New request
   - Name: Get todos
   - Method: GET
   - URL: {{base_url}}/todos
2. Add basic auth to collection
3. Set auth to inherit
4. Send reqeust
5. Set add todo to auth inherit
6. Send request

## 5. Mark todo as completed 

1. New request
   - Name: Mark todo as completed
   - Method: PUT
   - URL: {{base_url}}/todos/{{id}}/complete

2. Auth to inherit
   - set id in vars to 1
   - let's add a {{latest_todo_id}} variable
   - add a post script to "Add todo" request
   ```javascript
      if (res.status == 200 && res.body.id){
         bru.setVar("latest_todo_id",res.body.id)
   }
   ```
3. send request
4. show variable to the audience
5. use var {{latest_todo_id}} in mark as completed
6. send request

## 6. Get non completed todos
1. add param open to Get todos request
2. send request

## 7. Delete a todo
1. add a new request
   - Name: Delete a todo
   - Method: DELETE
   - URL: {{base_url}}/todos/{{id}}
2. set auth to inherit
2. set var id to {{latest_todo_id}}
3. send request
4. send get todos request

## 8. Adjust for OAuth2
1. adjust start.sh for oauth2
2new Request
   - Name: Get token
3. Auth to Oauth2
   - Grant Type: Password Credentials
   - Token URL: {{base_url}}/token
4. Set client id in environment

   **local**
   - client_id: todo-app-local
   
   **dev**
   - client_id: todo-app-dev
   
   **prod**
   - client_id: todo-app
5. use var client_id in auth tab
6. set post response script
   ```javascript
   if(req.getAuthMode() == 'oauth2' && res.body.access_token) {
      bru.setVar('access_token', res.body.access_token);
   }
   ```
7. Set all requests to Auth Bearer and use var {{access_token}}
8. Move Token Request on second place 
9. Show Runner and run whole collection

## 9. Assertions
1. Go to assertions tab in request add a todo
2. add assertions:
   - res.status - equals - 200
   - res.body.title - equals - {{title}}
   - res.body.is - isNumber
   - res.body.completed - equals - false
   
   
   
   
   
   
   




