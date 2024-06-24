# Maledetta Tre Est App - project of Mobile Computing
Project for the University Bachelor Degree Course of Mobile Computing (A.A. 2020-2021)

TreEst is a railway transport company. Unfortunately, TreEst trains are often late, crowded or even cancelled. 
In addition to these problems, passengers (especially commuters who use TreEst every day) complain that TreEst provides little information on the status of trains.
To address these problems, passengers organized themselves and created the “Maledetta TreEst” system. 
The purpose of the system is to allow passengers to share information on the status of trains with each other.

The aim of this project is to create an android app and its respective cross platform app (using the cordova plugin). 
In this repository you can find the src of the Android application and the www of Cordova where there is the code to run the platform.

### SYSTEM DATA

• Line: TreEst manages various railway **lines**. Each line is made up of a list of stations (station) each with a name (sname) and geographical coordinates (lat and lon). The first and last stations (the two terminus) define the name of the line. E.g.: a line that has stations [“Abbiategrasso”, “Baggio”, “Como”, “Domodossola”] is the “Abbiategrasso-Domodossola” line. For each line there are two **sections** (direction) identified by an id (did). In the previous example, for the “Abbiategrasso-Domodossola” line there are the sections “Abbiategrasso-Domodossola towards Abbiategrasso” and “Abbiategrasso-Domodossola towards Domodossola”. Note: Some stations may be part of multiple lines.

• **User (user):** is identified by a name (uname) and contains an optional profile image (upicture), which must be encoded in Base64 and the version number of the profile image (pversion) which at the beginning ( when the user does not yet have an image) is equal to zero. Each user has two other identifiers: a session number (sid) which is known only to the user's application and serves as login credentials, and a user identifier (uid) which is known to other users.

• A **post** **board** is defined for each section of each line. Each post is identified by a numeric **identifier** (pid), is created by a user (author, the uid of the user who created the post), and contains **delay** information (delay, 0: on time, 1: delayed by a few minutes, 2: delay more than 15 minutes, 3: trains cancelled) and travel **status** (status, 0: ideal situation, 1: acceptable, 2: serious problems for passengers) and a **comment**. The three pieces of information (delay, status and comment) are all optional, but at least one of these must be reported (otherwise we would have an empty post).

• A “**follow**” relationship is defined between users: the relationship is asymmetric (user A can follow user B without B following A) and A can start following B without B providing his authorization.

### SYSTEM DESCRIPTION

The functionalities are the following:

• **Implicit registration:** Each user has a session number that identifies him to the server. When started for the first time, the application requests a session number from the server and then stores it in persistent mode. In all communications between client and server, the client indicates its session number. The user never enters their session number by hand (the user doesn't even know what a session number is) nor does they ever take an explicit logging action. The user knows nothing about login or registration, the app does everything without asking the user anything.

• **Profile:** The user has the option to specify a username and profile picture. Profile images are associated with a version number, which is incremented every time a user changes their image. The user does not see version number information.

• **Choice of notice board:** The user must be able to choose which board to view (remember, there is a board for each route). The choice is made like this: if the user has already previously viewed a board, that same board must be displayed by default. It must be possible to quickly change routes on the same line. E.g.: if the user views the noticeboard of the “Abbiategrasso-Domodossola direction Abbiategrasso” route, he must be able to quickly move to the “Abbiategrasso-Domodossola direction Domodossola” route. At the first start, or whenever the user wishes, the user can select a different route by choosing from all the available routes shown in a list.

• **Noticeboard display:** When the user views a board he sees the list of the latest posts. For each one, the image, the author's name, the date and time of publication and, if available, the following data are displayed: comment, delay, status. It is the server's job to decide what the "latest posts" are. The client must show all posts sent by the server. Posts from followed users are displayed prominently. From each post, the user can choose whether to start following or stop following the author of the post. The user can insert a new post.

• **Route details:** From the bulletin board page the user can view the details of a route: a map shows the stations and, if possible, the user's current position.
