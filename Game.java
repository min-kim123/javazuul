package Zuul;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0 (February 2002)
 */
class Game {
    private Parser parser;
    private Room currentRoom;
    Room outside, theatre, pub, computinglab, office, onetwenty, storageroom, skillslab, cadaverlab, biolab, chemlab, seminar, hallway, portal, roboticslab;
    ArrayList<Item> inventory = new ArrayList<Item>();
    ArrayList<Item> items = new ArrayList<Item>();
// Create the game and initialize its internal map.
    public Game() {
        createRooms();
        parser = new Parser();
    }
    public static void main(String[] args) {
    	Game mygame = new Game();
    	mygame.play();
    }
// Create all the rooms and link their exits together.
    private void createRooms(){
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        computinglab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        onetwenty = new Room("in the coolest place in the world.");
        storageroom = new Room("in the storage room... Omg a cadaver");
        skillslab = new Room("in the Skills Lab");
        cadaverlab = new Room("in the Cadaver Lab");
        biolab = new Room("in the Bio Lab");
        chemlab = new Room("in the Chem Lab");
        seminar = new Room ("in the seminar room");
        hallway = new Room("in the gloomy hallway");
        portal = new Room("in the magical portal");
        roboticslab = new Room("in the robotics lab");
        // initialise room exits
        //OUTSIDE
        outside.setExit("east", theatre);
        outside.setExit("south", computinglab);
        outside.setExit("west", pub);
        outside.setExit("north", onetwenty);
        //LECTURE THEATER
        theatre.setExit("west", outside);
        theatre.setExit("north", biolab);
        theatre.setExit("east", seminar);
        //PUB
        pub.setExit("east", outside);
        //LAB
        computinglab.setExit("north", outside);
        computinglab.setExit("east", office);
        computinglab.setExit("south", roboticslab);
        //OFFICE
        office.setExit("west", computinglab);
        //ONETWENTY
        onetwenty.setExit("south", outside);
        //STORAGE ROOM
        storageroom.setExit("east", skillslab);
        //SKILLS LAB
        skillslab.setExit("east", cadaverlab);
        skillslab.setExit("south", biolab);
        skillslab.setExit("west", storageroom);
        //CADAVER LAB
        cadaverlab.setExit("south", chemlab);
        cadaverlab.setExit("west", skillslab);
        //BIO LAB
        biolab.setExit("north", skillslab);
        biolab.setExit("west", theatre);
        //CHEM LAB
        chemlab.setExit("north", cadaverlab);
        chemlab.setExit("west", seminar);
        //SEMINAR ROOM
        seminar.setExit("north", chemlab);
        seminar.setExit("south", hallway);
        seminar.setExit("west", theatre);
        //HALLWAY
        hallway.setExit("north", seminar);
        hallway.setExit("east", portal);
        //PORTAL
        portal.setExit("west", hallway);
        //ROBOTICS LAB
        roboticslab.setExit("north", computinglab);
        
        currentRoom = outside;  // start game outside
        
        //ITEMS
        inventory.add(new Item("Computer"));
        
        Item robot = new Item("Robot");
        onetwenty.setItem(robot);
        items.add(robot);
        Item book = new Item("Book");
        onetwenty.setItem(book);
        items.add(book);

        Item pen = new Item("Pen");
        seminar.setItem(pen);
        items.add(pen);
        
        Item grass = new Item("Grass");
        outside.setItem(grass);
        items.add(grass);
        
        Item flower = new Item("Flower");
        outside.setItem(flower);
        items.add(flower);
        
        Item cadaver = new Item("Cadaver");
        cadaverlab.setItem(cadaver);
        items.add(cadaver);
        
        Item eyeball = new Item("Eyeball");
        cadaverlab.setItem(eyeball);
        items.add(eyeball);
        
        Item statue = new Item("Statue");
        hallway.setItem(statue);
        items.add(statue);
        
        Item bike = new Item("Bike");
        roboticslab.setItem(bike);
        items.add(bike);
        
        Item bunsen_burner = new Item("Bunsen_Burner");
        biolab.setItem(bunsen_burner);
        items.add(bunsen_burner);
        
        Item graduated_cylinder = new Item("Graduated_Cylinder");
        chemlab.setItem(graduated_cylinder);
        items.add(graduated_cylinder);
    }

    /*  Main play routine.  Loops until end of play.*/
    public void play() 
    {            
        printWelcome();
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
        boolean finished = false;
        //make function
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }
    /** Print out the opening message for the player.*/
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to Adventure!");
        System.out.println("Adventure is a new, incredibly boring adventure game.");

        System.out.println(items.size());
        

        System.out.println("Your mission is to escape the university. You will have to gather the Book, Eyeball, and Grass and drop them in the magic portal.");
        System.out.println("Caution: if you enter the portal without the 3 items, you will be transported outside the university");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }
    /**
     * Given a command, process (that is: execute) the command.
     * If this command ends the game, true is returned, otherwise false is
     * returned.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;
        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }
        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
            // Book, Grass, and Eyeball
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("inventory")) {
        	printInventory();
        }
        else if (commandWord.equals("get")) {
        	getItem(command);
        }
        else if (commandWord.equals("drop")) {
        	dropItem(command);
        }
       
        return wantToQuit;
    }
    
    private void dropItem(Command command) {//DROP ITEM
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to  drop...
            System.out.println("Drop what?");
            return;
        }
        String item = command.getSecondWord();
     // Try to leave current room.
        Item newItem = null;
        int index = 0;
        for (int i = 0; i < inventory.size(); i++) {
        	if (inventory.get(i).getDescription().equals(item)) {
        		newItem = inventory.get(i);
        		index = i;
        	}
        }

        if (newItem == null)
            System.out.println("That item is not in your inventory!");
        else {
            inventory.remove(index);
            currentRoom.setItem( new Item(item));
            System.out.println("Dropped: " + item);
        }
    }

    private void getItem(Command command) {//PICK UP (GET) ITEM
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to pick up...
            System.out.println("Get what?");
            return;
        }
        String item = command.getSecondWord();
        // Try to leave current room.
        Item newItem = currentRoom.getItem(item);

        if (newItem == null)
            System.out.println("That item is not here!");
        else {
            inventory.add(newItem);
            currentRoom.removeItem(item);
            System.out.println("Picked up: " + item);
        }
    }
    private void printInventory() {//PRINT INVENTORY
		String output = "";
		for (int i = 0; i < inventory.size(); i++) {
			output += inventory.get(i).getDescription() + " ";
		}
		System.out.println("You are carrying: ");
		System.out.println(output);
	}
	// implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }
    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
    	boolean empty = inventory.isEmpty();

    	boolean x = false;
    	int count = 0;
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }
        //check second word and if it doesn't exist do this, if it is portal do the else if,
        String direction = command.getSecondWord();
        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
        	System.out.println("There is no door!");
        }
        else if (nextRoom == portal) {
        	for (int i = 0; i < inventory.size(); ++i) {
        		//if the inventory.get(i).description == "Grass"
        		if (inventory.get(i).description == "Grass") {
        			count+=1;
        		}
        		if (inventory.get(i).description == "Book") {
        			count+=1;
        		}
        		if (inventory.get(i).description == "Eyeball") {
        			count+=1;
        		}
        	}
        	if (count == 3) {//all 3 items are in the inventory
        		System.out.println("d");
        		currentRoom = nextRoom;
        		while(inventory.size()!=0) {
        			System.out.println("Drop your inventory");
        			Command com = parser.getCommand();
                    x = processCommand(com);
                    System.out.println("Inventory size: " + inventory.size());
        		}
        		if (inventory.size()==0) {
        			System.out.println("You escaped the university!");
        			System.exit(0);
        		}
        	}
        	else {//all 3 are not in the inventory
        		System.out.println("You tried to enter the portal without the Book, Grass, and Eyeball in your inventory.");
        		System.out.println("The portal has transported you outside the main entrance of the university.");
        		currentRoom = outside;
        	}
        }
            
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }
    
    //  "Quit" was entered. Check the rest of the command to see whether we really quit the game. Return true, if this command quits the game, false otherwise.
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else
            return true;  // signal that we want to quit
    }
}