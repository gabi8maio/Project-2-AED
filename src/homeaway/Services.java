/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway;

import dataStructures.Iterator;
import dataStructures.TwoWayIterator;

import java.io.Serializable;

public interface Services extends Comparable<Services>, ServicesChange, Serializable {

    /**
     * @return - The name of the service
     */
    String getServiceName();

    /**
     * @return - The type of the service
     */
    String getServiceType();

    /**
     * @return - The latitude of the service
     */
    long getLatitude();

    /**
     * @return - The longitude of the service
     */
    long getLongitude();

    /**
     * @return - The value of the service; if the service is eating or lodging is its capacity, if it is a leisure service the value is its discount
     */
    int getValue();

    /**
     * @return - An integer that represents the last service updating its rating
     */
    int getLastUpdatedOrder();

    /**
     * @return - The rating of the service
     */
    int getAverageStars();

    /**
     * @return - An iterator of the tags that a service was tagged with
     */
    Iterator<String> getTags();

    /**
     * @return - An iterator of the students currently at this service
     */
    TwoWayIterator<Students> getStudentsThere();

    /**
     * Method used to check if a service is full or not
     * @return True - the service if full
     * @return False - the service is not full
     */
    String isFull();


    /**
     * Method used to check if the service is empty
     * @return True - if the service is empty
     * @return False - if the service is not empty
     */
    boolean isThereAnyStudents();
}
