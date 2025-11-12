/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway.Exeptions;

import java.io.Serializable;

public class NoServicesInSystemException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 0L;
    private static final String message = "No services in the system.";
    public NoServicesInSystemException() {
        super(message);
    }
}
