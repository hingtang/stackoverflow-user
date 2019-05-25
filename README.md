# StackOverFlow User List
Show `User List` in pagination and `User Reputation List` via the StackOverFlow API: [User List API](https://api.stackexchange.com/2.2/users?page=1&pagesize=30&site=stackoverflow) [User Reputation API](https://api.stackexchange.com/2.2/users/11683/reputation-history?page=1&pagesize=30&site=stackoverflow)

## What is this?
Fossil Android home test

This project built with Kotlin, MVVM, Android Architecture Component, Clean Architecture to show the StackOverFlow user list.

## Android Architecture Component
- Retrofit : to retrieve the data from server
- RxAndroid : to subscribe the API calls
- Dagger 2 : to use the Dependency Injection Framework
- LiveData : to observe the data
- ROOM : to store the data

I also use mockito, espresso for testing. 

## Other comments:
