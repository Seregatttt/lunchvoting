<p>A voting system for deciding where to have lunch.</p>
<p>REST API using Hibernate/Spring/SpringMVC <strong>without frontend</strong>.</p>
<ul>
<li>2 types of users: admin and regular users</li>
<li>Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)</li>
<li>Menu changes each day (admins do the updates)</li>
<li>Users can vote on which restaurant they want to have lunch at</li>
<li>Only one vote counted per user</li>
<li>If user votes again the same day:
<ul>
<li>If it is before 11:00 we asume that he changed his mind.</li>
<li>If it is after 11:00 then it is too late, vote can't be changed</li>
</ul>
</li>
</ul>
<p>Each restaurant provides new menu each day.</p>

## REST API documentation

### Registration

`curl -s -X POST -d '{"name":"New User","email":"testReg@mail.ru","password":"testReg-password"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/lunchvoting/rest/profile/register`  

### User

#### Profile:
get profile <br>
`curl -s http://localhost:8080/lunchvoting/rest/profile --user user1@mail.ru:password1`

update profile <br>
`curl -s -X PUT  http://localhost:8080/lunchvoting/rest/profile --user user2@mail.ru:password2 -d '{"name":"New name", "email":"user-2@yandex.ru", "password":"password2"}' -H 'Content-Type:application/json;charset=UTF-8'` 

delete profile <br>
`curl -s -X DELETE http://localhost:8080/lunchvoting/rest/profile --user user2@mail.ru:password2`

#### Voting:

get all restaurants with menu<br>
`curl -s http://localhost:8080/lunchvoting/rest/profile/showMenuAndRestaurant --user user1@mail.ru:password1`

get all restaurants with menu for startdate and enddate<br>
`curl -s "http://localhost:8080/lunchvoting/rest/profile/showMenuAndRestaurant?startDate=2020-05-03&endDate=2020-05-03" --user user1@mail.ru:password1`

vote for restaurant with menu id 100006 <br>
`curl -s -X POST  http://localhost:8080/lunchvoting/rest/profile/restaurants/10006/votes -d {} -H 'Content-Type:application/json;charset=UTF-8' --user user1@mail.ru:password1`

change vote for restaurant with menu id 100001 <br>
`curl -s -X PUT  http://localhost:8080/lunchvoting/rest/profile/restaurants/10001/votes -d {} -H 'Content-Type:application/json;charset=UTF-8' --user user1@mail.ru:password1`

get history Votes for login user<br>
`curl -s http://localhost:8080/lunchvoting/rest/profile/historyLunchVotes --user user1@mail.ru:password1`

delete vote for restaurant with menu id 100003 <br>
`curl -s -X DELETE http://localhost:8080/lunchvoting/rest/profile/restaurants/10003/votes/ --user user2@mail.ru:password2`

### Admin

#### Users:

get All Users<br>
`curl -s http://localhost:8080/lunchvoting/rest/admin/users --user admin@mail.ru:password`

get Users 101<br>
`curl -s http://localhost:8080/lunchvoting/rest/admin/users/101 --user admin@mail.ru:password`

add Users<br>
`curl -s -X POST -d '{"name":"New User","email":"test@mail.ru","password":"test-password"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/lunchvoting/rest/admin/users --user admin@mail.ru:password`

update Users<br>
`curl -s -X PUT -d '{"name":"New User","email":"test22@mail.ru","password":"test-password"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/lunchvoting/rest/admin/users/101 --user admin@mail.ru:password`

delete Users<br>
`curl -s -X DELETE http://localhost:8080/lunchvoting/rest/admin/users/101 --user admin@mail.ru:password`

validate with Error<br>
`curl -s -X POST -d '{}' -H 'Content-Type: application/json' http://localhost:8080/lunchvoting/rest/admin/users --user admin@mail.ru:password`

#### Restaurants:

get all restaurants <br>
`curl -s http://localhost:8080/lunchvoting/rest/admin/restaurants --user admin@mail.ru:password`

get  restaurants id 10 <br>
`curl -s "http://localhost:8080/lunchvoting/rest/admin/restaurants/10" --user admin@mail.ru:password`

add restaurant "New restaurant" <br>
`curl -s -X POST -d '{"name":"ew restaurant","address":"new-address"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/lunchvoting/rest/admin/restaurants --user admin@mail.ru:password`

update restaurant id 12 <br>
`curl -s -X PUT -d '{"name":"update-name","address":"update-address"}' -H 'Content-Type:application/json;charset=UTF-8' "http://localhost:8080/lunchvoting/rest/admin/restaurants/12" --user admin@mail.ru:password`

delete restaurant 12 <br>
`curl -s -X DELETE http://localhost:8080/lunchvoting/rest/admin/restaurants/12 --user admin@mail.ru:password`

#### Menu:

get All Menu for restaurant id 10<br>
`curl -s http://localhost:8080/lunchvoting/rest/admin/restaurants/10/menus --user admin@mail.ru:password`

get Menu id 10003 for restaurant id 10<br>
`curl -s http://localhost:8080/lunchvoting/rest/admin/restaurants/10/menus/10003 --user admin@mail.ru:password`

add Menu for restaurant id 10 for 2020-05-15<br>
`curl -s -X POST -d '{"dateMenu":"2020-05-15"}' -H 'Content-Type:application/json;charset=UTF-8' "http://localhost:8080/lunchvoting/rest/admin/restaurants/10/menus" --user admin@mail.ru:password`

update Menu<br>
`curl -s -X PUT -d '{"dateMenu":"2020-05-22"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/lunchvoting/rest/admin/restaurants/10/menus/10003 --user admin@mail.ru:password`

delete Menu<br>
`curl -s -X DELETE http://localhost:8080/lunchvoting/rest/admin/restaurants/10/menus/10003 --user admin@mail.ru:password`

create doublecate<br>
`curl -s -X POST -d '{"dateMenu":"2020-05-01"}' -H 'Content-Type:application/json;charset=UTF-8' "http://localhost:8080/lunchvoting/rest/admin/restaurants/10/menus" --user admin@mail.ru:password`

#### Meals:

get All Meals for Menu id 10001<br>
`curl -s http://localhost:8080/lunchvoting/rest/admin/menus/10001/meals --user admin@mail.ru:password`

get Meals id 1003 for Menu id 10001<br>
`curl -s http://localhost:8080/lunchvoting/rest/admin/menus/10001/meals/1003 --user admin@mail.ru:password`

add Meals for Menu id 10001<br>
`curl -s -X POST -d '{"name":"super-cake","price":11.12}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/lunchvoting/rest/admin/menus/10001/meals --user admin@mail.ru:password`

update Menu<br>
`curl -s -X PUT -d '{"name":"update salad","price":44.44}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/lunchvoting/rest/admin/menus/10001/meals/1003 --user admin@mail.ru:password`

delete Menu<br>
`curl -s -X DELETE http://localhost:8080/lunchvoting/rest/admin/menus/10004/meals/1010   --user admin@mail.ru:password`

create invalid Meals for Menu id 10001<br>
`curl -s -X POST -d '{"name":"super-cake","price":0.0}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/lunchvoting/rest/admin/menus/10001/meals --user admin@mail.ru:password`










