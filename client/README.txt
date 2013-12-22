
Progress
========

The design of the app is solid now. All of the class interfaces are finished.
The activities are close to the desired result. The database and its CRUD functions are done.
Currently we work on 1) implementing classes, 2) finishing activities (setting up listeners, etc.), 3) setting up web-server



Implemented features
====================

- Location, Network­-based Geo location, GPS - are implemented in entities.LocationService class. The getLocation() function returns the location to the caller.

- Gestures, Touchscreen - right now the listeners for gestures are in Activity classes, e.g. LocateStation class.

- SQL Lite DB - the database and the CRUD functions are in the dblayout class.

- Web Services - ws.remote package implements the functions to query the server and parse the response.



#
# from the previous commit (unit 3)
#


Database
========

The structure is described in src/cmu18641/bustracker/dblayout/Database_UML_diagram.png
To visualize the structure, please refer to the document src/cmu18641/bustracker/dblayout/Database_mock_values.pdf


Packages
========

The class diagrams is in the classDiagrams directory

