#### get All Users
curl -s http://localhost:8080/lunchvoting/rest/admin/users --user admin@mail.ru:password

#### get Users 101
curl -s http://localhost:8080/lunchvoting/rest/admin/users/101 --user admin@mail.ru:password

#### add Users
curl -s -X POST -d '{"name":"New User","email":"test@mail.ru","password":"test-password"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/lunchvoting/rest/admin/users --user admin@mail.ru:password

#### update Users
curl -s -X PUT -d '{"id":"101",name":"user1","email":"user1@mail.ru","password":"test-password"}' -H 'Content-Type: application/json' http://localhost:8080/lunchvoting/rest/admin/users --user admin@mail.ru:password

#### delete Users
curl -s -X DELETE http://localhost:8080/lunchvoting/rest/admin/users/101 --user admin@mail.ru:password

#### validate with Error
curl -s -X POST -d '{}' -H 'Content-Type: application/json' http://localhost:8080/lunchvoting/rest/admin/users --user admin@mail.ru:password

