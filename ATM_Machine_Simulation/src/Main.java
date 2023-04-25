import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDate;

interface IClient {
	public Boolean checkPIN();
	public String getCurrency();
	public ArrayList getAccountList();
}

// Client class
class Client implements IClient {
	private String name, nationality, occupation, address, gender, currency;
	private int age, PIN;
	protected ArrayList<Bank_Account> accounts = new ArrayList<Bank_Account>();
	
	public Client(String name, String nationality, String occupation, String address, String gender, String currency, int age) {
		this.name = name;
		this.nationality = nationality;
		this.occupation = occupation;
		this.address = address;
		this.gender = gender;
		this.age = age;
		this.currency = currency;
		this.PIN = generatePIN();
	}
	
	public Boolean checkPIN() {
		Scanner scanner = new Scanner(System.in);
		String str = scanner.nextLine();
		System.out.println(str);
		return this.PIN == Integer.parseInt(str);
	}
		
	private static int generatePIN() {
		Random random = new Random();
		String pin = "";
		for (int i=0; i<4; i++) {
			int randomInt = random.nextInt(10);
			String str = Integer.toString(randomInt);
			pin += str;
		}
		int num = Integer.parseInt(pin);
		System.out.println("Your PIN is "+pin);
		return num;
	}
	
	public String getCurrency() {
		return this.currency;
	}
	
	public ArrayList getAccountList() {
		return this.accounts;
	}
}

interface IBank_Account {
	public int getAccountId();
	public boolean deposit(double amount);
	public boolean withdraw(double amount);
	public boolean inquire_balance();
}
// Bank Account Abstract Class
abstract class Bank_Account implements IBank_Account {
	private static int account_number;
	protected String account_type;
	private String branch, currency;
	protected double balance;
	private Client owner;
	
	public Bank_Account(String branch, Client client) {
		this.account_number += 1;
		this.branch = branch;
		this.currency = client.getCurrency();
		this.balance = 0.0;
		this.owner = client;
	}
	
	public int getAccountId() {
		return this.account_number;
	}
	public boolean deposit(double amount) {
		this.balance += amount;
		return true;
	}
	public boolean withdraw(double amount) {
		this.balance -= amount;
		return true;
	}
	public boolean inquire_balance() {
		System.out.println("Current balance of account is "+this.balance);
		return true;
	}
}

interface ISaving_Account {
	public void payInterest();
}

// Saving Account Abstract Class
class Saving_Account extends Bank_Account implements ISaving_Account {
	private double interest_rate;
	
	public Saving_Account(String branch, Client client, double interest_rate) {
		super(branch, client);
		this.account_type = "Saving_Account";
		this.interest_rate = interest_rate;
		client.accounts.add(this);
	}
	
	public void payInterest() {
		this.balance += this.balance*this.interest_rate;
	}
	
}

interface ICurrent_Account {
}

//Current Account Abstract Class
class Current_Account extends Bank_Account implements ICurrent_Account {
	
	public Current_Account(String branch, Client client) {
		super(branch, client);
		this.account_type = "Current_Account";
		client.accounts.add(this);
	}
}

interface ILoan {
	public void payLoan(double amount);
	public void addInterest();
}

// Loan Class
class Loan implements ILoan{
	private static int loan_account_number = 0;
	private String currency, paymentMethod;
	protected double amount, interest, duration;
	private Client owner;
	
	public Loan(Client client, double amount, String paymentMethod, double interest, double duration) {
		this.loan_account_number += 1;
		this.currency = client.getCurrency();
		this.amount = amount;
		this.owner = client;
		this.paymentMethod = paymentMethod;
		this.interest = interest;
		this.duration = duration;
	}
	
	public void payLoan(double amount) {
		this.amount -= amount;
	}
	
	public void addInterest() {
		this.amount += this.amount*this.interest;
	}
}

interface ITransaction {
}

// transaction class
abstract class Transaction implements ITransaction {
	private static int transaction_id = 0;
	private int account_id;
	private LocalDate transaction_date;
	protected String state;
	
	public Transaction(Bank_Account account, Client client) {
		this.transaction_id += 1;
		this.account_id = account.getAccountId();
		this.transaction_date = LocalDate.now();
		this.state = null;
	}
}


// deposit class
class Deposit extends Transaction {
	private double transaction_amount;
	
	public Deposit(Bank_Account account, Client client, double amount) {
		super(account, client);
		if (account.deposit(amount)) {
			this.state = "Successful";
		} else {
			this.state = "fail";
		}
		this.transaction_amount = amount;
	}
}

// withdrawal class
class Withdrawal extends Transaction {
	private double transaction_amount;
	
	public Withdrawal(Bank_Account account, Client client, double amount) {
		super(account, client);
		if (account.withdraw(amount)) {
			this.state = "Successful";
		} else {
			this.state = "fail";
		}
		this.transaction_amount = -1*amount;
	}
}

// Balance_Inquiry class
class Balance_Inquiry extends Transaction {
	
	public Balance_Inquiry(Bank_Account account, Client client) {
		super(account, client);
		if (account.inquire_balance()) {
			this.state = "Successful";
		} else {
			this.state = "fail";
		}
		
	}
}


public class Main {
	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client client1 = new Client("Ravindu", "Sinhala", "Developer", "No 32, Samagi Road, Gampaha", "Male", "Rupees", 25);
		Saving_Account account1 = new Saving_Account("Colombo",client1,0.05);
		Current_Account account2 = new Current_Account("Colombo",client1);
		System.out.println("Welcome");
		System.out.println("Enter your PIN");
		ArrayList<Bank_Account> accounts = client1.getAccountList();
		if(client1.checkPIN()) {
			System.out.println("Select account \n");
			int i = 0;
			for (Bank_Account acc: accounts) {
				System.out.print(acc.account_type + "-" + acc.getAccountId());
				System.out.println("  "+i);
				i += 1;
			}
		}
		String str = scanner.nextLine();
		int acc_index = Integer.parseInt(str);
		Bank_Account Selected_Account;
		if (acc_index < accounts.size()) {
			Selected_Account = accounts.get(acc_index);
		} else {
			Selected_Account = null;
		}
		if (Selected_Account != null) {
			System.out.print("Choose Option");
			System.out.print("View Balance : 1\n"
					+ "Withdraw money : 2\n"
					+ "Deposit money : 3\n"
					+ "Exit : 4\n");
			String optionStr = scanner.nextLine();
			int option_num = Integer.parseInt(optionStr);
			if (option_num==1) {
				Balance_Inquiry transaction_object = new Balance_Inquiry(Selected_Account, client1);
			} else if (option_num==2) {
				System.out.println("Enter Amount");
				String amountStr = scanner.nextLine();
				int amount = Integer.parseInt(amountStr);
				Withdrawal transaction_object = new Withdrawal(Selected_Account, client1, amount);
			} else if (option_num==3) {
				System.out.println("Enter Amount");
				String amountStr = scanner.nextLine();
				int amount = Integer.parseInt(amountStr);
				Deposit transaction_object = new Deposit(Selected_Account, client1, amount);
			}
		}
		scanner.close();
		System.out.println("Have a nice day!");

	}

}
