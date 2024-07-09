import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Scanner;
/**
 * Main function for product, pulls data from sql and saves all in a hash set and returns it
 * @author Noah Rouse <email@noahrouse36@gmail.com>
 */
public final class Main {
   private static final String SERIALIZED_FILE = "data.sav";

   /**
    * loads the hash table from the serialized file
    * @return the HashTable read from the file
    */
   private static HashTable<String, Product> loadHashTable () {
      HashTable<String, Product> table = null;
      try {
         final FileInputStream fileIn = new FileInputStream(SERIALIZED_FILE);
         final ObjectInputStream objIn = new ObjectInputStream(fileIn);
         @SuppressWarnings("unchecked")
         final HashTable<String, Product> obj = (HashTable<String, Product>) objIn.readObject();
         table = obj;
         objIn.close();
         fileIn.close();
      } catch (IOException | ClassNotFoundException e) {
         e.printStackTrace();
      }

      return table;
   }

   /**
    * Saves the hash table into a file
    * @param table the HashTable being saved
    * @throws FileNotFoundException
    */
   private static void saveHashTable(final HashTable<String, Product> table) {
      try {
         final FileOutputStream fileOut = new FileOutputStream(SERIALIZED_FILE);
         final ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
         objOut.writeObject(table);
         objOut.close();
         fileOut.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   /**
    * Retrieves a product from the hash table
    * @param name of product to be found
    * @param table HashTable containing all of the products
    * @return null if not found, the product if found
    */
   public static Product displayProduct (final String name,
         final HashTable<String, Product> table) {
      final Product product;
      try {
         product = table.get(name);
      } catch (final NoSuchElementException e) {
         System.out.printf("Product %s does not exist\n", name);
         return null;
      }

      System.out.printf("name: %s\namount: %s\n", product.name(), product.amount());
      if (!product.suppliers().isEmpty()) {
         System.out.println("Suppliers:");
         for (final String i: product.suppliers()) {
            System.out.printf("   %s\n", i);
         }
      }
      if (product.price() != null) {
         System.out.printf("Price: $%s\n", product.price().toString());
      }

      return product;
   }

   /**
    * displays all products in the table
    * @param table contains all product
    */
   public static void displayAllProducts (final HashTable<String, Product> table) {
      for (final Product i: table.values()) {
         System.out.println(i.name());
      }
   }

   /**
    * Adds a product to the table if there is no new product.
    * @param name of Product
    * @param table containing the list of products
    * @return
    */
   public static Product addProduct (final String name,
         final HashTable<String, Product> table) {
      try {
         table.get(name);
         System.out.printf("Product %s already exists\n", name);
         return null;
      } catch (final NoSuchElementException e) {
         final Product product = new Product(name);
         table.put(name, product);
         System.out.printf("Added product \"%s\"\n", name);
         return product;
      } 
   }

   /**
    * Removes the product from the hash table
    * @param name of product
    * @param table containing products
    * @return the product removed or null if no such product exists
    */
   public static Product removeProduct (final String name,
         final HashTable<String, Product> table) {
      try {
         final Product product = table.remove(name);
         System.out.printf("Removed product \"%s\"\n", name);
         return product;
      } catch (final NoSuchElementException e) {
         System.out.printf("Product %s not exist\n", name);
         return null;
      }
   }

   /**
    * adds a supplier name to the specified products
    * @param productName
    * @param supplier name of supplier
    * @param table contains list of all products
    * @return changed product or null if nothing was changed
    */
   public static Product addSupplier (final String productName, final String supplier,
         final HashTable<String, Product> table) {
      final Product product;
      try {
         product = table.get(productName);
      } catch (final NoSuchElementException e) {
         System.out.printf("Product \"%s\" does not exist\n", productName);
         return null;
      }

      try {
         product.getSupplier(supplier);
         System.out.printf("Supplier \"%s\" already exists\n", supplier);
         return null;
      } catch (final NoSuchElementException e) {
         product.addSupplier(supplier);
         System.out.printf("Added supplier \"%s\" to Product \"%s\"\n", supplier,
               productName);
         return product;
      }
   }

   /**
    * Removes a supplier from product
    * @param productName
    * @param supplier name of supplier
    * @param table contains all products
    * @return name of supplier removed or null if no change
    */
   public static String removeSupplier (final String productName, final String supplier,
         final HashTable<String, Product> table) {
      final Product product;
      try {
         product = table.get(productName);
      } catch (final NoSuchElementException e) {
         System.out.printf("Product \"%s\" does not exist\n", productName);
         return null;
      }

      try {
         final String removed = product.removeSupplier(supplier);
         System.out.printf("Removed supplier \"%s\" from product \"%s\"\n", supplier,
               productName);
         return removed;
      } catch (final NoSuchElementException e) {
         System.out.printf("Supplier %s does not exist for product %s\n", supplier,
               productName);
         return null;
      }
   }

   /**
    * Sets inventory of a product to a specified amount
    * @param name of product
    * @param amount of product
    * @param table contains all products
    * @return -1 if product not found or new amount of product
    */
   public static int setInventory (final String name, final int amount,
         final HashTable<String, Product> table) {
      final Product product;
      try {
         product = table.get(name);
      } catch (final NoSuchElementException e) {
         System.out.printf("Product \"%s\" does not exist\n", name);
         return -1;
      }

      return product.setInventory(amount);
   }

   /**
    * increases inventory by a specified amount
    * @param name of product
    * @param amount of product
    * @param table contains all products
    * @return changed inventory of product or -1 if product does not exist
    */
   public static int increaseInventory (final String name, final int amount,
         final HashTable<String, Product> table) {
      final Product product;
      try {
         product = table.get(name);
      } catch (final NoSuchElementException e) {
         System.out.printf("Product \"%s\" does not exist\n", name);
         return -1;
      }

      return product.increaseInventory(amount);
   }

   /**
    * decreases inventory by a specified amount
    * @param name of product
    * @param amount of product
    * @param table contains all product
    * @return changed inventory or -1 if product not found or not enough inventory
    */
   public static int decreaseInventory (final String name, final int amount,
      final HashTable<String, Product> table) {
   final Product product;
      try {
         product = table.get(name);
      } catch (final NoSuchElementException e) {
      System.out.printf("Product \"%s\" does not exist\n", name);
      return -1;
      }
      
      try {
         return product.decreaseInventory(amount);
      } catch (final Exception e) {
         System.out.printf("Product \"%s\" does not have enough inventory\n", name);
         return -1;
      }
   }

   /**
    * Sets the price of the product to a specified amount
    * @param name of product
    * @param price of product
    * @param table contains all products
    * @return null if product does not exist, or price product has been set to
    */
   public static BigDecimal setPrice (final String name, final BigDecimal price,
         final HashTable<String, Product> table) {
      final Product product;
      try {
         product = table.get(name);
      } catch (final NoSuchElementException e) {
         System.out.printf("Product \"%s\" does not have enough inventory\n", name);
         return null;
      }

      return product.setPrice(price);
   }

   public static void main (final String[] args) throws Exception {
      final File file = new File(SERIALIZED_FILE);
      final HashTable<String, Product> hash;
      if (file.exists()) {
         hash = loadHashTable();
      } else {
         hash = new HashTable<String, Product>();
         saveHashTable(hash);
      }

      final Scanner inStream = new Scanner(System.in);
      while (true) {
         final String[] cmds = inStream.nextLine().split("\\s+");
         if (cmds.length == 0) {
         } else if (cmds[0].toUpperCase().equals("HELP")) {
         } else if (cmds[0].toUpperCase().equals("EXIT")) {
            break;
         } else if (cmds[0].toUpperCase().equals("DISPLAYPRODUCT")) {
            if (cmds.length >= 2) {
               displayProduct(cmds[1], hash);
            } else {
               System.out.println("Missing product name");
            }
         } else if (cmds[0].toUpperCase().equals("DISPLAYALLPRODUCTS")) {
            displayAllProducts(hash);
         } else if (cmds[0].toUpperCase().equals("ADDPRODUCT")) {
            if (cmds.length >= 2) {
               addProduct(cmds[1], hash);
               saveHashTable(hash);
            } else {
               System.out.println("Missing product name");
            }
         } else if (cmds[0].toUpperCase().equals("REMOVEPRODUCT")) {
            if (cmds.length >= 2) {
               removeProduct(cmds[1], hash);
               saveHashTable(hash);
            } else {
               System.out.println("Missing product name");
            }
         } else if (cmds[0].toUpperCase().equals("ADDSUPPLIER")) {
            if (cmds.length >= 3) {
               addSupplier(cmds[1], cmds[2], hash);
               saveHashTable(hash);
            } else {
               if (cmds.length < 2) {
                  System.out.println("Missing product name");
               }

               if (cmds.length < 3) {
                  System.out.println("Missing supplier name");
               }
            }
         } else if (cmds[0].toUpperCase().equals("REMOVESUPPLIER")) {
            if (cmds.length >= 3) {
               removeSupplier(cmds[1], cmds[2], hash);
               saveHashTable(hash);
            } else {
               if (cmds.length < 2) {
                  System.out.println("Missing product name");
               }

               if (cmds.length < 3) {
                  System.out.println("Missing supplier name");
               }
            }
         } else if (cmds[0].toUpperCase().equals("SETINVENTORY")) {
            if (cmds.length >= 3) {
               final int amount;
               try {
                  amount = Integer.parseInt(cmds[2]);
                  setInventory(cmds[1], amount, hash);
                  saveHashTable(hash);
               } catch (final NumberFormatException e) {
                  System.out.println("Invalid amount");
               }
            } else {
               if (cmds.length < 2) {
                  System.out.println("Missing product name");
               }

               if (cmds.length < 3) {
                  System.out.println("Missing inventory amount");
               }
            }
         } else if (cmds[0].toUpperCase().equals("INCREASEINVENTORY")) {
            if (cmds.length >= 3) {
               final int amount;
               try {
                  amount = Integer.parseInt(cmds[2]);
                  setInventory(cmds[1], amount, hash);
                  saveHashTable(hash);
               } catch (final NumberFormatException e) {
                  System.out.println("Invalid amount");
               }
            } else {
               if (cmds.length < 2) {
                  System.out.println("Missing product name");
               }

               if (cmds.length < 3) {
                  System.out.println("Missing increase amount");
               }
            }
         } else if (cmds[0].toUpperCase().equals("DECREASEINVENTORY")) {
            if (cmds.length >= 3) {
               final int amount;
               try {
                  amount = Integer.parseInt(cmds[2]);
                  decreaseInventory(cmds[1], amount, hash);
                  saveHashTable(hash);
               } catch (final NumberFormatException e) {
                  System.out.println("Invalid amount");
               }
            } else {
               if (cmds.length < 2) {
                  System.out.println("Missing product name");
               }

               if (cmds.length < 3) {
                  System.out.println("Missing decrease amount");
               }
            }
         } else if (cmds[0].toUpperCase().equals("SETPRICE")) {
            if (cmds.length >= 3) {
               final BigDecimal price;
               try {
                  price = new BigDecimal(cmds[2]);
                  setPrice(cmds[1], price, hash);
                  saveHashTable(hash);
               } catch (final NumberFormatException e) {
                  System.out.println("Invalid price");
               }
            } else {
               if (cmds.length < 2) {
                  System.out.println("Missing product name");
               }

               if (cmds.length < 3) {
                  System.out.println("Missing price");
               }
            }
         }
      }
      inStream.close();
   }
}
