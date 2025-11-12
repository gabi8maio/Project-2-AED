/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */

import dataStructures.Iterator;
import dataStructures.TwoWayIterator;
import homeaway.*;
import homeaway.Exeptions.*;
import java.util.Scanner;

public class Main {

    // Constants for output messages
    private static final String CMD_NOT_EXIST = "Unknown command. Type help to see available commands.";
    private static final String END = "Bye!";
    private static final String BOUNDS_CREATED = "%s created.\n";
    private static final String BOUNDS_SAVED = "%s saved.\n";
    private static final String BOUNDS_LOADED = "%s loaded.\n";
    private static final String BOUNDS_NOT_DEFINED = "System bounds not defined.";
    private static final String USERS_COMMAND = "%s: %s\n";
    private static final String RANKING_COMMAND = "%s: %d\n";
    private static final String RANKED_COMMAND = "%s services closer with %d average\n";
    private static final String LESS_SIGNAL = "<";
    private static final String TAG_COMMAND = "%s %s\n";
    private static final String SERVICE_ADDED = "%s %s added.\n";
    private static final String STUDENT_ADDED = "%s added.\n";
    private static final String STUDENT_LEFT = "%s has left.\n";
    private static final String STUDENT_DISTRACTED = "%s is now at %s. %s is distracted!\n";
    private static final String STUDENT_NOW_AT = "%s is now at %s.\n";
    private static final String MOVE_HOME = "lodging %s is now %s's home. %s is at home.\n";
    private static final String WHERE_FORMAT = "%s is at %s %s (%d, %d).\n";
    private static final String NO_VISITED_LOCATIONS = "%s has not visited any locations!\n";
    private static final String EVALUATION_REGISTERED = "Your evaluation has been registered!";
    private static final String SERVICES_SORTED_HEADER = "Services sorted in descending order";
    private static final String SERVICES_COMMAND = "%s: %s (%d, %d).\n";
    private static final String STUDENTS_COMMAND = "%s: %s at %s.\n";

    /**
     * Method to get the command
     * @param input - Scanner
     * @return - The command
     */
    private static Command getCommand(Scanner input) {
        try {
            String comm = input.next().toUpperCase();
            return Command.valueOf(comm);
        } catch (IllegalArgumentException e) {
            return Command.UNKNOWN;
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        HomeAwaySystem system = new HomeAwaySystemClass();
        Command cmd;
        do {
            cmd = getCommand(in);
            switch (cmd) {
                case HELP -> {
                    executeHelp();
                    in.nextLine();
                }
                case EXIT -> {
                    executeExit(system);
                    System.out.println(END);
                }
                case BOUNDS -> executeBounds(in, system);
                case SAVE -> executeSave( system);
                case LOAD -> executeLoad(in, system);
                default -> {
                    // Are bounds defines to other commands
                    if (!system.hasBounds()) {
                        System.out.println(BOUNDS_NOT_DEFINED);
                        in.nextLine();
                    } else {
                        switch (cmd) {
                            case SERVICE -> executeService(in, system);
                            case SERVICES -> executeServices( system);
                            case STUDENT -> executeStudent(in, system);
                            case STUDENTS -> executeStudents(in, system);
                            case LEAVE -> executeLeave(in, system);
                            case GO -> executeGo(in, system);
                            case MOVE -> executeMove(in, system);
                            case USERS -> executeUsers(in, system);
                            case WHERE -> executeWhere(in, system);
                            case VISITED -> executeVisited(in, system);
                            case STAR -> executeStar(in, system);
                            case RANKING -> executeRanking( system);
                            case RANKED -> executeRanked(in, system);
                            case TAG -> executeTag(in, system);
                            case FIND -> executeFind(in, system);
                            case UNKNOWN -> {
                                System.out.println(CMD_NOT_EXIST);
                                in.nextLine();
                            }
                        }
                    }
                }
            }
        } while (!cmd.equals(Command.EXIT));
        in.close();
    }

    /**
     * Executes the "bounds" command
     * @param in - Scanner
     * @param system - System class
     */
    private static void executeBounds(Scanner in, HomeAwaySystem system) {
        String name;
        try {
            long topLatitude = in.nextLong();
            long leftLongitude = in.nextLong();
            long bottomLatitude = in.nextLong();
            long rightLongitude = in.nextLong();
            name = in.nextLine().trim();
            system.addTemporaryArea(name, topLatitude, bottomLatitude, leftLongitude, rightLongitude);
            System.out.printf(BOUNDS_CREATED, name);
        } catch (BoundsAlreadyExistException | InvalidBoundsException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Executes the "save" command
     * @param system - System class
     */
    private static void executeSave( HomeAwaySystem system) {
        try {
            String areaName = system.saveArea();
            System.out.printf(BOUNDS_SAVED, areaName);
        } catch (SystemBoundsNotDefinedException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Execute the "load" command
     * @param in - Scanner
     * @param system - System class
     */
    private static void executeLoad(Scanner in, HomeAwaySystem system) {
        String areaName;
        String realAreaName = null; // Porque nos "outputs" vem o nome da area
        try {
            areaName = in.nextLine().trim();
            realAreaName = system.loadArea(areaName);
            System.out.printf(BOUNDS_LOADED, realAreaName);
        } catch (BoundsDoesNotExistException e) {
            System.out.printf(e.getMessage(), realAreaName);
        }
    }

    /**
     * Execute the "services" command
     * @param in - Scanner
     * @param system - System class
     */
    private static void executeService(Scanner in, HomeAwaySystem system) {
        String serviceType = in.next().toUpperCase().trim();
        long latitude = in.nextLong();
        long longitude = in.nextLong();
        double price = in.nextDouble();
        int value = in.nextInt();
        String serviceName = in.nextLine().trim();

        try{
            system.addService(serviceType, latitude, longitude, price, value, serviceName);
            System.out.printf(SERVICE_ADDED, serviceType.toLowerCase(), serviceName);

        } catch(InvalidServiceTypeException | InvalidLocationException | InvalidPriceMenuException | InvalidRoomPriceException |
            InvalidTicketPriceException | InvalidDiscountException | InvalidCapacityException e) {
            System.out.println(e.getMessage());
        } catch ( ServiceAlreadyExistsException e){
            System.out.print(e.getMessage());
        }
    }


    /**
     * Execute the "services" command
     * @param system - System class
     */
    private static void executeServices(HomeAwaySystem system) {
        try {
            Iterator<Services> serviceIterator = system.getServiceIterator();
            while (serviceIterator.hasNext()) {
                Services service = serviceIterator.next();
                System.out.printf(SERVICES_COMMAND, service.getServiceName(),
                        service.getServiceType().toLowerCase(),
                        service.getLatitude(),
                        service.getLongitude());
            }
        }catch(NoServicesYetException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Execute the "student" command
     * @param in - Scanner
     * @param system - System class
     */
    private static void executeStudent(Scanner in, HomeAwaySystem system) {

        String studentType = in.nextLine().toLowerCase().trim();
        String name = in.nextLine().trim();
        String country = in.nextLine().trim();
        String lodging = in.nextLine().trim();

        try{
            system.addStudent(studentType, name, country, lodging);
            System.out.printf(STUDENT_ADDED, name);

        }catch (InvalidStudentTypeException | LodgingNotExistsException | LodgingIsFullException |
                StudentAlreadyExistsException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Execute the "students" command
     * @param in - Scanner
     * @param system - System class
     */
    private static void executeStudents(Scanner in, HomeAwaySystem system) {
        String argument = in.nextLine().trim();
        try {
            Iterator <Students> it = system.getStudentsIterator(argument);

            while (it.hasNext()) {
                Students student = it.next();
                System.out.printf(STUDENTS_COMMAND,
                    student.getName(),
                    student.getType().toLowerCase(),
                    student.getPlaceNow().getServiceName());
            }

        } catch (NoStudentsException e) {
            System.out.println(e.getMessage());
        } catch  (NoStudentsFromCountryException e) {
            System.out.printf(e.getMessage(), argument);
        }
    }

    /**
     * Executes the "leave" command
     * @param in - Scanner
     * @param system - System class
     */
    private static void executeLeave(Scanner in, HomeAwaySystem system){
        String name = in.nextLine().trim();
        try {

            System.out.printf(STUDENT_LEFT, system.removeStudent(name).getName());

        } catch (StudentDoesNotExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Executes the "go" command
     * @param in - Scanner
     * @param system - System class
     */
    private static void executeGo(Scanner in, HomeAwaySystem system) {
        String studentName = in.nextLine().trim();
        String locationName = in.nextLine().trim();

        try {

            Students studentNameReal = system.goStudentToLocation(studentName, locationName);

            if(system.isServiceMoreExpensiveForThrifty(studentName, locationName))
                System.out.printf(STUDENT_DISTRACTED,studentNameReal.getName(),studentNameReal.getPlaceNow().getServiceName(),studentNameReal.getName());
            else
                System.out.printf(STUDENT_NOW_AT,studentNameReal.getName(),studentNameReal.getPlaceNow().getServiceName());

        } catch (StudentAlreadyThereException | StudentAlreadyExistsException | InvalidServiceException |
                 StudentDoesNotExistsException e) {
            System.out.println(e.getMessage());
        } catch (UnknownLocationException | EatingIsFullException e){
            System.out.printf(e.getMessage(), locationName);
        }
    }

    /**
     * Executes the "move" command
     * @param in - Scanner
     * @param system - System class
     */
    private static void executeMove(Scanner in, HomeAwaySystem system) {
        String studentName = in.nextLine().trim();
        String locationName = in.nextLine().trim();

        try {

            Students student = system.moveStudentToLocation(studentName, locationName);
            System.out.printf(MOVE_HOME,student.getPlaceHome().getServiceName(),student.getName(),student.getName());
        } catch (LodgingNotExistsException | StudentDoesNotExistsException | StudentHomeException | LodgingIsFullException | MoveNotAcceptableException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Executes the "users" command
     * @param in - Scanner
     * @param system - System class
     */
    private static void executeUsers(Scanner in, HomeAwaySystem system) {
        String order = in.next().trim();
        String serviceName = in.nextLine().trim();

        try {
            TwoWayIterator<Students> studentIterator = system.usersCommand(order, serviceName);

            if(order.equals(LESS_SIGNAL)) {
                studentIterator.fullForward();
                while (studentIterator.hasPrevious()) {

                    Students student = studentIterator.previous();
                    System.out.printf(USERS_COMMAND,
                            student.getName(),
                            student.getType().toLowerCase());
                }
            }else{
                while (studentIterator.hasNext()) {
                 Students student = studentIterator.next();
                      System.out.printf(USERS_COMMAND,
                        student.getName(),
                        student.getType().toLowerCase());
                }
            }
        }catch(NoStudentsOnServiceException | OrderNotExistsException | ServiceDoesNotExistException | ServiceNotControlEntryExitException e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * Executes the "where" command
     * @param in - Scanner
     * @param system - System class
     */
    private static void executeWhere(Scanner in, HomeAwaySystem system) {
        String studentName = in.nextLine().trim();

        try {

            Students student = system.getStudentLocationInfo(studentName);
            Services place = student.getPlaceNow();
            System.out.printf(WHERE_FORMAT, student.getName(), place.getServiceName(),place.getServiceType().toLowerCase(),place.getLatitude(),place.getLongitude());

        } catch (StudentDoesNotExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Executes the "visited" command
     * @param in - Scanner
     * @param system - System class
     */
    private static void executeVisited(Scanner in, HomeAwaySystem system) {
        String studentName = in.nextLine().trim();
        try {
            Iterator<Services> visitedIterator = system.getVisitedLocationsIterator(studentName);
            if (!visitedIterator.hasNext()) System.out.printf(NO_VISITED_LOCATIONS, studentName);
            else {
                while (visitedIterator.hasNext()) {
                    Services service = visitedIterator.next();
                    System.out.println(service.getServiceName());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Executes the "star" command
     * @param in - Scanner
     * @param system - System class
     */
    private static void executeStar(Scanner in, HomeAwaySystem system) {
        try {
            int stars = in.nextInt();
            String serviceName = in.nextLine().trim();
            String description = in.nextLine().trim();

            system.starCommand(stars, serviceName, description);
            System.out.println(EVALUATION_REGISTERED);

        } catch (InvalidEvaluationException | ServiceDoesNotExistException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Executes the "ranking" command
     * @param system - System class
     */
    private static void executeRanking( HomeAwaySystem system) {
        try {
            Iterator<Services> rankingIterator = system.getServicesByRankingIterator();
            System.out.println(SERVICES_SORTED_HEADER);
            while (rankingIterator.hasNext()) {
                Services service = rankingIterator.next();
                    System.out.printf(RANKING_COMMAND, service.getServiceName(), service.getAverageStars());
            }
        } catch (NoServicesInSystemException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Executes the "ranked" command
     * @param in - Scanner
     * @param system - System class
     */
    private static void executeRanked(Scanner in, HomeAwaySystem system) {
        try {
            String type = in.next().trim();
            int stars = in.nextInt();
            String studentName = in.nextLine().trim();

            Iterator<Services> rankedIterator = system.getRankedServicesIterator(stars, type, studentName);

            System.out.printf(RANKED_COMMAND, type, stars);
            while (rankedIterator.hasNext()) {
                Services service = rankedIterator.next();
                System.out.println(service.getServiceName());
            }

        } catch (InvalidStarsException | StudentDoesNotExistsException | InvalidServiceTypeException | NoTypeServicesException | NoServicesWithAverage e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Executes the "tag" command
     * @param in - Scanner
     * @param system - System class
     */
    private static void executeTag(Scanner in, HomeAwaySystem system) {
        String tag = in.nextLine().trim();
        try {
            Iterator<Services> tagIterator = system.getServicesByTagIterator(tag);

            while (tagIterator.hasNext()) {
                Services service = tagIterator.next();
                System.out.printf(TAG_COMMAND, service.getServiceType().toLowerCase(), service.getServiceName());
            }

        } catch (NoServicesWithTagException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Executes the "find" command
     * @param in - Scanner
     * @param system - System class
     */
    private static void executeFind(Scanner in, HomeAwaySystem system) {
        try {
            String studentName = in.nextLine().trim();
            String serviceType = in.nextLine().trim();

            Services service = system.findMostRelevantService(studentName, serviceType);
            System.out.println(service.getServiceName());
        } catch (InvalidServiceTypeException | StudentDoesNotExistsException | NoTypeServicesException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Executes the "exit" command
     * @param system - System class
     */
    private static void executeExit(HomeAwaySystem system) {
        system.saveArea();
    }

    /**
     * Executes the "help" command
     */
    private static void executeHelp() {
        Command[] help = Command.values();
        for (int i = 0; i < help.length - 1; i++) {
            System.out.println(help[i].getMsg());
        }
    }
}