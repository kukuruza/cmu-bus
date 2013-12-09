Cmu Bus Tracker
===============
Look up the next bus for Pittsburgh Port Authority Transportation System and CMU shuttles.


What's inside
-------------
The system consists of 
1) Tomcat webserver ('webserver' directory)
2) Android app ('CmuBusTracker' directory)
3) Additional submission documents ('documents' directory)


Installation
------------

App: import 'CmuBusTracker' directory as an Android project. It should compile without any issues.

Server: you need Tomcat 7.0 to run the webserver. If you are using Eclipse JEE, import 'webserver' directory as a project. Change

Possible issues.


Usage
-----
The usage pattern is as follows:
1) choose bus stop
2) choose buses that go through this bus stop
3) view next several buses

For web interface: 
1) Go to http://localhost:8080/webserver/. Choose the stop from the list. You can enter a street for your stop, to make the list smaller. Click 'choose stop'
2) Pick the buses from the list. Only buses passing though the chosen stop are displayed. Every bus has a number and direction. Click 'Get schedule'
3) See the schedule. The buses are sorted by arrival times.

For app interface (the home page brings you straight to step 2, the closest bus stop already chosen):
1) From home screen tap 'change bus stop'. That will bring you to a list of stops, sorted by distance from you. You can search by street name to make the list shorter. Tap 'Choose' button.
2) Pick buses from the list. Only buses passing though the chosen stop are displayed. Every bus has a number and direction. Tap 'Get schedule' button
3) See the schedule. The buses are sorted by arrival times. A label 'local info' / 'server info' indicates whether the data was retrieved from the local database or from the server.

Extra for the app: from the home screen (step 2, the screen has button 'Get schedule') tap on menu button.
4) From the menu you can add the current stop to the list of favorites. When added the favorite stops will be displayed ahead of others in the list
5) From the menu, tap 'Settings'. In the settings you can choose whether to connect to the server first by default to retrieve data. The server has up-to-date schedule and in some cases it has real-time estimate of buses arrival times.
6) Menu on every screen has the item 'help' which guides a user though.

More
----
Please look into documents/meeting_requirements.pdf and documents/design.pdf for detailed usage description and design accordingly

