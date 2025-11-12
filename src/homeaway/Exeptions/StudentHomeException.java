/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway.Exeptions;

import java.io.Serializable;

public class StudentHomeException extends RuntimeException implements Serializable {
    private static final String message = "That is ";
    private static final String message2 = "'s home!";
    public StudentHomeException(String studentName) {
        super(message + studentName + message2);
    }
}
