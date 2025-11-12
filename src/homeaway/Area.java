/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway;

import dataStructures.*;
import java.io.Serializable;

public interface Area extends Serializable {

    /**
     * Creates the service and adds it to the data structure used to keep all the services
     * @param serviceType - The type of the service
     * @param latitude - The latitude of the service
     * @param longitude - The longitude of the service
     * @param price - The price of the service
     * @param value - The value of the service (its real representation dependes on the type of service)
     * @param serviceName - The name of The service
     */
    void createService(String serviceType, long latitude, long longitude, double price, int value, String serviceName);

    /**
     * Adds/creates a student and adds it to the data structure used to keep all the students
     * @param studentType - The type of student
     * @param name - The name of the student
     * @param country - The country where the student was born
     * @param lodging - The place which will be the student house
     */
    void addStudent(String studentType, String name, String country, String lodging);

    /**
     * Removes the student from the Area
     * @param studentName - The name of the student to remove
     * @return - The "object" of the student that was removed
     */
    Students removeStudent(String studentName);

    /**
     * Moves the student from a lodging service to another lodging service
     * @param studentName - The name of the student to move
     * @param serviceName - The name of the service to where the student will be moved
     * @return - The student that was moved
     */
    Students moveStudentToLocation(String studentName, String serviceName);

    /**
     * Changes the current location to another one
     * @param studentName - The student to change the location
     * @param serviceName - The name of the service to where the student will go
     * @return - The moved student
     */
    Students goStudentToLocation(String studentName, String serviceName);

    /**
     * Adds a new rating(star) and tags to a certain service
     * @param rating - The stars given by the student
     * @param serviceName - The name of the service being rated
     * @param tag - The tags to be added to the service
     */
    void starCommand(int rating, String serviceName, String tag);

    /**
     * @return - An iterator of the all the services
     */
    Iterator<Services> getServicesIterator();

    /**
     * @return - An iterator of all the students
     */
    Iterator<Students> getAllStudentsIterator();

    /**
     * Method used to iterate the student os a given country
     * Complexity:  Best case: O(1)
     *              Worst case: O(n)
     * @param country - The country from where wa want to iterate
     * @return - The iterator of the students from the country
     */
    Iterator<Students> getStudentsByCountryIterator(String country);

    /**
     * Complexity: Best case: O(n)
     *             Worst case: O(n*m)
     * @param tag - Tag chosen to see
     * @return - returns an Iterator that has the tag of a certain Service
     */
    Iterator<Services> getServicesByTagIterator(String tag);

    /**
     * Complexity: Best case: O(n)
     *               Worst case: O(n*m)
     * @param stars - The stars given
     * @param type - The type of Students
     * @param studentName - The Student name
     * @return - returns an iterator of services that is sorted by star rating in descending order
     *  (highest rated first). The Services that have equal ratings will be sorted by most recent update
     */
    Iterator<Services> getRankedServicesIterator(int stars, String type, String studentName);

    /**
     * @param serviceName - The name of the Service given
     * @return - returns an Iterator with the students that are on a certain service given
     */
    TwoWayIterator<Students> getStudentsByService(String serviceName);

    /**
     * Method used to iterate the services ordered by the number of stars and the last updated service
     * @return
     */
    Iterator<Services> getServicesByRankingIterator();

    /**
     * @param studentName - The name of Student given
     * @return - returns an Iterator with the locations visited by a certain student given
     */
    Iterator<Services> getVisitedLocationsIterator(String studentName);

    /**
     * Complexity: Best case: O(1) Worst case: O(n)
     * @param serviceName - The Service Name
     * @return - returns the name of the Service if exists, if not null
     */


    String serviceExists(String serviceName);

    /**
     * Complexity: Best case: O(1) Worst case: O(n)
     * @param serviceName - The service Name
     * @return - returns true if the Lodging exists
     */
    boolean lodgingExists(String serviceName);

    /**
     * @param serviceName - The Service Name
     * @return - returns true if there is at least one Student on the given service
     */
    boolean isThereAnyStudents(String serviceName);

    /**
     * Complexity: Best case: O(1) Worst case: O(n)
     * @param studentName - The Student Name
     * @param locationName - The location Name
     * @return - returns true if the student is at the location given
     */
    boolean isStudentAtLocation(String studentName, String locationName);

    /**
     * @param studentName - The Student Name
     * @param locationName - The service Name
     * @return - return true if the Student is home at the moment
     */
    boolean isStudentHome(String studentName, String locationName);

    /**
     * Complexity: Best case: O(1) Worst case: O(n)
     * @param serviceName - The Service name
     * @return - returns true if the Eating Service is full of Students
     */
    boolean isEatingServiceFull(String serviceName);

    /**
     * Complexity: Best case: O(1) Worst case: O(n)
     * @param name - Name of the Student to check
     * @return - returns the name of the Student if Exists, if not null
     */
    String studentExists(String name);

    /**
     * Complexity: Best case: O(1) Worst case: O(n)
     * @param name - The service Name
     * @return - returns true if the Service given is full of Students
     */
    String isItFull(String name);

    /**
     * @param latitude -  The Latitude chosen
     * @param longitude - The Longitude chosen
     * @return - returns true if it's in bounds
     */
    boolean isInBounds(long latitude, long longitude);

    /**
     * @param serviceName - The name of the Service
     * @return - returns true if the service given is an Eating or Leisure type
     */
    boolean isEatingOrLeisureService(String serviceName);

    /**
     * @param serviceName
     * @return - returns true if the service given is an Eating or Lodging type
     */
    boolean isEatingOrLodgingService(String serviceName);

    /**
     *
     * @param studentName - The Student Name
     * @param serviceName - The Service Name
     * @return - returns true if the Service is more Expensive than the one that  he knows
     */
    boolean isServiceMoreExpensiveForThrifty(String studentName, String serviceName);

    /**
     * Method to check if a move is acceptable under certain conditions
     * @param studentName - The name of the student that is going to be moved
     * @param locationName - The location to where the student is intended to be moved
     * @return True if is acceptable, False if is not acceptable
     */
    boolean isAcceptableMove(String studentName, String locationName);

    /**
     * Checks if a student is thrifty or not
     * @param studentName - The student to be checked
     * @return True if is thrifty, False if not
     */
    boolean isThrifty(String studentName);

    /**
     * @param name - Student Name
     * @return - returns true if the Student has visited any location
     */
    boolean hasVisitedLocation(String name);

    /**
     * Complexity: Best case: O(1) Worst case: O(n)
     * @param type - the type to be verified
     * @return - returns true if there is any Service with a certain Type
     */
    boolean hasServiceOfType(String type);

    /**
     * Complexity: Best case: O(1) Worst case: O(n)
     * @param type - type chosen
     * @param n - Average chosen
     * @return - returns true if there is a certain Type with a certain average
     */
    boolean isTypeWithAverage(String type, int n);

    /**
     * @return - The name of the area
     */
    String getName();

    /**
     * Complexity: Best case: O(1) Worst case: O(n)
     * @param studentName - Student Name
     * @return - returns the Student place at the moment Service Object
     */
    Students getStudentLocationInfo(String studentName);

    /**
     * @param studentName - Student's name
     * @param serviceType - Type of the service
     * @return - returns the best Service for that student and type
     */
    Services findMostRelevantService(String studentName, String serviceType);
}