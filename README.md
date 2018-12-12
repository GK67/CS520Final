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
 2. After change to the bin folder, enter the command (since you has created a superuser during installation): psql -U postgres
 3. Then, enter command for password (since you has created the password during installation).
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

Run User Interface:
2 radio buttons: Let you choose which way you want to input your location. 

Input method: The first way is choose enter the longitude and  latitude, if it is out of range, we will choose the closest node for your location. The second way is select a building name which is close to you.

Path algorithm has 3 methods,which is dropdown of 3 choices.

Last textarea will show path of the algorithm , total distance and total elevation changes.

Algorithm: 

Algorithm is based on A* search, it searches over all the nodes in the “ways”. Start from the start location node , and every time add two the closest node of origin node in the way and put them into the priority queue. Once all the closest nodes add in to priority queue, take out the origin node, put them in a “trash node” list, so you will not repeat adding same node. 

