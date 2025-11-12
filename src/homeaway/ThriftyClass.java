/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway;

public class ThriftyClass extends StudentsClassAbstract implements Thrifty, Students {

    private double cheapestEatingPrice;

    private static final long serialVersionUID = 0L;

    ThriftyClass(String studentType, String name, String country, Services lodging) {
        super (studentType, name, country, lodging);
        cheapestEatingPrice = Integer.MAX_VALUE;
    }

    public boolean isMoreExpensiveThanCheapest(Services services){
        if(!services.getServiceType().equalsIgnoreCase(TypesOfService.EATING.toString())) return false;

        if (getPrice(services) <= cheapestEatingPrice) {
            cheapestEatingPrice = getPrice(services);  // Atualiza o preço mais barato
            return false; // Não é mais caro - é mais barato
        } else {
            return true; // É mais caro que o mais barato atual
        }
    }

    /**
     * Method used to get the price of a service depending on its type
     * @param service - the service
     * @return
     */
    private double getPrice (Services service) {
        double price = 0;
        if (service instanceof Leisure) {
            price = ((Leisure)service).getPrice();
        } else if (service instanceof Lodging) {
            price = ((Lodging)service).getPrice();
        } else if (service instanceof Eating) {
            price = ((Eating)service).getPrice();
        }
        return price;
    }

}
