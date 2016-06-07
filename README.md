# LocalDatabaseService
The Local Database Service is a SOAP Web Service. This layer sits on top of a database in order to provide data to the [Storage Services](https://github.com/introSDE-FinalProject-2016/StorageServices). It's responsible for handling all persistence tasks on the database.

[API Documentation](http://docs.localdatabaseservices.apiary.io/#)  
[URL Client](https://github.com/introSDE-FinalProject-2016/Telegram-Bot)  
[URL Server (heroku)](https://nameless-reaches-22539.herokuapp.com/ws/people)  
[WSDL File](https://nameless-reaches-22539.herokuapp.com/ws/people?wsdl) 


###Install
In order to execute this server locally you need the following technologies:

* Java (jdk 1.8.0)
* Ant (version 1.9.6)
* SQLite (version 3.7.9)

Then, clone the repository. Run in your terminal:

```
$ git clone like https://github.com/yuly-sanchez/LocalDatabaseServices.git
$ cd LocalDatabaseServices
```

and run the following command:
```
ant install
```

###Getting Started
To run the server locally then run:
```
ant start
```
