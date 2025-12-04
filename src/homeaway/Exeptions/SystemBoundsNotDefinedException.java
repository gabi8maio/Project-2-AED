/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway.Exeptions;

import java.io.Serializable;

public class SystemBoundsNotDefinedException extends RuntimeException implements Serializable {
    private static final String message = "System bounds not defined.";
    public SystemBoundsNotDefinedException() {
        super(message);
    }
}
