import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.time.format.DateTimeFormatter;

class ItemCodeNotFound extends IOException {
    public ItemCodeNotFound(String message) {
        super(message);
    }
}

interface IGroceryItem{
    public double getPrice();
    public int getItem_code();
    public double getDiscount_rate();
    public double getWeight_or_size();
}

class GroceryItem implements IGroceryItem, Serializable{
    static int item_code_generator = 0;
    private int item_code, unique_item_code;
    private double price, weight_or_size, discount_rate;
    private LocalDate manufactured_date, expiry_date;
    private String manufacturer_name;

    public GroceryItem(int item_code, double price, double weight_or_size, int[] expiry_date, String manufacturer_name, double discount_rate) {
        this.unique_item_code = item_code_generator;
        item_code_generator++;
        this.item_code = item_code;
        this.price = price;
        this.weight_or_size = weight_or_size;
        this.manufactured_date = LocalDate.now();
        this.expiry_date = LocalDate.of(expiry_date[0], expiry_date[1], expiry_date[2]);
        this.manufacturer_name = manufacturer_name;
        this.discount_rate = discount_rate;
    }
    public double getPrice() {
        return this.price;
    }
    public int getItem_code() {
        return this.item_code;
    }
    public double getDiscount_rate() { return this.discount_rate;}
    public double getWeight_or_size() { return this.weight_or_size;}
}

abstract class Person {
    String name;
    public Person (String name) {
        this.name = name;
    }
}

class Cashier extends Person {
    public Cashier(String name) {
        super(name);
    }
}

class Customer extends Person {
    public Customer (String name) {
        super(name);
    }
}

class Bill implements Serializable{
    private String cashier_name, branch, customer_name;
    private double price_without_discount, total_discount, final_price;
    private LocalDateTime date_and_time;
    private ArrayList<GroceryItem> item_array;
    private HashMap<Integer, Integer> processedItem_array;

    public Bill (POS pos, Cashier cashier, Customer customer, ArrayList<GroceryItem> arrayList) {
        this.branch = pos.branch;
        this.cashier_name = cashier.name;
        this.customer_name = customer.name;
        this.item_array = arrayList;
        this.processedItem_array = createShrinkedItemList(item_array);

        int[] basic_discount_final = get_final_price(this.item_array);
        this.price_without_discount = basic_discount_final[0];
        this.total_discount = basic_discount_final[1];
        this.final_price = basic_discount_final[2];
        this.date_and_time = LocalDateTime.now();
    }
    public HashMap createShrinkedItemList(ArrayList<GroceryItem> item_array) {
        HashMap<Integer, Integer> processedItem_array = new HashMap<Integer, Integer>();
        //Map<Integer, Integer> dictionary = new HashMap<>();
        boolean containsKey;
        Integer value;
        int key_int;

        for (GroceryItem item : item_array) {
            key_int = item.getItem_code();
            containsKey = processedItem_array.containsKey(key_int);
            if (containsKey) {
                value = processedItem_array.get(key_int);
                value = value + 1;
                processedItem_array.put(key_int,value);
            } else {
                processedItem_array.put(key_int,1);
            }
        }
        return processedItem_array;
    }

    public void printBill() {
        System.out.println("Customer name :"+this.customer_name);
        System.out.println("Cashier name :"+this.cashier_name);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = this.date_and_time.format(formatter);
        System.out.println("Time of Transaction : "+formattedDateTime);
        for (Map.Entry<Integer,Integer> entry:this.processedItem_array.entrySet()) {
            System.out.println(
                    "Item Code : " + entry.getKey() + "\t\tAmount : " + entry.getValue()
            );
        }
        System.out.println("Total price : "+this.price_without_discount);
        System.out.println("Discount : "+this.total_discount);
        System.out.println("Final price : "+this.final_price);
    }
    public int[] get_final_price(ArrayList<GroceryItem> arrayList) {
        // ArrayList<Integer> basic_discount_final = new ArrayList<Integer>();
        int[] basic_discount_final = {0,0,0};
        for (GroceryItem item : arrayList) {
            basic_discount_final[0] += item.getPrice();
            basic_discount_final[1] += item.getDiscount_rate()*item.getPrice();
        }
        basic_discount_final[2] = basic_discount_final[0] - basic_discount_final[1];
        return basic_discount_final;
    }

}

class PendingBill implements Serializable{
    private ArrayList<GroceryItem> item_array;
    public PendingBill(ArrayList<GroceryItem> item_array) {
        this.item_array = item_array;
    }
    public void saveItem() {
        try{
            FileOutputStream fileStream = new FileOutputStream("pendingItems.ser");
            ObjectOutputStream os = new ObjectOutputStream(fileStream);
            os.writeObject(this);
            os.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public PendingBill retrieveItem() {
        try {
            FileInputStream fileStream = new FileInputStream("pendingItems.ser");
            ObjectInputStream os = new ObjectInputStream(fileStream);
            Object one = os.readObject();
            PendingBill pendingBill1= (PendingBill) one;
            os.close();
            return  pendingBill1;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public ArrayList getGroceryItemsArray() {
        return this.item_array;
    }
}

class POS implements Serializable{
    String branch;
    int item_code;
    ArrayList<GroceryItem> database;
    Cashier cashier;
    public POS (String branch, ArrayList<GroceryItem> arrayList, Cashier cashier) {
        this.branch = branch;
        this.database = arrayList;
        this.cashier = cashier;
    }
    public GroceryItem getItemDetails() throws ItemCodeNotFound {
        try {
            System.out.println("Enter item-code :");
            InputStreamReader r = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(r);
            String input_text = br.readLine();
            item_code = Integer.parseInt(input_text);
            GroceryItem item = null;
            // Fetch item details from the database
            boolean item_available = false;
            for (GroceryItem groceryItem : this.database) {
                if (groceryItem.getItem_code()==item_code) {
                    item = groceryItem;
                    item_available = true;
                    break;
                }
            }
            if (!item_available) {
                throw new ItemCodeNotFound(Integer.toString(item_code));
            }
//            br.close();
//            r.close();
            return item;
        } catch (ItemCodeNotFound e) {
            System.out.println(e.getMessage());
            return getItemDetails();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return getItemDetails();
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return getItemDetails();
        }
    }
}

public class Main {
    public static void main(String[] args) throws ItemCodeNotFound {
        Cashier cashier1 = new Cashier("Sanath");
        int[] expiry_array1 = {2024, 1, 1};
        int[] expiry_array2 = {2023, 6, 1};
        int[] expiry_array3 = {2023, 10, 20};
        GroceryItem rice = new GroceryItem(101,120.00, 1, expiry_array1,"Araliya", 0.1);
        GroceryItem bun = new GroceryItem(210,80.00, 1, expiry_array2,"NewProducts", 0.05);
        GroceryItem sugar = new GroceryItem(304,105.00, 1, expiry_array3,"Sethum", 0.15);
        GroceryItem milk_powder = new GroceryItem(436,955.00, 1, expiry_array3,"Anchor", 0);
        ArrayList<GroceryItem> allGroceryItems = new ArrayList<GroceryItem>();
        allGroceryItems.add(rice);
        allGroceryItems.add(bun);
        allGroceryItems.add(sugar);
        allGroceryItems.add(milk_powder);
        POS pos1 = new POS("Colombo",allGroceryItems,cashier1);

        ArrayList<GroceryItem> groceryItemsForCustomer = new ArrayList<>();
        groceryItemsForCustomer.add(pos1.getItemDetails());
        try{
            InputStreamReader r2 = new InputStreamReader(System.in);
            BufferedReader br2 = new BufferedReader(r2);
            while (true) {
                System.out.println("More items ? (y/n)");
                String input_text = br2.readLine();
                if (input_text.equals("n")) {
                    break;
                }
                groceryItemsForCustomer.add(pos1.getItemDetails());
            }
            br2.close();
            r2.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        PendingBill pendingBill1 = new PendingBill(groceryItemsForCustomer);
        pendingBill1.saveItem();
        PendingBill pendingBill2 = pendingBill1.retrieveItem();
        ArrayList<GroceryItem> groceryItemsForCustomerDeserialized = pendingBill2.getGroceryItemsArray();

        Customer customer1 = new Customer("Nimal");
        Bill bill = new Bill(pos1, cashier1, customer1, groceryItemsForCustomerDeserialized);
        bill.printBill();
    }
}

