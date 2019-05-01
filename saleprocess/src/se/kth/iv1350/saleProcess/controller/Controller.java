package se.kth.iv1350.saleProcess.controller;
import se.kth.iv1350.saleProcess.dbhandler.ChangeDTO;
import se.kth.iv1350.saleProcess.model.SaleInfo;
import se.kth.iv1350.saleProcess.dbhandler.SystemCreator;
import se.kth.iv1350.saleProcess.model.Payment;
import se.kth.iv1350.saleProcess.model.Sale;
import se.kth.iv1350.saleProcess.util.Amount;

/**
 * This is the applications only controller class.
 * All calls to the model pass through here.
 */
public class Controller {
    private Sale currentSale;
    private SystemCreator systemCreator;
    private Payment payment;

    /**
     * Creates a new instance and initiates the variable systemCreator.
     */
    public Controller(SystemCreator systemCreator){
        this.systemCreator = systemCreator;
    }

    /**
     * Creates an instance of the object type <code>Sale<code/>
     * and associates it with the variable name currentSale.
     */
    public void startNewSale() {
    this.currentSale = new Sale();
    }

    /**
     * Handles the current sales item registrations.
     *
     * @param itemID The <code>Item<code/> identification number.
     * @param quantity The quantity of the <code>Item<code/>.
     * @return The current sale information in form of a <code>SaleInfo<code/>.
     */
    public SaleInfo enterItem(int itemID, int quantity){
        currentSale.includeItems(this.systemCreator.getInventorySystem().getItemFromInventorySystem(itemID), quantity);

       return currentSale.getSaleInfo();
    }

    /**
     * Method used when all items of the sale has been registered.
     *
     * @return The total price including VAT of the sale in the form of a <code>String<code/>.
     */
    public String allItemsRegistered(){

        StringBuilder builder = new StringBuilder();

        builder.append("Total price(including VAT): ");
        builder.append(this.currentSale.getTotalPrice() + "\n");

        return builder.toString();
    }

    /**
     * This method handles the payment part of the sale. It calculates
     * the change, updates the amount in the cash register and then completes the sale.
     *
     * @param paidAmount The amount paid.
     * @return The amount of change the customer receives in form of a <>ChangeDTO</>.
     */
    public ChangeDTO pay(Amount paidAmount){

        this.systemCreator.getCashRegister().addPayment(this.currentSale.getTotalPrice());
        this.payment = new Payment(paidAmount, this.currentSale.getTotalPrice());
        this.payment.completeSale(this.currentSale, this.systemCreator);

        return this.payment.getChange();
    }

}
