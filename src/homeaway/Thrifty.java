/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway;

public interface Thrifty {

    /**
     * @param services Service
     * @return - returns True If the Service given is cheaper than the one stored
     */
    boolean isMoreExpensiveThanCheapest(Services services);
}
