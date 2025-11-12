/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway;

/**
 * Enum used to check if the type of student given by the user is a valid student type
 */
public enum StudentTypes {

    BOOKISH("BOOKISH"),
    OUTGOING("OUTGOING"),
    THRIFTY("THRIFTY");
    private final String typeName;

    private static final long serialVersionUID = 0L;

    StudentTypes(String typeName) {
        this.typeName = typeName;
    }

    /**
     * @return - the type name associated with this enum value.
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Checks if the type given is valid or not
     * @param type - String of the type
     * @return the enum value corresponding to the given string, case-insensitive. Null if no matching enum value was found.
     */
    public static StudentTypes fromString(String type) {
        for (StudentTypes serviceType : StudentTypes.values()) {
            if (serviceType.typeName.equalsIgnoreCase(type)) {
                return serviceType;
            }
        }
        return null;
    }
}
