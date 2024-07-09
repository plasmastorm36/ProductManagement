import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
/**
 * Made to store and handle name of product, supplier, and amount available
 * @author Noah Rouse <myEmail@noahrouse36@gmail.com>
 */
public final class Product implements Serializable, Comparable<Product> {
   private final String name;
   private final ArrayList<String> suppliers;
   private int inventory;
   private BigDecimal price;

   /**
    * sets name of product, initializes an empty suppliers list and sets inventory to zero
    * @param name the Name of the product
    */
   public Product (final String name) {
      this.name = name;
      this.suppliers = new ArrayList<String>();
      this.inventory = 0;
   }

   /**
    * Sets name and inventory of product
    * @param name The name of the product
    * @param amount The amount of the product
    */
   public Product (final String name, final int amount) {
      this(name);
      this.inventory = amount;
   }

   public Product (final String name, final String supplier) {
      this(name);
      addSupplier(supplier);
   }

   public Product (final String name, final String[] suppliers) {
      this(name);
      addSuppliers(suppliers);
   }

   public Product (final String name, final List<String> suppliers) {
      this(name);
      addSuppliers(suppliers);
   } 

   public Product (final String name, final BigDecimal price) {
      this(name);
      this.price = price;
   }

   public Product (final String name, final Number price) {
      this(name);
      this.price = new BigDecimal(price.toString());
   }

   public Product (final String name, final Number price, final int amount) {
      this(name, price);
      this.inventory = amount;
   }

   public Product (final String name, final Number price, final String supplier) {
      this(name, supplier);
      this.price = new BigDecimal(price.toString());
   }

   public Product (final String name, final Number price, final String[] suppliers) {
      this(name, suppliers);
      this.price = new BigDecimal(price.toString());
   }

   public Product (final String name, final Number price, final List<String> suppliers) {
      this(name, suppliers);
      this.price = new BigDecimal(price.toString());
   }

   /**
    * Sets product to be compared by name
    */
   @Override
   public int compareTo (final Product product) {
      return this.name.compareToIgnoreCase(product.name);
   }

   /**
    * 
    * @return Returns the name of the product
    */
   public String name () {
      return name;
   }

   /**
    * 
    * @return an ArrayList containing all suppliers
    */
   public ArrayList<String> suppliers () {
      return suppliers;
   }

   /**
    * 
    * @return the amount of the product being stored
    */
   public int amount () {
      return inventory;
   }

   /**
    * 
    * @return product price
    */
   public BigDecimal price () {
      return price;
   }

   /**
    * increases inventory of the product by a specified amount
    * @param amount The amount being added
    * @return Returns the new inventory
    */
   public int increaseInventory (final int amount) {
      this.inventory += amount;
      return this.inventory;
   }

   /**
    * removes inventory by the specified amount
    * @param amount The amount being subtracted from the inventory
    * @return returns the inventory left
    * @throws Exception When the amount is greater than the inventory
    */
   public int decreaseInventory (final int amount) throws Exception {
      if (amount > inventory) {
         throw new Exception("Error: not enough inventory");
      }

      inventory -= amount;
      return inventory;
   }

   /**
    * Changes inventory to the specified amount
    * @param amount Number to change the inventory to
    * @return Inventory of product
    */
   public int setInventory (final int amount) {
      inventory = amount;
      return inventory;
   }

   /**
    * Sets price
    * @param price new price of product
    * @return new price of product
    */
   public BigDecimal setPrice (final BigDecimal price) {
      this.price = price;
      return this.price;
   }

   /**
    * Sets price
    * @param price new price of product
    * @return new price of product
    */
   public BigDecimal setPrice (final Number price) {
      this.price = new BigDecimal(price.toString());
      return this.price;
   }

   /**
    * Increases price by specified amount
    * @param amount to increase
    * @return new price after increase
    */
   public BigDecimal increasePrice (final Number amount) {
      price.add(new BigDecimal(amount.toString()));
      return price;
   }

   /**
    * Decreases price by specified amount
    * @param amount to decrease
    * @return new price after decrease
    */
   public BigDecimal decreasePrice (final Number amount) {
      price.subtract(new BigDecimal(amount.toString()));
      return price; 
   }

   /**
    * Adds a name to the supplier list in sorted order
    * @param name Name of supplier
    * @return returns the add supplier name or null
    */
   public String addSupplier (final String name) {
      for (int i = 0; i < suppliers.size(); i++) {
         final String current = suppliers.get(i);
         if (name.equalsIgnoreCase(current))
            return null;
         if (name.compareToIgnoreCase(current) < 0) {
            suppliers.add(i, name);
            return name;
         }
      }

      suppliers.add(name);
      return name;
   }

   /**
    * Adds all names in sorted order to the supplier list
    * @param names a list of names of all of the suppliers being added
    */
   public void addSuppliers (final List<String> names) {
      for (final String i: names) {
         addSupplier(i);
      }
   }

   /**
    * Adds all names in sorted order to the supplier list
    * @param names an array of names of all of the suppliers being added
    */
   public void addSuppliers (final String[] names) {
      for (String i: names) {
         addSupplier(i);
      }
   }

   /**
    * Uses a binary search to look for and remove a supplier
    * @param name Supplier to be removed
    * @throws NoSuchElementException When there is no supplier by that name
    */
   public String removeSupplier (final String name) throws NoSuchElementException {
      final int index = binarySearch(name, 0, suppliers.size() - 1);
      if (index == -1) {
         throw new NoSuchElementException("No supplier by that name");
      }

      return suppliers.remove(index);
   }
   
   /**
    * Uses a binary search to find a supplier
    * @param name Supplier to be found
    * @return name of supplier to be found
    * @throws NoSuchElementException when supplier does not exist
    */
   public String getSupplier(final String name) throws NoSuchElementException {
      final int index = binarySearch(name, 0, suppliers().size() - 1);
      if (index == -1) {
         throw new NoSuchElementException("No supplier by that name");
      }

      return suppliers.get(index);
   }

   /**
    * fast search function
    * @param target
    * @param low
    * @param high
    * @return
    */
   private int binarySearch (final String target, final int low, final int high) {
      // base case element does not exist
      if (low > high) {
         return -1;
      }

      final int mid = (high + low) / 2;

      // base case target found
      if (suppliers.get(mid).equalsIgnoreCase(target)) {
         return mid;
      }

      // case target comes after mid value in alphabetic order
      if (suppliers.get(mid).compareToIgnoreCase(target) > 0) {
         return binarySearch(target, mid, high);
      }

      // case target comes before mid value in alphabetic order
      if (suppliers.get(mid).compareToIgnoreCase(target) < 0) {
         return binarySearch(target, low, mid);
      }

      return -1;
   }
}
