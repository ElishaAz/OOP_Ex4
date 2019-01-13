# OOP_Ex4
This is the fourth and final assighnment for the OOP course at Ariel University.
This project is a game where you control one player by giving it a direction.

there are multiple objects in the game:
 - Block: a rectangle defined by two Lat/Lon coordinets. The player cannot move into blocks, and his score is reduced if he tries.
 - Fruit: a circle defined by one Lat/Lon coordinet. The player's score increases every time he eats a fruit.
 - Packman: a circle defined by one Lat/Lon coordinet and raduis in meters.
 The player's score increases every time he eats a packman. Packmen eat fruit.
 - Ghost: a circle defined by one Lat/Lon coordinet and raduis in meters. 
 Ghosts chase the player, and the player's score is reduced whenever a ghost reaches him.
 
The objective is to get the heighest score possible before the time ends or all the fruits were eaten.

This project has two modes:
 - User play: play using the mouse. The algorithm will not let you hit a block.
 - Computer play: displays an algorithm playing. This algorithm is not optimal.
 
 This project has nine built in maps, and more maps can be loaded.
 
 The project interacts with a pseudo-server running the game using strings.
 
 The project includes an option to display the score board.
 
 The floyad Warshall algotithm is used to calculate safe paths.
 
 # dependencies
 
 The pseudo-server: Ex4_v0.2.jar (included).
 
 MySQL: mysql-connector-java-5.1.47.jar (included).
