/*
    Conventions for coding game map:
        0. represents empty house
        1. represents player's ship
            10. represents player's destroyed ship
        2. represents computer's ship
            20. represents computer's destroyed ship
        3. represents missed computer's attack
        4. represents missed player's attack
        34. represents missed player's and computer's attack
 */
import java.util.Random;
import java.util.Scanner;
public class BattleShip {
    // Console coloring escaping codes
    public static int[][] board = new int[10][10];
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static void main (String[] args){
        int[][] MAP = new int[10][10];
        Scanner console = new Scanner(System.in);
        startGame(MAP, console);
        board[0][0] = 1;
    }
    public static void startGame(int[][] map, Scanner console){
        System.out.println("***  Welcome to Battle Ships game  ***\n");
        System.out.println("This is the ocean map right now:");
        printMap(map);
        updateMap(map, deployPlayerShips(map, console));
        updateMap(map, deployComputerShips(map, console));
        System.out.println("\nStarting battle:\n");
        battleShips(map, console);
    }
    public static void battleShips (int[][] map, Scanner console){
        int turnCounter = 0;
        while (!checkGameFinished(map)){
            updateMap(map, takeTurn(map, turnCounter, console));
            if(turnCounter%2 == 0) System.out.println();
            if(turnCounter%2 == 1) printMap(map);
            turnCounter++;
        }
        if (checkGameFinished(map, true).equals("PLAYER")){
            System.out.println(ANSI_GREEN + "HOORAY! You won the battle!" + ANSI_RESET);
        } else if (checkGameFinished(map, true).equals("COMPUTER")) {
            System.out.println(ANSI_RED + "GAME OVER! Computer won the battle!" + ANSI_RESET);
        }
    }
    public static String checkGameFinished (int[][] map, boolean determineWinner){
        int computerRemainingShips = 5;
        int playerRemainingShips = 5;
        for(int i = 0; i < map.length; i++){
            for (int j = 0; j < map[i].length; j++){
                if (map[i][j] == 10){
                    playerRemainingShips--;
                } else if (map[i][j] == 20){
                    computerRemainingShips--;
                }
            }
        }
        if (computerRemainingShips == 0) {
            return "PLAYER";
        } else if (playerRemainingShips == 0) {
            return "COMPUTER";
        } else {
            return "NO ONE";
        }
    }
    public static boolean checkGameFinished (int[][] map){
        int computerRemainingShips = 5;
        int playerRemainingShips = 5;
        for(int i = 0; i < map.length; i++){
            for (int j = 0; j < map[i].length; j++){
                if (map[i][j] == 10){
                    playerRemainingShips--;
                } else if (map[i][j] == 20){
                    computerRemainingShips--;
                }
            }
        }
        if (computerRemainingShips == 0 || playerRemainingShips == 0){
            return true;
        } else {
            return false;
        }
    }
    public static int[][] takeTurn(int[][] map, int turnCounter, Scanner console){
        int[][] newMap = copyArray(map);
        if(turnCounter%2 == 0){ // Player plays
            System.out.println("YOUR TURN");
            while(true){
                int[] onTarget = getCoordinates(console);
                if(newMap[onTarget[1]][onTarget[0]] == 0){
                    newMap[onTarget[1]][onTarget[0]] = 4;
                    System.out.println(ANSI_WHITE + "You missed." + ANSI_RESET);
                    break;
                } else if (newMap[onTarget[1]][onTarget[0]] == 1){
                    System.out.println(ANSI_YELLOW + "You are targeting yourself. Try again:" + ANSI_RESET);
                    continue;
                } else if (newMap[onTarget[1]][onTarget[0]] == 2){
                    newMap[onTarget[1]][onTarget[0]] = 20;
                    System.out.println(ANSI_GREEN + "BOOM! You hit computer's ship." + ANSI_RESET);
                    break;
                } else if (newMap[onTarget[1]][onTarget[0]] == 3){
                    newMap[onTarget[1]][onTarget[0]] = 34;
                    System.out.println(ANSI_WHITE + "You missed." + ANSI_RESET);
                    break;
                } else if (newMap[onTarget[1]][onTarget[0]] == 4){
                    System.out.println(ANSI_YELLOW + "You are wasting your time. Try again:" + ANSI_RESET);
                    continue;
                } else if (newMap[onTarget[1]][onTarget[0]] == 34) {
                    System.out.println(ANSI_YELLOW + "You are wasting your time. Try again:" + ANSI_RESET);
                    continue;
                } else if (newMap[onTarget[1]][onTarget[0]] == 10) {
                    System.out.println(ANSI_YELLOW + "You are wasting your time. Try again:" + ANSI_RESET);
                    continue;
                } else if (newMap[onTarget[1]][onTarget[0]] == 20) {
                    System.out.println(ANSI_YELLOW + "You are wasting your time. Try again:" + ANSI_RESET);
                    continue;
                }
            }
        } else { // Computer plays
            System.out.println("COMPUTER'S TURN");
            while(true){
                int[] onTarget = getRandomCoordinates();
                if(newMap[onTarget[1]][onTarget[0]] == 0){
                    newMap[onTarget[1]][onTarget[0]] = 3;
                    System.out.println("Computer missed.");
                    break;
                } else if (newMap[onTarget[1]][onTarget[0]] == 1){
                    newMap[onTarget[1]][onTarget[0]] = 10;
                    System.out.println(ANSI_RED + "BOOM! Computer hit your ship." + ANSI_RESET);
                    break;
                } else if (newMap[onTarget[1]][onTarget[0]] == 2){
                    //System.out.println("Computer is targeting itself. Try again.");
                    continue;
                } else if (newMap[onTarget[1]][onTarget[0]] == 3){
                    //System.out.println("Computer is wasting its time. Try again.");
                    continue;
                } else if (newMap[onTarget[1]][onTarget[0]] == 4){
                    newMap[onTarget[1]][onTarget[0]] = 34;
                    System.out.println("Computer missed.");
                    break;
                } else if (newMap[onTarget[1]][onTarget[0]] == 34) {
                    //System.out.println("Computer is wasting its time. Try again.");
                    continue;
                } else if (newMap[onTarget[1]][onTarget[0]] == 10) {
                    //System.out.println("Computer is wasting its time. Try again.");
                    continue;
                } else if (newMap[onTarget[1]][onTarget[0]] == 20) {
                    //System.out.println("Computer is wasting its time. Try again.");
                    continue;
                }
            }
        }
        return newMap;
    }
    public static int[] getRandomCoordinates (){
        Random rand = new Random();
        int[] randomCoordinates = new int[2];
        randomCoordinates[0] = rand.nextInt(10);
        randomCoordinates[1] = rand.nextInt(10);
        return randomCoordinates;
    }
    public static int[] getCoordinates (Scanner console){
        int[] coordinates = new int[2];
        int X_Coordinate;
        int Y_Coordinate;
        boolean outerLoopContinueFlow = true;
        while (outerLoopContinueFlow) {
            while (true) {
                System.out.print("Enter X coordinate: ");
                X_Coordinate = console.nextInt();
                if (!(X_Coordinate >= 0) || !(X_Coordinate <= 9)) {
                    System.out.println(ANSI_YELLOW + "Coordinate out of range. Try again:" + ANSI_RESET);
                    continue;
                }
                coordinates[0] = X_Coordinate;
                outerLoopContinueFlow = false;
                break;
            }
            while (true) {
                System.out.print("Enter Y coordinate: ");
                Y_Coordinate = console.nextInt();
                if(Y_Coordinate == -1){
                    outerLoopContinueFlow = true;
                    break;
                }
                if (!(Y_Coordinate >= 0) || !(Y_Coordinate <= 9)) {
                    System.out.println(ANSI_YELLOW + "Coordinate out of range. Try again:" + ANSI_RESET);
                    continue;
                }
                coordinates[1] = Y_Coordinate;
                break;
            }
        }
        return coordinates;
    }
    public static int[][] deployComputerShips(int[][] map, Scanner console){
        System.out.println("Computer is deploying ships:");
        int[][] computerDeployedMap = copyArray(map);
        for(int i = 1; i<=5; i++){
            System.out.println("*** SHIP NO." + i + " ***");
            while (true){
                int[] thisShipLocation = getRandomCoordinates();
                if(computerDeployedMap[thisShipLocation[1]][thisShipLocation[0]] == 0) {
                    computerDeployedMap[thisShipLocation[1]][thisShipLocation[0]] = 2;
                    System.out.println(ANSI_BLUE + "Deployment done!" + ANSI_RESET);
                    break;
                }
                //System.out.println("Computer has entered a repeated location for its ships. Try again:");
            }
        }
        return computerDeployedMap;
    }
    public static int[][] copyArray (int[][] array){
        int[][] copiedArray = new int[array.length][array[0].length];
        for(int m = 0; m < array.length; m++){
            for(int n = 0; n < array[m].length; n++){
                copiedArray[m][n] = array[m][n];
            }
        }
        return copiedArray;
    }
    public static int[][] deployPlayerShips(int[][] map, Scanner console){
        System.out.println("Deploy your ships:");
        System.out.println("You have to determine 5 coordinates as your ships locations in the ocean.\nNote:");
        System.out.println("\t1. Moving right-hand side increases X coordinate and moving downside increases Y coordinate.");
        System.out.println("\t2. You can type -1 when keying coordinate Y in order to change coordinate X.");
        System.out.println("\t\tExample:\n\t\tEnter coordinate X: 4\n\t\tEnter coordinate Y: -1\n\t\tEnter coordinate X: 2\n\t\tEnter coordinate Y: 5\n\t\t...");
        int[][] playerDeployedMap = copyArray(map);
        for(int i = 1; i<=5; i++){
            System.out.println("*** SHIP NO." + i + " ***");
            while (true){
                int[] thisShipLocation = getCoordinates(console);
                if(playerDeployedMap[thisShipLocation[1]][thisShipLocation[0]] == 0) {
                    playerDeployedMap[thisShipLocation[1]][thisShipLocation[0]] = 1;
                    System.out.println(ANSI_BLUE + "SHIP NO." + i + " deployed at (" + thisShipLocation[0] + ", " + thisShipLocation[1] + ")" + ANSI_RESET);
                    printMap(playerDeployedMap);
                    break;
                }
                System.out.println(ANSI_YELLOW + "You have entered a repeated location for your ships. Try again:" + ANSI_RESET);
            }
        }
        return playerDeployedMap;
    }
    // This method (updateMap) changes the main MAP Array through Reference Semantics
    public static void updateMap (int[][] originalMap, int[][] newMap){ // original map (the old one) is to be changed by new map
        for(int i = 0; i < originalMap.length; i++){
            for(int j = 0; j < originalMap[i].length; j++){
                originalMap[i][j] = newMap[i][j];
            }
        }
    }
    public static void printMap (int[][] map){
        System.out.println("\n   0123456789   ");
        for (int i = 0; i<=map.length-1; i++){
            System.out.print(i +  " |");
            for(int j = 0; j<=map[i].length-1; j++){
                if(map[i][j] == 0)  System.out.print(ANSI_BLUE_BACKGROUND + " " + ANSI_RESET);
                if(map[i][j] == 1)  System.out.print(ANSI_BLUE_BACKGROUND + ANSI_WHITE + "@" + ANSI_RESET);
                if(map[i][j] == 2)  System.out.print(ANSI_BLUE_BACKGROUND + " " + ANSI_RESET);
                if(map[i][j] == 3)  System.out.print(ANSI_BLUE_BACKGROUND + " " + ANSI_RESET); // Based on game rules, missed computer attacks are not shown to the player!
                if(map[i][j] == 4)  System.out.print(ANSI_WHITE_BACKGROUND + ANSI_BLACK + "-" + ANSI_RESET);
                if(map[i][j] == 34)  System.out.print(ANSI_WHITE_BACKGROUND + ANSI_BLACK + "-" + ANSI_RESET);
                if(map[i][j] == 10)  System.out.print(ANSI_RED_BACKGROUND + ANSI_BLACK + "x" + ANSI_RESET);
                if(map[i][j] == 20)  System.out.print(ANSI_GREEN_BACKGROUND + ANSI_BLACK + "x" + ANSI_RESET);
            }
            System.out.println("| " + i);
        }
        System.out.println("   0123456789   \n");
    }
}
