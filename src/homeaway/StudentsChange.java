/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway;

public interface StudentsChange {
    /**
     * Sets the Students Home and stores it as a Service
     * @param place The Service Object "Home"
     */
    void setPlaceHome(Services place);

    /**
     * Set the place where the Student is at the moment
     * @param newPlace The new place that is now the place at the moment
     */
    void setPlaceGo(Services newPlace);
}