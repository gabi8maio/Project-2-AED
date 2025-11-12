/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway;

import dataStructures.Iterator;
import dataStructures.TwoWayIterator;
import homeaway.Exeptions.*;

import java.io.Serializable;

public interface HomeAwaySystem extends Serializable {


    /**
     * Creates a temporary area, this is an area that is kept while the command "save" is not used.
     * @param name - The name of the area
     * @param topLatitude - The top limit of the area (latitude)
     * @param bottomLatitude - The bottom limit of the area (latitude)
     * @param leftLongitude - The left limit of the area (longitude)
     * @param rightLongitude - The right limit of the area (longitude)
     * @throws BoundsAlreadyExistException - If there is already a temporary area (an area that was not saved yet)
     * @throws InvalidBoundsException - In the case of the topLatitude being less or equal than the bottomLatitude or
     * the leftLongitude being less or equal than the rightLongitude
     */
    void addTemporaryArea(String name, long topLatitude, long bottomLatitude, long leftLongitude, long rightLongitude)
            throws BoundsAlreadyExistException, InvalidBoundsException;

    /**
     * Asks the load area (current area) to the iterator of the services
     * @return -
     */
    Iterator<Services> getServiceIterator();

    /**
     * Checks if there is any area with the given name (if there is an area at the dat structure with the name given)
     * @param name - The name of the area
     * @return True if there is any area with the name, false else
     */
    boolean hasArea(String name);

    /**
     * Checks if there are any bounds created that were not saved yet
     * @return True if there are, False if there are not
     */
    boolean hasBounds();

    /**
     * Saves the current area
     * @return The name of the area being saved
     * @throws SystemBoundsNotDefinedException - If there is not a temporary area, this is, there are not bounds created without being saved
     */
    String saveArea() throws SystemBoundsNotDefinedException;

    /**
     * Loads an area with the given name, turn that area into the current area
     * @param name - The name of the area being loaded
     * @return The name of the area being loaded if successful or null if not successful
     * @throws BoundsDoesNotExistException - If there is already a temporary area (an area that was not saved yet)
     */
    String loadArea(String name) throws BoundsDoesNotExistException;

    /**
     * Gets the saved name (case-insensitive) of a certain service if the certain service exists
     * @param name - The name of the student
     * @return The real name of the service if the service exists, null if the service does not exist
     */
    String serviceNameExists(String name);

    /**
     * Adds a service to the current area
     * @param serviceType - The tyoe of the service being added
     * @param latitude - The latitude of the service
     * @param longitude - The longitude of the service
     * @param price - The price of the service
     * @param value - The value of the service
     * @param serviceName - The name of the service
     * @throws InvalidServiceTypeException - If the type is not a valid type
     * @throws InvalidLocationException - if the latitude or longitude is out of the bounds defined
     * @throws InvalidPriceMenuException - if the price is less or equal than 0 (if type is eating)
     * @throws InvalidRoomPriceException - if the price is less or equal than 0 (if type is lodging)
     * @throws InvalidTicketPriceException - if the price is less or equal than 0 (if type is leisure)
     * @throws InvalidDiscountException - if the value is less than 0 or greater than 100 (for leisure services)
     * @throws InvalidCapacityException - if the value is less or equal to 0 (for eating and lodging services)
     * @throws ServiceAlreadyExistsException - if there is already a service with that name
     */
    void addService(String serviceType, long latitude, long longitude, double price, int value, String serviceName)
            throws InvalidServiceTypeException, InvalidLocationException, InvalidPriceMenuException, InvalidRoomPriceException, InvalidTicketPriceException, InvalidDiscountException, InvalidCapacityException, ServiceAlreadyExistsException;

    /**
     * Checks if exists a lodging service with the given name
     * @param name - The name of the lodging being checked
     * @return True if the lodging service exists, false if it does not exist
     */
    boolean lodgingExists(String name);

    /**
     * Asks the current area if the lodging with the given name is full
     * @param name - The name of the lodging service being checked
     * @return True if its full, False if not
     */
    String lodgingIsFull(String name);

    /**
     * Handles the "star" command, tells the loaded area to update the average rating of a certain service
     * @param rating - The rating given by the student to the service
     * @param serviceName - The name of the service being evaluated
     * @param tag - The tag given by the student to the service
     * @throws InvalidEvaluationException - If the evaluation given by the student is less than 0 or greater than 5
     * @throws ServiceDoesNotExistException - if the service does not exist in the current area
     */
    void starCommand(int rating, String serviceName, String tag)
            throws InvalidEvaluationException, ServiceDoesNotExistException;

    /**
     * Iterates the services by the ranking order - First by the number of stars then order by which average was last updated
     * @return An iterator of the services ordered by ranking
     * @throws NoServicesInSystemException - If there are no services in the system
     */
    Iterator<Services> getServicesByRankingIterator()
            throws NoServicesInSystemException;

    /**
     * Iterates the services tagged with a given tag
     * @param tag - The tag that the service needs to be reviewed with to be printed
     * @return - The iterator of te services reviewed with the given tag
     * @throws NoServicesWithTagException - If there are no services tagged with the tag given in the parameters
     */
    Iterator<Services> getServicesByTagIterator(String tag) throws NoServicesWithTagException;

    /**
     * @param stars - the number of stars to get
     * @param type - the type of service
     * @param studentName - the student's name
     * @return - returns an Iterator with the services of a certain type order by a certain star,
     *           that are closer to the student
     * @throws InvalidStarsException - If the stars given are invalid number (n < 0 or n > 5)
     * @throws StudentDoesNotExistsException - If the student given does not exist
     * @throws InvalidServiceTypeException - If the service type is invalid
     * @throws NoTypeServicesException - If there are no services with certain type
     * @throws NoServicesWithAverage - If there are no Services with certain average
     */
    Iterator<Services> getRankedServicesIterator(int stars, String type, String studentName) throws InvalidStarsException, StudentDoesNotExistsException, InvalidServiceTypeException, NoTypeServicesException, NoServicesWithAverage;

    /**
     * @param studentName - the student's name
     * @param serviceType - the service type
     * @return - returns the most relevant Service for the certain student
     * @throws InvalidServiceTypeException - if the service is not valid
     * @throws StudentDoesNotExistsException - if the student does not exist
     * @throws NoTypeServicesException - if there is no service with the certain type
     */
    Services findMostRelevantService(String studentName, String serviceType) throws InvalidServiceTypeException, StudentDoesNotExistsException, NoTypeServicesException;

    /**
     * @param name - Student's name
     * @return - returns the string of the name of the Student if exists, if not null
     */
    String studentExists(String name);

    /**
     * Adds a Student to the System
     * @param studentType - The student's type
     * @param name - the Student Name
     * @param country - The student's name
     * @param lodging - The student's home
     * @throws InvalidStudentTypeException - if the student type is not valid
     * @throws LodgingNotExistsException - if the lodging does not exist
     * @throws LodgingIsFullException - if the lodging is full of Students
     * @throws StudentAlreadyExistsException - if the student already exists
     */
    void addStudent(String studentType, String name, String country, String lodging) throws InvalidStudentTypeException, LodgingNotExistsException, LodgingIsFullException, StudentAlreadyExistsException;

    /**
     * @param studentName - the Student Name
     * @return - returns the Object of the student removed
     * @throws StudentDoesNotExistsException - if the student name does not exist
     */
    Students removeStudent(String studentName) throws StudentDoesNotExistsException;

    /**
     * @param studentName - The student name
     * @return - returns the Object of the student changed
     * @throws StudentDoesNotExistsException - if the student name does not exist
     */
    Students getStudentLocationInfo(String studentName) throws StudentDoesNotExistsException;

    /**
     * @param studentName - The student's  name
     * @param locationName - The location name
     * @return - returns the location that the Student moved in
     * @throws LodgingNotExistsException - if the lodging given doesn't exist
     * @throws StudentDoesNotExistsException - if the student does not exist on this area
     * @throws StudentHomeException - if the student is already at home
     * @throws LodgingIsFullException - if the  lodging that is trying to go is full of Students
     * @throws MoveNotAcceptableException - if the student is thrifty and the price of the new lodging
     *                                      is more expensive than the one that is currently in
     */
    Students moveStudentToLocation(String studentName, String locationName) throws LodgingNotExistsException, StudentDoesNotExistsException, StudentHomeException, LodgingIsFullException, MoveNotAcceptableException;

    /**
     * @param studentName - The student name
     * @param locationName - The student Location
     * @return - returns an Object of the student that was moved from place
     * @throws UnknownLocationException - If the location is unknown from the area
     * @throws StudentDoesNotExistsException-  If the student does not exist
     * @throws InvalidServiceException - If the service is eating or leisure service
     * @throws StudentAlreadyThereException - If the student is already on that place
     * @throws EatingIsFullException - if the eating type service is already full
     */
    Students goStudentToLocation(String studentName, String locationName) throws UnknownLocationException, StudentDoesNotExistsException, InvalidServiceException, StudentAlreadyThereException, EatingIsFullException;

    /**
     * @return - returns an Iterator with every Student
     */
    Iterator<Students> getAllStudentsIterator();
    /**
     * @param country - The name of the country
     * @return - returns a Iterator with Students of a certain country
     */
    Iterator<Students> getStudentsByCountryIterator(String country);

    /**
     * @param argument - The argument given by the user, can be all to display every Student, or The name of
     *                 a country to see the Students
     * @return - returns an Iterator with the students
     * @throws NoStudentsException - If there are no Students with a certain name
     * @throws NoStudentsFromCountryException - If there are no Student from a certain country
     */
    Iterator<Students> getStudentsIterator(String argument) throws NoStudentsException, NoStudentsFromCountryException;
    /**
     * @param studentName - The student's name
     * @return - returns the Iterator with the visited locations made by the student
     */
    Iterator<Services> getVisitedLocationsIterator(String studentName);
    /**
     * @param order - The order to show the students
     * @param serviceName - The Service name
     * @return - returns an Iterator with the correct order to show students
     * @throws NoStudentsOnServiceException - if there are no Students on the service
     * @throws ServiceDoesNotExistException - if the service given does not Exist
     * @throws ServiceNotControlEntryExitException - if the service does not control the entry and exit fo students
     */
    TwoWayIterator<Students> usersCommand(String order, String serviceName) throws NoStudentsOnServiceException, ServiceDoesNotExistException, ServiceNotControlEntryExitException;
    /**
     * Method to check if a move is acceptable under certain conditions
     * @param studentName - The name of the student that is going to be moved
     * @param serviceName - The location to where the student is intended to be moved
     * @return True if is acceptable, False if is not acceptable
     */
    boolean isServiceMoreExpensiveForThrifty(String studentName, String serviceName);
}
