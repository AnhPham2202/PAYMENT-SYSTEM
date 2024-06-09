package anh.pham.adapters.inbound.request;

import anh.pham.ports.inbound.UserInboundPorts;

import java.util.Scanner;

public class UserRequestAdapter {
    private final UserInboundPorts userInput;

    public UserRequestAdapter(UserInboundPorts userInput) {
        this.userInput = userInput;
    }

    public void addFund() {
        System.out.println("Enter amount: ");
        Scanner scanner = new Scanner(System.in);
        int amount = scanner.nextInt();

        System.out.println("Your current available balance: " + userInput.addFund(amount).getBalance());
    }

}
