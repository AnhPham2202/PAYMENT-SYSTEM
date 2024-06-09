package anh.pham;

import anh.pham.adapters.inbound.request.BillRequestAdapter;
import anh.pham.adapters.inbound.request.PaymentHistoryRequestAdapter;
import anh.pham.adapters.inbound.request.UserRequestAdapter;
import anh.pham.adapters.outbound.persistence.BillPersistenceAdapter;
import anh.pham.adapters.outbound.persistence.PaymentHistoryPersistenceAdapter;
import anh.pham.adapters.outbound.persistence.UserPersistenceAdapter;
import anh.pham.adapters.outbound.repository.BillRepository;
import anh.pham.adapters.outbound.repository.PaymentHistoryRepository;
import anh.pham.adapters.outbound.repository.UserRepository;
import anh.pham.domain.service.BillService;
import anh.pham.domain.service.PaymentHistoryService;
import anh.pham.domain.service.UserService;
import anh.pham.exception.BadRequestException;
import anh.pham.exception.InvalidInformation;
import anh.pham.ports.inbound.PaymentHistoryInboundPorts;
import anh.pham.ports.outbound.PaymentHistoryOutboundPorts;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    /**
     *  INJECTION
     */
    private static final UserRepository USER_REPOSITORY = new UserRepository();
    private static final BillRepository BILL_REPOSITORY = new BillRepository();
    private static final PaymentHistoryRepository PAYMENT_HISTORY_REPOSITORY = new PaymentHistoryRepository();
    private static final UserPersistenceAdapter USER_PERSISTENCE_ADAPTER = new UserPersistenceAdapter(USER_REPOSITORY);
    private static final UserService USER_SERVICE = new UserService(USER_PERSISTENCE_ADAPTER);
    private static final BillPersistenceAdapter BILL_PERSISTENCE_ADAPTER = new BillPersistenceAdapter(BILL_REPOSITORY, USER_SERVICE);

    private static final BillService BILL_SERVICE = new BillService(BILL_PERSISTENCE_ADAPTER);
    private static final PaymentHistoryOutboundPorts PAYMENT_HISTORY_PERSISTENCE_ADAPTER = new PaymentHistoryPersistenceAdapter(PAYMENT_HISTORY_REPOSITORY);
    private static final PaymentHistoryInboundPorts PAYMENT_HISTORY_SERVICE = new PaymentHistoryService(PAYMENT_HISTORY_PERSISTENCE_ADAPTER);
    private static final UserRequestAdapter USER_REQUEST_ADAPTER = new UserRequestAdapter(USER_SERVICE);
    private static final PaymentHistoryRequestAdapter PAYMENT_HISTORY_REQUEST_ADAPTER = new PaymentHistoryRequestAdapter(PAYMENT_HISTORY_SERVICE);
    private static final BillRequestAdapter BILL_REQUEST_ADAPTER = new BillRequestAdapter(BILL_SERVICE, USER_SERVICE, PAYMENT_HISTORY_SERVICE);


    public static void main(String[] args) {
        printHeader();
        System.out.println("TO BE SIMPLIFY, YOU ARE USER WITH ID: 1, NAME: NANA, CURRENT BALANCE IS 100_000");

        while (true) {
            printGuide();

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    try {
                        USER_REQUEST_ADAPTER.addFund();
                    } catch (InvalidInformation e) {
                        System.out.println(e.getMessage());
                    } catch (InputMismatchException e) {
                        System.out.println("Amount should be an integer !!!");
                    }
                    break;
                case 2:
                    BILL_REQUEST_ADAPTER.viewAllBills();
                    break;
                case 3:
                    try {
                        BILL_REQUEST_ADAPTER.updateBills();
                    } catch (InvalidInformation | BadRequestException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        BILL_REQUEST_ADAPTER.addBill();
                    } catch (InvalidInformation e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    try {
                        BILL_REQUEST_ADAPTER.deleteBills();
                    } catch (InvalidInformation e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 6:
                    PAYMENT_HISTORY_REQUEST_ADAPTER.viewAllPayments();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Please choose the correct action you want to do !!!");
                    break;
            }



        }
    }

    private static void printGuide() {

        System.out.println("1. Add fund");
        System.out.println("2. View bills");
        System.out.println("3. Pay bills");
        System.out.println("4. Add bills");
        System.out.println("5. Delete bills");
        System.out.println("6. View Payments");
        System.out.println("7. EXIT");
    }
    private static void printHeader() {
        System.out.println("SOME CONSTRAINTS/RESTRICTIONS IN THIS APPLICATION BUT I DID TRY MY BEST ALL THE PART FROM DESIGNING ARCHITECTURE, CODDING AND UT:");
        System.out.println("1. MISSING SCHEDULED FEATURE");
        System.out.println("2. MISSING SEARCH BY PROVIDER");
        System.out.println();
        System.out.println("THIS APPLICATION I CHOSE HEXAGONAL ARCHITECTURE, IT MAY HAS MORE LAYERS THAN OTHER ARCHITECTURE BUT HERE THE REASONS: ");
        System.out.println("1. IT SEPARATES EVERY PART OF THE APPLICATION INTO SMALLER INDEPENDENT PARTS. YOU HAVE NO WORRIES WHEN YOU CHANGE YOUR CODE IN ONE PART AND IT MAY DESTROYS ALL THE THING");
        System.out.println("2. AS A PAYMENT APPLICATION, MAINTENANCE NEEDS TO BE CAREFUL. THIS ARCHITECTURE WORKS LIKE A CHAMP WHEN YOU SOURCE CODE GET BIGGER");
        System.out.println("3. AS A PAYMENT APPLICATION, SOMETIMES BIG ISSUES NEED TO CHANGE DATABASE OR TYPES OF DATABASE CAUSE SOME BIG PROBLEMS. THIS ARCHITECTURE HELPS US KEEP THIS SIMPLE. IT WORKS THROUGH PORT. UNLESS YOU CHANGE YOUR PORTS, NOTHING IS A BIG DEAL");
        System.out.println("ENJOY :)");
        System.out.println(".................................................................................................................................................................................................................................................");

    }
}