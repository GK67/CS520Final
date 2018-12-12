# CS520Final

Software Need: Command Prompt (already install in most computer), PostgreSQL

Set up environment(PostgreSQL):
 1. Download the installer from http://www.postgresql.org/download/windows/ from the download link
 2. Double click on the downloaded file. A window will show up that will guide you through the installation.
 3. The installer wizard will ask you to specify a directory where PostgreSQL should be installed. It is all right if you stick with the default option. So just click on "Next>".
 4. It will then ask you to specify a data directory. Again, all right to stick with the default option and click on "Next>".
 5. ou will next be prompted to enter a password for the superuser "postgres". Enter one and make a note of it.
 6. Next, you will be asked to enter a port number for the server to listen on. The default option should be 5432. Not a bad idea to leave it at that. 
 7. Next, leave the locale selection at "Default Locale".
 8. After that, going by the instruction of installation to finish the installation

Settings before Running the program:
 1. Open the Command Prompt, change directory to where the PostgreSQL installed, similar link, "..\PostgreSQL\10\bin"
 2. After change to the bin folder, enter the command (since you have created a superuser during installation): psql -U postgres
 3. Then, enter the password (you created the password during installation).
 4. After that, you are in the postgreSQL prompt, like "postgres=#".
 5. Then type, "CREATE DATABASE osmdb;", to create a database, which is called osmdb.
 6. Then close the Command Prompt.
 7. Go to the project folder, which has the .java files.
 8. In the folder, find the file,"dbconn.config", change the password which is next to "postgreSQLPassword".
 9. Since you create the superuser as "postgres", not need to change the user name.

Running the program:
 1. Open the Command Prompt, change the directory to the folder, which has .java files in it.
 2. Type (Make sure you know where your java jdk file are, set the path of the jdk bin): path C:\Program Files\Java\jdk1.8.0_101\bin;%path%
 3. Type (set the path of the external library): set CLASSPATH=.;postgresql-42.2.5.jar.;lib/*
 4. Type (Make the class files):javac -g MockProgram.java Query.java  Path.java UI.java NodeComparable.java
 5. Type (run the program): java MockProgram
 6. It will show a graphic interface of our program.

The product:
Our project is an Elevation-based Navigation system. We acquire geodata from OpenStreetMaps and elevation data from an OpenStreetMaps API, Open-Elevation.

Project features:
2 radio buttons: Let you choose which way you want to input your location. 

Input method: The first way is choose enter the longitude and  latitude, if it is out of range, we will choose the closest node for your location. The second way is select a building name which is close to you.

Path algorithm has 3 methods,which is dropdown of 3 choices.
Most Efficient: The most efficient path by putting reasonable weights onto change in elevation. This algorithm will consider whether or not it is more efficient to have elevation change or avoid elevation change by taking a detour.

Least Elevation: Putting heavy weights onto change in elevation, the path will try to avoid elevation change as much as possible unless it is extremely unreasonable.

Shortest Distance: Elevation change is not factored into the path search algorithm. The algorithm will output a path that requires the least distance without taking into account elevation.


Last textarea will show path of the algorithm , total distance and total elevation change.

Algorithm: 
Our pathing algorithm is an A* search. It begins by locating the closest node to the start location that is on a walkway/path. The algorithm uses a priority queue to queue through nodes. The priority queue gives highest priority to nodes that have the lowest distance to the destination as well as the total distance it took to get to that node. The priority queue holds ArrayLists that contain the total distance traveled as well as nodes that were previous in the path. After dequeuing from the priority queue, the algorithm adds on all neighboring nodes that appear in the path/walkway that the current node is on. The algorithm gives the according weight to changes in elevation based off the choice the user made. The algorithm repeats until a path to the end node is found or until all possible combinations are searched through and no path is found.

Modules:
ConvertData.java: contains all functions to retrieve information from the open source APIs
MockProgram.java: The main driver of the project
Query.java: contains all queries to retreive information from the database
Path.java: contains all functions for the path-searching algorithm
NodeComparable.java: contains the comparable class for the priority queue to use during the path algorithm
Ui.java: contains all methods from the UI/view

