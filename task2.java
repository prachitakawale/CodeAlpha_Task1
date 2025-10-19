import java.io.*;
import java.util.*;

class Stock {
    String symbol;
    double price;

    Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    void updatePrice(double newPrice) {
        this.price = newPrice;
    }
}

class User {
    double balance;
    Map<String, Integer> portfolio = new HashMap<>();

    User(double initialBalance) {
        this.balance = initialBalance;
    }

    void buyStock(Stock stock, int quantity) {
        double cost = stock.price * quantity;

        if (cost > balance) {
            System.out.println("Not enough balance to buy " + quantity + " shares.");
            return;
        }

        balance -= cost;
        portfolio.put(stock.symbol, portfolio.getOrDefault(stock.symbol, 0) + quantity);
        System.out.println("Bought " + quantity + " shares of " + stock.symbol);
    }

    void sellStock(Stock stock, int quantity) {
        int owned = portfolio.getOrDefault(stock.symbol, 0);

        if (quantity > owned) {
            System.out.println("You don’t own that many shares of " + stock.symbol);
            return;
        }

        balance += stock.price * quantity;
        portfolio.put(stock.symbol, owned - quantity);
        System.out.println("Sold " + quantity + " shares of " + stock.symbol);
    }

    void showPortfolio(Map<String, Stock> market) {
        System.out.println("\n--- Portfolio ---");
        for (String symbol : portfolio.keySet()) {
            int qty = portfolio.get(symbol);
            Stock stock = market.get(symbol);
            if (qty > 0 && stock != null) {
                double value = qty * stock.price;
                System.out.printf("%s: %d shares @ $%.2f = $%.2f\n", symbol, qty, stock.price, value);
            }
        }
        System.out.printf("Cash Balance: $%.2f\n", balance);
    }

    double getTotalValue(Map<String, Stock> market) {
        double total = balance;
        for (String symbol : portfolio.keySet()) {
            int qty = portfolio.get(symbol);
            Stock stock = market.get(symbol);
            if (stock != null) {
                total += qty * stock.price;
            }
        }
        return total;
    }
}

public class StockMarketSimulator {
    static Map<String, Stock> market = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Initialize some stocks
        market.put("AAPL", new Stock("AAPL", 150.00));
        market.put("GOOG", new Stock("GOOG", 2800.00));
        market.put("TSLA", new Stock("TSLA", 700.00));
        market.put("AMZN", new Stock("AMZN", 3300.00));

        // Create a user with $10,000 starting balance
        User user = new User(10000.00);

        boolean running = true;

        System.out.println("Welcome to the Stock Market Simulator!");

        while (running) {
            displayMarket();
            System.out.println("\nChoose an option:");
            System.out.println("1. Buy Stock");
            System.out.println("2. Sell Stock");
            System.out.println("3. View Portfolio");
            System.out.println("4. Show Total Portfolio Value");
            System.out.println("5. Simulate Market Change");
            System.out.println("6. Exit");

            System.out.print("Your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    buyStock(user);
                    break;
                case "2":
                    sellStock(user);
                    break;
                case "3":
                    user.showPortfolio(market);
                    break;
                case "4":
                    System.out.printf("Total Portfolio Value: $%.2f\n", user.getTotalValue(market));
                    break;
                case "5":
                    simulateMarketChange();
                    break;
                case "6":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }

        System.out.println("Thanks for using the simulator!");
    }

    static void displayMarket() {
        System.out.println("\n--- Market Data ---");
        for (Stock stock : market.values()) {
            System.out.printf("%s: $%.2f\n", stock.symbol, stock.price);
        }
    }

    static void buyStock(User user) {
        System.out.print("Enter stock symbol to buy: ");
        String symbol = scanner.nextLine().toUpperCase();

        Stock stock = market.get(symbol);
        if (stock == null) {
            System.out.println("Invalid stock symbol.");
            return;
        }

        System.out.print("Enter quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        user.buyStock(stock, quantity);
    }

    static void sellStock(User user) {
        System.out.print("Enter stock symbol to sell: ");
        String symbol = scanner.nextLine().toUpperCase();

        Stock stock = market.get(symbol);
        if (stock == null) {
            System.out.println("Invalid stock symbol.");
            return;
        }

        System.out.print("Enter quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        user.sellStock(stock, quantity);
    }

    static void simulateMarketChange() {
        Random random = new Random();
        for (Stock stock : market.values()) {
            double changePercent = (random.nextDouble() - 0.5) * 0.1; // -5% to +5%
            double newPrice = stock.price * (1 + changePercent);
            stock.updatePrice(Math.round(newPrice * 100.0) / 100.0);
        }
        System.out.println("Market prices have changed!");
    }
}
