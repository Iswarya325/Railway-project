import java.util.*;

public class Main{

    // ======================= DATA MODELS =======================

    static class Train {
        String trainNo, name, source, destination, departure, arrival;
        int totalSeats, availableSeats;
        double fare;

        Train(String no, String name, String src, String dest, String dep, String arr, int seats, double fare) {
            this.trainNo = no;
             this.name = name;
              this.source = src;
            this.destination = dest; 
            this.departure = dep; 
            this.arrival = arr;
            this.totalSeats = seats; 
            this.availableSeats = seats; this.fare = fare;
        }
    }

    static class Booking {
        String pnr, trainNo, passengerName, date, coachType, seatNo, status;
        double fare;
        int waitingListNo = 0;

        Booking(String pnr, String trainNo, String name, String date, String coach, String seat, double fare) {
            this.pnr = pnr; this.trainNo = trainNo; this.passengerName = name;
            this.date = date; this.coachType = coach; this.seatNo = seat;
            this.fare = fare; this.status = "CONFIRMED";
        }
    }

    static class User {
        String name, email, password, mobile;
        List<String> bookingHistory = new ArrayList<>();

        User(String name, String email, String pass, String mobile) {
            this.name = name; this.email = email; this.password = pass; this.mobile = mobile;
        }
    }

    static class Complaint {
        String id, pnr, issue, status;
        Complaint(String id, String pnr, String issue) {
            this.id = id; this.pnr = pnr; this.issue = issue; this.status = "OPEN";
        }
    }

    // ======================= DATA STORE =======================

    static Map<String, Train> trains = new HashMap<>();
    static Map<String, Booking> bookings = new HashMap<>();
    static Map<String, User> users = new HashMap<>();
    static Map<String, Complaint> complaints = new HashMap<>();
    static List<String> waitingList = new ArrayList<>();
    static User currentUser = null;
    static int pnrCounter = 1000;
    static int complaintCounter = 100;
    static Scanner sc = new Scanner(System.in);

    // ======================= INITIALIZATION =======================
static void initData() {

    // North & Long Distance Trains
    trains.put("12301", new Train("12301", "Rajdhani Express", "Delhi", "Mumbai", "16:55", "08:35", 100, 1500.0));
    trains.put("12951", new Train("12951", "Mumbai Mail", "Mumbai", "Delhi", "23:00", "15:55", 80, 1200.0));
    trains.put("22691", new Train("22691", "Karnataka Express", "Delhi", "Bangalore", "20:30", "06:00", 120, 1800.0));
    trains.put("12621", new Train("12621", "Tamil Nadu Express", "Delhi", "Chennai", "22:00", "07:45", 90, 1650.0));
    trains.put("12839", new Train("12839", "Howrah Mail", "Chennai", "Kolkata", "23:45", "13:30", 110, 1400.0));
    trains.put("11001", new Train("11001", "Deccan Express", "Mumbai", "Pune", "07:15", "10:10", 200, 300.0));

    // Tamil Nadu Trains
    trains.put("16127", new Train("16127", "Guruvayur Express", "Chennai", "Nagercoil", "21:40", "10:15", 120, 650.0));
    trains.put("12631", new Train("12631", "Nellai Express", "Chennai", "Tirunelveli", "19:50", "07:35", 150, 700.0));
    trains.put("12633", new Train("12633", "Kanyakumari Express", "Chennai", "Kanyakumari", "17:20", "06:00", 150, 750.0));
    trains.put("12637", new Train("12637", "Pandian Express", "Chennai", "Madurai", "21:40", "05:40", 140, 550.0));
    trains.put("12635", new Train("12635", "Vaigai Express", "Chennai", "Madurai", "13:50", "21:15", 120, 500.0));
    trains.put("12673", new Train("12673", "Cheran Express", "Chennai", "Coimbatore", "22:15", "06:55", 140, 600.0));
    trains.put("12671", new Train("12671", "Nilgiri Express", "Chennai", "Coimbatore", "21:05", "06:15", 120, 620.0));
    trains.put("12653", new Train("12653", "Rockfort Express", "Chennai", "Tiruchirappalli", "23:30", "05:45", 120, 450.0));
    trains.put("22675", new Train("22675", "Chozhan Express", "Chennai", "Tiruchirappalli", "07:50", "13:45", 110, 400.0));
    trains.put("16865", new Train("16865", "Uzhavan Express", "Chennai", "Thanjavur", "22:30", "06:10", 100, 500.0));
    trains.put("16101", new Train("16101", "Rameswaram Express", "Chennai", "Rameswaram", "17:45", "06:30", 130, 700.0));
    trains.put("16105", new Train("16105", "Tiruchendur Express", "Chennai", "Tiruchendur", "16:05", "07:45", 120, 750.0));
    trains.put("16351", new Train("16351", "Nagercoil Express", "Chennai", "Nagercoil", "18:25", "07:50", 120, 700.0));

    // Intercity Tamil Nadu Trains
    trains.put("20683", new Train("20683", "Coimbatore Intercity", "Chennai", "Coimbatore", "15:15", "22:00", 150, 550.0));
    trains.put("22671", new Train("22671", "Tejas Express", "Chennai", "Madurai", "06:00", "12:30", 100, 950.0));
    trains.put("12605", new Train("12605", "Pallavan Express", "Chennai", "Karaikudi", "15:45", "23:15", 120, 500.0));

    // Demo User
    users.put("demo@rail.in",
            new User("Rahul Kumar", "demo@rail.in", "pass123", "9876543210"));
}

    // ======================= UTILITY =======================

    static void line() { System.out.println("=".repeat(65)); }
    static void dash() { System.out.println("-".repeat(65)); }
    static void pause() {
        System.out.print("\nPress Enter to continue...");
        sc.nextLine();
    }
    static String genPNR() { return "PNR" + (++pnrCounter); }
    static String genComplaintId() { return "CMP" + (++complaintCounter); }

    static void header(String title) {
        System.out.println();
        line();
        System.out.printf("    RAILWAY RESERVATION SYSTEM  |  %s%n", title);
        line();
    }

    // ======================= AUTH =======================

    static void loginRegisterMenu() {
        while (true) {
            header("WELCOME");
            System.out.println("  1. Login");
            System.out.println("  2. Register");
            System.out.println("  3. Continue as Guest");
            System.out.println("  0. Exit");
            dash();
            System.out.print("  Choose: ");
            String ch = sc.nextLine().trim();
            switch (ch) {
                case "1": login(); break;
                case "2": register(); break;
                case "3": mainMenu(); break;
                case "0": System.out.println("\n  Thank you for using Railway Reservation System!\n"); System.exit(0);
                default: System.out.println("  Invalid choice!");
            }
        }
    }

    static void login() {
        header("USER LOGIN");
        System.out.print("  Email   : ");
        String email = sc.nextLine().trim();
        System.out.print("  Password: ");
        String pass = sc.nextLine().trim();
        User u = users.get(email);
        if (u != null && u.password.equals(pass)) {
            currentUser = u;
            System.out.println("\n   Login successful! Welcome, " + u.name);
            pause();
            mainMenu();
        } else {
            System.out.println("\n   Invalid credentials. Try again.");
            pause();
        }
    }

    static void register() {
        header("USER REGISTRATION");
        System.out.print("  Full Name : ");
        String name = sc.nextLine().trim();
        System.out.print("  Email     : ");
        String email = sc.nextLine().trim();
        if (users.containsKey(email)) {
            System.out.println("  Email already registered!"); pause(); return;
        }
        System.out.print("  Password  : ");
        String pass = sc.nextLine().trim();
        System.out.print("  Mobile    : ");
        String mobile = sc.nextLine().trim();
        users.put(email, new User(name, email, pass, mobile));
        System.out.println("\n   Registration successful! You can now login.");
        pause();
    }

    // ======================= MAIN MENU =======================

    
               static void mainMenu() {
    while (true) {
        header("MAIN MENU");

        System.out.println("  1. Train Search              2. PNR Status");
        System.out.println("  3. Coach Position            4. Track Train");
        System.out.println("  5. Order Food                6. Refund Management");
        System.out.println("  7. Ticket Booking            8. Ticket Cancellation");
        System.out.println("  9. Waiting List              10. Unreserved Ticket");
        System.out.println("  11. Platform Ticket");

        if (currentUser != null) {
            System.out.println("  12. Booking History          13. My Profile");
            System.out.println("  14. Seat Availability        15. Logout");
        } else {
            System.out.println("   12. Seat Availability");
            System.out.println("   13. Login/Register");
        }

        System.out.println("  0. Exit");
        dash();
        System.out.print("  Choose: ");

        String ch = sc.nextLine().trim();

        switch (ch) {
            case "1": searchTrains(); break;
            case "2": checkPNR(); break;
            case "3": coachPosition(); break;
            case "4": trackTrain(); break;
            case "5": orderFood(); break;
            case "6": refundManagement(); break;
            case "7": bookTicket(); break;
            case "8": cancelTicket(); break;
            case "9": waitingListStatus(); break;
            case "10": unreservedTicket(); break;
            case "11": platformTicket(); break;

            case "12":
                if (currentUser != null)
                    bookingHistory();
                else
                    seatAvailability();
                break;

            case "13":
                if (currentUser != null)
                    myProfile();
                else
                    loginRegisterMenu();
                break;

            case "14":
                if (currentUser != null)
                    seatAvailability();
                break;

            case "15":
                if (currentUser != null) {
                    currentUser = null;
                    System.out.println("  Logged out!");
                    pause();
                    return;
                }
                break;

            case "0":
                System.out.println("\n  Thank you for using Railway Reservation System! 🚂\n");
                System.exit(0);
                break;

            default:
                System.out.println("   Invalid choice!");
        }
    }
}
    // ======================= 1. TRAIN SEARCH =======================

    static void searchTrains() {
        header("TRAIN SEARCH");
        System.out.print("  From (source)     : ");
        String src = sc.nextLine().trim();
        System.out.print("  To   (destination): ");
        String dest = sc.nextLine().trim();
        System.out.print("  Date (DD-MM-YYYY) : ");
        String date = sc.nextLine().trim();

        System.out.println();
        boolean found = false;
        System.out.printf("  %-8s %-22s %-10s %-10s %-8s %-8s%n",
                "Train No", "Train Name", "Departure", "Arrival", "Seats", "Fare(₹)");
        dash();

        for (Train t : trains.values()) {
            if (t.source.equalsIgnoreCase(src) && t.destination.equalsIgnoreCase(dest)) {
                System.out.printf("  %-8s %-22s %-10s %-10s %-8d %-8.0f%n",
                        t.trainNo, t.name, t.departure, t.arrival, t.availableSeats, t.fare);
                found = true;
            }
        }

        if (!found) {
            System.out.println("\n   No trains found for this route on " + date);
            System.out.println("   Try: Delhi→Mumbai, Mumbai→Delhi, Delhi→Bangalore, Delhi→Chennai");
        }
        pause();
    }

    // ======================= 2. PNR STATUS =======================

    static void checkPNR() {
        header("PNR STATUS");
        System.out.print("  Enter PNR Number: ");
        String pnr = sc.nextLine().trim().toUpperCase();
        Booking b = bookings.get(pnr);
        if (b == null) {
            System.out.println("\n   PNR not found! Please check the number.");
        } else {
            System.out.println("\n  PNR Number   : " + b.pnr);
            System.out.println("  Train Number : " + b.trainNo);
            Train t = trains.get(b.trainNo);
            if (t != null) System.out.println("  Train Name   : " + t.name);
            System.out.println("  Passenger    : " + b.passengerName);
            System.out.println("  Travel Date  : " + b.date);
            System.out.println("  Coach        : " + b.coachType);
            System.out.println("  Seat No      : " + b.seatNo);
            System.out.println("  Fare Paid    : " + b.fare);
            System.out.println();
            if (b.status.equals("CONFIRMED")) System.out.println("   Status     : CONFIRMED");
            else if (b.status.equals("RAC"))  System.out.println("   Status     : RAC (Reservation Against Cancellation)");
            else if (b.status.startsWith("WL")) System.out.println("   Status     : " + b.status + " (Waiting List)");
            else System.out.println("  Status       : " + b.status);
        }
        pause();
    }

    // ======================= 3. COACH POSITION =======================

    static void coachPosition() {
        header("COACH POSITION");
        System.out.print("  Enter Train Number: ");
        String tn = sc.nextLine().trim();
        Train t = trains.get(tn);
        if (t == null) 
            { 
                System.out.println("   Train not found!");
                 pause();
                  return;
                 }

        System.out.println("\n  Train: " + t.name + " (" + tn + ")");
        System.out.println("  Platform Arrangement (Engine → Rear):");
        System.out.println();
        System.out.println("  [ENGINE] → [GEN-1] → [GEN-2] → [SL-1] → [SL-2] → [SL-3]");
        System.out.println("          → [3A-1] → [3A-2] → [2A-1] → [2A-2] → [1A] → [PAN-1]");
        System.out.println();
        System.out.println("  Coach Key:");
        System.out.println("  GEN = General | SL = Sleeper | 3A = AC 3-Tier");
        System.out.println("  2A  = AC 2-Tier | 1A = First AC | PAN = Pantry Car");
        System.out.println();
        System.out.println("  Tip: Check the digital display boards at the platform entry.");
        pause();
    }

    // ======================= 4. TRACK TRAIN =======================

    static void trackTrain() {
        header("TRACK YOUR TRAIN");
        System.out.print("  Enter Train Number: ");
        String tn = sc.nextLine().trim();
        Train t = trains.get(tn);
        if (t == null) { System.out.println("   Train not found!"); pause(); return; }

        String[] stations = {t.source, "Station B", "Station C", "Station D", t.destination};
        String[] times    = {"16:55", "19:30", "21:45", "00:10", t.arrival};
        String[] status   = {"Departed", "Departed", "At Platform", "Pending", "Pending"};

        System.out.println("\n  Train: " + t.name + "  |  Current Status: RUNNING (On Time)");
        System.out.println();
        System.out.printf("  %-20s %-12s %-15s%n", "Station", "Time", "Status");
        dash();
        for (int i = 0; i < stations.length; i++) {
            String icon = status[i].equals("At Platform") ? "[AT] " : (status[i].equals("Departed") ? "[OK] " : "[PENDING] ");
            System.out.printf("  %-20s %-12s %s%-15s%n", stations[i], times[i], icon, status[i]);
        }
        System.out.println("\n   LIVE: Train is currently at Station C  |  Delay: ON TIME");
        pause();
    }

    // ======================= 5. ORDER FOOD =======================

    static void orderFood() {
        header("ORDER FOOD");
        System.out.print("  Enter your PNR Number: ");
        String pnr = sc.nextLine().trim().toUpperCase();

        if (!bookings.containsKey(pnr)) {
            System.out.println("   PNR not found!"); pause(); return;
        }

        System.out.println("\n  Available Restaurants at Next Station:");
        System.out.println("  1.  Punjabi Dhaba     - Dal Makhani ₹120 | Paneer ₹150 | Roti ₹40");
        System.out.println("  2.  South Spice       - Idli ₹60 | Dosa ₹80 | Rice Meals ₹130");
        System.out.println("  3.  Fast & Furious     - Burger ₹90 | Sandwich ₹70 | Cold Drink ₹40");
        System.out.println("  4.  Railway Pantry     - Veg Thali ₹100 | Non-Veg Thali ₹130");
        System.out.print("\n  Select Restaurant (1-4): ");
        String rest = sc.nextLine().trim();

        System.out.print("  Enter item and quantity (e.g., Dal Makhani x2): ");
        String item = sc.nextLine().trim();

        System.out.println("\n  Order Placed Successfully!");
        System.out.println("  Order ID  : ORD" + (1000 + new Random().nextInt(999)));
        System.out.println("  Item      : " + item);
        System.out.println("  Delivery  : Seat " + bookings.get(pnr).seatNo);
        System.out.println("  Est. Time : 25-35 minutes");
        System.out.println("  Payment   : Cash on delivery");
        pause();
    }

    // ======================= 6. REFUND MANAGEMENT =======================

    static void refundManagement() {
        header("REFUND MANAGEMENT");
        System.out.print("  Enter Cancelled PNR: ");
        String pnr = sc.nextLine().trim().toUpperCase();
        Booking b = bookings.get(pnr);

        if (b == null || !b.status.equals("CANCELLED")) {
            System.out.println("   No cancelled booking found for this PNR.");
            System.out.println("   Cancel a ticket first to see refund status.");
            pause(); return;
        }

        double refund = b.fare * 0.75;
        System.out.println("\n  Refund Details:");
        System.out.println("  PNR          : " + pnr);
        System.out.println("  Passenger    : " + b.passengerName);
        System.out.println("  Ticket Fare  : " + b.fare);
        System.out.println("  Cancellation : " + (b.fare - refund));
        System.out.println("  Refund Amount: " + refund);
        System.out.println("  Refund Status:  PROCESSED");
        System.out.println("  Credit Date  : Within 5-7 working days to original payment method");
        pause();
    }

    
    // ======================= 7. TICKET BOOKING =======================

    static void bookTicket() {
        header("TICKET BOOKING");
        System.out.print("  Train Number   : ");
        String tn = sc.nextLine().trim();
        Train t = trains.get(tn);
        if (t == null) { System.out.println("   Train not found!"); pause(); return; }

        System.out.println("  Train: " + t.name + "  |  " + t.source + " → " + t.destination);
        System.out.println("  Available Seats: " + t.availableSeats + "  |  Base Fare: ₹" + t.fare);
        System.out.println();
        System.out.print("  Passenger Name : ");
        String name = sc.nextLine().trim();
        System.out.print("  Travel Date    : ");
        String date = sc.nextLine().trim();
        System.out.println("  Coach Type     : 1-General(" + (t.fare*0.3) + ")  2-Sleeper(" + (t.fare*0.6) + ")  3-AC 3A(" + t.fare + ")  4-AC 2A(" + (t.fare*1.5) + ")  5-First AC(" + (t.fare*2) + ")");
        System.out.print("  Coach Choice   : ");
        String cc = sc.nextLine().trim();

        String[] coaches = {"", "General", "Sleeper", "AC 3-Tier", "AC 2-Tier", "First AC"};
        double[] fareMultiplier = {0, 0.3, 0.6, 1.0, 1.5, 2.0};
        int ci = 3; try { ci = Integer.parseInt(cc); if (ci < 1 || ci > 5) ci = 3; } catch (Exception e) {}

        double fare = t.fare * fareMultiplier[ci];
        String pnr = genPNR();
        String seatNo, status;

        if (t.availableSeats > 0) {
            t.availableSeats--;
            int seat = t.totalSeats - t.availableSeats;
            seatNo = coaches[ci].charAt(0) + "" + seat;
            status = "CONFIRMED";
        } else {
            int wlNo = waitingList.size() + 1;
            seatNo = "WL";
            status = "WL/" + wlNo;
            waitingList.add(pnr);
        }

        Booking b = new Booking(pnr, tn, name, date, coaches[ci], seatNo, fare);
        b.status = status;
        bookings.put(pnr, b);

        if (currentUser != null) currentUser.bookingHistory.add(pnr);

        System.out.println();
        line();
        System.out.println("    BOOKING CONFIRMATION");
        line();
        System.out.println("  PNR Number   : " + pnr);
        System.out.println("  Train        : " + t.name + " (" + tn + ")");
        System.out.println("  Route        : " + t.source + " → " + t.destination);
        System.out.println("  Passenger    : " + name);
        System.out.println("  Date         : " + date);
        System.out.println("  Coach        : " + coaches[ci]);
        System.out.println("  Seat No      : " + seatNo);
        System.out.println("  Fare Paid    : " + fare);
        System.out.println("  Status       : " + (status.equals("CONFIRMED") ? " CONFIRMED" :  status));
        pause();
    }

    // ======================= 8. TICKET CANCELLATION =======================

    static void cancelTicket() {
        header("TICKET CANCELLATION");
        System.out.print("  Enter PNR Number: ");
        String pnr = sc.nextLine().trim().toUpperCase();
        Booking b = bookings.get(pnr);

        if (b == null) { System.out.println("   PNR not found!"); pause(); return; }
        if (b.status.equals("CANCELLED")) { System.out.println("    Ticket already cancelled!"); pause(); return; }

        System.out.println("\n  Booking: " + b.passengerName + " | " + b.trainNo + " | ₹" + b.fare);
        System.out.print("  Confirm cancellation? (yes/no): ");
        if (!sc.nextLine().trim().equalsIgnoreCase("yes")) { System.out.println("  Cancellation aborted."); pause(); return; }

        // Upgrade waiting list
        Train t = trains.get(b.trainNo);
        if (t != null && b.status.equals("CONFIRMED")) {
            t.availableSeats++;
            if (!waitingList.isEmpty()) {
                String upgradePNR = waitingList.remove(0);
                Booking wb = bookings.get(upgradePNR);
                if (wb != null) {
                    wb.status = "CONFIRMED";
                    wb.seatNo = b.seatNo;
                    System.out.println("  WL Passenger " + wb.passengerName + " (PNR: " + upgradePNR + ") has been CONFIRMED!");
                }
            }
        }

        double refund = b.fare * 0.75;
        b.status = "CANCELLED";

        System.out.println("\n  Ticket Cancelled Successfully!");
        System.out.println("  Refund    : " + refund + " (within 5-7 working days)");
        pause();
    }
    // ======================= 9. WAITING LIST =======================

    static void waitingListStatus() {
        header("WAITING LIST STATUS");
        System.out.println("  Current Waiting List:");
        if (waitingList.isEmpty()) {
            System.out.println("  No passengers in waiting list.");
        } else {
            System.out.printf("  %-5s %-12s %-20s %-10s%n", "WL#", "PNR", "Passenger", "Train");
            dash();
            for (int i = 0; i < waitingList.size(); i++) {
                Booking b = bookings.get(waitingList.get(i));
                if (b != null)
                    System.out.printf("  %-5d %-12s %-20s %-10s%n", i+1, b.pnr, b.passengerName, b.trainNo);
            }
        }
        pause();
    }

    

    // ======================= 10. UNRESERVED TICKET =======================

    static void unreservedTicket() {
        header("UNRESERVED TICKET BOOKING");
        System.out.print("  From  : ");
        String src = sc.nextLine().trim();
        System.out.print("  To    : ");
        String dest = sc.nextLine().trim();
        System.out.print("  Date  : ");
        String date = sc.nextLine().trim();
        System.out.print("  Count : ");
        int count = 1; try { count = Integer.parseInt(sc.nextLine().trim()); } catch (Exception e) {}

        double fare = 50 + count * 20;
        String tktId = "UNR" + (1000 + new Random().nextInt(9000));

        System.out.println("\n   Unreserved Ticket Generated!");
        System.out.println("  Ticket ID: " + tktId);
        System.out.println("  Route    : " + src + " → " + dest);
        System.out.println("  Date     : " + date);
        System.out.println("  Tickets  : " + count);
        System.out.println("  Total    : " + fare);
        System.out.println("  Valid for General Coaches only.");
        pause();
    }

    // ======================= 11. PLATFORM TICKET =======================

    static void platformTicket() {
        header("PLATFORM TICKET");
        System.out.print("  Station Name : ");
        String stn = sc.nextLine().trim();
        System.out.print("  No. of Tickets: ");
        int cnt = 1; try { cnt = Integer.parseInt(sc.nextLine().trim()); } catch (Exception e) {}

        String pid = "PLT" + (100 + new Random().nextInt(900));
        System.out.println("\n   Platform Ticket Booked!");
        System.out.println("  Ticket ID : " + pid);
        System.out.println("  Station   : " + stn);
        System.out.println("  Count     : " + cnt);
        System.out.println("  Total     : " + (10 * cnt));
        System.out.println("  Valid for : 2 hours from time of entry");
        pause();
    }

    
    // ======================= 12. BOOKING HISTORY =======================

    static void bookingHistory() {
        header("MY BOOKING HISTORY");
        if (currentUser == null) { System.out.println("  Please login first!"); pause(); return; }

        if (currentUser.bookingHistory.isEmpty()) {
            System.out.println("  No bookings found.");
        } else {
            System.out.printf("  %-12s %-12s %-20s %-12s %-10s%n", "PNR", "Train", "Passenger", "Date", "Status");
            dash();
            for (String pnr : currentUser.bookingHistory) {
                Booking b = bookings.get(pnr);
                if (b != null)
                    System.out.printf("  %-12s %-12s %-20s %-12s %-10s%n",
                            b.pnr, b.trainNo, b.passengerName, b.date, b.status);
            }
        }
        pause();
    }

    // ======================= 13. MY PROFILE =======================

    static void myProfile() {
        header("MY PROFILE");
        if (currentUser == null) { System.out.println("  Please login first!"); pause(); return; }
        System.out.println("  Name    : " + currentUser.name);
        System.out.println("  Email   : " + currentUser.email);
        System.out.println("  Mobile  : " + currentUser.mobile);
        System.out.println("  Bookings: " + currentUser.bookingHistory.size());
        pause();
    }

    // ======================= 17. SEAT AVAILABILITY =======================

    static void seatAvailability() {
        header("SEAT AVAILABILITY CHECK");
        System.out.print("  Enter Train Number: ");
        String tn = sc.nextLine().trim();
        Train t = trains.get(tn);
        if (t == null) { System.out.println("   Train not found!"); pause(); return; }
        System.out.println("\n  " + t.name + "  |  " + t.source + " → " + t.destination);
        System.out.println("  Available Seats : " + t.availableSeats + " / " + t.totalSeats);
        if (t.availableSeats == 0) System.out.println("    No seats available. Waiting list applies.");
        else System.out.println("   Seats are available!");
        pause();
    }

    

    // ======================= MAIN =======================

    public static void main(String[] args) {
        System.out.println();
        line();
        System.out.println("       WELCOME TO RAILWAY RESERVATION SYSTEM  ");
        System.out.println("         India's Smart Train Booking Platform");
        line();
        System.out.println("  Demo Login: Email: demo@rail.in | Password: pass123");
        line();
        initData();
        loginRegisterMenu();
    }
}