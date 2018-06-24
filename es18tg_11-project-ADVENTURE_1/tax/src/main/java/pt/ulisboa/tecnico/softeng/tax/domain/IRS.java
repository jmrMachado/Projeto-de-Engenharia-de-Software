package pt.ulisboa.tecnico.softeng.tax.domain;

import pt.ulisboa.tecnico.softeng.tax.dataobjects.*;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

import java.util.Map;
import java.util.TreeMap;

public class IRS {
	
    public static TreeMap<String, ItemType> _itemTypes = new TreeMap<>();
    public static TreeMap<String, TaxPayer> _contribuintes = new TreeMap<>();

    //IRS is a Singleton
    private static IRS instance = null;

    private IRS(){
    }

    //exception handling with lazy initialization
    public static IRS getInstance() {
        if (instance == null) {
            try {
                instance = new IRS();
            } catch (Exception e) {
                throw new TaxException("IRS could not be initialized.");
            }
        }
        return instance;
    }
    
    /**
     * Obter o ItemType associado ao nome
     * @param name
     * @return ItemType
     */
    public ItemType getItemTypeByName(String name){

        checkName(name);

        if (_itemTypes.containsKey(name)){
            return _itemTypes.get(name);
        }else {
            throw new TaxException("The name " + name + " is not associated to any item type.");
        }
    }

    private void checkName(String name) {
        if (name == null || name.trim().equals("")){
            throw new TaxException("NIF in incorrect format.");
        }
   }
    
    public String getNameByItemType(ItemType it){
    	
	    for(Map.Entry<String, ItemType> entry : _itemTypes.entrySet()) {
	    	if (entry.getValue().equals(it))
	    		return (String) entry.getKey();
	    }
    	throw new TaxException("The ItemType " + it.toString() + " is not associated to any name.");	    
    }

    /**
     * Obter o TaxPayer associado ao nome
     * @param NIF
     * @return TaxPayer
     */
    public TaxPayer getTaxPayerByNIF(String NIF){       
    	
    	checkNif(NIF);

        if (_contribuintes.containsKey(NIF)){
            return _contribuintes.get(NIF);
        } else {
            throw new TaxException("The requested NIF " + NIF + " does not exist.");
        }
    }
    
    /**
     * verifica se o nif segue as regras do nif.
     * @param NIF
     */
    private void checkNif(String NIF) {
    	
    	 if (NIF == null || NIF.length() != 9 || NIF.trim().equals("")){
             throw new TaxException("NIF in incorrect format.");
         } 
    	 
    	 for (int i = 0; i<NIF.length(); i++){
             if (Character.isLetter(NIF.charAt(i))){
                 throw new TaxException("NIF in incorrect format.");
             }
         }
    }
    
    /**
    * colocar invoices no vetor _invoice do TaxPayer
    * @param InvoiceData data
    */
    public void submitInvoice(InvoiceData data) {

        ItemType it;

        for (String str : IRS._itemTypes.keySet()) {
            if (str.equals(data.getItemType())) {
                it = IRS._itemTypes.get(data.getItemType());

                TaxPayer tps = this.getTaxPayerByNIF(data.getSellerNIF());
                TaxPayer tpb = this.getTaxPayerByNIF(data.getBuyerNIF());
                Seller s = (Seller) tps;
                Buyer b = (Buyer) tpb;
                Invoice i = new Invoice(data.getValue(), data.getDate(), it, s, b);

                TaxPayer._invoices.add(i);
            }
        }
    }

    
    public boolean checkForNifExistence(String nif){
        return _contribuintes.containsKey(nif);
    }

    public void addTaxPayer(TaxPayer person) {
            _contribuintes.put(person.getNIF(), person);
    }

    public void addItemType(ItemType item){
        if (item == null){
            throw new TaxException("The provided item cannot be of type null");
        }
        _itemTypes.put(item.getItemType(),item);
    }
}
