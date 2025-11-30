/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway;

public interface ServicesChange {

    /**
     * Method used to update the variable that is used to keep the ranking sorted, following the rules given at the statement
     * @param counter - the new value for the variable being updated
     */
    void updateCounterRating(int counter);

    /**
     * Used when the star command is used to recalculate its rating and if necessary update the rating and the place where ist kept in the data structure of rating
     * @param stars - the number of stars given to the service
     * @param tag - the tags given to the service when the "star" command is used
     * @param counter - lastUpdatedOrder, variable used to sort the data structure used to keep the raking organised
     */
    void addRating(int stars,String tag, int counter);

    /**
     * Adds the Student that is on this Service
     * @param newStudent - the student to add
     */
    void addStudentsThere(Students newStudent);

    /**
     * Removes the Student that is on this Service
     * @param student - the student to remove
     */
    void removeStudentsThere(Students student);

    /**
     * Increments by one the number of students that are living on Lodging
     */
    void addStudentsThereLodging();
    /**
     * Decrements by one the number of students that are living on Lodging
     */
    void removeStudentsThereLodging();

    /**
     * Set the number that was inserted on the Area.
     * (If the number is smaller than other the service is older)
     * @param counter The number of Insertion
     */
    void setNumOfInsertion(int counter);
}
