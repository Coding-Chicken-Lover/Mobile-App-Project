import java.util.ArrayList;

public class Game
{
	private UserInterface ui;
	private ArrayList<Boat> boats;
	private  String[][] grid;
	private AI opponent;
	private int boatSunkHuman;
	private int boatSunkAi;

	public Game(UserInterface ui, AI ai)
	{
		this.ui = ui;
		opponent = ai;
		boats = new ArrayList<Boat>();
		boatSunkHuman = 0; // keeps record of the number of human boats  that has been sunk
		boatSunkAi = 0; // keeps record of the number of AI boats that has been sunk
		grid = new String[10][10];

		for (int i = 0; i < grid.length; i++) {
		    for (int j = 0; j < grid[0].length; j++) {
		        grid[i][j] = " ";
            }
        }
    }

	public void start() {

		boolean finished = false;
		int turn = 0;
		String coordinate = "";
		boolean[] results;

		do
		{
		    int choice = ui.displayStartMenu();
		    if (choice == 1) {
		        opponent.generateBoats();
		    	finished = false;
                int accept = ui.autoGenerateBoats();
                if (accept == 1)
                {
                    generateBoats();
                    ui.drawBoard(grid);
                }
                else
                {
                    ui.drawBoard(grid);
                    placeBoatsOnBoard();
                }
		    	do
		    	{
		    		finished = false;
			    	if (turn == 0)
                    {

                        coordinate = ui.enterCoordinate(); // Human Guesses
                        if (coordinate.equals("q,0") || coordinate.equals("Q,0"))
                            turn = 2;
                        else
                        {
                            // TODO write code to display the position of hit ship on grid
                            // TODO FIX code where you hit a non existent ship on the AI's grid
                            results = opponent.checkIfHit(coordinate);
                            if (results[0] == true)
                            {
                                ui.displayHit(coordinate, turn);
                                opponent.markHitOnBoard(coordinate);
                            }

                            else
                            {
                                ui.displayMiss(coordinate, turn);
                                opponent.markHitOnBoard(coordinate);
                            }
                            if (results[1] == true)
                            {
                                ui.displaySunk(turn);
                                boatSunkAi++;
                            }
                            if (boatSunkAi == 15)
                                turn = 2;

                            turn = 1;
                        }
                    }

			    	if (turn == 1)
                    {
                        coordinate = opponent.getCoordinate(); // AI Guesses
                        results = checkIfHit(coordinate);
                        if (results[0] == true)
                        {
                            System.out.println("Enemy coordinate: " + coordinate);
                            markHitOnBoard(coordinate);
                            ui.displayHit(coordinate, turn);
                            ui.drawBoard(grid);
                        }

                        else
                        {
                            ui.displayMiss(coordinate, turn);
                            markHitOnBoard(coordinate);
                            ui.drawBoard(grid);
                        }

                        if (results[1] == true)
                        {
                            ui.displaySunk(turn);
                            boatSunkHuman++;
                        }
                        if (boatSunkHuman == 15)
                            turn = 2;

                        turn = 0;
                    }
                    ui.displayEndGame(boatSunkAi, boatSunkHuman);
		    	} while (turn != 2);

		    	finished = false;
		    	/*ui.drawBoard(grid);
                String coordinate = ui.enterCoordinate();
                enterCoordinateToGrid(coordinate);*/
            }
		    else
			    finished = true;
		} while (!finished);
	}

    private void markHitOnBoard(String coordinate)
    {
        String letters[] ={"A","B","C","D","E","F","G","H","I","J"};
        String parts[] = coordinate.split(",");
        boolean found = false;
        int letterPos = 0;

        for (int i = 0; i < letters.length && !found; i++)
        {
            if(letters[i].equals((parts[0])))
            {
                found = true;
                letterPos = i;
            }

        }
        grid[Integer.parseInt(parts[1]) - 1][letterPos] = "X";
    }

    private boolean[] checkIfHit(String coordinate)
    {
        boolean hit = false;
        boolean sunk = false;
        boolean results[] = new boolean[2];
        Boat b;

        // go through each boat, checking if the entered coordinate matches the position coordinate of any boat
        for (int i = 0; i < boats.size() && !hit; i++)
        {
            b = boats.get(i);               // takes a boat from the list of boats
            hit = b.checkIfHit(coordinate); // checks if entered coordinate has hit a part of that boat
            // if it has been hit, check if it has sunk
            if (hit)
                sunk = b.checkIfSunk();
        }
        results[0] = hit;
        results[1] = sunk;

        return results;
    }

    private void generateBoats()
    {
        String boatNames[] = {"Carrier", "Battleship", "Cruiser", "Submarine", "Destroyer"};
        ArrayList<String> points = new ArrayList<String>();

        for (int i = 0; i < boatNames.length; i++)
        {
            int index;

            if (boatNames[i].equals("Carrier"))
            {
                for (index = 0; index < 1; index++) // Carrier boats (5 spaces)(1 boats)
                {
                    points = generateBoatCoordinates(boatNames[i], 5);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    //displayAiBoats(boat);
                    //System.out.println(boat.getPoints());
                    drawBoatOntoGrid(boat, boatNames[i]);
                }
            }
            else if (boatNames[i].equals("Battleship"))
            {
                for (index = 0; index < 2; index++) // Battleship (4 spaces)(2 boats)
                {
                    points = generateBoatCoordinates(boatNames[i], 4);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    //displayAiBoats(boat);
                    //System.out.println(boat.getPoints());
                    drawBoatOntoGrid(boat, boatNames[i]);
                }
            }
            else if (boatNames[i].equals("Cruiser"))
            {
                for (index = 0; index < 3; index++) // cruisers (3 spaces)(3 boats)
                {
                    points = generateBoatCoordinates(boatNames[i], 3);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    //displayAiBoats(boat);
                    //System.out.println(boat.getPoints());
                    drawBoatOntoGrid(boat, boatNames[i]);
                }
            }
            else if (boatNames[i].equals("Submarine"))
            {
                for (index = 0; index < 4; index++) // battleship (3 spaces)(4 boats)
                {
                    points = generateBoatCoordinates(boatNames[i], 3);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    //displayAiBoats(boat);
                    //System.out.println(boat.getPoints());
                    drawBoatOntoGrid(boat, boatNames[i]);
                }
            }
            else if (boatNames[i].equals("Destroyer"))
            {
                for (index = 0; index < 5; index++) // carrier (2 spaces)(5 boats)
                {
                    points = generateBoatCoordinates(boatNames[i], 2);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    //displayAiBoats(boat);
                    //System.out.println(boat.getPoints());
                    drawBoatOntoGrid(boat, boatNames[i]);
                }
            }

        }
    }

    private ArrayList<String> generateBoatCoordinates(String boatName, int length)
    {
        // initializing
        ArrayList<String> points = new ArrayList<String>();
        //int lettersAsInts[] = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74};
        int pos = 0;
        int orientation = 0;
        int letter = 0;
        int number = 0;
        int distance;
        boolean correct = false;

        // Determine orientation
        do
        {
            correct = true;
            points = new ArrayList<String>();

            // Determine initial letter position
            letter = (int)(Math.random() * ((74 - 65) + 1)) + 65;
            // Determine initial number position
            number = (int)((Math.random() * 10) + 1);
            //Determine orientation of boat
            orientation = (int)((Math.random() * 2) + 1);

            if (letter + length > 74 || number + length > 10)
            {
                correct = false;
            }
            else
            {
                int j;
                if (orientation == 1)
                {
                    for (j = 1; j <= length; j++)
                    {
                        points.add((char)letter + "," + number);
                        letter++;
                    }
                    correct = checkForOverlapping(points);
                }
                else
                {
                    for (j = 0; j < length; j++)
                    {
                        points.add((char)letter + "," + number);
                        number++;
                    }
                    correct = checkForOverlapping(points);
                }
            }

        } while (!correct);

        return points;
    }

    private boolean checkForOverlapping(ArrayList<String> p)
    {
        boolean correct = true;
        for (int i = 0; i < boats.size() && correct; i++)
        {
            Boat b = boats.get(i);

            for (int j = 0; j < p.size(); j++)
                if ((b.checkIfOverlapsWithOther(p.get(j)) == true))
                    correct = false;
        }
        return correct;
    }

	private void placeBoatsOnBoard()
	{
		String boatNames[] = {"Carrier", "Battleship", "Cruiser", "Submarine", "Destroyer"};
        ArrayList<String> points = new ArrayList<String>();

		for (int i = 0; i < boatNames.length; i++)
		{
			int index;

			if (boatNames[i].equals("Carrier"))
            {
                for (index = 0; index < 1; index++) // Carrier boats (5 spaces)(1 boats)
                {
                    points = ui.getPoints(boatNames[i], 5);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    drawBoatOntoGrid(boat, boatNames[i]);
                }
            }
            else if (boatNames[i].equals("Battleship"))
            {
                for (index = 0; index < 2; index++) // Battleship (4 spaces)(2 boats)
                {
                    points = ui.getPoints(boatNames[i], 4);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    drawBoatOntoGrid(boat, boatNames[i]);
                }
            }
            else if (boatNames[i].equals("Cruiser"))
            {
                for (index = 0; index < 3; index++) // cruisers (3 spaces)(3 boats)
                {
                    points = ui.getPoints(boatNames[i], 3);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    drawBoatOntoGrid(boat, boatNames[i]);
                }
            }
            else if (boatNames[i].equals("Submarine"))
            {
                for (index = 0; index < 4; index++) // Submarine (3 spaces)(4 boats)
                {
                    points = ui.getPoints(boatNames[i], 3);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    drawBoatOntoGrid(boat, boatNames[i]);
                }
            }
            else if (boatNames[i].equals("Destroyer"))
            {
                for (index = 0; index < 5; index++) // Destroyer (2 spaces)(5 boats)
                {
                    points = ui.getPoints(boatNames[i], 2);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    drawBoatOntoGrid(boat, boatNames[i]);
                }
            }
		}
	}

    private void drawBoatOntoGrid(Boat boat, String boatName) {
	    String identification = "";
        String letters[] ={"A","B","C","D","E","F","G","H","I","J"};
        String parts[] = new String[0];
        boolean found;
        int letterPos = 0;

	    switch(boatName)
        {
            case "Destroyer":   identification = "D"; break;
            case "Submarine":   identification = "S"; break;
            case "Cruiser":     identification = "C"; break;
            case "Battleship":  identification = "B"; break;
            case "Carrier":     identification = "A"; break;
        }

        ArrayList<String> points = boat.getPoints();

	    for (int i = 0; i < points.size(); i++)
        {
            parts = points.get(i).split(",");
            found = false;
            for (int j = 0; j < letters.length && !found; j++)
            {
                if (parts[0].equalsIgnoreCase(letters[j]))
                {
                    found = true;
                    letterPos = j;
                }
            }
            grid[Integer.parseInt(parts[1]) - 1][letterPos] = identification;
        }
    }
}
