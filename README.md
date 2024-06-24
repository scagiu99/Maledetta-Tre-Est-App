# Maledetta-Tre-Est-App
Project for the University Bachelor Degree Course of Mobile Computing (A.A. 2020-2021)

TreEst is a railway transport company. Unfortunately, TreEst trains are often late, crowded or even cancelled. 
In addition to these problems, passengers (especially commuters who use TreEst every day) complain that TreEst provides little information on the status of trains.
To address these problems, passengers organized themselves and created the “Maledetta TreEst” system. 
The purpose of the system is to allow passengers to share information on the status of trains with each other.

The aim of this project is to create an android app and its respective cross platform app (using the cordova plugin). 
In this repository you can find the src of the Android application and the www of Cordova where there is the code to run the platform.

The functionalities are the following:

• **Implicit registration:** Each user has a session number that identifies him to the server. When started for the first time, the application requests a session number from the server and then stores it in persistent mode. In all communications between client and server, the client indicates its session number. The user never enters their session number by hand (the user doesn't even know what a session number is) nor does they ever take an explicit logging action. The user knows nothing about login or registration, the app does everything without asking the user anything.
• **Profile:** The user has the option to specify a username and profile picture. Profile images are associated with a version number, which is incremented every time a user changes their image. The user does not see version number information.
• **Choice of notice board:** The user must be able to choose which board to view (remember, there is a board for each route). The choice is made like this: if the user has already previously viewed a board, that same board must be displayed by default. It must be possible to quickly change routes on the same line. E.g.: if the user views the noticeboard of the “Abbiategrasso-Domodossola direction Abbiategrasso” route, he must be able to quickly move to the “Abbiategrasso-Domodossola direction Domodossola” route. At the first start, or whenever the user wishes, the user can select a different route by choosing from all the available routes shown in a list.
• **Noticeboard display:** When the user views a board he sees the list of the latest posts. For each one, the image, the author's name, the date and time of publication and, if available, the following data are displayed: comment, delay, status. It is the server's job to decide what the "latest posts" are. The client must show all posts sent by the server. Posts from followed users are displayed prominently. From each post, the user can choose whether to start following or stop following the author of the post. The user can insert a new post.
• **Route details:** From the bulletin board page the user can view the details of a route: a map shows the stations and, if possible, the user's current position.
